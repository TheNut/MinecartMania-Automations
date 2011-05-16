package com.afforess.minecartmaniaautomations;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;

public class MinecartManiaActionListener extends MinecartManiaListener{
	
	public void onMinecartActionEvent(MinecartActionEvent event) {
		if (!event.isActionTaken()) {
			MinecartManiaMinecart minecart = event.getMinecart();
			if (minecart.isStorageMinecart()) {
				//Efficiency. Don't farm overlapping tiles repeatedly, waste of time
				int interval = minecart.getDataValue("Farm Interval") == null ? -1 : (Integer)minecart.getDataValue("Farm Interval");
				if (interval > 0) {
					minecart.setDataValue("Farm Interval", interval - 1);
				}
				else {
					minecart.setDataValue("Farm Interval", minecart.getRange()/2);
					StorageMinecartUtils.doAutoFarm((MinecartManiaStorageCart)minecart);
					StorageMinecartUtils.doAutoTimber((MinecartManiaStorageCart)minecart);
					StorageMinecartUtils.doAutoCactusFarm((MinecartManiaStorageCart)minecart);
					StorageMinecartSugar.doAutoSugarFarm((MinecartManiaStorageCart)minecart);
				}
			}
		}
	}
}
