package me.abwasser.FirePixlo.lobby;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ParkourManager {

	public static HashMap<Player, Parkour> map = new HashMap<>();

	public static void event(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL) {
			Player p = e.getPlayer();
			if (e.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
				if (map.containsKey(p))
					map.remove(p);
				Parkour parkour = new Parkour(p);
				map.put(p, parkour);
				parkour.start(e.getClickedBlock().getLocation());
			}
			if (e.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
				if (!map.containsKey(p))
					return;
				Parkour parkour = map.get(p);
				parkour.setCheckpoint(e.getClickedBlock().getLocation());
			}
			if (e.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE) {
				if (!map.containsKey(p))
					return;
				Parkour parkour = map.get(p);
				parkour.finish(e.getClickedBlock().getLocation());
			}
		}
	}

}
