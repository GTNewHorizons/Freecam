package com.caedis.freecam.mixins.late.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.RevealGate;

import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
public class MixinRenderEventHandler {

    // Targets the draw calls rather than the per-frame renderLast/blockHighlight event methods,
    // so no CallbackInfo is allocated unless something is actually being drawn.

    // Goggles and scan aspect tags.
    @Inject(method = "drawTagsOnContainer", at = @At("HEAD"), cancellable = true)
    private void freecam$hideTagsOnContainer(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }

    // Goggles note text.
    @Inject(method = "drawTextInAir", at = @At("HEAD"), cancellable = true)
    private void freecam$hideTextInAir(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }

    // Thaumometer scan pulse.
    @Inject(method = "showScannedBlocks", at = @At("HEAD"), cancellable = true)
    private void freecam$hideScannedBlocks(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }

    // Golem placer/bell markers.
    @Inject(method = "renderMarkedBlocks", at = @At("HEAD"), cancellable = true)
    private void freecam$hideMarkedBlocks(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }
}
