package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;

public class CMD_Heal implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: "+index, Level.VERBOSE);
		if (index == 0) {
			return Selector.getTabCompeltion();
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e"+label+"§r from §e"+sender.getName(), Level.VERBOSE);
		if (args.length == 0 && sender instanceof Player) {
			Player p = (Player)sender;
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setFireTicks(0);
			p.setRemainingAir(1000);
			chat(p, "§aHeal", "§aYou healed yourself");
		} else if (args.length == 1) {
			Selector.select(args[0], new SelectorCallback() {
				
				@Override
				public void run(Player p) {
					p.setHealth(20);
					p.setFoodLevel(20);
					p.setFireTicks(0);
					p.setRemainingAir(10);
					chat(p, "§aHeal", "§aYou were healed by §e"+sender.getName());
					chat(sender, "§aHeal", "§aYou healed §e"+p.getName());
					
				}
			});
		} else {
			chat(sender, "§4Syntax", "§e/heal §7<Player>");
		}
		return false;
	}

}
