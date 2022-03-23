package io.github.bycubed7.lifestealcore.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.lifestealcore.managers.MemberManager;
import io.github.bycubed7.lifestealcore.units.Member;

public class CommandSet extends Action {

	private String feedbackMessage = "Updated hearts!";
	
	public CommandSet(JavaPlugin _plugin) {
		super("Set", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
	}

	@Override
	protected ActionFailed approved(Player player, String[] args) {

		// TODO: set <number>
		// set [username] <number>

		if (args.length < 2) return ActionFailed.ARGUMENTLENGTH;
		
		// Check the argument is a number
		try {
			Integer.parseInt(args[args.length - 1]);
		} 
	    catch (NumberFormatException ex) {
	     	return ActionFailed.USAGE;
	    }
		
		// Iterate through all except the last argument
		for (int i = 0; i < args.length - 1; i++) {
			Player foundPlayer = Bukkit.getPlayer(args[i]);  
			if (foundPlayer == null) {
				Tell.player(player, "Can't find player " + args[i]);
				return ActionFailed.OTHER;
			}
		}
		
		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {
		
		// Check the argument is a number
		Integer setAmount = Integer.parseInt(args[args.length - 1]);
		
		// Iterate through all except the last argument
		for (int i = 0; i < args.length - 1; i++) {
			Player foundPlayer = Bukkit.getPlayer(args[i]);
			
			Member member = MemberManager.getMember(foundPlayer);
			member.setHearts(setAmount);
			member.updateHearts();
			member.updateGamemode();
			MemberManager.setMember(member);
			
			Tell.player(foundPlayer, "Updated your hearts!");
		}
		MemberManager.saveToConfig();
		
		return true;
	}

	@Override
	protected List<String> tab(Player arg0, Command arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
