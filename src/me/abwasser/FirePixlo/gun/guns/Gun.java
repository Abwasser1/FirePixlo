package me.abwasser.FirePixlo.gun.guns;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.customItems.CustomItem;
import me.abwasser.FirePixlo.customItems.CustomItems;

public abstract class Gun extends CustomItem {

	public static void registerAllGuns() {
		CustomItems.register(new M9());
	}

	public static HashMap<Player, Boolean> aimingTable = new HashMap<>();

	public Gun(int magSize, float rps, float reloadTime, int zoom) {
		writeVariable("#" + id, "magSize", magSize); // int
		writeVariable("#" + id, "roundsInMeg", magSize); // int
		writeVariable("#" + id, "rps", rps); // float
		writeVariable("#" + id, "reloadTime", reloadTime); // float
		writeVariable("#" + id, "zoom", zoom); // int
		writeVariable("#" + id, "calcDeltaShot", (1000f / rps)); // float
		writeVariable("#" + id, "lastShot", 0l); // long
	}

	@Override
	public void registerRecipe() {

	}

	@Override
	public abstract NamespacedKey getNamespacedKey();

	public abstract int getGunTextureID();

	public abstract String getGunID();

	public abstract String getGunName();

	@Override
	public ItemStack getItem() {
		return itemBakery(Material.GOLDEN_HOE, getGunTextureID(), "§r" + getGunName(), 1, getNamespacedKey().getKey(),
				"#" + getNewInstace().id);
	}

	@Override
	public void onRightClick(PlayerInteractEvent e) {
		e.setCancelled(true);
		String id = getId(e.getItem());
		if (readInt(id, "roundsInMeg") > 0) {
			if (System.currentTimeMillis() - readLong(id, "lastShot") >= readFloat(id, "calcDeltaShot")) {
				fire(e.getPlayer(), e);
				int rim = readInt(id, "roundsInMeg");
				writeVariable(id, "roundsInMeg", (rim - 1));
				showAmmoAsLevel(e.getPlayer(), rim, readInt(id, "magSize"));
				writeVariable(id, "lastShot", System.currentTimeMillis());
			} else {
				Bukkit.broadcastMessage("COOLDOWN");// TODO REMOVE
			}
		} else {
			e.getPlayer().sendActionBar("§cPlease reload first! §7(§eLeft-Click§7)");
		}
	}

	public abstract void fire(Player p, PlayerInteractEvent e);

	@Override
	public void onLeftClickBlock(PlayerInteractEvent e) {
		onLeftClick(e);
	}

	@Override
	public void onLeftClick(PlayerInteractEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();
		String id = getId(e.getItem());
		p.sendActionBar("§cReloading...");
		int time = (int) ((int) (readFloat(id, "reloadTime") * 20f))
				* (1 - (readInt(id, "roundsInMeg") / readInt(id, "magSize")));
		p.setCooldown(e.getItem().getType(), time);
		new BukkitRunnable() {

			@Override
			public void run() {
				p.sendActionBar("§aReloaded!");
				writeVariable(id, "roundsInMeg", readInt(id, "magSize"));
				showAmmoAsLevel(e.getPlayer(), readInt(id, "roundsInMeg"), readInt(id, "magSize"));
			}
		}.runTaskLater(Main.getInstance(), time);
	}

	@Override
	public void onSwitchToOffhand(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
		aim(e.getPlayer());
	}

	public void aim(Player p) {
		if (!aimingTable.containsKey(p))
			aimingTable.put(p, false);
		if (aimingTable.get(p))
			V.fov(p, 0.1f);
		else
			 V.fov(p, readFloat(getId(p.getInventory().getItemInMainHand()), "zoom"));
	}

	@Override
	public void onUnselect(PlayerItemHeldEvent e) {
		e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
	}

	@Override
	public void onSelect(PlayerItemHeldEvent e) {
		String id = getId(e.getPlayer().getInventory().getItemInMainHand());
		showAmmoAsLevel(e.getPlayer(), readInt(id, "roundsInMeg"), readInt(id, "magSize"));
	}

	public void shotBullet(Player p, int damage, float velocity, int spread, String action) {
		Arrow pr = p.getWorld().spawnArrow(p.getEyeLocation(), p.getLocation().getDirection().multiply(velocity),
				velocity, spread);
		pr.setCustomName("§7BULLET>" + damage + ">" + action);
	}

	public void showAmmoAsLevel(Player p, int rim, int mag) {
		if (rim >= 0)
			p.setLevel(rim);
		if (rim >= 1) {
			float exp = (float) ((float) rim) / (float) mag;
			if (exp <= 1 && exp >= 0)
				p.setExp(exp);
		} else {
			p.setExp(0);
		}
	}

	public void writeVariable(String lore, String name, Object value) {
		writeData(Material.GOLDEN_HOE, lore, "Gun." + name, value + "");
	}

	public String readVariable(String lore, String name) {
		System.out.println(name + ": " + readDataString(Material.GOLDEN_HOE, lore, "Gun." + name));
		return readDataString(Material.GOLDEN_HOE, lore, "Gun." + name);
	}

	public Integer readInt(String lore, String name) {
		return Integer.parseInt(readVariable(lore, name));
	}

	public Float readFloat(String lore, String name) {
		return Float.parseFloat(readVariable(lore, name));
	}

	public Long readLong(String lore, String name) {
		return Long.parseLong(readVariable(lore, name));
	}

	public Boolean readBoolean(String lore, String name) {
		return Boolean.parseBoolean(readVariable(lore, name));
	}

}
