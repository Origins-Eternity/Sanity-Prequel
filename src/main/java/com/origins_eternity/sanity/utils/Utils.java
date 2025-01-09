package com.origins_eternity.sanity.utils;

import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.message.SyncSanity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

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
        if (sanity.getDown() > 0f) {
            sanity.setDown(sanity.getDown() - 1);
        }
        if (sanity.getUp() > 0f) {
            sanity.setUp(sanity.getUp() - 1);
        }
        if (isWet(player)) {
            sanity.consumeSanity(Configuration.cold);
        }
        if (player.getFoodStats().getFoodLevel() < 8) {
            sanity.consumeSanity(Configuration.hunger);
        }
        if (player.getAir() < 60) {
            sanity.consumeSanity(Configuration.choking);
        }
        if (player.fallDistance > 4f) {
            sanity.consumeSanity((int) player.fallDistance);
        }
        if (isDangerous(player)) {
            sanity.consumeSanity(Configuration.danger);
        }
        if (withPet(player) && sanity.getDown() == 0f) {
            sanity.recoverSanity(Configuration.pet);
        }
        if (player.world.getLight(new BlockPos(player), true) < 4) {
            if (!player.isPotionActive(MobEffects.NIGHT_VISION)) {
                sanity.consumeSanity(Configuration.dark);
            }
        }
        syncSanity(player);
    }

    public static int itemMatched(ItemStack item) {
        int match = -1;
        for (int i = 0; i < Configuration.food.length; i++) {
            String[] parts = Configuration.food[i].split(";");
            String[] name = parts[0].split(":");
            if (name.length == 2) {
                if (item.getItem().equals(Item.REGISTRY.getObject(new ResourceLocation(parts[0])))) {
                    if (item.getMetadata() == 0) {
                        match = i;
                        break;
                    }
                }
            } else if (name.length == 3) {
                ResourceLocation location = new ResourceLocation(name[0], name[1]);
                if (item.getItem().equals(Item.REGISTRY.getObject(location))) {
                    if (item.getMetadata() == Integer.parseInt(name[2])) {
                        match = i;
                        break;
                    }
                }
            }
        }
        return match;
    }

    public static boolean blockMatched(IBlockState block) {
        boolean match = false;
        for (String id : Configuration.blocks) {
            String[] name = id.split(":");
            if (name.length == 2) {
                if (block.getBlock().equals(Block.REGISTRY.getObject(new ResourceLocation(id)))) {
                    if (block.getBlock().getMetaFromState(block) == 0) {
                        match = true;
                        break;
                    }
                }
            } else if (name.length == 3) {
                ResourceLocation location = new ResourceLocation(name[0], name[1]);
                if (block.getBlock().equals(Block.REGISTRY.getObject(location))) {
                    if (block.getBlock().getMetaFromState(block) == Integer.parseInt(name[2])) {
                        match = true;
                        break;
                    }
                }
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
                if (player.world.isRemote) {
                    player.setSprinting(false);
                }
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
        BlockPos pos = new BlockPos(player);
        IBlockState state = player.world.getBlockState(pos);
        IBlockState state1 = player.world.getBlockState(pos.up());
        return blockMatched(state) || blockMatched(state1);
    }

    public static boolean withPet(EntityPlayer player) {
        boolean pet = false;
        AxisAlignedBB box = player.getEntityBoundingBox().grow(5);
        for (Entity entity: player.world.getEntitiesWithinAABB(Entity.class, box)) {
            if (entity instanceof EntityTameable) {
                EntityTameable entityTameable = (EntityTameable) entity;
                if (entityTameable.isTamed() && entityTameable.isOwner(player)) {
                    pet = true;
                }
            }
        }
        return pet;
    }
}