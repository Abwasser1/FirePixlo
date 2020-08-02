package me.abwasser.FirePixlo.npc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.abwasser.FirePixlo.KeyValuePair;
import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import net.minecraft.server.v1_16_R1.DataWatcher;
import net.minecraft.server.v1_16_R1.DataWatcherObject;
import net.minecraft.server.v1_16_R1.DataWatcherRegistry;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.MinecraftServer;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R1.PlayerInteractManager;
import net.minecraft.server.v1_16_R1.WorldServer;

public class NPC {

	public String name;
	public EntityPlayer npc;
	public Location start_loc;
	public Location loc;
	public HashMap<Integer, Byte> entityMeta = new HashMap<Integer, Byte>();
	public String skin = "";

	public NPC(String name, Location loc) {
		this.name = name;
		this.loc = loc;
		this.start_loc = loc;
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
		npc.ping = 0;
		entityMeta.put(16, (byte) 127);// macht den 2. Layer an, kappa!
		teleport(loc);
	}

	public void setSkin(String name) {
		skin = name;
		String[] properties = getSkin(name);
		npc.getProfile().getProperties().put("textures", new Property("textures", properties[0], properties[1]));
	}

	public void setSkin(Skins skin) {
		this.skin = "___" + skin.name();
		npc.getProfile().getProperties().put("textures",
				new Property("textures", skin.getTexture(), skin.getSignature()));
	}

	private String[] getSkin(String name) {
		String uuid = V.fetchUUID(name);
		if (uuid == null)
			return null;
		try {
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader = new InputStreamReader(url.openStream());
			JsonObject property = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray()
					.get(0).getAsJsonObject();
			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();
			return new String[] { texture, signature };
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void teleport(Location loc) {
		this.loc = loc;
		npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

	}

	public List<Packet<?>> getPackets() {
		return Arrays.asList(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc),
				new PacketPlayOutNamedEntitySpawn(npc),
				new PacketPlayOutEntityMetadata(npc.getId(), getDataWatcher(), true),
				new PacketPlayOutEntityHeadRotation(npc, (byte) (loc.getYaw() * 256 / 360)));

	}

	public DataWatcher getDataWatcher() {
		DataWatcher watcher = npc.getDataWatcher();
		for (Integer key : entityMeta.keySet())
			watcher.set(new DataWatcherObject<>(key, DataWatcherRegistry.a), entityMeta.get(key));
		return watcher;
	}

	public void sendPacket(Player p) {
		for (Packet<?> packet : getPackets())
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public void sendPacket() {
		for (Player p : Bukkit.getOnlinePlayers())
			sendPacket(p);
	}

	public void destroy(Player p) {
		V.sendPacket(p, new PacketPlayOutEntityDestroy(npc.getId()));
		V.sendPacket(p, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
	}

	public void destroy() {
		for (Player p : Bukkit.getOnlinePlayers())
			destroy(p);
	}

	public boolean isLookingAtNearbyPlayers = false;

	public void lookAtNearbyPlayers(boolean bool) {
		isLookingAtNearbyPlayers = bool;
		lookAtNearbyPlayerRunnable();
	}

	private void lookAtNearbyPlayerRunnable() {
		NPC npc = this;
		new BukkitRunnable() {

			@Override
			public void run() {
				if (!npc.isLookingAtNearbyPlayers) {
					cancel();
					return;
				}
				for (Player p : loc.getWorld().getNearbyPlayers(npc.loc, 8.1)) {
					new BukkitRunnable() {

						@Override
						public void run() {
							if (loc.distance(p.getLocation()) > 8) {
								float yaw = start_loc.getYaw();
								float pitch = start_loc.getPitch();
								npc.teleport(
										new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, pitch));
								PacketPlayOutEntityTeleport entityTeleport = new PacketPlayOutEntityTeleport(npc.npc);
								PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation(
										npc.npc, (byte) (loc.getYaw() * 256 / 360));
								V.sendPacket(p, entityTeleport);
								V.sendPacket(p, entityHeadRotation);
								return;
							}
							KeyValuePair pair = V.getYawAndPitch(loc, p.getEyeLocation());
							float yaw = (float) ((double) pair.key);
							float pitch = (float) ((double) pair.value);
							npc.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, pitch));
							PacketPlayOutEntityTeleport entityTeleport = new PacketPlayOutEntityTeleport(npc.npc);
							PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation(
									npc.npc, (byte) (loc.getYaw() * 256 / 360));
							V.sendPacket(p, entityTeleport);
							V.sendPacket(p, entityHeadRotation);
						}
					}.runTaskAsynchronously(Main.getInstance());
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

	public String serialize() {
		int id = npc.getId();
		String loc_ = V.locConverter(loc);
		boolean tracking = isLookingAtNearbyPlayers;
		return name + "_#_" + id + "_#_" + loc_ + "_#_" + skin + "_#_" + tracking;
	}

	public static NPC deserialize(String str) {
		String[] args = str.split("_#_");
		String name = args[0];
		// int id = Integer.parseInt(args[1]); //NOT-USED
		Location loc = V.locConverter(args[2]);
		String skin = args[3];
		boolean tracking = Boolean.parseBoolean(args[4]);
		NPC npc = new NPC(name, loc);
		if (!skin.isEmpty())
			npc.setSkin(skin);

		if (tracking)
			npc.lookAtNearbyPlayers(tracking);
		return npc;
	}

}
