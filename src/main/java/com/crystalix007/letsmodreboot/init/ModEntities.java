package com.crystalix007.letsmodreboot.init;

import com.crystalix007.letsmodreboot.LetsModReboot;
import com.crystalix007.letsmodreboot.entity.EntityFlyingCarrot;
import com.crystalix007.letsmodreboot.render.RenderEntityFlyingCarrot;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities
{
	public static void init()
	{
		EntityRegistry.registerModEntity(EntityFlyingCarrot.class, "flyingCarrot", 240, LetsModReboot.instance, 64, 1, true);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingCarrot.class, new RenderEntityFlyingCarrot());
	}
}