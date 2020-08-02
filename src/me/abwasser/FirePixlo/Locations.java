package me.abwasser.FirePixlo;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Location;

public class Locations {

	public static final String LOCATION_LOBBY = "lobby";
	public static final String LOCATION_LOBBY_WAIT = "lobby_wait";

	public static File dir = new File("FirePixlo/Locations/");

	public static void save(String name, Location loc) {
		File file = new File(dir, name + ".yml");
		YamlHelper helper = new YamlHelper(file);
		helper.writeLocation("Location.", loc);
		helper.save();
	}

	public static Location get(String name) {
		File file = new File(dir, name + ".yml");
		YamlHelper helper = new YamlHelper(file);
		return helper.readLocation("Location");
	}
	public static void delete(String name) {
		File file = new File(dir, name + ".yml");
		file.delete();
	}

	public static ArrayList<String> points() {
		ArrayList<String> list = new ArrayList<>();
		for (File f : dir.listFiles())
			list.add(f.getName().replace(".yml", ""));
		return list;

	}

}
