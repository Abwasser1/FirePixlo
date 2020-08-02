package me.abwasser.FirePixlo.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;

public class Selector {

	public static List<String> getTabCompeltion() {
		V.dev(Selector.class, "getTabCompeltion()", "Creating selector tabcompletion", Level.VERBOSE);
		List<String> list = new ArrayList<>();
		list.add("@a");
		for(Player p : Bukkit.getOnlinePlayers()) {
			list.add(p.getName());
		}
		V.dev(Selector.class, "getTabCompeltion()", "Created selector tabcompletion: §e"+Arrays.toString(list.toArray()), Level.VERBOSE);
		return list;
	}

	public static void select(String selector, SelectorCallback callback) throws NullPointerException {
		V.dev(Selector.class, "select()", "Resolving §e"+selector+"§r for §e"+callback.getClass().getName(), Level.VERBOSE);
		if (selector.startsWith("@")) {
			if (selector.equalsIgnoreCase("@a")) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					callback.run(p);
				}
			}
			if (selector.equalsIgnoreCase("@r")) {
				ArrayList<Player> allPlayers = new ArrayList<Player>();
				for (Player players : Bukkit.getOnlinePlayers()) {
					allPlayers.add(players);
				}
				int random = new Random().nextInt(allPlayers.size());
				callback.run(allPlayers.get(random));
			}

		} else {
			Player p = Bukkit.getPlayer(selector);
			if (p == null) {
				throw new NullPointerException("Player " + selector + " is not Online");
			}
			callback.run(p);
		}
	}

	public static interface SelectorCallback {
		public void run(Player p);
	}

}
