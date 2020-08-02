package me.abwasser.FirePixlo.npc;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NPCRegistry implements Listener {

	public static ArrayList<NPC> npcs = new ArrayList<>();
	public static HashMap<NPC, NPCCallback> callbacks = new HashMap<>();

	public static void register(NPC npc, NPCCallback callback) {
		npcs.add(npc);
		callbacks.put(npc, callback);
	}

	public static void deleteAll() {
		for (NPC npc : npcs) {
			npc.destroy();
		}
	}

	public static void loadNPCsForWorld(World w, Player p) {
		for (NPC npc : npcs)
			if (npc.loc.getWorld().getName().equalsIgnoreCase(w.getName()))
				npc.sendPacket(p);
	}

	public static void unloadNPCsForWorld(World w, Player p) {
		for (NPC npc : npcs)
			if (npc.loc.getWorld().getName().equalsIgnoreCase(w.getName()))
				npc.destroy(p);
	}

	@EventHandler
	public void onClick(ClickNPC e) {
		if (callbacks.containsKey(e.getNPC())) {
			callbacks.get(e.getNPC()).run(e.getNPC(), e.getPlayer(), e.getAction());
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		for (NPC npc : npcs)
			npc.sendPacket(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		for (NPC npc : npcs)
			npc.destroy(e.getPlayer());
	}

}
