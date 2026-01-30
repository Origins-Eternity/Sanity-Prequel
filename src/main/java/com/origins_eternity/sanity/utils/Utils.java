package com.origins_eternity.sanity.utils;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.message.SyncSanity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Loader;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.IThirst;

import java.util.Arrays;

import static com.origins_eternity.sanity.Sanity.packetHandler;
import static com.origins_eternity.sanity.config.Configuration.Mechanics;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.content.umbrella.Umbrellas.UMBRELLA;

public class Utils {
    public static void syncSanity(EntityPlayer player) {
        ISanity sanity = player.getCapability(SANITY, null);
        Capability<ISanity> capability = SANITY;
        SyncSanity message = new SyncSanity(capability.getStorage().writeNBT(capability, sanity, null));
        packetHandler.sendTo(message, (EntityPlayerMP) player);
    }

    public static double tickPlayer(EntityPlayer player) {
        double value = 0;
        if (isWet(player)) {
            value -= Mechanics.rain;
        }
        if (player.getFoodStats().getFoodLevel() < 6) {
            value -= Mechanics.hunger;
        }
        if (isThirst(player)) {
            value -= Mechanics.thirst;
        }
        if (player.getAir() < 90) {
            value -= Mechanics.choking;
        }
        if (player.fallDistance > 4f && !player.isElytraFlying()) {
            if (!player.isPotionActive(MobEffects.JUMP_BOOST)) {
                value -= player.fallDistance * Mechanics.fall;
            }
        }
        if (player.world.getLight(new BlockPos(player), true) < 4) {
            if (!player.isPotionActive(MobEffects.NIGHT_VISION)) {
                value -= Mechanics.dark;
            }
        }
        return value + checkArmor(player) + checkBody(player) + withCreature(player);
    }

    public static int stackMatched(ItemStack item) {
        int match = -1;
        for (int i = 0; i < Mechanics.food.length; i++) {
            String[] parts = Mechanics.food[i].split(";");
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

    public static int blockMatched(IBlockState block) {
        int match = -1;
        for (int i = 0; i < Mechanics.blocks.length; i++) {
            String[] parts = Mechanics.blocks[i].split(";");
            String[] name = parts[0].split(":");
            if (name.length == 2) {
                if (block.getBlock().equals(Block.REGISTRY.getObject(new ResourceLocation(parts[0])))) {
                    if (block.getBlock().getMetaFromState(block) == 0) {
                        match = i;
                        break;
                    }
                }
            } else if (name.length == 3) {
                ResourceLocation location = new ResourceLocation(name[0], name[1]);
                if (block.getBlock().equals(Block.REGISTRY.getObject(location))) {
                    if (block.getBlock().getMetaFromState(block) == Integer.parseInt(name[2])) {
                        match = i;
                        break;
                    }
                }
            }
        }
        return match;
    }

    public static int entityMatched(Entity entity) {
        int match = -1;
        for (int i = 0; i < Mechanics.entities.length; i++) {
            String[] parts = Mechanics.entities[i].split(";");
            ResourceLocation name = EntityList.getKey(entity);
            if (name != null && parts[0].equals(name.toString())) {
                match = i;
                break;
            }
        }
        return match;
    }

    public static double checkArmor(EntityPlayer player) {
        double value = 0;
        for (ItemStack armor : player.getArmorInventoryList()) {
            for (int i = 0; i < Mechanics.equipment.length; i++) {
                String[] parts = Mechanics.equipment[i].split(";");
                if (armor.getItem().equals(Item.REGISTRY.getObject(new ResourceLocation(parts[0])))) {
                    value += Double.parseDouble(parts[1]);
                    break;
                }
            }
        }
        return value;
    }

    public static boolean isWet(EntityPlayer player) {
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(player.posX, player.posY, player.posZ);
        if (player.world.isRainingAt(blockpos$pooledmutableblockpos) || player.world.isRainingAt(blockpos$pooledmutableblockpos.setPos(player.posX, player.posY + (double) player.height, player.posZ))) {
            blockpos$pooledmutableblockpos.release();
            return !player.getHeldItemMainhand().getItem().equals(UMBRELLA) && !player.getHeldItemOffhand().getItem().equals(UMBRELLA);
        }
        return player.world.getBlockState(new BlockPos(player).up()).getBlock().equals(Blocks.WATER);
    }

    public static double checkBody(EntityPlayer player) {
        double value = 0;
        BlockPos foot = new BlockPos(player);
        BlockPos head = new BlockPos(player).up();
        IBlockState down = player.world.getBlockState(foot);
        IBlockState up = player.world.getBlockState(head);
        int matchDown = blockMatched(down);
        int matchUp = blockMatched(up);
        value += matchDown != -1 ? Double.parseDouble(Mechanics.blocks[matchDown].split(";")[1].trim()) : 0;
        value += matchUp != -1 ? Double.parseDouble(Mechanics.blocks[matchUp].split(";")[1].trim()) : 0;
        return value;
    }

    private static double withCreature(EntityPlayer player) {
        double value = 0;
        AxisAlignedBB box = player.getEntityBoundingBox().grow(5, 3, 5);
        for (EntityLivingBase entity: player.world.getEntitiesWithinAABB(EntityLivingBase.class, box)) {
            if (entity != null) {
                int num = entityMatched(entity);
                if (num != -1) {
                    value += Double.parseDouble(Mechanics.entities[num].split(";")[1]);
                    continue;
                }
                if (entity instanceof EntityTameable) {
                    EntityTameable pet = (EntityTameable) entity;
                    value += pet.isTamed() && pet.isOwner(player) ? Mechanics.pet : 0;
                    continue;
                }
                if (entity instanceof EntityMob) {
                    value -= Mechanics.mob;
                    continue;
                }
                if (entity instanceof EntityPlayer && !(entity instanceof FakePlayer) && entity != player) {
                    ISanity sanity = entity.getCapability(SANITY, null);
                    value += sanity.getSanity() >= 50 ? Mechanics.normal : -Mechanics.abnormal;
                }
            }
        }
        return MathHelper.clamp(value, -0.5, 0.5);
    }

    public static boolean canEnable(EntityPlayer player) {
        return Mechanics.blacklist ? Arrays.stream(Mechanics.dimensions).noneMatch(num -> num == player.dimension) : Arrays.stream(Mechanics.dimensions).anyMatch(num -> num == player.dimension);
    }

    private static boolean isThirst(EntityPlayer player) {
        if (Loader.isModLoaded("toughasnails")) {
            IThirst thirstStats = player.getCapability(TANCapabilities.THIRST, null);
            return thirstStats.getThirst() < 6;
        }
        if (Loader.isModLoaded("simpledifficulty")) {
            IThirstCapability thirstStats1 = player.getCapability(SDCapabilities.THIRST, null);
            return thirstStats1.getThirstLevel() < 6;
        }
        return false;
    }

    public static boolean isMorphine(EntityPlayer player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getEffectName().equals("item.morphine.name")) {
                return true;
            }
        }
        return false;
    }
}