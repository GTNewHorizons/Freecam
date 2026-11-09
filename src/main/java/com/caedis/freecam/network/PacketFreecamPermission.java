package com.caedis.freecam.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketFreecamPermission implements IMessage {

    private boolean allowed;

    public PacketFreecamPermission() {}

    public PacketFreecamPermission(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(allowed);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        allowed = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketFreecamPermission, IMessage> {

        @Override
        public IMessage onMessage(PacketFreecamPermission message, MessageContext ctx) {
            ServerPermission.set(message.allowed);
            return null;
        }
    }
}
