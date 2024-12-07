package com.origins_eternity.sanity.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

@Config(modid = MOD_ID)
public class Configuration {
    @Config.Name("Nausea")
    @Config.LangKey("config.sanity.nausea")
    @Config.Comment("The sanity to reduce when player eat rotten food.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double nausea = 2.0;

    @Config.Name("Lightning")
    @Config.LangKey("config.sanity.lightning")
    @Config.Comment("The sanity to reduce when player is struck by lightning.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double lightning = 30.0;

    @Config.Name("Attack")
    @Config.LangKey("config.sanity.attack")
    @Config.Comment("The sanity to reduce when player attack an animal.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double attack = 0.5;

    @Config.Name("Hurt")
    @Config.LangKey("config.sanity.hurt")
    @Config.Comment("The sanity to reduce based on the damage with the ratio of 1 to this number.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double hurt = 1.0;

    @Config.Name("Trip")
    @Config.LangKey("config.sanity.trip")
    @Config.Comment("The sanity to reduce when player change dimension.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double trip = 10.0;

    @Config.Name("Wet")
    @Config.LangKey("config.sanity.wet")
    @Config.Comment("The sanity to reduce when player is wet.(per 0.5s)")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double wet = 0.1;

    @Config.Name("Hunger")
    @Config.LangKey("config.sanity.hunger")
    @Config.Comment("The sanity to reduce when player's foodLevel is low.(per 0.5s)")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double hunger = 0.1;

    @Config.Name("Choking")
    @Config.LangKey("config.sanity.choking")
    @Config.Comment("The sanity to reduce when player's air is low.(per 0.5s)")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double choking = 0.1;

    @Config.Name("Danger")
    @Config.LangKey("config.sanity.danger")
    @Config.Comment("The sanity to reduce when player is dangerous blocks or liquids that in the list below.(per 0.5s)")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double danger = 0.1;

    @Config.Name("Dark")
    @Config.LangKey("config.sanity.dark")
    @Config.Comment("The sanity to reduce when player is in dark.(per 0.5s)")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double dark = 0.1;

    @Config.Name("Eat")
    @Config.LangKey("config.sanity.eat")
    @Config.Comment("The sanity to increase when player eat healthy food.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double eat = 1.0;

    @Config.Name("Sleep")
    @Config.LangKey("config.sanity.sleep")
    @Config.Comment("The sanity to increase when player wake up.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double sleep = 50.0;

    @Config.Name("Advancement")
    @Config.LangKey("config.sanity.advancement")
    @Config.Comment("The sanity to increase when player gain an advancement.")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double advancement = 20.0;

    @Config.Name("Garland")
    @Config.LangKey("config.sanity.garland")
    @Config.Comment("The sanity to increase when player wear a garland.(per 0.5s)")
    @Config.RangeDouble(min = 0.0, max = 100.0)
    public static double garland = 0.2;

    @Config.Name("Bad Food")
    @Config.LangKey("config.sanity.food")
    @Config.Comment("The oredict name of bad food.")
    public static String food = "foodUnhealthy";

    @Config.Name("Dangerous Blocks")
    @Config.LangKey("config.sanity.blocks")
    @Config.Comment("The list of dangerous blocks or liquids which will decrease sanity when player is in it.")
    public static String[] blocks = new String[]{"web", "water"};

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