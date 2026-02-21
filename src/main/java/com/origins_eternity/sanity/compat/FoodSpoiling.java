package com.origins_eternity.sanity.compat;


import mod.acgaming.foodspoiling.config.FSConfig;
import mod.acgaming.foodspoiling.logic.FSData;
import mod.acgaming.foodspoiling.logic.FSLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;

public class FoodSpoiling {

    public static double getSpoilagePercentage(ItemStack stack, long currentWorldTime) {
        if (stack == null || stack.isEmpty()) {
            return 0.0;
        }

        if (!FSData.hasCreationTime(stack)) {
            return 0.0;
        }

        double days = FSLogic.getExpirationDays(stack);
        if (Double.isNaN(days) || days < 0) {
            return 0.0;
        }

        long creationTime = FSData.getCreationTime(stack);
        
        if (currentWorldTime == 0) {
            return 0.0;
        }

        long elapsedTime = currentWorldTime - creationTime;
        int totalSpoilTicks = (int) (days * FSConfig.GENERAL.dayLengthInTicks);

        if (totalSpoilTicks <= 0) {
            return 0.0;
        }

        double spoilagePercentage = (double) elapsedTime / totalSpoilTicks;
        return Math.min(Math.max(spoilagePercentage, 0.0), 1.0);
    }

    public static double calculateSanityPenalty(EntityPlayer player) {
        if (!FSConfig.ROTTING.rotInCreative && player.isCreative()) {
            return 0.0;
        }

        long currentWorldTime = player.world.getTotalWorldTime();
        double totalSpoilage = 0.0;
        int spoiledFoodCount = 0;

        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack stack = player.inventory.mainInventory.get(i);
            if (FSLogic.canRot(stack) == EnumActionResult.SUCCESS) {
                double spoilage = getSpoilagePercentage(stack, currentWorldTime);
                if (spoilage > 0.0) {
                    totalSpoilage += spoilage;
                    spoiledFoodCount++;
                }
            }
        }

        if (spoiledFoodCount == 0) {
            return 0.0;
        }

        return totalSpoilage / spoiledFoodCount;
    }
}
