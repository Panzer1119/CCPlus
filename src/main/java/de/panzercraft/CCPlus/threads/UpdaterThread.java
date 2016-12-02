package de.panzercraft.CCPlus.threads;

import java.time.Duration;
import java.time.Instant;

import de.panzercraft.CCPlus.Handler.PlayerHandler;
import net.minecraft.client.Minecraft;

public class UpdaterThread extends Thread {
	
	public long delay = 1;
	public long delay_coord_logger = 1000;
	
	@Override
	public void run() {
		final Instant started = Instant.now();
		boolean run = true;
		while(run) {
			final Instant now = Instant.now();
			final Duration duration = Duration.between(started, now);
			if(duration.toMillis() % delay_coord_logger == 0) {
				if(Minecraft.getMinecraft().theWorld != null) {
					PlayerHandler.logPlayers(now);
				}
			}
			try {
				Thread.sleep(delay);
			} catch (Exception ex) {
			}
		}
	}

}
