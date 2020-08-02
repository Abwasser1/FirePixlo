package me.abwasser.FirePixlo.varo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.abwasser.FirePixlo.PlayerProperty;
import me.abwasser.FirePixlo.YamlHelper;

public class VaroManager {

	public boolean isOpen = true;
	public ArrayList<String> allowed = null;
	public static File saveFile;
	public static HashMap<String, BukkitTask> runnabels = new HashMap<>();
	public static HashMap<String, BossBar> bossbars = new HashMap<>();

	public static int time = 900;

	public static void init() {
		saveFile = new File("FirePixlo/Varo/settings.yml");
	}

	public VaroManager(boolean isOpen, ArrayList<String> allowed) {
		this.isOpen = isOpen;
		this.allowed = allowed;
	}

	public boolean checkPlayerJoin(Player p) {
		if (isOpen) {
			PlayerProperty.writePlayerData(p, "varo.remainingTime", time);
			return true;
		} else if (allowed.contains(p.getUniqueId().toString())) {
			if (PlayerProperty.readPlayerData(p, "varo.remainingTime") == null) {
				PlayerProperty.writePlayerData(p, "varo.remainingTime", time);
				return true;
			} else if (((int) PlayerProperty.readPlayerData(p, "varo.remainingTime")) > 10) {
				return true;
			}
		}
		return false;
	}

	public void addPlayer(Player p) {
		if (!allowed.contains(p.getUniqueId().toString()))
			allowed.add(p.getUniqueId().toString());
	}

	public void quit(Player p) {
		if (bossbars.get(p.getUniqueId().toString()) != null)
			bossbars.get(p.getUniqueId().toString()).removeAll();

		if (runnabels.get(p.getUniqueId().toString()) != null)
			runnabels.get(p.getUniqueId().toString()).cancel();
	}

	public void reset() {
		saveFile.delete();
		isOpen = true;
		allowed = new ArrayList<>();
	}

	public void save() {
		YamlHelper helper = new YamlHelper(saveFile);
		helper.write("settings.isOpen", isOpen);
		if (new File(saveFile, "Players").listFiles() != null)
			for (File file : new File(saveFile, "Players").listFiles())
				file.delete();
		for (String str : allowed) {
			new YamlHelper(new File(saveFile, "Players/" + str)).write("allowed", true);
		}

	}

	public void kill(Player p) {
		allowed.remove(p.getUniqueId().toString());
		
	}

	public static VaroManager restore() {
		if (saveFile.exists()) {
			YamlHelper helper = new YamlHelper(saveFile);
			boolean isOpen = (boolean) helper.read("settings.isOpen");
			ArrayList<String> list = new ArrayList<>();
			if (new File(saveFile, "Players").listFiles() != null)
				for (File file : new File(saveFile, "Players").listFiles())
					list.add(file.getName().replace(".yml", ""));
			return new VaroManager(isOpen, list);
		} else
			return new VaroManager(true, new ArrayList<String>());

	}

	public void start() {
		VaroTiming.startVaro();
	}

	public void startPlayerTimers(Player p) {
		VaroTiming.startPlayer(p);
	}

}
