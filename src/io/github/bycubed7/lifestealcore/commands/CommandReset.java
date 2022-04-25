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
import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.lifestealcore.managers.MemberManager;
import io.github.bycubed7.lifestealcore.units.Member;

public class CommandReset extends Action {

	private String feedbackMessage = "Your hearts have been reset!";

	public CommandReset(CubePlugin _plugin) {
		super("Reset", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());

	}

	@Override
	protected void setupArguments(List<ActionUse> arguments) {
		arguments.add( ActionUse.create()
		);
		
		arguments.add( ActionUse.create()
			.add(Arg.create("player", "PLAYER"))
		);
	}

	@Override
	protected Execution approved(Player player, Map<String, String> args) {
		
		//ArrayList<Member> members = new ArrayList<Member>();
		
		Debug.log(args.toString());
		
		// Is the player resetting themself or someone else?
		String playerNames = args.get("Player");
		if (playerNames == null);
			//members.add(MemberManager.GetMember(player));
		else
			for (String playerName : playerNames.split(" ")) {
				Player otherplayer = Bukkit.getPlayer(playerName);
				
				if (otherplayer == null) {
					return Execution.createFail()
							.setReason("Can't find player " + playerName)
							.dontShowUsage();
				}
				
				//members.add(memberToAdd.get());
			}
		
		
		return Execution.NONE;
	}

	@Override
	protected boolean execute(Player player, Map<String, String> args) {
		ArrayList<Member> members = new ArrayList<Member>();
				
		// Is the player resetting themself or someone else?
		String playerNames = args.get("Player");
		if (playerNames == null)
			members.add(MemberManager.getMember(player));
		else
			for (String playerName : playerNames.split(" "))
				members.add(MemberManager.getMember(Bukkit.getPlayer(playerName)));
		
		members.forEach(m -> {
			m.setHearts(20);
			if (m.getPlayer().isPresent()) 
				Tell.player(m.getPlayer().get(), feedbackMessage);
		});
		
		return true;
	}
}
