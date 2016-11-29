package de.panzercraft.CCPlus.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PlayerPlus {
	
	public static EntityPlayer getPlayerByName(String name) {
		EntityPlayer ep = null;
		for(Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if(o instanceof EntityPlayer) {
				EntityPlayer temp = (EntityPlayer) o;
				if(temp.getDisplayName().equals(name)) {
					ep = temp;
					break;
				}
			}
		}
		return ep;
	}

}
