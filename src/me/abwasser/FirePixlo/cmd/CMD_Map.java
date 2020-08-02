package me.abwasser.FirePixlo.cmd;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxEditor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.display.MapUtils;
import me.abwasser.FirePixlo.display.MapUtils.Combo;
import me.abwasser.FirePixlo.display.MapUtils.DisplayTile;
import me.abwasser.FirePixlo.gui.Var;

public class CMD_Map implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (args.length == 3) {
			String file = args[0];
			int sizeX = Integer.parseInt(args[1]);
			int sizeY = Integer.parseInt(args[2]);
			double rand = Math.random();
			new Thread() {
				public void run() {
					try {
						for (BufferedImage buf : MapUtils.split(ImageIO.read(new URL(file)), sizeX, sizeY)) {
							File f = new File("Maps/" + rand + "/" + Math.random() + ".png");
							f.mkdirs();
							f.createNewFile();
							ImageIO.write(buf, "png", f);
							Combo c = MapUtils.getMap(p.getWorld());
							p.getInventory().addItem(c.map);
							new BukkitRunnable() {
								
								@Override
								public void run() {
									MapUtils.sendPicture(p, c.id, buf);
									
								}
							}.runTaskLater(Main.getInstance(), 20);
							
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();

		}
		if (args.length == 4) {
			String file = args[0];
			int sizeX = Integer.parseInt(args[1]);
			int sizeY = Integer.parseInt(args[2]);
			int fps = Integer.parseInt(args[3]);
			ArrayList<DisplayTile> tiles = new ArrayList<>();
			for (int x = 0; x != sizeX; x++)
				for (int y = 0; y != sizeY; y++) {
					Combo combo = MapUtils.getMap(p.getWorld());
					ItemStack is = Var.renameItem(combo.map, "§c" + x + "§3 | §c" + y);
					p.getInventory().addItem(is);
					tiles.add(new DisplayTile(combo.id, is, x, y, sizeX, sizeY));
				}
			MapUtils.playVideoAsync(p, new File(file), fps, tiles.toArray(new DisplayTile[tiles.size()]));

		}
		return false;
	}

}
