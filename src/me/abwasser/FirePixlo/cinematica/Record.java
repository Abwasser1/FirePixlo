package me.abwasser.FirePixlo.cinematica;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import me.abwasser.FirePixlo.YamlHelper;

public class Record {

	public File file;
	public YamlHelper helper;
	public HashMap<Integer, Frame> frames = new HashMap<Integer, Frame>();
	public int currentFrame;

	public Record(File file) {
		this.file = file;
		this.helper = new YamlHelper(file);
		currentFrame = 0;
		cacheFrames();
	}

	public @Nullable Frame getFrame(int frame) {
		if(frame < 0) 
			frame = 0;
		if(frame > getLegth())
			frame = getLegth();
		return frames.get(frame);
	}

	public @Nullable Frame getFrame() {
		return getFrame(currentFrame);
	}

	public Frame rewind(int time) {
		currentFrame -= time;
		if (currentFrame < 0)
			currentFrame = 0;
		return getFrame();
	}
	public Frame rewind() {
		return rewind(1);
	}
	public Frame skip(int time) {
		currentFrame += time;
		if (currentFrame > getLegth())
			currentFrame = getLegth();
		return getFrame();
	}
	public Frame skip() {
		return skip(1);
	}

	public int getLegth() {
		return helper.readInt("rec.meta.length");
	}

	public void cacheFrames() {
		for (int i = 0; i <= getLegth(); i++) {
			Location loc = helper.readLocation("rec.track." + i + ".loc");
			EntityType entityType = EntityType.valueOf(helper.readString("rec.track." + i + ".entityType"));
			frames.put(i, new Frame(loc, entityType));
		}
	}
	public void clearCache() {
		frames.clear();
	}

}
