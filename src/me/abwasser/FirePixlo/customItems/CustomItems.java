package me.abwasser.FirePixlo.customItems;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import me.abwasser.FirePixlo.Perfmon;
import me.abwasser.FirePixlo.Perfmon.Type;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;

public class CustomItems implements Listener {

	public static ArrayList<CustomItem> registry = new ArrayList<>();
	public static HashMap<String, CustomItem> namespaces = new HashMap<>();

	public static void register(CustomItem customItem) {
		V.dev(CustomItems.class, "register()", "Registered custom item: §e" + customItem.getNamespacedKey().toString(),
				Level.MEDIUM);
		registry.add(customItem);
		namespaces.put(customItem.getNamespacedKey().getKey(), customItem);
	}

	public static void registerAllRecipes() {
		for (CustomItem ct : registry) {
			ct.registerRecipe();
			V.dev(CustomItems.class, "registerAllRecipes()", "Registered recipe: §e" + ct.getNamespacedKey().toString(),
					Level.VERBOSE);
		}
	}

	public static ItemStack getKnowlegeBook() {
		V.dev(CustomItems.class, "getKnowlegeBook()", "Getting Knowledge Book", Level.VERBOSE);
		ItemStack is = new ItemStack(Material.KNOWLEDGE_BOOK);
		KnowledgeBookMeta kbm = (KnowledgeBookMeta) is.getItemMeta();
		for (CustomItem ct : registry)
			kbm.addRecipe(ct.getNamespacedKey());
		V.dev(CustomItems.class, "getKnowlegeBook()", "Added Recipes to Knowledge Book", Level.VERBOSE);
		kbm.setDisplayName("Firenomicon");
		is.setItemMeta(kbm);
		V.dev(CustomItems.class, "getKnowlegeBook()", "Returned Knowledge Book", Level.VERBOSE);
		return is;
	}

