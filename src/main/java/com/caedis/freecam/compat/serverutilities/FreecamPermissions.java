package com.caedis.freecam.compat.serverutilities;

import net.minecraft.entity.player.EntityPlayerMP;

import com.caedis.freecam.config.GeneralConfig;
import com.caedis.freecam.config.GeneralConfig.CollisionMode;
import com.caedis.freecam.config.MiscConfig;
import com.caedis.freecam.config.MiscConfig.OverlayVisibility;
import com.caedis.freecam.network.FreecamPermissionState;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import serverutils.events.CustomPermissionPrefixesRegistryEvent;
import serverutils.events.RegisterRankConfigEvent;
import serverutils.lib.config.ConfigBoolean;
import serverutils.lib.config.ConfigEnum;
import serverutils.lib.config.ConfigNull;
import serverutils.lib.config.ConfigValue;
import serverutils.lib.util.misc.NameMap;
import serverutils.lib.util.permission.DefaultPermissionLevel;
import serverutils.lib.util.permission.PermissionAPI;
import serverutils.ranks.Ranks;

/** ServerUtilities nodes and per-player resolution. Only loaded when ServerUtilities is present. */
public class FreecamPermissions {

    private static final CollisionMode[] COLLISION_MODES = CollisionMode.values();
    private static final OverlayVisibility[] OVERLAY_VISIBILITIES = OverlayVisibility.values();

    public static final String NODE_USE = "freecam.use";
    public static final String NODE_COLLISION_MODE = "freecam.collision_mode";
    public static final String NODE_FULL_BRIGHT = "freecam.full_bright";
    public static final String NODE_DISABLE_SUBMERSION_FOG = "freecam.disable_submersion_fog";
    public static final String NODE_OVERLAY_VISIBILITY = "freecam.overlay_visibility";

    /** Call during Init. Node level falls back to the server config value. */
    public static void registerNodes() {
        PermissionAPI.registerNode(
            NODE_USE,
            GeneralConfig.disabled ? DefaultPermissionLevel.NONE : DefaultPermissionLevel.ALL,
            "Allows the player to use freecam");
    }

    @SubscribeEvent
    public void registerPrefixes(CustomPermissionPrefixesRegistryEvent event) {
        event.register(
            "freecam.",
            GeneralConfig.disabled ? DefaultPermissionLevel.NONE : DefaultPermissionLevel.ALL,
            "Freecam");
    }

    @SubscribeEvent
    public void registerRankConfigs(RegisterRankConfigEvent event) {
        event.register(
            NODE_COLLISION_MODE,
            new ConfigEnum<CollisionMode>(NameMap.create(GeneralConfig.collisionMode, COLLISION_MODES)));
        event.register(NODE_FULL_BRIGHT, new ConfigBoolean(MiscConfig.fullBright));
        event.register(NODE_DISABLE_SUBMERSION_FOG, new ConfigBoolean(MiscConfig.disableSubmersionFog));
        event.register(
            NODE_OVERLAY_VISIBILITY,
            new ConfigEnum<OverlayVisibility>(NameMap.create(MiscConfig.overlayVisibility, OVERLAY_VISIBILITIES)));
    }

    public static FreecamPermissionState resolve(EntityPlayerMP player) {
        ConfigValue collisionValue = rankNodeValue(player, NODE_COLLISION_MODE);
        ConfigValue fullBrightValue = rankNodeValue(player, NODE_FULL_BRIGHT);
        ConfigValue submersionFogValue = rankNodeValue(player, NODE_DISABLE_SUBMERSION_FOG);
        ConfigValue overlayValue = rankNodeValue(player, NODE_OVERLAY_VISIBILITY);

        return new FreecamPermissionState(
            PermissionAPI.hasPermission(player, NODE_USE),
            collisionValue.isNull() ? GeneralConfig.collisionMode
                : parseEnum(COLLISION_MODES, collisionValue.getString(), GeneralConfig.collisionMode),
            fullBrightValue.isNull() ? MiscConfig.fullBright : fullBrightValue.getBoolean(),
            submersionFogValue.isNull() ? MiscConfig.disableSubmersionFog : submersionFogValue.getBoolean(),
            overlayValue.isNull() ? MiscConfig.overlayVisibility
                : parseEnum(OVERLAY_VISIBILITIES, overlayValue.getString(), MiscConfig.overlayVisibility));
    }

    // Resolve without the wildcard collapse (recursive=false), so a rank's `*: true` doesn't force these on.
    // Parent ranks are still walked; unset (null) falls back to the live config value.
    private static ConfigValue rankNodeValue(EntityPlayerMP player, String node) {
        if (!Ranks.isActive()) return ConfigNull.INSTANCE;
        return Ranks.INSTANCE.getPermission(player.getGameProfile(), node, false);
    }

    // Rank config stores enum names lowercased. Scan avoids allocating an uppercased
    // string, and an exception for values that aren't enum names (e.g. a rank set "true").
    private static <E extends Enum<E>> E parseEnum(E[] values, String value, E fallback) {
        if (value == null) return fallback;
        for (E constant : values) {
            if (constant.name()
                .equalsIgnoreCase(value)) return constant;
        }
        return fallback;
    }
}
