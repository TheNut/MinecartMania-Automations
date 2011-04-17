package com.afforess.minecartmaniaautomations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.BlockUtils;

public class StorageMinecartUtils {

	public static void doAutoFarm(MinecartManiaStorageCart minecart, Collection<AbstractBlock> blocks) {
		if (minecart.getDataValue("AutoHarvest") == null && minecart.getDataValue("AutoTill") == null && minecart.getDataValue("AutoSeed") == null) {
			return;
		}

		for (AbstractBlock block : blocks) {
			
			//Setup data
			int x = block.getBlockX();
			int y = block.getBlockY();
			int z = block.getBlockZ();
			int aboveId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y+1, z);
			int belowId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y-1, z);
			
			//Harvest fully grown crops first
			if (minecart.getDataValue("AutoHarvest") != null) {
				if (block.getId() == Item.CROPS.getId()) {
					//fully grown
					if (block.getData() == 0x7) {
						minecart.addItem(Item.WHEAT.getId());
						minecart.addItem(Item.SEEDS.getId());
						if ((new Random()).nextBoolean()) { //Randomly add second seed.
							minecart.addItem(Item.SEEDS.getId());
						}
						block.setId(0);
						block.setData(0);
					}
				}
			}
			
			//till soil
			if (minecart.getDataValue("AutoTill") != null) {
				if (block.getId() == Item.GRASS.getId() ||  block.getId() == Item.DIRT.getId()) {
					if (aboveId == Item.AIR.getId()) {
						block.setId(Item.SOIL.getId());
					}
				}
			}

			//Seed tilled land 
			if (minecart.getDataValue("AutoSeed") != null) {
				if (belowId == Item.SOIL.getId()) {
					if (block.getId() == Item.AIR.getId()) {
						if (minecart.removeItem(Item.SEEDS.getId())) {
							block.setId(Item.CROPS.getId());
						}
					}
				}
			}
		}
	}

	public static void doAutoCactusFarm(MinecartManiaStorageCart minecart, Collection<AbstractBlock> blocks) {
		if((minecart.getDataValue("AutoCactus") == null) && (minecart.getDataValue("AutoReCactus") == null)) {
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
			if (minecart.getDataValue("AutoCactus") != null) {

				// Like sugar, we need to break this from the top first. 

				if (block.getId() == Item.CACTUS.getId() && aboveId != Item.CACTUS.getId()) {
					if (belowId == Item.SAND.getId()) {
						if(minecart.getDataValue("AutoReCactus") == null) {
							// Only harvest the bottom if we're not replanting. 
							minecart.addItem(Item.CACTUS.getId());
							block.setId(0);
						}
					} else {
						minecart.addItem(Item.CACTUS.getId());
						block.setId(0);
					}
				}
			}
			
			//Replant Cactus
			if (minecart.getDataValue("AutoReCactus") != null) {
				if (belowId == Item.SAND.getId()) {
					if (block.getId() == Item.AIR.getId()) {

						// Need to check for blocks to the sides of the cactus position 
						// as this would normally block planting.

						int sidemx = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x-1, y, z);
						int sidepx = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x+1, y, z);
						int sidemz = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y, z-1);
						int sidepz = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y, z+1);

						boolean blockcactus = false;

						if(sidemx != Item.AIR.getId()) { blockcactus = true; }
						if(sidepx != Item.AIR.getId()) { blockcactus = true; }
						if(sidemz != Item.AIR.getId()) { blockcactus = true; }
						if(sidepz != Item.AIR.getId()) { blockcactus = true; }

						if (!blockcactus && minecart.removeItem(Item.CACTUS.getId())) {
							block.setId(0);
						}
					}
				}
			}
		}
	}


	public static void doAutoTimber(MinecartManiaStorageCart minecart, Collection<AbstractBlock> blocks) {
		if (minecart.getDataValue("AutoTimber") == null) {
			return;
		}
		HashSet<AbstractBlock> clearedLogs = new HashSet<AbstractBlock>();
		for (AbstractBlock block : blocks) {
			if (block.getId() == Item.LOG.getId() && !clearedLogs.contains(block)) {
				int down = 1;
				while (MinecartManiaWorld.getBlockIdAt(block.getWorld(), block.getBlockX(), block.getBlockY() - down, block.getBlockZ()) == Item.LOG.getId()) {
					down++;
				}
				int baseId = MinecartManiaWorld.getBlockIdAt(block.getWorld(), block.getBlockX(), block.getBlockY() - down, block.getBlockZ());
				//base of tree
				if (isBaseOfTree(baseId)) {
					//Attempt to replant the tree
					AbstractBlock below = block.clone();
					below.setY(block.getBlockY() - down + 1);
					HashSet<AbstractBlock> cleared = new HashSet<AbstractBlock>();
					removeLogs(below, cleared, minecart);
					if (cleared.size() > 0) {
						if (minecart.addItem(Item.LOG.getId(), cleared.size())) {
							clearedLogs.addAll(cleared);
						}
						else {
							break;
						}
					}
					if (cleared.size() > 0 ) {
						if (minecart.contains(Item.SAPLING)) {
							minecart.removeItem(Item.SAPLING.getId());
							below.setId(Item.SAPLING.getId());
						}
					}
					
				}
			}
		}
		clearedLogs.removeAll(blocks);
		blocks.addAll(clearedLogs);
	}
	
	private static boolean isBaseOfTree(int id) {
		return id == Item.DIRT.getId() || id == Item.GRASS.getId();
	}

	private static void removeLogs(AbstractBlock block, HashSet<AbstractBlock> blocks, MinecartManiaStorageCart minecart) {
		HashSet<AbstractBlock> adjacent = getAdjacentBlocks(block);
		adjacent.removeAll(blocks);
		for (AbstractBlock data : adjacent) {
			Item item = Item.getItem(data.getId(), data.getData());
			if (item != null && item.getId() == Item.LOG.getId()) {
				Location below = data.clone();
				below.setY(below.getY() - 1);
				blocks.add(data);
				data.setId(0); 
				data.setData(0);
				removeLogs(data, blocks, minecart);
			}
		}
	}
	
	private static HashSet<AbstractBlock> getAdjacentBlocks(AbstractBlock block) {
		ArrayList<Block> tempList = BlockUtils.getAdjacentBlocks(block, 1);
		HashSet<AbstractBlock> blocks = new HashSet<AbstractBlock>();
		for (Block b : tempList) {
			blocks.add(AbstractBlock.fromBlock(b));
		}
		return blocks;
	}

}
