package com.qouteall.hiding_in_the_bushes;

import com.qouteall.immersive_portals.McHelper;
import com.qouteall.immersive_portals.PehkuiInterface;
import com.qouteall.immersive_portals.portal.Portal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.api.ScaleData;

public class PehkuiInterfaceInitializer {
    
    public static void init() {
        if (!O_O.isDedicatedServer()) {
            PehkuiInterface.onClientPlayerTeleported = PehkuiInterfaceInitializer::onPlayerTeleportedClient;
        }
        
        PehkuiInterface.onServerEntityTeleported = PehkuiInterfaceInitializer::onEntityTeleportedServer;
    }
    
    @Environment(EnvType.CLIENT)
    private static void onPlayerTeleportedClient(Portal portal) {
        if (portal.scaling != 1.0) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            ScaleData scaleData = ScaleData.of(player);
            Vec3d eyePos = McHelper.getEyePos(player);
            Vec3d lastTickEyePos = McHelper.getLastTickEyePos(player);
    
            float oldScale = scaleData.getScale();
            
            scaleData.setScaleTickDelay(0);
            scaleData.setScale((float) (oldScale * portal.scaling));
            scaleData.setTargetScale((float) (oldScale * portal.scaling));
            scaleData.tick();
    
            McHelper.setEyePos(player, eyePos, lastTickEyePos);
        }
    }
    
    private static void onEntityTeleportedServer(Entity entity, Portal portal) {
        if (portal.scaling != 1.0) {
            ScaleData scaleData = ScaleData.of(entity);
            Vec3d eyePos = McHelper.getEyePos(entity);
            Vec3d lastTickEyePos = McHelper.getLastTickEyePos(entity);
            
            float oldScale = scaleData.getScale();
            
            scaleData.setScaleTickDelay(0);
            scaleData.setScale((float) (oldScale * portal.scaling));
            scaleData.setTargetScale((float) (oldScale * portal.scaling));
            scaleData.tick();
    
            McHelper.setEyePos(entity, eyePos, lastTickEyePos);
        }
    }
}