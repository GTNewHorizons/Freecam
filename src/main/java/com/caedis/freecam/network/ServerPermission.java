package com.caedis.freecam.network;

import javax.annotation.Nullable;

/** Whether the server allows this player to use freecam. Null means no override. */
public final class ServerPermission {

    private static volatile Boolean allowed;

    private ServerPermission() {}

    @Nullable
    public static Boolean allowed() {
        return allowed;
    }

    public static void set(boolean newAllowed) {
        allowed = newAllowed;
    }

    public static void clear() {
        allowed = null;
    }
}
