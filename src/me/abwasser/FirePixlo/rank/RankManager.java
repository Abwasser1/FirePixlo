package me.abwasser.FirePixlo.rank;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.YamlHelper;
import me.abwasser.FirePixlo.cmd.CMD_Dev;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;

public class RankManager {

	public static String pathToFile = "rank.yml";
	public static Rank defaultRank = Rank.PLAYER;

	public static void savePlayer(Player p, Rank rank) {
		File file = new File(pathToFile);
		YamlHelper helper = new YamlHelper(file);
		helper.write(p.getUniqueId().toString(), rank.name());
		helper.save();
	}

	public static Rank getPlayer(Player p) {
		File file = new File(pathToFile);
		YamlHelper helper = new YamlHelper(file);
		String str = helper.readString(p.getUniqueId().toString());
		if (V.validateString(str)) {
			return Rank.valueOf(str.toUpperCase());
		}
		savePlayer(p, Rank.PLAYER);
		return Rank.PLAYER;
	}

	public static void loadPlayer(Player p) {
		Rank rank = getPlayer(p);
		p.setDisplayName(rank.prefix + "" + p.getName() + "" + rank.suffix);
		p.setPlayerListName(rank.prefix + "" + p.getName() + "" + rank.suffix);
		if(rank == Rank.DEV) 
			CMD_Dev.a(Level.ERROR, p);
	}

	public static boolean isInRank(Player p, Rank rank) {
		Rank r = getPlayer(p);
		if (r == rank)
			return true;
		return false;
	}

	public static ArrayList<UUID> getAllInRank(Rank rank) {
		File file = new File(pathToFile);
		YamlHelper helper = new YamlHelper(file);
		ArrayList<UUID> uuids = new ArrayList<>();
		for (String key : helper.getCfg().getKeys(false)) {
			String rankname = helper.readString(key);
			if (rankname.equalsIgnoreCase(rank.name())) {
				uuids.add(UUID.fromString(key));
			}
		}
		return uuids;
	}

}
