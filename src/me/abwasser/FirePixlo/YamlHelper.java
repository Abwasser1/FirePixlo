package me.abwasser.FirePixlo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlHelper {

	File file;
	YamlConfiguration cfg;

	public YamlHelper(File file) {
		this.file = file;
		this.cfg = YamlConfiguration.loadConfiguration(this.file);
	}

	public File getFile() {
		return file;
	}

	public YamlConfiguration getCfg() {
		return cfg;
	}

	public YamlHelper(String path) {
		if (!path.endsWith(".yml")) {
			path += ".yml";
		}
		this.file = new File(path);
		this.cfg = YamlConfiguration.loadConfiguration(this.file);
	}

	public void write(String path, Object toSave) {
		cfg.set(path, toSave);
		save();
	}

	public void writeUNSAFE(String path, Object toSave) {
		cfg.set(path, toSave);
	}

	public Object read(String path) {
		return cfg.get(path);
	}

	public Integer readInt(String path) {
		return cfg.getInt(path);
	}

	public Float readFloat(String path) {
		return Float.parseFloat(readString(path));
	}

	public Double readDouble(String path) {
		return cfg.getDouble(path);
	}

	public String readString(String path) {
		return cfg.getString(path);
	}
	
	public Boolean readBoolean(String path) {
		return cfg.getBoolean(path);
	}

	// Location
	public void writeLocation(String path, Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		write(path + ".world", world);
		write(path + ".x", x);
		write(path + ".y", y);
		write(path + ".z", z);
		write(path + ".yaw", yaw);
		write(path + ".pitch", pitch);
		save();
	}

	public void writeLocationUNSAFE(String path, Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		writeUNSAFE(path + ".world", world);
		writeUNSAFE(path + ".x", x);
		writeUNSAFE(path + ".y", y);
		writeUNSAFE(path + ".z", z);
		writeUNSAFE(path + ".yaw", yaw);
		writeUNSAFE(path + ".pitch", pitch);
	}

	public Location readLocation(String path) {

		String world = readString(path + ".world");
		double x = readDouble(path + ".x");
		double y = readDouble(path + ".y");
		double z = readDouble(path + ".z");
		float yaw = readFloat(path + ".yaw");
		float pitch = readFloat(path + ".pitch");
		World w = null;
		for (World worlds : Bukkit.getWorlds())
			if (worlds.getName().equals(world))
				w = worlds;
		return new Location(w, x, y, z, yaw, pitch);
	}

	public void save() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
