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
    WAILA(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("waila.MixinOverlayRenderer")
        .addRequiredMod(TargetedMod.WAILA)),
    THAUMCRAFT(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("thaumcraft.MixinRenderEventHandler")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    HOLOINVENTORY(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("holoinventory.MixinRenderer")
        .addRequiredMod(TargetedMod.HOLOINVENTORY)),
    IC2(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("ic2.MixinIC2Keyboard")
        .addRequiredMod(TargetedMod.IC2)),
    GALAXYSPACE(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins(
            "galaxyspace.MixinGalaxySpaceKeyHandler$MixinItemJetPack",
            "galaxyspace.MixinGalaxySpaceKeyHandler$MixinItemSpacesuitJetPlate",
            "galaxyspace.MixinGalaxySpaceParticles$MixinEntityJetpackFlameFX",
            "galaxyspace.MixinGalaxySpaceParticles$MixinEntityJetpackSmokeFX")
        .addRequiredMod(TargetedMod.GALAXYSPACE)),
    GREGTECH(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("gregtech.MixinGregTechJetpack")
        .addRequiredMod(TargetedMod.GREGTECH)),
    PROJECTRED(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("projectred.MixinProjectRedJetpack")
        .addRequiredMod(TargetedMod.PROJECTRED));
    // spotless:on

    private final MixinBuilder builder;
}
