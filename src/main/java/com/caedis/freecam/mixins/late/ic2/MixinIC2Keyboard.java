package com.caedis.freecam.mixins.late.ic2;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.caedis.freecam.camera.FreecamController;

@Mixin(value = ic2.core.util.Keyboard.class, remap = false)
public abstract class MixinIC2Keyboard {

    @Inject(method = "isJumpKeyDown", at = @At("HEAD"), cancellable = true)
    private static void disableJumpInFreecam(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        FreecamController controller = FreecamController.instance();
        // Only disable jetpack jump when freecam is active AND camera is being controlled
        if (controller.isActive() && !controller.isPlayerControlled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isBoostKeyDown", at = @At("HEAD"), cancellable = true)
    private static void disableBoostInFreecam(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        FreecamController controller = FreecamController.instance();
        // Only disable jetpack boost when freecam is active AND camera is being controlled
        if (controller.isActive() && !controller.isPlayerControlled()) {
            cir.setReturnValue(false);
        }
    }
}
