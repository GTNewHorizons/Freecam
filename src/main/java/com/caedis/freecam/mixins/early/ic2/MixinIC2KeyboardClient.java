package com.caedis.freecam.mixins.early.ic2;

import net.minecraft.client.settings.KeyBinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ic2.core.util.KeyboardClient.class, remap = false)
public class MixinIC2KeyboardClient {

    @Redirect(
        method = "sendKeyUpdate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/settings/GameSettings;isKeyDown(Lnet/minecraft/client/settings/KeyBinding;)Z",
            remap = true))
    private boolean freecam$redirectToGetIsKeyPressed(KeyBinding keyBinding) {
        return keyBinding.getIsKeyPressed();
    }
}
