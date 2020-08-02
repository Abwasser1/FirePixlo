package me.abwasser.FirePixlo.customItems.obsidian.scythe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ObsidianScythe extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		V.dev(ObsidianScythe.class, "getNamespacedKey()",
				"Returned NamespacedKey: §e" + new NamespacedKey(Main.getInstance(), "obsidian_axe"), Level.VERBOSE);
		return new NamespacedKey(Main.getInstance(), "obsidian_scythe");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_SWORD, 110, "§rObsidian Scythe", 1, getNamespacedKey().getKey(),
				"#" + getNewInstace().id);
		V.dev(ObsidianScythe.class, "getItem()", "Returned Item: §e" + is.toString(), Level.VERBOSE);
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		V.dev(ObsidianScythe.class, "getNewInstace()", "Returned New Instance", Level.VERBOSE);
		return new ObsidianScythe();
	}

	@Override
	public void registerRecipe() {
		ShapedRecipe sr = new ShapedRecipe(getNamespacedKey(), getItem());
		sr.shape(" oo", " b ", " b ");
		sr.setIngredient('o', Material.BRICK);
		sr.setIngredient('b', Material.BLAZE_ROD);
		Bukkit.addRecipe(sr);
		V.dev(ObsidianScythe.class, "registerRecipe()", "registered recipe §e" + sr.getKey(), Level.VERBOSE);
	}

	@Override
	public void onKill(EntityDeathEvent e) {
		Player p = e.getEntity().getKiller();
		String id = getId(p.getInventory().getItemInMainHand());
		p.sendMessage("Killed: " + e.getEntity().getType() + " : " + (e.getEntity() instanceof Monster));
		if (e.getEntity() instanceof Player) {
			add(id, "killed.players");
		} else {
			if (e.getEntity() instanceof Monster) {
				add(id, "killed.monsters");
			} else {
				add(id, "killed.friendly");
			}
		}
		int players = readInt(id, "killed.players");
		int monsters = readInt(id, "killed.monsters");
		int friendly = readInt(id, "killed.friendly");

		int currentlvl = readInt(id, "level");
		if (currentlvl == 0) {
			if (monsters >= 25) {
				currentlvl = 1;
				ItemStack is = p.getInventory().getItemInMainHand();
				ItemMeta im = is.getItemMeta();
				im.setCustomModelData(111);
				is.setItemMeta(im);
				p.getInventory().setItemInMainHand(is);
				V.chat(p, "§cObsidian§3Scythe", "§3Your Scythe has been updated to Level 1!");
				V.chat(p, "§cObsidian§3Scythe", "§3Now you can hit §c2 times per second§3!");

			}
		}
		if (currentlvl == 1) {
			if ((monsters + friendly) >= 150) {
				currentlvl = 2;
				ItemStack is = p.getInventory().getItemInMainHand();
				ItemMeta im = is.getItemMeta();
				im.setCustomModelData(112);
				is.setItemMeta(im);
				p.getInventory().setItemInMainHand(is);
				V.chat(p, "§cObsidian§3Scythe", "§3Your Scythe has been updated to Level 2!");
				V.chat(p, "§cObsidian§3Scythe", "§3Now you get §cspeed 1 §3whenever your scythe is in your hand!");
			}
		}
		if (currentlvl == 2) {
			if (players >= 10) {
				currentlvl = 3;
				ItemStack is = p.getInventory().getItemInMainHand();
				ItemMeta im = is.getItemMeta();
				im.setCustomModelData(113);
				is.setItemMeta(im);
				p.getInventory().setItemInMainHand(is);
				V.chat(p, "§cObsidian§3Scythe", "§3Your Scythe has been updated to Level 3!");
				V.chat(p, "§cObsidian§3Scythe", "§3Now you get §cspeed 2§3 whenever your scythe is in your hand!");
			}
		}
		if (currentlvl == 3) {
			if (monsters >= 300) {
				currentlvl = 4;
				ItemStack is = p.getInventory().getItemInMainHand();
				ItemMeta im = is.getItemMeta();
				im.setCustomModelData(114);
				is.setItemMeta(im);
				p.getInventory().setItemInMainHand(is);
				V.chat(p, "§cObsidian§3Scythe", "§3Your Scythe has been updated to Level 4!");
				V.chat(p, "§cObsidian§3Scythe", "§3Now you get §cstrength 1§3 whenever your scythe is in your hand!");
			}
		}
		if (currentlvl == 4) {
			if ((monsters + friendly) >= 1000) {
				currentlvl = 5;
				ItemStack is = p.getInventory().getItemInMainHand();
				ItemMeta im = is.getItemMeta();
				im.setCustomModelData(115);
				is.setItemMeta(im);
				p.getInventory().setItemInMainHand(is);
				V.chat(p, "§cObsidian§3Scythe", "§3Your Scythe has been updated to Level 5!");
				V.chat(p, "§cObsidian§3Scythe",
						"§3Now you get §cstrength 2 §3and §cspeed 3§3 whenever your scythe is in your hand!");
			}
		}
		writeData(id, "level", currentlvl);
		currentLevel = currentlvl;
	}

	public void add(String id, String path) {
		if (readData(id, path) == null)
			writeData(id, path, 0);
		int i = (int) readData(id, path);
		i++;
		writeData(id, path, i);
	}

	public int readInt(String id, String path) {
		if (readData(id, path) == null)
			writeData(id, path, 0);
		return (int) readData(id, path);
	}

	int currentLevel = -1;
	boolean isLoaded = false;

	@Override
	public void onMove(PlayerMoveEvent e) {
		if (!isLoaded) {
			currentLevel = readInt(getId(e.getPlayer().getInventory().getItemInMainHand()), "level");
			isLoaded = true;
		}
		effect(e.getPlayer(), currentLevel);
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e) {
		if (!isLoaded) {
			currentLevel = readInt(getId(((Player)e.getDamager()).getInventory().getItemInMainHand()), "level");
			isLoaded = true;
		}
		attackSpeed(((Player)e.getDamager()), currentLevel);
	}
	
	@Override
	public void onUnselect(PlayerItemHeldEvent e) {
		e.getPlayer().setCooldown(getItem().getType(), 13);
	}

	@Override
	public void onDrop(PlayerDropItemEvent e) {
		e.getPlayer().setCooldown(getItem().getType(), 13);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e) {
		e.getWhoClicked().setCooldown(getItem().getType(), 13);
	}

	public void attackSpeed(Player p, int level) {
		if (level > 0 && level < 4)
			p.setCooldown(getItem().getType(), 10);
		else if (level == 5)
			p.setCooldown(getItem().getType(), 6);
		else
			p.setCooldown(getItem().getType(), 13);

	}

	public void effect(Player p, int level) {

	}

}
