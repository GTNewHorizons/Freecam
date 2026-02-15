package com.caedis.freecam.config;

import com.caedis.freecam.FreecamMod;
import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = FreecamMod.MODID, category = "misc")
public class MiscConfig {

    @Config.Comment("Enable full brightness while in freecam")
    @Config.DefaultBoolean(false)
    public static boolean fullBright;

    @Config.Comment("Disable fog when camera is submerged in water or lava")
    @Config.DefaultBoolean(true)
    public static boolean disableSubmersionFog;

    @Config.Comment("Camera perspective when entering freecam")
    @Config.DefaultEnum("INSIDE")
    public static Perspective initialPerspective;

    @Config.Comment("Show the player's hand while in freecam")
    @Config.DefaultBoolean(false)
    public static boolean showHand;

    public enum Perspective {

        INSIDE(0),
        FIRST_PERSON(0),
        THIRD_PERSON(1),
        THIRD_PERSON_MIRROR(2);

        public final int thirdPersonView;

        Perspective(int thirdPersonView) {
            this.thirdPersonView = thirdPersonView;
        }
    }
}
