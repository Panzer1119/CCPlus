package de.panzercraft.CCPlus.entities;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAnalyzerTileEntity extends TileEntity implements IPeripheral {
	
	private World world;
	
	public BlockAnalyzerTileEntity(World world) {
		this.world = world;
	}

	@Override
	public String getType() {
		return "block_anaylzer";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {}; 
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch(method) {
			default:
				return new Object[] {};
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
		return other == this && other instanceof BlockAnalyzerTileEntity;
	}

}
