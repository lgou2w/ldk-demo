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

import com.lgou2w.ldk.bukkit.cmd.Command
import com.lgou2w.ldk.bukkit.cmd.CommandRoot
import com.lgou2w.ldk.bukkit.cmd.Initializable
import com.lgou2w.ldk.bukkit.cmd.Parameter
import com.lgou2w.ldk.bukkit.cmd.Permission
import com.lgou2w.ldk.bukkit.cmd.Playable
import com.lgou2w.ldk.bukkit.cmd.Playername
import com.lgou2w.ldk.bukkit.cmd.RegisteredCommand
import com.lgou2w.ldk.bukkit.entity.itemInMainHand
import com.lgou2w.ldk.bukkit.item.isAir
import com.lgou2w.ldk.chat.toColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandRoot("irepo")
class ItemRepositoryCommand(private val plugin: Main) : Initializable {

    private val PREFIX = "[ItemRepository] "

    override fun initialize(command: RegisteredCommand) {
        command.isAllowCompletion = true
        command.prefix = PREFIX
    }

    @Command("add")
    @Playable
    @Permission("irepo.add")
    fun add(player: Player,
            @Parameter("target")
            @Playername
            target: String
    ) {
        val stack = player.inventory.itemInHand
        if (stack.isAir()) {
            player.send("&c请手持一个物品再执行此命令.")
            return
        }
        val repository = plugin.itemRepositories.getOrLoadRepository(target)
        repository.items.add(stack.clone())
//        repository.save(plugin.itemRepositories)
        player.itemInMainHand = null
        player.send("&a成功将物品存储到 &6$target&a 的物品仓库中.")
    }

    @Command("view")
    @Playable
    @Permission("irepo.view")
    fun view(player: Player) {
        val repository = plugin.itemRepositories.getOrLoadRepository(player.name)
        plugin.itemRepositories.openRepository(repository, player)
    }

    private fun CommandSender.send(message: String) {
        sendMessage(PREFIX + message.toColor())
    }
}
