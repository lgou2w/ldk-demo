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

import com.lgou2w.ldk.bukkit.PluginBase
import com.lgou2w.ldk.bukkit.cmd.CommandManager
import com.lgou2w.ldk.bukkit.cmd.DefaultCommandManager
import com.lgou2w.ldk.bukkit.event.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerQuitEvent

class Main : PluginBase(), EventListener {

    lateinit var commandManager : CommandManager
    lateinit var itemRepositories: ItemRepositories

    override fun load() {
    }

    override fun enable() {
        itemRepositories = ItemRepositories(this)
        commandManager = DefaultCommandManager(this)
        commandManager.registerCommand(ItemRepositoryCommand(this))
    }

    override fun disable() {
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val repository = itemRepositories.getRepository(event.player.name) ?: return
        try {
            itemRepositories.saveAndRemoveRepository(repository)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
