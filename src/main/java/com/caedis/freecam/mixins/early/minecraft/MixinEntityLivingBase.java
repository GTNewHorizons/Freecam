package com.caedis.freecam.mixins.early.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;
import com.caedis.freecam.config.GeneralConfig;
import com.caedis.freecam.config.MovementConfig;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {

    @Shadow
    public abstract float getHealth();

    @Inject(method = "setHealth", at = @At("HEAD"))
    private void freecam$setHealth(float health, CallbackInfo ci) {
        if (GeneralConfig.disableOnDamage && FreecamController.instance()
            .isActive() && (this.equals(Minecraft.getMinecraft().thePlayer))) {
            if (!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode && getHealth() > health) {
                FreecamController.instance()
                    .scheduleDisable();
            }
        }
    }

    @Inject(method = "moveEntityWithHeading", at = @At("HEAD"), cancellable = true)
    private void freecam$cancelMovement(float strafe, float forward, CallbackInfo ci) {
        if (MovementConfig.lockPlayerBody && FreecamController.instance()
            .isActive() && this.equals(Minecraft.getMinecraft().thePlayer)) {
            ci.cancel();
        }
    }
}
