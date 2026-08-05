package com.caedis.freecam.mixins.late.gregtech;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;

import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.behaviors.JetpackBehavior;

/**
 * Mixin to disable GregTech Mechanical Armour jetpack controls during freecam. Targets
 * the JetpackBehavior class which handles jetpack functionality for GT armor with jetpack addons.
 */
@Mixin(value = JetpackBehavior.class, remap = false)
public abstract class MixinGregTechJetpack {

    /**
     * Prevent the performFlying method from executing during freecam by canceling it at HEAD. This prevents all
     * jetpack movement, energy drain, and particle effects when freecam is active and the camera (not the player) is
     * being controlled.
     */
    @Inject(method = "performFlying", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableJetpackInFreecam(ArmorContext context, CallbackInfo ci) {
        FreecamController controller = FreecamController.instance();
        // Only disable jetpack when freecam is active AND camera is being controlled
        if (controller.isActive() && !controller.isPlayerControlled()) {
            ci.cancel();
        }
    }
}
