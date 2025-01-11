package com.origins_eternity.sanity;

import com.origins_eternity.sanity.utils.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Sanity.MOD_ID, name = Sanity.MOD_NAME, version = Sanity.VERSION, dependencies = "after:betterquesting@[3.5.259,);")
public class Sanity {
	public static final String MOD_ID = "sanity";
	public static final String MOD_NAME = "Sanity";
	public static final String VERSION = "1.2.0";
	
	@SidedProxy(clientSide = "com.origins_eternity.sanity.utils.proxy.ClientProxy", serverSide = "com.origins_eternity.sanity.utils.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel("sanity");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) { proxy.preInit(event); }

	@EventHandler
	public void init(FMLInitializationEvent event) { proxy.init(event); }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { proxy.postInit(event); }
}