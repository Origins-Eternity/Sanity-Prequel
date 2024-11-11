package com.origins_eternity.sanity.utils.registry;

import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.armor.Armors;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ContentRegister {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item armor : Armors.ARMORS) {
            event.getRegistry().register(armor);
        }
        OreDictionary.registerOre(Configuration.food, Items.ROTTEN_FLESH);
        OreDictionary.registerOre(Configuration.food, Items.CHICKEN);
        OreDictionary.registerOre(Configuration.food, Items.PORKCHOP);
        OreDictionary.registerOre(Configuration.food, Items.MUTTON);
        OreDictionary.registerOre(Configuration.food, Items.BEEF);
        OreDictionary.registerOre(Configuration.food, Items.RABBIT);
        OreDictionary.registerOre(Configuration.food, Items.POISONOUS_POTATO);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        for (Item armor : Armors.ARMORS) {
            ModelLoader.setCustomModelResourceLocation(armor, 0, new ModelResourceLocation(Objects.requireNonNull(armor.getRegistryName()), "inventory"));
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerModels();
    }
}