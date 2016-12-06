package de.panzercraft.CCPlus.utils;

import net.minecraft.entity.Entity;

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

}
