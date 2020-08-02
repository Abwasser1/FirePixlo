package me.abwasser.FirePixlo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.abwasser.FirePixlo.PluginRule.Rules;
import me.abwasser.FirePixlo.advancements.CraftAttackAdvancements;
import me.abwasser.FirePixlo.cmd.CMD_AbwasserMaps;
import me.abwasser.FirePixlo.cmd.CMD_CameraSlide;
import me.abwasser.FirePixlo.cmd.CMD_Dev;
import me.abwasser.FirePixlo.cmd.CMD_EFT;
import me.abwasser.FirePixlo.cmd.CMD_FOV;
import me.abwasser.FirePixlo.cmd.CMD_Get;
import me.abwasser.FirePixlo.cmd.CMD_Heal;
import me.abwasser.FirePixlo.cmd.CMD_Hub;
import me.abwasser.FirePixlo.cmd.CMD_Map;
import me.abwasser.FirePixlo.cmd.CMD_NPC;
import me.abwasser.FirePixlo.cmd.CMD_Packet;
import me.abwasser.FirePixlo.cmd.CMD_Play;
import me.abwasser.FirePixlo.cmd.CMD_PlayerTime;
import me.abwasser.FirePixlo.cmd.CMD_PluginRule;
import me.abwasser.FirePixlo.cmd.CMD_Rank;
import me.abwasser.FirePixlo.cmd.CMD_Rec;
import me.abwasser.FirePixlo.cmd.CMD_Server;
import me.abwasser.FirePixlo.cmd.CMD_Shutdown;
import me.abwasser.FirePixlo.cmd.CMD_Start;
import me.abwasser.FirePixlo.cmd.CMD_StopPlugin;
import me.abwasser.FirePixlo.cmd.CMD_SysLoc;
import me.abwasser.FirePixlo.cmd.CMD_Varo;
import me.abwasser.FirePixlo.cmd.CMD_World;
import me.abwasser.FirePixlo.cmd.CommandRegistry;
import me.abwasser.FirePixlo.customItems.CraftBlocker;
import me.abwasser.FirePixlo.customItems.CustomItems;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianAxe;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianBoots;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianChestplate;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianHelmet;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianIngot;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianLeggins;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianPickaxe;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianPowder;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianShovel;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianSword;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianAxe;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianBoots;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianChestplate;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianHelmet;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianLeggins;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianPickaxe;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianShovel;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianSword;
import me.abwasser.FirePixlo.customItems.obsidian.scythe.ObsidianScythe;
import me.abwasser.FirePixlo.customItems.other.OpOreFinder;
import me.abwasser.FirePixlo.customItems.other.OreFinder;
import me.abwasser.FirePixlo.gun.guns.Gun;
import me.abwasser.FirePixlo.npc.NPCRegistry;
import me.abwasser.FirePixlo.npc.NPCs;
import me.abwasser.FirePixlo.server.DummyServer;
import me.abwasser.FirePixlo.server.Server;
import me.abwasser.FirePixlo.server.ServerListeners;
import me.abwasser.FirePixlo.server.ServerManager;
import me.abwasser.FirePixlo.varo.VaroManager;

public class Main extends JavaPlugin {

	private static Main instance;
	public static String pr;
	public static File homeDir;

	public static String texturePackUrl = "https://drive.google.com/uc?export=download&id=1ubMT_ZtsY_Fq31XTn-Li2m2gboDsfrDy";

