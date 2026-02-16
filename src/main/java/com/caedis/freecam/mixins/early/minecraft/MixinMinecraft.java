package com.caedis.freecam.mixins.early.minecraft;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Unique
    private boolean freecam$shouldCancel() {
        FreecamController controller = FreecamController.instance();
        return controller.isActive() && !controller.isPlayerControlled();
    }

    @Definition(
        id = "gameSettings",
        field = "Lnet/minecraft/client/Minecraft;gameSettings:Lnet/minecraft/client/settings/GameSettings;")
    @Definition(
        id = "keyBindTogglePerspective",
        field = "Lnet/minecraft/client/settings/GameSettings;keyBindTogglePerspective:Lnet/minecraft/client/settings/KeyBinding;")
    @Definition(id = "isPressed", method = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z")
    @Expression("this.gameSettings.keyBindTogglePerspective.isPressed()")
    @ModifyExpressionValue(method = "runTick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean freecam$blockPerspectiveToggle(boolean original) {
        if (FreecamController.instance()
            .isActive()) {
            return false;
        }
        return original;
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
