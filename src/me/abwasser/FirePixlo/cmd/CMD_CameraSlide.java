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
import me.abwasser.FirePixlo.cameraSlides.CameraSlide;
import me.abwasser.FirePixlo.cameraSlides.SlidePlayer;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.gui.AnvilGUI;
import me.abwasser.FirePixlo.gui.TextInput;
import me.abwasser.FirePixlo.gui.Var;;

public class CMD_CameraSlide implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (args.length == 1) {
			p.getInventory().setItemInMainHand(ItemListener.markItemStack(
					Var.itemBakery(Material.TARGET, "Pos 1", 1, (short) 0, "Mark position 1"), new Callback() {
						@Override
						public void call(PlayerInteractEvent e) {
							e.setCancelled(true);
							Location loc1 = e.getPlayer().getLocation();
							p.getInventory()
									.setItemInMainHand(ItemListener.markItemStack(
											Var.itemBakery(Material.TARGET, "Pos 2", 1, (short) 0, "Mark position 2"),
											new Callback() {
												@Override
												public void call(PlayerInteractEvent e) {
													e.setCancelled(true);
													p.getInventory().setItemInMainHand(null);
													Location loc2 = e.getPlayer().getLocation();
													new TextInput("Duration in Ticks",
															Var.itemBakery(Material.CLOCK, "", 1, (short) 0,
																	"Duration in Ticks")).open(p,
																			new me.abwasser.FirePixlo.gui.Callback() {

																				@Override
																				public void call(Object obj) {
																					int duration = Integer
																							.parseInt((String) obj);
																					CameraSlide slide = new CameraSlide();
																					slide.setLoc1(loc1);
																					slide.setLoc2(loc2);
																					slide.setDuration(duration);
																					slide.save(args[0]);

																				}
																			});

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
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("play")) {
				SlidePlayer player = new SlidePlayer();
				player.play(args[1], p);
			} else
				chat(sender, "§4Syntax", "§e/cameraSlide §8<Name> §eor §8play <Name>");
		}else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("play")) {
				SlidePlayer player = new SlidePlayer();
				player.playbackSpeed = Integer.parseInt(args[2]);
				player.play(args[1], p);
			} else
				chat(sender, "§4Syntax", "§e/cameraSlide §8<Name> §eor §8play <Name>");
		} else
			chat(sender, "§4Syntax", "§e/cameraSlide §8<Name> §eor §8play <Name>");

		return false;
	}
}
