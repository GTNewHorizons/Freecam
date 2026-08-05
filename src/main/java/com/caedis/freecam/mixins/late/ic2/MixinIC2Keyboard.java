package com.caedis.freecam.mixins.late.ic2;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.caedis.freecam.camera.FreecamController;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(value = ic2.core.util.Keyboard.class, remap = false)
public abstract class MixinIC2Keyboard {

    @ModifyReturnValue(method = "isJumpKeyDown", at = @At("RETURN"))
    private static boolean disableJumpInFreecam(boolean original, EntityPlayer player) {
        FreecamController controller = FreecamController.instance();
        // Only disable jetpack jump when freecam is active AND camera is being controlled
        if (controller.isActive() && !controller.isPlayerControlled()) {
            return false;
        }
        return original;
    }

    @ModifyReturnValue(method = "isBoostKeyDown", at = @At("RETURN"))
    private static boolean disableBoostInFreecam(boolean original, EntityPlayer player) {
        FreecamController controller = FreecamController.instance();
        // Only disable jetpack boost when freecam is active AND camera is being controlled
        if (controller.isActive() && !controller.isPlayerControlled()) {
            return false;
        }
        return original;
    }
}
