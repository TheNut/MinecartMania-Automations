package com.afforess.minecartmaniaautomations;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecartManiaAutomations extends JavaPlugin{
	public static Logger log;
	public static Server server;
	public static PluginDescriptionFile description;
	public static MinecartManiaActionListener listener = new MinecartManiaActionListener();

	public void onEnable() {
		server = this.getServer();
		description = this.getDescription();
		log = Logger.getLogger("Minecraft");
		getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.Normal, this);
		log.info( description.getName() + " version " + description.getVersion() + " is enabled!" );
	}

	public void onDisable() {
	}


}
