package com.caedis.freecam.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TargetedMod implements ITargetMod {

    ANGELICA("com.gtnewhorizons.angelica.loading.AngelicaTweaker");

    private final String coreModClass;

    @Nonnull
    @Override
    public TargetModBuilder getBuilder() {
        return new TargetModBuilder().setCoreModClass(coreModClass);
    }
}
