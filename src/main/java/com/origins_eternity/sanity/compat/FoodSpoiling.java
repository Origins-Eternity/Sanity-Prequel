package com.origins_eternity.sanity.compat;


import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import mod.acgaming.foodspoiling.logic.FSData;
import mod.acgaming.foodspoiling.logic.FSMaps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.origins_eternity.sanity.config.Configuration.Mechanics;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static mod.acgaming.foodspoiling.logic.FSLogic.getTicksToRot;

public class FoodSpoiling {
    public static double getPercentage(ItemStack stack, EntityPlayer player) {
        if (FSData.hasCreationTime(stack)) {
            long creationTime = FSData.getCreationTime(stack);
            int rotTicks = getTicksToRot(player, stack);
            if (rotTicks >= 0) {
                long existTime = player.world.getTotalWorldTime() - creationTime;
                double percentage = 1 - existTime / (double)rotTicks;
                return Math.round(percentage * 10) / 10.0;
            }
        }
        return 0;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            if (player.ticksExisted % 20 == 0 && !player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                if (isSpoiling(player)) {
                    sanity.consumeSanity(Mechanics.foodSpoiling);
                }
            }
        }
    }

    private static boolean isSpoiling(EntityPlayer player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem() instanceof ItemFood) {
                ItemStack food = new ItemStack(stack.getItem(), 1, stack.getMetadata());
                for (ItemStack rot : FSMaps.FOOD_CONVERSIONS.values()) {
                    if (ItemStack.areItemStacksEqual(food, rot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}