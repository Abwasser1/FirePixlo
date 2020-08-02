package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ReinforcedObsidianBoots extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_boot");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_BOOTS, 200, "§rReinforced Obsidian Boot", 1, getNamespacedKey().getKey());
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		return new ReinforcedObsidianBoots();
	}

	@Override
	public void registerRecipe() {
	}
}
