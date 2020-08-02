package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;;

public class CMD_Dev implements Commander, TabCallback {
	public static HashMap<Level, ArrayList<Player>> map = new HashMap<>();

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()",  "Tab-Completing Index: "+index, Level.VERBOSE);
		if (index == 0) {
			return Arrays.asList("Verbose", "Medium", "High", "Errors", "None");
		} else if (index == 1) {
			return Selector.getTabCompeltion();
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e"+label+"§r from §e"+sender.getName(), Level.VERBOSE);
		if (args.length == 1) {
			b((Player) sender);
			if (args[0].equalsIgnoreCase("none"))
				return true;
			a(Level.ERROR, (Player) sender);
			if (args[0].equalsIgnoreCase("errors"))
				return true;
			a(Level.HIGH, (Player) sender);
			if (args[0].equalsIgnoreCase("high"))
				return true;
			a(Level.MEDIUM, (Player) sender);
			if (args[0].equalsIgnoreCase("medium"))
				return true;
			a(Level.VERBOSE, (Player) sender);
		} else if (args.length == 2) {
			Selector.select(args[1], new SelectorCallback() {

				@Override
				public void run(Player p) {
					b(p);
					if (args[0].equalsIgnoreCase("none"))
						return;
					a(Level.ERROR, p);
					if (args[0].equalsIgnoreCase("errors"))
						return;
					a(Level.HIGH, p);
					if (args[0].equalsIgnoreCase("high"))
						return;
					a(Level.MEDIUM, p);
					if (args[0].equalsIgnoreCase("medium"))
						return;
					a(Level.VERBOSE, p);

				}
			});

		} else {
			chat(sender, "§4Syntax", "§e/dev §7<Verbose|Medium|High|Errors>");
		}

		return false;
	}

	public static void a(Level level, Player p) {
		if (map.get(level) == null) {
			map.put(level, new ArrayList<Player>());
		}
		ArrayList<Player> temp = map.get(level);
		temp.add(p);
		map.put(level, temp);
		
	}

	public static void b(Player p) {
		for (Level level : Level.values()) {
			if (map.get(level) == null) {
				map.put(level, new ArrayList<Player>());
			}
			ArrayList<Player> temp = map.get(level);
			temp.remove(p);
			map.put(level, temp);
		}
	}

	public static enum Level {
		VERBOSE("§eVerbose", "§7"), MEDIUM("§6Medium", "§e"), HIGH("§cHigh", "§c"), ERROR("§4ERROR", "§d");

		String colored, msgColor;

		private Level(String colored, String msgColor) {
			this.colored = colored;
			this.msgColor = msgColor;
		}

		public String getColored() {
			return colored;
		}

		public String getMsgColor() {
			return msgColor;
		}
	}

}
