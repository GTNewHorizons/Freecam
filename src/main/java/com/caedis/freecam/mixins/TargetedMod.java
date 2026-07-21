package com.caedis.freecam.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

public enum TargetedMod implements ITargetMod {

    ANGELICA("com.gtnewhorizons.angelica.loading.AngelicaTweaker"),
    WAILA(null, "Waila"),
    THAUMCRAFT(null, "Thaumcraft"),
    HOLOINVENTORY(null, "holoinventory");

    private final TargetModBuilder builder;

    TargetedMod(String coreModClass) {
        this.builder = new TargetModBuilder().setCoreModClass(coreModClass);
    }

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
