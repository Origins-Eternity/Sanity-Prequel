package com.origins_eternity.sanity.utils;

import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.message.SyncSanity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.oredict.OreDictionary;

import static com.origins_eternity.sanity.Sanity.packetHandler;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;

public class Utils {
    public static void syncSanity(EntityPlayer player) {
        ISanity sanity = player.getCapability(SANITY, null);
        Capability<ISanity> capability = SANITY;
        SyncSanity message = new SyncSanity(capability.getStorage().writeNBT(capability, sanity, null));
        packetHandler.sendTo(message, (EntityPlayerMP) player);
    }

    public static void tickPlayer(EntityPlayer player) {
        ISanity sanity = player.getCapability(SANITY, null);
        if (sanity.getDown() > 0) {
            sanity.setDown(sanity.getDown() - 1);
        } else if (sanity.getUp() > 0) {
            sanity.setUp(sanity.getUp() - 1);
        }
        syncSanity(player);
    }

    public static boolean itemMatched(ItemStack item) {
        boolean match = false;
        for (ItemStack food : OreDictionary.getOres(Configuration.food)) {
            if (food.getItem().equals(item.getItem())) {
                match = true;
                break;
            }
        }
        return match;
    }
}