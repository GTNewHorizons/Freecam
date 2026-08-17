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

    @Config.Sync
    @Config.Comment("[Server Controlled] Maximum distance the camera can travel from the player. Set to 0 to use render distance.")
    @Config.DefaultInt(0)
    @Config.RangeInt(min = 0)
    public static int maxDistance;

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
