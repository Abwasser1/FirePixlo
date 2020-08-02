package me.abwasser.FirePixlo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_16_R1.MinecraftKey;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayInAdvancements;
import net.minecraft.server.v1_16_R1.PacketPlayInAdvancements.Status;

public class L_Detector {

	Channel channel;
	public static Map<UUID, Channel> channels = new HashMap<UUID, Channel>();

	public void inject(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		channel = cp.getHandle().playerConnection.networkManager.channel;
		channels.put(cp.getUniqueId(), channel);

		if (channel.pipeline().get("PacketInjector2") != null)
			return;

		channel.pipeline().addAfter("decoder", "PacketInjector2",
				new MessageToMessageDecoder<PacketPlayInAdvancements>() {
					@Override
					protected void decode(ChannelHandlerContext context, PacketPlayInAdvancements packet,
							List<Object> arg) throws Exception {
						arg.add(packet);
						readPacket(cp, packet);

					}
				});
	}

	public void injectSafe(Player p) {
		if (channels.containsKey(p.getUniqueId())) {
			uninject(p);
		}
		inject(p);
	}

	public void uninject(Player p) {
		try {
		channel = channels.get(p.getUniqueId());
		if (channel.pipeline().get("PacketInjector2") != null)
			channel.pipeline().remove("PacketInjector2");
		}catch(NullPointerException e) {}

	}

	public void readPacket(Player p, Packet<?> packet) {

		if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInAdvancements")) {
			PacketPlayInAdvancements.Status status = (Status) getValue(packet, "a");
			if (getValue(packet, "b") != null) {
				MinecraftKey key = (MinecraftKey) getValue(packet, "b");
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

					@Override
					public void run() {
						Bukkit.getPluginManager().callEvent(new L_PressEvent(p, status, key));
					}
				}, 0);
				return;
			}

			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					Bukkit.getPluginManager().callEvent(new L_PressEvent(p, status, null));
				}
			}, 0);
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

	public static class L_PressEvent extends Event {

		private final Player player;
		private final MinecraftKey key;
		private final Status status;

		private static final HandlerList HANDLERS = new HandlerList();

		public L_PressEvent(Player player, Status status, MinecraftKey key) {
			this.player = player;
			this.status = status;
			this.key = key;
		}

		public Player getPlayer() {
			return player;
		}

		public MinecraftKey getKey() {
			return key;
		}

		public Status getStatus() {
			return status;
		}

		@Override
		public HandlerList getHandlers() {
			return HANDLERS;
		}

		public static HandlerList getHandlerList() {
			return HANDLERS;
		}

	}

}
