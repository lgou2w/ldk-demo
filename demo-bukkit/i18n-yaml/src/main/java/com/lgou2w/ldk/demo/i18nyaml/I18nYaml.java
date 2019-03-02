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

package com.lgou2w.ldk.demo.i18nyaml;

import com.lgou2w.ldk.bukkit.PluginBase;
import com.lgou2w.ldk.bukkit.i18n.PluginResourceProvider;
import com.lgou2w.ldk.bukkit.i18n.YamlConfigurationAdapter;
import com.lgou2w.ldk.i18n.Language;
import com.lgou2w.ldk.i18n.LanguageManager;
import com.lgou2w.ldk.i18n.SimpleLanguageManager;
import com.lgou2w.ldk.i18n.StringFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class I18nYaml extends PluginBase {

    private LanguageManager languageManager;
    private Language language;

    @Override protected void load() { }
    @Override protected void enable() {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        languageManager = new SimpleLanguageManager(
                "i18n", new YamlConfigurationAdapter(),
                new PluginResourceProvider(this)
        );
        languageManager.setGlobalFormatter(new StringFormatter());
        try {
            language = languageManager.load(Locale.ROOT);
            getLogger().info("total: " + language.getSize());
            getLogger().info(language.get("msg"));
            getLogger().info(language.get("msg.hi"));
            getLogger().info(language.get("a.b.c.d"));
            getLogger().info(language.get("format", "abc"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override protected void disable() {
        Map<String, String> entries = new HashMap<>(language.getSize());
        for (Map.Entry<String, String> entry : language.getEntries())
            entries.put(entry.getKey(), entry.getValue());
        try (FileOutputStream output = new FileOutputStream(new File(getDataFolder(), "i18n.yml"))) {
            new YamlConfigurationAdapter().readapt(output, entries);
        } catch (Exception e) {
            e.printStackTrace();
        }
        language = null;
        languageManager = null;
    }
}
