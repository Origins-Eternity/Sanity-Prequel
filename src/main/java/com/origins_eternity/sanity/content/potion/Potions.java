package com.origins_eternity.sanity.content.potion;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import java.util.ArrayList;

public class Potions {
    public static final ArrayList<Potion> POTIONS = new ArrayList<>();
    public static final Potion Composure = new Composure();
    public static final PotionType Composure_Potion = new PotionType(new PotionEffect(Composure, 600)).setRegistryName("composure");
    public static final PotionType Long_Composure_Potion = new PotionType("composure", new PotionEffect(Composure, 1800)).setRegistryName("long_composure");
    public static final PotionType Strong_Composure_Potion = new PotionType("composure", new PotionEffect(Composure, 300, 1)).setRegistryName("strong_composure");

    public static void registerRecipes() {
        addRecipe(PotionTypes.SWIFTNESS, Item.getItemFromBlock(Blocks.RED_FLOWER), Composure_Potion);
        addRecipe(PotionTypes.SWIFTNESS, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), Composure_Potion);
        addRecipe(Composure_Potion, Items.REDSTONE, Long_Composure_Potion);
        addRecipe(Composure_Potion, Items.GLOWSTONE_DUST, Strong_Composure_Potion);
    }

    private static void addRecipe(PotionType fromType, Item ingredient, PotionType toType) {
        ItemStack input = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), fromType);
        ItemStack output = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), toType);
        BrewingRecipeRegistry.addRecipe(input, new ItemStack(ingredient), output);
    }
}