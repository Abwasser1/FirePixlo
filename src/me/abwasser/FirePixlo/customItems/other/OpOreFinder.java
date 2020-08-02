package me.abwasser.FirePixlo.customItems.other;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class OpOreFinder extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "op_ore_finder");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.STICK, 1, "§rOP Ore Finder", 1, getNamespacedKey().getKey());
		return is;
	}
	@Override
	public CustomItem getNewInstace() {
		return new OpOreFinder();
	}

	@Override
	public void registerRecipe() {
	}

	@Override
	public void onRightClickBlock(PlayerInteractEvent e) {
		for (int x = -25; x <= 25; x++)
			for (int y = -25; y <= 25; y++)
				for (int z = -25; z <= 25; z++) {
					look(e.getClickedBlock().getRelative(x, y, z));
				}
	}

	@Override
	public void onRightClickOffhandBlock(PlayerInteractEvent e) {
		onRightClickBlock(e);
	}

	public void look(Block b) {
		if(V.glowingBlocks.containsKey(b)) {
			V.glowingBlocks.get(b).setGlowing(false);
			V.glowingBlocks.get(b).remove();
		}
		ChatColor chatColor = null;
		switch (b.getType()) {
		case DIAMOND_ORE:
			chatColor = ChatColor.AQUA;
			break;
		case GOLD_ORE:
			chatColor = ChatColor.GOLD;
			break;
		case LAPIS_ORE:
			chatColor = ChatColor.BLUE;
			break;
		case REDSTONE_ORE:
			chatColor = ChatColor.RED;
			break;
		case IRON_ORE:
			chatColor = ChatColor.GRAY;
			break;
		case COAL_ORE:
			chatColor = ChatColor.BLACK;
			break;
		case EMERALD_ORE:
			chatColor = ChatColor.GREEN;
			break;
		default:
			break;
		}
		if (chatColor != null) {
			V.glowBlock(b, chatColor, 10);
		}
	}

}
