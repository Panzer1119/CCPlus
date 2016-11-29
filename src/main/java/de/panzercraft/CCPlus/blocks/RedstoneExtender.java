package de.panzercraft.CCPlus.blocks;

import de.panzercraft.CCPlus.entities.PlayerDetectorPlusTileEntity;
import de.panzercraft.CCPlus.entities.RedstoneExtenderTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RedstoneExtender extends BlockContainer {

	public RedstoneExtender(Material blockMaterial) {
		super(blockMaterial);
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
		switch(face) {
			case DOWN:
				return world.getIndirectPowerLevelTo(x, y + 1, z, 4);
			case EAST:
				return world.getIndirectPowerLevelTo(x - 1, y, z, 3);
			case NORTH:
				return world.getIndirectPowerLevelTo(x, y, z + 1, 2);
			case SOUTH:
				return world.getIndirectPowerLevelTo(x, y, z - 1, 0);
			case UP:
				return world.getIndirectPowerLevelTo(x, y - 1, z, -1);
			case WEST:
				return world.getIndirectPowerLevelTo(x + 1, y, z, 1);
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
			for(EnumFacing face : EnumFacing.values()) {
				rete.input.put(face, getPowerLevelInput(rete.getWorldObj(), x, y, z, face));
			}
		}
    }
	
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof RedstoneExtenderTileEntity) {
			RedstoneExtenderTileEntity rete = (RedstoneExtenderTileEntity) te;
			return (rete.isStrongOutput) ? rete.strength : 0;
		} else {
			return 0;
		}
    }
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return side != -1;
    }
	
}
