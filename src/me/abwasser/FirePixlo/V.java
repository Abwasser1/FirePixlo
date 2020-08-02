package me.abwasser.FirePixlo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonParser;

import eu.endercentral.crazy_advancements.Advancement;
import eu.endercentral.crazy_advancements.AdvancementDisplay;
import eu.endercentral.crazy_advancements.AdvancementDisplay.AdvancementFrame;
import eu.endercentral.crazy_advancements.AdvancementVisibility;
import eu.endercentral.crazy_advancements.NameKey;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.V.PlayerTime.TimeOption;
import me.abwasser.FirePixlo.cmd.CMD_Dev;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import me.abwasser.FirePixlo.gui.Var;
import me.abwasser.FirePixlo.npc.PacketReader;
import me.abwasser.FirePixlo.rank.RankManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayOutAbilities;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_16_R1.PacketPlayOutUpdateTime;
import net.minecraft.server.v1_16_R1.PlayerAbilities;

public class V {

	public static HashMap<Block, Shulker> glowingBlocks = new HashMap<>();

	public static void glowBlock(Block b, ChatColor color, float seconds) {
		Location loc = b.getLocation();
		Shulker shulker = (Shulker) loc.getWorld().spawnEntity(loc, EntityType.SHULKER, SpawnReason.CUSTOM);
		shulker.setInvulnerable(true);
		shulker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 10, false, false, false));
		shulker.setAI(false);
		shulker.setCollidable(false);
		shulker.setSilent(true);
		glowingBlocks.put(b, shulker);
		try {
			Team team = Bukkit.getScoreboardManager().getMainScoreboard()
					.registerNewTeam("Z_S_" + new Random().nextInt(1999999999));

			team.setColor(color);
			team.addEntry(shulker.getUniqueId().toString());

			shulker.setGlowing(true);
			new BukkitRunnable() {

				@Override
				public void run() {
					if (glowingBlocks.containsKey(b)) {
						shulker.setGlowing(false);
						glowingBlocks.remove(b, shulker);
						shulker.remove();
					}
					team.unregister();
				}
			}.runTaskLater(Main.getInstance(), (int) Math.ceil(seconds * 20));
		} catch (Exception e) {
			glowBlock(b, color, seconds);
		}
	}

	public static void chat(CommandSender p, String category, String msg) {
		p.sendMessage("§8| §r§l" + category + " §r§8> §e" + msg);
	}

	public static String chatFormat(String category, String msg) {
		return "§8| §r§l" + category + " §r§8> §e" + msg;
	}

	public static void chat(ArrayList<Player> players, String category, String msg) {
		for (Player p : players)
			chat(p, category, msg);
	}

	public static void chat(CommandSender p, String category, String msg, String perm) {
		if (p.hasPermission(perm))
			p.sendMessage("§8| §r§l" + category + " §r§8> §e" + msg);
	}

	public static boolean validateString(String str) {
		if (str == null || str.equalsIgnoreCase("") || str.equalsIgnoreCase(" ")) {
			return false;
		}
		return true;
	}

	public static void fov(Player p, float fov) {
		PlayerAbilities abilities = new PlayerAbilities();
		try {
			abilities.walkSpeed = fov;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		PacketPlayOutAbilities packet = new PacketPlayOutAbilities(abilities);
		V.sendPacket(p, packet);
	}

	public static String format(AsyncPlayerChatEvent e) {
		RankManager.loadPlayer(e.getPlayer());
		String msg = "";
		String[] args = e.getMessage().split(" ");
		for (String str : args) {
			if (str.startsWith("@")) {
				msg += "§c" + str + "§f ";
				String selector = str.replaceFirst("@", "");
				if (str.length() == 2) {
					selector = str;
				}
				Selector.select(selector, new SelectorCallback() {

					@Override
					public void run(Player p) {
						advMsg(e.getPlayer(), Var.skullBakery(e.getPlayer().getName(), e.getPlayer().getName()),
								"§e" + e.getPlayer().getDisplayName() + " §ementioned you in the chat!");
					}
				});
			} else
				msg += str + " ";
		}
		return e.getPlayer().getDisplayName() + "» "
				+ (e.getPlayer().isOp() ? ChatColor.translateAlternateColorCodes('^', msg) : msg);
	}

	public static int thinChars(String str) {
		int result = 0;
		List<Character> chars = Arrays.asList('i', 'l', 'I', '.', ',', ';', ':');
		for (char c : str.toCharArray()) {
			if (chars.contains(c))
				result++;
		}
		return result;
	}

	public static void reduceDebugInfo(Player p) {
		if(PluginRule.readBoolean(Rules.REDUCE_DEBUG_INFO)) {
			PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(((CraftPlayer) p).getHandle(), (byte) 22);
			sendPacket(p, packet);
		}
	}

	public static void expandDebugInfo(Player p) {
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(((CraftPlayer) p).getHandle(), (byte) 23);
		sendPacket(p, packet);
	}

	public static String padString(String str, int length) {

		int thick = 0;
		List<Character> chars = Arrays.asList('i', 'l', 'I', '.', ',', ';', ':');
		for (char c : str.toCharArray()) {
			if (!chars.contains(c))
				thick++;
		}
		int thin = thinChars(str);
		int res = (int) (thick + Math.floor(thin / 2));
		for (int i = res; i <= length; i++)
			str += " ";
		return str;
	}

	@SuppressWarnings("rawtypes")
	public static void dev(Class clazz, String method, String msg, Level severityLevel) {
		if (CMD_Dev.map.get(severityLevel) != null)
			if (!CMD_Dev.map.get(severityLevel).isEmpty())
				for (Player p : CMD_Dev.map.get(severityLevel))
					p.sendMessage("§8| §r§6§lDEV§r§7>" + severityLevel.getColored() + "§a"
							+ clazz.getName().replace("me.abwasser.FirePixlo.", "") + "§7>§a" + method + "§7>"
							+ severityLevel.getMsgColor() + "" + msg.replace("§r", "§r" + severityLevel.getMsgColor()));
	}

	public static void kick(Player p, String msg) {
		p.kickPlayer(Main.pr + "\n\n" + msg + "\n\n" + Main.pr);
	}

	public static boolean deleteWorld(World world) {
		for (Player p : world.getPlayers()) {
			p.kickPlayer(Main.pr + "\\r\\n\\r\\n§cYoure World was deleted");
		}
		Bukkit.unloadWorld(world, true);
		File f = world.getWorldFolder();
		boolean b = f.delete();
		return b;
	}

	public static void particleHelper(Player p, Particle particle, Location loc, int dx, int dy, int dz, int count,
			float speed) {
		p.spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), 7500, dx, dy, dz, speed);

	}

	public static void particleHelper(World w, Particle particle, Location loc, int dx, int dy, int dz, int count,
			float speed) {
		w.spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), 7500, dx, dy, dz, speed);

	}

	public static void chatPrettyprint(Player p, String msg, int timeInTicks, @Nullable Callback callback) {
		String str = msg;
		int charcount = str.length();
		int charPerTick = charcount / (timeInTicks / 3);

		new BukkitRunnable() {
			int currentChars = 0;

			@Override
			public void run() {
				try {
					currentChars += charPerTick;
					if (str.toCharArray()[currentChars - 1] == '\\' || str.toCharArray()[currentChars - 1] == '§')
						currentChars++;
					if (currentChars > charcount) {
						chatClear(p);
						p.sendMessage(str);
						this.cancel();
						if (callback != null) {
							callback.run(p);
						}
						return;
					}
					chatClear(p);
					p.sendMessage(str.substring(0, currentChars));
				} catch (ArrayIndexOutOfBoundsException e) {
					cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 3);

	}

	public static void chatPrettyprint(Player p, String msg, @Nullable Callback callback) {
		chatPrettyprint(p, msg, (msg.length() * 2), callback);

	}

	public static void chatPrettyprintFAST(Player p, String msg, @Nullable Callback callback) {
		chatPrettyprint(p, msg + " ", (msg.length() * 1), callback);

	}

	public static void chatClear(Player p) {
		for (int i = 0; i != 10; i++) {
			p.sendMessage(" ");
		}
	}

	public static String LocToString(Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float pitch = loc.getPitch();
		float yaw = loc.getYaw();
		return world + ":" + x + ":" + y + ":" + z + ":" + pitch + ":" + yaw;
	}

	public static String fetchUUID(String name) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			return uuid;
		} catch (Exception e) {
		}
		return null;
	}

	public static void injectPlayerSAFE(Player p) {
		try {
			new PacketReader().injectSafe(p);
			new L_Detector().injectSafe(p);
		} catch (Exception e) {
			p.kickPlayer(Main.pr + "\n\n§cSorry, we had an serverside exception!\n§eError while injecting!");
			e.printStackTrace();
		}
	}

	public static void injectPlayer(Player p) {
		try {
			new PacketReader().inject(p);
			new L_Detector().inject(p);
		} catch (Exception e) {
			p.kickPlayer(Main.pr + "\n\n§cSorry, we had an serverside exception!\n§eError while injecting!");
			e.printStackTrace();
		}
	}

	public static void injectPlayer() {
		if (Bukkit.getOnlinePlayers().size() > 0)
			for (Player p : Bukkit.getOnlinePlayers())
				injectPlayer(p);
	}

	public static void uninjectPlayer(Player p) {
		new PacketReader().uninject(p);
		new L_Detector().uninject(p);
	}

	public static void uninjectPlayer() {
		if (Bukkit.getOnlinePlayers().size() > 0)
			for (Player p : Bukkit.getOnlinePlayers())
				uninjectPlayer(p);
	}

	public static KeyValuePair getYawAndPitch(Location from, Location to) {
		double dirx = from.getX() - to.getX();
		double diry = from.getY() - to.getY();
		double dirz = from.getZ() - to.getZ();

		double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

		// to degree
		pitch = pitch * 180.0 / Math.PI;
		yaw = yaw * 180.0 / Math.PI;
		pitch += 60 - from.distance(to) * 8;
		yaw += 90f;
		return new KeyValuePair(yaw, pitch);
	}

	public static String locConverter(Location loc) {
		return loc.getWorld().getName() + "_" + loc.getX() + "_" + loc.getY() + "_" + loc.getZ() + "_" + loc.getYaw()
				+ "_" + loc.getPitch();
	}

	public static Location locConverter(String loc) {
		String[] args = loc.split("_");
		World w = Bukkit.getWorld(args[0]);
		double x = Double.parseDouble(args[1]);
		double y = Double.parseDouble(args[2]);
		double z = Double.parseDouble(args[3]);
		float yaw = Float.parseFloat(args[4]);
		float pitch = Float.parseFloat(args[5]);
		return new Location(w, x, y, z, yaw, pitch);
	}

	public static String getDeathMessage(String deathMessage) {
		String msg = "§c☠ ";
		String[] args = deathMessage.split(" ");
		String player = args[0];
		if (deathMessage.contains("was shot by")) {
			if (args[4].equalsIgnoreCase("Arrow"))
				msg += "§3" + player + " §cwas shot!";
			else {
				msg += "§3" + player + " §cwas shot by §3" + args[4] + "";
				if (args.length > 5)
					msg += "§cusing §3" + args[6];
				else
					msg += "§c!";

			}
		}
		if (deathMessage.contains("was pricked to death"))
			msg += "§3" + player + " §cwas pricked to death by a cactus!";
		if (deathMessage.contains("walked into a cactus whilst trying to escape"))
			msg += "§3" + player + " §cran into a cactus while trying to escape §3" + args[9] + "!";
		return msg;
	}

	public static void tempBossbar(Player p, String msg, BarColor color, int durationInTicks, boolean fill) {
		BossBar b = Bukkit.createBossBar(msg, color, BarStyle.SOLID);
		safeBossbar(p, b);
		new BukkitRunnable() {
			int timer = 0;

			@Override
			public void run() {
				if (timer > durationInTicks) {
					b.removeAll();
					cancel();
					return;
				}

				double process = (double) timer / (double) durationInTicks;
				if (fill)
					b.setProgress(process);
				else
					b.setProgress(1 - process);
				timer++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	@SuppressWarnings("deprecation")
	public static void preparePlayer(Player p) {
		p.setResourcePack(Main.texturePackUrl);
	}

	public static byte[] sha1(String url) {
		File file = new File("tp.zip");
		try {
			FileUtils.copyURLToFile(new URL(url), file);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return sha1(file);
	}

	@SuppressWarnings("resource")
	public static byte[] sha1(File file) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");

			InputStream fis = new FileInputStream(file);
			int n = 0;
			byte[] buffer = new byte[8192];
			while (n != -1) {
				n = fis.read(buffer);
				if (n > 0) {
					digest.update(buffer, 0, n);
				}
			}
			return digest.digest();
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void advMsg(Player p, ItemStack is, String msg) {
		Advancement adv = root("messages", "asd", is, "obsidian", "\n" + msg + "\n", "-", AdvancementFrame.GOAL, true,
				false, AdvancementVisibility.ALWAYS);
		adv.displayToast(p);
	}

	public static Advancement root(String prefix, String name, ItemStack icon, String background, String title,
			String desc, AdvancementFrame frame, boolean toast, boolean broadcast, AdvancementVisibility visibility) {
		AdvancementDisplay rootDisplay = new AdvancementDisplay(icon, title, desc, frame, toast, broadcast, visibility);
		rootDisplay.setBackgroundTexture("textures/block/" + background + ".png");
		Advancement root = new Advancement(null, new NameKey(prefix, name), rootDisplay);
		return root;
	}

	public static Advancement child(String prefix, String name, ItemStack icon, String title, String desc,
			AdvancementFrame frame, boolean toast, boolean broadcast, AdvancementVisibility visibility, float x,
			float y, Advancement parent) {
		AdvancementDisplay childDisplay = new AdvancementDisplay(icon, title, desc, frame, toast, broadcast,
				visibility);
		childDisplay.setPositionOrigin(parent);
		childDisplay.setCoordinates(x, y);
		Advancement kid = new Advancement(parent, new NameKey(prefix, name), childDisplay);
		return kid;
	}

	public static HashMap<Player, BossBar> safeBossbars = new HashMap<>();

	public static void safeBossbar(Player p, BossBar bar) {
		if (safeBossbars.containsKey(p)) {
			safeBossbars.get(p).removePlayer(p);
		}
		bar.addPlayer(p);
		safeBossbars.put(p, bar);
	}

	public static PlayerProfile fake(String name) {
		return new PlayerProfile() {

			@Override
			public void setProperty(ProfileProperty arg0) {

			}

			@Override
			public void setProperties(Collection<ProfileProperty> arg0) {

			}

			@Override
			public String setName(String arg0) {
				return null;
			}

			@Override
			public UUID setId(UUID arg0) {
				return null;
			}

			@Override
			public boolean removeProperty(String arg0) {
				return false;
			}

			@Override
			public boolean isComplete() {
				return false;
			}

			@Override
			public boolean hasProperty(String arg0) {
				return false;
			}

			@Override
			public Set<ProfileProperty> getProperties() {
				return null;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public UUID getId() {
				return null;
			}

			@Override
			public boolean completeFromCache() {
				return false;
			}

			@Override
			public boolean complete(boolean arg0) {
				return false;
			}

			@Override
			public void clearProperties() {

			}

			@Override
			public boolean complete(boolean arg0, boolean arg1) {
				return false;
			}

			@Override
			public boolean completeFromCache(boolean arg0) {
				return false;
			}

			@Override
			public boolean completeFromCache(boolean arg0, boolean arg1) {
				return false;
			}
		};
	}

	@SuppressWarnings("deprecation")
	public static Packet<?> reflection(Packet<?> packet, String fieldName, int value) {
		try {
			Field field = packet.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);// allows us to access the field

			field.setInt(packet, value);// sets the field to an integer
			field.setAccessible(!field.isAccessible());// we want to stop accessing this now
		} catch (Exception e) {
			e.printStackTrace();
			dev(V.class, "reflection()", "Reflection error", Level.ERROR);
		}
		return packet;
	}

	@SuppressWarnings("deprecation")
	public static Packet<?> reflection(Packet<?> packet, String fieldName, byte value) {
		try {
			Field field = packet.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);// allows us to access the field

			field.setByte(packet, value);// sets the field to an integer
			field.setAccessible(!field.isAccessible());// we want to stop accessing this now
		} catch (Exception e) {
			e.printStackTrace();
			dev(V.class, "reflection()", "Reflection error", Level.ERROR);
		}
		return packet;
	}

	public static void sendPacket(Player p, Packet<?> packet) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void sendPacket(ArrayList<Player> players, Packet<?> packet) {
		for (Player p : players)
			sendPacket(p, packet);
	}

	public static HashMap<String, String> createHashMap(KeyValuePair... keyValuePairs) {
		HashMap<String, String> hashMap = new HashMap<>();
		for (KeyValuePair kvp : keyValuePairs) {
			hashMap.put((String) kvp.getKey(), (String) kvp.getValue());
		}
		return hashMap;
	}

	public static long calcTime(long start) {
		return System.currentTimeMillis() - start;
	}

	public static Location between(Location loc1, Location loc2) {
		double X, Y, Z;
		double X1 = loc1.getX();
		double Y1 = loc1.getY();
		double Z1 = loc1.getZ();
		double X2 = loc2.getX();
		double Y2 = loc2.getY();
		double Z2 = loc2.getZ();
		X = (X1 + X2) / 2;
		Y = (Y1 + Y2) / 2;
		Z = (Z1 + Z2) / 2;
		float PITCH, YAW;
		float Pi1 = loc1.getPitch();
		float Ya1 = loc1.getYaw();
		float Pi2 = loc2.getPitch();
		float Ya2 = loc2.getYaw();
		PITCH = Pi1 / 2 + Pi2 / 2 + (Pi1 % 2 + Pi2 % 2) / 2;
		YAW = Ya1 / 2 + Ya2 / 2 + (Ya1 % 2 + Ya2 % 2) / 2;

		return new Location(loc1.getWorld(), X, Y, Z, PITCH, YAW);
	}

	public static void playerTime(Player p, int time, boolean cycle, int cyclespeed) {
		PlayerTime.map.put(p, new TimeOption(time, cycle, cyclespeed));
	}

	public static class PlayerTime {

		public static HashMap<Player, TimeOption> map = new HashMap<Player, V.PlayerTime.TimeOption>();

		public static void loop() {
			new BukkitRunnable() {

				@Override
				public void run() {
					for (Player p : map.keySet()) {
						TimeOption option = map.get(p);
						PacketPlayOutUpdateTime packet = new PacketPlayOutUpdateTime(p.getWorld().getFullTime(),
								(int) Math.floor(option.time), false);
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
						if (option.cycle) {
							float new_time = option.time + option.cyclespeed;
							map.put(p, new TimeOption(new_time, option.cycle, option.cyclespeed));
						}

					}

				}
			}.runTaskTimerAsynchronously(Main.getInstance(), 0, 1);
		}

		public static class TimeOption {

			public float time;
			public boolean cycle;
			public int cyclespeed;

			public TimeOption(float time, boolean cycle, int cyclespeed) {
				super();
				this.time = time;
				this.cycle = cycle;
				this.cyclespeed = cyclespeed;
			}

		}
	}

	public static void createAbwasserMap(Location loc1, Location loc2, Player p) {
		new Thread(new Runnable() {

			@SuppressWarnings({ "deprecation", "static-access" })
			@Override
			public void run() {
				V.chat(p, "§cAbwasser§3Maps", "Calculating Image data");
				int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
				int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

				int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
				int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

				int imageX = maxX - minX;
				int imageZ = maxZ - minZ;

				BufferedImage image = new BufferedImage(imageX, imageZ, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				g.setBackground(new Color(0, 0, 0));
				int counter = 0;
				for (int x = minX; x != maxX; x++)
					for (int z = minZ; z != maxZ; z++) {
						Block b = loc1.getWorld().getHighestBlockAt(x, z);
						Color c = getBlockColor(b.getType());
						image.setRGB(x - minX, z - minZ, c.getRGB());
						String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
						int bx = x, bz = z;
						counter++;
						TextComponent text1 = new TextComponent("Indexing Block [" + bx + "|" + bz + "] --> ");
						TextComponent text2 = new TextComponent("█");
						TextComponent text3 = new TextComponent(" [" + counter + "/" + (imageX * imageZ) + "] "+ ((int)(((double)counter / (double)(imageX * imageZ))*1000))/10+"%");
						text2.setColor(net.md_5.bungee.api.ChatColor.of(hex));
						p.sendMessage(text1, text2, text3);
					}
				try {

					V.chat(p, "§cAbwasser§3Maps", "Saving Image");
					ImageIO.write(image, "png", new File("map_" + Math.random() + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				V.chat(p, "§cAbwasser§3Maps", "Finished");
			}

		}).start();
	}

	/*
	 * 
	 * new TextComponent().fromLegacyText("[\"\",{\"text\":\"Indexing Block [" + bx
	 * + "|" + bz + "] --> \"},{\"text\":\"█\",\"color\":\"" + hex +
	 * "\"},{\"text\":\"[" + counter + "/" + (imageX * imageZ) + "]\"}]")
	 * 
	 * 
	 */
	public static String colorToHex(Color c) {
		return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
	}

	public static Color getBlockColor(Material mat) {
		switch (mat) {
		case GRASS_BLOCK:
			return new Color(91, 135, 49);
		case DIRT:
			return new Color(134, 96, 67);
		case COARSE_DIRT:
			return new Color(119, 85, 59);
		case WATER:
			return new Color(63, 118, 228);
		case PODZOL:
			return new Color(91, 63, 24);
		case SPRUCE_LEAVES:
			return new Color(44, 77, 44);
		case SPRUCE_LOG:
			return new Color(58, 37, 16);
		case SAND:
			return new Color(219, 207, 163);
		case OAK_LOG:
			return new Color(109, 85, 50);
		case SNOW:
			return new Color(249, 254, 254);
		case SNOW_BLOCK:
			return new Color(249, 254, 254);
		case STONE:
			return new Color(125, 125, 125);
		default:
			return new Color(0, 0, 0);
		}
	}

}
