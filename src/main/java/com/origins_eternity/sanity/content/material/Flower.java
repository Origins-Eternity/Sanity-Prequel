package com.origins_eternity.sanity.content.material;

import com.origins_eternity.sanity.Sanity;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import static com.origins_eternity.sanity.content.sound.Sounds.FLOWERS_EQUIP;

public class Flower {
    public static final ItemArmor.ArmorMaterial FLOWER = EnumHelper.addArmorMaterial("flower", Sanity.MOD_ID + ":flower",80, new int[]{0, 0, 0, 0}, 0, FLOWERS_EQUIP, 0);
}
