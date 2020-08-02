package me.abwasser.FirePixlo.cinematica;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.abwasser.FirePixlo.gui.Callback;
import me.abwasser.FirePixlo.gui.GUI;
import me.abwasser.FirePixlo.gui.GUIListener;
import me.abwasser.FirePixlo.gui.TextInput;
import me.abwasser.FirePixlo.gui.Var;

public class TimedEffectGUI {

	public ArrayList<String> command = new ArrayList<>();
	public Callback callback;

	public TimedEffectGUI(Callback callback) {
		this.callback = callback;
	}

	public GUI getGUI() {
		GUI gui = new GUI(6, "§cTimed§eEffect§7 - §8GUI");
		gui.setItem(0, Var.itemBakery(Material.GREEN_WOOL, "§aAdd", 1, (short) 0, ""), "", new GUIListener() {

			@Override
			public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID, InventoryClickEvent e,
					GUI gui) {
				p.closeInventory();
				CommandInput ci = new CommandInput(new Callback() {

					@Override
					public void call(Object obj) {
						command.add((String) obj);
					}
				});
				ci.getGUI().open(p);
			}
		});
		gui.setItem(1, Var.itemBakery(Material.RED_WOOL, "§cFinish", 1, (short) 0, ""), "", new GUIListener() {

			@Override
			public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID, InventoryClickEvent e,
					GUI gui) {
				p.closeInventory();
				for(String str : command) {
					callback.call(str);
				}
			}
		});
		int slot = 2;
		for (String cmd : command) {
			gui.setItem(slot,
					Var.itemBakery(Material.GRAY_WOOL, cmd.split("|#|")[0], 1, (short) 0, cmd.split("|#|")[1]));
			slot++;
		}
		return gui;
	}

	public static class CommandInput {

		public Callback callback;

		public CommandInput(Callback callback) {
			this.callback = callback;
		}

		public GUI getGUI() {
			GUI gui = new GUI(3, "§cTimed§eEffect§7 - §8Input");
			gui.setItem(11, Var.itemBakery(Material.YELLOW_WOOL, "§aTitle", 1, (short) 0, ""), "", new GUIListener() {

				@Override
				public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
						InventoryClickEvent e, GUI gui) {
					TextInput title = new TextInput("Title",
							Var.itemBakery(Material.NAME_TAG, "^7[^cFire^3Pixlo^7]", 1, (short) 0, ""));
					title.open(p, new Callback() {

						@Override
						public void call(Object text) {
							String upper = (String) text;
							TextInput title = new TextInput("Subtitle",
									Var.itemBakery(Material.NAME_TAG, "^eSubtitle", 1, (short) 0, ""));
							title.open(p, new Callback() {
								@Override
								public void call(Object text) {
									String sub = (String) text;
									callback.call("TITLE |#| " + upper + " |~| " + sub);
								}
							});
						}
					});
				}
			});
			gui.setItem(12, Var.itemBakery(Material.YELLOW_WOOL, "§aChat", 1, (short) 0, ""), "", new GUIListener() {

				@Override
				public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
						InventoryClickEvent e, GUI gui) {
					TextInput title = new TextInput("CHAT",
							Var.itemBakery(Material.NAME_TAG, "Hallo, das ist das", 1, (short) 0, ""));
					title.open(p, new Callback() {
						@Override
						public void call(Object text) {
							callback.call("CHAT |#| " + (String) text);

						}
					});
				}
			});
			gui.setItem(13, Var.itemBakery(Material.YELLOW_WOOL, "§aTime", 1, (short) 0, ""), "", new GUIListener() {

				@Override
				public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
						InventoryClickEvent e, GUI gui) {
					TextInput title = new TextInput("Time",
							Var.itemBakery(Material.NAME_TAG, "sunrise|noon|sunset|midnight|%zahl%", 1, (short) 0, ""));
					title.open(p, new Callback() {
						@Override
						public void call(Object text) {
							callback.call("TIME |#| " + (String) text);

						}
					});
				}
			});
			gui.setItem(15, Var.itemBakery(Material.COMMAND_BLOCK, "§aCommand", 1, (short) 0, ""), "",
					new GUIListener() {

						@Override
						public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
								InventoryClickEvent e, GUI gui) {
							TextInput title = new TextInput("Command",
									Var.itemBakery(Material.NAME_TAG,
											"/playsound minecraft:ui.toast.challenge_complete master @p ~ ~ ~ 10 1 1",
											1, (short) 0, ""));
							title.open(p, new Callback() {
								@Override
								public void call(Object text) {
									callback.call("CMD |#| " + (String) text);
								}
							});
						}
					});
			gui.backgroundFill();
			return gui;
		}
	}

}
