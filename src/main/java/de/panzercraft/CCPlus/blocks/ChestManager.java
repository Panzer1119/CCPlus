package de.panzercraft.CCPlus.blocks;

import de.panzercraft.CCPlus.CCPlus;
import de.panzercraft.CCPlus.entities.ChestManagerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ChestManager extends BlockContainer {

	public ChestManager(Material material) {
		super(material);
		setBlockName("ChestManager");
		setBlockTextureName("CCPlus:ChestManager");
		setCreativeTab(CCPlus.tabCCPlus);
		setStepSound(Block.soundTypePiston);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int blockID) {
		return new ChestManagerTileEntity(world);
	}

}
