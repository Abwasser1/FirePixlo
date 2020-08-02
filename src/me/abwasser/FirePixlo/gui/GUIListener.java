package me.abwasser.FirePixlo.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface GUIListener {

	void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID, InventoryClickEvent e, GUI gui);
	
}
