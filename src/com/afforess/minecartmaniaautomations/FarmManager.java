package com.afforess.minecartmaniaautomations;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.afforess.minecartmaniacore.MinecartManiaCore;

public class FarmManager {
	private static ConcurrentLinkedQueue<Runnable> actions = new ConcurrentLinkedQueue<Runnable>();
	private static volatile boolean busy = false;
	
	public static void addTask(Runnable task) {
		actions.add(task);
		if (!busy) {
			updateQueue();
		}
	}
	
	public static void updateQueue() {
		busy = true;
		Runnable task = actions.poll();
		if (task != null) {
			MinecartManiaCore.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaCore.instance, task);
		}
		else {
			busy = false;
		}
	}
	
	public static void finishedTask() {
		busy = false;
		updateQueue();
	}

}
