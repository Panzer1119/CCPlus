package de.panzercraft.CCPlus.entities;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.panzercraft.CCPlus.blocks.BlockPos;
import de.panzercraft.CCPlus.blocks.ChestManager;
import de.panzercraft.CCPlus.blocks.RedstoneExtender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ChestManagerTileEntity extends TileEntity implements IPeripheral {
	
	private World world;
	
	public ChestManagerTileEntity(World world) {
		this.world = world;
	}
	
	private ChestManager getBlock() {
		Block block = world.getBlock(this.xCoord, this.yCoord, this.zCoord);
		if(block instanceof ChestManager) {
			return (ChestManager) block;
		} else {
			return null;
		}
	}

	@Override
	public String getType() {
		return "chest_manager";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"isChestPresent", "getStackInSlot"}; 
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		String side = ((String) arguments[0]);
		EnumFacing face = EnumFacing.valueOf(side);
		ChestManager chestmanager = getBlock();
		BlockPos blockpos_this = new BlockPos(this);
		switch(method) {
			case 0:
				return new Object[] {isChestPresent(face)};
			case 1:
				if(isChestPresent(face)) {
					int slot = ((Number) arguments[1]).intValue();
					BlockChest chest = (BlockChest) getChest(face);
					TileEntityChest tileentitychest = getChestTileEntity(face); 
					return new Object[] {tileentitychest.getStackInSlot(slot).toString()};
				}
				return new Object[] {};
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
		return this == other && other instanceof ChestManagerTileEntity;
	}
	
	private Block getChest(EnumFacing face) {
		BlockPos blockpos_this = new BlockPos(this);
		BlockPos blockpos_chest = blockpos_this.offset(face);
		Block block_chest = world.getBlock(blockpos_chest.x, blockpos_chest.y, blockpos_chest.z);
		return block_chest;
	}
	
	private boolean isChestPresent(EnumFacing face) {
		Block block_chest = getChest(face);
		return block_chest != null && block_chest instanceof BlockChest;
	}
	
	private TileEntityChest getChestTileEntity(EnumFacing face) {
		BlockPos blockpos_this = new BlockPos(this);
		BlockPos blockpos_chest = blockpos_this.offset(face);
		TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(blockpos_chest.x, blockpos_chest.y, blockpos_chest.z);
		return tileentitychest;
	}

}
