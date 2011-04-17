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
			MinecartManiaMinecart minecart = event.getMinecart();
			if (minecart.isStorageMinecart()) {
				//Efficiency. Don't farm overlapping tiles repeatedly, waste of time
				int interval = MinecartManiaWorld.getIntValue(minecart.getDataValue("Farm Interval"));
				if (interval > 0) {
					minecart.setDataValue("Farm Interval", interval - 1);
				}
				else {
					minecart.setDataValue("Farm Interval", minecart.getRange());
					updateFarms((MinecartManiaStorageCart)minecart);
				}
			}
		}
	}
	
	private void updateFarms(final MinecartManiaStorageCart minecart) {
		StorageMinecartUtils.doAutoFarm(minecart);
		StorageMinecartUtils.doAutoTimber(minecart);
		StorageMinecartUtils.doAutoCactusFarm(minecart);
		StorageMinecartSugar.doAutoSugarFarm(minecart);
	}
}
