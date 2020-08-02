package me.abwasser.FirePixlo.npc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.abwasser.FirePixlo.Main;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayInUseEntity;

public class PacketReader {

	Channel channel;
	public static Map<UUID, Channel> channels = new HashMap<UUID, Channel>();

	public void inject(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		channel = cp.getHandle().playerConnection.networkManager.channel;
		channels.put(cp.getUniqueId(), channel);

		if (channel.pipeline().get("PacketInjector") != null)
			return;

		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {
			@Override
			protected void decode(ChannelHandlerContext context, PacketPlayInUseEntity packet, List<Object> arg)
					throws Exception {
				arg.add(packet);
				readPacket(cp, packet);

			}
		});
	}
	/*
	 * public void inject(Player p) { CraftPlayer cp = (CraftPlayer) p; channel =
	 * cp.getHandle().playerConnection.networkManager.channel;
	 * channels.put(cp.getUniqueId(), channel);
	 * 
	 * if (channel.pipeline().get("PacketInjector") != null) return;
	 * 
	 * channel.pipeline().addAfter("decoder", "PacketInjector", new
	 * MessageToMessageDecoder<Packet<?>>() {
	 * 
	 * @Override protected void decode(ChannelHandlerContext context, Packet<?>
	 * packet, List<Object> arg) throws Exception { arg.add(packet);
	 * System.out.println(packet);
	 * 
	 * } }); }
	 */

	public void injectSafe(Player p) {
		if (channels.containsKey(p.getUniqueId())) {
			uninject(p);
		}
		inject(p);
	}

	public void uninject(Player p) {
		channel = channels.get(p.getUniqueId());
		if (channel != null)
			if (channel.pipeline().get("PacketInjector") != null)
				channel.pipeline().remove("PacketInjector");

	}

	public void readPacket(Player p, Packet<?> packet) {

		if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
			int id = (int) getValue(packet, "a");
			if (getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")) {
				for (NPC npc : NPCRegistry.npcs) {
					if (npc.npc.getId() == id) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new ClickNPC(p, npc, ClickNPCAction.LEFT_CLICK));
							}
						}, 0);

					}
				}
				return;
			}

			if (getValue(packet, "d").toString().equalsIgnoreCase("OFF_HAND"))
				return;

			if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT"))
				return;

			if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT"))
				for (NPC npc : NPCRegistry.npcs) {
					if (npc.npc.getId() == id) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

							@Override
							public void run() {
								Bukkit.getPluginManager().callEvent(new ClickNPC(p, npc, ClickNPCAction.RIGHT_CLICK));
							}
						}, 0);

					}
				}

		}
	}

	private Object getValue(Object instance, String name) {
		Object result = null;
		try {

			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			result = field.get(instance);
			field.setAccessible(false);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
