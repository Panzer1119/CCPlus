package de.panzercraft.CCPlus.entities;

import java.util.HashMap;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.panzercraft.CCPlus.CCPlus;
import de.panzercraft.CCPlus.utils.MathPlus;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BlockAnalyzerTileEntity extends TileEntity implements IPeripheral {
	
	private World world;
	
	public BlockAnalyzerTileEntity(World world) {
		this.world = world;
	}

	@Override
	public String getType() {
		return "block_analyzer";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getBlock"}; 
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch(method) {
			case 0:
				HashMap<String, Object> data = new HashMap<String, Object>();
				int x = ((Number) arguments[0]).intValue();
				int y = ((Number) arguments[1]).intValue();
				int z = ((Number) arguments[2]).intValue();
				World world_2 = world;
				int dim = world.provider.dimensionId;
				if(arguments.length > 3) {
					dim = ((Number) arguments[3]).intValue();
					world_2 = MinecraftServer.getServer().worldServerForDimension(dim);
				}
				if(CCPlus.block_analyzer_enable_dimensional_analysis || dim == world.provider.dimensionId) {
					if(!isInRange(x, y, z)) {
						return new Object[] {"Out of range"};
					} else {
						if(CCPlus.block_analyzer_enable_dimensional_analysis && dim != world.provider.dimensionId && world_2 == null) {
							return new Object[] {"Dimension does not exist"};
						}
						Block block = world_2.getBlock(x, y, z);
						if(block != null) {
							data.put("unlocalizedName", block.getUnlocalizedName());
							data.put("localizedName", block.getLocalizedName());
							data.put("id", block.getIdFromBlock(block));
							data.put("name", new ItemStack(block, 1).getDisplayName());
						} else {
							data.clear();
						}
					}
				} else {
					return new Object[] {"Multidimensional block analysis isnt supported"};
				}
				return new Object[] {data};
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
	
	public boolean isInRange(int x, int y, int z) {
		double distance = MathPlus.distanceXYZ(this, x, y, z);
		return !(CCPlus.block_analyzer_range != -1 && distance > CCPlus.block_analyzer_range);
	}

}
