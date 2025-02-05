package com.origins_eternity.sanity.utils;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.message.SyncSanity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.IThirst;

import static com.origins_eternity.sanity.Sanity.packetHandler;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.content.umbrella.Umbrellas.UMBRELLA;

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
        }
        if (sanity.getUp() > 0) {
            sanity.setUp(sanity.getUp() - 1);
        }
        if (sanity.getFlash() > 0 && player.ticksExisted % 20 == 0) {
            sanity.setFlash(sanity.getFlash() - 1);
        }
        if (isWet(player)) {
            sanity.consumeSanity(Configuration.rain);
        }
        if (player.getFoodStats().getFoodLevel() < 6) {
            sanity.consumeSanity(Configuration.hunger);
        }
        if (isThirst(player)) {
            sanity.consumeSanity(Configuration.thirst);
        }
        if (player.getAir() < 60) {
            sanity.consumeSanity(Configuration.choking);
        }
        if (player.fallDistance > 4f) {
            sanity.consumeSanity(player.fallDistance * Configuration.fall);
        }
        if (isDangerous(player)) {
            sanity.consumeSanity(Configuration.danger);
        }
        if (withMob(player)) {
            sanity.consumeSanity(Configuration.mob);
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

    public static boolean isWet(EntityPlayer player) {
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(player.posX, player.posY, player.posZ);
        if (player.world.isRainingAt(blockpos$pooledmutableblockpos) || player.world.isRainingAt(blockpos$pooledmutableblockpos.setPos(player.posX, player.posY + (double)player.height, player.posZ))) {
            blockpos$pooledmutableblockpos.release();
            return !player.getHeldItemMainhand().getItem().equals(UMBRELLA) && !player.getHeldItemOffhand().getItem().equals(UMBRELLA);
        }
        return false;
    }

    public static boolean isDangerous(EntityPlayer player) {
        BlockPos pos = new BlockPos(player);
        IBlockState state = player.world.getBlockState(pos);
        IBlockState state1 = player.world.getBlockState(pos.up());
        return blockMatched(state) || blockMatched(state1);
    }

    private static boolean withPet(EntityPlayer player) {
        boolean pet = false;
        AxisAlignedBB box = player.getEntityBoundingBox().grow(5, 3, 5);
        for (EntityTameable entity: player.world.getEntitiesWithinAABB(EntityTameable.class, box)) {
            if (entity != null) {
                if (entity.isTamed() && entity.isOwner(player)) {
                    pet = true;
                    break;
                }
            }
        }
        return pet;
    }

    private static boolean withMob(EntityPlayer player) {
        boolean mob = false;
        AxisAlignedBB box = player.getEntityBoundingBox().grow(8, 5, 8);
        for (EntityMob entity: player.world.getEntitiesWithinAABB(EntityMob.class, box)) {
            if (entity != null) {
                mob = true;
                break;
            }
        }
        return mob;
    }

    private static boolean isThirst(EntityPlayer player) {
        boolean thirst = false;
        if (Loader.isModLoaded("toughasnails")) {
            IThirst thirstStats = player.getCapability(TANCapabilities.THIRST, null);
            if (thirstStats.getThirst() < 6) {
                thirst = true;
            }
        }
        if (Loader.isModLoaded("simpledifficulty")) {
            IThirstCapability thirstStats1 = player.getCapability(SDCapabilities.THIRST, null);
            if (thirstStats1.getThirstLevel() < 6) {
                thirst = true;
            }
        }
        return thirst;
    }
}