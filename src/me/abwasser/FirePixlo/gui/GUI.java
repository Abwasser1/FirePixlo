 package me.abwasser.FirePixlo.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.abwasser.FirePixlo.Main;


public class GUI {

	public int rows;
	public String title;
	public Inventory inv;
	public static HashMap<String, String> tokenToID = new HashMap<String, String>();
	public static HashMap<String, GUIListener> tokenToListener = new HashMap<String, GUIListener>();

	public GUI(int rows, String title) {
		this.rows = rows;
		this.title = title;
		inv = Bukkit.createInventory(null, getSize(), Main.pr + "§7> §r" + title);
		Var.invToGUI.put(getInv(), this);
	}
	public GUI(int rows, String title, int randomIntEgalWasDuEingibst) {
		this.rows = rows;
		this.title = title;
		inv = Bukkit.createInventory(null, getSize(), title);
		Var.invToGUI.put(getInv(), this);
	}

	public Inventory getInv() {
		return inv;
	}

	public int getSize() {
		return rows * 9;
	}

	public void open(Player p) {
		p.openInventory(getInv());
	}

	public void fill(ItemStack is) {
		for (int i = 0; i < getSize(); i++)
			inv.setItem(i, is);
	}

	public void backgroundFill(ItemStack is) {
		for (int i = 0; i < getSize(); i++)
			if (inv.getItem(i) == null)
				inv.setItem(i, is);
	}

	public void backgroundFill() {
		backgroundFill(Var.itemBakery(Material.BLACK_STAINED_GLASS_PANE, " ", 1, (short) 0, ""));
	}

	public void setItem(int slot, ItemStack is) {
		inv.setItem(slot, is);
	}

	public void setItem(int slot, ItemStack is, String customItemID, GUIListener callback) {
		String token = Token.generateToken();
		tokenToID.put(token, customItemID);
		tokenToListener.put(token, callback);

		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		if (lore == null)
			lore = Arrays.asList();
		if (lore.size() >= 1)
			if (lore.get(lore.size() - 1).startsWith("#")) {
				lore.remove(lore.size() - 1);

			}

		if (lore.size() >= 3)
			if (lore.get(lore.size() - 3).startsWith("value=")) {
				lore.remove(lore.size() - 3);

			}
		if (lore.size() >= 3)
			if (lore.get(lore.size() - 3).startsWith("#")) {
				lore.remove(lore.size() - 3);

			}
		if (lore.size() >= 2)
			if (lore.get(lore.size() - 2).startsWith("#")) {
				lore.remove(lore.size() - 2);
			}

		lore.add("#" + token);
		im.setLore(lore);
		is.setItemMeta(im);
		setItem(slot, is);
	}

	public void setItem(GElement element) {
		setItem(element.getSlot(), element.getItemStack(), element.getCustomItemID(), element.getCallback());
	}
	public void setItem(int slot, GElement element) {
		setItem(slot, element.getItemStack(), element.getCustomItemID(), element.getCallback());
	}

	public static void fire(InventoryClickEvent e, ItemStack is, Player p) {
		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() {
		 */
		List<String> lore = is.getItemMeta().getLore();
		String token = lore.get(lore.size() - 1).replace("#", "");
		tokenToListener.get(token).onClick(p, e.getInventory(), is, e.getSlot(), tokenToID.get(token), e,
				Var.invToGUI.get(e.getInventory()));
		/*
		 * } }).start(); return;
		 */

	}

}
