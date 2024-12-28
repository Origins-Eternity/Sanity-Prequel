package com.origins_eternity.sanity.content.armor;

import com.origins_eternity.sanity.campat.Baubles;
import com.origins_eternity.sanity.content.ArmorCreator;
import com.origins_eternity.sanity.content.material.Flower;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class Armors {
    public static final ItemArmor FLOWER = new ArmorCreator(Flower.FLOWER, 1, EntityEquipmentSlot.HEAD, "Garland", 60);
    public static final ItemArmor GARLAND = new Baubles(Flower.FLOWER, 1, EntityEquipmentSlot.HEAD, "Garland", 60);
}