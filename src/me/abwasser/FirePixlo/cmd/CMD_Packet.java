package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.abwasser.FirePixlo.KeyValuePair;
import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.MinecraftKey;
import net.minecraft.server.v1_16_R1.PacketDataSerializer;
import net.minecraft.server.v1_16_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_16_R1.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_16_R1.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_16_R1.PacketPlayOutGameStateChange.a;
import net.minecraft.server.v1_16_R1.PacketPlayOutKickDisconnect;
import net.minecraft.server.v1_16_R1.PacketPlayOutUpdateTime;

public class CMD_Packet implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			return Arrays.asList("gameStateChange", "animation", "disconnect", "plugin-channel:brand", "timeUpdate");
		} else if (index == 1) {
			if (args[0].equalsIgnoreCase("gameStateChange"))
				return Selector.getTabCompeltion();
			else if (args[0].equalsIgnoreCase("animation"))
				return Selector.getTabCompeltion();
			else if (args[0].equalsIgnoreCase("disconnect"))
				return Selector.getTabCompeltion();
			else if (args[0].equalsIgnoreCase("plugin-channel:brand"))
				return Selector.getTabCompeltion();
			else if (args[0].equalsIgnoreCase("timeUpdate"))
				return Selector.getTabCompeltion();

		} else if (index == 2) {
			if (args[0].equalsIgnoreCase("gameStateChange"))
				return PacketPlayOutGameStateChangeEnum.getTabCompletion();
			else if (args[0].equalsIgnoreCase("animation")) {
				List<String> list = Selector.getTabCompeltion();
				list.add("@e[r=5]");
				return list;

			} else if (args[0].equalsIgnoreCase("disconnect"))
				return Arrays.asList("%reason%");
			else if (args[0].equalsIgnoreCase("plugin-channel:brand"))
				return Arrays.asList("%brand%");
			else if (args[0].equalsIgnoreCase("timeUpdate"))
				return Arrays.asList("sunrise", "noon", "sunset", "midnight", "%zahl%", "normal");

		} else if (index == 3) {
			if (args[0].equalsIgnoreCase("gameStateChange")) {
				PacketPlayOutGameStateChangeEnum enumm = PacketPlayOutGameStateChangeEnum
						.valueOf(args[2].toUpperCase());
				return Arrays.asList(enumm.suboptions.keySet().toArray(new String[0]));
			} else if (args[0].equalsIgnoreCase("animation")) {
				return PacketPlayOutAnimationEnum.getTabCompletion();
			}
		} else if (index == 4) {
			if (args[0].equalsIgnoreCase("animation")) {
				return Arrays.asList("%times%");
			}
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Recieved §e" + label + "§r from §e" + sender.getName(), Level.VERBOSE);
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("disconnect")) {
				Selector.select(args[1], new SelectorCallback() {

					@Override
					public void run(Player p) {
						PacketPlayOutKickDisconnect packet = new PacketPlayOutKickDisconnect(
								IChatBaseComponent.ChatSerializer.a("{\"text\":\""
										+ ChatColor.translateAlternateColorCodes('^', args[2]).replace('_', ' ')
										+ "\"}"));
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
					}
				});

			}
			if (args[0].equalsIgnoreCase("plugin-channel:brand")) {

				Selector.select(args[1], new SelectorCallback() {

					@Override
					public void run(Player p) {
						Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "minecraft:brand");
						ByteBuf bytebuf = Unpooled.buffer();
						writeString(args[2], bytebuf);
						PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(
								new MinecraftKey("minecraft:brand"), new PacketDataSerializer(bytebuf));
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
						Bukkit.getMessenger().unregisterOutgoingPluginChannel(Main.getInstance(), "minecraft:brand");
					}
				});

			}
			if (args[0].equalsIgnoreCase("timeUpdate")) {

				Selector.select(args[1], new SelectorCallback() {

					@Override
					public void run(Player p) {
						int time;
						switch (args[2]) {
						case "sunrise":
							time = 0;
							break;
						case "noon":
							time = 6000;
							break;
						case "sunset":
							time = 12000;
							break;
						case "midnight":
							time = 18000;
							break;
						default:
							time = Integer.parseInt(args[2]);
							break;
						}
						PacketPlayOutUpdateTime packet = new PacketPlayOutUpdateTime(p.getWorld().getFullTime(), time,
								false);
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
					}
				});

			}

		} else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("gameStateChange")) {
				PacketPlayOutGameStateChangeEnum option = PacketPlayOutGameStateChangeEnum
						.valueOf(args[2].toUpperCase());

				Selector.select(args[1], new SelectorCallback() {

					@Override
					public void run(Player p) {
						String suboption = "";
						try {
							suboption = Float.parseFloat(args[3]) + "";
						} catch (NumberFormatException e) {
							suboption = option.suboptions.get(args[3].toLowerCase());
						}
						PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(new a(option.option),
								Float.parseFloat(suboption));
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
					}
				});

			}

		} else if (args.length == 5) {
			if (args[0].equalsIgnoreCase("animation")) {
				PacketPlayOutAnimationEnum option = PacketPlayOutAnimationEnum.valueOf(args[3].toUpperCase());
				int loops = Integer.parseInt(args[4]);
				new BukkitRunnable() {
					int counter = 0;

					@Override
					public void run() {
						if (counter >= loops) {
							cancel();
							return;
						}
						counter++;
						Selector.select(args[1], new SelectorCallback() {

							@Override
							public void run(Player p) {
								if (args[2].startsWith("@e")) {
									int radius = Integer.parseInt(args[2].replace("@e[r=", "").replace("]", ""));
									for (Entity entity : p.getWorld().getNearbyLivingEntities(p.getLocation(),
											radius)) {
										PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
										V.reflection(packet, "a", entity.getEntityId());
										V.reflection(packet, "b", option.option);
										((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
									}
								} else {
									Selector.select(args[2], new SelectorCallback() {
										@Override
										public void run(Player t) {
											PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
											V.reflection(packet, "a", t.getEntityId());
											V.reflection(packet, "b", option.option);
											((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
										}
									});
								}
							}
						});
					}
				}.runTaskTimer(Main.getInstance(), 0, 1);
			}

		} else {
			chat(sender, "§4Syntax", "§e/packet §7Tab");
		}
		return false;
	}

	public static enum PacketPlayOutGameStateChangeEnum {

		INVALID_BED("invalid_bed", 0, V.createHashMap(new KeyValuePair("default", "0"))),
		END_RAINING("end_raining", 1, V.createHashMap(new KeyValuePair("default", "0"))),
		BEGIN_RAINING("begin_raining", 2, V.createHashMap(new KeyValuePair("default", "0"))),
		CHANGE_GAMEMODE("change_gamemode", 3,
				V.createHashMap(new KeyValuePair("survival", "0"), new KeyValuePair("creative", "1"),
						new KeyValuePair("adventure", "2"), new KeyValuePair("spectator", "3"))),
		EXIT_END("exit_end", 4,
				V.createHashMap(new KeyValuePair("no_credits", "0"), new KeyValuePair("show_credits", "1"))),
		DEMO_MESSAGE("demo_message", 5,
				V.createHashMap(new KeyValuePair("show_welcome_screen", "0"),
						new KeyValuePair("tell_movement_controls", "101"), new KeyValuePair("tell_jump_control", "102"),
						new KeyValuePair("tell_inventory_control", "103"), new KeyValuePair("demo_over", "104"))),
		ARROW_HITTING_PLAYER("arrrow_hitting_player", 6, V.createHashMap(new KeyValuePair("default", "0"))),
		FADE_VALUE("fade_value", 7, V.createHashMap(new KeyValuePair("%zahl%", "0.5"))),
		FADE_TIME("fade_time", 8, V.createHashMap(new KeyValuePair("%zahl%", "0.5"))),
		PLAY_PUFFERFISH_STING_SOUND("play_pufferfish_sting_sound", 9,
				V.createHashMap(new KeyValuePair("default", "0"))),
		PLAY_ELDER_GUARDIAN_MOB_APPEARANCE("play_elder_guardian_mob_appearance", 10,
				V.createHashMap(new KeyValuePair("default", "0"))),
		ENABLE_RESPAWN_SCREEN("enable_respawn_screen", 11,
				V.createHashMap(new KeyValuePair("enabled", "0"), new KeyValuePair("disabled", "1")));

		public String name;
		public int option;
		public HashMap<String, String> suboptions;

		private PacketPlayOutGameStateChangeEnum(String name, int option, HashMap<String, String> suboptions) {
			this.name = name;
			this.option = option;
			this.suboptions = suboptions;
		}

		public static ArrayList<String> getTabCompletion() {
			ArrayList<String> list = new ArrayList<>();
			for (PacketPlayOutGameStateChangeEnum changeEnum : PacketPlayOutGameStateChangeEnum.values())
				list.add(changeEnum.name().toLowerCase());
			return list;
		}
	}

	public static enum PacketPlayOutAnimationEnum {

		SWING_MAIN_ARM("swing_main_arm", 0), TAKE_DAMAGE("take_damage", 1), LEAVE_BED("leave_bed", 2),
		SWING_OFFHAND("swing_offhand", 3), CRITICAL_EFFECT("critical_effect", 4),
		MAGICAL_CRITICAL_EFFECT("magical_critical_effect", 5);

		public String name;
		public int option;

		private PacketPlayOutAnimationEnum(String name, int option) {
			this.name = name;
			this.option = option;
		}

		public static ArrayList<String> getTabCompletion() {
			ArrayList<String> list = new ArrayList<>();
			for (PacketPlayOutAnimationEnum animationEnum : PacketPlayOutAnimationEnum.values())
				list.add(animationEnum.name().toLowerCase());
			return list;
		}
	}

	private void writeString(String s, ByteBuf buf) {
		if (s.length() > 32767) {
			throw new IllegalArgumentException(
					String.format("Cannot send string longer than Short.MAX_VALUE (got %s characters)",
							new Object[] { Integer.valueOf(s.length()) }));
		}

		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		writeVarInt(b.length, buf);
		buf.writeBytes(b);
	}

	private void writeVarInt(int value, ByteBuf output) {
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
