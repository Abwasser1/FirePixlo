package me.abwasser.FirePixlo.cmd;

import static me.abwasser.FirePixlo.V.chat;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import me.abwasser.FirePixlo.cmd.Selector.SelectorCallback;
import net.minecraft.server.v1_16_R1.PacketPlayOutAbilities;
import net.minecraft.server.v1_16_R1.PlayerAbilities;

public class CMD_FOV implements Commander, TabCallback {

	@Override
	public List<String> callback(int index, String label, String[] args) {
		V.dev(this.getClass(), "callback()", "Tab-Completing Index: " + index, Level.VERBOSE);
		if (index == 0) {
			return Selector.getTabCompeltion();
		}
		return null;
	}

	@Override
	public boolean callback(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1 && sender instanceof Player) {
			PlayerAbilities abilities = new PlayerAbilities();
			try {
				abilities.walkSpeed = Float.parseFloat(args[0]);
			} catch (NumberFormatException e) {
				chat(sender, "§4Syntax", "§e/fov §7<Player> <FOV> §eor §e/fov §7<FOV>");
			}
			PacketPlayOutAbilities packet = new PacketPlayOutAbilities(abilities);
			V.sendPacket((Player) sender, packet);
		} else if (args.length == 2) {
			Selector.select(args[0], new SelectorCallback() {

				@Override
				public void run(Player p) {
					PlayerAbilities abilities = new PlayerAbilities();
					try {
						abilities.walkSpeed = Float.parseFloat(args[0]);
					} catch (NumberFormatException e) {
						chat(sender, "§4Syntax", "§e/fov §7<Player> <FOV> §eor §e/fov §7<FOV>");
					}
					PacketPlayOutAbilities packet = new PacketPlayOutAbilities(abilities);
					V.sendPacket((Player) sender, packet);
				}
			});
		} else {
			chat(sender, "§4Syntax", "§e/fov §7<Player> <FOV> §eor §e/fov §7<FOV>");
		}
		return false;
	}

}
