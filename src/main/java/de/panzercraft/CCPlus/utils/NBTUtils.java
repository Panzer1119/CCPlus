package de.panzercraft.CCPlus.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils {
	
    public static NBTTagCompound getItemTag(ItemStack stack) {
        initNBTTagCompound(stack);
        return stack.stackTagCompound;
    }

    public static boolean hasTag(ItemStack stack, String key) {
        return stack != null && stack.stackTagCompound != null && stack.stackTagCompound.hasKey(key);
    }

    public static ItemStack removeTag(ItemStack stack, String key) {
        if (stack.stackTagCompound != null) {
            stack.stackTagCompound.removeTag(key);
        }
        return stack;
    }

    public static ItemStack setString(ItemStack stack, String key, String value) {
        initNBTTagCompound(stack);
        stack.stackTagCompound.setString(key, value);
        return stack;
    }

    public static String getString(ItemStack stack, String key) {
        initNBTTagCompound(stack);
        if (!hasTag(stack, key)) {
            setString(stack, key, "");
        }
        return stack.stackTagCompound.getString(key);
    }

    public static ItemStack setInteger(ItemStack stack, String key, int value) {
        initNBTTagCompound(stack);
        stack.stackTagCompound.setInteger(key, value);
        return stack;
    }

    public static int getInteger(ItemStack stack, String key) {
        initNBTTagCompound(stack);
        if (!hasTag(stack, key)) {
            setInteger(stack, key, 0);
        }
        return stack.stackTagCompound.getInteger(key);
    }

    public static ItemStack setFloat(ItemStack stack, String key, float value) {
        initNBTTagCompound(stack);
        stack.stackTagCompound.setFloat(key, value);
        return stack;
    }

    public static float getFloat(ItemStack stack, String key) {
        initNBTTagCompound(stack);
        if (!hasTag(stack, key)) {
            setFloat(stack, key, 0);
        }
        return stack.stackTagCompound.getFloat(key);
    }

    private static void initNBTTagCompound(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

}
