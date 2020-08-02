package me.abwasser.FirePixlo.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;

public class CommandRegistry implements CommandExecutor, TabCompleter {

	public static HashMap<String, Entry> registry = new HashMap<>();

	public static void register(String cmd, boolean console, String permission, Commander commander, TabCallback tabC) {
		V.dev(CommandRegistry.class, "register()", "Registering command §e" + cmd + "§r...", Level.VERBOSE);
		registry.put(cmd.toLowerCase(), new Entry(cmd, console, permission, commander, tabC));
		Main.getInstance().getCommand(cmd).setExecutor(new CommandRegistry());
		Main.getInstance().getCommand(cmd).setTabCompleter(new CommandRegistry());
		V.dev(CommandRegistry.class, "register()", "Registered command §e" + cmd + "§r", Level.MEDIUM);
		V.dev(CommandRegistry.class, "register()", "->Console: §e" + console, Level.VERBOSE);
		V.dev(CommandRegistry.class, "register()", "->Permission: §e" + permission, Level.VERBOSE);
		V.dev(CommandRegistry.class, "register()", "->Commander: §e" + commander.getClass().getSimpleName(),
				Level.VERBOSE);
		V.dev(CommandRegistry.class, "register()", "->TabCallback: §e" + tabC.getClass().getSimpleName(),
				Level.VERBOSE);
	}

	public static void register(String cmd, boolean console, String permission, Commander commander) {
		register(cmd, console, permission, commander, new TabCallback() {
			@Override
			public List<String> callback(int index, String label, String[] args) {
				return Arrays.asList("");
			}
		});
	}

	public static class Entry {
		String cmd;
		boolean console;
		String permission;
		TabCallback tabC;
		Commander commander;

		public Entry(String cmd, boolean console, String permission, Commander commander, TabCallback tabC) {
			V.dev(Entry.class, "Constructor", "Created new Entry for command §e" + cmd + "§r", Level.VERBOSE);
			this.cmd = cmd;
			this.console = console;
			this.permission = permission;
			this.tabC = tabC;
			this.commander = commander;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(CommandRegistry.class, "onCommand()", "Recieved call for command §e" + cmd + "§r: §e" + label,
				Level.MEDIUM);
		if (registry.containsKey(cmd.getName().toLowerCase())) {
			Entry entry = registry.get(cmd.getName().toLowerCase());
			if (!entry.console) {
				if (!(sender instanceof Player)) {
					V.dev(CommandRegistry.class, "onCommand()",
							"Console tried running Player only command §e" + cmd + "§r: §e" + label, Level.VERBOSE);
					sender.sendMessage(Main.pr + "§c That command is only for Players!");
					return false;
				}
			}
			if (!entry.permission.isEmpty()) {
				if (!sender.hasPermission(entry.permission)) {
					V.dev(CommandRegistry.class, "onCommand()", "§e" + sender.getName() + "§r tried running §e" + cmd
							+ "§r, but didn't have the permisson §e" + entry.permission, Level.MEDIUM);
					V.dev(CommandRegistry.class, "onCommand()", "-> Label: §e" + label, Level.VERBOSE);
					sender.sendMessage(Main.pr + "§c You dont have the permission to execute that command!");
					return false;
				}
			}
			entry.commander.callback(sender, cmd, label, args);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(CommandRegistry.class, "onTabComplete()", "§e" + sender.getName() + "§r wants tabcompletion for " + cmd,
				Level.VERBOSE);
		if (registry.containsKey(cmd.getName().toLowerCase())) {
			Entry entry = registry.get(cmd.getName().toLowerCase());
			if (!entry.permission.isEmpty()) {
				if (!sender.hasPermission(entry.permission)) {
					return Arrays.asList("");
				}
			}
			final List<String> completions = new ArrayList<>();
			final List<String> list = entry.tabC.callback(args.length - 1, label, args);
			if (list == null) {
				return new ArrayList<String>();
			}
			StringUtil.copyPartialMatches(args[args.length - 1], list, completions);
			if (completions.size() == 0) {
				StringUtil.copyPartialMatches("firepixlo:"+args[args.length - 1], list, completions);
			}
			Collections.sort(completions);
			V.dev(CommandRegistry.class, "onTabComplete()", "§e" + sender.getName() + "§r recieved tabcompletion for §e"
					+ cmd + "§r: §e" + Arrays.toString(completions.toArray()), Level.VERBOSE);
			return completions;
		}
		return new ArrayList<String>();
	}

}
