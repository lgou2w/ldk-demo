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

import com.lgou2w.ldk.bukkit.gui.ButtonEvent
import com.lgou2w.ldk.bukkit.gui.GuiType
import com.lgou2w.ldk.bukkit.gui.PageableGui
import com.lgou2w.ldk.bukkit.item.Enchantment
import com.lgou2w.ldk.bukkit.item.builder
import com.lgou2w.ldk.bukkit.item.isNotAir
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.nbt.NBTStreams
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.ofCompound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import java.io.File

class ItemRepositories(private val plugin: Main) {

    private val directory = File(plugin.dataFolder, "data")
    private val repositories : MutableMap<String, ItemRepository> = HashMap()

    private fun getDataFile(name: String) : File {
        if (!directory.exists())
            directory.mkdirs()
        return File(directory, "$name.dat")
    }

    fun getRepository(name: String) : ItemRepository?
            = repositories[name]

    fun getOrLoadRepository(name: String) : ItemRepository
            = getRepository(name) ?: loadRepository(name) ?: createRepository(name)

    fun loadRepository(name: String) : ItemRepository? {
        val file = getDataFile(name)
        if (!file.exists())
            return null
        val data = NBTStreams.readFile(file) as NBTTagCompound
        val repository = ItemRepository(name)
        repository.load(data)
        repositories[name] = repository
        return repository
    }

    fun saveRepository(repository: ItemRepository) {
        val file = getDataFile(repository.owner)
        val data = ofCompound()
        repository.save(data)
        NBTStreams.writeFile(data, file)
    }

    fun createRepository(name: String) : ItemRepository {
        val repository = ItemRepository(name)
        repositories[name] = repository
        return repository
    }

    fun saveAndRemoveRepository(repository: ItemRepository) {
        saveRepository(repository)
        repositories.remove(repository.owner)
    }

    /**
     * 可取不可存, 存储只能通过 API 或命令方式.
     * 只能由 LMB 获取到物品, 之后自动 SORT 仓库物品.
     * 由 6 层组成, 上 5 层为仓库物品. 下一层为控制按钮, 例如翻页等等.
     */

    fun openRepository(repository: ItemRepository, viewer: Player) {
        val maxPage = Math.ceil(repository.items.size.toDouble() / 45.0).toInt()
        val root = PageableGui(GuiType.CHEST_6, "Item Repository" + if (maxPage > 1) " (1/$maxPage)" else "")
        var countPage = 1
        var current = root
        var slot = 0
        for ((index, item) in repository.items.withIndex()) {
            if (index != 0 && index % 45 == 0) {
                current.setButton(9, 6, Material.ARROW.builder(countPage + 1) {
                    setDisplayName(ChatSerializer.fromRaw("&6Next Page"))
                    addEnchantment(Enchantment.UNBREAKING, 1)
                    addFlag(ItemFlag.HIDE_ENCHANTS)
                }.build()).onClicked = PageableGui.nextPage()
                current = current.addPage(GuiType.CHEST_6, "Item Repository (${++countPage}/$maxPage)")
                current.setButton(1, 6, Material.ARROW.builder(countPage - 1) {
                    setDisplayName(ChatSerializer.fromRaw("&6Previous Page"))
                    addEnchantment(Enchantment.UNBREAKING, 1)
                    addFlag(ItemFlag.HIDE_ENCHANTS)
                }.build()).onClicked = PageableGui.previousPage()
                current.setProperty("page", countPage)
                current.setProperty("repository", repository.owner)
                current.isAllowMove = false
                slot = 0
            }
            current.setButton(slot++, item).onClicked = ButtonEvent.cancelThen { event ->
                val result = event.clicker.inventory.addItem(event.button.stack)
                val button = event.button
                val view = event.view
                val source = getRepository(view.getPropertyAs<String>("repository").notNull()).notNull()
                val page = view.getPropertyAs<Int>("page").notNull()
                val take = result.values.firstOrNull()
                val takeIndex = (page - 1) * 45 + button.index
                if (take != null && take.isNotAir()) {
                    source.items[takeIndex] = take.clone()
                } else {
                    source.items.removeAt(takeIndex)
                }
                if (result.isEmpty() && view.removeButton(button)) {
                    for (nextIdx in button.index + 1 until 45) {
                        val next = view.getButton(nextIdx) ?: continue
                        view.setButton(nextIdx - 1, next.stack, next.onClicked)
                        view.removeButton(nextIdx)
                    }
                }
            }
        }
        root.setProperty("page", 1)
        root.setProperty("repository", repository.owner)
        root.isAllowMove = false
        root.open(viewer)
    }
}
