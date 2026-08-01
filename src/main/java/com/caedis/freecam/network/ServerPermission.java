package com.caedis.freecam.network;

import javax.annotation.Nullable;

/** Last permission state received from the server. Null means no override. */
public final class ServerPermission {

    private static volatile FreecamPermissionState state;

    private ServerPermission() {}

    @Nullable
    public static FreecamPermissionState state() {
        return state;
    }

    public static void set(FreecamPermissionState newState) {
        state = newState;
    }

    public static void clear() {
        state = null;
    }
}
