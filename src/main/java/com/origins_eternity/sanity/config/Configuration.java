package com.origins_eternity.sanity.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

@Config(modid = MOD_ID)
public class Configuration {
    @Config.Name("Kill Max")
    @Config.LangKey("config.ercore.kill")
    @Config.Comment("The max sanity to reduce when player kill mobs.")
    @Config.RangeInt(min = 5, max = 100)
    public static int kill = 5;

    @Config.Name("Eat Max")
    @Config.LangKey("config.ercore.eat")
    @Config.Comment("The max sanity to reduce when player eat rotten food.")
    @Config.RangeInt(min = 2, max = 100)
    public static int eat = 2;

    @Config.Name("Rotten Food")
    @Config.LangKey("config.ercore.food")
    @Config.Comment("The oredict name of rotten food.")
    public static String food = "foodRotten";

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MOD_ID)) {
                ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}