package me.abwasser.FirePixlo.rank;

public enum Rank {

	ADMIN("§7[§4§lAdmin§r§7] §a", "§r"), DEV("§7[§3§lDEV§r§7] §3", "§r"), BUILDER("§7[§d§lBuilder§r§7] §3", "§r"),
	VIP("§7[§6§lVIP§r§7] §2", "§r"), PLAYER("§e", "§r");

	String prefix;
	String suffix;

	private Rank(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}

}
