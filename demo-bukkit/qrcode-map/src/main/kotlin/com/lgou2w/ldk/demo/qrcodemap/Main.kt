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

package com.lgou2w.ldk.demo.qrcodemap

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.lgou2w.ldk.bukkit.PluginBase
import com.lgou2w.ldk.bukkit.event.registerListeners
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView

class Main : PluginBase() {

    override fun load() {
    }

    override fun enable() {
        registerListeners {
            event<PlayerCommandPreprocessEvent> {
                if (message == "/qrcode-map") {
                    val mv = createQRCodeMap("https://github.com/lgou2w")
                    val item = ItemStack(Material.FILLED_MAP)
                    val meta = item.itemMeta as MapMeta
                    meta.mapId = mv.id
                    item.itemMeta = meta
                    player.inventory.addItem(item)
                    isCancelled = true
                }
            }
        }
    }

    override fun disable() {
    }

    fun createQRCodeMap(content: String): MapView {
        return Bukkit.createMap(Bukkit.getWorlds().first()).apply {
            renderers.forEach { removeRenderer(it) }
            addRenderer(QRCodeMapRender(content))
        }
    }

    class QRCodeMapRender(val content: String) : MapRenderer() {
        val qrcode by lazy {
            QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 127, 127,
                    mapOf(
                            EncodeHintType.CHARACTER_SET to "utf-8",
                            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L,
                            EncodeHintType.MARGIN to 1
                    )
            )
        }
        override fun render(mv: MapView, mc: MapCanvas, player: Player) {
            for (y in 0 until qrcode.height)
                for (x in 0 until qrcode.width) {
                    if (qrcode[x, y])
                        mc.setPixel(x, y, 44) // 深灰色
                    else
                        mc.setPixel(x, y, 32) // 白色
                }
        }
    }
}
