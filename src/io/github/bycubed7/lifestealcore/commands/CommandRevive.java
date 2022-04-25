package io.github.bycubed7.lifestealcore.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

public class CommandRevive extends Action {

	private String feedbackMessage = "Revived PLAYER!";

	public CommandRevive(CubePlugin _plugin) {
		super("Revive", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
		
	}

	@Override
	protected void setupArguments(List<ActionUse> arguments) {
		arguments.add( ActionUse.create()
			.add(Arg.create("player", "PLAYER"))
		);
	}

	@Override
	protected Execution approved(Player player, Map<String, String> args) {
		// Revive <Username> [hearts]

		String targetPlayerName = args.get("player");
		String targetTransferAmount = args.getOrDefault("value", "2");
		
		if (targetPlayerName == null) 
			return Execution.createFail()
					.setReason("Can't find player to revive!");
		
		Player otherPlayer = Bukkit.getPlayer(targetPlayerName);
		if (otherPlayer == null)
			return Execution.createFail().setReason("Can't find player: " + targetPlayerName);
				
		// Check the argument is a number
		int transferAmount;
		try {
			transferAmount = Integer.parseInt(targetTransferAmount);
		} 
	    catch (NumberFormatException ex) {
	       	return Execution.USAGE;
	    }
		
		Member member = MemberManager.getMember(player);
		
		// Does the player have enough hearts?
		if (member.getHearts() < transferAmount)
			return Execution.createFail()
					.setReason("You don't have any enough hearts to revive with!");

		Member possibleMember = MemberManager.getMember(otherPlayer);
		
		// Is the other member in
		if (possibleMember.getHearts() > 0)
			return Execution.createFail()
					.setReason("The member is still alive!");
		
		return Execution.NONE;
	}

	@Override
	protected boolean execute(Player player, Map<String, String> args) {
		String targetPlayerName = args.get("player");
		String targetTransferAmount = args.getOrDefault("value", "2");
		
		Member member = MemberManager.getMember(player);
		Member memberToRevive = MemberManager.getMember(Bukkit.getPlayer(targetPlayerName));

		// Get the transfer amount
		int transferAmount = Integer.parseInt(targetTransferAmount);
		
		// Transfer the hearts
		member.removeHearts(transferAmount);
		memberToRevive.addHearts(transferAmount);

		member.updateGamemode();
		memberToRevive.updateGamemode();

		MemberManager.setMember(member);
		MemberManager.setMember(memberToRevive);
		
		MemberManager.saveToConfig();	
		
		// Feedback
		Tell.player(player, feedbackMessage.replaceAll("PLAYER", targetPlayerName));
		
		// Tell other player, if they are on
		Optional<Player> possibleOtherPlayer = memberToRevive.getPlayer();
		if (possibleOtherPlayer.isPresent())
			Tell.player(possibleOtherPlayer.get(), player.getDisplayName() + " has revived you!");
		
		return true;
	}

}
