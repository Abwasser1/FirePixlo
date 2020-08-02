package me.abwasser.FirePixlo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.server.Server;
import me.abwasser.FirePixlo.server.ServerManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R1.MinecraftKey;
import net.minecraft.server.v1_16_R1.PacketDataSerializer;
import net.minecraft.server.v1_16_R1.PacketPlayOutCustomPayload;

public class Debug {

	public static String tpsC = "";

	public static List<String> getServerInfo(Player p) {
		Server server = ServerManager.getServer(p);
		List<String> list = server.getServerTab(p);
		if (list == null)
			return null;
		List<String> list2 = new ArrayList<>();
		for (String str : list)
			list2.add("§c" + str.split(":")[0] + "§7:§3" + str.split(":")[1]);
		return list2;
	}

	public static List<String> getBase(Player p) {
		String ping = "§cPing§7: §3" + ((CraftPlayer) p).getHandle().ping;
		String tps = "§cTPS§7: §3" + tpsC;
		String server = "§cServer§7: §3" + ServerManager.getServer(p).getName();
		if (PluginRule.readBoolean(Rules.DO_PERFMON)) {
			String memoryUsage = "§cMemory-Usage§7: §3" + (Runtime.getRuntime().freeMemory() / 1000000) + "/"
					+ (Runtime.getRuntime().maxMemory() / 1000000);
			String cpuCores = "§cCPU-Cores§7: §3" + Runtime.getRuntime().availableProcessors();
			String pluginLag = "§cPlugin-Lag§7: §3"
					+ new DecimalFormat("###.###").format(Perfmon.lagCausedLastSecond / 1000000d) + "ms";
			DecimalFormat df = new DecimalFormat("#.#####");
			String pluginUsage = "§cPlugin-Usage§7: §3" + df.format(Perfmon.lagCausedLastSecond / 10000000d) + "%";
			return Arrays.asList(ping, tps, server, memoryUsage, cpuCores, pluginLag, pluginUsage);
		}
		return Arrays.asList(ping, tps, server);
	}

	public static void updateTPS() {
		double[] arr = Bukkit.getServer().getTPS();
		tpsC = (Math.round(arr[0] * 10d) / 10d) + "";
	}

	public static List<BaseComponent> getLogo(Player p) {
		Server server = ServerManager.getServer(p);
		if (server.getLogo() == null) {
			return null;
		}
		try {
			BufferedImage img = ImageIO.read(server.getLogo());
			List<BaseComponent> liste = new ArrayList<BaseComponent>();
			for (int y = 0; y != img.getHeight(); y++) {

				for (int x = 0; x != img.getWidth(); x++) {
					Color c = new Color(img.getRGB(x, y));
					String hex = V.colorToHex(c);
					TextComponent comp = new TextComponent("█");
					comp.setColor(ChatColor.of(hex));
					liste.add(comp);
				}
				liste.add(new TextComponent("\n"));
			}
			return liste;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void run() {
		new BukkitRunnable() {

			@Override
			public void run() {
				Debug.updateTPS();
				for (Player p : Bukkit.getOnlinePlayers()) {
					ArrayList<String> lines = new ArrayList<String>();
					for (String str : getBase(p))
						lines.add(str);
					if (getServerInfo(p) != null)
						for (String str : getServerInfo(p))
							lines.add(str);
					String data = "";
					for (String str : lines)
						data += str + "\n §r";
					data += "\n";
					List<BaseComponent> liste = new ArrayList<BaseComponent>();
					for (BaseComponent dataComp : new ChatHelper(data).convert())
						liste.add(dataComp);
					if (getLogo(p) != null)
						for (BaseComponent logoComp : getLogo(p))
							liste.add(logoComp);

					p.setPlayerListHeaderFooter(new ChatHelper(Main.pr).convert(),
							liste.toArray(new BaseComponent[liste.size()]));
					lines.clear();
				}
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
	}

	public static void sendBrand(Player p, String data) {
		Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "minecraft:brand");
		ByteBuf bytebuf = Unpooled.buffer();
		writeString(data, bytebuf);
		PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(new MinecraftKey("minecraft:brand"),
				new PacketDataSerializer(bytebuf));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(Main.getInstance(), "minecraft:brand");
	}

	public static void writeString(String s, ByteBuf buf) {
		if (s.length() > 32767) {
			throw new IllegalArgumentException(
					String.format("Cannot send string longer than Short.MAX_VALUE (got %s characters)",
							new Object[] { Integer.valueOf(s.length()) }));
		}

		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		writeVarInt(b.length, buf);
		buf.writeBytes(b);
	}

	public static void writeVarInt(int value, ByteBuf output) {
		do {
			int part = value & 0x7F;

			value >>>= 7;
			if (value != 0) {
				part |= 0x80;
			}

			output.writeByte(part);
		} while (value != 0);
	}

}
