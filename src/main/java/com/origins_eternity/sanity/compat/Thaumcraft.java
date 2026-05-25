package com.origins_eternity.sanity.compat;

import com.origins_eternity.sanity.capability.sanity.ISanity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import static com.origins_eternity.sanity.capability.Capabilities.SANITY;

public class Thaumcraft {
    public static int getWarp(EntityPlayer player) {
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
        int value = warp.get(IPlayerWarp.EnumWarpType.PERMANENT) + warp.get(IPlayerWarp.EnumWarpType.NORMAL);
        return Math.min(value, 50);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            if (player.ticksExisted % 20 == 0 && !player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                sanity.setMax(100 - getWarp(player));
                if (sanity.getSanity() > sanity.getMax()) {
                    sanity.setSanity(sanity.getMax());
                }
            }
        }
    }
}