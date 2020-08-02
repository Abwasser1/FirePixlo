package me.abwasser.FirePixlo.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClickNPC extends Event implements Cancellable {

	private final Player player;
	private final NPC npc;
	private final ClickNPCAction action;
	private boolean isCancelled;

	private static final HandlerList HANDLERS = new HandlerList();

	public ClickNPC(Player player, NPC npc, ClickNPCAction action) {
		this.player = player;
		this.npc = npc;
		this.action = action;
	}

	public Player getPlayer() {
		return player;
	}

	public NPC getNPC() {
		return npc;
	}

	public ClickNPCAction getAction() {
		return action;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
