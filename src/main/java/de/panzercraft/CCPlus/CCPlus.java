package de.panzercraft.CCPlus;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import de.panzercraft.CCPlus.Handler.PlayerHandler;
import de.panzercraft.CCPlus.Proxies.CCPlusProxy;
import de.panzercraft.CCPlus.blocks.BlockAnalyzer;
import de.panzercraft.CCPlus.blocks.PlayerDetectorPlus;
import de.panzercraft.CCPlus.blocks.RedstoneExtender;
import de.panzercraft.CCPlus.entities.BlockAnalyzerTileEntity;
import de.panzercraft.CCPlus.entities.PlayerDetectorPlusTileEntity;
import de.panzercraft.CCPlus.entities.RedstoneExtenderTileEntity;
import de.panzercraft.CCPlus.threads.UpdaterThread;
import ibxm.Player;

@Mod(modid = CCPlus.MODID, version = CCPlus.VERSION, dependencies = "required-after:ComputerCraft;after:CCTurtle")
public class CCPlus {
	
    public static final String MODID = "CCPlus";
    public static final String VERSION = "0.1.4_2016.12.03";
    
    @Instance(value = "CCPlus")
    public static CCPlus ccplus;
    
    @SidedProxy(clientSide = "de.panzercraft.CCPlus.Proxies.CCPlusClientProxy", serverSide = "de.panzercraft.CCPlus.Proxies.CCPlusProxy")
    public static CCPlusProxy proxy;
    
    public static final ExecutorService executor = Executors.newFixedThreadPool(10);
    
    public static boolean debug_enabled = false;
    public static boolean debug_hawk_eye_enabled = false;
    public static boolean player_detector_plus_explosion_disabled = true;
    public static boolean player_detector_plus_player_info_x_enabled = true;
    public static boolean player_detector_plus_player_info_y_enabled = true;
    public static boolean player_detector_plus_player_info_z_enabled = true;
    public static boolean player_detector_plus_player_info_dim_enabled = true;
    public static boolean player_detector_plus_player_info_flying_enabled = true;
    public static boolean player_detector_plus_player_info_health_enabled = true;
    public static boolean player_detector_plus_player_info_creative_enabled = true;
    public static boolean player_detector_plus_player_info_foodLevel_enabled = true;
    public static boolean player_detector_plus_player_info_maxHealth_enabled = true;
    public static boolean player_detector_plus_player_info_lifeTime_enabled = true;
    public static boolean player_detector_plus_player_info_isOperator_enabled = true;
    public static boolean player_detector_plus_player_info_saturationLevel_enabled = true;
    public static boolean player_detector_plus_player_info_onlineTime_enabled = true;
    public static boolean player_detector_plus_player_info_onlineTimeDimension_enabled = true;
    public static String[] player_detector_plus_blacklisted_players = new String[] {};
    public static boolean player_detector_plus_player_blacklist_enabled = false;
    public static boolean block_analyzer_enable_dimensional_analysis = false;
    public static int block_analyzer_range = 100;
    
    public static File config_file = null;
    
    //BLOCKS
    public final PlayerDetectorPlus playerdetectorplusinstance = new PlayerDetectorPlus(Material.ground);
    public final RedstoneExtender redstoneextenderinstance = new RedstoneExtender(Material.ground);
    public final BlockAnalyzer blockanalyzerinstance = new BlockAnalyzer(Material.ground);
    
    public static Thread updater = new UpdaterThread();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	config_file = event.getSuggestedConfigurationFile();
    	Configuration config = new Configuration(config_file);
    	config.load();
    	
    	Property prop = config.get("debug", "debug_enabled", debug_enabled);
    	prop.comment = "Enables or disables all debug functions (Default: false)";
    	debug_enabled = prop.getBoolean(debug_enabled);
    	
