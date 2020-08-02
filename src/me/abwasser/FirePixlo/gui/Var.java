package me.abwasser.FirePixlo.gui;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Var {

	public static HashMap<Inventory, GUI> invToGUI = new HashMap<>();

	@SuppressWarnings("deprecation")
	public static ItemStack itemBakery(Material mat, String displayname, int amount, short durability, String... lore) {
		ItemStack is = new ItemStack(mat);
		is.setAmount(amount);
		is.setDurability(durability);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(displayname);
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);

		return is;
	}
	
	public static ItemStack renameItem(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack skullBakery(String itemName, String playername) {
		ItemStack skull = itemBakery(Material.PLAYER_HEAD, itemName, 1, (short) 0, "");
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(playername);
		skull.setItemMeta(meta);
		return skull;
	}
	
	public static ItemStack modifyItemStack(ItemStack is, int cmd) {
		ItemMeta im = is.getItemMeta();
		im.setCustomModelData(cmd);
		is.setItemMeta(im);
		return is;
	}
	
}
