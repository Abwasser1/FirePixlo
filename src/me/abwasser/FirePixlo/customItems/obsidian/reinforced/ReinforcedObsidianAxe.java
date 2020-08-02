package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ReinforcedObsidianAxe extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_axe");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_AXE, 200, "§rReinforced Obsidian Axe", 1, getNamespacedKey().getKey());
		V.dev(ReinforcedObsidianAxe.class, "getItem()", "Returned Item: §e" + is.toString(), Level.VERBOSE);
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		V.dev(ReinforcedObsidianAxe.class, "getNewInstace()", "Returned New Instance", Level.VERBOSE);
		return new ReinforcedObsidianAxe();
	}

	@Override
	public void registerRecipe() {
		
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
