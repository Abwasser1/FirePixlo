package me.abwasser.FirePixlo.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.gui.AnvilGUI.AnvilClickEvent;
import me.abwasser.FirePixlo.gui.AnvilGUI.AnvilSlot;


public class TextInput {

	String text = "undefined";
	String title;
	ItemStack is;

	public TextInput(String title, ItemStack is) {
		this.title = title;
		this.is = is;
	}

	// TODO ADD CALLBACK
	public void open(Player p, Callback callback) {
		AnvilGUI GUI = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

			@Override
			public void onAnvilClick(AnvilClickEvent e) {
				if (e.getSlot() == AnvilSlot.OUTPUT && e.hasText()) {
					e.setWillClose(false);
					text = e.getText();
					callback.call(text);

				}
			}

		});
		GUI.setSlot(AnvilSlot.INPUT_LEFT, is);
		GUI.setSlotName(AnvilSlot.INPUT_LEFT, is.getItemMeta().getDisplayName());
		GUI.setTitle(title);
		GUI.open();
		return;
	}

}
