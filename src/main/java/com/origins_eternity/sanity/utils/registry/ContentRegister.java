package com.origins_eternity.sanity.utils.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static com.origins_eternity.sanity.Sanity.MOD_ID;
import static com.origins_eternity.sanity.content.armor.Armors.FLOWER;
import static com.origins_eternity.sanity.content.armor.Armors.GARLAND;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ContentRegister {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if (Loader.isModLoaded("baubles")) {
            event.getRegistry().register(GARLAND);
        } else {
            event.getRegistry().register(FLOWER);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        if (Loader.isModLoaded("baubles")) {
            ModelLoader.setCustomModelResourceLocation(GARLAND, 0, new ModelResourceLocation(Objects.requireNonNull(GARLAND.getRegistryName()), "inventory"));
        } else {
            ModelLoader.setCustomModelResourceLocation(FLOWER, 0, new ModelResourceLocation(Objects.requireNonNull(FLOWER.getRegistryName()), "inventory"));
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerModels();
    }
}