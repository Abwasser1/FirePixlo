package me.abwasser.FirePixlo.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GTextInput implements GUIListener, GElement {

	int slot;
	ItemStack gui;
	ItemStack gui2;
	ItemStack anvil;
	GTextInputInterface callback;
	String id;
	String title;
	GTextInput instance;

	public GTextInput(int slot, ItemStack gui, ItemStack gui2, ItemStack anvil, String title,
			GTextInputInterface callback) {
		this.slot = slot;
		this.gui = gui;
		this.gui2 = gui2;
		this.anvil = anvil;
		this.callback = callback;
		id = Token.generateToken(10);
		this.title = title;
		instance = this;
	}

	@Deprecated
	public GTextInput(int slot, ItemStack gui, ItemStack gui2, ItemStack anvil, String title) {
		this.slot = slot;
		this.gui = gui;
		this.gui2 = gui2;
		this.anvil = anvil;
		this.callback = null;
		id = Token.generateToken(10);
		this.title = title;
		instance = this;
	}

	String text = "undefined";

	/**
	 * <b>!!This method is Sync use callback for Async behavior</b>
	 * 
	 * @return Text
	 */
	@Deprecated
	public String getText() {
		return text;
	}

	public int getSlot() {
		return slot;
	}

	@Override
	public ItemStack getItemStack() {
		return gui;
	}

	@Override
	public String getCustomItemID() {
		return id;
	}

	@Override
	public GUIListener getCallback() {
		return this;
	}

	@Override
	public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID, InventoryClickEvent e,
			GUI gui) {

		if (id.equals(customID)) {
			if (this.slot == slot) {
				p.closeInventory();
				new TextInput(title, anvil).open(p, new Callback() {

					@Override
					public void call(Object obj) {
						text = (String) obj;
						ItemMeta im = anvil.getItemMeta();
						im.setDisplayName(text);
						anvil.setItemMeta(im);
						ItemMeta imm = gui2.getItemMeta();
						List<String> lore = imm.getLore();
						lore.add("value=§f" + text + "§r");
						imm.setLore(lore);
						gui2.setItemMeta(imm);
						gui.setItem(slot, gui2, customID, instance);
						gui.open(p);
						if (callback != null)
							callback.onenter(text, instance, p, e);
						

					}
				});

			}
		}

	}

}
