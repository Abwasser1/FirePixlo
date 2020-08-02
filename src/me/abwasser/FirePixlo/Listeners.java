package me.abwasser.FirePixlo;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffectType;

import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;

import me.abwasser.FirePixlo.Perfmon.Type;
import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.cmd.Selector;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import me.abwasser.FirePixlo.customItems.CraftBlocker;
import me.abwasser.FirePixlo.npc.PacketReader;
import me.abwasser.FirePixlo.rank.RankManager;
import me.abwasser.FirePixlo.server.ServerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Listeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onJoin()");
		RankManager.loadPlayer(e.getPlayer());
		PacketReader reader = new PacketReader();
		reader.inject(e.getPlayer());
		a.stop();
	}

	@EventHandler
	public void onResourcePack(PlayerResourcePackStatusEvent e) {
		Player p = e.getPlayer();
		switch (e.getStatus()) {

		case ACCEPTED:
			p.sendMessage("§aThanks for downloading our Texture pack!");
			V.tempBossbar(p, "§aDownloading...", BarColor.YELLOW, 120, true);
			break;
		case DECLINED:
			p.kickPlayer(Main.pr
					+ "\n\n§cYou decliened our Texturepack!\n\n§cInstructions to accept them:\n§31§7)§e If you haven't already added it to your Serverlist add it now!\n§32§7)§eSelect it by single clicking!\n§33§7)§eClick \"Edit\"\n§34§7)§eClick on \"Server Resource Packs: Disabled\"\n§35§7)§eJoin again an accept the Texturepack this time!");
			break;
		case FAILED_DOWNLOAD:
			p.kickPlayer(Main.pr
					+ "\n\n§cYou download failed, sorry for the inconvenience!\n\n§eIf this problem persists please contact an Developer!");
			break;
		case SUCCESSFULLY_LOADED:	
			
			p.sendMessage("§aWe recommend using Optifine, it is necessary for adding CustomArmors, without it you will a serious handicap!");
			TextComponent textComponent = new TextComponent("§l§8[§6§nDOWNLOAD§r§l§8]");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder("§6Download Optifine here!").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
					"https://drive.google.com/uc?export=download&id=1knFIhQ_Y80fMFi4sqZ7s1pwMJj8UDZHX"));
			p.sendMessage(textComponent);
			p.removePotionEffect(PotionEffectType.SLOW);
			p.removePotionEffect(PotionEffectType.BLINDNESS);
			p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
			ServerManager.movePlayerToLobby(p);
			break;
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onQuit()");
		PacketReader reader = new PacketReader();
		reader.uninject(e.getPlayer());
		a.stop();
	}

	@EventHandler
	public void hideShulker(BlockBreakEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "hideShulker()");
		if (V.glowingBlocks.containsKey(e.getBlock())) {
			V.glowingBlocks.get(e.getBlock()).setGlowing(false);
			V.glowingBlocks.get(e.getBlock()).remove();
		}
		a.stop();
	}

	@EventHandler
	public void onJump(PlayerJumpEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onJump()");
		e.getPlayer().sendMessage("Server: " + ServerManager.getServer(e.getPlayer()).getName());
		a.stop();
	}

	@EventHandler
	public void onTick(ServerTickEndEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onTick()");
		if (!PluginRule.readBoolean(Rules.DO_PERFMON)) {
			a.stop();
			return;
		}
		Perfmon.tickend();
		a.stop();
	}

	@EventHandler
	public void listPing(PaperServerListPingEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "listPing()");
		e.setHidePlayers(false);
		e.getPlayerSample().clear();
		e.getPlayerSample().add(V.fake("§cFire§3Pixlo §eist jetzt back im Biz!"));
		e.getPlayerSample().add(V.fake("§e§lSpielmodi§7:§r"));
		e.getPlayerSample().add(V.fake("§7   -§cVaro"));
		e.getPlayerSample().add(V.fake("§7   -§cLaser§3Tag"));
		e.getPlayerSample().add(V.fake("§7   -§cEscaperino§3From§cTarschmov"));
		e.getPlayerSample().add(V.fake("§7   -§cCraft§3Attack"));
		e.getPlayerSample().add(V.fake("§7   -§eund viele mehr!"));
		e.getPlayerSample().add(V.fake(" "));
		e.getPlayerSample().add(V.fake("§cYallah join jetzt!"));
		e.setNumPlayers(Bukkit.getOnlinePlayers().size());
		e.setMaxPlayers(1000);
		e.setVersion("Du Birne wechsel auf 1.16.1");
		e.setProtocolVersion(736);
		e.setMotd(Main.pr
				+ " §7» §eNow running on §cYallah§3Cord      §r[§61.16.1§r]\n§eSpiele§7: §cVaro§7,§c Craft§3Attack§7,§c Laser§3Tag§7, §cE§3F§cT");
		try {
			e.setServerIcon(Bukkit.loadServerIcon(new File("icon.png")));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		a.stop();
	}

	@EventHandler
	public void bypassSelfInteractionKick(PlayerKickEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "bypassSelfInteractionKick()");
		if (e.getReason().contains("Cannot") && e.getReason().contains("interact") && e.getReason().contains("with")
				&& e.getReason().contains("self!")) {
			e.setCancelled(true);
			e.getPlayer().sendTitle(new Title("SAVED", "your ass"));
		}
		a.stop();
	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onCraft()");
		CraftBlocker.onCraft(e);
		a.stop();
	}

	@EventHandler
	public void onSmelt(FurnaceSmeltEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "onSmelt()");
		CraftBlocker.onSmelt(e);
		a.stop();
	}

}
