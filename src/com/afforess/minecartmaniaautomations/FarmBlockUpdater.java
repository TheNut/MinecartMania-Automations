package com.afforess.minecartmaniaautomations;

import java.util.Collection;
import com.afforess.minecartmaniacore.debug.DebugTimer;

public class FarmBlockUpdater implements Runnable{
	
	private Collection<AbstractBlock> blocks;
	public FarmBlockUpdater(Collection<AbstractBlock> blocks) {
		this.blocks = blocks;
	}

	@Override
	public void run() {
		DebugTimer timer = new DebugTimer("Updating Automated Farming Blocks");
		for (AbstractBlock block : blocks) {
			block.update();
		}
		timer.logProcessTime();
	}

}
