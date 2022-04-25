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
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.lifestealcore.managers.MemberManager;

public class CommandCheck extends Action {

	private String feedbackMessage = "PLAYER has VALUE hearts!";
	
	public CommandCheck(CubePlugin _plugin) {
		super("Check", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
		
	}

	@Override
	protected void setupArguments(List<ActionUse> arguments) {
		arguments.add(ActionUse.create()
			.add(Arg.create("player", "PLAYER"))
		);

		arguments.add(ActionUse.create()
		);
	}

	@Override
	protected Execution approved(Player player, Map<String, String> args) {
		String playerName = args.getOrDefault("player", player.getName());
		
		Player otherPlayer = Bukkit.getPlayer(playerName);
		if (otherPlayer == null)
			return Execution.createFail().setReason("Can't find player: " + playerName);
		
		return Execution.NONE;
	}

	@Override
	protected boolean execute(Player player, Map<String, String> args) {
		String playerName = args.getOrDefault("player", player.getName());
		Player otherPlayer = Bukkit.getPlayer(playerName);
		
		Integer heartCount = MemberManager.getMember(otherPlayer).getHearts();		
		
		Tell.player(player, feedbackMessage
				.replaceAll("PLAYER", playerName)
				.replaceAll("VALUE", heartCount.toString())
		);
		return true;
	}
}
