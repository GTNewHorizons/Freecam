package com.caedis.freecam.mixins.early.minecraft;

import net.minecraft.client.settings.KeyBinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.caedis.freecam.camera.FreecamController;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {

    @Shadow
    public abstract String getKeyCategory();

    @Inject(method = "getIsKeyPressed", at = @At("HEAD"), cancellable = true)
    private void freecam$suppressMovementKeys(CallbackInfoReturnable<Boolean> cir) {
        FreecamController controller = FreecamController.instance();
        if (controller.isActive() && !controller.isPlayerControlled()) {
            if ("key.categories.movement".equals(getKeyCategory())) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "isPressed", at = @At("HEAD"), cancellable = true)
    private void freecam$suppressMovementKeyPress(CallbackInfoReturnable<Boolean> cir) {
        FreecamController controller = FreecamController.instance();
        if (controller.isActive() && !controller.isPlayerControlled()) {
            if ("key.categories.movement".equals(getKeyCategory())) {
                cir.setReturnValue(false);
            }
        }
    }
}
