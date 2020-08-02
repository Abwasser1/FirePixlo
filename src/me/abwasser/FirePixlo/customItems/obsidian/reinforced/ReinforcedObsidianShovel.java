package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.customItems.CustomItem;


public class ReinforcedObsidianShovel extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_shovel");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_SHOVEL, 200, "§rReinforced Obsidian Shovel", 1, getNamespacedKey().getKey());
		return is;
	}
	@Override
	public CustomItem getNewInstace() {
		return new ReinforcedObsidianShovel();
	}

	@Override
	public void registerRecipe() {
	
	}

	@Override
	public void onBreakBlock(BlockBreakEvent e) {
		if (e.getPlayer().getLocation().getPitch() > 45 || e.getPlayer().getLocation().getPitch() < -45) {
			for (int x = -1; x <= 1; x++)
				for (int z = -1; z <= 1; z++)
					breakBlock(e.getBlock().getRelative(x, 0, z), e.getPlayer().getInventory().getItemInMainHand());
		} else {
			if (e.getPlayer().getFacing() == BlockFace.NORTH || e.getPlayer().getFacing() == BlockFace.SOUTH) {
				for (int x = -1; x <= 1; x++)
					for (int y = -1; y <= 1; y++)
						breakBlock(e.getBlock().getRelative(x, y, 0), e.getPlayer().getInventory().getItemInMainHand());
			} else {
				for (int z = -1; z <= 1; z++)
					for (int y = -1; y <= 1; y++)
						breakBlock(e.getBlock().getRelative(0, y, z), e.getPlayer().getInventory().getItemInMainHand());
			}
		}
	}

	@Override
	public void onLeftClickBlock(PlayerInteractEvent e) {
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 3, 2, false, false, false));
		if (e.getPlayer().getLocation().getPitch() > 45 || e.getPlayer().getLocation().getPitch() < -45) {
			for (int x = -1; x <= 1; x++)
				for (int z = -1; z <= 1; z++)
					showBlock(e.getClickedBlock().getRelative(x, 0, z));
		} else {
			if (e.getPlayer().getFacing() == BlockFace.NORTH || e.getPlayer().getFacing() == BlockFace.SOUTH) {
				for (int x = -1; x <= 1; x++)
					for (int y = -1; y <= 1; y++)
						showBlock(e.getClickedBlock().getRelative(x, y, 0));
			} else {
				for (int z = -1; z <= 1; z++)
					for (int y = -1; y <= 1; y++)
						showBlock(e.getClickedBlock().getRelative(0, y, z));
			}
		}

	}

	@Override
	public void onRightClickBlock(PlayerInteractEvent e) {
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++) {
				paveBlock(e.getClickedBlock().getRelative(x, 0, z), e.getItem());
			}
	}

	@Override
	public void onRightClickOffhandBlock(PlayerInteractEvent e) {
		onRightClickBlock(e);
	}

	public void breakBlock(Block b, ItemStack tool) {
		List<Material> mineable = Arrays.asList(Material.GRASS_BLOCK, Material.SAND, Material.SOUL_SAND, Material.DIRT,
				Material.COARSE_DIRT, Material.PODZOL, Material.MYCELIUM, Material.CLAY, Material.GRAVEL, Material.GRASS_PATH);
		if (mineable.contains(b.getType())) {
			if (V.glowingBlocks.containsKey(b)) {
				V.glowingBlocks.get(b).setGlowing(false);
				V.glowingBlocks.get(b).remove();
			}
			if (tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
				b.setType(Material.AIR);
				b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(b.getType()));
				reduceDurability(1, tool);
			} else {
				b.breakNaturally();
				reduceDurability(1, tool);
			}
		}
	}

	public void showBlock(Block b) {
		List<Material> mineable = Arrays.asList(Material.GRASS_BLOCK, Material.SAND, Material.SOUL_SAND, Material.DIRT,
				Material.COARSE_DIRT, Material.PODZOL, Material.MYCELIUM, Material.CLAY, Material.GRAVEL, Material.GRASS_PATH);
		if (mineable.contains(b.getType())) {
			V.glowBlock(b, ChatColor.DARK_GRAY, 0.25f);
		}
	}

	public void paveBlock(Block b, ItemStack tool) {
		List<Material> mineable = Arrays.asList(Material.GRASS_BLOCK);
		if (mineable.contains(b.getType())) {
			b.setType(Material.GRASS_PATH);
			reduceDurability(1, tool);
		}
	}

}
