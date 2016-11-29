package de.panzercraft.CCPlus;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import de.panzercraft.CCPlus.blocks.PlayerDetectorPlus;
import de.panzercraft.CCPlus.blocks.RedstoneExtender;
import de.panzercraft.CCPlus.entities.PlayerDetectorPlusTileEntity;
import de.panzercraft.CCPlus.entities.RedstoneExtenderTileEntity;
import ibxm.Player;

@Mod(modid = CCPlus.MODID, version = CCPlus.VERSION, dependencies = "required-after:ComputerCraft;after:CCTurtle")
public class CCPlus {
	
    public static final String MODID = "CCPlus";
    public static final String VERSION = "0.1";
    
    public static boolean player_detector_plus_explosion_disabled = true;
    
    public PlayerDetectorPlus playerdetectorplusinstance = new PlayerDetectorPlus(Material.ground);
    public RedstoneExtender redstoneextenderinstance = new RedstoneExtender(Material.ground);
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    	Property prop = config.get("debug", "player_detector_plus_explosion_disabled", player_detector_plus_explosion_disabled);
    	prop.comment = "Disables the createExplosion function from the player detector plus which was only for testing (Default: true)";
    	player_detector_plus_explosion_disabled = prop.getBoolean(player_detector_plus_explosion_disabled);
    	config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {/*
    	playerdetectorplusinstance.setCreativeTab(CreativeTabs.tabMisc);
    	GameRegistry.registerBlock(playerdetectorplusinstance, "de.panzercraft.block.PlayerDetectorPlus");
    	GameRegistry.registerTileEntity(PlayerDetectorPlusTileEntity.class, "de.panzercraft.entities.PlayerDetectorPlusTileEntitry");
    	LanguageRegistry.addName(playerdetectorplusinstance, "Player Detector Plus");
    	ComputerCraftAPI.registerPeripheralProvider(new IPeripheralProvider() {

			@Override
			public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
				TileEntity temp = world.getTileEntity(x, y, z);
				if(temp instanceof PlayerDetectorPlusTileEntity) {
					return (IPeripheral) temp;
				} else {
					return null;
				}
			}
    		
    	});*/
    	redstoneextenderinstance.setCreativeTab(CreativeTabs.tabMisc);
    	GameRegistry.registerBlock(redstoneextenderinstance, "de.panzercraft.block.RedstoneExtender");
    	GameRegistry.registerTileEntity(RedstoneExtenderTileEntity.class, "de.panzercraft.entities.RedstoneExtenderTileEntitry");
    	LanguageRegistry.addName(redstoneextenderinstance, "Redstone Extender");
    	ComputerCraftAPI.registerPeripheralProvider(new IPeripheralProvider() {

			@Override
			public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
				TileEntity temp = world.getTileEntity(x, y, z);
				if(temp instanceof RedstoneExtenderTileEntity) {
					return (IPeripheral) temp;
				} else {
					return null;
				}
			}
    		
    	});
        //System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //System.out.println(getPlayerUUID());
    }
    
    public UUID getPlayerUUID() {
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
