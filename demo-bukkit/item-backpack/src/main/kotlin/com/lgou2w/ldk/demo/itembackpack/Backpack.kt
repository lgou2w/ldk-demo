/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lgou2w.ldk.demo.itembackpack

import com.lgou2w.ldk.bukkit.item.ItemFactory
import com.lgou2w.ldk.bukkit.item.builder
import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.ofCompound
import com.lgou2w.ldk.nbt.ofList
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import java.util.UUID

class Backpack(
        val id: UUID
) : InventoryHolder {

    private val container : Inventory by lazy { Bukkit.createInventory(this@Backpack, 6 * 9, "Backpack") }
    override fun getInventory(): Inventory {
        return container
    }

    fun open(human: HumanEntity) {
        human.openInventory(inventory)
    }

    companion object {

        val MATERIAL = Material.LEATHER
        val NAME = ChatColor.GOLD + "Backpack"

        const val KEY = "ldk-demo-itembackpack"
        const val KEY_ID = "id"
        const val KEY_ITEMS = "Items"
        const val KEY_ITEM_SLOT = "Slot"

        @JvmStatic
        fun get(inventory: Inventory?) : Backpack? {
            return inventory?.holder as? Backpack
        }

        @JvmStatic
        fun read(stack: ItemStack?) : Backpack? {
            if (stack == null || !isBackpack(stack))
                return null
            val data = ItemFactory.readTagSafe(stack).getCompound(KEY)
            val id = UUID.fromString(data.getString(KEY_ID))
            val items = data.getListOrNull(KEY_ITEMS)
            val backpack = Backpack(id)
            if (items != null && items.isNotEmpty()) {
                items.asElements<NBTTagCompound>().forEach { item ->
                    val slot = item.getByte(KEY_ITEM_SLOT).toInt()
                    val itemStack = ItemFactory.createItem(item)
                    backpack.inventory.setItem(slot, itemStack)
                }
            }
            return backpack
        }

        @JvmStatic
        fun save(backpack: Backpack, stack: ItemStack?) : ItemStack {
            if (stack != null && !isBackpack(stack))
                throw IllegalArgumentException("The stack of items saved to is not a backpack item.")
            val item = stack?.clone() ?: MATERIAL.builder().setDisplayName(ChatSerializer.fromRaw(NAME)).build()
            val data = ItemFactory.readTagSafe(item)
            data[KEY] = ofCompound {
                putString(KEY_ID, backpack.id.toString())
                put(KEY_ITEMS, ofList {
                    for (index in 0 until backpack.inventory.size) {
                        val containerStack = backpack.inventory.getItem(index)
                        if (containerStack != null && containerStack.type != Material.AIR) {
                            val compound = ItemFactory.readItem(containerStack)
                            compound.putByte(KEY_ITEM_SLOT, index)
                            addCompound(compound)
                        }
                    }
                })
            }
            return ItemFactory.writeTag(item, data)
        }

        @JvmStatic
        fun create() : ItemStack {
            val builder = MATERIAL.builder().setDisplayName(ChatSerializer.fromRaw(NAME))
            builder.tag[KEY] = ofCompound { putString(KEY_ID, UUID.randomUUID().toString()) }
            return builder.build()
        }

        @JvmStatic
        fun isBackpack(stack: ItemStack?) : Boolean {
            return stack?.type == MATERIAL &&
                   stack.itemMeta.notNull().displayName == NAME &&
                   ItemFactory.readTag(stack)?.containsKey(KEY) == true
        }
    }
}
