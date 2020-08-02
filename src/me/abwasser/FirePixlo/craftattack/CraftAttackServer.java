package me.abwasser.FirePixlo.craftattack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.PlayerProperty;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.advancements.CraftAttackAdvancements;
import me.abwasser.FirePixlo.server.Server;

public class CraftAttackServer extends Server {

	public ArrayList<Player> onlinePlayers = new ArrayList<>();
	public World serverWorld;
	public String name, description, pr;
	public boolean isLoaded = true;
	public boolean canUnload = true;

	public CraftAttackServer(World serverWorld, String name, String description, String prefix) {
		this.serverWorld = serverWorld;
		this.name = name;
		this.description = description;
		this.pr = prefix;
	}

	@Override
	public ArrayList<Player> getOnlinePlayers() {
		return onlinePlayers;
	}

	@Override
	public void joinPlayer(Player p, Server from) {
		p.setAllowFlight(false);
		if (getOnlinePlayers().isEmpty())
			loadWorld();
		onlinePlayers.add(p);
		clearPlayer(p);
		if (hasPlayerData(p)) {
			loadPlayer(p);
		} else {
			p.teleport(getServerWorld().getSpawnLocation());
		}
		getServerWorld().setDifficulty(Difficulty.NORMAL);
		CraftAttackAdvancements.manager.addPlayer(p);
		CraftAttackAdvancements.manager.update(p);
		serverBroadcast("§a§l+§7» §r§a" + p.getName() + " §ejoined the server!");
		V.expandDebugInfo(p);
	}

	@Override
	public void leavePlayer(Player p, Server to) {
		onlinePlayers.remove(p);
		savePlayer(p);
		p.setAllowFlight(true);
		if (getOnlinePlayers().isEmpty())
			unloadWorld();
		try {
			CraftAttackAdvancements.manager.removePlayer(p);
		} catch (NullPointerException e) {
		}
		serverBroadcast("§c§l-§7» §r§c" + p.getName() + " §eleft");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPr() {
		return pr;
	}

	@Override
	public void shutdown(Server fallback) {
		if (!getOnlinePlayers().isEmpty()) {
			ArrayList<Player> b = new ArrayList<>();
			;
			for (Player p : getOnlinePlayers())
				b.add(p);
			for (Player p : b) {
				leavePlayer(p, fallback);
				fallback.joinPlayer(p, this);
			}
		}

		unloadWorld();
	}

	@Override
	public void shutdown() {
		if (!getOnlinePlayers().isEmpty())
			for (Player p : getOnlinePlayers()) {
				onlinePlayers.remove(p);
				V.kick(p, "§cThe server you were on was closed!\n§e" + getName());
			}
		unloadWorld();
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public List<String> getServerTab(Player p) {
		String difficulty = "difficulty: " + serverWorld.getDifficulty().name();
		return Arrays.asList(difficulty);
	}

	@Override
	public boolean isOnline(Player p) {
		return getOnlinePlayers().contains(p);
	}

	@Override
	public World getServerWorld() {
		return serverWorld;
	}

	@Override
	public void unloadWorld() {
		if (Main.getInstance().isEnabled()) {
			new BukkitRunnable() {

				@Override
				public void run() {
					if (canUnload) {
						Bukkit.unloadWorld(getServerWorld(), true);
						isLoaded = false;
					}
				}
			}.runTask(Main.getInstance());
		} else {
			Bukkit.unloadWorld(getServerWorld(), true);
			isLoaded = false;
		}
	}

	@Override
	public void loadWorld() {
		WorldCreator wc = new WorldCreator(getName());
		Bukkit.createWorld(wc);
		isLoaded = true;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	@Override
	public void canUnload(boolean unload) {
		this.canUnload = unload;
	}

	@Override
	public void onDeath(PlayerDeathEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				e.setDeathMessage(null);
				serverBroadcast(V.getDeathMessage(e.getDeathMessage()));
				Location loc = e.getEntity().getLocation();
				V.chat(e.getEntity(), "§cCraft§3Attack", "§cYou died at §8[§3" + loc.getBlockX() + "§8, §3"
						+ loc.getBlockY() + "§8, §3" + loc.getBlockZ() + "§8]");
				e.getEntity().spigot().respawn();

			}
		}.runTaskLater(Main.getInstance(), 1);
	}

	@Override
	public void onRespawn(PlayerRespawnEvent e) {
		e.getPlayer().sendMessage("onRespawn");
		Location bed = PlayerProperty.readPlayerDataLocation(e.getPlayer().getUniqueId(), "craftAttack.respawnpoint");
		if (bed == null)
			e.setRespawnLocation(getServerWorld().getSpawnLocation());
		else
			e.setRespawnLocation(bed);
	}

	@Override
	public void onBedEnter(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		switch (e.getBedEnterResult()) {

		case NOT_POSSIBLE_HERE:
			p.sendMessage("§cCraft§3Attack§8> §cYou can't set your respawnpoint here!");
			break;
		case NOT_POSSIBLE_NOW:
			p.sendMessage("§cCraft§3Attack§8> §cYou can't set your respawnpoint right now!");
			break;
		case NOT_SAFE:
			p.sendMessage("§cCraft§3Attack§8> §cThere are monsters nearby!");
			break;
		case TOO_FAR_AWAY:
			p.sendMessage("§cCraft§3Attack§8> §cMove a litte closer to your bed!");
			break;
		case OTHER_PROBLEM:
			p.sendMessage("§cCraft§3Attack§8> §cYou can't set your respawnpoint!");
			break;
		case OK:
			PlayerProperty.writePlayerData(p.getUniqueId(), "craftAttack.respawnpoint", e.getBed().getLocation());
			p.sendMessage("§cCraft§3Attack§8> §aRespawnpoint set!");
			break;
		}

	}
}