    	prop = config.get("debug", "debug_hawk_eye_enabled", debug_hawk_eye_enabled);
    	prop.comment = "Enables or disables the hawk_eye like player position logger function (Default: false)";
    	debug_hawk_eye_enabled = prop.getBoolean(debug_hawk_eye_enabled);
    	
    	prop = config.get("debug", "player_detector_plus_explosion_disabled", player_detector_plus_explosion_disabled);
    	prop.comment = "Disables the createExplosion function from the player detector plus which was only for testing (Default: true)";
    	player_detector_plus_explosion_disabled = prop.getBoolean(player_detector_plus_explosion_disabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_x_enabled", player_detector_plus_player_info_x_enabled);
    	prop.comment = "Enables the x information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_x_enabled = prop.getBoolean(player_detector_plus_player_info_x_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_y_enabled", player_detector_plus_player_info_y_enabled);
    	prop.comment = "Enables the y information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_y_enabled = prop.getBoolean(player_detector_plus_player_info_y_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_z_enabled", player_detector_plus_player_info_z_enabled);
    	prop.comment = "Enables the z information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_z_enabled = prop.getBoolean(player_detector_plus_player_info_z_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_dim_enabled", player_detector_plus_player_info_dim_enabled);
    	prop.comment = "Enables the dimension information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_dim_enabled = prop.getBoolean(player_detector_plus_player_info_dim_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_flying_enabled", player_detector_plus_player_info_flying_enabled);
    	prop.comment = "Enables the flying information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_flying_enabled = prop.getBoolean(player_detector_plus_player_info_flying_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_health_enabled", player_detector_plus_player_info_health_enabled);
    	prop.comment = "Enables the health information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_health_enabled = prop.getBoolean(player_detector_plus_player_info_health_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_creative_enabled", player_detector_plus_player_info_creative_enabled);
    	prop.comment = "Enables the creative information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_creative_enabled = prop.getBoolean(player_detector_plus_player_info_creative_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_foodLevel_enabled", player_detector_plus_player_info_foodLevel_enabled);
    	prop.comment = "Enables the food level information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_foodLevel_enabled = prop.getBoolean(player_detector_plus_player_info_foodLevel_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_maxHealth_enabled", player_detector_plus_player_info_maxHealth_enabled);
    	prop.comment = "Enables the maximum health information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_maxHealth_enabled = prop.getBoolean(player_detector_plus_player_info_maxHealth_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_lifeTime_enabled", player_detector_plus_player_info_lifeTime_enabled);
    	prop.comment = "Enables the life time in seconds information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_lifeTime_enabled = prop.getBoolean(player_detector_plus_player_info_lifeTime_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_isOperator_enabled", player_detector_plus_player_info_isOperator_enabled);
    	prop.comment = "Enables the operator information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_isOperator_enabled = prop.getBoolean(player_detector_plus_player_info_isOperator_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_saturationLevel_enabled", player_detector_plus_player_info_saturationLevel_enabled);
    	prop.comment = "Enables the saturation level information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_saturationLevel_enabled = prop.getBoolean(player_detector_plus_player_info_saturationLevel_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_onlineTime_enabled", player_detector_plus_player_info_onlineTime_enabled);
    	prop.comment = "Enables the online time in seconds information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_onlineTime_enabled = prop.getBoolean(player_detector_plus_player_info_onlineTime_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_info_onlineTimeDimension_enabled", player_detector_plus_player_info_onlineTimeDimension_enabled);
    	prop.comment = "Enables the online time for the actual dimension in seconds information from the player detector plus to be received by the getPlayer method (Default: true)";
    	player_detector_plus_player_info_onlineTimeDimension_enabled = prop.getBoolean(player_detector_plus_player_info_onlineTimeDimension_enabled);
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_blacklisted_players", player_detector_plus_blacklisted_players);
    	prop.comment = "This is the blacklist for players which are not visible for the player detector plus";
    	player_detector_plus_blacklisted_players = prop.getStringList();
    	
    	prop = config.get("player_detector_plus", "player_detector_plus_player_blacklist_enabled", player_detector_plus_player_blacklist_enabled);
    	prop.comment = "Enables or disables the blacklist for the players (Default: false)";
    	player_detector_plus_player_blacklist_enabled = prop.getBoolean(player_detector_plus_player_blacklist_enabled);
    	if(player_detector_plus_blacklisted_players.length == 0) {
    		player_detector_plus_player_blacklist_enabled = false;
    	}
    	
    	prop = config.get("block_analyzer", "block_analyzer_enable_dimensional_analysis", block_analyzer_enable_dimensional_analysis);
    	prop.comment = "Enables the analysis over any dimension (Default: false)";
    	block_analyzer_enable_dimensional_analysis = prop.getBoolean(block_analyzer_enable_dimensional_analysis);
    	
    	prop = config.get("block_analyzer", "block_analyzer_range", block_analyzer_range);
    	prop.comment = "Sets the range/radius of the block analyzer (-1 for infinite a range) (Default: 100)";
    	block_analyzer_range = prop.getInt(block_analyzer_range);
    	
    	config.save();
    	
    	FMLCommonHandler.instance().bus().register(new PlayerHandler());
    	
    	registerAnything();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.registerRenderers();
        //System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    	loadRecipes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //System.out.println(getPlayerUUID());
    	ComputerCraftAPI.registerPeripheralProvider(new IPeripheralProvider() {

			@Override
			public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
				TileEntity temp = world.getTileEntity(x, y, z);
				if(temp instanceof PlayerDetectorPlusTileEntity || temp instanceof RedstoneExtenderTileEntity || temp instanceof BlockAnalyzerTileEntity) {
					return (IPeripheral) temp;
				} else {
					return null;
				}
			}
    		
    	});
    	executor.execute(updater);
    }
    
    private void loadRecipes() {
    	GameRegistry.addShapedRecipe(
    			new ItemStack(playerdetectorplusinstance, 1), 
    			"XYX",
    			"YZY",
    			"XYX",
    			Character.valueOf('X'), Blocks.stone, 
    			Character.valueOf('Y'), Items.ender_eye, 
    			Character.valueOf('Z'), Items.redstone);
    	GameRegistry.addShapedRecipe(
    			new ItemStack(redstoneextenderinstance, 1), 
    			"XYX",
    			"YZY",
    			"XYX",
    			Character.valueOf('X'), Blocks.stone, 
    			Character.valueOf('Y'), Blocks.redstone_torch, 
    			Character.valueOf('Z'), Items.redstone);
    }
    
    private void registerAnything() {
    	GameRegistry.registerBlock(playerdetectorplusinstance, "PlayerDetectorPlus");
    	GameRegistry.registerTileEntity(PlayerDetectorPlusTileEntity.class, "PlayerDetectorPlusTileEntity");
    	GameRegistry.registerBlock(redstoneextenderinstance, "RedstoneExtender");
    	GameRegistry.registerTileEntity(RedstoneExtenderTileEntity.class, "RedstoneExtenderTileEntity");
    	GameRegistry.registerBlock(blockanalyzerinstance, "BlockAnalyzer");
    	GameRegistry.registerTileEntity(BlockAnalyzerTileEntity.class, "BlockAnalyzerTileEntity");
    }
    
    public static UUID getPlayerUUID() {
        try {
        	Minecraft minecraft = Minecraft.getMinecraft();
            try {
            	EntityClientPlayerMP player = minecraft.thePlayer;
                try {
                	UUID playerUUID = player.getUniqueID();
                	return playerUUID;
                } catch (Exception ex) {
                	System.err.println("Error while getting the UUID");
                }
            } catch (Exception ex) {
            	System.err.println("Error while getting the player");
            }
        } catch (Exception ex) {
        	System.err.println("Error while getting Minecraft");
        }
        return null;
    }
    
}
