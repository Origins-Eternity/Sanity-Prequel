package com.origins_eternity.sanity.content.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

public class Sounds {
    public static SoundEvent INSANITY = addSounds("insanity");
    public static SoundEvent SWISH = addSounds("swish");
    public static SoundEvent FLOWERS_EQUIP = addSounds("flowers_equip");

    private static SoundEvent addSounds(String name) {
        ResourceLocation location = new ResourceLocation(MOD_ID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        GameRegistry.findRegistry(SoundEvent.class).register(event);
        return event;
    }
}
