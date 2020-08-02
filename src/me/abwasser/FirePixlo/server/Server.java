package me.abwasser.FirePixlo.server;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;

import io.netty.channel.unix.Socket;
import me.abwasser.FirePixlo.L_Detector.L_PressEvent;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.YamlHelper;

public abstract class Server {

	public File getServerFolder() {
		return new File("Servers/" + getName() + "/");
	}

	public abstract ArrayList<Player> getOnlinePlayers();

	public abstract void joinPlayer(Player p, Server from);

	public abstract void leavePlayer(Player p, Server to);

	public abstract String getName();

	public abstract List<String> getServerTab(Player p);

	public abstract String getPr();

	public abstract void shutdown(Server fallback);

	public abstract void shutdown();

	public abstract String getDescription();

	public abstract boolean isOnline(Player p);

	public abstract World getServerWorld();

	public abstract void unloadWorld();

	public abstract void loadWorld();

	public abstract boolean isLoaded();

	public abstract void canUnload(boolean unload);

	public void onInit() {
	}

	public void clearPlayer(Player p) {
		p.getInventory().clear();
		p.setLevel(0);
		p.setExp(0);
		for (PotionEffect pe : p.getActivePotionEffects())
			p.removePotionEffect(pe.getType());
		p.setHealth(20);
		p.setHealthScale(20);
		p.setFoodLevel(20);
		p.setGlowing(false);
		p.setGameMode(GameMode.ADVENTURE);
		p.setInvulnerable(false);
		p.getInventory().setHeldItemSlot(0);
	}

