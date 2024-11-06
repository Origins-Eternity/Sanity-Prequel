package com.origins_eternity.sanity.content.material;

import com.origins_eternity.sanity.Sanity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class Flower {
    public static final ItemArmor.ArmorMaterial FLOWER = EnumHelper.addArmorMaterial("flower", Sanity.MOD_ID + ":flower",80, new int[]{0, 0, 0, 0}, 0, SoundEvents.BLOCK_GRASS_PLACE, 0);
}
