package me.abwasser.FirePixlo.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.PluginRule;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.craftattack.CraftAttackServer;
import me.abwasser.FirePixlo.eft.EFTServer;
import me.abwasser.FirePixlo.lobby.LobbyServer;
import me.abwasser.FirePixlo.minigames.MiniGamesServer;
import me.abwasser.FirePixlo.varo.VaroServer;

public class ServerManager {

	public static ArrayList<Server> servers = new ArrayList<>();
	public static LobbyServer lobbyServer;
	public static VaroServer varoServer;
	public static CraftAttackServer craftAttackServer;
	public static MiniGamesServer miniGamesServer;
	public static EFTServer eftServer;
	public static HashMap<Player, Long> connections = new HashMap<Player, Long>();

	public static void init() {
		loadWorlds();
		if (Bukkit.getWorld("world_the_end") != null) {
			lobbyServer = new LobbyServer(Bukkit.getWorld("world_the_end"), "lobby", "The main lobby", "§3Lobby");
			a(lobbyServer);
		}
		if (Bukkit.getWorld("varo") != null) {
			varoServer = new VaroServer(Bukkit.getWorld("varo"), "varo", "Varo", "§4Varo");
			a(varoServer);
		}
		if (Bukkit.getWorld("craftAttack") != null) {
			craftAttackServer = new CraftAttackServer(Bukkit.getWorld("craftAttack"), "craftAttack",
					"The CraftAttack server", "§cCraft§3Attack");
			a(craftAttackServer);
		}
		if (Bukkit.getWorld("miniGames") != null) {
			miniGamesServer = new MiniGamesServer(Bukkit.getWorld("miniGames"), "miniGames", "The miniGames server",
					"§cMini§3Games");
			a(miniGamesServer);
		}
		if (Bukkit.getWorld("EFT06") != null) {
			eftServer = new EFTServer(Bukkit.getWorld("EFT06"), "EFT06", "The eft server", "§cE§3F§cT");
			a(eftServer);
		}
		checkLoop();
	}

	private static void a(Server server) {
		server.onInit();
		servers.add(server);
	}

	public static void loadWorlds() {
		if (new File("FireAPI/Yamls/WorldGeneration/a/").exists())
			if (new File("FireAPI/Yamls/WorldGeneration/a/").listFiles().length > 0)
				for (File file : new File("FireAPI/Yamls/WorldGeneration/a/").listFiles()) {
					new WorldCreator(file.getName().replace(".yml", "")).createWorld();
				}
	}

	public static Server getServer(Player p) {
		for (Server server : servers)
			if (server.isOnline(p))
				return server;
		return new DummyServer();
	}

	public static Server getServer(String name) {
		for (Server server : servers)
			if (server.getName().equalsIgnoreCase(name))
				return server;
		return new DummyServer();
	}

	public static Server getServer(World w) {
		for (Server server : servers)
			if (server.getServerWorld().getUID().equals(w.getUID()))
				return server;
		return new DummyServer();
	}

	public static ArrayList<String> getServers() {
		ArrayList<String> list = new ArrayList<>();
		for (Server server : servers)
			list.add(server.getName());
		return list;
	}

	public static void movePlayerToLobby(Player p) {
		movePlayer(p, lobbyServer);
	}

	public static void movePlayer(Player p, Server to) {
		V.tempBossbar(p, "§eMoving you to§8: §f" + to.getPr() + "§e!", BarColor.BLUE, 10, false);
		Server old = getServer(p);
		old.leavePlayer(p, to);
		to.joinPlayer(p, old);
	}

	public static void checkLoop() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (!PluginRule.readBoolean(Rules.BUILD_MODE))
					for (Server server : servers)
						server.checkForMislocatedPlayers();
			}
		}.runTaskTimer(Main.getInstance(), 0, 100);
	}

}
