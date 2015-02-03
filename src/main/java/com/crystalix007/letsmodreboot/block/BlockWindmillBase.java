package com.crystalix007.letsmodreboot.block;

import com.crystalix007.letsmodreboot.creativetab.CreativeTabsLMRB;
import com.crystalix007.letsmodreboot.proxy.ClientProxy;
import com.crystalix007.letsmodreboot.tileentities.TileEntityWindmillBase;
import com.crystalix007.letsmodreboot.utility.PositionedBlock;
import com.crystalix007.letsmodreboot.utility.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Vector;

public class BlockWindmillBase extends BlockContainerLMRB {
	public BlockWindmillBase() {
		super(Material.circuits);
		setBlockName("windmillBaseBlock");
		setCreativeTab(CreativeTabsLMRB.MECHANICAL_TAB);
		setBlockBounds(0, 0, 0, 1, 5.f / 16.f, 1);
		setHarvestLevel("pickaxe", 1);
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int v) {
		super.onBlockDestroyedByPlayer(world, x, y, z, v);

		Vector<PositionedBlock> blocks = WorldHelper.getBlocks(world, x - 1, y, z - 1, x + 1, y, z + 1);

		for (PositionedBlock block : blocks) {
			if (block.getBlock() instanceof BlockWindmillBase)
				((BlockWindmillBase) block.getBlock()).isMultiBlockStructure(world, x, y, z);
		}

		return;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbourBlock) {
		if (neighbourBlock instanceof BlockWindmillBase)
			isMultiBlockStructure(world, x, y, z);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		isMultiBlockStructure(world, x, y, z);
		return;
	}

	public void updateMultiblockStructure(World world, int x, int y, int z) {
		isMultiBlockStructure(world, x, y, z);
		return;
	}

	public boolean isSupported(World world, int x, int y, int z)
	{
		Vector<PositionedBlock> blocks = WorldHelper.getBlocks(world, x - 1, y, z - 1, x + 1, y, z + 1);
		boolean allFound = true;

		for (PositionedBlock block : blocks) {
			if (block.getBlock() instanceof BlockWindmillBase && world.getBlockMetadata(block.xPos, block.yPos, block.zPos) == 2)
			{
				allFound = true;
				blocks = WorldHelper.getBlocks(world, block.xPos - 1, block.yPos, block.zPos - 1, block.xPos + 1, block.yPos, block.zPos + 1);

				for (PositionedBlock positionedBlock : blocks) {
					if (!(positionedBlock.getBlock() instanceof BlockWindmillBase) || world.getBlockMetadata(positionedBlock.xPos, positionedBlock.yPos, positionedBlock.zPos) == 0)
					{
						allFound = false;
						break;
					}
				}

				if (allFound)
					return true;
			}
		}
		return false;
	}


	public boolean isMultiBlockStructure(World world, int x, int y, int z) {
		Vector<PositionedBlock> blocks;
		boolean hasHole;

		for (int xSubtract = -2; xSubtract <= 0; xSubtract++) {
			for (int zSubtract = -2; zSubtract <= 0; zSubtract++) {
				hasHole = false;
				blocks = WorldHelper.getBlocks(world, x + xSubtract, y, z + zSubtract, x + xSubtract + 2, y, z + zSubtract + 2);

				/*world.setBlock(x + xSubtract, y, z + zSubtract, Blocks.wool);
				world.setBlock(x - xSubtract, y, z + zSubtract, Blocks.wool);
				world.setBlock(x - xSubtract, y, z - zSubtract, Blocks.wool);
				world.setBlock(x + xSubtract, y, z - zSubtract, Blocks.wool);*/ //Marks boundaries

				for (PositionedBlock block : blocks) {
					if (!(block.getBlock() instanceof BlockWindmillBase) || (world.getBlockMetadata(block.xPos, block.yPos, block.zPos) != 0)) {
						hasHole = true;
						break;
					}
					/*else {
						ClientProxy.printMessageToPlayer("Block meta: " + String.valueOf(world.getBlockMetadata(block.xPos, block.yPos, block.zPos)));
					}*/
				}

				if (!hasHole) {
					for (PositionedBlock block : blocks) {
						world.setBlockMetadataWithNotify(block.xPos, block.yPos, block.zPos, 1, 2);
					}

					PositionedBlock theCentre = blocks.elementAt(((blocks.size() + 1) / 2) - 1);
					world.setBlockMetadataWithNotify(theCentre.xPos, theCentre.yPos, theCentre.zPos, 2, 2);
					ClientProxy.printMessageToPlayer("Block: " + String.valueOf(theCentre.xPos) + ", " + String.valueOf(theCentre.yPos) + ", " + String.valueOf(theCentre.zPos));
					ClientProxy.printMessageToPlayer("Found complete multiblock");
					return true;
				}
				else
				{
					for (PositionedBlock block : blocks) {
						if (block.getBlock() instanceof BlockWindmillBase && (!((BlockWindmillBase) block.getBlock()).isSupported(world, block.xPos, block.yPos, block.zPos)))
							world.setBlockMetadataWithNotify(block.xPos, block.yPos, block.zPos, 0, 2);
					}
				}

				blocks.clear();
			}
		}
		return false;
	}

	public int getPositionInMultiblock(World world, int x, int y, int z)
	{
		Vector<PositionedBlock> blocks;
		boolean hasHole;

		for (int xSubtract = -2; xSubtract <= 0; xSubtract++) {
			for (int zSubtract = -2; zSubtract <= 0; zSubtract++) {
				hasHole = false;
				blocks = WorldHelper.getBlocks(world, x + xSubtract, y, z + zSubtract, x + xSubtract + 2, y, z + zSubtract + 2);

				for (PositionedBlock block : blocks) {
					if (!(block.getBlock() instanceof BlockWindmillBase) || (world.getBlockMetadata(block.xPos, block.yPos, block.zPos) != 0)) {
						hasHole = true;
						break;
					}
					else {
//						ClientProxy.printMessageToPlayer("Block meta: " + String.valueOf(world.getBlockMetadata(block.xPos, block.yPos, block.zPos)));
					}
				}

				if (!hasHole) {
					PositionedBlock startBlock = blocks.elementAt(0);
					return ((x - startBlock.xPos) + (3 * (z - startBlock.zPos)));
				}
				blocks.clear();
			}
		}

		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityWindmillBase();
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}