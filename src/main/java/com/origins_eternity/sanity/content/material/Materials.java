package com.origins_eternity.sanity.content.material;

import com.origins_eternity.sanity.Sanity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import static com.origins_eternity.sanity.content.sound.Sounds.FLOWERS_EQUIP;

public class Materials {
    public static final ItemArmor.ArmorMaterial FLOWER = EnumHelper.addArmorMaterial("flower", Sanity.MOD_ID + ":flower",60, new int[]{0, 0, 0, 0}, 0, FLOWERS_EQUIP, 0);
    public static final Item.ToolMaterial LEATHER = EnumHelper.addToolMaterial("leather", 0, 120, 1f, 0f, 15);
}