	@EventHandler
	public void eventPlayerInteract(PlayerInteractEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventPlayerInteract()");
		if (e.getItem() != null) {
			ItemStack is = e.getItem();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventPlayerInteract()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						V.dev(CustomItems.class, "eventPlayerInteract()", "->Action: " + e.getAction().name(),
								Level.VERBOSE);
						if (e.getAction() == Action.LEFT_CLICK_AIR) {
							if (e.getHand() == EquipmentSlot.OFF_HAND) {
								ct.onLeftClickOffhandAir(e);
							} else if (e.getHand() == EquipmentSlot.HAND)
								ct.onLeftClickAir(e);

						} else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
							if (e.getHand() == EquipmentSlot.OFF_HAND) {
								ct.onLeftClickOffhandBlock(e);
							} else if (e.getHand() == EquipmentSlot.HAND)
								ct.onLeftClickBlock(e);

						} else if (e.getAction() == Action.RIGHT_CLICK_AIR) {

							if (e.getHand() == EquipmentSlot.OFF_HAND) {
								ct.onRightClickOffhandAir(e);
							} else if (e.getHand() == EquipmentSlot.HAND)
								ct.onRightClickAir(e);

						} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

							if (e.getHand() == EquipmentSlot.OFF_HAND) {
								ct.onRightClickOffhandBlock(e);
							} else if (e.getHand() == EquipmentSlot.HAND)
								ct.onRightClickBlock(e);

						}
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventBlockDestroy(BlockBreakEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventBlockDestroy()");
		if (e.getPlayer().getInventory().getItemInMainHand() != null) {
			ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventBlockDestroy()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onBreakBlock(e);
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventDeath(PlayerDeathEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventDeath()");
		if (e.getEntity().getInventory().getItemInMainHand() != null) {
			ItemStack is = e.getEntity().getInventory().getItemInMainHand();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventDeath()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onDeathWhileInHand(e);
					}
				}
			}
		}
		if (e.getEntity().getInventory().getItemInOffHand() != null) {
			ItemStack is = e.getEntity().getInventory().getItemInOffHand();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventDeath()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onDeathWhileInOffHand(e);
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventItemDrop(PlayerDropItemEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventItemDrop()");
		if (e.getItemDrop().getItemStack() != null) {
			ItemStack is = e.getItemDrop().getItemStack();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventItemDrop()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onDrop(e);
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventAttack(EntityDamageByEntityEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventAttack()");
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (p.getInventory().getItemInMainHand() != null) {
				ItemStack is = p.getInventory().getItemInMainHand();
				if (is.getLore() != null) {
					if (!is.getLore().isEmpty()) {
						if (namespaces.containsKey(is.getLore().get(0))) {
							CustomItem ct = namespaces.get(is.getLore().get(0));
							V.dev(CustomItems.class, "eventAttack()", "Registered §e" + e.getClass().getSimpleName()
									+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
							ct.onAttack(e);
						}
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventDamage(EntityDamageEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventDamage()");
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (p.getInventory().getItemInMainHand() != null) {
				ItemStack is = p.getInventory().getItemInMainHand();
				if (is.getLore() != null) {
					if (!is.getLore().isEmpty()) {
						if (namespaces.containsKey(is.getLore().get(0))) {
							CustomItem ct = namespaces.get(is.getLore().get(0));
							V.dev(CustomItems.class, "eventDamage()", "Registered §e" + e.getClass().getSimpleName()
									+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
							ct.onDamage(e);
						}
					}
				}
			}
			if (p.getInventory().getItemInOffHand() != null) {
				ItemStack is = p.getInventory().getItemInOffHand();
				if (is.getLore() != null) {
					if (!is.getLore().isEmpty()) {
						if (namespaces.containsKey(is.getLore().get(0))) {
							CustomItem ct = namespaces.get(is.getLore().get(0));
							V.dev(CustomItems.class, "eventDamage()", "Registered §e" + e.getClass().getSimpleName()
									+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
							ct.onDamage(e);
						}
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventSwapHand(PlayerSwapHandItemsEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventSwapHand()");
		if (e.getMainHandItem() != null) {
			ItemStack is = e.getMainHandItem();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventSwapHand()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onSwitchToMainhand(e);
					}
				}
			}
		}
		if (e.getOffHandItem() != null) {
			ItemStack is = e.getOffHandItem();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventSwapHand()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onSwitchToOffhand(e);
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventItemHeld(PlayerItemHeldEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventItemHeld()");
		ItemStack oldIS = e.getPlayer().getInventory().getItem(e.getPreviousSlot());
		ItemStack newIS = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if (oldIS != null) {
			ItemStack is = oldIS;
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						ct.onUnselect(e);
					}
				}
			}
		}
		if (newIS != null) {
			ItemStack is = newIS;
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventItemHeld()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onSelect(e);
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventMove(PlayerMoveEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventMove()");
		ItemStack newIS = e.getPlayer().getInventory().getItemInMainHand();
		if (newIS != null) {
			ItemStack is = newIS;
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						V.dev(CustomItems.class, "eventItemHeld()", "Registered §e" + e.getClass().getSimpleName()
								+ "§r for §e" + ct.getNamespacedKey().getKey(), Level.VERBOSE);
						ct.onMove(e);
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventEntityDeath(EntityDeathEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventEntityDeath()");
		Entity killer = e.getEntity().getKiller();
		if (killer instanceof Player) {
			ItemStack is = ((Player) killer).getInventory().getItemInMainHand();
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						ct.onKill(e);
					}
				}
			}
		}
		a.stop();
	}

	@EventHandler
	public void eventInventoryClick(InventoryClickEvent e) {
		Perfmon a = new Perfmon(Type.EVENT, this.getClass(), "eventInventoryClick()");
		ItemStack is = e.getCurrentItem();
		if (is != null)
			if (is.getLore() != null) {
				if (!is.getLore().isEmpty()) {
					if (namespaces.containsKey(is.getLore().get(0))) {
						CustomItem ct = namespaces.get(is.getLore().get(0));
						ct.onInventoryClick(e);
					}
				}
			}
		a.stop();
	}

}
