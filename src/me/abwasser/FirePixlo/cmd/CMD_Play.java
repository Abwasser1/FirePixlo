package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.cinematica.AdvancedPlayer;

public class CMD_Play implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			ArrayList<Player> list = new ArrayList<Player>();
			list.add((Player)sender);
			AdvancedPlayer player = new AdvancedPlayer(new File("FirePixlo/records/", args[0]+".yml"), list);
			player.resume();
		}else
			chat(sender, "§4Syntax", "§e/play <filename>");
		return false;
	}

}
