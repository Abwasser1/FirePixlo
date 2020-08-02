package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.server.ServerManager;

public class CMD_Varo implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			return Arrays.asList("start", "reset", "info");
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {

		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("start")) {
				ServerManager.varoServer.manager.start();
			}
			if (args[0].equalsIgnoreCase("reset")) {
				ServerManager.varoServer.manager.reset();
				ServerManager.varoServer.deletePlayers();
			}
			if (args[0].equalsIgnoreCase("info")) {
				V.chat(sender, "§3Info", "isOpen: " + ServerManager.varoServer.manager.isOpen);
				V.chat(sender, "§3Info", "allowed.size(): " + ServerManager.varoServer.manager.allowed.size());
				V.chat(sender, "§3Info",
						"allowed: " + Arrays.toString(ServerManager.varoServer.manager.allowed.toArray()));
				
			}
		} else {
			chat(sender, "§4Syntax", "§e/varo <start|reset|info>");
		}
		return false;
	}

}
