package io.github.bycubed7.lifestealcore.commands;

import java.util.ArrayList;
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
import io.github.bycubed7.lifestealcore.units.Member;

public class CommandPause extends Action {

	public static List<Member> paused;

	private String feedbackMessage = "STATE PLAYER!";

	public CommandPause(CubePlugin _plugin) {
		super("Pause", _plugin);
		
		paused = new ArrayList<Member>();
		
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
	}

	@Override
	protected void setupArguments(List<ActionUse> arguments) {
		arguments.add(ActionUse.create()
			.add(Arg.create("player", "PLAYER").setRecurring(true))
		);
		
		arguments.add(ActionUse.create()
		);
	}

	@Override
	protected Execution approved(Player player, Map<String, String> args) {
		List<Player> foundPlayers = new ArrayList<Player>();

		String playerNames = args.get("player");
		if (playerNames == null)
			foundPlayers.add(player);  
		else {
			for (String playerName : playerNames.split(" ")) {
				Player foundPlayer = Bukkit.getPlayer(playerName);  
				
				if (foundPlayer == null)
					return Execution.createFail()
							.setReason("Can't find player " + playerName)
							.dontShowUsage();
				
				foundPlayers.add(foundPlayer);
			}
			
		}
				
		// If the given player is null
		if (foundPlayers.size() == 0) 
			return Execution.createFail()
					.setReason("No players listed!")
					.dontShowUsage();
		
		return Execution.NONE;
	}

	@Override
	protected boolean execute(Player player, Map<String, String> args) {
		List<Player> foundPlayers = new ArrayList<Player>();
		
		String playerNames = args.get("player");
		if (playerNames == null)
			foundPlayers.add(player);  
		else {
			for (String playerName : playerNames.split(" "))
				foundPlayers.add( Bukkit.getPlayer(playerName));
		}
		
		for (Player foundPlayer : foundPlayers) {
			Member member = MemberManager.getMember(foundPlayer);
			
			if (paused.contains(member)) {
				paused.remove(member);
				Tell.player(player, feedbackMessage
						.replace("STATE", "Resumed")
						.replace("PLAYER", member.getName())
				);
			}
			else {
				paused.add(member);
				Tell.player(player, feedbackMessage
						.replace("STATE", "Paused")
						.replace("PLAYER", member.getName())
				);
			}
		}
				
		return true;
	}
}
