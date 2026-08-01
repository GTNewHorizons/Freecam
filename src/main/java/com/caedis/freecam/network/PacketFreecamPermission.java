package com.caedis.freecam.network;

import com.caedis.freecam.config.GeneralConfig;
import com.caedis.freecam.config.GeneralConfig.CollisionMode;
import com.caedis.freecam.config.MiscConfig;
import com.caedis.freecam.config.MiscConfig.OverlayVisibility;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketFreecamPermission implements IMessage {

    // values() clones its array on every call
    private static final CollisionMode[] COLLISION_MODES = CollisionMode.values();
    private static final OverlayVisibility[] OVERLAY_VISIBILITIES = OverlayVisibility.values();

    private FreecamPermissionState state;

    public PacketFreecamPermission() {}

    public PacketFreecamPermission(FreecamPermissionState state) {
        this.state = state;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(state.allowed);
        buf.writeByte(state.collisionMode.ordinal());
        buf.writeBoolean(state.fullBright);
        buf.writeBoolean(state.disableSubmersionFog);
        buf.writeByte(state.overlayVisibility.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boolean allowed = buf.readBoolean();
        CollisionMode collisionMode = byOrdinal(COLLISION_MODES, buf.readByte(), GeneralConfig.collisionMode);
        boolean fullBright = buf.readBoolean();
        boolean disableSubmersionFog = buf.readBoolean();
        OverlayVisibility overlayVisibility = byOrdinal(
            OVERLAY_VISIBILITIES,
            buf.readByte(),
            MiscConfig.overlayVisibility);
        state = new FreecamPermissionState(allowed, collisionMode, fullBright, disableSubmersionFog, overlayVisibility);
    }

    // Unknown ordinals fall back to the local config value
    private static <E extends Enum<E>> E byOrdinal(E[] values, int ordinal, E fallback) {
        return ordinal >= 0 && ordinal < values.length ? values[ordinal] : fallback;
    }

    public static class Handler implements IMessageHandler<PacketFreecamPermission, IMessage> {

        @Override
        public IMessage onMessage(PacketFreecamPermission message, MessageContext ctx) {
            ServerPermission.set(message.state);
            return null;
        }
    }
}
