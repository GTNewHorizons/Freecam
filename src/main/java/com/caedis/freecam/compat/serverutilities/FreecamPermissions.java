package com.caedis.freecam.compat.serverutilities;

import net.minecraft.entity.player.EntityPlayerMP;

import com.caedis.freecam.config.GeneralConfig;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import serverutils.events.CustomPermissionPrefixesRegistryEvent;
import serverutils.lib.util.permission.DefaultPermissionLevel;
import serverutils.lib.util.permission.PermissionAPI;

/** ServerUtilities node for freecam access. Only loaded when ServerUtilities is present. */
public class FreecamPermissions {

    public static final String NODE_USE = "freecam.use";

    /** Call during Init. Node level falls back to the server config value. */
    public static void registerNodes() {
        PermissionAPI.registerNode(NODE_USE, defaultLevel(), "Allows the player to use freecam");
    }

    @SubscribeEvent
    public void registerPrefixes(CustomPermissionPrefixesRegistryEvent event) {
        event.register("freecam.", defaultLevel(), "Freecam");
    }

    public static boolean isAllowed(EntityPlayerMP player) {
        return PermissionAPI.hasPermission(player, NODE_USE);
    }

    // NONE when the server disabled freecam, so nobody has it until a rank grants the node
    private static DefaultPermissionLevel defaultLevel() {
        return GeneralConfig.disabled ? DefaultPermissionLevel.NONE : DefaultPermissionLevel.ALL;
    }
}
