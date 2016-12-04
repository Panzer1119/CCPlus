package de.panzercraft.CCPlus.blocks;

import de.panzercraft.CCPlus.CCPlus;
import de.panzercraft.CCPlus.entities.BlockAnalyzerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAnalyzer extends BlockContainer {

	public BlockAnalyzer(Material material) {
		super(material);
		setBlockName("BlockAnalyzer");
		setBlockTextureName("CCPlus:BlockAnalyzer");
		setCreativeTab(CCPlus.tabCCPlus);
		setStepSound(Block.soundTypePiston);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int blockID) {
		return new BlockAnalyzerTileEntity(world);
	}

}
