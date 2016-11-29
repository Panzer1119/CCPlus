package de.panzercraft.CCPlus.blocks;

import de.panzercraft.CCPlus.entities.PlayerDetectorPlusTileEntity;
import de.panzercraft.CCPlus.entities.RedstoneExtenderTileEntity;
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
		return world.getIndirectPowerLevelTo(x, y, z, face.ordinal());
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof RedstoneExtenderTileEntity) {
			RedstoneExtenderTileEntity rete = (RedstoneExtenderTileEntity) te;
			for(EnumFacing face : EnumFacing.values()) {
				if(face.ordinal() == side) {
					return rete.output.get(face);
				}
			}
			return 0;
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
	
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof RedstoneExtenderTileEntity) {
			RedstoneExtenderTileEntity rete = (RedstoneExtenderTileEntity) te;
			return (rete.isStrongOutput) ? rete.strength : 0;
		} else {
			return 0;
		}
    }
	
}
