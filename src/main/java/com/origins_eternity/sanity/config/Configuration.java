package com.origins_eternity.sanity.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

@Config(modid = MOD_ID)
public class Configuration {

    @Config.Name("Mechanics")
    @Config.Comment("Mechanics Options")
    @Config.LangKey("config.sanity.mechanics")
    public static ConfigMechanics Mechanics = new ConfigMechanics();

    @Config.Name("Overlay")
    @Config.Comment("Overlay Options")
    @Config.LangKey("config.sanity.overlay")
    public static ConfigOverlay Overlay = new ConfigOverlay();

    @Config.Name("Shader")
    @Config.Comment("Shader Options")
    @Config.LangKey("config.sanity.shader")
    public static ConfigShader Shader = new ConfigShader();

    public static class ConfigMechanics {
        @Config.Name("Reset Sanity")
        @Config.LangKey("config.sanity.reset")
        @Config.Comment("Whether to reset sanity after respawning.")
        public boolean reset = true;

        @Config.Name("Attack Animal")
        @Config.LangKey("config.sanity.animal")
        @Config.Comment("The sanity to decrease when players attack an animal.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double attackAnimal = 0.5;

        @Config.Name("Attack Villager")
        @Config.LangKey("config.sanity.villager")
        @Config.Comment("The sanity to decrease when players attack a villager.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double attackVillager = 1.5;

        @Config.Name("Attack Player")
        @Config.LangKey("config.sanity.player")
        @Config.Comment("The sanity to decrease when players attack another player.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double attackPlayer = 2.0;

        @Config.Name("Lightning")
        @Config.LangKey("config.sanity.lightning")
        @Config.Comment("The sanity to decrease when players are struck by lightning.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double lightning = 30.0;

        @Config.Name("Hurt")
        @Config.LangKey("config.sanity.hurt")
        @Config.Comment("The sanity to decrease based on the damage with the ratio of 1 to this number.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double hurt = 1.0;

        @Config.Name("Fall")
        @Config.LangKey("config.sanity.fall")
        @Config.Comment("The sanity to decrease based on the fall distance with the ratio of 1 to this number.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double fall = 1.0;

        @Config.Name("Trip")
        @Config.LangKey("config.sanity.trip")
        @Config.Comment("The sanity to decrease when players change dimension.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double trip = 10.0;

        @Config.Name("Rain")
        @Config.LangKey("config.sanity.rain")
        @Config.Comment("The sanity to decrease when players get wet by rain. (per 0.5s)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double rain = 0.1;

        @Config.Name("Hunger")
        @Config.LangKey("config.sanity.hunger")
        @Config.Comment("The sanity to decrease when player's foodLevel is low. (per 0.5s)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double hunger = 0.1;

        @Config.Name("Thirst")
        @Config.LangKey("config.sanity.thirst")
        @Config.Comment("The sanity to decrease when player's thirstLevel is low. (per 0.5s, support ToughAsNails and SimpleDifficulty)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double thirst = 0.1;

        @Config.Name("Choking")
        @Config.LangKey("config.sanity.choking")
        @Config.Comment("The sanity to decrease when player's air is low.(per 0.5s)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double choking = 0.1;

        @Config.Name("Dark")
        @Config.LangKey("config.sanity.dark")
        @Config.Comment("The sanity to decrease when players are in dark. (per 0.5s)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double dark = 0.1;

        @Config.Name("Mob")
        @Config.LangKey("config.sanity.mob")
        @Config.Comment("The sanity to decrease when mobs are around player. (per 0.5s, within 8 blocks)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double mob = 0.1;

        @Config.Name("Kill Mob")
        @Config.LangKey("config.sanity.kill")
        @Config.Comment("The sanity to increase when players kill a mob.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double killMob = 2.5;

        @Config.Name("Eat")
        @Config.LangKey("config.sanity.eat")
        @Config.Comment("The sanity to increase when players eat food that isn't in the list.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double eat = 5.0;

        @Config.Name("Quest")
        @Config.LangKey("config.sanity.quest")
        @Config.Comment("The sanity to increase when players complete a quest. (only support FTB Quests)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double quest = 10.0;

        @Config.Name("Sleep")
        @Config.LangKey("config.sanity.sleep")
        @Config.Comment("The sanity to increase when players wake up.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double sleep = 50.0;

        @Config.Name("Bred")
        @Config.LangKey("config.sanity.bred")
        @Config.Comment("The sanity to increase when players bred animals.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double bred = 10.0;

        @Config.Name("Advancement")
        @Config.LangKey("config.sanity.advancement")
        @Config.Comment("The sanity to increase when players gain an advancement.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double advancement = 15.0;

        @Config.Name("Garland")
        @Config.LangKey("config.sanity.garland")
        @Config.Comment("The sanity to increase when players wear a garland. (per 0.5s)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double garland = 0.2;

        @Config.Name("Pet")
        @Config.LangKey("config.sanity.pet")
        @Config.Comment("The sanity to increase when players stay with pets. (per 0.5s, within 5 blocks)")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double pet = 0.2;

        @Config.Name("Lost")
        @Config.LangKey("config.sanity.lost")
        @Config.Comment("The sanity to decrease when players lose pets.")
        @Config.RangeDouble(min = 0.0, max = 100.0)
        public double lost = 30.0;

        @Config.Name("Food")
        @Config.LangKey("config.sanity.food")
        @Config.Comment("The list of food and the sanity to decrease or increase when eat it. ('food;value')")
        public String[] food = new String[]{"minecraft:rotten_flesh;-3.0", "minecraft:spider_eye;-3.0", "minecraft:chicken;-2.0", "minecraft:porkchop;-2.0", "minecraft:mutton;-2.0", "minecraft:beef;-2.0", "minecraft:rabbit;-2.0", "minecraft:poisonous_potato;-2.0", "minecraft:fish;-2.0", "minecraft:fish:1;-2.0", "minecraft:fish:2;-2.0", "minecraft:fish:3;-2.0"};

        @Config.Name("Environment")
        @Config.LangKey("config.sanity.environment")
        @Config.Comment("The list of blocks or liquids which will decrease or increase sanity when players are in it. ('block;value', per 0.5s)")
        public String[] blocks = new String[]{"minecraft:web;-0.1", "minecraft:water;-0.1"};

        @Config.Name("Entities")
        @Config.LangKey("config.sanity.entities")
        @Config.Comment("The list of entities which will decrease sanity when players attack it. ('entity;value')")
        public String[] entities = new String[]{"minecraft:enderman;1.0"};
        
        @Config.Name("Equipment")
        @Config.LangKey("config.sanity.equipment")
        @Config.Comment("The list of equipment which will decrease or increase sanity when players wear it. ('equipment;value', per 0.5s)")
        public String[] equipment = new String[]{"minecraft:pumpkin;-0.1"};
        
        @Config.Name("Sounds")
        @Config.LangKey("config.sanity.sounds")
        @Config.Comment("The list of sounds which will play randomly when sanity is low.")
        public String[] sounds = new String[]{"entity.creeper.primed", "entity.tnt.primed", "entity.skeleton.ambient", "entity.skeleton.step", "entity.zombie.ambient", "entity.zombie.step", "entity.enderman.ambient", "entity.hostile.big_fall", "block.chest.open", "block.chest.close", "block.wooden_door.open", "block.wooden_trapdoor.open", "entity.wolf.growl"};

        @Config.Name("Dimensions")
        @Config.LangKey("config.sanity.dimensions")
        @Config.Comment("The list of dimensions which will enable sanity.")
        public int[] dimensions = new int[]{1, 0, -1};

        @Config.Name("Blacklist")
        @Config.LangKey("config.sanity.blacklist")
        @Config.Comment("Make the list of dimensions blacklist.")
        public boolean blacklist = false;
    }

    public static class ConfigOverlay {
        @Config.Name("Blood Overlay")
        @Config.LangKey("config.sanity.blood")
        @Config.Comment("Whether to enable the blood overlay.")
        public boolean blood = true;

        @Config.Name("Brain Overlay")
        @Config.LangKey("config.sanity.brain")
        @Config.Comment("Whether to enable the brain overlay.")
        public boolean brain = true;

        @Config.Name("Brain OffX")
        @Config.LangKey("config.sanity.offx")
        @Config.Comment("Offset on x of the Brain. A positive number means a shift to the right.")
        public int offX = 0;

        @Config.Name("Brain OffY")
        @Config.LangKey("config.sanity.offy")
        @Config.Comment("Offset on y of the Brain. A positive number means a shift to the top.")
        public int offY = 0;

        @Config.Name("Brain Flash Time")
        @Config.LangKey("config.sanity.flash")
        @Config.Comment("How many seconds the brain should be visible when sanity changed. (set this to -1 to disable)")
        @Config.RangeInt(min = -1, max = 30)
        public int flash = -1;
    }

    public static class ConfigShader {
        @Config.Name("Level1")
        @Config.RequiresMcRestart
        @Config.LangKey("config.sanity.level1")
        @Config.Comment("Set level1 shader and enable when sanity is lower than the value. ('shader;value')")
        public String level1 = "deconverge.json;60";

        @Config.Name("Level2")
        @Config.RequiresMcRestart
        @Config.LangKey("config.sanity.level2")
        @Config.Comment("Set level2 shader and enable when sanity is lower than the value. ('shader;value')")
        public String level2 = "notch.json;40";

        @Config.Name("Level3")
        @Config.RequiresMcRestart
        @Config.LangKey("config.sanity.level3")
        @Config.Comment("Set level3 shader and enable when sanity is lower than the value. ('shader;value')")
        public String level3 = "bits.json;10";
    }

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