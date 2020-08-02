package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import me.abwasser.FirePixlo.customItems.CustomItem;
import me.abwasser.FirePixlo.customItems.CustomItems;;

public class CMD_Get implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			List<String> names = new ArrayList<String>();
			for (CustomItem ci : CustomItems.registry) {
				names.add(ci.getNamespacedKey().toString());
			}
			return names;
		} else if (index == 1)
			return Selector.getTabCompeltion();
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 1) {
			if (!args[0].startsWith("firepixlo:"))
				args[0] = "firepixlo:" + args[0];
			HashMap<String, CustomItem> names = new HashMap<String, CustomItem>();
			for (CustomItem ci : CustomItems.registry) {
				names.put(ci.getNamespacedKey().toString(), ci);
			}
			if (names.containsKey(args[0])) {
				((Player) sender).getInventory().addItem(names.get(args[0]).getNewInstace().getItem());

			} else
				chat(sender, "§dGet", "§cI dont know this item!");
		} else if (args.length == 2) {
			if (!args[1].startsWith("firepixlo:"))
				args[1] = "firepixlo:" + args[1];
			Selector.select(args[1], new SelectorCallback() {

				@Override
				public void run(Player p) {
					HashMap<String, CustomItem> names = new HashMap<String, CustomItem>();
					for (CustomItem ci : CustomItems.registry) {
						names.put(ci.getNamespacedKey().toString(), ci);
					}
					if (names.containsKey(args[0])) {
						p.getInventory().addItem(names.get(args[0]).getItem());
					} else
						chat(sender, "§dGet", "§cI dont know this item!");

				}
			});

		} else {

			chat(sender, "§4Syntax", "§e/get §7<Item> <Player>");
		}

		return false;
	}

}
