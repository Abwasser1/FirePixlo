package me.abwasser.FirePixlo.advancements;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.endercentral.crazy_advancements.Advancement;
import eu.endercentral.crazy_advancements.AdvancementDisplay;
import eu.endercentral.crazy_advancements.AdvancementDisplay.AdvancementFrame;
import eu.endercentral.crazy_advancements.AdvancementVisibility;
import eu.endercentral.crazy_advancements.CrazyAdvancements;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianAxe;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianChestplate;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianHelmet;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianIngot;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianPickaxe;
import me.abwasser.FirePixlo.customItems.obsidian.ObsidianSword;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianAxe;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianChestplate;
import me.abwasser.FirePixlo.customItems.obsidian.reinforced.ReinforcedObsidianSword;
import me.abwasser.FirePixlo.customItems.obsidian.scythe.ObsidianScythe;
import me.abwasser.FirePixlo.gui.Var;

public class CraftAttackAdvancements {

	public static AdvancementManager manager;

	public static void init() {
		manager = CrazyAdvancements.getNewAdvancementManager();
		advanced_technology();
		charm_power();
	}

	public static void advanced_technology() {
		Advancement root = root("advanced_technology", "root", new ItemStack(Material.OBSIDIAN), "obsidian",
				"Advanced Technology", "The Root!", AdvancementFrame.GOAL, false, false, AdvancementVisibility.ALWAYS);
		Advancement harder_than_steel = child("advanced_technology", "harder_than_steel", new ObsidianIngot().getItem(),
				"Harder than steel", "Smelt an obsidian ingot", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, 0, root);
		Advancement blazing_pick = child("advanced_technology", "blazing_pick", new ObsidianPickaxe().getItem(),
				"Blazing pick", "Craft a obsidian pickaxe", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, 1.5f, harder_than_steel);
		Advancement black_toolery = child("advanced_technology", "black_toolery", new ObsidianAxe().getItem(),
				"Black toolery", "Craft all obsidian tools", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, 1, blazing_pick);
		Advancement reinforced_toolery = child("advanced_technology", "reinforced_toolery",
				new ReinforcedObsidianAxe().getItem(), "Reinforced toolery", "Craft all reinforced obsidian tools",
				AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1, 1, black_toolery);
		Advancement night_sword = child("advanced_technology", "night_sword", new ReinforcedObsidianSword().getItem(),
				"Night blade", "Craft a reinforced obsidian sword", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, -1, black_toolery);
		Advancement overcharge = child("advanced_technology", "overcharge",
				Var.modifyItemStack(new ReinforcedObsidianSword().getItem(), 3), "Overcharge",
				"Reach overcharge on the reinforced obsidian sword", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, 1, night_sword);
		Advancement cover_me_with_obsidian = child("advanced_technology", "cover_me_with_obsidian",
				new ObsidianChestplate().getItem(), "Cover me with Obsidian", "Craft an obsidian armor piece",
				AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1, -1, blazing_pick);
		Advancement i_am_obsidian = child("advanced_technology", "i_am_obsidian", new ObsidianHelmet().getItem(),
				"I am Obsidian", "Get a full obsidian armor", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, -0.75f, cover_me_with_obsidian);
		Advancement reinforcements_needed = child("advanced_technology", "reinforcements_needed",
				new ReinforcedObsidianChestplate().getItem(), "Reinforcements needed", "Craft a full reinforced obsidian armor",
				AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1, 1, i_am_obsidian);
		Advancement withering_lands = child("advanced_technology", "withering_lands",
				new ItemStack(Material.WITHER_ROSE), "Withering lands", "Enter the withered valley",
				AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1, -1.5f, harder_than_steel);
		Advancement withered_moron = child("advanced_technology", "withered_moron", new ItemStack(Material.WITHER_ROSE),
				"Withered moron", "Kill 10 withers", AdvancementFrame.CHALLENGE, true, true,
				AdvancementVisibility.ALWAYS, 1, -1, withering_lands);
		Advancement withered_maniac = child("advanced_technology", "withered_maniac",
				new ItemStack(Material.WITHER_SKELETON_SKULL), "Withered maniac", "Kill 50 withers", AdvancementFrame.CHALLENGE,
				true, true, AdvancementVisibility.ALWAYS, 1, -1, withered_moron);
		Advancement withered_megalomaniac = child("advanced_technology", "withered_megalomaniac",
				new ItemStack(Material.WITHER_SKELETON_SKULL), "Withered megalomaniac", "Kill 100 withers",
				AdvancementFrame.CHALLENGE, true, true, AdvancementVisibility.ALWAYS, 1, -1, withered_maniac);
		Advancement ancient_stone = child("advanced_technology", "ancient_stone",
				new ItemStack(Material.WITHER_SKELETON_SKULL), "Ancient stone", "Get an unactivated crystal",
				AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1, 0.75f, withered_moron);
		Advancement ancient_power = child("advanced_technology", "ancient_power",
				new ItemStack(Material.WITHER_SKELETON_SKULL), "Ancient power", "Craft an activated crystal",
				AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1, 1, ancient_stone);
		Advancement black_scythe = child("advanced_technology", "black_scythe", new ObsidianScythe().getItem(),
				"Black scythe", "Craft a obsidian scythe", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, -1, ancient_stone);
		Advancement maximum_power = child("advanced_technology", "maximum_power",
				Var.modifyItemStack(new ObsidianScythe().getItem(), 14), "Maximum power",
				"Reach charge 4 on the obsidian scythe", AdvancementFrame.GOAL, true, true,
				AdvancementVisibility.ALWAYS, 1, 1, black_scythe);
		Advancement the_grim_reaper = child("advanced_technology", "the_grim_reaper",
				Var.modifyItemStack(new ObsidianScythe().getItem(), 15), "The grim reaper",
				"Reach charge 5 on the obsidian scythe", AdvancementFrame.CHALLENGE, true, true,
				AdvancementVisibility.HIDDEN, 1, -1, maximum_power);
	}

