package com.caedis.freecam.compat;

import java.util.function.Supplier;

import cpw.mods.fml.common.Loader;

public enum Mods {

    // spotless:off
    ANGELICA("angelica"),
    // RenderHandEvent was added in Angelica 2.1.30
    ANGELICA_HAND_EVENT(() -> Loader.isModLoaded("angelica")
        && classFileExists("com/gtnewhorizons/angelica/event/RenderHandEvent.class")),
    CONTROLLING(() -> Loader.isModLoaded("controlling")
        && classFileExists("com/blamejared/controlling/keybinding/ComboKeyBinding.class")),
    EFR("etfuturum")
    ;
    // spotless:on

    public final String modid;
    private final Supplier<Boolean> supplier;
    private Boolean loaded;

    Mods(String modid) {
        this.modid = modid;
        this.supplier = null;
    }

    Mods(Supplier<Boolean> supplier) {
        this.supplier = supplier;
        this.modid = null;
    }

    public boolean isLoaded() {
        if (loaded == null) {
            if (supplier != null) {
                loaded = supplier.get();
            } else if (modid != null) {
                loaded = Loader.isModLoaded(modid);
            } else loaded = false;
        }
        return loaded;
    }

    private static boolean classFileExists(String classFilePath) {
        return Mods.class.getClassLoader()
            .getResource(classFilePath) != null;
    }
}
