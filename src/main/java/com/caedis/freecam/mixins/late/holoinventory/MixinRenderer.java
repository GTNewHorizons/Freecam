package com.caedis.freecam.mixins.late.holoinventory;

import net.dries007.holoInventory.client.Renderer;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.caedis.freecam.camera.RevealGate;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

@Mixin(value = Renderer.class, remap = false)
public class MixinRenderer {

    // Modify the read only. KeyManager owns this field and persists it to config,
    // so writing it would fight the user's toggle keybind.
    @ModifyExpressionValue(
        method = "renderEvent",
        at = @At(
            value = "FIELD",
            target = "Lnet/dries007/holoInventory/client/Renderer;enabled:Z",
            opcode = Opcodes.GETFIELD))
    private boolean freecam$hideHologram(boolean original) {
        return original && !RevealGate.shouldHide();
    }
}
