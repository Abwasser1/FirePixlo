package me.abwasser.FirePixlo.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GTextInputInterface {

	public void onenter(String value, GTextInput Gspin, Player p, InventoryClickEvent e);
	
}
