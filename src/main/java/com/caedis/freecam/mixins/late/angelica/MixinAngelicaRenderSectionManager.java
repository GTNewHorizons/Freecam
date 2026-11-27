package com.caedis.freecam.mixins.late.angelica;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.caedis.freecam.camera.CameraEntity;
import com.caedis.freecam.config.GeneralConfig.CollisionMode;

@Pseudo
@Mixin(targets = "com.gtnewhorizons.angelica.rendering.celeritas.AngelicaRenderSectionManager", remap = false)
public class MixinAngelicaRenderSectionManager {

    @ModifyVariable(
        method = "shouldUseOcclusionCulling(Lorg/embeddedt/embeddium/impl/render/viewport/Viewport;Z)Z",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0,
        require = 0)
    private boolean freecam$useSpectatorCulling(boolean spectator) {
        return spectator || Minecraft.getMinecraft().renderViewEntity instanceof CameraEntity camera
            && camera.getCollisionMode() == CollisionMode.NONE;
    }
}
