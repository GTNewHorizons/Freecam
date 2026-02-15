package com.caedis.freecam.mixins.early.minecraft;

import net.minecraft.util.MovementInputFromOptions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions {

    @Inject(method = "updatePlayerMoveState", at = @At("RETURN"))
    private void freecam$cancelMovement(CallbackInfo ci) {
        FreecamController controller = FreecamController.instance();
        if (controller.isActive() && !controller.isPlayerControlled()) {
            ((MovementInputFromOptions) (Object) this).moveStrafe = 0.0F;
            ((MovementInputFromOptions) (Object) this).moveForward = 0.0F;
            ((MovementInputFromOptions) (Object) this).jump = false;
            ((MovementInputFromOptions) (Object) this).sneak = false;
        }
    }
}
