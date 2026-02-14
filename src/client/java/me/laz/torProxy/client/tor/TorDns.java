package me.laz.torProxy.client.tor;

import me.laz.torProxy.client.config.ConfigManager;
import net.minecraft.client.network.ServerAddress;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TorDns {
    private TorDns(){};

    // store the original server address for the thread that is performing the connect
    public static final ThreadLocal<ServerAddress> LAST_ADDRESS = new ThreadLocal<>();

    private static final AtomicInteger NEXT_PORT = new AtomicInteger(40000);

    private static final ConcurrentHashMap<Integer, ServerAddress> PING_MAP = new ConcurrentHashMap<>();

    public static int registerPing(ServerAddress address) {
        //int port = NEXT_PORT.getAndIncrement();
        int port = NEXT_PORT.getAndUpdate(current -> {
            int next = current + 1;
            if (next > 65535) next = 40000;
            return next;
        });
        PING_MAP.put(port, address);
        return port;
    }

    public static ServerAddress consumePing(int port) {
        return PING_MAP.remove(port);
    }
}
