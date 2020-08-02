package me.abwasser.FirePixlo.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;


public class Listeners implements Listener {
	

	@EventHandler
	public void guiEvent(InventoryClickEvent e) {
		try {
			Inventory inventory = e.getClickedInventory();
			ItemStack clicked = e.getCurrentItem();
			Player p = (Player) e.getWhoClicked();
			if (e.getView().getTitle().startsWith(Main.pr)) {
				e.setCancelled(true);
				List<String> lore = clicked.getItemMeta().getLore();
				if (lore == null)
					return;
				GUI.fire(e, clicked, p);

			}
		} catch (Exception e1) {
		}
	}

}
