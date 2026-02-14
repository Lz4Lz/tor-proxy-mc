package me.laz.torProxy.client.mixin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import me.laz.torProxy.client.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.laz.torProxy.client.*;

import java.net.SocketAddress;

// Prevents normal DNS, letting Tor handle DNS resolving.
// Which also lets you connect to .onion servers.

@Mixin(value = io.netty.bootstrap.Bootstrap.class, remap = false)
abstract class NettyNoDnsMixin {

    @Shadow
    public abstract ChannelFuture connect(SocketAddress remoteAddress);

    @Inject(
            method = "doResolveAndConnect0",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableNettyResolver(
            Channel channel,
            SocketAddress remoteAddress,
            SocketAddress localAddress,
            ChannelPromise promise,
            CallbackInfoReturnable<ChannelFuture> cir
    ) {
        if (!ConfigManager.config.torEnabled) return;
        cir.setReturnValue(channel.connect(remoteAddress, localAddress, promise));
    }
}
