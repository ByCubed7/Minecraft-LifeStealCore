package io.github.bycubed7.lifestealcore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.lifestealcore.managers.MemberManager;
import io.github.bycubed7.lifestealcore.units.Member;

public class CommandReset extends Action {

	private String feedbackMessage = "Your hearts have been reset!";

	public CommandReset(JavaPlugin _plugin) {
		super("Reset", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
	}

	@Override
	protected ActionFailed approved(Player player, String[] args) {
		
		//ArrayList<Member> members = new ArrayList<Member>();
		
		// Is the player resetting themself or someone else?
		if (args.length == 0);
			//members.add(MemberManager.GetMember(player));
		else
			for (String playerName : args) {
				Optional<Member> memberToAdd = MemberManager.findMemberByName(playerName);
				
				if (memberToAdd.isEmpty()) {
					Tell.player(player, "Can not find player: " + playerName + "!");
					return ActionFailed.OTHER;
				}
				
				//members.add(memberToAdd.get());
			}
		
		
		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {
		ArrayList<Member> members = new ArrayList<Member>();
		
		// Is the player resetting themself or someone else?
		if (args.length == 0)
			members.add(MemberManager.getMember(player));
		else
			for (String playerName : args)
				members.add(MemberManager.findMemberByName(playerName).get());
		
		members.forEach(m -> {
			m.setHearts(20);
			if (m.getPlayer().isPresent()) 
				Tell.player(m.getPlayer().get(), feedbackMessage);
		});
		
		return true;
	}


	@Override
	protected List<String> tab(Player arg0, Command arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
