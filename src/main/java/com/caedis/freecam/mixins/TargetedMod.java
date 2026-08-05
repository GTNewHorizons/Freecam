package com.caedis.freecam.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

public enum TargetedMod implements ITargetMod {

    WAILA(null, "Waila"),
    THAUMCRAFT(null, "Thaumcraft"),
    HOLOINVENTORY(null, "holoinventory"),
    IC2("ic2.core.IC2", "IC2"),
    GALAXYSPACE("galaxyspace.core.GSCore", "GalaxySpace"),
    GREGTECH("gregtech.GT_Mod", "gregtech"),
    PROJECTRED("mrtjp.projectred.ProjectRedExpansion", "ProjRed|Expansion");

    private final TargetModBuilder builder;

    TargetedMod(String coreModClass, String modId) {
        this.builder = new TargetModBuilder().setCoreModClass(coreModClass)
            .setModId(modId);
    }

    @Nonnull
    @Override
    public TargetModBuilder getBuilder() {
        return builder;
    }
}
