package com.caedis.freecam.config;

import com.caedis.freecam.FreecamMod;
import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = FreecamMod.MODID)
public class GeneralConfig {

    @Config.Sync
    @Config.Comment("[Server Controlled] Completely disable freecam functionality")
    @Config.DefaultBoolean(false)
    public static boolean disabled;

    @Config.Sync
    @Config.Comment("[Server Controlled] Camera collision mode: FULL (collide with all blocks), IGNORE_TRANSPARENT (pass through non-opaque blocks), IGNORE_OPENABLE (pass through doors/trapdoors/fence gates), NONE (no collision)")
    @Config.DefaultEnum("FULL")
    public static CollisionMode collisionMode;

    @Config.Comment("Disable freecam when the player takes damage")
    @Config.DefaultBoolean(true)
    public static boolean disableOnDamage;

    public enum CollisionMode {
        FULL,
        IGNORE_TRANSPARENT,
        IGNORE_OPENABLE,
        NONE
    }
}
