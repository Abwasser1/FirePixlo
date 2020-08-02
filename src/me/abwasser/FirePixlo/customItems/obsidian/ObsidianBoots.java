package me.abwasser.FirePixlo.customItems.obsidian;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ObsidianBoots extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "obsidian_boot");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_BOOTS, 100, "§rObsidian Boot", 1, getNamespacedKey().getKey());
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		return new ObsidianBoots();
	}

	@Override
	public void registerRecipe() {
		ShapedRecipe sr = new ShapedRecipe(getNamespacedKey(), getItem());
		sr.shape("   ", "o o", "o o");
		sr.setIngredient('o', Material.BRICK);
		Bukkit.addRecipe(sr);
		V.dev(ObsidianBoots.class, "registerRecipe()", "registered recipe §e" + sr.getKey(), Level.VERBOSE);
	}
}
