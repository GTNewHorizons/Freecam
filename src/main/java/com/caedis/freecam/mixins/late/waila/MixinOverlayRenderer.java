package com.caedis.freecam.mixins.late.waila;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.RevealGate;

import mcp.mobius.waila.overlay.OverlayRenderer;

@Mixin(value = OverlayRenderer.class, remap = false)
public class MixinOverlayRenderer {

    // Target is static, so this handler must be static too.
    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private static void freecam$hideOverlay(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }
}
