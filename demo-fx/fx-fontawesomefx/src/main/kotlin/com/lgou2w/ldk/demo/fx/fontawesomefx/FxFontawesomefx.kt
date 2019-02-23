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

package com.lgou2w.ldk.demo.fx.fontawesomefx

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.binding.StringBinding
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.layout.Border
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.App
import tornadofx.FX
import tornadofx.View
import tornadofx.bind
import tornadofx.flowpane
import tornadofx.hbox
import tornadofx.label
import tornadofx.launch
import tornadofx.scrollpane
import tornadofx.stackpane
import tornadofx.vbox
import java.util.Collections

fun main() {
    launch<MyApp>()
}

class MyApp : App(MyView::class)
class MyView : View("FxFontawesomefx") {
    override val root = stackpane {
        minWidth = 1280.0
        maxHeight = 720.0
    }
    val iconSizeProperty = SimpleDoubleProperty(24.0)
    val iconSetProperty = FXCollections.observableList(Constants.VALUES.flatMap { it.values }.toMutableList())
    val iconActiveProperty = SimpleStringProperty(null)
    init {
        with (root) {
            stackpane {
                padding = Insets(12.0)
                hbox {
                    spacing = 12.0
                    vbox {
                        id = Constants.R_VIEW_NAVIGATION
                        spacing = 8.0
                        padding = Insets(8.0)
                        border = Border(BorderStroke(Constants.BORDER_COLOR, BorderStrokeStyle.SOLID, null, null))
                        Constants.VALUES.map { value ->
                            label {
                                text = value.name
                                style += "-fx-font-size: 1em;"
                                style += "-fx-font-weigh: bold;"
                                style += "-fx-cursor: hand;"
                                setOnMouseClicked {
                                    val iconSet = Constants.VALUES.find { find -> find.name == text }
                                    if (iconSet != null) {
                                        var values = iconSet.values.toMutableList()
                                        if (iconSet === Constants.ALL)
                                            values = Constants.VALUES.flatMap { it.values }.toMutableList()
                                        iconSetProperty.setAll(values)
                                    }
                                }
                            }
                        }
                    }
                    scrollpane {
                        id = Constants.R_VIEW_SCROLL_PANE
                        padding = Insets(8.0)
                        border = Border(BorderStroke(Constants.BORDER_COLOR, BorderStrokeStyle.SOLID, null, null))
                        flowpane {
                            id = Constants.R_VIEW_ICONS
                            vgap = 12.0
                            hgap = 12.0
                            children.bind(iconSetProperty) { icon ->
                                FontAwesomeIconView(icon).apply {
                                    glyphSizeProperty().bind(iconSizeProperty)
                                    setOnMouseEntered {
                                        iconActiveProperty.value = icon.name.replace("_", "-").toLowerCase()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        root.sceneProperty().addListener { _, _, scene ->
            scene.windowProperty().addListener { _, _, window ->
                window.setOnShown {
                    root.minWidthProperty().bind(window.widthProperty())
                    root.minHeightProperty().bind(window.heightProperty())
                    val viewNavigation = root.lookup("#" + Constants.R_VIEW_NAVIGATION) as VBox
                    val viewScrollPane = root.lookup("#" + Constants.R_VIEW_SCROLL_PANE) as ScrollPane
                    val viewIcons = viewScrollPane.lookup("#" + Constants.R_VIEW_ICONS) as FlowPane
                    viewIcons.prefWidthProperty().bind(viewScrollPane.widthProperty().subtract(48.0))
                    viewScrollPane.prefWidthProperty().bind(root.widthProperty().subtract(viewNavigation.widthProperty()).subtract(48.0))
                    scene.setOnKeyPressed { event ->
                        when (event.code) {
                            KeyCode.ADD -> iconSizeProperty.value += 2.0
                            KeyCode.SUBTRACT -> iconSizeProperty.value -= 2.0
                            else -> {}
                        }
                    }
                    FX.primaryStage.titleProperty().bind(object : StringBinding() {
                        init {
                            super.bind(iconSizeProperty)
                            super.bind(iconActiveProperty)
                        }
                        override fun computeValue(): String {
                            val size = iconSizeProperty.value
                            val active = iconActiveProperty.value
                            return "FxFontawesome @lgou2w | Size: $size" + if (active == null) "" else " | Active: fa-$active"
                        }
                        override fun dispose() {
                            super.unbind(iconSizeProperty)
                            super.unbind(iconActiveProperty)
                        }
                        override fun getDependencies(): ObservableList<*> {
                            return FXCollections.observableArrayList(iconSizeProperty, iconActiveProperty)
                        }
                    })
                }
            }
        }
    }
}

object Constants {

    const val R_VIEW_NAVIGATION = "view-navigation"
    const val R_VIEW_SCROLL_PANE = "view-scroll"
    const val R_VIEW_ICONS = "view-icons"

    val BORDER_COLOR = Color.rgb(177, 177, 177)

    data class Set(val name: String, val values: List<FontAwesomeIcon>)

    private fun iconSets(value: List<FontAwesomeIcon>): List<FontAwesomeIcon>
            = Collections.unmodifiableList(value)

    val WEB_APPLICATION = iconSets(listOf(
            FontAwesomeIcon.ADDRESS_BOOK,
            FontAwesomeIcon.ADDRESS_CARD,
            FontAwesomeIcon.ADJUST,
            FontAwesomeIcon.AMERICAN_SIGN_LANGUAGE_INTERPRETING,
            FontAwesomeIcon.ANCHOR,
            FontAwesomeIcon.ARCHIVE,
            FontAwesomeIcon.AREA_CHART,
            FontAwesomeIcon.ARROWS,
            FontAwesomeIcon.ARROWS_H,
            FontAwesomeIcon.ARROWS_V,
            FontAwesomeIcon.ASL_INTERPRETING,
            FontAwesomeIcon.ASSISTIVE_LISTENING_SYSTEMS,
            FontAwesomeIcon.ASTERISK,
            FontAwesomeIcon.AT,
            FontAwesomeIcon.AUDIO_DESCRIPTION,
            FontAwesomeIcon.AUTOMOBILE,
            FontAwesomeIcon.BALANCE_SCALE,
            FontAwesomeIcon.BAN,
            FontAwesomeIcon.BANK,
            FontAwesomeIcon.BAR_CHART,
            FontAwesomeIcon.BARCODE,
            FontAwesomeIcon.BARS,
            FontAwesomeIcon.BATH,
            FontAwesomeIcon.BATHTUB,
            FontAwesomeIcon.BATTERY,
            FontAwesomeIcon.BATTERY_0,
            FontAwesomeIcon.BATTERY_1,
            FontAwesomeIcon.BATTERY_2,
            FontAwesomeIcon.BATTERY_3,
            FontAwesomeIcon.BATTERY_4,
            FontAwesomeIcon.BATTERY_EMPTY,
            FontAwesomeIcon.BATTERY_FULL,
            FontAwesomeIcon.BATTERY_HALF,
            FontAwesomeIcon.BATTERY_QUARTER,
            FontAwesomeIcon.BATTERY_THREE_QUARTERS,
            FontAwesomeIcon.BED,
            FontAwesomeIcon.BEER,
            FontAwesomeIcon.BELL,
            FontAwesomeIcon.BELL_SLASH,
            FontAwesomeIcon.BICYCLE,
            FontAwesomeIcon.BINOCULARS,
            FontAwesomeIcon.BIRTHDAY_CAKE,
            FontAwesomeIcon.BLIND,
            FontAwesomeIcon.BLUETOOTH,
            FontAwesomeIcon.BLUETOOTH_B,
            FontAwesomeIcon.BOLT,
            FontAwesomeIcon.BOMB,
            FontAwesomeIcon.BOOK,
            FontAwesomeIcon.BOOKMARK,
            FontAwesomeIcon.BRAILLE,
            FontAwesomeIcon.BRIEFCASE,
            FontAwesomeIcon.BUG,
            FontAwesomeIcon.BUILDING,
            FontAwesomeIcon.BULLHORN,
            FontAwesomeIcon.BULLSEYE,
            FontAwesomeIcon.BUS,
            FontAwesomeIcon.CAB,
            FontAwesomeIcon.CALCULATOR,
            FontAwesomeIcon.CALENDAR,
            FontAwesomeIcon.CAMERA,
            FontAwesomeIcon.CAR,
            FontAwesomeIcon.CART_ARROW_DOWN,
            FontAwesomeIcon.CART_PLUS,
            FontAwesomeIcon.CC,
            FontAwesomeIcon.CERTIFICATE,
            FontAwesomeIcon.CHECK,
            FontAwesomeIcon.CHECK_CIRCLE,
            FontAwesomeIcon.CHECK_SQUARE,
            FontAwesomeIcon.CHILD,
            FontAwesomeIcon.CIRCLE,
            FontAwesomeIcon.CIRCLE_THIN,
            FontAwesomeIcon.CLONE,
            FontAwesomeIcon.CLOSE,
            FontAwesomeIcon.CLOUD,
            FontAwesomeIcon.CLOUD_DOWNLOAD,
            FontAwesomeIcon.CLOUD_UPLOAD,
            FontAwesomeIcon.CODE,
            FontAwesomeIcon.CODE_FORK,
            FontAwesomeIcon.COFFEE,
            FontAwesomeIcon.COG,
            FontAwesomeIcon.COGS,
            FontAwesomeIcon.COMMENT,
            FontAwesomeIcon.COMMENTING,
            FontAwesomeIcon.COMMENTS,
            FontAwesomeIcon.COMPASS,
            FontAwesomeIcon.COPYRIGHT,
            FontAwesomeIcon.CREATIVE_COMMONS,
            FontAwesomeIcon.CREDIT_CARD,
            FontAwesomeIcon.CREDIT_CARD_ALT,
            FontAwesomeIcon.CROP,
            FontAwesomeIcon.CROSSHAIRS,
            FontAwesomeIcon.CUBE,
            FontAwesomeIcon.CUBES,
            FontAwesomeIcon.CUTLERY,
            FontAwesomeIcon.DASHBOARD,
            FontAwesomeIcon.DATABASE,
            FontAwesomeIcon.DEAF,
            FontAwesomeIcon.DEAFNESS,
            FontAwesomeIcon.DESKTOP,
            FontAwesomeIcon.DIAMOND,
            FontAwesomeIcon.DOWNLOAD,
            FontAwesomeIcon.DRIVERS_LICENSE,
            FontAwesomeIcon.EDIT,
            FontAwesomeIcon.ELLIPSIS_H,
            FontAwesomeIcon.ELLIPSIS_V,
            FontAwesomeIcon.ENVELOPE,
            FontAwesomeIcon.ENVELOPE_OPEN,
            FontAwesomeIcon.ENVELOPE_SQUARE,
            FontAwesomeIcon.ERASER,
            FontAwesomeIcon.EXCHANGE,
            FontAwesomeIcon.EXCLAMATION,
            FontAwesomeIcon.EXCLAMATION_CIRCLE,
            FontAwesomeIcon.EXCLAMATION_TRIANGLE,
            FontAwesomeIcon.EXTERNAL_LINK,
            FontAwesomeIcon.EXTERNAL_LINK_SQUARE,
            FontAwesomeIcon.EYE,
            FontAwesomeIcon.EYE_SLASH,
            FontAwesomeIcon.EYEDROPPER,
            FontAwesomeIcon.FAX,
            FontAwesomeIcon.FEED,
            FontAwesomeIcon.FEMALE,
            FontAwesomeIcon.FIGHTER_JET,
            FontAwesomeIcon.FILM,
            FontAwesomeIcon.FILTER,
            FontAwesomeIcon.FIRE,
            FontAwesomeIcon.FIRE_EXTINGUISHER,
            FontAwesomeIcon.FLAG,
            FontAwesomeIcon.FLAG_CHECKERED,
            FontAwesomeIcon.FLASH,
            FontAwesomeIcon.FLASK,
            FontAwesomeIcon.FOLDER,
            FontAwesomeIcon.FOLDER_OPEN,
            FontAwesomeIcon.GAMEPAD,
            FontAwesomeIcon.GAVEL,
            FontAwesomeIcon.GEAR,
            FontAwesomeIcon.GEARS,
            FontAwesomeIcon.GIFT,
            FontAwesomeIcon.GLASS,
            FontAwesomeIcon.GLOBE,
            FontAwesomeIcon.GRADUATION_CAP,
            FontAwesomeIcon.GROUP,
            FontAwesomeIcon.HARD_OF_HEARING,
            FontAwesomeIcon.HASHTAG,
            FontAwesomeIcon.HEADPHONES,
            FontAwesomeIcon.HEART,
            FontAwesomeIcon.HEARTBEAT,
            FontAwesomeIcon.HISTORY,
            FontAwesomeIcon.HOME,
            FontAwesomeIcon.HOTEL,
            FontAwesomeIcon.HOURGLASS,
            FontAwesomeIcon.HOURGLASS_1,
            FontAwesomeIcon.HOURGLASS_2,
            FontAwesomeIcon.HOURGLASS_3,
            FontAwesomeIcon.HOURGLASS_END,
            FontAwesomeIcon.HOURGLASS_HALF,
            FontAwesomeIcon.HOURGLASS_START,
            FontAwesomeIcon.I_CURSOR,
            FontAwesomeIcon.ID_BADGE,
            FontAwesomeIcon.ID_CARD,
            FontAwesomeIcon.IMAGE,
            FontAwesomeIcon.INBOX,
            FontAwesomeIcon.INDUSTRY,
            FontAwesomeIcon.INFO_CIRCLE,
            FontAwesomeIcon.INSTITUTION,
            FontAwesomeIcon.KEY,
            FontAwesomeIcon.LANGUAGE,
            FontAwesomeIcon.LAPTOP,
            FontAwesomeIcon.LEAF,
            FontAwesomeIcon.LEGAL,
            FontAwesomeIcon.LEVEL_DOWN,
            FontAwesomeIcon.LEVEL_UP,
            FontAwesomeIcon.LIFE_BOUY,
            FontAwesomeIcon.LIFE_BUOY,
            FontAwesomeIcon.LIFE_RING,
            FontAwesomeIcon.LIFE_SAVER,
            FontAwesomeIcon.LINE_CHART,
            FontAwesomeIcon.LOCATION_ARROW,
            FontAwesomeIcon.LOCK,
            FontAwesomeIcon.LOW_VISION,
            FontAwesomeIcon.MAGIC,
            FontAwesomeIcon.MAGNET,
            FontAwesomeIcon.MAIL_FORWARD,
            FontAwesomeIcon.MAIL_REPLY,
            FontAwesomeIcon.MAIL_REPLY_ALL,
            FontAwesomeIcon.MALE,
            FontAwesomeIcon.MAP,
            FontAwesomeIcon.MAP_MARKER,
            FontAwesomeIcon.MAP_PIN,
            FontAwesomeIcon.MAP_SIGNS,
            FontAwesomeIcon.MICROCHIP,
            FontAwesomeIcon.MICROPHONE,
            FontAwesomeIcon.MICROPHONE_SLASH,
            FontAwesomeIcon.MINUS,
            FontAwesomeIcon.MINUS_CIRCLE,
            FontAwesomeIcon.MINUS_SQUARE,
            FontAwesomeIcon.MOBILE,
            FontAwesomeIcon.MOBILE_PHONE,
            FontAwesomeIcon.MONEY,
            FontAwesomeIcon.MORTAR_BOARD,
            FontAwesomeIcon.MOTORCYCLE,
            FontAwesomeIcon.MOUSE_POINTER,
            FontAwesomeIcon.MUSIC,
            FontAwesomeIcon.NAVICON,
            FontAwesomeIcon.OBJECT_GROUP,
            FontAwesomeIcon.OBJECT_UNGROUP,
            FontAwesomeIcon.PAINT_BRUSH,
            FontAwesomeIcon.PAPER_PLANE,
            FontAwesomeIcon.PAW,
            FontAwesomeIcon.PENCIL,
            FontAwesomeIcon.PENCIL_SQUARE,
            FontAwesomeIcon.PERCENT,
            FontAwesomeIcon.PHONE,
            FontAwesomeIcon.PHONE_SQUARE,
            FontAwesomeIcon.PIE_CHART,
            FontAwesomeIcon.PLANE,
            FontAwesomeIcon.PLUG,
            FontAwesomeIcon.PLUS,
            FontAwesomeIcon.PLUS_CIRCLE,
            FontAwesomeIcon.PLUS_SQUARE,
            FontAwesomeIcon.PODCAST,
            FontAwesomeIcon.POWER_OFF,
            FontAwesomeIcon.PRINT,
            FontAwesomeIcon.PUZZLE_PIECE,
            FontAwesomeIcon.QRCODE,
            FontAwesomeIcon.QUESTION,
            FontAwesomeIcon.QUESTION_CIRCLE,
            FontAwesomeIcon.QUOTE_LEFT,
            FontAwesomeIcon.QUOTE_RIGHT,
            FontAwesomeIcon.RANDOM,
            FontAwesomeIcon.RECYCLE,
            FontAwesomeIcon.REFRESH,
            FontAwesomeIcon.REGISTERED,
            FontAwesomeIcon.REMOVE,
            FontAwesomeIcon.REORDER,
            FontAwesomeIcon.REPLY,
            FontAwesomeIcon.REPLY_ALL,
            FontAwesomeIcon.RETWEET,
            FontAwesomeIcon.ROAD,
            FontAwesomeIcon.ROCKET,
            FontAwesomeIcon.RSS,
            FontAwesomeIcon.RSS_SQUARE,
            FontAwesomeIcon.S15,
            FontAwesomeIcon.SEARCH,
            FontAwesomeIcon.SEARCH_MINUS,
            FontAwesomeIcon.SEARCH_PLUS,
            FontAwesomeIcon.SEND,
            FontAwesomeIcon.SERVER,
            FontAwesomeIcon.SHARE,
            FontAwesomeIcon.SHARE_ALT,
            FontAwesomeIcon.SHARE_ALT_SQUARE,
            FontAwesomeIcon.SHARE_SQUARE,
            FontAwesomeIcon.SHIELD,
            FontAwesomeIcon.SHIP,
            FontAwesomeIcon.SHOPPING_BAG,
            FontAwesomeIcon.SHOPPING_BASKET,
            FontAwesomeIcon.SHOPPING_CART,
            FontAwesomeIcon.SHOWER,
            FontAwesomeIcon.SIGN_IN,
            FontAwesomeIcon.SIGN_LANGUAGE,
            FontAwesomeIcon.SIGN_OUT,
            FontAwesomeIcon.SIGNAL,
            FontAwesomeIcon.SIGNING,
            FontAwesomeIcon.SITEMAP,
            FontAwesomeIcon.SLIDERS,
            FontAwesomeIcon.SORT,
            FontAwesomeIcon.SORT_ALPHA_ASC,
            FontAwesomeIcon.SORT_ALPHA_DESC,
            FontAwesomeIcon.SORT_AMOUNT_ASC,
            FontAwesomeIcon.SORT_AMOUNT_DESC,
            FontAwesomeIcon.SORT_ASC,
            FontAwesomeIcon.SORT_DESC,
            FontAwesomeIcon.SORT_DOWN,
            FontAwesomeIcon.SORT_NUMERIC_ASC,
            FontAwesomeIcon.SORT_NUMERIC_DESC,
            FontAwesomeIcon.SORT_UP,
            FontAwesomeIcon.SPACE_SHUTTLE,
            FontAwesomeIcon.SPINNER,
            FontAwesomeIcon.SPOON,
            FontAwesomeIcon.SQUARE,
            FontAwesomeIcon.STAR,
            FontAwesomeIcon.STAR_HALF,
            FontAwesomeIcon.STAR_HALF_EMPTY,
            FontAwesomeIcon.STAR_HALF_FULL,
            FontAwesomeIcon.STICKY_NOTE,
            FontAwesomeIcon.STREET_VIEW,
            FontAwesomeIcon.SUITCASE,
            FontAwesomeIcon.SUPPORT,
            FontAwesomeIcon.TABLET,
            FontAwesomeIcon.TACHOMETER,
            FontAwesomeIcon.TAG,
            FontAwesomeIcon.TAGS,
            FontAwesomeIcon.TASKS,
            FontAwesomeIcon.TAXI,
            FontAwesomeIcon.TELEVISION,
            FontAwesomeIcon.TERMINAL,
            FontAwesomeIcon.THERMOMETER,
            FontAwesomeIcon.THERMOMETER_0,
            FontAwesomeIcon.THERMOMETER_1,
            FontAwesomeIcon.THERMOMETER_2,
            FontAwesomeIcon.THERMOMETER_3,
            FontAwesomeIcon.THERMOMETER_4,
            FontAwesomeIcon.THERMOMETER_EMPTY,
            FontAwesomeIcon.THERMOMETER_FULL,
            FontAwesomeIcon.THERMOMETER_HALF,
            FontAwesomeIcon.THERMOMETER_QUARTER,
            FontAwesomeIcon.THERMOMETER_THREE_QUARTERS,
            FontAwesomeIcon.THUMB_TACK,
            FontAwesomeIcon.THUMBS_DOWN,
            FontAwesomeIcon.THUMBS_UP,
            FontAwesomeIcon.TICKET,
            FontAwesomeIcon.TIMES,
            FontAwesomeIcon.TIMES_CIRCLE,
            FontAwesomeIcon.TIMES_RECTANGLE,
            FontAwesomeIcon.TINT,
            FontAwesomeIcon.TOGGLE_DOWN,
            FontAwesomeIcon.TOGGLE_LEFT,
            FontAwesomeIcon.TOGGLE_OFF,
            FontAwesomeIcon.TOGGLE_ON,
            FontAwesomeIcon.TOGGLE_RIGHT,
            FontAwesomeIcon.TOGGLE_UP,
            FontAwesomeIcon.TRADEMARK,
            FontAwesomeIcon.TRASH,
            FontAwesomeIcon.TREE,
            FontAwesomeIcon.TROPHY,
            FontAwesomeIcon.TRUCK,
            FontAwesomeIcon.TTY,
            FontAwesomeIcon.TV,
            FontAwesomeIcon.UMBRELLA,
            FontAwesomeIcon.UNIVERSAL_ACCESS,
            FontAwesomeIcon.UNIVERSITY,
            FontAwesomeIcon.UNLOCK,
            FontAwesomeIcon.UNLOCK_ALT,
            FontAwesomeIcon.UNSORTED,
            FontAwesomeIcon.UPLOAD,
            FontAwesomeIcon.USER,
            FontAwesomeIcon.USER_CIRCLE,
            FontAwesomeIcon.USER_PLUS,
            FontAwesomeIcon.USER_SECRET,
            FontAwesomeIcon.USER_TIMES,
            FontAwesomeIcon.USERS,
            FontAwesomeIcon.VCARD,
            FontAwesomeIcon.VIDEO_CAMERA,
            FontAwesomeIcon.VOLUME_CONTROL_PHONE,
            FontAwesomeIcon.VOLUME_DOWN,
            FontAwesomeIcon.VOLUME_OFF,
            FontAwesomeIcon.VOLUME_UP,
            FontAwesomeIcon.WARNING,
            FontAwesomeIcon.WHEELCHAIR,
            FontAwesomeIcon.WHEELCHAIR_ALT,
            FontAwesomeIcon.WIFI,
            FontAwesomeIcon.WINDOW_CLOSE,
            FontAwesomeIcon.WINDOW_MAXIMIZE,
            FontAwesomeIcon.WINDOW_MINIMIZE,
            FontAwesomeIcon.WINDOW_RESTORE,
            FontAwesomeIcon.WRENCH
    ))
    val ACCESSIBILITY = iconSets(listOf(
            FontAwesomeIcon.AMERICAN_SIGN_LANGUAGE_INTERPRETING,
            FontAwesomeIcon.ASL_INTERPRETING,
            FontAwesomeIcon.ASSISTIVE_LISTENING_SYSTEMS,
            FontAwesomeIcon.AUDIO_DESCRIPTION,
            FontAwesomeIcon.BLIND,
            FontAwesomeIcon.BRAILLE,
            FontAwesomeIcon.CC,
            FontAwesomeIcon.DEAF,
            FontAwesomeIcon.DEAFNESS,
            FontAwesomeIcon.HARD_OF_HEARING,
            FontAwesomeIcon.LOW_VISION,
            FontAwesomeIcon.SIGN_LANGUAGE,
            FontAwesomeIcon.SIGNING,
            FontAwesomeIcon.TTY,
            FontAwesomeIcon.UNIVERSAL_ACCESS,
            FontAwesomeIcon.VOLUME_CONTROL_PHONE,
            FontAwesomeIcon.WHEELCHAIR,
            FontAwesomeIcon.WHEELCHAIR_ALT
    ))
    val HAND = iconSets(listOf(
            FontAwesomeIcon.THUMBS_DOWN,
            FontAwesomeIcon.THUMBS_UP
    ))
    val TRANSPORTATION = iconSets(listOf(
            FontAwesomeIcon.AMBULANCE,
            FontAwesomeIcon.AUTOMOBILE,
            FontAwesomeIcon.BICYCLE,
            FontAwesomeIcon.BUS,
            FontAwesomeIcon.CAB,
            FontAwesomeIcon.CAR,
            FontAwesomeIcon.FIGHTER_JET,
            FontAwesomeIcon.MOTORCYCLE,
            FontAwesomeIcon.PLANE,
            FontAwesomeIcon.ROCKET,
            FontAwesomeIcon.SHIP,
            FontAwesomeIcon.SPACE_SHUTTLE,
            FontAwesomeIcon.SUBWAY,
            FontAwesomeIcon.TAXI,
            FontAwesomeIcon.TRAIN,
            FontAwesomeIcon.TRUCK,
            FontAwesomeIcon.WHEELCHAIR,
            FontAwesomeIcon.WHEELCHAIR_ALT
    ))
    val GENDER = iconSets(listOf(
            FontAwesomeIcon.GENDERLESS,
            FontAwesomeIcon.INTERSEX,
            FontAwesomeIcon.MARS,
            FontAwesomeIcon.MARS_DOUBLE,
            FontAwesomeIcon.MARS_STROKE,
            FontAwesomeIcon.MARS_STROKE_H,
            FontAwesomeIcon.MARS_STROKE_V,
            FontAwesomeIcon.MERCURY,
            FontAwesomeIcon.NEUTER,
            FontAwesomeIcon.TRANSGENDER,
            FontAwesomeIcon.TRANSGENDER_ALT,
            FontAwesomeIcon.VENUS,
            FontAwesomeIcon.VENUS_DOUBLE,
            FontAwesomeIcon.VENUS_MARS
    ))
    val FILE_TYPE = iconSets(listOf(
            FontAwesomeIcon.FILE,
            FontAwesomeIcon.FILE_TEXT
    ))
    val SPINNER = iconSets(listOf(
            FontAwesomeIcon.COG,
            FontAwesomeIcon.GEAR,
            FontAwesomeIcon.REFRESH,
            FontAwesomeIcon.SPINNER
    ))
    val FORM_CONTROL = iconSets(listOf(
            FontAwesomeIcon.CHECK_SQUARE,
            FontAwesomeIcon.CIRCLE,
            FontAwesomeIcon.MINUS_SQUARE,
            FontAwesomeIcon.PLUS_SQUARE,
            FontAwesomeIcon.SQUARE
    ))
    val PAY_MENT = iconSets(listOf(
            FontAwesomeIcon.CC_AMEX,
            FontAwesomeIcon.CC_DINERS_CLUB,
            FontAwesomeIcon.CC_DISCOVER,
            FontAwesomeIcon.CC_JCB,
            FontAwesomeIcon.CC_MASTERCARD,
            FontAwesomeIcon.CC_PAYPAL,
            FontAwesomeIcon.CC_STRIPE,
            FontAwesomeIcon.CC_VISA,
            FontAwesomeIcon.CREDIT_CARD,
            FontAwesomeIcon.CREDIT_CARD_ALT,
            FontAwesomeIcon.GOOGLE_WALLET,
            FontAwesomeIcon.PAYPAL
    ))
    val CHART = iconSets(listOf(
            FontAwesomeIcon.AREA_CHART,
            FontAwesomeIcon.BAR_CHART,
            FontAwesomeIcon.LINE_CHART,
            FontAwesomeIcon.PIE_CHART
    ))
    val CURRENCY = iconSets(listOf(
            FontAwesomeIcon.BITCOIN,
            FontAwesomeIcon.BTC,
            FontAwesomeIcon.CNY,
            FontAwesomeIcon.DOLLAR,
            FontAwesomeIcon.EUR,
            FontAwesomeIcon.GBP,
            FontAwesomeIcon.GG,
            FontAwesomeIcon.GG_CIRCLE,
            FontAwesomeIcon.ILS,
            FontAwesomeIcon.INR,
            FontAwesomeIcon.JPY,
            FontAwesomeIcon.KRW,
            FontAwesomeIcon.MONEY,
            FontAwesomeIcon.RMB,
            FontAwesomeIcon.ROUBLE,
            FontAwesomeIcon.RUB,
            FontAwesomeIcon.RUBLE,
            FontAwesomeIcon.RUPEE,
            FontAwesomeIcon.SHEKEL,
            FontAwesomeIcon.SHEQEL,
            FontAwesomeIcon.TRY,
            FontAwesomeIcon.TURKISH_LIRA,
            FontAwesomeIcon.USD,
            FontAwesomeIcon.WON,
            FontAwesomeIcon.YEN
    ))
    val TEXT_EDITOR = iconSets(listOf(
            FontAwesomeIcon.ALIGN_CENTER,
            FontAwesomeIcon.ALIGN_JUSTIFY,
            FontAwesomeIcon.ALIGN_LEFT,
            FontAwesomeIcon.ALIGN_RIGHT,
            FontAwesomeIcon.BOLD,
            FontAwesomeIcon.CHAIN,
            FontAwesomeIcon.CHAIN_BROKEN,
            FontAwesomeIcon.CLIPBOARD,
            FontAwesomeIcon.COLUMNS,
            FontAwesomeIcon.COPY,
            FontAwesomeIcon.CUT,
            FontAwesomeIcon.DEDENT,
            FontAwesomeIcon.ERASER,
            FontAwesomeIcon.FILE,
            FontAwesomeIcon.FILE_TEXT,
            FontAwesomeIcon.FONT,
            FontAwesomeIcon.HEADER,
            FontAwesomeIcon.INDENT,
            FontAwesomeIcon.ITALIC,
            FontAwesomeIcon.LINK,
            FontAwesomeIcon.LIST,
            FontAwesomeIcon.LIST_ALT,
            FontAwesomeIcon.LIST_OL,
            FontAwesomeIcon.LIST_UL,
            FontAwesomeIcon.OUTDENT,
            FontAwesomeIcon.PAPERCLIP,
            FontAwesomeIcon.PARAGRAPH,
            FontAwesomeIcon.PASTE,
            FontAwesomeIcon.REPEAT,
            FontAwesomeIcon.ROTATE_LEFT,
            FontAwesomeIcon.ROTATE_RIGHT,
            FontAwesomeIcon.SAVE,
            FontAwesomeIcon.SCISSORS,
            FontAwesomeIcon.STRIKETHROUGH,
            FontAwesomeIcon.SUBSCRIPT,
            FontAwesomeIcon.SUPERSCRIPT,
            FontAwesomeIcon.TABLE,
            FontAwesomeIcon.TEXT_HEIGHT,
            FontAwesomeIcon.TEXT_WIDTH,
            FontAwesomeIcon.TH,
            FontAwesomeIcon.TH_LARGE,
            FontAwesomeIcon.TH_LIST,
            FontAwesomeIcon.UNDERLINE,
            FontAwesomeIcon.UNLINK
    ))
    val DIRECTIONAL = iconSets(listOf(
            FontAwesomeIcon.ANGLE_DOUBLE_DOWN,
            FontAwesomeIcon.ANGLE_DOUBLE_LEFT,
            FontAwesomeIcon.ANGLE_DOUBLE_RIGHT,
            FontAwesomeIcon.ANGLE_DOUBLE_UP,
            FontAwesomeIcon.ANGLE_DOWN,
            FontAwesomeIcon.ANGLE_LEFT,
            FontAwesomeIcon.ANGLE_RIGHT,
            FontAwesomeIcon.ANGLE_UP,
            FontAwesomeIcon.ARROW_CIRCLE_DOWN,
            FontAwesomeIcon.ARROW_CIRCLE_LEFT,
            FontAwesomeIcon.ARROW_CIRCLE_RIGHT,
            FontAwesomeIcon.ARROW_CIRCLE_UP,
            FontAwesomeIcon.ARROW_DOWN,
            FontAwesomeIcon.ARROW_LEFT,
            FontAwesomeIcon.ARROW_RIGHT,
            FontAwesomeIcon.ARROW_UP,
            FontAwesomeIcon.ARROWS,
            FontAwesomeIcon.ARROWS_ALT,
            FontAwesomeIcon.ARROWS_H,
            FontAwesomeIcon.ARROWS_V,
            FontAwesomeIcon.CARET_DOWN,
            FontAwesomeIcon.CARET_LEFT,
            FontAwesomeIcon.CARET_RIGHT,
            FontAwesomeIcon.CARET_UP,
            FontAwesomeIcon.CHEVRON_CIRCLE_DOWN,
            FontAwesomeIcon.CHEVRON_CIRCLE_LEFT,
            FontAwesomeIcon.CHEVRON_CIRCLE_RIGHT,
            FontAwesomeIcon.CHEVRON_CIRCLE_UP,
            FontAwesomeIcon.CHEVRON_DOWN,
            FontAwesomeIcon.CHEVRON_LEFT,
            FontAwesomeIcon.CHEVRON_RIGHT,
            FontAwesomeIcon.CHEVRON_UP,
            FontAwesomeIcon.EXCHANGE,
            FontAwesomeIcon.LONG_ARROW_DOWN,
            FontAwesomeIcon.LONG_ARROW_LEFT,
            FontAwesomeIcon.LONG_ARROW_RIGHT,
            FontAwesomeIcon.LONG_ARROW_UP,
            FontAwesomeIcon.TOGGLE_DOWN,
            FontAwesomeIcon.TOGGLE_LEFT,
            FontAwesomeIcon.TOGGLE_RIGHT,
            FontAwesomeIcon.TOGGLE_UP
    ))
    val VIDEO_PLAYER = iconSets(listOf(
            FontAwesomeIcon.ARROWS_ALT,
            FontAwesomeIcon.BACKWARD,
            FontAwesomeIcon.COMPRESS,
            FontAwesomeIcon.EJECT,
            FontAwesomeIcon.EXPAND,
            FontAwesomeIcon.FAST_BACKWARD,
            FontAwesomeIcon.FAST_FORWARD,
            FontAwesomeIcon.FORWARD,
            FontAwesomeIcon.PAUSE,
            FontAwesomeIcon.PAUSE_CIRCLE,
            FontAwesomeIcon.PLAY,
            FontAwesomeIcon.PLAY_CIRCLE,
            FontAwesomeIcon.RANDOM,
            FontAwesomeIcon.STEP_BACKWARD,
            FontAwesomeIcon.STEP_FORWARD,
            FontAwesomeIcon.STOP,
            FontAwesomeIcon.STOP_CIRCLE,
            FontAwesomeIcon.YOUTUBE_PLAY
    ))
    val BRAND = iconSets(listOf(
            FontAwesomeIcon.FA_500PX,
            FontAwesomeIcon.ADN,
            FontAwesomeIcon.AMAZON,
            FontAwesomeIcon.ANDROID,
            FontAwesomeIcon.ANGELLIST,
            FontAwesomeIcon.APPLE,
            FontAwesomeIcon.BANDCAMP,
            FontAwesomeIcon.BEHANCE,
            FontAwesomeIcon.BEHANCE_SQUARE,
            FontAwesomeIcon.BITBUCKET,
            FontAwesomeIcon.BITBUCKET_SQUARE,
            FontAwesomeIcon.BITCOIN,
            FontAwesomeIcon.BLACK_TIE,
            FontAwesomeIcon.BLUETOOTH,
            FontAwesomeIcon.BLUETOOTH_B,
            FontAwesomeIcon.BTC,
            FontAwesomeIcon.BUYSELLADS,
            FontAwesomeIcon.CC_AMEX,
            FontAwesomeIcon.CC_DINERS_CLUB,
            FontAwesomeIcon.CC_DISCOVER,
            FontAwesomeIcon.CC_JCB,
            FontAwesomeIcon.CC_MASTERCARD,
            FontAwesomeIcon.CC_PAYPAL,
            FontAwesomeIcon.CC_STRIPE,
            FontAwesomeIcon.CC_VISA,
            FontAwesomeIcon.CHROME,
            FontAwesomeIcon.CODEPEN,
            FontAwesomeIcon.CODIEPIE,
            FontAwesomeIcon.CONNECTDEVELOP,
            FontAwesomeIcon.CSS3,
            FontAwesomeIcon.DASHCUBE,
            FontAwesomeIcon.DELICIOUS,
            FontAwesomeIcon.DEVIANTART,
            FontAwesomeIcon.DIGG,
            FontAwesomeIcon.DRIBBBLE,
            FontAwesomeIcon.DROPBOX,
            FontAwesomeIcon.DRUPAL,
            FontAwesomeIcon.EDGE,
            FontAwesomeIcon.EERCAST,
            FontAwesomeIcon.EMPIRE,
            FontAwesomeIcon.ENVIRA,
            FontAwesomeIcon.ETSY,
            FontAwesomeIcon.EXPEDITEDSSL,
            FontAwesomeIcon.FA,
            FontAwesomeIcon.FACEBOOK,
            FontAwesomeIcon.FACEBOOK_F,
            FontAwesomeIcon.FACEBOOK_OFFICIAL,
            FontAwesomeIcon.FACEBOOK_SQUARE,
            FontAwesomeIcon.FIREFOX,
            FontAwesomeIcon.FIRST_ORDER,
            FontAwesomeIcon.FLICKR,
            FontAwesomeIcon.FONT_AWESOME,
            FontAwesomeIcon.FONTICONS,
            FontAwesomeIcon.FORT_AWESOME,
            FontAwesomeIcon.FORUMBEE,
            FontAwesomeIcon.FOURSQUARE,
            FontAwesomeIcon.FREE_CODE_CAMP,
            FontAwesomeIcon.GE,
            FontAwesomeIcon.GET_POCKET,
            FontAwesomeIcon.GG,
            FontAwesomeIcon.GG_CIRCLE,
            FontAwesomeIcon.GIT,
            FontAwesomeIcon.GIT_SQUARE,
            FontAwesomeIcon.GITHUB,
            FontAwesomeIcon.GITHUB_ALT,
            FontAwesomeIcon.GITHUB_SQUARE,
            FontAwesomeIcon.GITLAB,
            FontAwesomeIcon.GITTIP,
            FontAwesomeIcon.GLIDE,
            FontAwesomeIcon.GLIDE_G,
            FontAwesomeIcon.GOOGLE,
            FontAwesomeIcon.GOOGLE_PLUS,
            FontAwesomeIcon.GOOGLE_PLUS_CIRCLE,
            FontAwesomeIcon.GOOGLE_PLUS_OFFICIAL,
            FontAwesomeIcon.GOOGLE_PLUS_SQUARE,
            FontAwesomeIcon.GOOGLE_WALLET,
            FontAwesomeIcon.GRATIPAY,
            FontAwesomeIcon.GRAV,
            FontAwesomeIcon.HACKER_NEWS,
            FontAwesomeIcon.HOUZZ,
            FontAwesomeIcon.HTML5,
            FontAwesomeIcon.IMDB,
            FontAwesomeIcon.INSTAGRAM,
            FontAwesomeIcon.INTERNET_EXPLORER,
            FontAwesomeIcon.IOXHOST,
            FontAwesomeIcon.JOOMLA,
            FontAwesomeIcon.JSFIDDLE,
            FontAwesomeIcon.LASTFM,
            FontAwesomeIcon.LASTFM_SQUARE,
            FontAwesomeIcon.LEANPUB,
            FontAwesomeIcon.LINKEDIN,
            FontAwesomeIcon.LINKEDIN_SQUARE,
            FontAwesomeIcon.LINODE,
            FontAwesomeIcon.LINUX,
            FontAwesomeIcon.MAXCDN,
            FontAwesomeIcon.MEANPATH,
            FontAwesomeIcon.MEDIUM,
            FontAwesomeIcon.MEETUP,
            FontAwesomeIcon.MIXCLOUD,
            FontAwesomeIcon.MODX,
            FontAwesomeIcon.ODNOKLASSNIKI,
            FontAwesomeIcon.ODNOKLASSNIKI_SQUARE,
            FontAwesomeIcon.OPENCART,
            FontAwesomeIcon.OPENID,
            FontAwesomeIcon.OPERA,
            FontAwesomeIcon.OPTIN_MONSTER,
            FontAwesomeIcon.PAGELINES,
            FontAwesomeIcon.PAYPAL,
            FontAwesomeIcon.PIED_PIPER,
            FontAwesomeIcon.PIED_PIPER_ALT,
            FontAwesomeIcon.PIED_PIPER_PP,
            FontAwesomeIcon.PINTEREST,
            FontAwesomeIcon.PINTEREST_P,
            FontAwesomeIcon.PINTEREST_SQUARE,
            FontAwesomeIcon.PRODUCT_HUNT,
            FontAwesomeIcon.QQ,
            FontAwesomeIcon.QUORA,
            FontAwesomeIcon.RA,
            FontAwesomeIcon.RAVELRY,
            FontAwesomeIcon.REBEL,
            FontAwesomeIcon.REDDIT,
            FontAwesomeIcon.REDDIT_ALIEN,
            FontAwesomeIcon.REDDIT_SQUARE,
            FontAwesomeIcon.RENREN,
            FontAwesomeIcon.RESISTANCE,
            FontAwesomeIcon.SAFARI,
            FontAwesomeIcon.SCRIBD,
            FontAwesomeIcon.SELLSY,
            FontAwesomeIcon.SHARE_ALT,
            FontAwesomeIcon.SHARE_ALT_SQUARE,
            FontAwesomeIcon.SHIRTSINBULK,
            FontAwesomeIcon.SIMPLYBUILT,
            FontAwesomeIcon.SKYATLAS,
            FontAwesomeIcon.SKYPE,
            FontAwesomeIcon.SLACK,
            FontAwesomeIcon.SLIDESHARE,
            FontAwesomeIcon.SNAPCHAT,
            FontAwesomeIcon.SNAPCHAT_GHOST,
            FontAwesomeIcon.SNAPCHAT_SQUARE,
            FontAwesomeIcon.SOUNDCLOUD,
            FontAwesomeIcon.SPOTIFY,
            FontAwesomeIcon.STACK_EXCHANGE,
            FontAwesomeIcon.STACK_OVERFLOW,
            FontAwesomeIcon.STEAM,
            FontAwesomeIcon.STEAM_SQUARE,
            FontAwesomeIcon.STUMBLEUPON,
            FontAwesomeIcon.STUMBLEUPON_CIRCLE,
            FontAwesomeIcon.SUPERPOWERS,
            FontAwesomeIcon.TELEGRAM,
            FontAwesomeIcon.THEMEISLE,
            FontAwesomeIcon.TRIPADVISOR,
            FontAwesomeIcon.TUMBLR,
            FontAwesomeIcon.TUMBLR_SQUARE,
            FontAwesomeIcon.TWITCH,
            FontAwesomeIcon.TWITTER,
            FontAwesomeIcon.TWITTER_SQUARE,
            FontAwesomeIcon.USB,
            FontAwesomeIcon.VIACOIN,
            FontAwesomeIcon.VIADEO_SQUARE,
            FontAwesomeIcon.VIMEO_SQUARE,
            FontAwesomeIcon.VINE,
            FontAwesomeIcon.VK,
            FontAwesomeIcon.WECHAT,
            FontAwesomeIcon.WEIXIN,
            FontAwesomeIcon.WHATSAPP,
            FontAwesomeIcon.WIKIPEDIA_W,
            FontAwesomeIcon.WINDOWS,
            FontAwesomeIcon.WORDPRESS,
            FontAwesomeIcon.WPBEGINNER,
            FontAwesomeIcon.WPEXPLORER,
            FontAwesomeIcon.WPFORMS,
            FontAwesomeIcon.XING,
            FontAwesomeIcon.XING_SQUARE,
            FontAwesomeIcon.Y_COMBINATOR,
            FontAwesomeIcon.Y_COMBINATOR_SQUARE,
            FontAwesomeIcon.YC,
            FontAwesomeIcon.YC_SQUARE,
            FontAwesomeIcon.YELP,
            FontAwesomeIcon.YOAST,
            FontAwesomeIcon.YOUTUBE,
            FontAwesomeIcon.YOUTUBE_PLAY,
            FontAwesomeIcon.YOUTUBE_SQUARE
    ))
    val MEDICAL = iconSets(listOf(
            FontAwesomeIcon.AMBULANCE,
            FontAwesomeIcon.H_SQUARE,
            FontAwesomeIcon.HEART,
            FontAwesomeIcon.HEARTBEAT,
            FontAwesomeIcon.MEDKIT,
            FontAwesomeIcon.PLUS_SQUARE,
            FontAwesomeIcon.STETHOSCOPE,
            FontAwesomeIcon.USER_MD,
            FontAwesomeIcon.WHEELCHAIR,
            FontAwesomeIcon.WHEELCHAIR_ALT
    ))

    val ALL = Set("All", emptyList())

    val VALUES = listOf(
            ALL,
            Set("Web Application", WEB_APPLICATION),
            Set("Accessibility", ACCESSIBILITY),
            Set("Hand", HAND),
            Set("Transportation", TRANSPORTATION),
            Set("Gender", GENDER),
            Set("File Type", FILE_TYPE),
            Set("Spinner", SPINNER),
            Set("Form Control", FORM_CONTROL),
            Set("Pay Ment", PAY_MENT),
            Set("Chart", CHART),
            Set("Currency", CURRENCY),
            Set("Text Editor", TEXT_EDITOR),
            Set("Directional", DIRECTIONAL),
            Set("Video Player", VIDEO_PLAYER),
            Set("Brand", BRAND),
            Set("Medical", MEDICAL)
    )
}
