package de.panzercraft.CCPlus.Handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
		File file = getLogFile(player);
		File file_done = savePlayerLogToFile(player, file);
		System.out.println(String.format("Player \"%s\" logged out (%s) (File: %s)", player.getDisplayName(), LocalDateTime.ofInstant(logged_out, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), file));
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
	
	public static void checkAchievements() {
		//System.out.println("Checking Achievements");
		for(EntityPlayer player : getPlayers()) {
			//System.out.println(String.format("X: %.2f, Z: %.2f", player.posX, player.posZ));
			if(player.dimension == 0 && ((int) Math.abs(player.posX)) == 0 && ((int) Math.abs(player.posZ)) == 0) {
				player.addStat(CCPlus.achievement_visit_groundzero, 1);
			}
		}
	}
	
	public static boolean isInGame() {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			try {
				return Minecraft.getMinecraft().theWorld != null;
			} catch (Exception ex) {
				return false;
			}
		} else {
			return true;
		}
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
		//System.out.println("Logged \"" + player.getDisplayName() + "\"");
		final HashMap<Instant, BlockPosExact> positions = (hawk_eye.get(player) != null) ? hawk_eye.get(player) : new HashMap<Instant, BlockPosExact>();
		BlockPosExact pos = new BlockPosExact(player.posX, player.posY, player.posZ);
		pos.instant = now;
		pos.dimension = player.dimension;
		positions.put(now, pos);
		hawk_eye.put(player, positions);
	}
	
	public static void logPlayers(Instant now) {
		EntityPlayer[] players = getPlayers();
		for(EntityPlayer player : players) {
			logPlayer(player, now);
		}
	}
	
	public static File getLogOutputFolder() {
		File output = null;
		if(!CCPlus.debug_log_output_folder.isEmpty()) {
			output = new File(CCPlus.debug_log_output_folder);
		}
		return output;
	}
	
	public static File getLogFile(EntityPlayer player) {
		String extension = "txt";
		File file = null;
		File folder =  getLogOutputFolder();
		if(folder != null) {
			file = new File(folder.getAbsolutePath() + File.separator + player.getDisplayName() + ((extension.isEmpty()) ? "" : "." + extension));
		}
		return file;
	}

	public static File savePlayerLogToFile(EntityPlayer player, File file) {
		boolean stop = false;
		if(file == null) {
			System.err.println("File is null");
			stop = true;
		} else if(file.exists()) {
			System.out.println("File exists");
			if(file.isDirectory()) {
				System.err.println("File exists and is a directory");
				stop = true;
			}
		} else if(player == null) {
			System.err.println("Player is null");
			stop = true;
		} else if(hawk_eye.get(player) == null) {
			System.err.println("Player has no data");
			stop = true;
		}
		if(stop) {
			return null;
		}
		/*
		if(file == null || (file.exists() && file.isDirectory()) || player == null || hawk_eye.get(player) == null) {
			return null;
		}
		*/
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			//System.out.println("Created the file: " + file.getAbsolutePath());
			HashMap<Instant, BlockPosExact> data = hawk_eye.get(player);
			ArrayList<BlockPosExact> pos = new ArrayList<BlockPosExact>();
			for(Instant instant : data.keySet()) {
				BlockPosExact pos_new = data.get(instant);
				pos_new.instant = instant;
				pos.add(pos_new);
			}
			pos.sort(new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					BlockPosExact b1 = (BlockPosExact) o1;
					BlockPosExact b2 = (BlockPosExact) o2;
					return b1.instant.compareTo(b2.instant);
				}
				
				
			});
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			for(BlockPosExact bpe : pos) {
				bw.write(String.format("%s#%s", bpe.instant.toString(), bpe.toString()));
				bw.newLine();
			}
			bw.close();
			fw.close();
			bw = null;
			fw = null;
			return file;
		} catch (Exception ex) {
			System.err.println("Exception while writing to the log file");
			return null;
		}
	}
	
	public static ArrayList<BlockPosExact> loadPlayerFromLogFile(File file) {
		if(file == null || !file.exists() || (file.exists() && file.isDirectory())) {
			return null;
		}
		HashMap<Instant, BlockPosExact> data = new HashMap<Instant, BlockPosExact>();
		ArrayList<BlockPosExact> pos = new ArrayList<BlockPosExact>();
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				try {
					Instant timestamp = Instant.parse(line.split("#")[0]);
					String pos_string = line.split("#")[1];
					BlockPosExact blockpos = new BlockPosExact(pos_string, ";");
					data.put(timestamp, blockpos);
				} catch (Exception ex) {
					System.err.println("Error while reading log: " + ex);
				}
			}
			scanner.close();
			scanner = null;
			for(Instant instant : data.keySet()) {
				BlockPosExact pos_new = data.get(instant);
				pos_new.instant = instant;
				pos.add(pos_new);
			}
			pos.sort(new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					BlockPosExact b1 = (BlockPosExact) o1;
					BlockPosExact b2 = (BlockPosExact) o2;
					return b1.instant.compareTo(b2.instant);
				}
				
				
			});
		} catch (Exception ex) {
			System.err.println("Error while reading from the log file");
		}
		return pos;
	}
	
	public static void printLog(File file) {
		//new File("E:\\Daten\\Minecraft\\Mods\\Eigene\\1.7.10\\forge-1.7.10-10.13.4.1614-1.7.10-src\\eclipse\\logs\\Player441.txt")
		ArrayList<BlockPosExact> data = PlayerHandler.loadPlayerFromLogFile(file);
    	for(BlockPosExact pos : data) {
    		System.out.println(String.format("%s: %s", LocalDateTime.ofInstant(pos.instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), pos.toString()));
    	}
	}
	
}
