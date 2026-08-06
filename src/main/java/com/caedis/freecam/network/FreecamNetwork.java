package com.caedis.freecam.network;

import net.minecraft.entity.player.EntityPlayerMP;

import com.caedis.freecam.FreecamMod;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class FreecamNetwork {

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(FreecamMod.MODID);

    private FreecamNetwork() {}

    public static void init() {
        CHANNEL.registerMessage(PacketFreecamPermission.Handler.class, PacketFreecamPermission.class, 0, Side.CLIENT);
    }

    public static void send(boolean allowed, EntityPlayerMP player) {
        CHANNEL.sendTo(new PacketFreecamPermission(allowed), player);
    }
}
