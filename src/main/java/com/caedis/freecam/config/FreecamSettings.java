package com.caedis.freecam.config;

import com.caedis.freecam.config.GeneralConfig.CollisionMode;
import com.caedis.freecam.config.MiscConfig.OverlayVisibility;
import com.caedis.freecam.network.FreecamPermissionState;
import com.caedis.freecam.network.ServerPermission;

/** Effective values: server permission override when present, otherwise config. */
public final class FreecamSettings {

    private FreecamSettings() {}

    public static boolean disabled() {
        FreecamPermissionState state = ServerPermission.state();
        return state != null ? !state.allowed : GeneralConfig.disabled;
    }

    public static CollisionMode collisionMode() {
        FreecamPermissionState state = ServerPermission.state();
        return state != null ? state.collisionMode : GeneralConfig.collisionMode;
    }

    public static boolean fullBright() {
        FreecamPermissionState state = ServerPermission.state();
        return state != null ? state.fullBright : MiscConfig.fullBright;
    }

    public static boolean disableSubmersionFog() {
        FreecamPermissionState state = ServerPermission.state();
        return state != null ? state.disableSubmersionFog : MiscConfig.disableSubmersionFog;
    }

    public static OverlayVisibility overlayVisibility() {
        FreecamPermissionState state = ServerPermission.state();
        return state != null ? state.overlayVisibility : MiscConfig.overlayVisibility;
    }
}
