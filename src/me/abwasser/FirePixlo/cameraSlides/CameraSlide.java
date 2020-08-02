package me.abwasser.FirePixlo.cameraSlides;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.YamlHelper;

public class CameraSlide {

	public Location loc1;
	public Location loc2;
	public int duration;

	public CameraSlide() {
	}

	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}

	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void save(String name) {
		YamlHelper helper = new YamlHelper(new File(Main.homeDir, "CameraSlides/" + name + ".yml"));
		interpolate(new Callback() {

			@Override
			public void call(Location loc, int time) {
				if (loc == null) {
					Bukkit.broadcastMessage("FINISHED!");
					helper.save();
					return;
				}
				helper.writeLocationUNSAFE(""+time, loc);
			}
		});
	}

	public void interpolate(Callback callback) {
		double dx = loc1.getX() - loc2.getX();
		double dy = loc1.getY() - loc2.getY();
		double dz = loc1.getZ() - loc2.getZ();
		double dya = loc1.getYaw() - loc2.getYaw();
		dya += (dya>180) ? -360 : (dya<-180) ? 360 : 0;
		float dpi = (loc1.getPitch()+90) - (loc2.getPitch()+90);

		double cx = loc1.getX();
		double cy = loc1.getY();
		double cz = loc1.getZ();
		float cya = loc1.getYaw();
		float cpi = (loc1.getPitch()+90);

		double multiplier = 1d / (double)duration;
		for (int i = 0; i != duration-1; i++) {
			callback.call(new Location(loc1.getWorld(), cx, cy, cz, cya, cpi-90), i);
			cx -= dx * multiplier;
			cy -= dy * multiplier;
			cz -= dz * multiplier;
			cya -= dya * multiplier;
			cpi -= dpi * multiplier;
			Bukkit.broadcastMessage(
					"Interpolating Frame: #" + i + " | " + cx + "," + cy + "," + cz + "#" + cya + ", " + cpi+"^"+multiplier);
		}
		callback.call(loc2, duration-1);
		
		callback.call(null, 1000000);

	}

	public interface Callback {

		public void call(Location loc, int time);

	}

	
}
