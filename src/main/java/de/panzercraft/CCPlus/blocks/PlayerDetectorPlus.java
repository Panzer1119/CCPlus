package de.panzercraft.CCPlus.blocks;

import de.panzercraft.CCPlus.entities.PlayerDetectorPlusTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PlayerDetectorPlus extends BlockContainer {
	
	public PlayerDetectorPlus(Material blockMaterial) {
		super(blockMaterial);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int blockID) {
		return new PlayerDetectorPlusTileEntity(world);
	}

}
