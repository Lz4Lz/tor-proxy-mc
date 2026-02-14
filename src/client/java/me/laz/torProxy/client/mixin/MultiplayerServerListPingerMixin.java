package me.laz.torProxy.client.mixin;


import me.laz.torProxy.client.config.ConfigManager;
import me.laz.torProxy.client.tor.TorDns;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MultiplayerServerListPinger.class)
public abstract class MultiplayerServerListPingerMixin {

    @ModifyArg(
            method = "add",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AllowedAddressResolver;resolve(Lnet/minecraft/client/network/ServerAddress;)Ljava/util/Optional;"
            )
    )
    private ServerAddress routePingThroughTor(ServerAddress original) {
        if (!ConfigManager.config.torEnabled) {
            return new ServerAddress(original.getAddress(), original.getPort());
        }
        int port = TorDns.registerPing(original);
        return new ServerAddress("127.0.0.1", port);
    }
}
