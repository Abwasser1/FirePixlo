package me.abwasser.FirePixlo.gui;

import org.bukkit.inventory.ItemStack;

public interface GElement {

	public int getSlot();

	public ItemStack getItemStack();

	public String getCustomItemID();

	public GUIListener getCallback();

}
