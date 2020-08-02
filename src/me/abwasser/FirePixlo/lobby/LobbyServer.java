package me.abwasser.FirePixlo.lobby;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;

import me.abwasser.FirePixlo.Debug;
import me.abwasser.FirePixlo.L_Detector.L_PressEvent;
import me.abwasser.FirePixlo.Locations;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.npc.NPCRegistry;
import me.abwasser.FirePixlo.server.Server;
import net.minecraft.server.v1_16_R1.PacketPlayInAdvancements.Status;

public class LobbyServer extends Server {

	public ArrayList<Player> onlinePlayers = new ArrayList<>();
	public World serverWorld;
	public String name, description, pr;
	public boolean isLoaded = true;
	public boolean canUnload = false;

	public LobbyServer(World serverWorld, String name, String description, String prefix) {
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
	public File getLogo() {
		return new File("logos/lobby.png");
	}

	@Override
	public void joinPlayer(Player p, Server from) {
		onlinePlayers.add(p);
		clearPlayer(p);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 255, false, false, false));
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 255, false, false, false));
		p.teleport(Locations.get(Locations.LOCATION_LOBBY));
		Debug.sendBrand(p, "§cYallah§3Cord§7 x §7Abwasser");
		p.setAllowFlight(false);
		serverBroadcast("§a§l+§7» §r§a" + p.getName() + " §ejoined the server!");
		getServerWorld().setDifficulty(Difficulty.PEACEFUL);
		NPCRegistry.loadNPCsForWorld(getServerWorld(), p);
		V.reduceDebugInfo(p);
		V.injectPlayerSAFE(p);
	}

	@Override
	public void leavePlayer(Player p, Server to) {
		p.setAllowFlight(true);
		onlinePlayers.remove(p);
		serverBroadcast("§c§l-§7» §r§c" + p.getName() + " §eleft the server!");
		NPCRegistry.unloadNPCsForWorld(getServerWorld(), p);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getServerTab(Player p) {
		return null;
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
	public boolean isOnline(Player p) {
		return getOnlinePlayers().contains(p);
	}

	@Override
	public World getServerWorld() {
		return serverWorld;
	}

	@Override
	public void unloadWorld() {
		/*
		 * Bukkit.unloadWorld(getServerWorld(), true); isLoaded = false;
		 */
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
	public void onDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onDeath(PlayerDeathEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
			e.setCancelled(true);
	}

	@Override
	public void onBlockDestroy(BlockDestroyEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
			e.setCancelled(true);
	}

	@Override
	public void onAttack(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onNether(PlayerPortalEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onEnd(PlayerPortalEvent e) {
		e.setCancelled(true);
	}

	@Override
	public void onEnterPortal(EntityPortalEnterEvent e) {
		Player p = (Player) e.getEntity();
		p.teleport(Locations.get(Locations.LOCATION_LOBBY), TeleportCause.PLUGIN);
	}

	@Override
	public void onMove(PlayerMoveEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			if (e.getTo().getY() < 115)
				e.getPlayer().teleport(Locations.get(Locations.LOCATION_LOBBY), TeleportCause.PLUGIN);
		} else {
			if (e.getTo().getY() < 0)
				e.getPlayer().teleport(Locations.get(Locations.LOCATION_LOBBY), TeleportCause.PLUGIN);
		}
	}

	@Override
	public void onInteract(PlayerInteractEvent e) {
		ParkourManager.event(e);
	}

	@Override
	public void canUnload(boolean unload) {
		this.canUnload = unload;

	}

	@Override
	public void onL(L_PressEvent e) {
		if (e.getPlayer().isOp()) {
			if (e.getStatus() == Status.OPENED_TAB) {
				e.getPlayer().closeInventory();
				if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
					e.getPlayer().setGameMode(GameMode.CREATIVE);
				else
					e.getPlayer().setGameMode(GameMode.SURVIVAL);
			}
		}
	}
}
