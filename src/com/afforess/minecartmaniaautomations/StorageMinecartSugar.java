package com.afforess.minecartmaniaautomations;

/*
 * Created by Spathizilla, used with his permission.
 * Afforess, 2/27/11
 */

import java.util.Collection;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;

public class StorageMinecartSugar {
	public static void doAutoSugarFarm(MinecartManiaStorageCart minecart, Collection<AbstractBlock> blocks) {
		if((minecart.getDataValue("AutoSugar") == null) && (minecart.getDataValue("AutoPlant") == null)) {
			return;
		}
		for (AbstractBlock block : blocks) {
			//Setup data
			int x = block.getBlockX();
			int y = block.getBlockY();
			int z = block.getBlockZ();

			int aboveId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y+1, z); 
			int belowId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y-1, z);
			
			//Harvest Sugar
			if (minecart.getDataValue("AutoSugar") != null) {
			
				// Check for sugar blocks and ensure they're the top one in the stack. 
				// Breaking sugar below the top will result in cane on the track which can stop the cart
				// until autocollection is turned back on.

				if (block.getId() == Item.SUGAR_CANE_BLOCK.getId() && aboveId != Item.SUGAR_CANE_BLOCK.getId()) {
					if (belowId == Item.GRASS.getId() ||  belowId == Item.DIRT.getId()) {
						if(minecart.getDataValue("AutoPlant") == null) {
							minecart.addItem(Item.SUGAR_CANE.getId());
							block.setId(0);
						}
					} else {
						minecart.addItem(Item.SUGAR_CANE.getId());
						block.setId(0);
					}
				}
			}
			
			//Replant cane
			if (minecart.getDataValue("AutoPlant") != null) {
				if (belowId == Item.GRASS.getId() ||  belowId == Item.DIRT.getId()) {
					if (block.getId() == Item.AIR.getId()) {

						// Need to check for water or the cane will not plant.
						int water1 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x+1, y-1, z);
						int water2 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x-1, y-1, z);
						int water3 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y-1, z+1);
						int water4 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y-1, z-1);

						boolean foundwater = false;

						if(water1 == Item.WATER.getId() || water1 == Item.STATIONARY_WATER.getId()) foundwater = true;
						else if(water2 == Item.WATER.getId() || water2 == Item.STATIONARY_WATER.getId()) foundwater = true;
						else if(water3 == Item.WATER.getId() || water3 == Item.STATIONARY_WATER.getId()) foundwater = true;
						else if(water4 == Item.WATER.getId() || water4 == Item.STATIONARY_WATER.getId()) foundwater = true;

						if(foundwater) {
							if (minecart.removeItem(Item.SUGAR_CANE.getId())) {
								block.setId(Item.SUGAR_CANE_BLOCK.getId());
							}
						}
					}
				}
			}
		}
	}
}

