package me.abwasser.FirePixlo.customItems.obsidian;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;


public class ObsidianIngot extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "obsidian_ingot");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.FLOWER_BANNER_PATTERN, 102, "§rObsidian Ingot", 1, getNamespacedKey().getKey());
		return is;
	}
	@Override
	public CustomItem getNewInstace() {
		return new ObsidianIngot();
	}

	@Override
	public void registerRecipe() {
		FurnaceRecipe recipe = new FurnaceRecipe(getNamespacedKey(), getItem(), Material.GLOWSTONE_DUST, 20, 1);
		Bukkit.addRecipe(recipe);
	}

	
	

}
