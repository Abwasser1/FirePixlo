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

public class CMD_PlayerTime implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0)
			return Selector.getTabCompeltion();
		else if (index == 1)
			return Arrays.asList("sunrise", "noon", "sunset", "midnight", "%zahl%", "normal");
		else if (index == 2)
			return Arrays.asList("doDayLightCycle", "static");
		else if (index == 3)
			return Arrays.asList("%zahl%");
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		int time;
		switch(args[1]) {
		case "sunrise":
			time = 0;
			break;
		case "noon":
			time = 6000;
			break;
		case "sunset":
			time = 12000;
			break;
		case "midnight":
			time = 18000;
			break;
		default:
			time = Integer.parseInt(args[1]);
			break;
		}
		if (args.length == 2) {
			Selector.select(args[0], new SelectorCallback() {
				
				@Override
				public void run(Player p) {
					V.playerTime(p, time, false, 1);
				}
			});
		} else if (args.length == 3) {
			Selector.select(args[0], new SelectorCallback() {
				
				@Override
				public void run(Player p) {
					V.playerTime(p, time, args[2].equalsIgnoreCase("doDayLightCycle"), 1);
				}
			});
		} else if (args.length == 4) {
			Selector.select(args[0], new SelectorCallback() {
				
				@Override
				public void run(Player p) {
					V.playerTime(p, time, args[2].equalsIgnoreCase("doDayLightCycle"), Integer.parseInt(args[3]));
				}
			});
		} else {
			chat(sender, "§4Syntax", "§e/heal §7<Player>");
		}
		return false;
	}

}
