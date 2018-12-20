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

import com.lgou2w.ldk.sql.SQLiteConnectionFactory
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Paths
import java.sql.Connection

fun main() {

    // kotlin + coroutines + exposed + sqlite

    val sqlite = Paths.get("C:/Users/MoonLake/Desktop/exposed.db")
    val cf = SQLiteConnectionFactory(sqlite).apply { initialize() }
    runBlocking {
        Database.connect({ cf.openSession() })
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction {
            SchemaUtils.create(ExposedTable)
            if (ExposedTable.selectAll().count() <= 0) {
                ExposedTable.insert { it[name] = "lgou2w" }
            } else {
                ExposedTable.selectAll()
                    .forEach {
                        val id = it[ExposedTable.id]
                        val name = it[ExposedTable.name]
                        println("$id = $name")
                    }
            }
        }
    }
    cf.shutdown()
}

object ExposedTable : IntIdTable() {
    val name = varchar("name", 16).uniqueIndex()
}
