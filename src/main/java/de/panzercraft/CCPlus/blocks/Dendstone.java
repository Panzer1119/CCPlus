package de.panzercraft.CCPlus.blocks;

import de.panzercraft.CCPlus.CCPlus;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class Dendstone extends Block {

	public Dendstone(Material material) {
		super(material);
		setBlockName("Dendstone");
		setBlockTextureName("CCPlus:Dendstone");
		setCreativeTab(CCPlus.tabCCPlus);
		setStepSound(Block.soundTypeStone);
		setResistance(1000F);
		setHardness(20F);
		setLightLevel(0.1F);
		//setHarvestLevel(, 3);
	}

}
