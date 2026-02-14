package me.laz.torProxy.client;

import me.laz.torProxy.client.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class TorProxyClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Tor");

    @Override
    public void onInitializeClient() {
        ConfigManager.loadConfig();
        LOGGER.info("Initialized client.");
    }
}
