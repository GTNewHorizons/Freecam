package com.caedis.freecam.config;

import com.caedis.freecam.FreecamMod;
import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = FreecamMod.MODID, category = "misc")
public class MiscConfig {

    @Config.Sync
    @Config.Comment("Enable full brightness while in freecam")
    @Config.DefaultBoolean(false)
    public static boolean fullBright;

    @Config.Sync
    @Config.Comment("Disable fog when camera is submerged in water or lava")
    @Config.DefaultBoolean(false)
    public static boolean disableSubmersionFog;

    @Config.Sync
    @Config.Comment("Show overlays (WAILA, etc.) while in freecam")
    @Config.DefaultEnum("HIDE")
    public static OverlayVisibility overlayVisibility;

    @Config.Comment("Camera perspective when entering freecam")
    @Config.DefaultEnum("INSIDE")
    public static Perspective initialPerspective;

    @Config.Comment("Show the player's hand while in freecam")
    @Config.DefaultBoolean(false)
    public static boolean showHand;

    public enum Perspective {
        INSIDE,
        FIRST_PERSON,
        THIRD_PERSON,
        THIRD_PERSON_MIRROR
    }

    public enum OverlayVisibility {
        HIDE,
        SHOW
    }
}
