package de.panzercraft.CCPlus.generator;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGeneratorCCPlus implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.dimensionId) {
			case -1: generateNether(  world, random, chunkX * 16, chunkZ * 16); break;
			case  0: generateSurface( world, random, chunkX * 16, chunkZ * 16); break;
			case  1: generateEnd(     world, random, chunkX * 16, chunkZ * 16); break;
			default: generateOther(   world, random, chunkX * 16, chunkZ * 16); break;
		}
	}

	private void generateNether(World world, Random random, int x, int z) {
		
	}
	
	private void generateSurface(World world, Random random, int x, int z) {
		//generateOre(Blocks.glass, world, random, x, z, 16, 16, 6 + random.nextInt(4), 15, 10, 128); //Example
	}
	
	private void generateEnd(World world, Random random, int x, int z) {
		
	}
	
	private void generateOther(World world, Random random, int i, int j) {
		
	}
	
	public void generateOre(Block block, World world, Random random, int posX, int posZ, int maxX, int maxZ, int maxLength, int maxCount, int minY, int maxY) {
		int deltaY = maxY - minY;
		for(int i = 0; i < maxCount; i++) {
			int positionX = posX + random.nextInt(maxX);
			int positionY = minY + random.nextInt(deltaY);
			int positionZ = posZ + random.nextInt(maxZ);
			new WorldGenMinable(block, maxLength).generate(world, random, positionX, positionY, positionZ);
		}
	}

}
