package io.github.bycubed7.lifestealcore.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.lifestealcore.units.Member;

public class MemberManager implements Listener {
	private static CubePlugin plugin;
	private static HashSet<Member> members;
	
	private static int transferAmount = 2;
	
	public MemberManager(CubePlugin _plugin) {
		plugin = _plugin;
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, plugin);
		members = new HashSet<Member>();
		loadFromConfig();
		
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		transferAmount = config.getInt("death.transfer_count");
	}
	
	private static Member createMember(Player player) {
		Member newMember = new Member(player, 20);
		members.add(newMember);
		return newMember;
	}
	
	// Get or create member
	public static Member getMember(Player player) {
		Optional<Member> possibleMember = members.stream().filter(m -> m.getId().equals(player.getUniqueId())).findFirst();
		Member member = possibleMember.orElse(createMember(player));
		return member;
	}
	public static void setMember(Member member) {
		members.remove(member);
		members.add(member);
	}
	
	private static void loadFromConfig() {
        File file = new File(plugin.getDataFolder(), "hearts.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        List<String> list = config.getStringList("hearts");
        
        members.clear();
        for (String data : list)
        	members.add(Member.fromString(data));
	}
	
	public static void saveToConfig() {
        File file = new File(plugin.getDataFolder(), "hearts.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        HashSet<Member> currentMembers = new HashSet<Member>();
        
        // Add / override with our current configuartion
        for (Member member : members) currentMembers.add(member);
        
        // Add on the current configuartion
        List<String> list = config.getStringList("hearts");
        for (String data : list) currentMembers.add(Member.fromString(data));
        
        // Convert the Member hashset to a sting arraylist
        ArrayList<String> data = new ArrayList<String>();
        for (Member member : currentMembers)
        	data.add(member.toString());
        
        // Set to config
        config.set("hearts", data);

        try {
            config.save(file);
        } catch (IOException e) {
            Debug.log(e.getMessage());
        }
	}
	
	public static void onMurder(Member killed, Member killer) {
		killer.addHearts(transferAmount);
		killed.removeHearts(transferAmount);
		killer.updateHearts();
		killed.updateGamemode();
		setMember(killer);
		setMember(killed);
		saveToConfig();
	}
	
	@Deprecated
	public static Optional<Member> findMemberByName(String name) {
		return members.stream().filter(m -> m.getName().equals(name)).findFirst();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		// Make sure the player exists
		Member member = getMember(player);
		member.updateHearts();
		member.updateGamemode();
		saveToConfig();	
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Member member = getMember(player);
		member.updateHearts();
		member.updateGamemode();
	}
}
