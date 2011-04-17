package com.afforess.minecartmaniaautomations;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.afforess.minecartmaniacore.utils.ComparableLocation;

public class AbstractBlock extends ComparableLocation{
	private int id = 0;
	private int data = 0;
	private boolean dirty = false;
	public AbstractBlock(Location location, int id, int data) {
		super(location);
		this.id = id;
		this.data = data;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		dirty = true;
		this.id = id;
	}
	
	public int getData() {
		return this.data;
	}
	
	public void setData(int data) {
		dirty = true;
		this.data = data;
	}
	
	public boolean updated() {
		return dirty;
	}
	
	public void update() {
		if (getBlock().getTypeId() != id && dirty) {
			getBlock().setTypeId(id);
		}
		if (getBlock().getData() != data && dirty) {
			getBlock().setData((byte) data);
		}
	}
	
	public AbstractBlock clone() {
		return new AbstractBlock(new Location(getWorld(), getX(), getY(), getZ()), id, data);
	}
	
	public static AbstractBlock fromBlock(Block block) {
		return new AbstractBlock(block.getLocation(), block.getTypeId(), block.getData());
	}
	
	

}
