package com.qouteall.immersive_portals.mixin_client.sync;

import com.qouteall.immersive_portals.dimension_sync.DimId;
import com.qouteall.immersive_portals.ducks.IEPlayerMoveC2SPacket;
import com.qouteall.immersive_portals.network.NetworkAdapt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerMoveC2SPacket.class)
public class MixinPlayerMoveC2SPacket_C {
    @Inject(
        method = "<init>(Z)V",
        at = @At("RETURN")
    )
    private void onConstruct(boolean boolean_1, CallbackInfo ci) {
        RegistryKey<World> dimension = MinecraftClient.getInstance().player.world.getRegistryKey();
        ((IEPlayerMoveC2SPacket) this).setPlayerDimension(dimension);
        assert dimension == MinecraftClient.getInstance().world.getRegistryKey();
    }
    
    @Inject(
        method = "write",
        at = @At("HEAD")
    )
    private void onWrite(PacketByteBuf buf, CallbackInfo ci) {
        if (NetworkAdapt.doesServerHasIP()) {
            RegistryKey<World> playerDimension = ((IEPlayerMoveC2SPacket) this).getPlayerDimension();
            DimId.writeWorldId(buf, playerDimension, true);
        }
    }
}
