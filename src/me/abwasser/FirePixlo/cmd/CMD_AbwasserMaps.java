package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.abwasser.FirePixlo.ItemListener;
import me.abwasser.FirePixlo.ItemListener.Callback;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.gui.Var;;

public class CMD_AbwasserMaps implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (args.length == 0) {
			p.getInventory().setItemInMainHand(ItemListener.markItemStack(
					Var.itemBakery(Material.REDSTONE_TORCH, "Pos 1", 1, (short) 0, "Mark position 1"), new Callback() {
						@Override
						public void call(PlayerInteractEvent e) {
							e.setCancelled(true);
							if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
								Location loc1 = e.getClickedBlock().getLocation();
								p.getInventory().setItemInMainHand(
										ItemListener.markItemStack(Var.itemBakery(Material.REDSTONE_TORCH, "Pos 2", 1,
												(short) 0, "Mark position 2"), new Callback() {
													@Override
													public void call(PlayerInteractEvent e) {
														e.setCancelled(true);
														p.getInventory().setItemInMainHand(null);
														if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
															Location loc2 = e.getClickedBlock().getLocation();
															V.createAbwasserMap(loc1, loc2, p);
														}
													}

													@Override
													public void call(InventoryClickEvent e) {
														e.setCancelled(true);
													}

													@Override
													public void call(PlayerDropItemEvent e) {
														e.setCancelled(true);
													}

													@Override
													public void call(PlayerSwapHandItemsEvent e) {
														e.setCancelled(true);
													}
												}));
							}
						}

						@Override
						public void call(InventoryClickEvent e) {
							e.setCancelled(true);
						}

						@Override
						public void call(PlayerDropItemEvent e) {
							e.setCancelled(true);
						}

						@Override
						public void call(PlayerSwapHandItemsEvent e) {
							e.setCancelled(true);
						}
					}));
		} else {
			chat(sender, "§4Syntax", "§e/abwassermaps");
		}

		return false;
	}
}
