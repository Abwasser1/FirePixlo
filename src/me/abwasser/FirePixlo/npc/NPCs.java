package me.abwasser.FirePixlo.npc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Locations;
import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.server.ServerManager;

public class NPCs {

	public static void init() {
		spawnVaro();
		spawnLaserTag();
		spawnCraftAttack();
		spawnEFT();
		spawnMinigames();
		//particelEffect();
	}

	public static void spawnVaro() {
		NPCRegistry.register(a("npc_lobby_varo", "§cVaro", Skins.KNITE, true), new NPCCallback() {

			@Override
			public void run(NPC npc, Player p, ClickNPCAction action) {
				ServerManager.movePlayer(p, ServerManager.varoServer);

			}
		});
	}

	public static void spawnLaserTag() {
		NPCRegistry.register(a("npc_lobby_lasertag", "§cLaser§7-§3Tag", Skins.LASER_TAG, true), new NPCCallback() {

			@Override
			public void run(NPC npc, Player p, ClickNPCAction action) {
				p.sendMessage("§cNot implemented yet! :(");

			}
		});
	}
	public static void spawnCraftAttack() {
		NPCRegistry.register(a("npc_lobby_craftattack", "§cCraft§3Attack", Skins.RED_KNITE, true), new NPCCallback() {

			@Override
			public void run(NPC npc, Player p, ClickNPCAction action) {
				ServerManager.movePlayer(p, ServerManager.craftAttackServer);

			}
		});
	}
	public static void spawnEFT() {
		NPCRegistry.register(a("npc_lobby_eft", "§cE§3F§cT", Skins.US_SOLDIER, true), new NPCCallback() {

			@Override
			public void run(NPC npc, Player p, ClickNPCAction action) {
				ServerManager.movePlayer(p, ServerManager.eftServer);

			}
		});
	}
	public static void spawnMinigames() {
		NPCRegistry.register(a("npc_lobby_minigames", "§cMini§3Games", Skins.MARIO, true), new NPCCallback() {

			@Override
			public void run(NPC npc, Player p, ClickNPCAction action) {
				p.sendMessage("§cNot implemented yet! :(");

			}
		});
	}
	
	
	
	public static void particelEffect() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
					b("npc_lobby_varo", Particle.LAVA);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	public static NPC a(String loc, String name, Skins skin, boolean track) {
		NPC npc = new NPC(name, Locations.get(loc));
		npc.setSkin(skin);
		npc.lookAtNearbyPlayers(track);
		npc.sendPacket();
		return npc;
	}

	public static NPC a(String loc, String name, String skin, boolean track) {
		NPC npc = new NPC(name, Locations.get(loc));
		npc.setSkin(skin);
		npc.lookAtNearbyPlayers(track);
		npc.sendPacket();
		return npc;
	}
	public static void b(String loc_, Particle particle) {
		Location loc = Locations.get(loc_);
		loc.getWorld().spawnParticle(particle, loc, 2, 0, 0, 0, 1);
	}

}
