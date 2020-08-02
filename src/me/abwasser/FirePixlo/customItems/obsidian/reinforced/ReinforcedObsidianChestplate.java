package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ReinforcedObsidianChestplate extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_chestplate");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_CHESTPLATE, 200, "§rReinforced Obsidian Chestplate", 1, getNamespacedKey().getKey());
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		return new ReinforcedObsidianChestplate();
	}

	@Override
	public void registerRecipe() {
	}
}
