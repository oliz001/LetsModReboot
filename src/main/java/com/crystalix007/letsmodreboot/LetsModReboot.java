package com.crystalix007.letsmodreboot;

import com.crystalix007.letsmodreboot.client.handler.KeyInputHandler;
import com.crystalix007.letsmodreboot.event.EventHookContainerClass;
import com.crystalix007.letsmodreboot.handler.ConfigurationHandler;
import com.crystalix007.letsmodreboot.init.*;
import com.crystalix007.letsmodreboot.proxy.IProxy;
import com.crystalix007.letsmodreboot.reference.Reference;
import com.crystalix007.letsmodreboot.utility.LogHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class LetsModReboot
{
	@Mod.Instance(Reference.MOD_ID)
	public static LetsModReboot instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)    //Network handling, mod config, items and blocks init
	{
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());

		//Registers key bindings
		proxy.registerKeyBindings();

		//Init items
		ModItems.init();
		//Init blocks
		ModBlocks.init();
		//Init OreDictionary stuff
		ModOreDictionary.init();

		LogHelper.info("Pre-initialization complete");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)  //Register GUIs, TileEntities, crafting recipes, general, even handlers
	{
		FMLCommonHandler.instance().bus().register(new KeyInputHandler());
		MinecraftForge.EVENT_BUS.register(new EventHookContainerClass());

		Recipes.init();
		ModEntities.init();
		ModBlocks.registerTileEntities();

		LogHelper.info("Initialization complete");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)  //Things you want to do after other mods have loaded
	{
		for (String oreName : OreDictionary.getOreNames())
			LogHelper.info("Id: "+ OreDictionary.getOreID(oreName) + ", name: " + oreName);

		LogHelper.info("Post-initialization complete");
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event){
		ModCommands.init(event);
	}
}