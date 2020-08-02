package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.gui.Callback;
import me.abwasser.FirePixlo.gui.GUIs;
import me.abwasser.FirePixlo.gui.GUIs.selectWorld;;

public class CMD_World implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: "+index, Level.VERBOSE);
		if (index == 0) {
			return Arrays.asList("create", "teleport", "tp", "delete");
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e"+label+"§r from §e"+sender.getName(), Level.VERBOSE);
		Player p = (Player)sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("create")) {
				new GUIs.WorldCreatorGUI().getGUI().open(p);
			}
			if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
				selectWorld sw = new GUIs.selectWorld();
				sw.getGUI().open(p);
				sw.setCallback(new Callback() {

					@Override
					public void call(Object obj) {
						World w = (World) obj;
						p.teleport(w.getSpawnLocation());
					}
				});
			}
			if (args[0].equalsIgnoreCase("delete")) {
				selectWorld sw = new GUIs.selectWorld();
				sw.getGUI().open(p);
				sw.setCallback(new Callback() {

					@Override
					public void call(Object obj) {
						World w = (World) obj;
						V.deleteWorld(w);
					}
				});
			}
			if (args[0].equalsIgnoreCase("folder")) {
				new GUIs.FileExplorerGUI().getGUI("/").open(p);
			}
		} else {
			chat(sender, "§4Syntax", "§e/world §7<create|teleport/tp|delete>");
		}

		return false;
	}

}
