package de.panzercraft.CCPlus.blocks;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.panzercraft.CCPlus.entities.PlayerDetectorPlusTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerDetectorPlus extends BlockContainer {
	
	public PlayerDetectorPlus(Material blockMaterial) {
		super(blockMaterial);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int blockID) {
		return new PlayerDetectorPlusTileEntity(world);
	}

}
