package me.abwasser.FirePixlo.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.YamlHelper;

public class GUIs {
	/*
	 * 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | <<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 9 | 10 |
	 * 11 | 12 | 13 | 14 | 15 | 16 | 17 | <<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 18 | 19 |
	 * 20 | 21 | 22 | 23 | 24 | 25 | 26 | <<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 27 | 28 |
	 * 29 | 30 | 31 | 32 | 33 | 34 | 35 | <<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 36 | 37 |
	 * 38 | 39 | 40 | 41 | 42 | 43 | 44 | <<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 45 | 46 |
	 * 47 | 48 | 49 | 50 | 51 | 52 | 53 | <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * 
	 * 
	 */

	public static class WorldCreatorGUI {

		String world_name;
		WorldType wt;
		Environment env;
		World w = null;
		boolean sync = false;
		Callback callback = null;

		public WorldCreatorGUI() {
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("deprecation")
		public GUI getGUI(String id) {

			GUI gui = new GUI(3, "§3World§7-§6Creator");
			ItemStack name = Var.itemBakery(Material.NAME_TAG, "World-Name", 1, (short) 0, "Name für die neue Welt");

			ItemStack wt_AMPLIFIED = Var.itemBakery(Material.ELYTRA, "§6World-Type", 1, (short) 0,
					"World-Type: §eAMPLIFIED§d!");
			ItemStack wt_BUFFET = Var.itemBakery(Material.LEGACY_SAPLING, "§6World-Type", 1, (short) 0,
					"World-Type: §eBUFFET§d!");
			ItemStack wt_CUSTOMIZED = Var.itemBakery(Material.STRUCTURE_VOID, "§6World-Type", 1, (short) 0,
					"World-Type: §eCUSTOMIZED§d!");
			ItemStack wt_FLAT = Var.itemBakery(Material.SMOOTH_SANDSTONE, "§6World-Type", 1, (short) 0,
					"World-Type: §eFLAT§d!");
			ItemStack wt_LARGE_BIOMES = Var.itemBakery(Material.JUNGLE_SAPLING, "§6World-Type", 1, (short) 0,
					"World-Type: §eLARGE_BIOMES§d!");
			ItemStack wt_NORMAL = Var.itemBakery(Material.GRASS_BLOCK, "§6World-Type", 1, (short) 0,
					"World-Type: §eNORMAL§d!");
			ItemStack wt_VERSION_1_1 = Var.itemBakery(Material.COMMAND_BLOCK, "§6World-Type", 1, (short) 0,
					"World-Type: §eVERSION_1_1§d!");

			ItemStack env_NORMAL = Var.itemBakery(Material.GRASS_BLOCK, "§6Environment", 1, (short) 0,
					"Environment: §eNORMAL§d!");
			ItemStack env_NETHER = Var.itemBakery(Material.NETHERRACK, "§6Environment", 1, (short) 0,
					"Environment: §eNETHER§d!");
			ItemStack env_THE_END = Var.itemBakery(Material.ENDER_EYE, "§6Environment", 1, (short) 0,
					"Environment: §eTHE_END§d!");

			GTextInput element_name = new GTextInput(10, name, name, name, "Choose a worldname",
					new GTextInputInterface() {

						@Override
						public void onenter(String value, GTextInput Gspin, Player p, InventoryClickEvent e) {
							world_name = value;
						}
					});

			Option option_1 = new Option(wt_NORMAL, "NORMAL");
			Option option_2 = new Option(wt_FLAT, "FLAT");
			Option option_3 = new Option(wt_LARGE_BIOMES, "LARGE_BIOMES");
			Option option_4 = new Option(wt_AMPLIFIED, "AMPLIFIED");
			Option option_5 = new Option(wt_BUFFET, "BUFFET");
			Option option_6 = new Option(wt_CUSTOMIZED, "CUSTOMIZED");
			Option option_7 = new Option(wt_VERSION_1_1, "VERSION_1_1");

			GSpinner element_wt = new GSpinner(12, new GSpinnerInterface() {

				@Override
				public void onselect(String value, GSpinner Gspin, Player p, InventoryClickEvent e) {
					wt = WorldType.valueOf(value);
				}
			}, option_1, option_2, option_3, option_4, option_5, option_6, option_7);

			Option option_8 = new Option(env_NORMAL, "NORMAL");
			Option option_9 = new Option(env_NETHER, "NETHER");
			Option option_10 = new Option(env_THE_END, "THE_END");

			GSpinner element_env = new GSpinner(14, new GSpinnerInterface() {

				@Override
				public void onselect(String value, GSpinner Gspin, Player p, InventoryClickEvent e) {
					env = Environment.valueOf(value);

				}
			}, option_8, option_9, option_10);

			ItemStack is = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta im = (SkullMeta) is.getItemMeta();
			im.setOwner("Dipicrylamine");
			im.setDisplayName("§aCreate");
			im.setLore(Arrays.asList("Create the World"));
			is.setItemMeta(im);

			gui.setItem(10, element_name);
			gui.setItem(12, element_wt);
			gui.setItem(14, element_env);
			gui.setItem(16, is, "btn-create", new GUIListener() {

				@Override
				public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
						InventoryClickEvent e, GUI gui) {
					p.closeInventory();
					if (world_name == null) {
						p.sendTitle(Main.pr, "§cPlease specify a worldname!");
					}
					Bukkit.broadcastMessage(Main.pr
							+ "§7[§cAntiLag§7] §cDer Server generiert gerade eine neue Welt deshalb könnte es zu Laggs kommen!");
					Bukkit.broadcastMessage(
							Main.pr + "§7[§cAntiLag§7] §cFallst du dich nicht bewegen kannst gebe §e/lag 1 §cein!");
					try {
						p.sendTitle("§eGenerating§7: §6" + world_name, "§7██████████");
						try {
							TimeUnit.MILLISECONDS.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						WorldCreator wc = new WorldCreator(world_name);
						p.sendTitle("§eType§7: §6" + wt.getName(), "§3█§7█████████");
						try {
							TimeUnit.MILLISECONDS.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						wc.type(wt);
						p.sendTitle("§eEnviorment§7: §6" + env.name(), "§3██§7████████");
						try {
							TimeUnit.MILLISECONDS.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						wc.environment(env);
						p.sendTitle("§eStarting ChunkGenerator", "§3████§7██████");
						w = wc.createWorld();
						p.sendTitle("§eFinished World§7", "§3██████████§7");
					} catch (Exception e2) {
						e2.printStackTrace();
						p.sendTitle(Main.pr, "§cWorld generation failed!");
						/*
						 * if (callback != null) callback.call(null);
						 */
					} finally {
						YamlHelper helper = new YamlHelper("FireAPI/Yamls/WorldGeneration/a/" + w.getName() + ".yml");
						helper.write("Creator.Name", p.getName());
						helper.write("Creator.UUID", p.getUniqueId().toString());
						Bukkit.broadcastMessage(Main.pr + "§7[§cAntiLag§7] §aDie Laggs sollten jetzt verschwinden!");
					}
					if (callback != null && w != null)
						callback.call(w);

				}
			});
			gui.backgroundFill();
			return gui;
		}

		public GUI getGUI() {
			return getGUI(Token.generateToken(100));
		}

		public void setCallback(Callback callback) {
			this.callback = callback;
		}

		@Deprecated
		@Nullable
		public World getWorld() {
			return w;
		}

		@Deprecated
		public void sync() {
			while (!sync) {
			}
			return;
		}

		@Deprecated
		public boolean isSync() {
			return sync;
		}

	}

	public static class selectWorld {

		World selected;
		Callback callback;

		public selectWorld() {
		}

		@SuppressWarnings("deprecation")
		public GUI getGUI() {
			// Ich möchte sterben :(
			int rows = (int) Math
					.ceil((double) ((double) ((double) Bukkit.getWorlds().size() + (double) 1.0d) / ((double) 9)));
			if (rows > 6)
				rows = 6;
			GUI gui = new GUI(rows, "§6Select a World!");
			int slot = 1;
			ItemStack iss = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta imm = (SkullMeta) iss.getItemMeta();
			imm.setOwner("Omanoctoa");
			imm.setDisplayName("§dCreate new World");
			imm.setLore(Arrays.asList("Create a new World"));
			iss.setItemMeta(imm);
			String tokenn = Token.generateToken(10);
			gui.setItem(0, iss, tokenn, new GUIListener() {

				@Override
				public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
						InventoryClickEvent e, GUI gui) {
					p.closeInventory();
					WorldCreatorGUI wcg = new GUIs.WorldCreatorGUI();
					wcg.setCallback(new Callback() {

						@Override
						public void call(Object obj) {

							if (obj == null)
								p.sendTitle(Main.pr, "§cInternal Error\r\n§7Please reload and retry!");
							selected = (World) obj;
							if (callback != null)
								callback.call(selected);

						}
					});
					GUI gui2 = wcg.getGUI();
					gui2.open(p);
				}

			});
			for (World w : Bukkit.getWorlds()) {
				ItemStack is = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta im = (SkullMeta) is.getItemMeta();
				switch (w.getEnvironment()) {
				case NORMAL:
					im.setOwner("Dipicrylamine");
					im.setDisplayName("§a" + w.getName());
					break;
				case NETHER:
					im.setOwner("haohanklliu");
					im.setDisplayName("§c" + w.getName());
					break;
				case THE_END:
					im.setOwner("Dic");
					im.setDisplayName("§e" + w.getName());
					break;

				default:
					break;
				}
				im.setLore(Arrays.asList("Name: " + w.getName(), "Type: " + w.getWorldType().getName(),
						"Enviorment: " + w.getEnvironment().name(), "Players: " + w.getPlayers().size()));
				is.setItemMeta(im);
				String token = Token.generateToken(10);
				gui.setItem(slot, is, token, new GUIListener() {

					@Override
					public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
							InventoryClickEvent e, GUI gui) {
						p.closeInventory();
						selected = Bukkit.getWorld(is.getItemMeta().getDisplayName().substring(2));
						if (callback != null)
							callback.call(Bukkit.getWorld(is.getItemMeta().getDisplayName().substring(2)));
					}
				});
				slot++;
			}
			return gui;
		}

		public void setCallback(Callback callback) {
			this.callback = callback;
		}

		public World getSelected() {
			return selected;
		}

	}

