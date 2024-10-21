package com.origins_eternity.sanity.utils;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.message.SyncSanity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.capabilities.Capability;

import static com.origins_eternity.sanity.Sanity.packetHandler;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;

public class Utils {
    public static void syncEndurance(EntityPlayer player) {
        ISanity sanity = player.getCapability(SANITY, null);
        Capability<ISanity> capability = SANITY;
        SyncSanity message = new SyncSanity(capability.getStorage().writeNBT(capability, sanity, null));
        packetHandler.sendTo(message, (EntityPlayerMP) player);
    }

    public static void tickUpdate(EntityPlayer player) {
        ISanity sanity = player.getCapability(SANITY, null);
    }

    private static void addPotions(EntityPlayer player, Potion potion) {
        if (!player.isPotionActive(potion)) {
            player.addPotionEffect(new PotionEffect(potion, 312, 2, false, false));
        }
    }

    public static void addTiredDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            addPotions(player, MobEffects.SLOWNESS);
        }
    }

    public static void addExhaustedDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            addPotions(player, MobEffects.BLINDNESS);
            addPotions(player, MobEffects.HUNGER);
        }
    }

    public static void checkStatus(EntityPlayer player) {
        ISanity sanity = player.getCapability(SANITY, null);
        if (sanity.isTired()) {
            player.setSprinting(false);
            addTiredDebuff(player);
            if (sanity.isExhausted()) {
                addExhaustedDebuff(player);
            }
        }
    }
}