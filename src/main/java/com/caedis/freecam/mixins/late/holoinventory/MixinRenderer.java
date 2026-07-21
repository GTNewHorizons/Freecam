package com.caedis.freecam.mixins.late.holoinventory;

import net.dries007.holoInventory.client.Renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.RevealGate;

@Mixin(value = Renderer.class, remap = false)
public class MixinRenderer {

    // Single entry point for every hologram draw.
    @Inject(method = "renderEvent", at = @At("HEAD"), cancellable = true)
    private void freecam$hideHologram(CallbackInfo ci) {
        if (RevealGate.shouldHide()) {
            ci.cancel();
        }
    }
}
