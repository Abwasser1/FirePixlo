package me.abwasser.FirePixlo.customItems.other;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class OreFinder extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "ore_finder");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.STICK, 1, "§rOre Finder", 1, getNamespacedKey().getKey());
		return is;
	}
	@Override
	public CustomItem getNewInstace() {
		return new OreFinder();
	}
	@Override
	public void registerRecipe() {
		ShapedRecipe sr = new ShapedRecipe(getNamespacedKey(), getItem());
		sr.shape("ded", "ese", "ded");
		sr.setIngredient('d', Material.DIAMOND);
		sr.setIngredient('e', Material.EMERALD);
		sr.setIngredient('s', Material.NETHER_STAR);
		Bukkit.addRecipe(sr);
	}

	@Override
	public void onRightClickBlock(PlayerInteractEvent e) {
		look(e.getClickedBlock(), e.getBlockFace(), 10);
	}

	@Override
	public void onRightClickOffhandBlock(PlayerInteractEvent e) {
		onRightClickBlock(e);
	}

	public void look(Block b, BlockFace bf, int index) {
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
		default:
			break;
		}
		if(chatColor !=  null) {
			V.glowBlock(b, chatColor, 10);
		}
		Block t = b.getRelative(bf.getOppositeFace());
		index--;
		if(index <= 0) {
			return;
		}
		look(t, bf, index);
	}

}
