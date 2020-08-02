package me.abwasser.FirePixlo.gui;

import org.bukkit.inventory.ItemStack;

public class Option {

	public ItemStack is;
	public String value;

	public Option(ItemStack is, String value) {
		this.is = is;
		this.value = value;
	}

	public ItemStack getIs() {
		return is;
	}

	public void setIs(ItemStack is) {
		this.is = is;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
