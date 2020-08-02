package me.abwasser.FirePixlo.eft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cameraSlides.CameraSlide.Callback;
import me.abwasser.FirePixlo.cameraSlides.SlidePlayer;
import me.abwasser.FirePixlo.eft.EFTGameManager.Status;
import me.abwasser.FirePixlo.server.Server;

public class EFTServer extends Server {

	public ArrayList<Player> onlinePlayers = new ArrayList<>();
	public World serverWorld;
	public String name, description, pr;
	public boolean isLoaded = true;
	public boolean canUnload = true;

	public EFTGameManager manager;

	public boolean gameLoopIsRunning = false;

	public EFTServer(World serverWorld, String name, String description, String prefix) {
		this.serverWorld = serverWorld;
		this.name = name;
		this.description = description;
		this.pr = prefix;
		manager = new EFTGameManager();
	}

	@Override
	public ArrayList<Player> getOnlinePlayers() {
		return onlinePlayers;
	}

	@Override
	public void joinPlayer(Player p, Server from) {
		if (manager.status == Status.IN_GAME) {
			from.joinPlayer(p, this);
		}
		if (getOnlinePlayers().isEmpty())
			loadWorld();
		onlinePlayers.add(p);
		clearPlayer(p);
		p.teleport(getServerWorld().getSpawnLocation());
		getServerWorld().setDifficulty(Difficulty.NORMAL);
		serverBroadcast("§a§l+§7» §r§a" + p.getName() + " §ejoined the server!");
		p.setAllowFlight(false);
		V.reduceDebugInfo(p);
		p.setGameMode(GameMode.SPECTATOR);
		if (!gameLoopIsRunning) {
			gameLoopIsRunning = true;
			mainGameLoop();
		}
	}

	@Override
	public void leavePlayer(Player p, Server to) {
		p.hideTitle();
		onlinePlayers.remove(p);
		savePlayer(p);
		p.setAllowFlight(true);
		if (getOnlinePlayers().isEmpty())
			unloadWorld();
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
		return Arrays.asList("Status: " + manager.status);
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
	public void onEntitySpawn(EntitySpawnEvent e) {
		e.setCancelled(false);
	}

	public void mainGameLoop() {
		/*
		 * if (manager.status == Status.WAIT) { for(Player p : getOnlinePlayers()) {
		 * p.sendTitle("§8[§cE§3F§cT§8]", "§7Waiting for other players...", 5, 10000,
		 * 5);
		 * 
		 * } int index = new Random().nextInt(EFTGameManager.waitSlides.length - 1);
		 * SlidePlayer player = new SlidePlayer();
		 * player.play(EFTGameManager.waitSlides[index], new Callback() {
		 * 
		 * @Override public void call(Location loc, int time) { if (loc == null)
		 * mainGameLoop(); } }, getOnlinePlayers().toArray(new
		 * Player[getOnlinePlayers().size()]));
		 * 
		 * }
		 */
	}
}
