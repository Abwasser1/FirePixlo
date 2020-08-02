package me.abwasser.FirePixlo.server;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;

import me.abwasser.FirePixlo.L_Detector.L_PressEvent;
import me.abwasser.FirePixlo.Locations;
import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.Perfmon;
import me.abwasser.FirePixlo.Perfmon.Type;
import me.abwasser.FirePixlo.V;
import net.minecraft.server.v1_16_R1.EntitySalmon;

public class ServerListeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onJoin()");
		Player p = e.getPlayer();
		p.teleport(Locations.get(Locations.LOCATION_LOBBY_WAIT));
		new BukkitRunnable() {

			@Override
			public void run() {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 100, false, false, false));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10000, 100, false, false, false));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10000, 100, false, false, false));
				V.preparePlayer(p);
			}
		}.runTaskLater(Main.getInstance(), 10);
		a.stop();
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onQuit()");
		ServerManager.getServer((e.getPlayer())).leavePlayer(e.getPlayer(), ServerManager.lobbyServer);
		a.stop();
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onKick()");
		ServerManager.getServer((e.getPlayer())).leavePlayer(e.getPlayer(), ServerManager.lobbyServer);
		a.stop();
	}

	@EventHandler
	public void evtDeath(PlayerDeathEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtDeath()");
		ServerManager.getServer(e.getEntity()).onDeath(e);
		a.stop();
	}

	@EventHandler
	public void evtDamage(EntityDamageEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtDamage()");
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ServerManager.getServer(p).onDamage(e);
		}
		a.stop();
	}

	@EventHandler
	public void evtBedEnter(PlayerBedEnterEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtBedEnter()");
		ServerManager.getServer((e.getPlayer())).onBedEnter(e);
		a.stop();
	}

	@EventHandler
	public void evtAttack(EntityDamageByEntityEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtAttack()");
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			ServerManager.getServer(p).onAttack(e);
		}
		a.stop();
	}

	@EventHandler
	public void evtBlockBreak(BlockBreakEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtBlockBreak()");
		ServerManager.getServer(e.getPlayer()).onBlockBreak(e);
		a.stop();
	}

	@EventHandler
	public void evtNether(PlayerPortalEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtNether()");
		if (e.getCause() == TeleportCause.NETHER_PORTAL) {
			ServerManager.getServer(e.getPlayer()).onNether(e);
		}
		a.stop();
	}

	@EventHandler
	public void evtEnd(PlayerPortalEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtEnd()");
		if (e.getCause() == TeleportCause.END_PORTAL) {
			ServerManager.getServer(e.getPlayer()).onEnd(e);
		}
		a.stop();
	}

	@EventHandler
	public void evtEnterPortal(EntityPortalEnterEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtEnterPortal()");
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ServerManager.getServer(p).onEnterPortal(e);
		}
		a.stop();
	}

	@EventHandler
	public void evtMove(PlayerMoveEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtMove()");
		ServerManager.getServer(e.getPlayer()).onMove(e);
		a.stop();
	}

	@EventHandler
	public void evtBlockDestroy(BlockDestroyEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtBlockDestroy()");
		ServerManager.getServer(e.getBlock().getWorld()).onBlockDestroy(e);
		a.stop();
	}

	@EventHandler
	public void evtBlockPlace(BlockPlaceEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtBlockPlace()");
		ServerManager.getServer(e.getPlayer()).onBlockPlace(e);
		a.stop();
	}

	@EventHandler
	public void evtChat(AsyncPlayerChatEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtChat()");
		ServerManager.getServer(e.getPlayer()).onChat(e);
		a.stop();
	}

	@EventHandler
	public void evtInteract(PlayerInteractEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtInteract()");
		ServerManager.getServer(e.getPlayer()).onInteract(e);

		a.stop();
	}

	@EventHandler
	public void evtRespawn(PlayerRespawnEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtRespawn()");
		ServerManager.getServer(e.getPlayer()).onRespawn(e);

		a.stop();
	}

	@EventHandler
	public void evtL(L_PressEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtL()");
		ServerManager.getServer(e.getPlayer()).onL(e);
		a.stop();
	}
	
	@EventHandler
	public void evtSpawnEntity(EntitySpawnEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "evtSpawnEntity()");
		ServerManager.getServer(e.getLocation().getWorld()).onEntitySpawn(e);
		a.stop();
	}
	
}
