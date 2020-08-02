package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.PluginRule;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;

public class CMD_PluginRule implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			List<String> list = new ArrayList<String>();
			for (Rules rule : Rules.values())
				list.add(rule.name());
			return list;
		}
		if (index == 1) {
			List<String> list = new ArrayList<String>();
			for (String opt : Rules.valueOf(args[0]).options)
				list.add(opt);
			return list;
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 1) {
			if (Rules.valueOf(args[0]) == null) {
				V.chat(sender, "§cPlugin§8-§3Rule", "§cUnknown Plugin-Rule!");
				return false;
			}
			V.chat(sender, "§cPlugin§8-§3Rule", "§ePlugin-Rule §c" + Rules.valueOf(args[0]).name() + "§8: §3"
					+ PluginRule.read(Rules.valueOf(args[0])));
		} else if (args.length == 2) {
			if (Rules.valueOf(args[0]) == null) {
				V.chat(sender, "§cPlugin§8-§3Rule", "§cUnknown Plugin-Rule!");
				return false;
			}
			PluginRule.write(Rules.valueOf(args[0]), args[1]);
			V.chat(sender, "§cPlugin§8-§3Rule", "§eSet Plugin-Rule §c" + Rules.valueOf(args[0]).name() + "§e to §3"
					+ PluginRule.read(Rules.valueOf(args[0])));
		} else {
			chat(sender, "§4Syntax", "§e/pluginrule §7<Rule> [Value]");
		}
		return false;
	}

}
