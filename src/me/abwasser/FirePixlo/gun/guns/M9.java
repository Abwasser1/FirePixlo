package me.abwasser.FirePixlo.gun.guns;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;
import me.abwasser.FirePixlo.customItems.CustomItems;

public class M9 extends Gun {

	public M9() {
		super(15, 2, 1.5f, 100);
	}

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "m9");
	}

	@Override
	public CustomItem getNewInstace() {
		M9 temporary = new M9();
		CustomItems.register(temporary);
		return temporary;
	}

	@Override
	public int getGunTextureID() {
		return 10;
	}

	@Override
	public String getGunID() {
		return id;
	}

	@Override
	public String getGunName() {
		return "M9";
	}

	@Override
	public void fire(Player p, PlayerInteractEvent e) {
		shotBullet(p, 5, 1, 5, "na");
	}
}
