package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ReinforcedObsidianHelmet extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_helmet");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_HELMET, 200, "§rReinforced Obsidian Helmet", 1,
				getNamespacedKey().getKey());
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		return new ReinforcedObsidianHelmet();
	}

	@Override
	public void registerRecipe() {
	}
}
