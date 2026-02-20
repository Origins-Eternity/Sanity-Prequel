package com.origins_eternity.sanity.content.tab;

import com.origins_eternity.sanity.content.armor.Garland;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

public class CreativeTab {
    public static final CreativeTabs SANITY = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Garland.GARLAND, 1);
        }
    };
}