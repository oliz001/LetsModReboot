package com.crystalix007.letsmodreboot.tileentities;

import com.crystalix007.letsmodreboot.init.ModBlocks;
import com.crystalix007.letsmodreboot.utility.PositionedBlock;
import com.crystalix007.letsmodreboot.utility.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;
import java.util.Vector;

public class TileEntityNuclearWaste extends TileEntity
{
	static int minSec = 3, maxSec = 6;
	long remainingTicks;
	int ticksExisted = 0;

	private long abs(int num){
		return (num < 0 ? num * -1 : num);
	}

	public TileEntityNuclearWaste()
	{
		Random random = new Random();

		remainingTicks = ((abs(random.nextInt()) % (maxSec - minSec)) + minSec);

		markDirty();
	}


	void tickNuclearWaste()
	{
		remainingTicks--;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setLong("remainingTicks", remainingTicks);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		remainingTicks = compound.getInteger("remainingTicks");
	}

	public void destroyTileEntity()
	{
		worldObj.removeTileEntity(xCoord, yCoord, zCoord);
		invalidate();
	}

	@Override
	public void updateEntity()
	{
		ticksExisted++;

		if ((ticksExisted % 20) == 0) {
			if (remainingTicks >= 0)
				tickNuclearWaste();

			if (remainingTicks == 0) {
				Vector<PositionedBlock> blocks = WorldHelper.getBlocks(worldObj, xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1);

				for (int i = 0; i < blocks.size(); i++) {
					PositionedBlock positionedBlock = blocks.elementAt(i);
					Block thisBlock = positionedBlock.getBlock();

					if (thisBlock != null && (thisBlock != Blocks.air) && (thisBlock != ModBlocks.blockNuclearWaste) && (thisBlock != ModBlocks.blockBioDetergent) && (thisBlock != ModBlocks.blockLockdown)) {
						if (!thisBlock.getUnlocalizedName().contains("ore")) {

							if (!worldObj.isRemote) {
								worldObj.setBlock(positionedBlock.xPos, positionedBlock.yPos, positionedBlock.zPos, ModBlocks.blockNuclearWaste);
								worldObj.notifyBlockChange(positionedBlock.xPos, positionedBlock.yPos, positionedBlock.zPos, ModBlocks.blockNuclearWaste);
							}
						}
					}
				}
			}
		}

		//Prevent infinite checking
		if (remainingTicks < 0 && !worldObj.isRemote) {
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			worldObj.notifyBlockChange(xCoord, yCoord, zCoord, Blocks.air);
			destroyTileEntity();
		}
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}
}
