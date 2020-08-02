package me.abwasser.FirePixlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemListener implements Listener {

	public static HashMap<String, Callback> map = new HashMap<>();

	public static ItemStack markItemStack(ItemStack is, Callback callback) {
		ItemMeta im = is.getItemMeta();
		List<String> list = im.getLore();
		if (list == null)
			list = new ArrayList<>();
		ArrayList<String> list1 = new ArrayList<>();
		if (list.size() > 0)
			for (String str : list)
				if (str.startsWith("#"))
					list1.add(str);
		for (String str : list1)
			list.remove(str);
		String id = Token.generateToken();
		list.add("#" + id);
		im.setLore(list);
		is.setItemMeta(im);
		map.put(id, callback);
		return is;
	}

	public static ItemStack markItemStack(ItemStack is, String id, Callback callback) {
		ItemMeta im = is.getItemMeta();
		List<String> list = im.getLore();
		if (list == null)
			list = new ArrayList<>();
		ArrayList<String> list1 = new ArrayList<>();
		if (list.size() > 0)
			for (String str : list)
				if (str.startsWith("#"))
					list1.add(str);
		for (String str : list1)
			list.remove(str);
		list.add("#" + id);
		im.setLore(list);
		is.setItemMeta(im);
		map.put(id, callback);
		return is;
	}

	public static ItemStack markItemStack(ItemStack is, String id) {
		ItemMeta im = is.getItemMeta();
		List<String> list = im.getLore();
		if (list == null)
			list = new ArrayList<>();
		list.add("#" + id);
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}

	@EventHandler
	public void click(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		try {
			ItemStack is = p.getInventory().getItemInMainHand();
			List<String> lore = is.getItemMeta().getLore();
			for (String line : lore)
				if (line.startsWith("#")) {
					if (map.keySet().contains(line.replace("#", ""))) {
						map.get(line.replace("#", "")).call(e);
					}
				}

		} catch (Exception e1) {
		}
	}

	@EventHandler
	public void click(InventoryClickEvent e) {
		try {
			ItemStack is = e.getCurrentItem();
			List<String> lore = is.getItemMeta().getLore();
			for (String line : lore)
				if (line.startsWith("#")) {
					if (map.keySet().contains(line.replace("#", ""))) {
						map.get(line.replace("#", "")).call(e);
					}
				}

		} catch (Exception e1) {
		}
	}

	@EventHandler
	public void click(PlayerDropItemEvent e) {
		try {
			ItemStack is = e.getItemDrop().getItemStack();
			List<String> lore = is.getItemMeta().getLore();
			for (String line : lore)
				if (line.startsWith("#")) {
					if (map.keySet().contains(line.replace("#", ""))) {
						map.get(line.replace("#", "")).call(e);
					}
				}

		} catch (Exception e1) {
		}
	}

	@EventHandler
	public void click(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		try {
			ItemStack is = e.getOffHandItem();
			List<String> lore = is.getItemMeta().getLore();
			for (String line : lore)
				if (line.startsWith("#")) {
					if (map.keySet().contains(line.replace("#", ""))) {
						map.get(line.replace("#", "")).call(e);
					}
				}

		} catch (Exception e1) {
		}
	}

	public static abstract class Callback {
		public void call(PlayerInteractEvent e) {
		}

		public void call(InventoryClickEvent e) {
		}

		public void call(PlayerDropItemEvent e) {
		}

		public void call(PlayerSwapHandItemsEvent e) {
		}

	}

}
