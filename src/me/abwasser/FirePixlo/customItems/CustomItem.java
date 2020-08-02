package me.abwasser.FirePixlo.customItems;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Multimap;

import me.abwasser.FirePixlo.Token;
import me.abwasser.FirePixlo.V;
import me.abwasser.FirePixlo.YamlHelper;
import me.abwasser.FirePixlo.cmd.CMD_Dev.Level;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import net.minecraft.server.v1_16_R1.NBTTagFloat;
import net.minecraft.server.v1_16_R1.NBTTagInt;
import net.minecraft.server.v1_16_R1.NBTTagList;
import net.minecraft.server.v1_16_R1.NBTTagString;

public abstract class CustomItem {

	public String id;

	public CustomItem() {
		id = Token.generateToken(16);
	}

	public abstract void registerRecipe();

	public abstract NamespacedKey getNamespacedKey();

	public abstract ItemStack getItem();

	public abstract CustomItem getNewInstace();

	public void onRightClick(PlayerInteractEvent e) {
	}

	public void onLeftClick(PlayerInteractEvent e) {
	}

	public void onRightClickAir(PlayerInteractEvent e) {
		onRightClick(e);
	}

	public void onLeftClickAir(PlayerInteractEvent e) {
		onLeftClick(e);
	}

	public void onRightClickBlock(PlayerInteractEvent e) {
		onRightClick(e);
	}

	public void onLeftClickBlock(PlayerInteractEvent e) {
		onLeftClick(e);
	}

	public void onRightClickOffhand(PlayerInteractEvent e) {
	}

	public void onLeftClickOffhand(PlayerInteractEvent e) {
	}

	public void onRightClickOffhandAir(PlayerInteractEvent e) {
		onRightClickOffhand(e);
	}

	public void onLeftClickOffhandAir(PlayerInteractEvent e) {
		onLeftClickOffhand(e);
	}

	public void onRightClickOffhandBlock(PlayerInteractEvent e) {
		onRightClickOffhand(e);
	}

	public void onLeftClickOffhandBlock(PlayerInteractEvent e) {
		onLeftClickOffhand(e);
	}

	public void onBreakBlock(BlockBreakEvent e) {
	}

	public void onDeathWhileInHand(PlayerDeathEvent e) {
	}

	public void onDeathWhileInOffHand(PlayerDeathEvent e) {
		onDeathWhileInHand(e);
	}

	public void onDrop(PlayerDropItemEvent e) {
	}

	public void onAttack(EntityDamageByEntityEvent e) {
	}

	public void onDamage(EntityDamageEvent e) {
	}

	@Deprecated
	public void onCraft(CraftItemEvent e) {
	}

	public void onSwitchToOffhand(PlayerSwapHandItemsEvent e) {
	}

	public void onSwitchToMainhand(PlayerSwapHandItemsEvent e) {
	}

	public void onMove(PlayerMoveEvent e) {
	}

	public void onUnselect(PlayerItemHeldEvent e) {
	}

	public void onSelect(PlayerItemHeldEvent e) {

	}

	public void onKill(EntityDeathEvent e) {
	}

	public void onInventoryClick(InventoryClickEvent e) {
	}

	public void itemBakery(Material material) {
	}

	public ItemStack itemBakery(Material material, String displayName) {
		return itemBakery(material, displayName, 1);
	}

	public ItemStack itemBakery(Material material, String displayName, int amount) {
		return itemBakery(material, displayName, 1, 0);
	}

	@SuppressWarnings("deprecation")
	public ItemStack itemBakery(Material material, String displayName, int amount, int damage) {
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(displayName);
		is.setAmount(amount);
		is.setDurability((short) damage);
		is.setItemMeta(im);
		return is;
	}

