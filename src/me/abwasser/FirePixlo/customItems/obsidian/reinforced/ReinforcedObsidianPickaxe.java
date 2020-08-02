package me.abwasser.FirePixlo.customItems.obsidian.reinforced;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.customItems.CustomItem;

public class ReinforcedObsidianPickaxe extends CustomItem {

	@Override
	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(Main.getInstance(), "reinforced_obsidian_pickaxe");
	}

	@Override
	public ItemStack getItem() {
		ItemStack is = itemBakery(Material.DIAMOND_PICKAXE, 200, "§rReinforced Obsidian Pickaxe", 1,
				getNamespacedKey().getKey());
		return is;
	}

	@Override
	public CustomItem getNewInstace() {
		return new ReinforcedObsidianPickaxe();
	}

	@Override
	public void registerRecipe() {

	}

	@Override
	public void onAttack(EntityDamageByEntityEvent e) {
		e.getEntity().setFireTicks(100);
	}

	@Override
	public void onBreakBlock(BlockBreakEvent e) {
		ItemStack drop;
		switch (e.getBlock().getType()) {
		case IRON_ORE:
			drop = new ItemStack(Material.IRON_INGOT);
			break;
		case GOLD_ORE:
			drop = new ItemStack(Material.GOLD_INGOT);
			break;
		case COBBLESTONE:
			drop = new ItemStack(Material.STONE);
			break;
		case STONE:
			drop = new ItemStack(Material.STONE);
			break;
		case CACTUS:
			drop = new ItemStack(Material.GREEN_DYE);
			break;
		case NETHERRACK:
			drop = new ItemStack(Material.NETHER_BRICK);
			break;
		case WET_SPONGE:
			drop = new ItemStack(Material.SPONGE);
			break;
		case SAND:
			drop = new ItemStack(Material.GLASS);
			break;
		default:
			return;
		}
		e.setDropItems(false);
		Location loc = e.getBlock().getLocation();
		loc.getWorld().dropItemNaturally(loc, drop);
	}

	@Override
	public void onLeftClickBlock(PlayerInteractEvent e) {
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 3, false, false, false));
	}

}
