package com.caedis.freecam.mixins.late.waila;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.caedis.freecam.camera.RevealGate;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import mcp.mobius.waila.overlay.OverlayRenderer;

@Mixin(value = OverlayRenderer.class, remap = false)
public class MixinOverlayRenderer {

    // Gates both the draw and the per-tick raytrace/tooltip build in WailaTickHandler.
    @ModifyReturnValue(method = "isOverlayVisible", at = @At("RETURN"))
    private static boolean freecam$hideOverlay(boolean original) {
        return original && !RevealGate.shouldHide();
    }
}
