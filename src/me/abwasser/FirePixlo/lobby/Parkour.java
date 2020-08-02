package me.abwasser.FirePixlo.lobby;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.abwasser.FirePixlo.ItemListener;
import me.abwasser.FirePixlo.ItemListener.Callback;
import me.abwasser.FirePixlo.Locations;
import me.abwasser.FirePixlo.PlayerProperty;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.gui.Var;

public class Parkour {

	public Player p;
	public Location checkPoint;
	public Location startPoint;
	public Location checkPointRot;
	public Location startPointRot;
	public long time;
	public HashMap<Location, Integer> hashMap = new HashMap<>();
	public int parkour_time;
	public boolean finished = false;

	public Parkour(Player p) {
		this.p = p;
	}

	public void start(Location loc) {
		if (finished)
			return;
		if (p.getInventory().getHeldItemSlot() != 4)
			p.getInventory().setHeldItemSlot(2);
		this.startPoint = loc;
		this.checkPoint = loc;
		this.startPointRot = p.getLocation();
		this.checkPointRot = p.getLocation();
		this.time = System.currentTimeMillis();
		equipPlayer();
		String key2 = "lobby.parkour." + V.LocToString(startPoint) + ".time";
		if (PlayerProperty.readPlayerData(p, key2) != null)
			p.sendActionBar("§cYour Highscore§7:§3 " + convert((int) PlayerProperty.readPlayerData(p, key2)));
		else
			p.sendActionBar("§3Started Parkour!");

	}

	public void setCheckpoint(Location loc) {
		if (finished)
			return;
		this.checkPoint = loc;
		this.checkPointRot = p.getLocation();
		hashMap.put(loc, (int) V.calcTime(time));
		String key2 = "lobby.parkour." + V.LocToString(startPoint) + ".meta. " + V.LocToString(loc);
		if (PlayerProperty.readPlayerData(p, key2) != null) {
			int now = (int) V.calcTime(time);
			int then = (int) PlayerProperty.readPlayerData(p, key2);
			if (now > then)
				p.sendActionBar("§c§l+§r§3" + ((now - then) / 1000d));
			else
				p.sendActionBar("§a§l-§r§3" + ((then - now) / 1000d));
		}

	}

	public void finish(Location loc) {
		if (finished)
			return;
		hashMap.put(loc, (int) V.calcTime(time));
		parkour_time = (int) V.calcTime(time);
		String key2 = "lobby.parkour." + V.LocToString(startPoint) + ".time";
		if (PlayerProperty.readPlayerData(p, key2) == null)
			PlayerProperty.writePlayerData(p, key2, parkour_time);
		else if (((int) PlayerProperty.readPlayerData(p, key2)) > parkour_time)
			PlayerProperty.writePlayerData(p, key2, parkour_time);

		for (Location loc2 : hashMap.keySet()) {
			String key = "lobby.parkour." + V.LocToString(startPoint) + ".meta. " + V.LocToString(loc2);
			if (PlayerProperty.readPlayerData(p, key) == null)
				PlayerProperty.writePlayerData(p, key, hashMap.get(loc2));
			else if (((int) PlayerProperty.readPlayerData(p, key)) > hashMap.get(loc2))
				PlayerProperty.writePlayerData(p, key, hashMap.get(loc2));
		}
		stop();
	}

	public void backToLastCP() {
		if (finished)
			return;
		p.teleport(checkPointRot);
		if (p.getInventory().getHeldItemSlot() != 4)
			p.getInventory().setHeldItemSlot(2);

	}

	public void reset() {
		if (finished)
			return;
		p.teleport(startPointRot);
		if (p.getInventory().getHeldItemSlot() != 4)
			p.getInventory().setHeldItemSlot(2);

	}

	public void stop() {
		finished = true;
		p.teleport(Locations.get(Locations.LOCATION_LOBBY));
		p.getInventory().clear();
		parkour_time = 0;
	}

	public String convert(int ms) {
		int sec = ms / 1000;
		return "§3" + (int) Math.floor((sec / 60)) + "§cm §3" + (sec % 60) + "§cs";
	}

	public void equipPlayer() {
		p.getInventory().setItem(2, ItemListener.markItemStack(
				Var.itemBakery(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, "§3Back to Checkpoint", 1, (short) 0, ""),
				new Callback() {
					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerInteractEvent e) {
						if (e.getAction() != Action.PHYSICAL)
							backToLastCP();
						e.setCancelled(true);
					}

				}));
		p.getInventory().setItem(4, ItemListener
				.markItemStack(Var.itemBakery(Material.OAK_DOOR, "§cReset", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerInteractEvent e) {
						if (e.getAction() != Action.PHYSICAL)
							reset();
						e.setCancelled(true);
					}

				}));
		p.getInventory().setItem(6, ItemListener
				.markItemStack(Var.itemBakery(Material.RED_BED, "§4Exit", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerInteractEvent e) {
						if (e.getAction() != Action.PHYSICAL)
							stop();
						e.setCancelled(true);
					}

				}));
	}

}
