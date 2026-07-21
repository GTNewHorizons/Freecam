package com.caedis.freecam.mixins;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Mixins implements IMixins {

    // spotless:off
    FREECAM(new MixinBuilder().setPhase(Phase.EARLY)
        .addClientMixins(
            "minecraft.MixinEntityRenderer",
            "minecraft.MixinMovementInputFromOptions",
            "minecraft.MixinMinecraft",
            "minecraft.MixinEntityLivingBase"
        )),
    ANGELICA(new MixinBuilder().setPhase(Phase.EARLY)
        .addClientMixins("angelica.MixinHandRenderer")
        .addRequiredMod(TargetedMod.ANGELICA)),
    WAILA(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("waila.MixinOverlayRenderer")
        .addRequiredMod(TargetedMod.WAILA)),
    THAUMCRAFT(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("thaumcraft.MixinRenderEventHandler")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    HOLOINVENTORY(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("holoinventory.MixinRenderer")
        .addRequiredMod(TargetedMod.HOLOINVENTORY));
    // spotless:on

    private final MixinBuilder builder;
}
