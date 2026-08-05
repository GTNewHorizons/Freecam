package com.caedis.freecam.mixins.late.galaxyspace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import galaxyspace.core.item.armor.ItemJetPack;
import galaxyspace.core.item.armor.ItemSpacesuitJetPlate;

/**
 * Mixin to disable GalaxySpace jetpack controls during freecam. Targets both standalone jetpack and spacesuit with
 * integrated jetpack.
 */
public class MixinGalaxySpaceKeyHandler {

    /**
     * Mixin for standalone jetpack item
     */
    @Mixin(value = ItemJetPack.class, remap = false)
    public static abstract class MixinItemJetPack {

        @ModifyReturnValue(method = "useJetpack", at = @At("RETURN"), remap = false)
        private boolean disableJetpackInFreecam(boolean original, EntityPlayer player) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                return false;
            }
            return original;
        }

        @Inject(method = "onArmorTick", at = @At("HEAD"), cancellable = true, remap = false)
        private void cancelArmorTickInFreecam(World world, EntityPlayer player, ItemStack itemStack, CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                ci.cancel();
            }
        }

        /**
         * Cancel the use() method which spawns particles
         */
        @Inject(method = "use", at = @At("HEAD"), cancellable = true, remap = false)
        private void cancelUseInFreecam(ItemStack itemStack, double acceleration, CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                ci.cancel();
            }
        }
    }

    /**
     * Mixin for spacesuit chest piece with integrated jetpack (the commonly used variant)
     */
    @Mixin(value = ItemSpacesuitJetPlate.class, remap = false)
    public static abstract class MixinItemSpacesuitJetPlate {

        @ModifyReturnValue(method = "useJetpack", at = @At("RETURN"), remap = false)
        private boolean disableJetpackInFreecam(boolean original, EntityPlayer player) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                return false;
            }
            return original;
        }

        @Inject(method = "onArmorTick", at = @At("HEAD"), cancellable = true, remap = false)
        private void cancelArmorTickInFreecam(World world, EntityPlayer player, ItemStack itemStack, CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                ci.cancel();
            }
        }

        /**
         * Cancel the use() method which spawns particles
         */
        @Inject(method = "use", at = @At("HEAD"), cancellable = true, remap = false)
        private void cancelUseInFreecam(ItemStack itemStack, double acceleration, CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                ci.cancel();
            }
        }
    }
}
