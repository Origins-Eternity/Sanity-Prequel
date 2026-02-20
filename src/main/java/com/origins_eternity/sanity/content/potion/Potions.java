package com.origins_eternity.sanity.content.potion;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;

public class Potions {
    public static final ArrayList<Potion> POTIONS = new ArrayList<>();
    public static final Potion Composure = new Composure();
    public static final PotionType Composure_Potion = new PotionType("composure", new PotionEffect(Composure, 600)).setRegistryName("composure");
    public static final PotionType Long_Composure_Potion = new PotionType("composure", new PotionEffect(Composure, 1800)).setRegistryName("long_composure");
    public static final PotionType Strong_Composure_Potion = new PotionType("composure", new PotionEffect(Composure, 300, 1)).setRegistryName("strong_composure");
    public static final PotionType[] types = {Composure_Potion, Long_Composure_Potion, Strong_Composure_Potion};

    public static void registerPotionRecipes() {
        NonNullList<ItemStack> flowers = NonNullList.create();
        Blocks.RED_FLOWER.getSubBlocks(CreativeTabs.DECORATIONS, flowers);
        Blocks.YELLOW_FLOWER.getSubBlocks(CreativeTabs.DECORATIONS, flowers);

        PotionHelper.addMix(PotionTypes.REGENERATION, Ingredient.fromStacks(flowers.toArray(new ItemStack[0])), Composure_Potion);
        PotionHelper.addMix(Composure_Potion, Items.REDSTONE, Long_Composure_Potion);
        PotionHelper.addMix(Composure_Potion, Items.GLOWSTONE_DUST, Strong_Composure_Potion);

        for (PotionType type : types) {
            PotionHelper.addMix(type, Items.GUNPOWDER, type);
            PotionHelper.addMix(type, Items.DRAGON_BREATH, type);
        }
    }
}