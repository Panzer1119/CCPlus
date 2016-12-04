package de.panzercraft.CCPlus.Handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import de.panzercraft.CCPlus.CCPlus;
import net.minecraft.item.ItemStack;

public class CraftingHandler {
	
	@SubscribeEvent
	public void itemCrafted(ItemCraftedEvent event) {
		if(event.crafting.getItem().equals(new ItemStack(CCPlus.playerdetectorplusinstance, 1).getItem())) {
			event.player.addStat(CCPlus.achievement_craftPDP, 1);
		}
	}

}
