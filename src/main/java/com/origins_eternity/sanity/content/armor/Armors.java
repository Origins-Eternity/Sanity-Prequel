package com.origins_eternity.sanity.content.armor;

import com.origins_eternity.sanity.content.ArmorCreator;
import com.origins_eternity.sanity.content.material.Flower;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

import java.util.ArrayList;
import java.util.List;

public class Armors {
    public static final List<Item> ARMORS = new ArrayList<>();
    public static final ItemArmor FLOWER = new ArmorCreator(Flower.FLOWER, 1, EntityEquipmentSlot.HEAD, "Garland", 80);
}