package me.laz.torProxy.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Tor Proxy Config"));

        builder.setSavingRunnable(ConfigManager::saveConfig);

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Tor"), ConfigManager.config.torEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(value -> ConfigManager.config.torEnabled = value)
                .build());

        general.addEntry(entryBuilder
                .startStrField(Text.literal("Tor Host"), ConfigManager.config.torHost)
                .setDefaultValue("127.0.0.1")
                .setSaveConsumer(value -> ConfigManager.config.torHost = value)
                .build());

        general.addEntry(entryBuilder
                .startIntField(Text.literal("Tor Port"), ConfigManager.config.torPort)
                .setDefaultValue(9050)
                .setSaveConsumer(value -> ConfigManager.config.torPort = value)
                .build());

        return builder.build();
    }
}
