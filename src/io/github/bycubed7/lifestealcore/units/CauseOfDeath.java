package io.github.bycubed7.lifestealcore.units;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CauseOfDeath {
	Player player;
	DamageCause cause;
	long time;
	
	public CauseOfDeath(Player _player, DamageCause _cause) {
		player = _player;
		cause = _cause;
		
		time = System.currentTimeMillis();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public DamageCause getCause() {
		return cause;
	}
	
	public long getTimeSince() {
		return System.currentTimeMillis() - time;
	}
}
