package com.afforess.minecartmaniaautomations;
import java.util.HashSet;
import org.bukkit.Location;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.utils.BlockUtils;

public class MinecartManiaActionListener extends MinecartManiaListener{
	
	@SuppressWarnings("unchecked")
	public void onMinecartActionEvent(MinecartActionEvent event) {
		if (!event.isActionTaken()) {
			MinecartManiaMinecart minecart = event.getMinecart();
			if (minecart.isStorageMinecart()) {
				HashSet<Location> previousBlocks = null;
				if (minecart.getDataValue("Previous Farm Blocks") != null) {
					previousBlocks = ((HashSet<Location>)minecart.getDataValue("Previous Farm Blocks"));
				}
				HashSet<AbstractBlock> current = toAbstractBlockSet(BlockUtils.getAdjacentLocations(minecart.minecart.getLocation(), minecart.getRange()));
				if (previousBlocks != null) {
					for (Location loc : previousBlocks) {
						if (current.contains(loc)) {
							current.remove(loc);
						}
					}
				}
				FarmUpdater updater = new FarmUpdater((MinecartManiaStorageCart)minecart, current);
				FarmManager.addTask(updater);
			}
		}
	}

	private static HashSet<AbstractBlock> toAbstractBlockSet(HashSet<Location> set) {
		HashSet<AbstractBlock> newSet = new HashSet<AbstractBlock>(set.size());
		for (Location loc : set) {
			newSet.add(AbstractBlock.fromBlock(loc.getBlock()));
		}
		return newSet;
	}
}
