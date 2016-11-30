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
	public int strength = 0;
	public final HashMap<EnumFacing, Boolean> output_strong = new HashMap<EnumFacing, Boolean>();
	public final HashMap<EnumFacing, Integer> output = new HashMap<EnumFacing, Integer>();
	public final HashMap<EnumFacing, Integer> input = new HashMap<EnumFacing, Integer>();

	public RedstoneExtenderTileEntity(World world) {
		this.world = world;
		init();
	}
	
	private void init() {
		for(EnumFacing face : EnumFacing.values()) {
			output_strong.put(face, true);
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
		//Every setRedstone function can have up to 3 parameters the first has to be the side which gets accessed, the second parameter should be the strength and the third parameter
		//is the option to set if the output is strong or not
	}
	
	public EnumFacing convertToEnumFacing(String side) {
		return EnumFacing.valueOf(side);
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		String side = ((String) arguments[0]);
		EnumFacing face = convertToEnumFacing(side);
		int strength = 0;
		boolean strong = true;
		switch(method) {
			case 0:
				getBlock().update(this);
				return new Object[] {(input.get(face) > 0) ? true : false};
			case 1:
				if(arguments.length > 1) {
					boolean on = ((Boolean) arguments[1]);
					strength = (on) ? 15 : 0;
				}
				if(arguments.length > 2) {
					strong = ((Boolean) arguments[2]);
				}
				output_strong.put(face, strong);
				output.put(face, strength);
				getBlock().update(this);
				return new Object[] {};
			case 2:
				getBlock().update(this);
				return new Object[] {input.get(face)};
			case 3:
				if(arguments.length > 1) {
					strength = ((Number) arguments[1]).intValue();
				}
				if(arguments.length > 2) {
					strong = ((Boolean) arguments[2]);
				}
				output_strong.put(face, strong);
				output.put(face, strength);
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
