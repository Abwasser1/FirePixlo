package me.abwasser.FirePixlo.customItems.obsidian;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ObsidianSword extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		V.dev(ObsidianSword.class, "getNamespacedKey()",
				"Returned NamespacedKey: §e" + new NamespacedKey(Main.getInstance(), "obsidian_sword"), Level.VERBOSE);
		return new NamespacedKey(Main.getInstance(), "obsidian_sword");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_SWORD, 100, "§rObsidian Sword", 1, getNamespacedKey().getKey());
		V.dev(ObsidianSword.class, "getItem()", "Returned Item: §e" + is.toString(), Level.VERBOSE);
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		V.dev(ObsidianSword.class, "getNewInstace()", "Returned New Instance", Level.VERBOSE);
		return new ObsidianSword();
	}

	@Override
	public void registerRecipe() {
		ShapedRecipe sr = new ShapedRecipe(getNamespacedKey(), getItem());
		sr.shape(" o ", " o ", " b ");
		sr.setIngredient('o', Material.BRICK);
		sr.setIngredient('b', Material.BLAZE_ROD);
		Bukkit.addRecipe(sr);
		V.dev(ObsidianSword.class, "registerRecipe()", "registered recipe §e" + sr.getKey(), Level.VERBOSE);
	}

	@Override
	public void onAttack(EntityDamageByEntityEvent e) {
		((Player)e.getDamager()).resetCooldown();
	}

}
