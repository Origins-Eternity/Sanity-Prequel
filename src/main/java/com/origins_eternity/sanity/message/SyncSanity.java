package com.origins_eternity.sanity.message;

import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import static com.origins_eternity.sanity.utils.proxy.ClientProxy.mc;

public class SyncSanity implements IMessage {
    private NBTTagCompound nbt;

    public SyncSanity() {

    }

    public SyncSanity(NBTBase nbt) {
        this.nbt = (NBTTagCompound)nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.nbt);
    }

    public NBTTagCompound getNBT() {
        return this.nbt;
    }

    public static class Handler implements IMessageHandler<SyncSanity, IMessage> {
        @Override
        public IMessage onMessage(SyncSanity message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player != null) {
                    mc().addScheduledTask(() -> {
                        Capability<ISanity> capability = Capabilities.SANITY;
                        capability.getStorage().readNBT(capability, player.getCapability(capability, null), null, message.getNBT());
                    });
                }
            }
            return null;
        }
    }
}