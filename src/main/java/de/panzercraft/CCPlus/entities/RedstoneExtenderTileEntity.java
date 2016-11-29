package de.panzercraft.CCPlus.entities;

import java.util.HashMap;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.panzercraft.CCPlus.blocks.RedstoneExtender;
import net.minecraft.block.Block;
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
		init();
	}
	
	private void init() {
		for(EnumFacing face : EnumFacing.values()) {
			output.put(face, 0);
			input.put(face, 0);
		}
	}
	
	private RedstoneExtender getBlock() {
		Block block = world.getBlock(this.xCoord, this.yCoord, this.zCoord);
		if(block instanceof RedstoneExtender) {
			return (RedstoneExtender) block;
		} else {
			return null;
		}
	}

	@Override
	public String getType() {
		return "redstone_extender";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getRedstoneInput", "setRedstoneOutput", "getRedstoneAnalogInput", "setRedstoneAnalogOutput"};
	}
	
	public EnumFacing convertToEnumFacing(String side) {
		return EnumFacing.valueOf(side);
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		String side = ((String) arguments[0]);
		EnumFacing face = convertToEnumFacing(side);
		int strength = 0;
		if(arguments.length > 1) {
			strength = ((Number) arguments[1]).intValue();
		}
		switch(method) {
			case 0:
				getBlock().update(this);
				return new Object[] {(input.get(face) > 0) ? true : false};
			case 1:
				output.put(face, strength);
				isStrongOutput = strength > 0;
				getBlock().update(this);
				return new Object[] {};
			case 2:
				getBlock().update(this);
				return new Object[] {input.get(face)};
			case 3:
				output.put(face, strength);
				isWeakOutput = strength > 0;
				getBlock().update(this);
				return new Object[] {};
			default: 
				getBlock().update(this);
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
