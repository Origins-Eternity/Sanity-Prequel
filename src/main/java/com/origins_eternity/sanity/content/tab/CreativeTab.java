package com.origins_eternity.sanity.content.tab;

import com.origins_eternity.sanity.content.armor.Armors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

public class CreativeTab {
    public static final CreativeTabs SANITY = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            if (Loader.isModLoaded("baubles")) {
                return new ItemStack(Armors.GARLAND, 1);
            } else {
                return new ItemStack(Armors.FLOWER, 1);
            }
        }
    };
}