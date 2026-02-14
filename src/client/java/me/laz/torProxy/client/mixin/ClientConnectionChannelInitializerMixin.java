package me.laz.torProxy.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks5ProxyHandler;

import java.net.InetSocketAddress;
import me.laz.torProxy.client.config.ConfigManager;


@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public abstract class ClientConnectionChannelInitializerMixin {

    @Inject(
            method = "initChannel(Lio/netty/channel/Channel;)V",
            at = @At("HEAD")
    )
    private void addTorProxy(Channel channel, CallbackInfo ci) {
        if (!ConfigManager.config.torEnabled) return;

        channel.pipeline().addFirst(
                "tor_proxy",
                new Socks5ProxyHandler(new InetSocketAddress(
                        ConfigManager.config.torHost,
                        ConfigManager.config.torPort
                ))
        );
    }
}
