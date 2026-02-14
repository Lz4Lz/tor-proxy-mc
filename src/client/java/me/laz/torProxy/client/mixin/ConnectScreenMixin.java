package me.laz.torProxy.client.mixin;
import me.laz.torProxy.client.config.ConfigManager;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import me.laz.torProxy.client.tor.TorDns;


@Mixin(targets = "net.minecraft.client.gui.screen.multiplayer.ConnectScreen$1")
abstract class ConnectScreenMixin {

    @ModifyArg(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AllowedAddressResolver;resolve(Lnet/minecraft/client/network/ServerAddress;)Ljava/util/Optional;"
            )
    )
    private ServerAddress skipDnsResolution(ServerAddress original) {

        if (!ConfigManager.config.torEnabled) {
            return new ServerAddress(original.getAddress(), original.getPort());
        }

        TorDns.LAST_ADDRESS.set(original);

        return new ServerAddress("127.0.0.1", 25565);
    }
}
