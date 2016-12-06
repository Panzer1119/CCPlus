package de.panzercraft.CCPlus.utils;

import de.panzercraft.CCPlus.blocks.BlockPos;
import de.panzercraft.CCPlus.blocks.BlockPosExact;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class MathPlus {
	
	public static double distanceXYZ(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2) + Math.pow(Math.abs(z1 - z2), 2));
	}

	public static double distanceXZ(double x1, double z1, double x2, double z2) {
		return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(z1 - z2), 2));
	}
	
	public static double distanceXYZ(Entity e1, Entity e2) {
		if(e1.dimension != e2.dimension) {
			return -1.0;
		} else {
			return distanceXYZ(e1.posX, e1.posY, e1.posZ, e2.posX, e2.posY, e2.posZ);
		}
	}
	
	public static double distanceXZ(Entity e1, Entity e2) {
		if(e1.dimension != e2.dimension) {
			return -1.0;
		} else {
			return distanceXZ(e1.posX, e1.posZ, e2.posX, e2.posZ);
		}
	}
	
	public static double distanceXYZ(Entity e1, double x2, double y2, double z2) {
		return distanceXYZ(e1.posX, e1.posY, e1.posZ, x2, y2, z2);
	}
	
	public static double distanceXZ(Entity e1, double x2, double z2) {
		return distanceXZ(e1.posX, e1.posZ, x2, z2);
	}
	
	public static double distanceXYZ(TileEntity e1, TileEntity e2) {
		if(e1.getWorldObj().provider.dimensionId != e2.getWorldObj().provider.dimensionId) {
			return -1.0;
		} else {
			return distanceXYZ(e1.xCoord, e1.yCoord, e1.zCoord, e2.xCoord, e2.yCoord, e2.zCoord);
		}
	}
	
	public static double distanceXZ(TileEntity e1, TileEntity e2) {
		if(e1.getWorldObj().provider.dimensionId != e2.getWorldObj().provider.dimensionId) {
			return -1.0;
		} else {
			return distanceXZ(e1.xCoord, e1.zCoord, e2.xCoord, e2.zCoord);
		}
	}
	
	public static double distanceXYZ(TileEntity e1, double x2, double y2, double z2) {
		return distanceXYZ(e1.xCoord, e1.yCoord, e1.zCoord, x2, y2, z2);
	}
	
	public static double distanceXZ(TileEntity e1, double x2, double z2) {
		return distanceXZ(e1.xCoord, e1.zCoord, x2, z2);
	}
	
	public static double distanceXYZ(BlockPos pos1, BlockPos pos2) {
		return distanceXYZ(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
	}
	
	public static double distanceXZ(BlockPos pos1, BlockPos pos2) {
		return distanceXZ(pos1.x, pos1.z, pos2.x, pos2.z);
	}
	
	public static double distanceXYZ(BlockPosExact pos1, BlockPosExact pos2) {
		return distanceXYZ(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
	}
	
	public static double distanceXZ(BlockPosExact pos1, BlockPosExact pos2) {
		return distanceXZ(pos1.x, pos1.z, pos2.x, pos2.z);
	}

}
