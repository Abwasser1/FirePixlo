package me.abwasser.FirePixlo.varo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.PlayerProperty;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.server.Server;
import me.abwasser.FirePixlo.server.ServerManager;

public class VaroServer extends Server {

	public ArrayList<Player> onlinePlayers = new ArrayList<>();
	public World serverWorld;
	public String name, description, pr;
	public boolean isLoaded = true;
	public boolean canUnload = true;
	public VaroManager manager;

	public VaroServer(World serverWorld, String name, String description, String prefix) {
		this.serverWorld = serverWorld;
		this.name = name;
		this.description = description;
		this.pr = prefix;
		this.manager = VaroManager.restore();
	}

	@Override
	public ArrayList<Player> getOnlinePlayers() {
		return onlinePlayers;
	}

	@Override
	public void joinPlayer(Player p, Server from) {
		p.setAllowFlight(false);
		if (!manager.checkPlayerJoin(p)) {
			ServerManager.movePlayerToLobby(p);
			V.chat(p, "§cVaro§3Server", "§cYou cannot join this Server!");
			return;
		}
		manager.addPlayer(p);
		if (getOnlinePlayers().isEmpty())
			loadWorld();
		onlinePlayers.add(p);
		clearPlayer(p);
		if (hasPlayerData(p)) {
			loadPlayer(p);
		} else {
			p.teleport(getServerWorld().getSpawnLocation());
		}
		getServerWorld().setDifficulty(Difficulty.HARD);
		serverBroadcast("§a§l+§7» §r§a" + p.getName() + " §ejoined the server!");
		if (!manager.isOpen)
			manager.startPlayerTimers(p);
		V.expandDebugInfo(p);
	}

	@Override
	public void leavePlayer(Player p, Server to) {
		manager.quit(p);
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
		manager.save();
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
		manager.save();
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
		String open = "isOpen: " + manager.isOpen;
		String rem_time = "remainingTime: " + PlayerProperty.readPlayerData(p, "varo.remainingTime");
		String difficulty = "difficulty: " + serverWorld.getDifficulty().name();
		return Arrays.asList(open, rem_time, difficulty);
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
				leavePlayer(e.getEntity(), ServerManager.lobbyServer);
				e.setDeathMessage(null);
				serverBroadcast("§c☠ " + e.getEntity().getName() + " died!");
				e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
						SoundCategory.MASTER, 10, 0.8f);
				manager.kill(e.getEntity());
				e.getEntity().kickPlayer("§cYou died, therefore you're eliminated from varo!");
			}
		}.runTaskLater(Main.getInstance(), 1);
	}
	
	

}
