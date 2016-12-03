package de.panzercraft.CCPlus.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.panzercraft.CCPlus.CCPlus;
import de.panzercraft.CCPlus.Handler.PlayerHandler;
import de.panzercraft.CCPlus.blocks.PlayerDetectorPlus;
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

	public PlayerDetectorPlusTileEntity(World world) {
		this.world = world;
	}
	
	private HashMap<String, Object> getPlayerInfo(EntityPlayer player) {
		HashMap<String, Object> data_player = new HashMap<String, Object>();
		if(player == null) {
			data_player.clear();
			return data_player;
		}
		if(CCPlus.player_detector_plus_player_info_x_enabled) {
			data_player.put("x", player.posX);
		}
		if(CCPlus.player_detector_plus_player_info_y_enabled) {
			data_player.put("y", player.posY);
		}
		if(CCPlus.player_detector_plus_player_info_z_enabled) {
			data_player.put("z", player.posZ);
		}
		if(CCPlus.player_detector_plus_player_info_dim_enabled) {
			data_player.put("dim", player.dimension);
		}
		if(CCPlus.player_detector_plus_player_info_flying_enabled) {
			data_player.put("flying", player.capabilities.isFlying);
		}
		if(CCPlus.player_detector_plus_player_info_health_enabled) {
			data_player.put("health", player.getHealth());
		}
		if(CCPlus.player_detector_plus_player_info_creative_enabled) {
			data_player.put("creative", player.capabilities.isCreativeMode);
		}
		if(CCPlus.player_detector_plus_player_info_foodLevel_enabled) {
			data_player.put("foodLevel", player.getFoodStats().getFoodLevel());
		}
		if(CCPlus.player_detector_plus_player_info_maxHealth_enabled) {
			data_player.put("maxHealth", player.getMaxHealth());
		}
		if(CCPlus.player_detector_plus_player_info_lifeTime_enabled) {
			data_player.put("lifeTime", PlayerHandler.getLifeTime(player));
		}
		if(CCPlus.player_detector_plus_player_info_saturationLevel_enabled) {
			data_player.put("saturationLevel", player.getFoodStats().getSaturationLevel());
		}
		if(CCPlus.player_detector_plus_player_info_onlineTime_enabled) {
			data_player.put("onlineTime", PlayerHandler.getOnlineTime(player));
		}
		if(CCPlus.player_detector_plus_player_info_onlineTimeDimension_enabled) {
			data_player.put("onlineTimeDimension", PlayerHandler.getOnlineTimeActualDimension(player));
		}
		return data_player;
	}

	@Override
	public String getType() {
		return "player_detector_plus";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getWorldID", "getPlayer", "getPlayers"/*, "createExplosion"*/, "getPlayerOnlineTimeDimension"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch(method) {
			case 0:
				return new Object[] {world.provider.dimensionId};
			case 1:
				EntityPlayer ep1 = null;
				if(!world.isRemote) {
					String name = (String) arguments[0];
					ep1 = PlayerPlus.getPlayerByName(name);
				} else {
					ep1 = Minecraft.getMinecraft().thePlayer;
				}
				if(CCPlus.player_detector_plus_player_blacklist_enabled && ep1 != null) {
					boolean forbidden = false;
					for(String name_ : CCPlus.player_detector_plus_blacklisted_players) {
						if(ep1.getDisplayName().equals(name_)) {
							forbidden = true;
							break;
						}
					}
					if(forbidden) {
						ep1 = null;
					}
				}
				return new Object[] {getPlayerInfo(ep1)};
			case 2:
				EntityPlayer[] players = PlayerHandler.getPlayers();
				HashMap<String, HashMap<String, Object>> data_players = new HashMap<String, HashMap<String, Object>>();
				for(EntityPlayer player : players) {
					if(CCPlus.player_detector_plus_player_blacklist_enabled) {
						boolean forbidden = false;
						for(String name : CCPlus.player_detector_plus_blacklisted_players) {
							if(player.getDisplayName().equals(name)) {
								forbidden = true;
								break;
							}
						}
						if(forbidden) {
							continue;
						}
					}
					data_players.put(player.getDisplayName(), getPlayerInfo(player));
				}
				return new Object[] {data_players};
			case 3:
				/*
				if(!CCPlus.debug_enabled || CCPlus.player_detector_plus_explosion_disabled) {
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
				*/
				if(!CCPlus.player_detector_plus_player_info_onlineTimeDimension_enabled) {
					return new Object[] {PlayerHandler.getOnlineTimeDimension(null, 0)};
				}
				EntityPlayer ep3 = null;
				int dimension = 0;
				if(!world.isRemote) {
					String name = (String) arguments[0];
					ep3 = PlayerPlus.getPlayerByName(name);
				} else {
					ep3 = Minecraft.getMinecraft().thePlayer;
				}
				if(CCPlus.player_detector_plus_player_blacklist_enabled && ep3 != null) {
					boolean forbidden = false;
					for(String name_ : CCPlus.player_detector_plus_blacklisted_players) {
						if(ep3.getDisplayName().equals(name_)) {
							forbidden = true;
							break;
						}
					}
					if(forbidden) {
						ep3 = null;
					}
				}
				if(arguments.length > 1) {
					dimension = ((Number) arguments[1]).intValue();
				} else if(ep3 != null) {
					dimension = ep3.dimension;
				}
				return new Object[] {PlayerHandler.getOnlineTimeDimension(ep3, dimension)};
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
		return other == this && other instanceof PlayerDetectorPlusTileEntity;
	}

}