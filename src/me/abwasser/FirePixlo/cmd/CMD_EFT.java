package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.ItemListener;
import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.ItemListener.Callback;
import me.abwasser.FirePixlo.eft.Exit;
import me.abwasser.FirePixlo.gui.Var;;

public class CMD_EFT implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		if (index == 0) {
			return Arrays.asList("registerExits", "fixmap");
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("registerExits")) {
				Player p = (Player) sender;
				p.getInventory().setItemInMainHand(ItemListener.markItemStack(Var.itemBakery(Material.CRIMSON_FUNGUS,
						"Mark the signs", 1, (short) 0, "lel. Markier schon bi***!"), new Callback() {
							@Override
							public void call(PlayerInteractEvent e) {
								if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
									Block b = e.getClickedBlock();
									if (b.getType() == Material.WARPED_SIGN
											|| b.getType() == Material.WARPED_WALL_SIGN) {
										Sign sign = (Sign) b.getState();
										if (!sign.getLine(0).equalsIgnoreCase("[Exit]")) {
											p.sendMessage("Invalid Syntax! Line 1");
											return;
										}
										if (sign.getLine(1).isEmpty()) {
											p.sendMessage("Invalid Syntax! Line 2");
											return;
										}
										if (sign.getLine(2).isEmpty()) {
											p.sendMessage("Invalid Syntax! Line 3");
											return;

										}
										if (sign.getLine(3).isEmpty()) {
											p.sendMessage("Invalid Syntax! Line 4");
											return;
										}
										try {
											String name = sign.getLine(1);
											String radius = sign.getLine(2).split(" ")[0];
											String heigth = sign.getLine(2).split(" ")[1];
											String alwaysOpen = sign.getLine(3).split(" ")[0];
											String time = sign.getLine(3).split(" ")[1];
											new Exit(b.getLocation(), name, Integer.parseInt(radius),
													Integer.parseInt(heigth), alwaysOpen == "t" ? true : false,
													Integer.parseInt(time)).save();
											sign.setLine(0, "[Registered]");
											sign.update();
										} catch (Exception e1) {
											e1.printStackTrace();
											p.sendMessage("§cSyntax Error");
										}
									} else
										p.sendMessage("§cWait a second...\nThats not an warped_sign you dumbo!");
								}
							}
						}));
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("fixmap")) {
				Player p = (Player) sender;
				int radius = Integer.parseInt(args[1]);
				int x1 = p.getLocation().getBlockX() - radius;
				int y1 = p.getLocation().getBlockY() - radius;
				int z1 = p.getLocation().getBlockZ() - radius;

				int x2 = p.getLocation().getBlockX() + radius;
				int y2 = p.getLocation().getBlockY() + radius;
				int z2 = p.getLocation().getBlockZ() + radius;

				int verified = 0;
				int found = 0;
				int checked = 0;

				for (int x = x1; x != x2; x++) {
					for (int y = y1; y != y2; y++) {
						if (y < 0 || y > 255) {
							continue;
						}
						for (int z = z1; z != z2; z++) {
							Block b = p.getWorld().getBlockAt(x, y, z);
							Bukkit.broadcastMessage("§7Checking... [" + x + "," + y + "," + z + "]");
							checked++;
							if (b.getType() == Material.WATER || b.getType() == Material.LAVA) {
								Bukkit.broadcastMessage("§eVerifying... [" + x + "," + y + "," + z + "]");
								verified++;
								if (b.getWorld().getHighestBlockAt(b.getLocation()).getType() != Material.WATER
										&& b.getWorld().getHighestBlockAt(b.getLocation()).getType() != Material.LAVA) {
									Bukkit.broadcastMessage("§cFound [" + x + "," + y + "," + z + "]");
									found++;
									b.setType(Material.INFESTED_STONE);
								}
							}
						}
					}
				}
				Bukkit.broadcastMessage("§cChecked§8: §3" + checked);
				Bukkit.broadcastMessage("§cVerified§8: §3" + verified);
				Bukkit.broadcastMessage("§cFound§8: §3" + found);

			}
		} else {
			chat(sender, "§4Syntax", "§e/eft §7registerExits");
			chat(sender, "§4Syntax", "§e/eft §7fixmap <radius>");
		}
		return false;
	}

}
