package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.abwasser.FirePixlo.ItemListener;
import me.abwasser.FirePixlo.ItemListener.Callback;
import me.abwasser.FirePixlo.Locations;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.gui.Var;

public class CMD_SysLoc implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0)
			return Arrays.asList("set", "test", "delete");

		if (index == 1)
			return Locations.points();

		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				Locations.save(args[1], ((Player) sender).getLocation());
			} else if (args[0].equalsIgnoreCase("test")) {
				((Player) sender).teleport(Locations.get(args[1]));

			} else if (args[0].equalsIgnoreCase("delete")) {
				Locations.delete(args[1]);
			} else {
				chat(sender, "§4Syntax", "§e/sysloc <set|test|delete> §7<Name>");
			}
		} else if (args.length == 3) {
			((Player) sender).getInventory().addItem(ItemListener
					.markItemStack(Var.itemBakery(Material.NETHER_STAR, "mark", 1, (short) 0, ""), new Callback() {

						@Override
						public void call(PlayerInteractEvent e) {
							Block b = e.getClickedBlock();
							((Player) sender).getInventory().addItem(ItemListener.markItemStack(
									Var.itemBakery(Material.NETHER_STAR, "mark2", 1, (short) 0, ""), new Callback() {

										@Override
										public void call(PlayerInteractEvent e) {
											Block b2 = e.getClickedBlock();
											Location loc1 = b.getLocation();
											Location loc2 = b2.getLocation();
											Location result = V.between(loc1, loc2);
											V.chat(sender, "TEST", result.getX()+"; "+ result.getY()+"; "+ result.getZ()+"; ");
											V.glowBlock(loc1.getWorld().getBlockAt(result), ChatColor.GREEN, 10);
										}
									}));
						}
					}));
		} else {
			chat(sender, "§4Syntax", "§e/sysloc <set|test|delete> §7<Name>");
		}
		return false;
	}

}
