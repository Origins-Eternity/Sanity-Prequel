package com.origins_eternity.sanity.utils;

import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.armor.Armors;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.message.SyncSanity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
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
        if (isWet(player)) {
            sanity.consumeSanity(Configuration.wet);
        }
        if (player.getFoodStats().getFoodLevel() < 8) {
            sanity.consumeSanity(Configuration.hunger);
        }
        if (player.getAir() < 60) {
            sanity.consumeSanity(Configuration.choking);
        }
        if (player.fallDistance > 4) {
            sanity.consumeSanity((int) player.fallDistance);
        }
        if (isDangerous(player)) {
            sanity.consumeSanity(Configuration.danger);
        }
        if (player.world.getLight(new BlockPos(player), true) < 4) {
            if (!player.isPotionActive(MobEffects.NIGHT_VISION)) {
                sanity.consumeSanity(Configuration.dark);
            }
        }
        sanity.setGarland(player.inventory.armorItemInSlot(3).getItem().equals(Armors.FLOWER));
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

    private static void addPotions(EntityPlayer player, Potion potion) {
        if (!player.isPotionActive(potion)) {
            player.addPotionEffect(new PotionEffect(potion, 312, 1, false, false));
        }
    }

    private static void addLostDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            addPotions(player, MobEffects.NAUSEA);
            addPotions(player, MobEffects.BLINDNESS);
        }
    }

    private static void addDizzyDebuff(EntityPlayer player) {
        if (!player.world.isRemote) {
            addPotions(player, MobEffects.WEAKNESS);
            addPotions(player, MobEffects.MINING_FATIGUE);
        }
    }

    public static void checkStatus(EntityPlayer player) {
        ISanity sanity = player.getCapability(SANITY, null);
        if (sanity.getSanity() < 50f) {
            addDizzyDebuff(player);
            if (sanity.getSanity() < 10f) {
                player.setSprinting(false);
                addLostDebuff(player);
            }
        }
    }

    public static boolean isWet(EntityPlayer player) {
        boolean wet = false;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(player.posX, player.posY, player.posZ);
        if (player.world.isRainingAt(blockpos$pooledmutableblockpos) || player.world.isRainingAt(blockpos$pooledmutableblockpos.setPos(player.posX, player.posY + (double)player.height, player.posZ))) {
            blockpos$pooledmutableblockpos.release();
            wet = true;
        }
        return wet;
    }

    public static boolean isDangerous(EntityPlayer player) {
        boolean dangerous = false;
            BlockPos pos = new BlockPos(player);
            String blocks = player.world.getBlockState(pos).getBlock().getTranslationKey() + player.world.getBlockState(pos.up()).getBlock().getTranslationKey();
            for (String block : Configuration.blocks) {
                if (blocks.contains(block)) {
                    dangerous = true;
                    break;
                }
            }
        return dangerous;
    }
}