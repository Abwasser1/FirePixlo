package me.abwasser.FirePixlo.npc;

import org.bukkit.entity.Player;

public abstract class NPCCallback {

	public abstract void run(NPC npc, Player p, ClickNPCAction action);
	
}
