package me.abwasser.FirePixlo.challenges;

import org.bukkit.entity.Player;

public class DuoChallenge {

	public Player p1;
	public Player p2;

	public DuoChallenge(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public Player getP1() {
		return p1;
	}

	public Player getP2() {
		return p2;
	}
}
