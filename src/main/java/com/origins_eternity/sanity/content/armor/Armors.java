package com.origins_eternity.sanity.content.armor;

import com.origins_eternity.sanity.compat.Baubles;
import com.origins_eternity.sanity.content.ArmorCreator;
import com.origins_eternity.sanity.content.material.Materials;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class Armors {
    public static final ItemArmor FLOWER = new ArmorCreator(Materials.FLOWER, 1, EntityEquipmentSlot.HEAD, "Garland", 60);
    public static final ItemArmor GARLAND = new Baubles(Materials.FLOWER, 1, EntityEquipmentSlot.HEAD, "Garland", 60);
}