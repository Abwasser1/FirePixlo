package me.abwasser.FirePixlo.cinematica;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.YamlHelper;

public class SimpleRecorder {

	public File file;
	public YamlHelper helper;
	public int currentFrame;
	public HashMap<Integer, Frame> map = new HashMap<Integer, Frame>();

	public SimpleRecorder(File file) {
		this.file = file;
		this.helper = new YamlHelper(file);
		this.currentFrame = -1;
	}

	public void capture(Location loc, EntityType entityType) {
		currentFrame++;
		map.put(currentFrame, new Frame(loc, entityType));
	}

	public void finish(@Nullable Callback callback) {
		new BukkitRunnable() {

			@Override
			public void run() {
				long start = System.currentTimeMillis();
				helper.write("rec.meta.length", currentFrame);
				for (int i : map.keySet()) {
					long start_frame = System.currentTimeMillis();
					Frame f = map.get(i);
					helper.writeLocationUNSAFE("rec.track." + i + ".loc", f.loc);
					helper.writeUNSAFE("rec.track." + i + ".entityType", f.entity.name());
					helper.save();
					callback.frameSaved(i, V.calcTime(start_frame));
				}
				if (callback != null)
					callback.finish(file, V.calcTime(start));
			}
		}.runTaskAsynchronously(Main.getInstance());
	}

	public static abstract class Callback {

		public void frameSaved(int frame, long time) {
		}

		public void finish(File file, long time) {
		}

	}

}
