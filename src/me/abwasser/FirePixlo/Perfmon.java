package me.abwasser.FirePixlo;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.TableGenerator.Alignment;
import me.abwasser.FirePixlo.TableGenerator.Receiver;

public class Perfmon {

	public static long lagCausedThisTick = 0;
	public static long lagCausedThisSecond = 0;
	public static long lagCausedLastSecond = 0;
	private static int second = 0;

	long start;
	Type type;
	Class<?> clazz;
	String methode;

	public Perfmon(Type type, Class<?> clazz, String methode) {
		this.start = System.nanoTime();
		this.type = type;
		this.clazz = clazz;
		this.methode = methode;
	}

	public void stop() {
		if (!PluginRule.readBoolean(Rules.DO_PERFMON))
			return;
		
		long time = System.nanoTime() - start;
		String a = getColor(time / 1000000d);
		String str = "§e" + type.name() + "§7>§e" + clazz.getSimpleName() + "§7.§e" + methode + "§7> §a";
		for (Player p : Bukkit.getOnlinePlayers()) {
			TableGenerator gen = new TableGenerator(Alignment.LEFT, Alignment.RIGHT, Alignment.RIGHT);
			gen.addRow("Lorem ficksum ich hasse diesen Server so friggn sehr kappa", "§a0000000000§ens",
					"§a00.00§ems ");
			double ms = time / 1000000d;
			gen.addRow(str, a + time + "§ens", a + new DecimalFormat("##.##").format(ms) + "§ems");
			V.chat(p, "§cPerf§3mon", gen.generate(Receiver.CLIENT, true, true).get(1));

		}
		save(type, clazz, methode, time);
		lagCausedThisTick += time;
		addToSec(time);

	}

	public static void tickend() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.sendActionBar(V.chatFormat("§cPerf§3mon", "§cLag caused this tick§7: §c" + lagCausedThisTick
					+ "§ens §8| §c" + new DecimalFormat("##.##").format(lagCausedThisTick / 1000000d) + "§ems§c!"));
		lagCausedThisTick = 0;
	}

	public static String getColor(double ms) {
		if (ms >= 2d) {
			return "§4";
		} else if (ms >= 1d) {
			return "§c";
		} else if (ms >= 0.5d) {
			return "§6";
		} else if (ms >= 0.1d) {
			return "§e";
		}
		return "§a";
	}

	public static void addToSec(long time) {
		if (second != (int) Math.floor(System.currentTimeMillis() / 1000)) {
			second = (int) Math.floor(System.currentTimeMillis() / 1000);
			lagCausedLastSecond = lagCausedThisSecond;
			lagCausedThisSecond = 0;
		}
		lagCausedThisSecond += time;
	}

	public static void save(Type type, Class<?> clazz, String methode, long time) {

	}

	public enum Type {
		EVENT, LOOP;
	}
}
