package com.caedis.freecam.mixins.early.angelica;

import net.coderbot.iris.pipeline.HandRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.caedis.freecam.camera.FreecamController;
import com.caedis.freecam.config.MiscConfig;

@Mixin(value = HandRenderer.class, remap = false)
public class MixinHandRenderer {

    @Inject(method = "canRender", at = @At("HEAD"), cancellable = true)
    private void freecam$disableHand(CallbackInfoReturnable<Boolean> cir) {
        if (FreecamController.instance()
            .isActive() && !MiscConfig.showHand) {
            cir.setReturnValue(false);
        }
    }
}
