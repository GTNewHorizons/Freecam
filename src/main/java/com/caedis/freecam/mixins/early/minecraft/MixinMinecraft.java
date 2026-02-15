package com.caedis.freecam.mixins.early.minecraft;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    private boolean freecam$shouldCancel() {
        FreecamController controller = FreecamController.instance();
        return controller.isActive() && !controller.isPlayerControlled();
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
