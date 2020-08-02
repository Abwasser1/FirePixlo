package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import me.abwasser.FirePixlo.server.Server;
import me.abwasser.FirePixlo.server.ServerManager;

public class CMD_Server implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			return Arrays.asList("join");
		}
		if (index == 1) {
			return ServerManager.getServers();
		}
		if (index == 2) {
			return Selector.getTabCompeltion();
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 0) {
			for (Server server : ServerManager.servers) {
				if (server.isLoaded())
					V.chat(sender, "§cServer§3Manager", "§a"+server.getName()+" §8| §a"+server.getServerWorld() + " §8| §a"+server.getOnlinePlayers().size());
				else
					V.chat(sender, "§cServer§3Manager", "§c"+server.getName()+" §8| §c"+server.getServerWorld() + " §8| §c"+server.getOnlinePlayers().size());
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("join")) {
				Server server = ServerManager.getServer(args[1]);
				if (server != null) {
					Server oldServer = ServerManager.getServer((Player) sender);
					oldServer.leavePlayer((Player) sender, server);
					server.joinPlayer((Player) sender, oldServer);
				} else
					chat(sender, "§aServer", "§cIch kenne diesen Server nicht!");
			} else
				chat(sender, "§4Syntax", "§e/server §7<join> <Server>");
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("join")) {
				Server server = ServerManager.getServer(args[1]);
				if (server != null) {
					Selector.select(args[2], new SelectorCallback() {

						@Override
						public void run(Player p) {
							ServerManager.movePlayer(p, server);

						}
					});

				} else
					chat(sender, "§aServer", "§cIch kenne diesen Server nicht!");
			} else
				chat(sender, "§4Syntax", "§e/server §7<join> <Server> <Player>");
		} else {
			chat(sender, "§4Syntax", "§e/server §7<join> <Server>");
		}
		return false;
	}

}
