package io.github.bycubed7.lifestealcore.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionUse;
import io.github.bycubed7.corecubes.commands.Arg;
import io.github.bycubed7.corecubes.commands.Execution;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.lifestealcore.managers.MemberManager;
import io.github.bycubed7.lifestealcore.units.Member;

public class CommandSet extends Action {

	private String feedbackMessage = "Updated your hearts!";
	
	public CommandSet(CubePlugin _plugin) {
		super("Set", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());

	}

	@Override
	protected void setupArguments(List<ActionUse> arguments) {
		arguments.add(
			ActionUse.create()
				.add(Arg.create("player", "PLAYER"))
				.add(Arg.create("value", "20"))
		);
		
		arguments.add(
			ActionUse.create()
				.add(Arg.create("value", "20"))
		);
	}

	@Override
	protected Execution approved(Player player, Map<String, String> args) {
		String targetPlayerName = args.get("player");
		String targetTransferAmount = args.getOrDefault("value", "2");
		
		Debug.log("targetPlayerName : " + targetPlayerName);
		Debug.log("targetTransferAmount : " + targetTransferAmount);
		
		if (targetPlayerName == null) 
			return Execution.createFail()
					.setReason("Can't find player to set!");
		
		// Check the argument is a number
		try {
			Integer.parseInt(targetTransferAmount);
		} 
	    catch (NumberFormatException ex) {
	     	return Execution.USAGE;
	    }
		
		return Execution.NONE;
	}

	@Override
	protected boolean execute(Player player, Map<String, String> args) {
		String targetPlayerName = args.get("player");
		String targetTransferAmount = args.getOrDefault("value", "2");

		// Iterate through all except the last argument
		Player foundPlayer = Bukkit.getPlayer(targetPlayerName);
		int transferAmount = Integer.parseInt(targetTransferAmount);
			
		Member member = MemberManager.getMember(foundPlayer);
		member.setHearts(transferAmount);
		member.updateHearts();
		member.updateGamemode();
		MemberManager.setMember(member);
		
		Tell.player(foundPlayer, feedbackMessage);
		
		
		MemberManager.saveToConfig();
		
		return true;
	}
}
