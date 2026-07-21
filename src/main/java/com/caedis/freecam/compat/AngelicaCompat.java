package com.caedis.freecam.compat;

import com.caedis.freecam.camera.FreecamController;
import com.caedis.freecam.config.MiscConfig;
import com.gtnewhorizons.angelica.event.RenderHandEvent;

import cpw.mods.fml.common.Optional;

// Moved to its own class so the predicate does not classload
public class AngelicaCompat {

    // Angelica posts this from HandRenderer#canRender; cancelling hides the hand under shaders.
    @Optional.Method(modid = "angelica")
    public static void registerAngelicaHandListener() {
        RenderHandEvent.BUS.addListener(
            event -> FreecamController.instance()
                .isActive() && !MiscConfig.showHand);
    }
}
