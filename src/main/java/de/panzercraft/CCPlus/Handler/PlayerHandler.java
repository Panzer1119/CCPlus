package de.panzercraft.CCPlus.Handler;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import de.panzercraft.CCPlus.CCPlus;
import de.panzercraft.CCPlus.blocks.BlockPosExact;
import de.panzercraft.CCPlus.utils.PlayerPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerHandler {

	public static final HashMap<EntityPlayer, Instant> logged_in_times = new HashMap<EntityPlayer, Instant>();
	public static final HashMap<EntityPlayer, HashMap<Integer, Instant>> logged_in_times_dimensions = new HashMap<EntityPlayer, HashMap<Integer, Instant>>();
	public static final HashMap<EntityPlayer, Instant> respawned_times = new HashMap<EntityPlayer, Instant>();
	public static final HashMap<EntityPlayer, HashMap<Instant, BlockPosExact>> hawk_eye = new HashMap<EntityPlayer, HashMap<Instant, BlockPosExact>>();
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		Instant logged_in = Instant.now();
		EntityPlayer player = event.player;
		boolean isOP = PlayerPlus.isOperator(player);
		logged_in_times.put(player, logged_in);
		respawned_times.put(player, logged_in);
		logged_in_times_dimensions.put(player, new HashMap<Integer, Instant>());
		logged_in_times_dimensions.get(player).put(player.dimension, logged_in);
		System.out.println(String.format("Player \"%s\" logged in (%s) Operator: %b", player.getDisplayName(), LocalDateTime.ofInstant(logged_in, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), isOP));
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		Instant logged_out = Instant.now();
		EntityPlayer player = event.player;
		if(logged_in_times.containsKey(player)) {
			logged_in_times.remove(player);
		}
		if(logged_in_times_dimensions.containsKey(player)) {
			logged_in_times_dimensions.remove(player);
		}
		if(respawned_times.containsKey(player)) {
			respawned_times.remove(player);
		}
		System.out.println(String.format("Player \"%s\" logged out (%s)", player.getDisplayName(), LocalDateTime.ofInstant(logged_out, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))));
	}
	
	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		Instant changed_dimension = Instant.now();
		EntityPlayer player = event.player;
		final HashMap<Integer, Instant> dim_times = (logged_in_times_dimensions.get(player) != null) ? logged_in_times_dimensions.get(player) : new HashMap<Integer, Instant>();
		dim_times.put(player.dimension, changed_dimension);
		logged_in_times_dimensions.put(player, dim_times);
		System.out.println(String.format("Player \"%s\" changed dimension to %d (%s)", player.getDisplayName(), player.dimension, LocalDateTime.ofInstant(changed_dimension, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))));
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Instant respawned = Instant.now();
		EntityPlayer player = event.player;
		respawned_times.put(player, respawned);
		System.out.println(String.format("Player \"%s\" respawned (%s)", player.getDisplayName(), LocalDateTime.ofInstant(respawned, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))));
	}

	@SubscribeEvent
	public void itemCrafted(ItemCraftedEvent event) {
		if(event.crafting.getItem().equals(new ItemStack(CCPlus.playerdetectorplusinstance, 1).getItem())) {
			event.player.addStat(CCPlus.achievement_craftPDP, 1);
		}
	}
	
	@SubscribeEvent
	public void itemSmelted(ItemSmeltedEvent event) {
		
	}
	
	@SubscribeEvent
	public void itemPickedUp(ItemPickupEvent event) {
		
	}
	
	public static EntityPlayer[] getPlayers() {
		EntityPlayer[] players = new EntityPlayer[MinecraftServer.getServer().getConfigurationManager().getCurrentPlayerCount()];
		int i = 0;
		for(Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if(o instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) o;
				players[i] = player;
				i++;
			}
		}
		return players;
	}
	
	public static long getOnlineTime(EntityPlayer player) {
		Instant now = Instant.now();
		if(player != null) {
			long online_time = 0;
			Instant logged_in = logged_in_times.get(player);
			if(logged_in != null) {
				online_time = Duration.between(logged_in, now).getSeconds();
			}
			return online_time;
		} else {
			return -1;
		}
	}

	public static long getOnlineTimeDimension(EntityPlayer player, int dimension) {
		Instant now = Instant.now();
		if(player != null) {
			long online_time_dimension = 0;
			if(logged_in_times_dimensions.get(player) != null) {
				Instant logged_in_dimension = logged_in_times_dimensions.get(player).get(dimension);
				if(logged_in_dimension != null) {
					online_time_dimension = Duration.between(logged_in_dimension, now).getSeconds();
				}
			}
			return online_time_dimension;
		} else {
			return -1;
		}
	}
	
	public static long getOnlineTimeActualDimension(EntityPlayer player) {
		if(player != null) {
			return getOnlineTimeDimension(player, player.dimension);
		} else {
			return -1;
		}
	}
	
	public static long getLifeTime(EntityPlayer player) {
		Instant now = Instant.now();
		if(player != null) {
			long life_time = 0;
			Instant respawned = respawned_times.get(player);
			if(respawned != null) {
				life_time = Duration.between(respawned, now).getSeconds();
			}
			return life_time;
		} else {
			return -1;
		}
	}
	
	public static void logPlayer(EntityPlayer player, Instant now) {
		if(player == null || now == null) {
			return;
		}
		final HashMap<Instant, BlockPosExact> positions = (hawk_eye.get(player) != null) ? hawk_eye.get(player) : new HashMap<Instant, BlockPosExact>();
		positions.put(now, new BlockPosExact(player.posX, player.posY, player.posZ));
		hawk_eye.put(player, positions);
	}
	
	public static void logPlayers(Instant now) {
		EntityPlayer[] players = getPlayers();
		for(EntityPlayer player : players) {
			logPlayer(player, now);
		}
	}
	
}
