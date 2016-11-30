package de.panzercraft.CCPlus.utils;

import de.panzercraft.CCPlus.blocks.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockUtils {

	public static String getNameForBlock(Block block) {
		return Block.blockRegistry.getNameForObject(block).toString();
	}
	
	public static boolean blockIsGettingExternallyPowered(World world, BlockPos pos) {
		for(EnumFacing side : EnumFacing.values()) {
			if(isPoweringSide(world, pos, side)) {
				return true;
			}
		}
		return false;
	}
	
	public static int getPoweringSideValue(World world, BlockPos pos, EnumFacing side) {
		pos = pos.offset(side);
		Block block = world.getBlock(pos.x, pos.y, pos.z);
		int power_weak = block.isProvidingWeakPower(world, pos.x, pos.y, pos.z, side.ordinal());
		if(power_weak > 0) {
			return power_weak;
		}
		if(block.shouldCheckWeakPower(world, pos.x, pos.y, pos.z, side.ordinal())) {
			for(EnumFacing side2 : EnumFacing.values()) {
				if(side2 != oppositeFacing(side)) {
					BlockPos pos2 = pos.offset(side2);
					Block block2 = world.getBlock(pos2.x, pos2.y, pos2.z);
					int power_strong = block.isProvidingStrongPower(world, pos2.x, pos2.y, pos2.z, side2.ordinal());
					if(power_strong > 0) {
						return power_strong;
					}
				}
			}
		}
		return 0;
	}
	
	public static boolean isPoweringSide(World world, BlockPos pos, EnumFacing side) {
		return getPoweringSideValue(world, pos, side) > 0;
	}
	
	public static EnumFacing oppositeFacing(EnumFacing dir) {
        return EnumFacing.values()[dir.ordinal() ^ 1];
    }
	
}
