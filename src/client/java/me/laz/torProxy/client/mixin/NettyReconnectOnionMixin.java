package me.laz.torProxy.client.mixin;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import me.laz.torProxy.client.config.ConfigManager;
import net.minecraft.client.network.ServerAddress;
import me.laz.torProxy.client.tor.TorDns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Mixin(value = Bootstrap.class, remap = false)
abstract class NettyReconnectOnionMixin {

    @Shadow
    public abstract ChannelFuture connect(java.net.SocketAddress remoteAddress);

    @Inject(
            method = "connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void reconnectUsingOriginalHost(
            InetAddress address,
            int port,
            CallbackInfoReturnable<ChannelFuture> cir
    ) {

        if (!ConfigManager.config.torEnabled) return;

        ServerAddress ping = TorDns.consumePing(port);
        if (ping != null) {
            cir.setReturnValue(
                    this.connect(
                            InetSocketAddress.createUnresolved(
                                    ping.getAddress(),
                                    ping.getPort()
                            )
                    )
            );
            return;
        }

        ServerAddress join = TorDns.LAST_ADDRESS.get();
        if (join != null) {
            cir.setReturnValue(
                    this.connect(
                            InetSocketAddress.createUnresolved(
                                    join.getAddress(),
                                    join.getPort()
                            )
                    )
            );
        }
    }
}
