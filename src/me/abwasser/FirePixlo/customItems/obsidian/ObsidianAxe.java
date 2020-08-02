package me.abwasser.FirePixlo.customItems.obsidian;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ObsidianAxe extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		V.dev(ObsidianAxe.class, "getNamespacedKey()", "Returned NamespacedKey: §e" + new NamespacedKey(Main.getInstance(), "obsidian_axe"),
				Level.VERBOSE);
		return new NamespacedKey(Main.getInstance(), "obsidian_axe");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_AXE, 100, "§rObsidian Axe", 1, getNamespacedKey().getKey());
		V.dev(ObsidianAxe.class, "getItem()", "Returned Item: §e" + is.toString(), Level.VERBOSE);
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		V.dev(ObsidianAxe.class, "getNewInstace()", "Returned New Instance", Level.VERBOSE);
		return new ObsidianAxe();
	}

	@Override
	public void registerRecipe() {
		ShapedRecipe sr = new ShapedRecipe(getNamespacedKey(), getItem());
		sr.shape(" oo", " bo", " b ");
		sr.setIngredient('o', Material.BRICK);
		sr.setIngredient('b', Material.BLAZE_ROD);
		Bukkit.addRecipe(sr);
		V.dev(ObsidianAxe.class, "registerRecipe()", "registered recipe §e" + sr.getKey(), Level.VERBOSE);
	}

	@Override
	public void onBreakBlock(BlockBreakEvent e) {
		Material type = e.getBlock().getType();
		ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
		if (type == Material.ACACIA_LOG || type == Material.BIRCH_LOG || type == Material.DARK_OAK_LOG
				|| type == Material.JUNGLE_LOG || type == Material.OAK_LOG || type == Material.SPRUCE_LOG) {
			e.setCancelled(true);
			new Thread(new Runnable() {

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					V.dev(this.getClass(), "run()", "Running Blockbreaker in new Thread#" + this.hashCode(), Level.VERBOSE);
					double key = Math.random();
					mined.put(key, new ArrayList<>());
					a(e.getBlock(), type, tool, key);
					if (tool.getDurability() > 1561) {
						new BukkitRunnable() {

							@Override
							public void run() {
								tool.setAmount(0);
							}
						}.runTask(Main.getInstance());
					}
					V.dev(this.getClass(), "run()", "Finished Blockbreaker in new Thread#" + this.hashCode(), Level.VERBOSE);
				}
			}).start();
		}
	}

	@Override
	public void onLeftClickBlock(PlayerInteractEvent e) {
		Material type = e.getClickedBlock().getType();
		if (type == Material.ACACIA_LOG || type == Material.BIRCH_LOG || type == Material.DARK_OAK_LOG
				|| type == Material.JUNGLE_LOG || type == Material.OAK_LOG || type == Material.SPRUCE_LOG) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 3, 2, false, false, false));
		}
	}

	HashMap<Double, ArrayList<Block>> mined = new HashMap<>();

	@SuppressWarnings("deprecation")
	private void a(Block b, Material type, ItemStack tool, double key) {
		V.dev(this.getClass(), "a()", "Called recursive blockbreaking methode", Level.VERBOSE);
		if (b.getType() != type) {
			return;
		}
		if (tool.getDurability() > 1561) {
			return;
		}
		if (!mined.get(key).contains(b)) {
			reduceDurability(1, tool);
			ArrayList<Block> temp = mined.get(key);
			temp.add(b);
			mined.put(key, temp);
		}
		new BukkitRunnable() {

			@Override
			public void run() {
				b.breakNaturally();
				V.dev(this.getClass(), "run()", "destroyed block "+b.getType(), Level.VERBOSE);
			}
		}.runTask(Main.getInstance());
		for (int x = -1; x <= 1; x++)
			for (int y = 1; y >= -1; y--)
				for (int z = -1; z <= 1; z++)
					if (!(x == 0 && y == 0 && z == 0)) {
						Block t = b.getRelative(x, y, z);
						if (t.getType() == type)
							new Thread(new Runnable() {

								@Override
								public void run() {
									a(t, type, tool, key);
								}
							}).start();
					}

		return;
	}

}
