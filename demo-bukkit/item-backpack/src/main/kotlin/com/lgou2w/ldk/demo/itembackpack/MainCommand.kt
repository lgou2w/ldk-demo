/*
 * Copyright (C) 2019 The lgou2w <lgou2w@hotmail.com>
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

import com.lgou2w.ldk.bukkit.cmd.Command
import com.lgou2w.ldk.bukkit.cmd.CommandRoot
import com.lgou2w.ldk.bukkit.cmd.Permission
import com.lgou2w.ldk.bukkit.cmd.Playable
import com.lgou2w.ldk.bukkit.cmd.StandardCommand
import com.lgou2w.ldk.chat.ChatColor
import org.bukkit.entity.Player

@CommandRoot("backpack")
class MainCommand : StandardCommand() {

    @Command("backpack")
    @Permission("ldk.backpack")
    @Playable
    fun backpack(player: Player) {
        val backpack = Backpack.create()
        player.inventory.addItem(backpack)
        player.send(ChatColor.GREEN + "You get an item backpack.")
    }
}
