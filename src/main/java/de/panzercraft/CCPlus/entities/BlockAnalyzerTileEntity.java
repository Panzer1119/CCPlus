package de.panzercraft.CCPlus.entities;

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import de.panzercraft.CCPlus.CCPlus;
import de.panzercraft.CCPlus.blocks.BlockPos;
import de.panzercraft.CCPlus.blocks.BlockPosExact;
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
		return new String[] {"getBlock", "isBlockInRange", "getBlocks"}; 
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
					boolean inRange = isInRange(x, y, z);
					if(!inRange) {
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
			case 1:
				String block_unlocalizedName = ((String) arguments[0]);
				int block_range = ((Number) arguments[1]).intValue();
				boolean inRange = isInRange(block_range);
				if(!inRange) {
					return new Object[] {};
				}
				int minx = this.xCoord - block_range;
				int maxx = this.xCoord + block_range;
				int miny = this.yCoord - block_range;
				int maxy = this.yCoord + block_range;
				int minz = this.zCoord - block_range;
				int maxz = this.zCoord + block_range;
				BlockPos minpos = new BlockPos(minx, miny, minz);
				BlockPos maxpos = new BlockPos(maxx, maxy, maxz);
				boolean found = false;
				for(int x2 = minx; x2 <= maxx; x2++) {
					for(int y2 = miny; y2 <= maxy; y2++) {
						for(int z2 = minz; z2 <= maxz; z2++) {
							BlockPos pos = new BlockPos(x2, y2, z2);
							Block block = world.getBlock(pos.x, pos.y, pos.z);
							if(block != null) {
								if(block.getUnlocalizedName().equals(block_unlocalizedName)) {
									found = true;
								}
							}
							if(found) {
								break;
							}
						}
						if(found) {
							break;
						}
					}
					if(found) {
						break;
					}
				}
				return new Object[] {found};
			case 2:
				String block_unlocalizedName_2 = ((String) arguments[0]);
				int block_range_2 = ((Number) arguments[1]).intValue();
				boolean inRange_2 = isInRange(block_range_2);
				if(!inRange_2) {
					return new Object[] {};
				}
				int minx_2 = this.xCoord - block_range_2;
				int maxx_2 = this.xCoord + block_range_2;
				int miny_2 = this.yCoord - block_range_2;
				int maxy_2 = this.yCoord + block_range_2;
				int minz_2 = this.zCoord - block_range_2;
				int maxz_2 = this.zCoord + block_range_2;
				BlockPos minpos_2 = new BlockPos(minx_2, miny_2, minz_2);
				BlockPos maxpos_2 = new BlockPos(maxx_2, maxy_2, maxz_2);
				ArrayList<HashMap<String, Object>> positions = new ArrayList<HashMap<String, Object>>();
				for(int x2 = minx_2; x2 <= maxx_2; x2++) {
					for(int y2 = miny_2; y2 <= maxy_2; y2++) {
						for(int z2 = minz_2; z2 <= maxz_2; z2++) {
							BlockPos pos = new BlockPos(x2, y2, z2);
							Block block = world.getBlock(pos.x, pos.y, pos.z);
							if(block != null) {
								if(block.getUnlocalizedName().equals(block_unlocalizedName_2)) {
									HashMap<String, Object> temp = new HashMap<String, Object>();
									temp.put("x", pos.x);
									temp.put("y", pos.y);
									temp.put("z", pos.z);
									//temp.put("dim", world.provider.dimensionId);
									positions.add(temp);
								}
							}
						}
					}
				}
				HashMap<Integer, Object> positions_map = new HashMap<Integer, Object>();
				for(int i = 0; i < positions.size(); i++) {
					positions_map.put((i + 1), positions.get(i));
				}
				return new Object[] {positions_map};
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
		return isInRange(distance);
	}
	
	public boolean isInRange(double distance) {
		boolean inRange = ((CCPlus.block_analyzer_range != -1) ? distance <= CCPlus.block_analyzer_range : true);
		return inRange;
	}

}
