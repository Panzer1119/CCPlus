package de.panzercraft.CCPlus.blocks;

import java.time.Instant;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class BlockPosExact {

    public double x = -1;
    public double y = -1;
    public double z = -1;
    public int dimension = 0;
    public Instant instant = Instant.now();

    public BlockPosExact(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public BlockPosExact(TileEntity te) {
        this(te.xCoord, te.yCoord, te.zCoord);
    }
    
    public BlockPosExact(String coords, String delimiter) {
    	this(Double.parseDouble(coords.split(delimiter)[0].replaceAll(",", ".")), Double.parseDouble(coords.split(delimiter)[1].replaceAll(",", ".")), Double.parseDouble(coords.split(delimiter)[2].replaceAll(",", ".")));
    	if(coords.split(delimiter).length > 3) {
    		dimension = Integer.parseInt(coords.split(delimiter)[3]);
    	}
    }
    
    public double getX() {
    	return x;
    }
    
    public double getY() {
    	return y;
    }
    
    public double getZ() {
    	return z;
    }
    
    public int getXint() {
    	return (int) x;
    }
    
    public int getYint() {
    	return (int) y;
    }
    
    public int getZint() {
    	return (int) z;
    }
    
    public BlockPosExact add(double x, double y, double z) {
        return x == 0 && y == 0 && z == 0 ? this : new BlockPosExact(this.getX() + x, this.getY() + y, this.getZ() + z);
    }
    
    public BlockPosExact offset(EnumFacing dir) {
        return new BlockPosExact(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ());
    }
    
    public boolean equals(BlockPosExact other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
    
    public BlockPos toBlockPos() {
    	BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
    	return pos;
    }
    
    @Override
    public String toString() {
        return String.format("%.14f;%.14f;%.14f;%d", x, y, z, dimension);
    }

}
