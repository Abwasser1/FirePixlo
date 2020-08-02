package me.abwasser.FirePixlo.display;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import me.abwasser.FirePixlo.Main;
import me.abwasser.FirePixlo.V;
import net.minecraft.server.v1_16_R1.MCUtil;
import net.minecraft.server.v1_16_R1.PacketPlayOutMap;

public class MapUtils {

	public static Combo getMap(World w) {
		ItemStack is = new ItemStack(Material.FILLED_MAP);
		MapMeta mm = (MapMeta) is.getItemMeta();
		MapView mv = Bukkit.createMap(w);
		mm.setMapView(mv);
		mm.setDisplayName("Mappa");
		mm.setColor(Color.AQUA);
		is.setItemMeta(mm);
		return new Combo(mv.getId(), is);
	}

	@SuppressWarnings("deprecation")
	public static void sendPicture(Player p, int mapID, BufferedImage image) {
		MapView mv = Bukkit.getMap(mapID);
		for (MapRenderer mr : mv.getRenderers())
			mv.removeRenderer(mr);
		mv.setLocked(true);
		mv.addRenderer(new MapRenderer() {

			@Override
			public void render(MapView mv, MapCanvas mc, Player p) {
				if (new File("Maps/saved/" + mv.getId()).exists()) {
					try {
						BufferedImage img = ImageIO.read(new File("Maps/saved/" + mv.getId() + "/image.png"));
						byte[] bytes = MapPalette.imageToBytes(resize(image, new Dimension(128, 128)));
						PacketPlayOutMap map = new PacketPlayOutMap(mapID, (byte) 0, false, true,
								Collections.emptyList(), bytes, 0, 0, 128, 128);
						V.sendPacket(p, map);
						bytes = null;
						System.gc();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
		try {
			File file = new File("Maps/saved/" + mv.getId() + "/image.png");
			file.mkdirs();
			file.createNewFile();
			ImageIO.write(image, "png", file);
			byte[] bytes = MapPalette.imageToBytes(resize(image, new Dimension(128, 128)));
			PacketPlayOutMap map = new PacketPlayOutMap(mapID, (byte) 0, false, true, Collections.emptyList(), bytes, 0,
					0, 128, 128);
			V.sendPacket(p, map);
			bytes = null;
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static BufferedImage resize(final BufferedImage img, final Dimension size) throws IOException {
		final BufferedImage image = img;
		final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = resized.createGraphics();
		g.drawImage(image, 0, 0, size.width, size.height, null);
		g.dispose();
		return resized;
	}

	public static BufferedImage resize(final BufferedImage img, int x, int y, final Dimension size) throws IOException {
		final BufferedImage image = img;
		final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = resized.createGraphics();
		g.drawImage(image, x, y, size.width, size.height, null);
		g.dispose();
		return resized;
	}

	public static BufferedImage[] split(final BufferedImage img, int x, int y) {
		int rX = x * 128;
		int rY = y * 128;
		try {
			BufferedImage scaled = resize(img, new Dimension(rX, rY));
			ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
			for (int nx = 0; nx != x; nx++)
				for (int ny = 0; ny != y; ny++) {
					System.out.println("Map: " + x + ", " + y + " " + nx + ", " + ny);
					BufferedImage tile = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
					int offsetX = nx * 128;
					int offsetY = ny * 128;
					for (int cx = offsetX; cx != offsetX + 128; cx++) {
						for (int cy = offsetY; cy != offsetY + 128; cy++) {
							tile.setRGB(cx - offsetX, cy - offsetY, scaled.getRGB(cx, cy));
						}
					}
					tiles.add(tile);
				}
			return tiles.toArray(new BufferedImage[tiles.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void playVideoAsync(Player p, File dir, int fps, DisplayTile... displayTiles) {
		new BukkitRunnable() {

			@Override
			public void run() {
				ArrayList<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
				Dimension sizeOfDisplay = new Dimension(displayTiles[0].sizeX * 128, displayTiles[0].sizeY * 128);
				for (File file : dir.listFiles())
					try {
						bufferedImages.add(resize(ImageIO.read(file), sizeOfDisplay));
						V.chat(p, "Map", "Buffered Frame: " + file.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				playVideoAsync(p, bufferedImages, fps, displayTiles);

			}
		}.runTaskAsynchronously(Main.getInstance());
	}

	/*
	 * public static BufferedImage tile() { int chunkWidth = int chunkHeight = int
	 * count = 0;
	 * 
	 * // draws the image chunk Graphics2D gr = imgs[count++].createGraphics();
	 * gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y,
	 * chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight,
	 * null); gr.dispose(); } } System.out.println("Splitting done"); }
	 */

	public static void playVideoAsync(Player p, ArrayList<BufferedImage> images, int fps, DisplayTile... displayTiles) {
		if (displayTiles.length == 0)
			V.chat(p, "§3Map", "§cNo DisplayTile specified");
		int needed = displayTiles[0].sizeX * displayTiles[0].sizeY;
		if (displayTiles.length != needed)
			V.chat(p, "§3Map", "§cToo many/Not enough DisplayTiles. §3" + displayTiles.length + "§c/§3" + needed);

		Thread t = new PlayerThread(p, images, fps, displayTiles);
		t.start();

	}

	public static class PlayerThread extends Thread {
		public Player p;
		public ArrayList<BufferedImage> images;
		public int fps;
		public DisplayTile[] displayTiles;
		public Dimension sizeOfDisplay;
		public int delay;

		public PlayerThread(Player p, ArrayList<BufferedImage> images, int fps, DisplayTile[] displayTiles) {
			this.p = p;
			this.images = images;
			this.fps = fps;
			this.displayTiles = displayTiles;
			int xSize = displayTiles[0].sizeX * 128;
			int ySize = displayTiles[0].sizeY * 128;
			this.sizeOfDisplay = new Dimension(xSize, ySize);
			this.delay = 1000 / fps;
		}

		public int index = 0;

		@Override
		public void run() {

			for (DisplayTile tile : displayTiles) {
				int xOff = tile.x * -128;
				int yOff = tile.y * -128;
				try {
					BufferedImage bufferedImage = resize(images.get(index), xOff, yOff, new Dimension(128, 128));
					sendPicture(p, tile.id, bufferedImage);
					V.chat(p, "Map", "Frame: " + index);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			index++;
			if (index >= images.size()) {
				stop();
				V.chat(p, "Map", "Stopped!");
			}
			try {
				TimeUnit.MILLISECONDS.sleep(delay);
			} catch (InterruptedException e) {
			}
			run();
		}

	}

	public static class Combo {

		public int id;
		public ItemStack map;

		public Combo(int id, ItemStack map) {
			this.id = id;
			this.map = map;
		}

	}

	public static class DisplayTile {

		public int id;
		public ItemStack map;
		public int x;
		public int y;
		public int sizeX;
		public int sizeY;

		public DisplayTile(int id, ItemStack map, int x, int y, int sizeX, int sizeY) {
			this.id = id;
			this.map = map;
			this.x = x;
			this.y = y;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}

	}
}
