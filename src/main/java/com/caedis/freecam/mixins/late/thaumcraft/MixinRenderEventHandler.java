package com.caedis.freecam.mixins.late.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.RevealGate;

import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
public class MixinRenderEventHandler {

    // Goggles aspect tags, scan result tags, note text, wand build preview.
    @Inject(method = "blockHighlight", at = @At("HEAD"), cancellable = true)
    private void freecam$hideBlockHighlight(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }

    // Thaumometer scan pulse and golem markers.
    @Inject(method = "renderLast", at = @At("HEAD"), cancellable = true)
    private void freecam$hideRenderLast(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }
}
