package io.github.bycubed7.lifestealcore.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.corecubes.unit.BiHashMap;
import io.github.bycubed7.lifestealcore.units.CauseOfDeath;
import io.github.bycubed7.lifestealcore.units.Member;

public class DeathManager implements Listener {
	
	// Player that died, the cause, and the player who's responsible
	BiHashMap<Player, DamageCause, CauseOfDeath> causes;
	
	public static String message = "KILLED died via KILLER's actions!";
	
	public DeathManager(CubePlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		causes = new BiHashMap<Player, DamageCause, CauseOfDeath>();
		
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		message = config.getString("death.message");
	}

	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent event) {		
		Player playerDead = event.getEntity();

		// Get source
		EntityDamageEvent lastDamageEvent = event.getEntity().getLastDamageCause();
		DamageCause lastDamageEventType = lastDamageEvent.getCause();
		
		// Try to find cause
		CauseOfDeath causeOfDeath = causes.get(playerDead, lastDamageEventType);
		if (causeOfDeath != null) {
			Player playerResponsible = causeOfDeath.getPlayer();	
		
			// Transfer hearts
			Member memberDead = MemberManager.getMember(playerDead);
			Member memberResponsible = MemberManager.getMember(playerResponsible);
			

			message = message.replaceAll("KILLED", playerDead.getDisplayName());
			message = message.replaceAll("KILLER", playerResponsible.getDisplayName());
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				Tell.player(player, "" + ChatColor.BOLD + ChatColor.YELLOW + message);
			}
			
			MemberManager.onMurder(memberDead, memberResponsible);
		}
		
		// Found or not, clear the past
		causes.remove(playerDead);
	}
	
	@EventHandler 
	public void OnPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (!(event.getDamager() instanceof Player)) return;
		Player playerHurt = (Player) event.getEntity();
		Player playerDamaging = (Player) event.getDamager();
		
		// Add to potential kills
		causes.put(playerHurt, DamageCause.ENTITY_ATTACK, new CauseOfDeath(playerDamaging, DamageCause.ENTITY_ATTACK));

		// Could be enchanted with fire aspect
		causes.put(playerHurt, DamageCause.FIRE_TICK, new CauseOfDeath(playerDamaging, DamageCause.FIRE_TICK));

		// Could cause the player to fall to thier death
		causes.put(playerHurt, DamageCause.FALL, new CauseOfDeath(playerDamaging, DamageCause.FALL));
		
		// The hit could cause the player to fall into the void
		causes.put(playerHurt, DamageCause.VOID, new CauseOfDeath(playerDamaging, DamageCause.VOID));
		
		// Could knock the player into lava
		causes.put(playerHurt, DamageCause.LAVA, new CauseOfDeath(playerDamaging, DamageCause.LAVA));
		
		// Could knock the player into fire?
		causes.put(playerHurt, DamageCause.FIRE, new CauseOfDeath(playerDamaging, DamageCause.FIRE));	
	}

	@EventHandler
	public void OnPlayerProjectileHitPlayer(ProjectileHitEvent event) {
		// Check the arrow was from a player
		// Check the the projectile hit a player
		if(!(event.getEntity().getShooter() instanceof Player)) return;
		if (!(event.getHitEntity() instanceof Player)) return;

		Player playerDamaging = (Player) event.getEntity().getShooter();
		Player playerHurt = (Player) event.getHitEntity();

		// Add to potential kills
		causes.put(playerHurt, DamageCause.PROJECTILE, new CauseOfDeath(playerDamaging, DamageCause.PROJECTILE));
		
		// Could be enchanted with flame
		causes.put(playerHurt, DamageCause.FIRE_TICK, new CauseOfDeath(playerDamaging, DamageCause.FIRE_TICK));

		// Could cause the player to fall to thier death
		causes.put(playerHurt, DamageCause.FALL, new CauseOfDeath(playerDamaging, DamageCause.FALL));
		
		// Could cause the player to fall into the void
		causes.put(playerHurt, DamageCause.VOID, new CauseOfDeath(playerDamaging, DamageCause.VOID));
		
		// Could knock the player into lava
		causes.put(playerHurt, DamageCause.LAVA, new CauseOfDeath(playerDamaging, DamageCause.LAVA));
		
		// Could knock the player into fire?
		causes.put(playerHurt, DamageCause.FIRE, new CauseOfDeath(playerDamaging, DamageCause.FIRE));	
		
		// Could get via a trident via lightning
		causes.put(playerHurt, DamageCause.LIGHTNING, new CauseOfDeath(playerDamaging, DamageCause.LIGHTNING));	
		
		// Could be hit with a splash poition of harming
		//causes.put(playerHurt, DamageCause.MAGIC, new CauseOfDeath(playerDamaging, DamageCause.MAGIC));	
	}
	
	@EventHandler 
	public void OnPlayerDamageEnderCrystal(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof EnderCrystal)) return;
		if (!(event.getDamager() instanceof Player)) return;
		EnderCrystal enderCrystal = (EnderCrystal) event.getEntity();
		Player playerDamaging = (Player) event.getDamager();
		
		for (Entity entity : enderCrystal.getNearbyEntities(10,10,10)) {
			if (!(entity instanceof Player)) continue;
			Player player = (Player) entity;
			// Add to potential kills
			causes.put(player, DamageCause.ENTITY_EXPLOSION, new CauseOfDeath(playerDamaging, DamageCause.ENTITY_EXPLOSION));
		}
	}

	@EventHandler
	public void OnPlayerProjectileHitEnderCrystal(ProjectileHitEvent event) {
		// Check the projectile was from a player
		// Check the the projectile hit a player
		if(!(event.getEntity().getShooter() instanceof Player)) return;
		if (event.getHitEntity() == null) return;
		if (!(event.getHitEntity().getType().equals(EntityType.ENDER_CRYSTAL))) return;

		Player playerDamaging = (Player) event.getEntity().getShooter();

		for (Entity entity : event.getHitEntity().getNearbyEntities(10,10,10)) {
			if (!(entity instanceof Player)) continue;
			Player player = (Player) entity;
			// Add to potential kills
			causes.put(player, DamageCause.ENTITY_EXPLOSION, new CauseOfDeath(playerDamaging, DamageCause.ENTITY_EXPLOSION));
		}
		
	}
	
}
