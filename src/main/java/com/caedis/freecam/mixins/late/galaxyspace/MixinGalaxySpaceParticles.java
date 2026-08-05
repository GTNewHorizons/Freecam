package com.caedis.freecam.mixins.late.galaxyspace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;

import galaxyspace.core.particle.EntityJetpackFlameFX;
import galaxyspace.core.particle.EntityJetpackSmokeFX;
import micdoodle8.mods.galacticraft.api.vector.Vector3;

/**
 * Mixin to prevent GalaxySpace jetpack particles from spawning/updating during freecam. Note: GalaxySpace 1.1.139+
 * uses Vector3 constructor instead of individual doubles.
 */
public class MixinGalaxySpaceParticles {

    /**
     * Mixin for jetpack flame particles
     */
    @Mixin(value = EntityJetpackFlameFX.class, remap = false)
    public static abstract class MixinEntityJetpackFlameFX extends EntityFX {

        // Constructor required for mixin
        public MixinEntityJetpackFlameFX(World world, double x, double y, double z) {
            super(world, x, y, z);
        }

        /**
         * Check if a position is near the player (within 4 blocks)
         */
        @Unique
        private boolean freecam_gtnh$isNearPlayer(double x, double y, double z) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return false;
            return player.getDistanceSq(x, y, z) < 16.0; // 4 blocks squared
        }

        @Inject(method = "<init>", at = @At("RETURN"), remap = false)
        private void onConstructor(World world, Vector3 position, Vector3 motion, CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            // Mark particle for immediate death if freecam is active and player's entity is at this position
            if (controller.isActive() && !controller.isPlayerControlled()
                && freecam_gtnh$isNearPlayer(position.x, position.y, position.z)) {
                this.setDead();
            }
        }

        @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true, remap = false)
        private void cancelUpdateInFreecam(CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                if (freecam_gtnh$isNearPlayer(this.posX, this.posY, this.posZ)) {
                    this.setDead();
                    ci.cancel();
                }
            }
        }
    }

    /**
     * Mixin for jetpack smoke particles
     */
    @Mixin(value = EntityJetpackSmokeFX.class, remap = false)
    public static abstract class MixinEntityJetpackSmokeFX extends EntityFX {

        // Constructor required for mixin
        public MixinEntityJetpackSmokeFX(World world, double x, double y, double z) {
            super(world, x, y, z);
        }

        /**
         * Check if a position is near the player (within 4 blocks)
         */
        @Unique
        private boolean freecam_gtnh$isNearPlayer(double x, double y, double z) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return false;
            return player.getDistanceSq(x, y, z) < 16.0; // 4 blocks squared
        }

        @Inject(method = "<init>", at = @At("RETURN"), remap = false)
        private void onConstructor(World world, Vector3 position, Vector3 motion, CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            // Mark particle for immediate death if freecam is active and player's entity is at this position
            if (controller.isActive() && !controller.isPlayerControlled()
                && freecam_gtnh$isNearPlayer(position.x, position.y, position.z)) {
                this.setDead();
            }
        }

        @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true, remap = false)
        private void cancelUpdateInFreecam(CallbackInfo ci) {
            FreecamController controller = FreecamController.instance();
            if (controller.isActive() && !controller.isPlayerControlled()) {
                if (freecam_gtnh$isNearPlayer(this.posX, this.posY, this.posZ)) {
                    this.setDead();
                    ci.cancel();
                }
            }
        }
    }
}
