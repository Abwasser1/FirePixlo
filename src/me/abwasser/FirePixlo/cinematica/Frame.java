package me.abwasser.FirePixlo.cinematica;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class Frame {

	public Location loc;
	public @Nullable EntityType entity;

	public Frame(Location loc, EntityType entity) {
		this.loc = loc;
		this.entity = entity;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public EntityType getEntity() {
		return entity;
	}

	public void setEntity(EntityType entity) {
		this.entity = entity;
	}

}
