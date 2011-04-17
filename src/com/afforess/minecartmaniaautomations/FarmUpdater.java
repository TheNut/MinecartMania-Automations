package com.afforess.minecartmaniaautomations;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.debug.DebugTimer;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;

public class FarmUpdater implements Runnable {
	private MinecartManiaStorageCart minecart;
	private HashSet<AbstractBlock> blocks;
	public FarmUpdater(MinecartManiaStorageCart minecart, HashSet<AbstractBlock> blocks) {
		this.minecart = minecart;
		this.blocks = blocks;
	}

	@Override
	public void run() {
		MinecartManiaLogger.getInstance().debug("Starting Automated Farming");
		DebugTimer timer = new DebugTimer("Automations");
		StorageMinecartUtils.doAutoFarm(minecart, blocks);
		StorageMinecartUtils.doAutoTimber(minecart, blocks);
		StorageMinecartUtils.doAutoCactusFarm(minecart, blocks);
		StorageMinecartSugar.doAutoSugarFarm(minecart, blocks);
		timer.logProcessTime();
		updatePrevious();
		update();
		FarmManager.finishedTask();
		MinecartManiaLogger.getInstance().debug("Finishing Automated Farming");
		MinecartManiaCore.server.getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.instance, new FarmBlockUpdater(blocks));
	}
	
	public void update() {
		Iterator<AbstractBlock> i = blocks.iterator();
		while(i.hasNext()) {
			AbstractBlock temp = i.next();
			if (!temp.updated()) {
				i.remove();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updatePrevious() {
		HashSet<Location> previousBlocks = null;
		if (minecart.getDataValue("Previous Farm Blocks") != null) {
			previousBlocks = (HashSet<Location>)minecart.getDataValue("Previous Farm Blocks");
			previousBlocks = (HashSet<Location>) previousBlocks.clone();
		}
		if (previousBlocks != null) {
			Iterator<Location> i = previousBlocks.iterator();
			while(i.hasNext()) {
				Location temp = i.next();
				if (temp.toVector().distance(minecart.minecart.getLocation().toVector()) > minecart.getRange()) {
					i.remove();
				}
			}
		}
		else {
			previousBlocks = new HashSet<Location>();
		}
		previousBlocks.addAll(blocks);
		minecart.setDataValue("Previous Farm Blocks", previousBlocks);
	}
}
