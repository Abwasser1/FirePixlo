package me.abwasser.FirePixlo.cinematica;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.ItemListener;
import me.abwasser.FirePixlo.ItemListener.Callback;
import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.YamlHelper;
import me.abwasser.FirePixlo.gui.Var;

public class AdvancedRecorder {

	public File file;
	public YamlHelper helper;
	public Player p;
	public SimpleRecorder recorder;

	public ItemStack[] inv_contents;
	public boolean paused;
	public boolean stopped;
	public EntityType entityType;
	public int speed;

	public AdvancedRecorder(File file, Player p) {
		this.file = file;
		this.helper = new YamlHelper(file);
		this.p = p;
		this.recorder = new SimpleRecorder(file);
		this.paused = true;
		this.stopped = false;
		this.entityType = EntityType.VILLAGER;
		this.speed = 1;
		start();
	}

	private void start() {
		new BukkitRunnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				if (Main.getInstance().getServer().getTPS()[0] < 18)
					V.chat(p, "§cAdvanced§3Recorder", "§cWarning the server is only running at §e"
							+ Main.getInstance().getServer().getTPS()[0] + "§c TPS!");
				if (stopped) {
					cancel();
					return;
				}
				if (paused)
					return;

				if (recorder.currentFrame % speed == 0) {
					recorder.capture(p.getLocation(), entityType);
					V.chat(p, "§cAdvanced§3Recorder",
							"§eCaptured Frame #§a" + recorder.currentFrame + "§e in §a" + V.calcTime(start) + "§ems!");
				}
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
		if (!stopped)
			recorder.finish(new me.abwasser.FirePixlo.cinematica.SimpleRecorder.Callback() {
				@Override
				public void frameSaved(int frame, long time) {
					V.chat(p, "§cAdvanced§3Recorder", "§eSave Frame #§a" + frame + "§e in §a" + time + "§ems!");
				}

				@Override
				public void finish(File file, long time) {
					V.chat(p, "§cAdvanced§3Recorder",
							"§eSaved Recording #§a" + file.getName() + "§e in §a" + time + "§ems!");
					V.chat(p, "§cAdvanced§3Recorder", "§eSize: " + file.length() / 1000 + "KB");
				}
			});
		stopped = true;
	}

	public void equipPlayer() {
		inv_contents = p.getInventory().getContents();
		p.getInventory().clear();
		ItemStack is_start = ItemListener
				.markItemStack(Var.itemBakery(Material.GREEN_WOOL, "§aRecord", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						resume();
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
		ItemStack is_pause = ItemListener
				.markItemStack(Var.itemBakery(Material.YELLOW_WOOL, "§cPause", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						pause();
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
		ItemStack is_stop = ItemListener.markItemStack(Var.itemBakery(Material.RED_WOOL, "§cStop", 1, (short) 0, ""),
				new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						stop();
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
		ItemStack is_shader_player = ItemListener.markItemStack(
				Var.itemBakery(Material.PLAYER_HEAD, "§ePlayer-Shader", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						entityType = EntityType.VILLAGER;
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
		ItemStack is_shader_enderman = ItemListener.markItemStack(
				Var.itemBakery(Material.ENDER_PEARL, "§eEnderman-Shader", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						entityType = EntityType.ENDERMAN;
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
		ItemStack is_shader_creeper = ItemListener.markItemStack(
				Var.itemBakery(Material.CREEPER_HEAD, "§eCreeper-Shader", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						entityType = EntityType.CREEPER;
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
		ItemStack is_shader_spider = ItemListener
				.markItemStack(Var.itemBakery(Material.COBWEB, "§eSpider-Shader", 1, (short) 0, ""), new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						entityType = EntityType.SPIDER;
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});

		p.getInventory().setItem(0, is_start);
		p.getInventory().setItem(1, is_pause);
		p.getInventory().setItem(2, is_stop);
		p.getInventory().setItem(3, is_shader_player);
		p.getInventory().setItem(4, is_shader_creeper);
		p.getInventory().setItem(5, is_shader_enderman);
		p.getInventory().setItem(6, is_shader_spider);
		p.getInventory().setItem(7, getSpeed1xClock(p, 7));
	}

	public void restorePlayer() {
		p.getInventory().setContents(inv_contents);
	}

	private ItemStack getSpeed1xClock(Player p, int slot) {
		return ItemListener.markItemStack(Var.itemBakery(Material.CLOCK, "§3Speed: 1x", 1, (short) 0, ""),
				new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						speed = 2;
						p.getInventory().setItem(slot, getSpeed2xClock(p, slot));
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
	}
	private ItemStack getSpeed2xClock(Player p, int slot) {
		return ItemListener.markItemStack(Var.itemBakery(Material.CLOCK, "§3Speed: 2x", 1, (short) 0, ""),
				new Callback() {
					@Override
					public void call(PlayerInteractEvent e) {
						speed = 1;
						p.getInventory().setItem(slot, getSpeed1xClock(p, slot));
					}

					@Override
					public void call(PlayerDropItemEvent e) {
						e.setCancelled(true);
					}

					@Override
					public void call(PlayerSwapHandItemsEvent e) {
						e.setCancelled(true);
					}
				});
	}

}
