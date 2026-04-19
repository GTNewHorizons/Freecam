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
        .addRequiredMod(TargetedMod.ANGELICA));
    // spotless:on

    private final MixinBuilder builder;
}
