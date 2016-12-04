package de.panzercraft.CCPlus.threads;

import java.time.Duration;
import java.time.Instant;

import de.panzercraft.CCPlus.CCPlus;
import de.panzercraft.CCPlus.Handler.PlayerHandler;
import net.minecraft.client.Minecraft;

public class UpdaterThread extends Thread {
	
	public long delay = 1;
	public long delay_coord_logger = 100;
	public long delay_achievements = 100;
	
	@Override
	public void run() {
		final Instant started = Instant.now();
		boolean run = true;
		while(run) {
			final Instant now = Instant.now();
			final Duration duration = Duration.between(started, now);
			try {
				if(CCPlus.debug_enabled) {
					if(CCPlus.debug_hawk_eye_enabled) {
						if(PlayerHandler.isInGame() && duration.toMillis() % delay_coord_logger == 0) {
							PlayerHandler.logPlayers(now);
						}
					}
				}
			} catch (Exception ex) {
			}
			try {
				if(PlayerHandler.isInGame() && duration.toMillis() % delay_achievements == 0) {
					PlayerHandler.checkAchievements();
				}
			} catch (Exception ex) {
			}
			try {
				Thread.sleep(delay);
			} catch (Exception ex) {
			}
		}
	}

}
