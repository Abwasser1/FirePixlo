package me.abwasser.FirePixlo.customItems.obsidian;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;


public class ObsidianPowder extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "obsidian_powder");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.FLOWER_BANNER_PATTERN, 100, "§rObsidian Powder", 1, getNamespacedKey().getKey());
		return is;
	}
	@Override
	public CustomItem getNewInstace() {
		return new ObsidianPowder();
	}

	@Override
	public void registerRecipe() {
		ShapedRecipe sr = new ShapedRecipe(getNamespacedKey(), getItem());
		sr.shape(" d ", "dod", " d ");
		sr.setIngredient('o', Material.OBSIDIAN);
		sr.setIngredient('d', Material.DIAMOND);
		Bukkit.addRecipe(sr);
	}

	
	

}
