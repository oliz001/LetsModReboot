package com.crystalix007.letsmodreboot.block;

import com.crystalix007.letsmodreboot.tileentities.TileEntityInvisiGlass;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockInvisiGlass extends BlockGlassLMRB implements ITileEntityProvider
{
	public BlockInvisiGlass()
	{
		super("invisiGlass");
		this.setBlockName("invisiGlass");
		this.lightOpacity = 0;
		this.isBlockContainer = true;
	}

	/*@Override //Makes it able to be walked through
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}*/

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float v, float v1, float v2)
	{
		ItemStack currentItem = player.getCurrentEquippedItem();
		if (currentItem == null)
			return false;

		TileEntityInvisiGlass tile = (TileEntityInvisiGlass)world.getTileEntity(x, y, z);

		if (currentItem.getUnlocalizedName().contains("dye"))
		{
			if (currentItem.getItemDamage() == 1 && tile.red < 240) {
				tile.red += 20;

				if (!player.capabilities.isCreativeMode)
					currentItem.stackSize--;
			}
			if (currentItem.getItemDamage() == 2 && tile.green < 240) {
				tile.green += 20;

				if (!player.capabilities.isCreativeMode)
					currentItem.stackSize--;
			}
			if (currentItem.getItemDamage() == 4 && tile.blue < 240) {
				tile.blue += 20;

				if (!player.capabilities.isCreativeMode)
					currentItem.stackSize--;
			}
		}

		/*if (world.isRemote)
		{
			ClientProxy.printMessageToPlayer("Red: " + String.valueOf(tile.red));
			ClientProxy.printMessageToPlayer("Green: " + String.valueOf(tile.green));
			ClientProxy.printMessageToPlayer("Blue: " + String.valueOf(tile.blue));
		}*/

		world.markBlockForUpdate(x, y, z);
		return super.onBlockActivated(world, x, y, z, player, i, v, v1, v2);
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor(IBlockAccess iBlockAccess, int x, int y, int z)
	{
		TileEntityInvisiGlass tile = (TileEntityInvisiGlass)iBlockAccess.getTileEntity(x, y, z);
		return ((65536 * tile.red) + (256 * tile.green) + tile.blue);
	}

	/*@SideOnly(Side.CLIENT)
	public int getRenderColor(int i)
	{
		return getBlockColor();
	}*/

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess iBlockAccess, int x, int y, int z)
	{
		return getBlockColor(iBlockAccess, x, y, z);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityInvisiGlass();
	}

	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer entityPlayer)
	{
		if (entityPlayer.capabilities.isCreativeMode)
			return;
		TileEntityInvisiGlass tile = (TileEntityInvisiGlass)world.getTileEntity(x, y, z);

		world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(Items.dye,tile.red / 20, 1)));
		world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(Items.dye, tile.green / 20, 2)));
		world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(Items.dye, tile.blue / 20, 4)));
	}
}
