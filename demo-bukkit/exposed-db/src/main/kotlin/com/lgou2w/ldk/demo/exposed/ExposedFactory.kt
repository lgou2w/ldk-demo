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

package com.lgou2w.ldk.demo.exposed

import com.lgou2w.ldk.bukkit.coroutines.BukkitCoroutineFactory
import com.lgou2w.ldk.hikari.ConnectionFactory
import org.bukkit.plugin.Plugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedFactory(
        plugin: Plugin,
        val connectionFactory: ConnectionFactory
) : BukkitCoroutineFactory(plugin) {

    override suspend fun launching() {
        super.launching()
        Database.connect(connectionFactory.dataSource)
    }
}

suspend fun <T> BukkitCoroutineFactory.query(block: BukkitCoroutineFactory.() -> T) : T {
    return with {
        transaction {
            block()
        }
    }
}
