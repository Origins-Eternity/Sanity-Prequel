package com.origins_eternity.sanity.compat.thaumcraft;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ThaumcraftEventHandler {

    private static int lastWarp = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.world.isRemote) {
            EntityPlayer player = event.player;
            int currentWarp = ThaumcraftWarp.getTotalWarp(player);

            if (currentWarp != lastWarp) {
                ThaumcraftWarp.syncSanityFromWarp(player);
                lastWarp = currentWarp;
            }
        }
    }
}