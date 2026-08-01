package com.caedis.freecam.network;

import java.util.Objects;

import com.caedis.freecam.config.GeneralConfig.CollisionMode;
import com.caedis.freecam.config.MiscConfig.OverlayVisibility;

/** Per-player freecam permissions resolved on the server. */
public final class FreecamPermissionState {

    public final boolean allowed;
    public final CollisionMode collisionMode;
    public final boolean fullBright;
    public final boolean disableSubmersionFog;
    public final OverlayVisibility overlayVisibility;

    public FreecamPermissionState(boolean allowed, CollisionMode collisionMode, boolean fullBright,
        boolean disableSubmersionFog, OverlayVisibility overlayVisibility) {
        this.allowed = allowed;
        this.collisionMode = collisionMode;
        this.fullBright = fullBright;
        this.disableSubmersionFog = disableSubmersionFog;
        this.overlayVisibility = overlayVisibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FreecamPermissionState other)) return false;
        return allowed == other.allowed && collisionMode == other.collisionMode
            && fullBright == other.fullBright
            && disableSubmersionFog == other.disableSubmersionFog
            && overlayVisibility == other.overlayVisibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowed, collisionMode, fullBright, disableSubmersionFog, overlayVisibility);
    }
}
