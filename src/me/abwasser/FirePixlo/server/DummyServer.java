package me.abwasser.FirePixlo.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class DummyServer extends Server {

	@Override
	public ArrayList<Player> getOnlinePlayers() {
		return new ArrayList<Player>();
	}

	@Override
	public void joinPlayer(Player p, Server from) {
		p.sendMessage("§cAttention! You have been moved to the §3Dummy§cServer if you are stuck on this Server §7(§eSee Tablist§7)§c type §a/l");
	}

	@Override
	public void leavePlayer(Player p, Server to) {

	}

	@Override
	public String getName() {
		return "Dummy";
	}
	@Override
	public List<String> getServerTab(Player p) {
		return Arrays.asList("§cPlease type§7: §3/l");
	}

	@Override
	public String getPr() {
		return "Dummy";
	}

	@Override
	public void shutdown(Server fallback) {

	}

	@Override
	public void shutdown() {

	}

	@Override
	public String getDescription() {
		return "Dummy Descrpiton";
	}

	@Override
	public boolean isOnline(Player p) {
		return false;
	}

	@Override
	public World getServerWorld() {
		return null;
	}

	@Override
	public void unloadWorld() {

	}

	@Override
	public void loadWorld() {

	}

	@Override
	public boolean isLoaded() {
		return false;
	}

	@Override
	public void canUnload(boolean unload) {
		// TODO Auto-generated method stub
		
	}

}
