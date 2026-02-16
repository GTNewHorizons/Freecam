package com.caedis.freecam.mixins.early.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Unique
    private boolean freecam$shouldCancel() {
        FreecamController controller = FreecamController.instance();
        return controller.isActive() && !controller.isPlayerControlled();
    }

    @Redirect(
        method = "runTick",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I",
            opcode = Opcodes.PUTFIELD))
    private void freecam$blockPerspectiveToggle(GameSettings instance, int value) {
        if (FreecamController.instance()
            .isActive()) {
            instance.thirdPersonView = 0;
            return;
        }

        instance.thirdPersonView = value;
    }

    @Inject(method = "func_147115_a", at = @At("HEAD"), cancellable = true)
    private void freecam$cancelLeftClick(boolean leftClick, CallbackInfo ci) {
        if (freecam$shouldCancel()) {
            ci.cancel();
        }
    }

    @Inject(method = "func_147116_af", at = @At("HEAD"), cancellable = true)
    private void freecam$cancelMiddleClick(CallbackInfo ci) {
        if (freecam$shouldCancel()) {
            ci.cancel();
        }
    }

    @Inject(method = "func_147121_ag", at = @At("HEAD"), cancellable = true)
    private void freecam$cancelRightClick(CallbackInfo ci) {
        if (freecam$shouldCancel()) {
            ci.cancel();
        }
    }

    @Inject(method = "func_147112_ai", at = @At("HEAD"), cancellable = true)
    private void freecam$cancelPickBlock(CallbackInfo ci) {
        if (freecam$shouldCancel()) {
            ci.cancel();
        }
    }
}
