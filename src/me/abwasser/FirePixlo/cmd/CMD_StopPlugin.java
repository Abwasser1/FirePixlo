package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;

public class CMD_StopPlugin implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			List<String> list = new ArrayList<String>();
			for (Plugin pl : Bukkit.getPluginManager().getPlugins())
				list.add(pl.getName());
			list.add("*");
			return list;
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {

		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 1) {
			String pl = args[0];
			if (pl.equalsIgnoreCase("*")) {
				Bukkit.getPluginManager().disablePlugins();
				return true;
			}
			Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(pl));
		} else {
			chat(sender, "§4Syntax", "§e/stopplugin <PLUGIN>");
		}
		return false;
	}

}
