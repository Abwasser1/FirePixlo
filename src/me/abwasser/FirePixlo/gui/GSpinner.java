package me.abwasser.FirePixlo.gui;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GSpinner implements GUIListener, GElement {

	public HashMap<String, Option[]> map1 = new HashMap<>();
	public HashMap<String, Integer> map2 = new HashMap<>();

	int slot;
	Option[] posibilities;
	String id;
	GSpinnerInterface Ginterface = null;
	Option selected;

	public GSpinner(int slot, Option... posibilities) {
		if (posibilities == null)
			throw new NullPointerException("Posibilities cannot be null");
		this.posibilities = posibilities;
		id = Token.generateToken(10);
		this.slot = slot;
		map1.put(id, posibilities);
		map2.put(id, 0);
	}

	public GSpinner(int slot, GSpinnerInterface callback, Option... posibilities) {
		if (posibilities == null)
			throw new NullPointerException("Posibilities cannot be null");
		this.posibilities = posibilities;
		id = Token.generateToken(10);
		this.slot = slot;
		map1.put(id, posibilities);
		map2.put(id, 0);
		Ginterface = callback;
	}

	@Override
	public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID, InventoryClickEvent e,
			GUI gui) {
		if (map1.containsKey(customID)) {
			Option[] stacks = map1.get(customID);
			int index = map2.get(id);
			index++;
			if (stacks.length == index)
				index = 0;
			ItemStack iss = stacks[index].getIs();
			gui.setItem(slot, iss, customID, this);
			map2.put(id, index);
			if (Ginterface != null)
				Ginterface.onselect(stacks[index].getValue(), this, p, e);
			selected = stacks[index];
		}
	}

	public Option getSelected() {
		return selected;
	}

	public String getValue() {
		return getSelected().getValue();
	}

	@Override
	public int getSlot() {
		return slot;
	}

	@Override
	public ItemStack getItemStack() {
		return posibilities[0].getIs();
	}

	@Override
	public String getCustomItemID() {
		return id;
	}

	@Override
	public GUIListener getCallback() {
		return this;
	}

}
