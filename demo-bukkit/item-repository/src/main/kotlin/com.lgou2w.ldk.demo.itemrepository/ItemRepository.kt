/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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

package com.lgou2w.ldk.demo.itemrepository

import com.lgou2w.ldk.bukkit.item.ItemFactory
import com.lgou2w.ldk.nbt.NBTReadable
import com.lgou2w.ldk.nbt.NBTSavable
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.ofCompound
import com.lgou2w.ldk.nbt.ofList
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemRepository(owner: String) : NBTReadable, NBTSavable {

    var owner : String = owner
        private set

    var items : MutableList<ItemStack> = ArrayList()
        private set

    override fun load(root: NBTTagCompound): NBTTagCompound {
        owner = root.getString("Owner")
        items = root.getList("Items").asElements<NBTTagCompound>().asSequence().map { item ->
            val type = Material.matchMaterial(item.getString("Id"))
            val count = item.getByte("Count").toInt()
            val tag = item.getCompoundOrNull("tag")
            ItemFactory.writeTag(ItemStack(type, count), tag)
        }.toMutableList()
        return root
    }

    override fun save(root: NBTTagCompound): NBTTagCompound {
        root.putString("Owner", owner)
        root.put(ofList("Items") {
            addCompound(*items.map { item ->
                ofCompound {
                    putString("Id", ItemFactory.materialType(item.type))
                    putByte("Count", item.amount)
                    val tag = ItemFactory.readTag(item)
                    if (tag != null)
                        put(tag)
                }
            }.toTypedArray())
        })
        return root
    }

    fun save(repositories: ItemRepositories) {
        repositories.saveRepository(this)
    }
}
