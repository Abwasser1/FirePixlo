package me.abwasser.FirePixlo.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickListener implements GUIListener{

	@Override
	public void onClick(Player p, Inventory inv, ItemStack is, int slot, String id, InventoryClickEvent e, GUI gui) {
		if(id.equalsIgnoreCase("closeBTN")) {
			p.closeInventory();
		}
		if(id.equalsIgnoreCase("glow")) {
			p.setGlowing(!p.isGlowing());
		}
		
	}

}
