package com.caedis.freecam.camera.tripod;

import java.util.EnumMap;

import net.minecraft.client.Minecraft;

import com.caedis.freecam.camera.CameraEntity;
import com.caedis.freecam.config.GeneralConfig;

public class TripodRegistry {

    private final EnumMap<TripodSlot, CameraEntity> cameras = new EnumMap<>(TripodSlot.class);

    public CameraEntity getOrCreate(TripodSlot slot) {
        CameraEntity camera = cameras.get(slot);
        if (camera == null) {
            Minecraft mc = Minecraft.getMinecraft();
            camera = new CameraEntity(mc.theWorld, mc.thePlayer);
            camera.setCollisionMode(GeneralConfig.collisionMode);
            cameras.put(slot, camera);
        }
        return camera;
    }

    public void clear() {
        cameras.clear();
    }
}