	public static class WorldServerCreatorGUI {

		public WorldServerCreatorGUI() {
			// TODO Auto-generated constructor stub
		}

		public GUI getGUI() {
			GUI gui = new GUI(3, "§3World§cServer§7-§6Creator");
			// WorldServer ws = new WorldServer(name, world, maxPlayers, gm, wso)
			return gui;
		}
	}

	public static class FileExplorerGUI {

		public FileExplorerGUI() {
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("deprecation")
		public GUI getGUI(String dir) {
			ArrayList<File> folders = new ArrayList<>();
			ArrayList<File> files = new ArrayList<>();
			File file = new File(dir);
			int rows = (int) Math
					.ceil((double) ((double) ((double) file.listFiles().length + (double) 1.0d) / ((double) 9))) + 1;
			if (rows > 6)
				rows = 6;
			GUI gui = new GUI(rows, "File Explorer");
			int slot = 1;
			gui.setItem(0, Var.itemBakery(Material.RED_WOOL, "..", 1, (short) 0, ".."), "dir", new GUIListener() {

				@Override
				public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
						InventoryClickEvent e, GUI gui) {
					p.closeInventory();
					FileExplorerGUI feg = new FileExplorerGUI();
					feg.getGUI(file.getParent()).open(p);
					;

				}
			});
			for (File f : file.listFiles()) {
				if (slot > 53)
					break;
				if (f.isDirectory()) {
					folders.add(f);
				} else {
					files.add(f);
				}
			}
			for (File f : folders) {
				if (slot > 45)
					break;
				if (f.isDirectory()) {
					gui.setItem(slot,
							Var.itemBakery(Material.YELLOW_WOOL, f.getName(), 1, (short) 0, f.getAbsolutePath()), "dir",
							new GUIListener() {

								@Override
								public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
										InventoryClickEvent e, GUI gui) {
									p.closeInventory();
									FileExplorerGUI feg = new FileExplorerGUI();
									feg.getGUI(f.getAbsolutePath()).open(p);
									;

								}
							});
				}
				slot++;
			}
			for (File f : files) {
				if (slot > 53)
					break;
				if (f.getName().toLowerCase().endsWith(".zip")) {
					ItemStack iss = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta imm = (SkullMeta) iss.getItemMeta();
					imm.setOwner("mattijs");
					imm.setDisplayName(f.getName());
					imm.setLore(Arrays.asList(f.getAbsolutePath()));
					iss.setItemMeta(imm);
					gui.setItem(slot, iss, "dir", new GUIListener() {

						@Override
						public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
								InventoryClickEvent e, GUI gui) {
							p.closeInventory();
							try {
								Desktop.getDesktop().open(f);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					});
				} else if (f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".png")
						|| f.getName().toLowerCase().endsWith(".bmp") || f.getName().toLowerCase().endsWith(".gif")
						|| f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".mp4")
						|| f.getName().toLowerCase().endsWith(".webm") || f.getName().toLowerCase().endsWith(".tiff")
						|| f.getName().toLowerCase().endsWith(".svg") || f.getName().toLowerCase().endsWith(".mov")
						|| f.getName().toLowerCase().endsWith(".mkv") || f.getName().toLowerCase().endsWith(".avi")
						|| f.getName().toLowerCase().endsWith(".m4v") || f.getName().toLowerCase().endsWith(".flv")) {
					ItemStack iss = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta imm = (SkullMeta) iss.getItemMeta();
					imm.setOwner("CCTV");
					imm.setDisplayName(f.getName());
					imm.setLore(Arrays.asList(f.getAbsolutePath()));
					iss.setItemMeta(imm);
					gui.setItem(slot, iss, "dir", new GUIListener() {

						@Override
						public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
								InventoryClickEvent e, GUI gui) {
							p.closeInventory();
							try {
								Desktop.getDesktop().open(f);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					});
				} else if (f.getName().toLowerCase().endsWith(".exe") || f.getName().toLowerCase().endsWith(".bat")
						|| f.getName().toLowerCase().endsWith(".ink") || f.getName().toLowerCase().endsWith(".msi")
						|| f.getName().toLowerCase().endsWith(".vbs")) {
					ItemStack iss = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta imm = (SkullMeta) iss.getItemMeta();
					imm.setOwner("zasf");
					imm.setDisplayName(f.getName());
					imm.setLore(Arrays.asList(f.getAbsolutePath()));
					iss.setItemMeta(imm);
					gui.setItem(slot, iss, "dir", new GUIListener() {

						@Override
						public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
								InventoryClickEvent e, GUI gui) {
							p.closeInventory();
							try {
								Desktop.getDesktop().open(f);
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						}
					});
				} else {
					gui.setItem(slot,
							Var.itemBakery(Material.LIGHT_GRAY_WOOL, f.getName(), 1, (short) 0, f.getAbsolutePath()),
							"dir", new GUIListener() {

								@Override
								public void onClick(Player p, Inventory inv, ItemStack is, int slot, String customID,
										InventoryClickEvent e, GUI gui) {
									p.closeInventory();
									try {
										Desktop.getDesktop().open(f);
									} catch (IOException e1) {
										e1.printStackTrace();
									}

								}
							});
				}

				slot++;
			}
			return gui;
		}

	}

	/*
	 * public static GUI createWorldServer() { GUI gui = new GUI(3,
	 * "§3WorldServer§7-§6Creator");
	 * 
	 * String wsname; String token; World world; int maxPlayers; GameMode gm;
	 * WorldServerOptions[] wso;
	 * 
	 * ItemStack globe = new ItemStack(Material.PLAYER_HEAD); SkullMeta globemeta =
	 * (SkullMeta) globe.getItemMeta(); globemeta.setOwner("Dipicrylamine");
	 * globemeta.setDisplayName("§aCreate a world");
	 * globemeta.setLore(Arrays.asList("Create the World"));
	 * globe.setItemMeta(globemeta);
	 * 
	 * gui.setItem(10, globe, "btn-create-world", new GUIListener() {
	 * 
	 * @Override public void onClick(Player p, Inventory inv, ItemStack is, int
	 * slot, String customID, InventoryClickEvent e, GUI gui) { token =
	 * Token.generateToken(20); GUI worldGUI = createWorld(token);
	 * 
	 * } });
	 * 
	 * return gui; }
	 */

}
