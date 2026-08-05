package com.caedis.freecam.mixins.late.projectred;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;

import mrtjp.projectred.expansion.ItemJetpack;

/**
 * Mixin to disable ProjectRed jetpack entirely during freecam. Cancels the entire onArmorTick method to prevent all
 * jetpack functionality including propulsion, fuel consumption, and particle effects.
 */
@Mixin(value = ItemJetpack.class, remap = false)
public abstract class MixinProjectRedJetpack {

    /**
     * Cancel the entire onArmorTick method during freecam to disable all jetpack functionality. This prevents
     * movement, fuel consumption, particle effects, and any other jetpack behavior when the camera is being controlled.
     */
    @Inject(method = "onArmorTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableJetpackInFreecam(World world, EntityPlayer player, ItemStack stack, CallbackInfo ci) {
        FreecamController controller = FreecamController.instance();
        if (controller.isActive() && !controller.isPlayerControlled()) {
            ci.cancel();
        }
    }
}