	public static void charm_power() {
		Advancement root = root("charm_power", "root", new ItemStack(Material.DIRT), "gold_ore", "Charm Power",
				"The Root!", AdvancementFrame.GOAL, false, false, AdvancementVisibility.ALWAYS);
		Advancement charming_neclace = child("charm_power", "charming_neclace", new ItemStack(Material.DIRT),
				"Charming neclace", "Get a charm", AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1,
				1, root);
		Advancement charm_farm = child("charm_power", "charm_farm", new ItemStack(Material.DIRT), "Charm farm",
				"Get all charms", AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1, 1,
				charming_neclace);
	}
	

	public static Advancement root(String prefix, String name, ItemStack icon, String background, String title,
			String desc, AdvancementFrame frame, boolean toast, boolean broadcast, AdvancementVisibility visibility) {
		AdvancementDisplay rootDisplay = new AdvancementDisplay(icon, title, desc, frame, toast, broadcast, visibility);
		rootDisplay.setBackgroundTexture("textures/block/" + background + ".png");
		Advancement root = new Advancement(null, new NameKey(prefix, name), rootDisplay);
		manager.addAdvancement(root);
		return root;
	}

	public static Advancement child(String prefix, String name, ItemStack icon, String title, String desc,
			AdvancementFrame frame, boolean toast, boolean broadcast, AdvancementVisibility visibility, float x,
			float y, Advancement parent) {
		AdvancementDisplay childDisplay = new AdvancementDisplay(icon, title, desc, frame, toast, broadcast,
				visibility);
		childDisplay.setPositionOrigin(parent);
		childDisplay.setCoordinates(x, y);
		Advancement kid = new Advancement(parent, new NameKey(prefix, name), childDisplay);
		manager.addAdvancement(kid);
		return kid;
	}
}
