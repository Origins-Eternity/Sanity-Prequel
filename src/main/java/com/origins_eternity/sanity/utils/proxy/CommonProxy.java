package com.origins_eternity.sanity.utils.proxy;

import com.origins_eternity.sanity.campat.FTBQuests;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.message.SyncSanity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.origins_eternity.sanity.Sanity.packetHandler;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        registerMessage();
    }

    public void init(FMLInitializationEvent event) {
        Capabilities.registerCapability(CapabilityManager.INSTANCE);
        if (Loader.isModLoaded("ftbquests")) {
            MinecraftForge.EVENT_BUS.register(FTBQuests.class);
        }
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    private static void registerMessage() {
        packetHandler.registerMessage(SyncSanity.Handler.class, SyncSanity.class, 0, Side.CLIENT);
    }
}