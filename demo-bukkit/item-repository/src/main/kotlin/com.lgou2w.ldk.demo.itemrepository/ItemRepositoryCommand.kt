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

import com.lgou2w.ldk.bukkit.cmd.AdaptiveCompleteProxy
import com.lgou2w.ldk.bukkit.cmd.Command
import com.lgou2w.ldk.bukkit.cmd.CommandManager
import com.lgou2w.ldk.bukkit.cmd.CommandRoot
import com.lgou2w.ldk.bukkit.cmd.Permission
import com.lgou2w.ldk.bukkit.cmd.Playable
import com.lgou2w.ldk.bukkit.cmd.StandardCommand
import com.lgou2w.ldk.bukkit.item.isAir
import org.bukkit.entity.Player

@CommandRoot("irepo")
class ItemRepositoryCommand(private val plugin: Main) : StandardCommand() {

    override fun initialize(manager: CommandManager) {
        command.completeProxy = AdaptiveCompleteProxy()
        command.prefix = "[ItemRepository] "
    }

    @Command("add")
    @Playable
    @Permission("irepo.add")
    fun add(player: Player, target: String) {
        val stack = player.inventory.itemInHand
        if (stack.isAir()) {
            player.send("&c请手持一个物品再执行此命令.")
            return
        }
        val repository = plugin.itemRepositories.getOrLoadRepository(target)
        repository.items.add(stack.clone())
//        repository.save(plugin.itemRepositories)
        player.inventory.itemInHand = null
        player.send("&a成功将物品存储到 &6$target&a 的物品仓库中.")
    }

    @Command("view")
    @Playable
    @Permission("irepo.view")
    fun view(player: Player) {
        val repository = plugin.itemRepositories.getOrLoadRepository(player.name)
        plugin.itemRepositories.openRepository(repository, player)
    }
}
