package com.caedis.freecam.mixins.early.minecraft;

import java.util.Arrays;

import com.caedis.freecam.config.GeneralConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.freecam.camera.FreecamController;
import com.caedis.freecam.config.MiscConfig;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Shadow
    @Final
    private int[] lightmapColors;

    @Shadow
    @Final
    private DynamicTexture lightmapTexture;

    @Inject(method = "updateLightmap", at = @At("HEAD"), cancellable = true)
    private void freecam$fullBright(float partialTicks, CallbackInfo ci) {
        if (FreecamController.instance()
            .isActive() && MiscConfig.fullBright) {
            Arrays.fill(lightmapColors, 0xFFFFFFFF);
            lightmapTexture.updateDynamicTexture();
            ci.cancel();
        }
    }

    @Redirect(
        method = "updateCameraAndRender",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;setAngles(FF)V"))
    private void freecam$redirectMouseLook(EntityClientPlayerMP player, float yaw, float pitch) {
        FreecamController controller = FreecamController.instance();
        if (controller.isActive() && !controller.isPlayerControlled()) {
            Minecraft.getMinecraft().renderViewEntity.setAngles(yaw, pitch);
        } else {
            player.setAngles(yaw, pitch);
        }
    }

    @Redirect(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;F)Lnet/minecraft/block/Block;"))
    private Block freecam$disableSubmersionFog(World world, EntityLivingBase entity, float partialTicks) {
        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(world, entity, partialTicks);
        if (FreecamController.instance()
            .isActive() && MiscConfig.disableSubmersionFog
            && (block.getMaterial() == Material.water || block.getMaterial() == Material.lava)) {
            return Blocks.air;
        }
        return block;
    }

    @Inject(method = "getMouseOver", at = @At("HEAD"), cancellable = true)
    private void freecam$playerControlledMouseOver(float partialTicks, CallbackInfo ci) {
        FreecamController controller = FreecamController.instance();
        if (controller.isActive() && controller.isPlayerControlled()) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null && mc.theWorld != null) {
                double reach = mc.playerController.getBlockReachDistance();
                mc.objectMouseOver = mc.thePlayer.rayTrace(reach, partialTicks);
            }
            ci.cancel();
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void freecam$hideHand(float partialTicks, int pass, CallbackInfo ci) {
        if (FreecamController.instance()
            .isActive() && !MiscConfig.showHand) {
            ci.cancel();
        }
    }
}
