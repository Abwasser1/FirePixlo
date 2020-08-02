package me.abwasser.FirePixlo.customItems;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CraftBlocker {

	public static ArrayList<Material> blocked = new ArrayList<Material>();

	public static void init() {
		blocked.add(Material.FLOWER_BANNER_PATTERN);
	}

	public static void onCraft(CraftItemEvent e) {
		Player p = (Player) e.getWhoClicked();
		p.sendMessage("CRAFTING");
		Recipe recipe = e.getRecipe();
		if (recipe.getResult().getItemMeta().hasDisplayName()) {
			if (recipe.getResult().getItemMeta().getDisplayName().startsWith("§r")) {
				for (int i = 1; i <= 9; i++)
					if (e.getInventory().getItem(i) != null)
						if (blocked.contains(e.getInventory().getItem(i).getType())) {
							if (e.getInventory().getItem(i).getItemMeta().getLore() == null) {
								e.setCurrentItem(new ItemStack(Material.DIRT));
								p.sendMessage("Denied");
							} else
								p.sendMessage("1");
						} else
							p.sendMessage("2");
			} else
				p.sendMessage("3");
		} else
			p.sendMessage("4");
	}

	public static void onSmelt(FurnaceSmeltEvent e) {
		Bukkit.broadcastMessage("SMELTING");
		if (e.getResult().getItemMeta().hasDisplayName()) {
			if (e.getResult().getItemMeta().getDisplayName().startsWith("§r")) {
				if (blocked.contains(e.getSource().getType())) {
					if (e.getSource().getItemMeta().getLore() == null) {
						e.setResult(new ItemStack(e.getSource().getType()));
						Bukkit.broadcastMessage("Denied");
					} else
						Bukkit.broadcastMessage("1");
				} else
					Bukkit.broadcastMessage("2");
			} else
				Bukkit.broadcastMessage("3");
		} else
			Bukkit.broadcastMessage("4");
	}

}
