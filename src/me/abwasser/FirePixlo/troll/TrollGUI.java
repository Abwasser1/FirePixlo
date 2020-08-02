package me.abwasser.FirePixlo.troll;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.gui.GUI;
import me.abwasser.FirePixlo.gui.GUIListener;
import me.abwasser.FirePixlo.gui.Var;

public class TrollGUI {

	public static GUI getGUI() {
		GUI gui = new GUI(6, "§cTroll§7-§aMaster §7~ §3Abwasser");
		gui.setItem(0, Var.itemBakery(Material.GLASS, "§3§lKnick in der Optik", 1, (short)0, ""), "", new GUIListener() {
			
			@Override
			public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID, InventoryClickEvent e,
					GUI gui) {
								
			}
		});
		return gui;
	}
	
}
