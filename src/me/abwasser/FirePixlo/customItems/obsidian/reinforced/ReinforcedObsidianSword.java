package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ReinforcedObsidianSword extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_sword");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_SWORD, 200, "§rReinforced Obsidian Sword", 1,
				getNamespacedKey().getKey(), "#" + getNewInstace().id);
		V.dev(ReinforcedObsidianSword.class, "getItem()", "Returned Item: §e" + is.toString(), Level.VERBOSE);
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		V.dev(ReinforcedObsidianSword.class, "getNewInstace()", "Returned New Instance", Level.VERBOSE);
		return new ReinforcedObsidianSword();
	}

	@Override
	public void registerRecipe() {

	}

	@Override
	public void onAttack(EntityDamageByEntityEvent e) {
		((Player) e.getDamager()).resetCooldown();
	}

}
