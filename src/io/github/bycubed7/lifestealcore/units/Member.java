package io.github.bycubed7.lifestealcore.units;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;


public class Member implements Serializable {
	private static final long serialVersionUID = 6574212432289190073L;
	private UUID id;
	private String name;
	private int hearts;
	
	public Member(Player player, Integer _hearts) {
		id = player.getUniqueId();
		name = player.getDisplayName();
		hearts = _hearts;
	}
	
	public Member(UUID _id, String _name, Integer _hearts) {
		id = _id;
		name = _name;
		hearts = _hearts;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	
	// Hearts
	
	public int getHearts() {
		return hearts;
	}
	
	public void setHearts(int amount) {
		hearts = amount;
		hearts = Math.max(0, hearts);
	}
	
	public void addHearts(int amount) {
		hearts = hearts + amount;
		hearts = Math.max(0, hearts);
	}
	
	public void removeHearts(int amount) {
		hearts = hearts - amount;
		hearts = Math.max(0, hearts);
	}
	
	public boolean updateHearts() {
		Optional<Player> possiblePlayer = getPlayer();
		if (possiblePlayer.isEmpty()) return false;
		
		possiblePlayer.get().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hearts);
		return true;
	}
	
	public boolean updateGamemode() {
		Optional<Player> possiblePlayer = getPlayer();
		if (possiblePlayer.isEmpty()) return false;
		Player player = possiblePlayer.get();

		// Make sure player is in survival or spectator
		if (!player.getGameMode().equals(GameMode.SURVIVAL) &&
			!player.getGameMode().equals(GameMode.SPECTATOR))
			return false;
		
		if (hearts == 0) player.setGameMode(GameMode.SPECTATOR);
		else player.setGameMode(GameMode.SURVIVAL);
		
		return true;
	}
	
	public Optional<Player> getPlayer() {
		return Optional.ofNullable(Bukkit.getPlayer(id));
	}
	
	static public Member fromString(String data) {
		String[] dataSplit = data.split(" ");
		String name = dataSplit[0];
		UUID id = UUID.fromString(dataSplit[1]);
		int heartCount = Integer.parseInt(dataSplit[2]);
		return new Member(id, name, heartCount);
	}
	
	@Override
	public String toString() {
		return name +" "+ id +" "+ hearts;
	}
	
	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof Member))
			return false;

		Member c = (Member) o;

		if (!id.equals(c.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}