	public void savePlayer(Player p) {
		File cfg = new File(getServerFolder(), "PlayerData/" + p.getUniqueId().toString() + ".yml");
		if (cfg.exists())
			cfg.delete();
		YamlHelper helper = new YamlHelper(cfg);
		helper.writeLocation("Location", p.getLocation());
		helper.write("HealthScale", p.getHealthScale());
		helper.write("Health", p.getHealth());
		helper.write("FoodLevel", p.getFoodLevel());
		helper.write("Saturation", p.getSaturation());
		helper.write("Gamemode", p.getGameMode().name());
		helper.write("ActiveSlot", p.getInventory().getHeldItemSlot());
		helper.write("Exp.levels", p.getLevel());
		helper.write("Exp.points", p.getExp());
		ItemStack[] inv = p.getInventory().getContents();
		for (int i = 0; i < inv.length; i++) {
			ItemStack item = inv[i];
			if (item == null)
				helper.write("Inv." + i, "empty");
			else
				helper.write("Inv." + i, item);
		}
		PotionEffect[] effecte = p.getActivePotionEffects().toArray(new PotionEffect[0]);
		for (int i = 0; i < effecte.length; i++) { // start iterating into the inv
			PotionEffect effect = effecte[i]; // getting the itemstack
			if (effect == null)
				helper.write("Effects." + i, "empty"); // if it's a null itemstack, we save
														// it as a string
			else
				helper.write("Effects." + i, effect); // else, we save the itemstack
		}
		helper.save();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH_mm_ss");
			Date date = Calendar.getInstance().getTime();
			String datum = sdf.format(date);
			FileUtils.copyFile(helper.getFile(),
					new File(getServerFolder(), "PlayerData/Backup/" + datum + "/" + helper.getFile().getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void loadPlayer(Player p) {
		File cfg = new File(getServerFolder(), "PlayerData/" + p.getUniqueId().toString() + ".yml");
		YamlHelper helper = new YamlHelper(cfg);
		Location loc = helper.readLocation("Location");
		p.teleport(loc);
		double healthScale = helper.readDouble("HealthScale");
		p.setHealthScale(healthScale);
		double health = helper.readDouble("Health");
		p.setHealth(health);
		int foodlevel = helper.readInt("FoodLevel");
		p.setFoodLevel(foodlevel);
		float saturation = helper.readFloat("Saturation");
		p.setSaturation(saturation);
		String gamemode = helper.readString("Gamemode");
		p.setGameMode(GameMode.valueOf(gamemode));
		int activeSlot = helper.readInt("ActiveSlot");
		p.getInventory().setHeldItemSlot(activeSlot);
		int levels = helper.readInt("Exp.levels");
		p.setLevel(levels);
		float points = helper.readFloat("Exp.points");
		p.setExp(points);
		helper.write("Exp.points", p.getExp());
		ConfigurationSection cs = helper.getCfg().getConfigurationSection("Inv");
		if (cs != null) {
			List<ItemStack> items = new ArrayList<>();
			for (String key : cs.getKeys(false)) {
				Object o = cs.get(key);
				if (o instanceof ItemStack)
					items.add((ItemStack) o);
				else
					items.add(null);
			}
			ItemStack[] inv = items.toArray(new ItemStack[0]);
			p.getInventory().setContents(inv);
		}
		ConfigurationSection cs2 = helper.getCfg().getConfigurationSection("Effects");
		if (cs2 != null) {
			List<PotionEffect> effects = new ArrayList<>();
			for (String key : cs2.getKeys(false)) {
				Object o = cs2.get(key);
				if (o instanceof PotionEffect)
					effects.add((PotionEffect) o);
				else
					effects.add(null);
			}
			p.addPotionEffects(effects);
		}
	}

	public void deletePlayer(Player p) {
		File cfg = new File(getServerFolder(), "PlayerData/" + p.getUniqueId().toString() + ".yml");
		cfg.delete();
	}

	public void deletePlayers() {
		for (File cfg : new File(getServerFolder(), "PlayerData/").listFiles())
			cfg.delete();
	}

	public boolean hasPlayerData(Player p) {
		return new File(getServerFolder(), "PlayerData/" + p.getUniqueId().toString() + ".yml").exists();
	}

	public void checkForMislocatedPlayers() {
		for (Player p : getOnlinePlayers()) {
			if (!p.getWorld().getName().equalsIgnoreCase(getServerWorld().getName())) {
				if (p.isOp()) {
					V.chat(p, "§cServer§3Manager",
							"§cWarning! You are in the wrong world, please type §e/server join <SERVER>§c!\n"
									+ "§ePlease report this issue to the Developer§7(§3Abwasser§7) §8[§e#ERR_MIS_POS§8] "
									+ p.getWorld().getName() + " -> " + getServerWorld().getName());
					continue;
				}
				leavePlayer(p, ServerManager.lobbyServer);
				V.chat(p, "§cServer§3Manager",
						"§cError! Your have been moved to the Lobby as a temporary fix!\n"
								+ "§ePlease report this issue to the Developer§7(§3Abwasser§7) §8[§e#ERR_MIS_POS§8] "
								+ p.getWorld().getName() + " -> " + getServerWorld().getName());

				ServerManager.lobbyServer.joinPlayer(p, this);
			}
		}
	}

	public void serverBroadcast(String str) {
		for (Player p : getOnlinePlayers())
			p.sendMessage(str);
	}
	
	public File getLogo() {
		return null;
	}

	public void onDeath(PlayerDeathEvent e) {
	}

	public void onDamage(EntityDamageEvent e) {
	}

	public void onAttack(EntityDamageByEntityEvent e) {
	}

	public void onBlockBreak(BlockBreakEvent e) {
	}

	public void onBlockDestroy(BlockDestroyEvent e) {
	}

	public void onBlockPlace(BlockPlaceEvent e) {
	}

	public void onMove(PlayerMoveEvent e) {
	}

	public void onNether(PlayerPortalEvent e) {
	}

	public void onEnd(PlayerPortalEvent e) {
	}

	public void onChat(AsyncPlayerChatEvent e) {
		serverBroadcast(V.format(e));
		e.setCancelled(true);
	}

	public void onEnterPortal(EntityPortalEnterEvent e) {

	}

	public void onInteract(PlayerInteractEvent e) {
	}

	public void onRespawn(PlayerRespawnEvent e) {
	}

	public void onL(L_PressEvent e) {
	}

	public void onBedEnter(PlayerBedEnterEvent e) {

	}
	
	public void onEntitySpawn(EntitySpawnEvent e) {
		
	}

	/*
	 * public void updateTablist(Player p) { for (Player player :
	 * Bukkit.getOnlinePlayers()) V.sendPacket(p, new
	 * PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)
	 * player).getHandle())); for (Player player : getOnlinePlayers())
	 * V.sendPacket(p, new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
	 * ((CraftPlayer) player).getHandle())); }
	 */

}
