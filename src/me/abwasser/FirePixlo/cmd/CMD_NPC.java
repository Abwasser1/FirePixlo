package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.gui.Var;
import net.minecraft.server.v1_16_R1.PacketPlayOutAbilities;
import net.minecraft.server.v1_16_R1.PlayerAbilities;;

public class CMD_NPC implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
//			new BukkitRunnable() {
//				
//				@Override
//				public void run() {
//					((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_ELDER_GUARDIAN_FLOP, 1, 1);
//					((Player) sender).sendMessage("*FAB*");
//				}
//			}.runTaskTimer(Main.getInstance(), 0, 2);
			V.preparePlayer((Player)sender);
		} else if (args.length == 1) {
			V.advMsg((Player)sender, Var.skullBakery(args[0], args[0]), "§a"+args[0]+"§e is now online!");
		}else if (args.length == 2) {
			V.advMsg((Player)sender, new ItemStack(Material.MAP), "§aFind an extraction point\n§70.28:57");
			new BukkitRunnable() {
				
				@Override
				public void run() {
					V.advMsg((Player)sender, new ItemStack(Material.MAP), "§f§lEXIT01§r Warehouse 4");
					V.advMsg((Player)sender, new ItemStack(Material.MAP), "§f§lEXIT02§r Old Gas Station");
				}
			}.runTaskLater(Main.getInstance(), 10);
			} else if (args.length == 7) {

			PlayerAbilities abilities = new PlayerAbilities();
			abilities.isInvulnerable = Boolean.parseBoolean(args[0]);
			abilities.isFlying = Boolean.parseBoolean(args[1]);
			abilities.canFly = Boolean.parseBoolean(args[2]);
			abilities.canInstantlyBuild = Boolean.parseBoolean(args[3]);
			abilities.mayBuild = Boolean.parseBoolean(args[4]);
			abilities.flySpeed = Float.parseFloat(args[5]);
			abilities.walkSpeed = Float.parseFloat(args[6]);
			PacketPlayOutAbilities packet = new PacketPlayOutAbilities(abilities);
			V.sendPacket((Player) sender, packet);

		} else {
			chat(sender, "§4Syntax", "§cRESERVED!");
		}

		return false;
	}

}
