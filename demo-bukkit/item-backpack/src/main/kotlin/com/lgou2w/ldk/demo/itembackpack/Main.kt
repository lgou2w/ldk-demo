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

import com.lgou2w.ldk.bukkit.PluginBase
import com.lgou2w.ldk.bukkit.cmd.CommandManager
import com.lgou2w.ldk.bukkit.cmd.DefaultCommandManager
import com.lgou2w.ldk.bukkit.cmd.SimpleChineseCommandFeedback
import com.lgou2w.ldk.bukkit.entity.itemInMainHand
import com.lgou2w.ldk.bukkit.event.EventListener
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent

class Main : PluginBase(), EventListener {

    private var commandManager : CommandManager? = null

    override fun load() {
    }

    override fun enable() {
        commandManager = DefaultCommandManager(this).apply {
            transforms.addDefaultTransforms()
            completes.addDefaultCompletes()
            globalFeedback = SimpleChineseCommandFeedback()
            registerCommand(MainCommand())
        }
        server.pluginManager.registerEvents(this, this)
    }

    override fun disable() {
        commandManager?.unregisterCommands()
        commandManager = null
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val isRight = event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK
        if (isRight && event.hasItem()) {
            val backpack = Backpack.read(event.item)
            if (backpack != null) {
                event.setUseInteractedBlock(Event.Result.DENY)
                backpack.open(event.player)
            }
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        val backpack = Backpack.get(event.inventory)
        if (backpack != null) {
            val stack = Backpack.save(backpack, event.player.inventory.itemInMainHand)
            event.player.itemInMainHand = stack
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onClick(event: InventoryClickEvent) {
        val backpack = Backpack.get(event.view.topInventory)
        if (backpack != null && event.clickedInventory == event.whoClicked.inventory) {
            val clickedItem = event.currentItem
            if (Backpack.isBackpack(clickedItem))
                event.isCancelled = true
        }
    }
}
