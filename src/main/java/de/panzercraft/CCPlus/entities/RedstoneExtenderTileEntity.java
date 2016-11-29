package de.panzercraft.CCPlus.entities;

import java.util.HashMap;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RedstoneExtenderTileEntity extends TileEntity implements IPeripheral {

	private World world;
	public boolean isStrongOutput = false;
	public boolean isWeakOutput = false;
	public int strength = 0;
	public final HashMap<EnumFacing, Integer> output = new HashMap<EnumFacing, Integer>();
	public final HashMap<EnumFacing, Integer> input = new HashMap<EnumFacing, Integer>();

	public RedstoneExtenderTileEntity(World world) {
		this.world = world;
	}

	@Override
	public String getType() {
		return "redstone_extender";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getRedstoneInput", "setRedstoneOutput", "getRedstoneAnalogInput", "setRedstoneAnalogOutput"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		String side = ((String) arguments[0]);
		switch(method) {
			case 0:
				
				return new Object[] {};
			case 1:
				
				return new Object[] {};
			case 2:
				
				return new Object[] {};
			case 3:
				
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
		return other == this && other instanceof RedstoneExtenderTileEntity;
	}
	
}
