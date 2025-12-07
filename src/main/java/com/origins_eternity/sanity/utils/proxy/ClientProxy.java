package com.origins_eternity.sanity.utils.proxy;

import com.origins_eternity.sanity.content.render.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

public class ClientProxy extends CommonProxy {
    public static final ResourceLocation hud = new ResourceLocation(MOD_ID, "textures/gui/sanity.png");
    public static final ResourceLocation blood = new ResourceLocation(MOD_ID, "textures/gui/blood.png");
    public static final ResourceLocation icon = new ResourceLocation(MOD_ID, "textures/gui/icon.png");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        MinecraftForge.EVENT_BUS.register(new Overlay());
    }

    @Override
    public void serverStart(FMLServerStartingEvent event) {
        super.serverStart(event);
    }

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }
}