	public ItemStack itemBakery(Material material, String displayName, int amount, int damage, String... lore) {
		ItemStack is = itemBakery(material, displayName, amount, damage);
		ItemMeta im = is.getItemMeta();
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);
		return is;
	}

	public ItemStack itemBakery(Material material, String displayName, int amount, String... lore) {
		return itemBakery(material, displayName, amount, 0, lore);
	}

	public ItemStack itemBakery(Material material, int customModelData, String displayName, int amount,
			String... lore) {
		ItemStack is = itemBakery(material, displayName, amount, lore);
		ItemMeta im = is.getItemMeta();
		im.setCustomModelData(customModelData);
		is.setItemMeta(im);
		return is;
	}

	public ItemStack modifyAttribute(ItemStack is, Attribute attribute, AttributeModifier modifier) {
		ItemStack iss = new ItemStack(is);
		ItemMeta im = iss.getItemMeta();
		Multimap<Attribute, AttributeModifier> map = im.getAttributeModifiers();
		map.put(attribute, modifier);
		im.setAttributeModifiers(map);
		iss.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public void reduceDurability(int durability, ItemStack is) {
		V.dev(this.getClass(), "reduceDurability()",
				"Reduced durability to " + (short) (is.getDurability() + durability), Level.VERBOSE);
		is.setDurability((short) (is.getDurability() + durability));
	}

	public String getId(ItemStack is) {
		return is.getLore().get(1);
	}

	public boolean matchIds(ItemStack is, String id) {
		Bukkit.broadcastMessage("Matching...");
		if (is.getItemMeta().getLore() != null) {
			if (is.getItemMeta().getLore().size() > 1) {
				String id2 = is.getLore().get(1);
				String id1 = "#" + id;
				if (id1.equals(id2)) {
					return true;
				} else
					Bukkit.broadcastMessage(id2 + " != " + id1);
			} else
				Bukkit.broadcastMessage("Item Lore is too short");
		} else
			Bukkit.broadcastMessage("Item has no Lore");
		return false;
	}

	public void writeData(String id, String path, Object value) {
		YamlHelper helper = new YamlHelper(
				new File("FirePixlo/ItemData/" + getItem().getType().name() + "/" + id + ".yml"));
		helper.write(path, value);
	}

	public void writeData(Material type, String id, String path, Object value) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/ItemData/" + type.name() + "/" + id + ".yml"));
		helper.write(path, value);
	}

	public Object readData(String id, String path) {
		YamlHelper helper = new YamlHelper(
				new File("FirePixlo/ItemData/" + getItem().getType().name() + "/" + id + ".yml"));
		return helper.read(path);
	}

	public Object readData(Material type, String id, String path) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/ItemData/" + type.name() + "/" + id + ".yml"));
		return helper.read(path);
	}

	public String readDataString(Material type, String id, String path) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/ItemData/" + type.name() + "/" + id + ".yml"));
		return helper.readString(path);
	}

	public void attribAttackSpeed(ItemStack is, float attackspeed, int attackdamage) {
		net.minecraft.server.v1_16_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		NBTTagList modifiers = new NBTTagList();
		NBTTagCompound speed = new NBTTagCompound();
		speed.set("AttributeName", NBTTagString.a("generic.attackSpeed"));
		speed.set("Name", NBTTagString.a("generic.attackSpeed"));
		speed.set("Amount", NBTTagFloat.a(attackspeed));
		speed.set("Operation", NBTTagInt.a(0));
		speed.set("UUIDLeast", NBTTagInt.a(894654));
		speed.set("UUIDMost", NBTTagInt.a(2872));
		speed.set("Slot", NBTTagString.a("mainhand"));

		modifiers.add(speed);
		NBTTagCompound damage = new NBTTagCompound();
		damage.set("AttributeName", NBTTagString.a("generic.attackDamage"));
		damage.set("Name", NBTTagString.a("generic.attackDamage"));
		damage.set("Amount", NBTTagInt.a(attackdamage));
		damage.set("Operation", NBTTagInt.a(0));
		damage.set("UUIDLeast", NBTTagInt.a(894654));
		damage.set("UUIDMost", NBTTagInt.a(2872));
		damage.set("Slot", NBTTagString.a("mainhand"));

		modifiers.add(damage);
		compound.set("AttributeModifiers", modifiers);
		nmsStack.setTag(compound);
		ItemStack item = CraftItemStack.asBukkitCopy(nmsStack);
		is.setItemMeta(item.getItemMeta());
	}

}
