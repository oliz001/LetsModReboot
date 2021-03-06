package com.crystalix007.letsmodreboot.init;

import com.crystalix007.letsmodreboot.block.BlockFlag;
import com.crystalix007.letsmodreboot.block.BlockLMRB;
import com.crystalix007.letsmodreboot.block.BlockSiliconNitride;
import com.crystalix007.letsmodreboot.block.BlockSpinel;
import com.crystalix007.letsmodreboot.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks
{
	public static final BlockLMRB flag = new BlockFlag();
	public static final BlockLMRB spinel = new BlockSpinel();
	public static final BlockLMRB siliconNitride = new BlockSiliconNitride();

	public static void init()
	{
		GameRegistry.registerBlock(flag, "flag");
		GameRegistry.registerBlock(spinel, "spinel");
		GameRegistry.registerBlock(siliconNitride, "siliconNitride");
	}
}
