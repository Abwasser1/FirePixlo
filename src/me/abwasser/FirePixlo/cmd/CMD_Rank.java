package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import me.abwasser.FirePixlo.rank.Rank;
import me.abwasser.FirePixlo.rank.RankManager;

public class CMD_Rank implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: "+index, Level.VERBOSE);
		if (index == 0) {
			List<String> list = new ArrayList<>();
			for (Rank rank : Rank.values())
				list.add(rank.name());
			return list;
		}
		if (index == 1) {
			return Selector.getTabCompeltion();
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e"+label+"§r from §e"+sender.getName(), Level.VERBOSE);
		if (args.length == 1 && sender instanceof Player) {
			try {
				Rank rank = Rank.valueOf(args[0]);
				RankManager.savePlayer((Player) sender, rank);
				RankManager.loadPlayer((Player)sender);
				chat(sender, "§6Rank", "§aYou are now an §e"+rank.name()+"!");
			} catch (Exception e) {
				chat(sender, "§6Rank", "§cI dont know this Rank!");
			}
		} else if (args.length == 2) {
			Selector.select(args[1], new SelectorCallback() {

				@Override
				public void run(Player p) {
					Rank rank = Rank.valueOf(args[0]);
					RankManager.savePlayer((Player) sender, rank);
					RankManager.loadPlayer((Player)sender);
					chat(sender, "§6Rank", "§e"+p.getName()+"§a made you an §e"+rank.name()+"!");
					chat(sender, "§6Rank", "§aYou made "+p.getName()+" an §e"+rank.name()+"!");
				}
			});
		} else {
			chat(sender, "§4Syntax", "§e/rank §7<Rank> <Player>");
		}
		return false;
	}

}
