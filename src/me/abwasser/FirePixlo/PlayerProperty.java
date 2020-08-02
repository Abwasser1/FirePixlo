package me.abwasser.FirePixlo;

import java.io.File;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerProperty {

	public static void writePlayerData(String uuid, String key, Object value) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/PlayerPropertys", uuid + ".yml"));
		helper.write(key, value);
	}

	public static void writePlayerData(UUID uuid, String key, Object value) {
		writePlayerData(uuid.toString(), key, value);
	}

	public static void writePlayerData(Player p, String key, Object value) {
		writePlayerData(p.getUniqueId(), key, value);
	}

	public static void writePlayerData(String uuid, String key, Location loc) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/PlayerPropertys", uuid + ".yml"));
		helper.writeLocation(key, loc);
	}

	public static void writePlayerData(UUID uuid, String key, Location loc) {
		writePlayerData(uuid.toString(), key, loc);
	}

	public static Object readPlayerData(String uuid, String key) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/PlayerPropertys", uuid + ".yml"));
		return helper.read(key);
	}

	public static String readPlayerDataString(String uuid, String key) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/PlayerPropertys", uuid + ".yml"));
		return helper.readString(key);
	}

	public static Object readPlayerData(UUID uuid, String key) {
		return readPlayerData(uuid.toString(), key);
	}

	public static String readPlayerDataString(UUID uuid, String key) {
		return readPlayerDataString(uuid.toString(), key);
	}

	public static Object readPlayerData(Player p, String key) {
		return readPlayerData(p.getUniqueId(), key);
	}

	public static String readPlayerDataString(Player p, String key) {
		return readPlayerDataString(p.getUniqueId(), key);
	}

	public static Location readPlayerDataLocation(String uuid, String key) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/PlayerPropertys", uuid + ".yml"));
		return helper.readLocation(key);
	}

	public static Location readPlayerDataLocation(UUID uuid, String key) {
		return readPlayerDataLocation(uuid.toString(), key);
	}

}
