package me.abwasser.FirePixlo.eft;

import java.io.File;

import org.bukkit.Location;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.YamlHelper;

public class Exit {

	public Location loc;
	public String name;
	public int radius;
	public int heigth;
	public boolean alwaysOpen;
	public int openTime;

	public Exit(Location loc, String name, int radius, int heigth, boolean alwaysOpen, int openTime) {
		this.loc = loc;
		this.name = name;
		this.radius = radius;
		this.heigth = heigth;
		this.alwaysOpen = alwaysOpen;
		this.openTime = openTime;
	}

	Location getLoc() {
		return loc;
	}

	void setLoc(Location loc) {
		this.loc = loc;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	int getRadius() {
		return radius;
	}

	void setRadius(int radius) {
		this.radius = radius;
	}

	int getHeigth() {
		return heigth;
	}

	void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	boolean isAlwaysOpen() {
		return alwaysOpen;
	}

	void setAlwaysOpen(boolean alwaysOpen) {
		this.alwaysOpen = alwaysOpen;
	}

	int getOpenTime() {
		return openTime;
	}

	void setOpenTime(int openTime) {
		this.openTime = openTime;
	}

	public void save() {
		YamlHelper helper = new YamlHelper(new File(Main.homeDir, "EFT/exit_" + Math.random() + ".yml"));
		helper.writeLocation("exit.loc", loc);
		helper.write("exit.name", name);
		helper.write("exit.radius", radius);
		helper.write("exit.heigth", heigth);
		helper.write("exit.alwaysOpen", alwaysOpen);
		helper.write("exit.openTime", openTime);
		helper.save();
	}

	public static Exit fromFile(File file) {
		YamlHelper helper = new YamlHelper(file);
		Location loc = helper.readLocation("exit.loc");
		String name = helper.readString("exit.name");
		int radius = helper.readInt("exit.radius");
		int heigth = helper.readInt("exit.heigth");
		boolean alwaysOpen = helper.readBoolean("exit.alwaysOpen");
		int openTime = helper.readInt("exit.openTime");
		return new Exit(loc, name, radius, heigth, alwaysOpen, openTime);
	}
}
