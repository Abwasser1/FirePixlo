package me.abwasser.FirePixlo.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface Commander {

	public boolean callback(CommandSender sender, Command cmd, String label, String[] args);

}
