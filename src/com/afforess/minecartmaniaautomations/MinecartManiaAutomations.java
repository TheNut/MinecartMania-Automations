package com.afforess.minecartmaniaautomations;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;

public class MinecartManiaAutomations extends JavaPlugin{
	public static MinecartManiaLogger log = MinecartManiaLogger.getInstance();
	public static Server server;
	public static PluginDescriptionFile description;
	public static MinecartManiaActionListener listener = new MinecartManiaActionListener();

	public void onEnable() {
		server = this.getServer();
		description = this.getDescription();
		getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.Normal, this);
		log.info( description.getName() + " version " + description.getVersion() + " is enabled!" );
	}

	public void onDisable() {
	}


}
