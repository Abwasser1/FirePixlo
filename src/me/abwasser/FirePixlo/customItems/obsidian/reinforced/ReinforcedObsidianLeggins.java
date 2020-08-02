package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ReinforcedObsidianLeggins extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_leggins");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_LEGGINGS, 200, "§rReinforced Obsidian Leggins", 1, getNamespacedKey().getKey());
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		return new ReinforcedObsidianLeggins();
	}

	@Override
	public void registerRecipe() {
	}
}
