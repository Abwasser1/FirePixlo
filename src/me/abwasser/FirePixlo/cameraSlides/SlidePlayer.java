package me.abwasser.FirePixlo.cameraSlides;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.PluginRule;
import me.abwasser.FirePixlo.YamlHelper;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.cameraSlides.CameraSlide.Callback;

public class SlidePlayer {

	public HashMap<Integer, Location> buffer;
	public int index = 0;
	public int playbackSpeed = 1;

	public SlidePlayer() {
		buffer = new HashMap<>();
	}

	public void play(String name, Player... players) {
		play(name, new Callback() {

			@Override
			public void call(Location loc, int time) {
			}

		}, players);
	}

	public void play(String name, Callback callback, Player... players) {
		File file = new File(Main.homeDir, "CameraSlides/" + name + ".yml");
		if (buffer.isEmpty()) {
			buffer(file);
		}
		new BukkitRunnable() {

			@Override
			public void run() {
				if (!buffer.containsKey(index)) {
					cancel();
					buffer.clear();
					callback.call(null, index);
					return;
				}
				Location loc = buffer.get(index);
				if (PluginRule.readBoolean(Rules.VERBOSE_DEBUG_OUTPUT))
					Bukkit.broadcastMessage("Showing #" + index + "...");
				callback.call(loc, index);
				for (Player p : players)
					p.teleport(loc);
				index++;
			}
		}.runTaskTimer(Main.getInstance(), 0, playbackSpeed);

	}

	public void buffer(File file) {
		YamlHelper helper = new YamlHelper(file);
		for (String key : helper.getCfg().getKeys(false)) {
			if (PluginRule.readBoolean(Rules.VERBOSE_DEBUG_OUTPUT))
				Bukkit.broadcastMessage("Buffering #" + key + "...");
			buffer.put(Integer.parseInt(key), helper.readLocation(key));
		}
	}
}
