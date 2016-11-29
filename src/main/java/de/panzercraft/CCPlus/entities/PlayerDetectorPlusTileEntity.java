package de.panzercraft.CCPlus.entities;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.panzercraft.CCPlus.utils.MathPlus;
import de.panzercraft.CCPlus.utils.PlayerPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PlayerDetectorPlusTileEntity extends TileEntity implements IPeripheral {
	
	private World world;
	private static final double range = 10.0;
	private boolean explosion_disabled = true;

	public PlayerDetectorPlusTileEntity(World world) {
		this.world = world;
	}

	@Override
	public String getType() {
		return "player_detector_plus";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getWorldID", "getPlayer", "getPlayers", "createExplosion"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch(method) {
			case 0:
				//Test das geht
				//Eclipse First Edit
				return new Object[] {world.provider.dimensionId};
			case 1:
				EntityPlayer ep1 = null;
				if(!world.isRemote) {
					String name = (String) arguments[0];
					ep1 = PlayerPlus.getPlayerByName(name);
				} else {
					ep1 = Minecraft.getMinecraft().thePlayer;
				}
				return new Object[] {ep1.posX, ep1.posY, ep1.posZ, ep1.dimension};
			case 2:
				Object[] data = new Object[MinecraftServer.getServer().getConfigurationManager().playerEntityList.size()];
				String[] names = new String[data.length + 1];
				for(int i = 0; i < data.length; i++) {
					data[i] = MinecraftServer.getServer().getConfigurationManager().playerEntityList.get(i);
					if(data[i] instanceof EntityPlayer) {
						EntityPlayer ep2 = (EntityPlayer) data[i];
						names[i] = ep2.getDisplayName();
					} else {
						names[i] = null;
					}
				}
				names[names.length - 1] = "Paul";
				return new Object[] {names};
			case 3: //
				if(explosion_disabled) {
					return new Object[] {false};
				}
				Double explosionX = ((Number) arguments[0]).doubleValue();
				Double explosionY = ((Number) arguments[1]).doubleValue();
				Double explosionZ = ((Number) arguments[2]).doubleValue();
				double distance = MathPlus.distanceXYZ(explosionX, explosionY, explosionZ, this.xCoord, this.yCoord, this.zCoord);
				if(distance < 0 || distance > range) {
					return new Object[] {false};
				} else {
					Float explosionSize = ((Number) arguments[3]).floatValue();
					EntityCreeper exploder = new EntityCreeper(world);
					world.createExplosion(exploder, explosionX, explosionY, explosionZ, explosionSize, true);
					return new Object[] {true};
				}
			default:
					
				return null;
		}
	}

	@Override
	public void attach(IComputerAccess computer) {
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}

	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}

}
