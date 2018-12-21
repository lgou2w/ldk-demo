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

package com.lgou2w.ldk.demo.tornadofx

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import tornadofx.App
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.label
import tornadofx.launch
import tornadofx.stackpane
import tornadofx.vbox

fun main() {
    launch<MyApp>()
}

class MyApp : App(MyView::class)
class MyView : View("FxTornadofx") {
    override val root = stackpane {
        minWidth = 600.0
        minHeight = 500.0
    }
    val valueProperty = SimpleStringProperty("HelloWorld!")
    init {
        with (root) {
            vbox {
                alignment = Pos.CENTER
                spacing = 24.0
                label {
                    textProperty().bind(valueProperty)
                    style += "-fx-font-size: 1.5em;"
                }
                button {
                    text = "Click me"
                    action {
                        valueProperty.value = "Hi, Tornadofx!"
                    }
                }
            }
        }
    }
}
