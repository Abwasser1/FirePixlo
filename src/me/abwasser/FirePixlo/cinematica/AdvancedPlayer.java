package me.abwasser.FirePixlo.cinematica;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.PluginRule;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.YamlHelper;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.server.DummyServer;
import me.abwasser.FirePixlo.server.Server;
import me.abwasser.FirePixlo.server.ServerManager;
import net.minecraft.server.v1_16_R1.PacketPlayOutCamera;

public class AdvancedPlayer {

	public File file;
	public YamlHelper helper;
	ArrayList<Player> viewers;
	HashMap<Player, Location> startLocs = new HashMap<Player, Location>();
	HashMap<Player, Server> startServer = new HashMap<Player, Server>();
	HashMap<Player, GameMode> startGMS = new HashMap<Player, GameMode>();
	public Record record;

	public boolean paused;
	public boolean stopped;

	public AdvancedPlayer(File file, ArrayList<Player> viewers) {
		this.file = file;
		this.helper = new YamlHelper(file);
		this.viewers = viewers;
		this.paused = true;
		this.stopped = false;
		this.record = new Record(file);
		villager = spawnCamera(EntityType.VILLAGER, viewers.get(0).getLocation());
		creeper = spawnCamera(EntityType.CREEPER, viewers.get(0).getLocation());
		enderman = spawnCamera(EntityType.ENDERMAN, viewers.get(0).getLocation());
		spider = spawnCamera(EntityType.SPIDER, viewers.get(0).getLocation());
		start();

	}

	private void start() {
		for (Player p : viewers) {
			startServer.put(p, ServerManager.getServer(p));
			startGMS.put(p, p.getGameMode());
			startLocs.put(p, p.getLocation());
			startServer.get(p).canUnload(false);
			startServer.get(p).leavePlayer(p, new DummyServer());
			p.teleport(record.getFrame(0).getLoc());
			p.setGameMode(GameMode.SPECTATOR);
		}
		new BukkitRunnable() {
			int currentframe = 0;

			@Override
			public void run() {
				long start = System.currentTimeMillis();
				if (stopped) {
					if (PluginRule.readBoolean(Rules.VERBOSE_DEBUG_OUTPUT))
						V.chat(viewers, "§cAdvanced§3Player", "§eStopped Player");
					cancel();
					for (Player p : viewers)
						startServer.get(p).joinPlayer(p, new DummyServer());
					for (Player p : viewers)
						startServer.get(p).canUnload(true);
					for (Player p : viewers)
						p.teleport(startLocs.get(p));
					for (Player p : viewers)
						p.setGameMode(startGMS.get(p));
					record.clearCache();
					return;
				}
				if (paused)
					return;
				if (PluginRule.readBoolean(Rules.VERBOSE_DEBUG_OUTPUT))
					V.chat(viewers, "§cAdvanced§3Player", "§eShowing Frame #§a" + currentframe);
				Frame frame = record.getFrame(currentframe);
				showFrame(frame);
				if (currentframe >= record.getLegth()) {
					if (PluginRule.readBoolean(Rules.VERBOSE_DEBUG_OUTPUT))
						V.chat(viewers, "§cAdvanced§3Player", "§eFinished");
					stop();
					return;
				}
				if (PluginRule.readBoolean(Rules.VERBOSE_DEBUG_OUTPUT))
					V.chat(viewers, "§cAdvanced§3Player", "§eFinsihed Frame in §a" + V.calcTime(start) + "§ems");
				currentframe++;

			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}

	public void stop() {
		stopped = true;
		for (Player p : viewers) {
			PacketPlayOutCamera packet = new PacketPlayOutCamera();
			V.reflection(packet, "a", p.getEntityId());
			V.sendPacket(p, packet);
		}
		villager.remove();
		creeper.remove();
		enderman.remove();
		spider.remove();
	}

	private Entity villager;
	private Entity creeper;
	private Entity enderman;
	private Entity spider;
	public EntityType currentlyWatching = null;

	public void showFrame(Frame frame) {
		Entity camera;
		switch (frame.entity) {
		case VILLAGER:
			camera = villager;
			break;
		case CREEPER:
			camera = creeper;
			break;
		case ENDERMAN:
			camera = enderman;
			break;
		case SPIDER:
			camera = spider;
			break;
		default:
			camera = villager;
			break;
		}
		System.out.println(frame.entity);
		for (Player p : viewers)
			p.teleport(frame.getLoc());
		camera.teleport(frame.getLoc());

		if (currentlyWatching == null) {
			PacketPlayOutCamera packetCamera = new PacketPlayOutCamera();
			V.reflection(packetCamera, "a", camera.getEntityId());
			V.sendPacket(viewers, packetCamera);
			currentlyWatching = frame.entity;
		}
		if (currentlyWatching != frame.entity) {
			System.out.println("§cSwitching Entity");
			PacketPlayOutCamera packetCamera = new PacketPlayOutCamera();
			V.reflection(packetCamera, "a", camera.getEntityId());
			V.sendPacket(viewers, packetCamera);
			currentlyWatching = frame.entity;
		}
	}

	private Entity spawnCamera(EntityType type, Location loc) {
		LivingEntity e = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
		e.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, false, false, false));
		e.setInvulnerable(true);
		e.setAI(false);
		e.setCollidable(false);
		e.setSilent(true);
		return e;
	}

}
