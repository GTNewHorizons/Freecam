package com.caedis.freecam.config;

import com.caedis.freecam.network.ServerPermission;

/** Freecam access: the server's per-player permission when it sent one, otherwise the config. */
public final class FreecamSettings {

    private FreecamSettings() {}

    public static boolean disabled() {
        Boolean allowed = ServerPermission.allowed();
        return allowed != null ? !allowed : GeneralConfig.disabled;
    }
}
