package com.caedis.freecam.camera;

import com.caedis.freecam.config.MiscConfig;
import com.caedis.freecam.config.MiscConfig.OverlayVisibility;

public final class RevealGate {

    private RevealGate() {}

    // True when overlays should be hidden: in freecam and configured to hide.
    public static boolean shouldHide() {
        return MiscConfig.overlayVisibility == OverlayVisibility.HIDE && FreecamController.instance()
            .isActive();
    }
}
