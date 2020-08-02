package me.abwasser.FirePixlo.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GSpinnerInterface {

	public void onselect(String value, GSpinner Gspin, Player p, InventoryClickEvent e);
	
}
