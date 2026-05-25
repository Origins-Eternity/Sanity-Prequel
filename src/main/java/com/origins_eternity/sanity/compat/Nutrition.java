package com.origins_eternity.sanity.compat;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import com.origins_eternity.sanity.capability.sanity.ISanity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

import static com.origins_eternity.sanity.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.config.Configuration.Compat;

public class Nutrition {
    @CapabilityInject(INutrientManager.class)
    private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            if (player.ticksExisted % 20 == 0 && !player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                String[] parts = Compat.nutrition.split(";");
                if (parts.length == 4) {
                    float value = getAverage(player);
                    int min = Integer.parseInt(parts[2]);
                    int max = Integer.parseInt(parts[3]);
                    if (value < min || value > max) {
                        double dec = Double.parseDouble(parts[0]);
                        double acc = Double.parseDouble(parts[1]);
                        double increase = value < min ? dec : acc;
                        double decrease = value > max ? acc : dec;
                        sanity.setIncreaseFactor(increase);
                        sanity.setDecreaseFactor(decrease);
                    }
                }
            }
        }
    }

    private static float getAverage(EntityPlayer player) {
        float total = 0f;
        Map<Nutrient, Float> playerNutrition = player.getCapability(NUTRITION_CAPABILITY, null).get();
        for(Nutrient nutrient : NutrientList.getVisible()) {
            total += playerNutrition.get(nutrient);
        }
        int size = NutrientList.getVisible().size();
        return size > 0 ? total / NutrientList.get().size() : -1f;
    }
}
