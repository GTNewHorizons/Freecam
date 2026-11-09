package com.caedis.freecam.compat.serverutilities;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;

/** Entry point; only call when Mods.SERVER_UTILITIES.isLoaded(). */
public final class ServerUtilitiesCompat {

    private ServerUtilitiesCompat() {}

    /** Call during PreInit, before any mod's Init can post the registry events. */
    public static void preInit() {
        FreecamPermissions permissions = new FreecamPermissions();
        MinecraftForge.EVENT_BUS.register(permissions);

        FreecamPermissionSync sync = new FreecamPermissionSync();
        MinecraftForge.EVENT_BUS.register(sync);
        FMLCommonHandler.instance()
            .bus()
            .register(sync);
    }

    /** Call during Init. Must run after preInit(). */
    public static void init() {
        FreecamPermissions.registerNodes();
    }
}
