package com.afforess.minecartmaniaautomations;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;

public class MinecartManiaActionListener extends MinecartManiaListener{
	
	public void onMinecartActionEvent(MinecartActionEvent event) {
		if (!event.isActionTaken()) {
			final MinecartManiaMinecart minecart = event.getMinecart();
			if (minecart.isStorageMinecart()) {
				//Efficiency. Don't farm overlapping tiles repeatedly, waste of time
				int interval = MinecartManiaWorld.getIntValue(minecart.getDataValue("Farm Interval")) * 4;
				if (interval > 0) {
					minecart.setDataValue("Farm Interval", interval - 1);
				}
				else {
					minecart.setDataValue("Farm Interval", minecart.getEntityDetectionRange());
				
					//Create a separate thread for each instead of running them all on 1 parallel thread
					Runnable run = new Runnable() {
						public void run() {
							StorageMinecartUtils.doAutoFarm((MinecartManiaStorageCart)minecart);
						}
					};
					MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
					
					run = new Runnable() {
						public void run() {
							StorageMinecartUtils.doAutoTimber((MinecartManiaStorageCart)minecart);
						}
					};
					MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
					
					run = new Runnable() {
						public void run() {
							StorageMinecartUtils.doAutoCactusFarm((MinecartManiaStorageCart)minecart);
						}
					};
					MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
					
					run = new Runnable() {
						public void run() {
							StorageMinecartSugar.doAutoSugarFarm((MinecartManiaStorageCart)minecart);
						}
					};
					MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
					
					//run = new Runnable() {
					//	public void run() {
					//		StorageMinecartUtils.doAutoFertilize((MinecartManiaStorageCart)minecart);
					//	}
					//};
					//MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
				}
			}
		}
	}
}
