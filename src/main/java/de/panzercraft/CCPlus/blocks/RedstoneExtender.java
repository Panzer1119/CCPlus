package de.panzercraft.CCPlus.blocks;

import java.util.Random;

import de.panzercraft.CCPlus.entities.PlayerDetectorPlusTileEntity;
import de.panzercraft.CCPlus.entities.RedstoneExtenderTileEntity;
import de.panzercraft.CCPlus.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneExtender extends BlockContainer {

	public RedstoneExtender(Material blockMaterial) {
		super(blockMaterial);
		setBlockName("RedstoneExtender");
		setBlockTextureName("CCPlus:RedstoneExtender");
		setCreativeTab(CreativeTabs.tabRedstone);
		setStepSound(Block.soundTypePiston);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int blockID) {
		return new RedstoneExtenderTileEntity(world);
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	private int getPowerLevelInput(World world, int x, int y, int z, EnumFacing face) {
		int side = convertFromEnumFacing(face);
		switch(face) {
			case DOWN:
				return world.getIndirectPowerLevelTo(x, y + 1, z, side);
			case EAST:
				return world.getIndirectPowerLevelTo(x - 1, y, z, side);
			case NORTH:
				return world.getIndirectPowerLevelTo(x, y, z + 1, side);
			case SOUTH:
				return world.getIndirectPowerLevelTo(x, y, z - 1, side);
			case UP:
				return world.getIndirectPowerLevelTo(x, y - 1, z, side);
			case WEST:
				return world.getIndirectPowerLevelTo(x + 1, y, z, side);
			default:
				return 0;
		}
	}
	
	public static EnumFacing convertToEnumFacing(int side) {
		switch(side) {
			case -1:
				return EnumFacing.UP;
			case 0:
				return EnumFacing.NORTH;
			case 1:
				return EnumFacing.EAST;
			case 2:
				return EnumFacing.SOUTH;
			case 3:
				return EnumFacing.WEST;
			case 4:
				return EnumFacing.DOWN;
			default:
				return EnumFacing.UP;
		}
	}
	
	public static int convertFromEnumFacing(EnumFacing face) {
		switch(face) {
			case DOWN:
				return 4;
			case EAST:
				return 1;
			case NORTH:
				return 0;
			case SOUTH:
				return 2;
			case UP:
				return -1;
			case WEST:
				return 3;
			default:
				return -1;
		}
	}
	
	public void update(RedstoneExtenderTileEntity rete) {
		World world = rete.getWorldObj();
		int x = rete.xCoord;
		int y = rete.yCoord;
		int z = rete.zCoord;
		world.notifyBlocksOfNeighborChange(x, y, z, this);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
	}
	
	private void reload(RedstoneExtenderTileEntity rete, BlockPos pos) {
		for(EnumFacing face : EnumFacing.values()) {
			rete.input.put(face, BlockUtils.getPoweringSideValue(rete.getWorldObj(), pos, face));
			//System.out.println(String.format("%s: has power on side %s: %d", pos.toString(), face.name(), BlockUtils.getPoweringSideValue(rete.getWorldObj(), pos, face)));
		}
		//System.out.println(String.format("%s: has power: %b", pos.toString(), BlockUtils.blockIsGettingExternallyPowered(rete.getWorldObj(), pos)));
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof RedstoneExtenderTileEntity) {
			RedstoneExtenderTileEntity rete = (RedstoneExtenderTileEntity) te;
			return rete.output.get(convertToEnumFacing(side));
		} else {
			return 0;
		}
    }
	
	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof RedstoneExtenderTileEntity) {
			RedstoneExtenderTileEntity rete = (RedstoneExtenderTileEntity) te;
			reload(rete, new BlockPos(x, y, z));
		}
    }
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		onNeighborChange(world, x, y, z, x, y, z);
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof RedstoneExtenderTileEntity) {
			RedstoneExtenderTileEntity rete = (RedstoneExtenderTileEntity) te;
			return (rete.output_strong.get(convertToEnumFacing(side))) ? rete.output.get(convertToEnumFacing(side)) : 0;
		} else {
			return 0;
		}
    }
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return side != -1;
    }
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}
	
}
