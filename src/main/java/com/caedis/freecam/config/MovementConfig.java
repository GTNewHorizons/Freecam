package com.caedis.freecam.config;

import com.caedis.freecam.FreecamMod;
import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = FreecamMod.MODID, category = "movement")
public class MovementConfig {

    @Config.Comment("Freecam default movement speed")
    @Config.DefaultFloat(1.0f)
    @Config.RangeFloat(min = 0.2f, max = 10.0f)
    public static float speed;

    @Config.Comment("Movement mode: STATIC (instant stop) or CREATIVE (momentum/drift)")
    @Config.DefaultEnum("CREATIVE")
    public static MovementMode movementMode;

    public enum MovementMode {
        CREATIVE,
        STATIC
    }
}