	@Override
	public void onEnable() {
		instance = this;
		pr = "§8[§cFire§3Pixlo§8]";
		homeDir = new File("FirePixlo");
		VaroManager.init();
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getPluginManager().registerEvents(new ServerListeners(), this);
		Bukkit.getPluginManager().registerEvents(new me.abwasser.FirePixlo.gui.Listeners(), this);
		Bukkit.getPluginManager().registerEvents(new CustomItems(), this);
		Bukkit.getPluginManager().registerEvents(new ItemListener(), this);
		Bukkit.getPluginManager().registerEvents(new NPCRegistry(), this);
		CommandRegistry.register("get", true, "fire.get", new CMD_Get(), new CMD_Get());
		CommandRegistry.register("heal", true, "fire.heal", new CMD_Heal(), new CMD_Heal());
		CommandRegistry.register("rank", true, "fire.rank", new CMD_Rank(), new CMD_Rank());
		CommandRegistry.register("world", false, "fire.world", new CMD_World(), new CMD_World());
		CommandRegistry.register("sysloc", false, "fire.sysloc", new CMD_SysLoc(), new CMD_SysLoc());
		CommandRegistry.register("server", false, "fire.server", new CMD_Server(), new CMD_Server());
		CommandRegistry.register("dev", false, "fire.dev", new CMD_Dev(), new CMD_Dev());
		CommandRegistry.register("varo", false, "fire.varo", new CMD_Varo(), new CMD_Varo());
		CommandRegistry.register("packet", true, "fire.packet", new CMD_Packet(), new CMD_Packet());
		CommandRegistry.register("playertime", true, "fire.playertime", new CMD_PlayerTime(), new CMD_PlayerTime());
		CommandRegistry.register("rec", true, "fire.rec", new CMD_Rec(), new CMD_Rec());
		CommandRegistry.register("play", true, "fire.play", new CMD_Play(), new CMD_Play());
		CommandRegistry.register("stopplugin", true, "fire.stopplugin", new CMD_StopPlugin(), new CMD_StopPlugin());
		CommandRegistry.register("npc", false, "fire.npc", new CMD_NPC(), new CMD_NPC());
		CommandRegistry.register("map", false, "fire.map", new CMD_Map(), new CMD_Map());
		CommandRegistry.register("hub", false, "", new CMD_Hub(), new CMD_Hub());
		CommandRegistry.register("pluginrule", false, "fire.pluginrule", new CMD_PluginRule(), new CMD_PluginRule());
		CommandRegistry.register("shutdown", false, "fire.shutdown", new CMD_Shutdown(), new CMD_Shutdown());
		CommandRegistry.register("start", false, "fire.start", new CMD_Start(), new CMD_Start());
		CommandRegistry.register("fov", true, "fire.fov", new CMD_FOV(), new CMD_FOV());
		CommandRegistry.register("abwassermaps", true, "fire.abwasserMaps", new CMD_AbwasserMaps(),
				new CMD_AbwasserMaps());
		CommandRegistry.register("cameraslide", true, "fire.cameraslide", new CMD_CameraSlide(), new CMD_CameraSlide());
		CommandRegistry.register("eft", true, "fire.eft", new CMD_EFT(), new CMD_EFT());
		ServerManager.init();
		CustomItems.register(new ObsidianPickaxe());
		CustomItems.register(new ObsidianAxe());
		CustomItems.register(new ObsidianShovel());
		CustomItems.register(new ObsidianSword());
		CustomItems.register(new ReinforcedObsidianPickaxe());
		CustomItems.register(new ReinforcedObsidianAxe());
		CustomItems.register(new ReinforcedObsidianShovel());
		CustomItems.register(new ReinforcedObsidianSword());
		CustomItems.register(new ObsidianPowder());
		CustomItems.register(new ObsidianIngot());
		CustomItems.register(new ObsidianScythe());
		CustomItems.register(new OreFinder());
		CustomItems.register(new OpOreFinder());
		CustomItems.register(new ObsidianHelmet());
		CustomItems.register(new ObsidianChestplate());
		CustomItems.register(new ObsidianLeggins());
		CustomItems.register(new ObsidianBoots());
		CustomItems.register(new ReinforcedObsidianHelmet());
		CustomItems.register(new ReinforcedObsidianChestplate());
		CustomItems.register(new ReinforcedObsidianLeggins());
		CustomItems.register(new ReinforcedObsidianBoots());
		Gun.registerAllGuns();
		CustomItems.registerAllRecipes();
		V.PlayerTime.loop();
		if (PluginRule.readBoolean(Rules.SMOOTHER_RELOAD)) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage("§e!!! WARNING EXPERIMENTAL FEATURE !!!");
				p.sendMessage("§eThe pluginrule §8\"§3SMOOTHER_RELOAD§8\"§e is set to §ctrue§e!");
				YamlHelper helper = new YamlHelper(new File(homeDir, "SMT_RL/" + p.getUniqueId() + ".yml"));
				String servername = helper.readString("SMT_RL.server");
				Location loc = helper.readLocation("SMT_RL.loc");
				Server server = ServerManager.getServer(servername);
				server.joinPlayer(p, new DummyServer());
				p.teleport(loc);
			}
		} else
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setAllowFlight(false);
				ServerManager.lobbyServer.joinPlayer(p, new DummyServer());
			}
		CraftBlocker.init();
		Debug.run();
		NPCs.init();
		V.injectPlayer();
		CraftAttackAdvancements.init();
	}

	@Override
	public void onDisable() {
		if (PluginRule.readBoolean(Rules.SMOOTHER_RELOAD)) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage("§e!!! WARNING EXPERIMENTAL FEATURE !!!");
				p.sendMessage("§eThe pluginrule §8\"§3SMOOTHER_RELOAD§8\"§e is set to §ctrue§e!");
				YamlHelper helper = new YamlHelper(new File(homeDir, "SMT_RL/" + p.getUniqueId() + ".yml"));
				helper.write("SMT_RL.server", ServerManager.getServer(p).getName());
				helper.writeLocation("SMT_RL.loc", p.getLocation());
				helper.save();
			}
		}
		for (Server server : ServerManager.servers) {
			for (Player p : server.getOnlinePlayers()) {
				V.chat(p, "§cServerControl", "§cThe Server §e" + server.getName() + "§c was shutdown! §8[§ePlanned§8]");
				V.chat(p, "§cServerControl", "§eMoving you to the Lobby!");
			}
			server.shutdown(new DummyServer());
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setAllowFlight(true);
			p.teleport(Locations.get(Locations.LOCATION_LOBBY));
		}
		V.uninjectPlayer();
		NPCRegistry.deleteAll();
	}

	public static Main getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		for (File file : new File(
				"\\\\VBOXSVR\\MattjesFilet\\Desktop\\Files\\1.14 Default resourcepack\\assets\\minecraft\\textures\\block")
						.listFiles()) {
			if (file.getName().endsWith(".png")) {
				try {
					BufferedImage img = ImageIO.read(file);
					int r = 0, g = 0, b = 0, counter = 0;
					for (int x = 0; x != img.getWidth(); x++)
						for (int z = 0; z != img.getHeight(); z++) {
							r += printPixelR(img.getRGB(x, z));
							g += printPixelG(img.getRGB(x, z));
							b += printPixelB(img.getRGB(x, z));
							counter++;
						}
					int dr = r / counter, dg = g / counter, db = b / counter;
					System.out.println("case " + file.getName().substring(0, file.getName().length() - 4) + ": ");
					System.out.println("	return \"" + dr + ", " + dg + ", " + db + "\";");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String printPixelARGB(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return red + ", " + green + ", " + blue;
	}

	public static int printPixelR(int pixel) {
		String str = printPixelARGB(pixel);
		return Integer.parseInt(str.split(", ")[0]);
	}

	public static int printPixelG(int pixel) {
		String str = printPixelARGB(pixel);
		return Integer.parseInt(str.split(", ")[1]);
	}

	public static int printPixelB(int pixel) {
		String str = printPixelARGB(pixel);
		return Integer.parseInt(str.split(", ")[2]);
	}
}
