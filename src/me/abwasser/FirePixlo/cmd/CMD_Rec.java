package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.io.File;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.cinematica.AdvancedRecorder;

public class CMD_Rec implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			AdvancedRecorder recorder = new AdvancedRecorder(new File("FirePixlo/records/", args[0]+".yml"), (Player)sender);
			recorder.equipPlayer();
		}else
			chat(sender, "§4Syntax", "§e/rec <filename>");
		return false;
	}

}
