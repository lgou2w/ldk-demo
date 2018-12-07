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

import com.lgou2w.ldk.bukkit.PluginBase
import com.lgou2w.ldk.bukkit.coroutines.State
import com.lgou2w.ldk.bukkit.event.EventListener
import com.lgou2w.ldk.sql.ConnectionFactory
import com.lgou2w.ldk.sql.MySQLConnectionFactory
import com.lgou2w.ldk.sql.buildConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.server.ServerCommandEvent

class ExposedDb : PluginBase(), EventListener {

    // Exposed + HikariCP + Coroutines

    lateinit var cf : ConnectionFactory
    lateinit var exposed : ExposedFactory

    override fun load() {
    }

    override fun enable() {
        cf = MySQLConnectionFactory(buildConfiguration {
            poolName = "exposed-db"
            address = "localhost:3306"
            database = "mcclake"
            username = "root"
            password = "moonlake"
            property = "useSSL" to true
        })
        cf.initialize()
        exposed = ExposedFactory(this, cf)
        server.pluginManager.registerEvents(this, this)
    }

    override fun disable() {
    }

    @EventHandler
    fun onCommand(event: ServerCommandEvent) {
        exposed.launcher(State.ASYNC) {
            query {
                // Async Craft Thread
                println("查询线程: ${Thread.currentThread().name}")
                val demo = Demo.find { Demos.name.eq("console") }
                    .limit(1)
                    .firstOrNull()
            }
            switchState(State.SYNC)
            query {
                // Server Thread
                println("查询线程: ${Thread.currentThread().name}")
                val demo = Demo.find { Demos.name.eq("console") }
                    .limit(1)
                    .firstOrNull()
            }
        }
    }
}
