package com.caedis.freecam.compat.serverutilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.caedis.freecam.network.FreecamNetwork;
import com.caedis.freecam.network.FreecamPermissionState;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import serverutils.events.ServerReloadEvent;
import serverutils.ranks.Ranks;

/** Pushes each player's freecam permissions to their client. */
public class FreecamPermissionSync {

    // Rank commands fire no event, so poll for changes
    private static final int POLL_INTERVAL_TICKS = 100;

    private final Map<UUID, FreecamPermissionState> lastSent = new HashMap<>();
    private int tickCounter;

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP player) {
            send(player);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        lastSent.remove(
            event.player.getGameProfile()
                .getId());
    }

    @SubscribeEvent
    public void onServerReload(ServerReloadEvent event) {
        for (EntityPlayerMP player : event.getOnlinePlayers()) {
            send(player);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (++tickCounter < POLL_INTERVAL_TICKS) return;
        tickCounter = 0;

        for (EntityPlayerMP player : onlinePlayers()) {
            sendIfChanged(player);
        }
    }

    private void send(EntityPlayerMP player) {
        // Ranks disabled: stay on the pure config path, no packet at all
        if (!Ranks.isActive()) return;
        FreecamPermissionState state = FreecamPermissions.resolve(player);
        // lastSent is not set here, so a lost login packet gets re-sent by the first poll
        FreecamNetwork.send(state, player);
    }

    private void sendIfChanged(EntityPlayerMP player) {
        if (!Ranks.isActive()) return;
        FreecamPermissionState state = FreecamPermissions.resolve(player);
        UUID id = player.getGameProfile()
            .getId();
        if (state.equals(lastSent.get(id))) return;
        lastSent.put(id, state);
        FreecamNetwork.send(state, player);
    }

    @SuppressWarnings("unchecked")
    private static List<EntityPlayerMP> onlinePlayers() {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null || server.getConfigurationManager() == null) return Collections.emptyList();
        return new ArrayList<>(server.getConfigurationManager().playerEntityList);
    }
}
