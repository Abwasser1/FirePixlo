package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.PluginRule;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import me.abwasser.FirePixlo.server.Server;
import me.abwasser.FirePixlo.server.ServerManager;

public class CMD_Shutdown implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			List<String> list = new ArrayList<String>();
			for (Server server : ServerManager.servers)
				list.add(server.getName());
			return list;
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 1) {
			HashMap<String, Server> list = new HashMap<String, Server>();
			for (Server server : ServerManager.servers)
				list.put(server.getName(), server);
			if (!list.containsKey(args[0])) {
				V.chat(sender, "§cShut§3down", "§cUnknown Server!");
				return false;
			}
			Server server = list.get(args[0]);
			if (server == ServerManager.lobbyServer)
				server.shutdown();
			else
				server.shutdown(ServerManager.lobbyServer);
			V.chat(sender, "§cShut§3down", "§cShutdown §3" + server.getName());
		} else {
			chat(sender, "§4Syntax", "§e/shutdown §7<Server>");
		}
		return false;
	}

}
