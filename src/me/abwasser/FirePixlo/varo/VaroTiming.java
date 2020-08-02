package me.abwasser.FirePixlo.varo;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.destroystokyo.paper.Title;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.PlayerProperty;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.server.ServerManager;

public class VaroTiming {

	public static void startPlayer(Player p) {
		if (((int) PlayerProperty.readPlayerData(p, "varo.remainingTime")) < VaroManager.time - 100) {
			startKickTimer(p);
			return;
		}
		try {
			p.setInvulnerable(true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 100, false, false, false));
			BossBar indicator = Bukkit.createBossBar("§cInvurnability §e10s", BarColor.RED, BarStyle.SEGMENTED_10);
			indicator.addPlayer(p);
			new BukkitRunnable() {
				int timer = 10;

				@Override
				public void run() {
					if ((double) ((double) timer / (double) (10d)) >= 0
							&& (double) ((double) timer / (double) (10d)) <= 1)
						indicator.setProgress((double) ((double) timer / (double) (10d)));
					indicator.setTitle("§cInvurnability §e" + timer + "s");
					ServerManager.varoServer
							.serverBroadcast("§e" + p.getName() + "§c will be attackable in §e" + timer + "s");
					p.playSound(p.getEyeLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
					if (timer == 0) {
						indicator.removeAll();
						ServerManager.varoServer.serverBroadcast("§e" + p.getName() + "§cis now attackable");
						p.setInvulnerable(false);
						startKickTimer(p);
						p.playSound(p.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.4f);
						cancel();
					}
					timer--;
				}
			}.runTaskTimer(Main.getInstance(), 0, 20);
		} catch (Exception e) {
			p.kickPlayer("§cAn error occured during you startTimer\n§e" + e.getLocalizedMessage());
		}
	}

	public static void startKickTimer(Player p) {
		try {
			BossBar indicator = Bukkit.createBossBar("§cError-Code§7: §e#BUKKIT_RUN_NOT_STARTED", BarColor.GREEN,
					BarStyle.SOLID);
			indicator.addPlayer(p);
			VaroManager.bossbars.put(p.getUniqueId().toString(), indicator);
			VaroManager.runnabels.put(p.getUniqueId().toString(), new BukkitRunnable() {
				int timer = ((int) PlayerProperty.readPlayerData(p, "varo.remainingTime"));

				@Override
				public void run() {
					double percent = (double) ((double) timer / (double) VaroManager.time);
					if (percent >= 0 && percent <= 1)
						indicator.setProgress(percent);
					if (timer > 60) {
						indicator.setTitle(
								"§c" + (int) (Math.floor(timer / 60)) + "m " + (timer % 60) + "s §eremaining");
					} else {
						indicator.setTitle("§c" + timer + "s §eremaining");
					}

					if (timer <= 10) {
						indicator.setColor(BarColor.PINK);
						ServerManager.varoServer
								.serverBroadcast("§e" + p.getName() + "§c will be kicked in " + timer + "s");
						p.playSound(p.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1, (float) timer / 10f);
					} else if (timer <= 30) {
						indicator.setColor(BarColor.RED);
					} else if (timer <= 60) {
						indicator.setColor(BarColor.YELLOW);
					}
					if (timer == 0) {
						ServerManager.movePlayerToLobby(p);
						ServerManager.varoServer.serverBroadcast("§e" + p.getName() + "§c was kicked from the server!");
						indicator.removeAll();
						cancel();
						return;
					}
					if (timer == 30) {
						ServerManager.varoServer.serverBroadcast("§e" + p.getName() + "§c will be kicked in 30s");
					}
					if (timer == 60) {
						ServerManager.varoServer.serverBroadcast("§e" + p.getName() + "§c will be kicked in 60s");
					}
					timer--;
					PlayerProperty.writePlayerData(p, "varo.remainingTime", timer);
				}
			}.runTaskTimer(Main.getInstance(), 0, 20));
		} catch (Exception e) {
			p.kickPlayer("§cAn error occured during you kickTimer\n§e" + e.getLocalizedMessage());
		}
	}

	public static void startVaro() {
		ServerManager.varoServer.manager.isOpen = false;
		for (Player p : ServerManager.varoServer.getOnlinePlayers())
			p.setGameMode(GameMode.ADVENTURE);
		new BukkitRunnable() {
			int timer = 15;

			@Override
			public void run() {
				for (Player p : ServerManager.varoServer.getOnlinePlayers()) {
					if (timer > 4) {
						p.sendTitle(new Title(ServerManager.varoServer.pr, "§eStarting in §a" + timer + "§e seconds!"));
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
					}
					if (timer == 4) {
						p.sendTitle(new Title(ServerManager.varoServer.pr, "§eStarting in §e" + timer + "§e seconds!"));
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0.8f);
					}
					if (timer == 3) {
						p.sendTitle(new Title(ServerManager.varoServer.pr, "§eStarting in §6" + timer + "§e seconds!"));
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0.6f);
					}
					if (timer == 2) {
						p.sendTitle(new Title(ServerManager.varoServer.pr, "§eStarting in §c" + timer + "§e seconds!"));
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0.4f);
					}
					if (timer == 1) {
						p.sendTitle(new Title(ServerManager.varoServer.pr, "§eStarting in §4" + timer + "§e second!"));
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0.2f);
					}
					if (timer == 0) {
						p.hideTitle();
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0f);
						p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.7f, 0.6f);
						p.setHealth(20);
						p.setFoodLevel(20);
						p.setGameMode(GameMode.SURVIVAL);
						p.setInvulnerable(false);
						PlayerProperty.writePlayerData(p, "varo.remainingTime", VaroManager.time);
						V.chatPrettyprint(p, ServerManager.varoServer.pr + "§a Good Luck!", null);
						startKickTimer(p);
					}
				}
				timer--;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
}
