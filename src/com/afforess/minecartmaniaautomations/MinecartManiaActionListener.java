package com.afforess.minecartmaniaautomations;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.event.MinecartMotionStopEvent;

public class MinecartManiaActionListener extends MinecartManiaListener{
	
	public void onMinecartActionEvent(MinecartActionEvent event) {
		if (!event.isActionTaken()) {
			MinecartManiaMinecart minecart = event.getMinecart();
			if (minecart.isStorageMinecart()) {
				//Efficiency. Don't farm overlapping tiles repeatedly, waste of time
				int interval = MinecartManiaWorld.getIntValue(minecart.getDataValue("Farm Interval"));
				if (interval > 0) {
					minecart.setDataValue("Farm Interval", interval - 1);
				}
				else {
					minecart.setDataValue("Farm Interval", minecart.getRange() * 2);
					updateFarms((MinecartManiaStorageCart)minecart);
				}
			}
		}
	}
	
	public void onMinecartStopEvent(MinecartMotionStopEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		if (minecart.isStorageMinecart()) {
			minecart.setDataValue("Farm Interval", minecart.getRange() * 2);
			updateFarms((MinecartManiaStorageCart)minecart);
		}
	}
	
	private void updateFarms(final MinecartManiaStorageCart minecart) {
		Runnable run = new Runnable() {
			public void run() {
				StorageMinecartUtils.doAutoFarm(minecart);
			}
		};
		MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
		
		run = new Runnable() {
			public void run() {
				StorageMinecartUtils.doAutoTimber(minecart);
			}
		};
		MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
		
		run = new Runnable() {
			public void run() {
				StorageMinecartUtils.doAutoCactusFarm(minecart);
			}
		};
		MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
		
		run = new Runnable() {
			public void run() {
				StorageMinecartSugar.doAutoSugarFarm(minecart);
			}
		};
		MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, run);
	}
}
