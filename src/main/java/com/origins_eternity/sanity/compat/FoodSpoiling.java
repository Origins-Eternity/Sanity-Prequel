package com.origins_eternity.sanity.compat;


import com.origins_eternity.sanity.capability.sanity.ISanity;
import mod.acgaming.foodspoiling.logic.FSData;
import mod.acgaming.foodspoiling.logic.FSLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.origins_eternity.sanity.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.config.Configuration.Compat;
import static mod.acgaming.foodspoiling.logic.FSLogic.canRot;

public class FoodSpoiling {
    public static int getPercentage(ItemStack stack, EntityPlayer player) {
        int per = 100;
        if (canRot(stack) == EnumActionResult.SUCCESS && FSData.hasCreationTime(stack)) {
            long exist = player.world.getTotalWorldTime() - FSData.getCreationTime(stack);
            int total = FSLogic.getTicksToRot(player, stack);
            per = MathHelper.clamp(100 - (int) (exist * 100L / (long) total), 0, 100);
            if (FSData.hasRemainingLifetime(stack)) {
                per = FSData.getRemainingLifetime(stack) * 100 / total;
            }
        }
        return per;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            if (player.ticksExisted % 20 == 0 && !player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                for (ItemStack stack : player.inventory.mainInventory) {
                    if (stack.getItem() instanceof ItemFood && getPercentage(stack, player) == 0) {
                        sanity.consumeSanity(Compat.foodSpoiling);
                    }
                }
            }
        }
    }
}