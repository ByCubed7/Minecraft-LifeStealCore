package io.github.bycubed7.lifestealcore.commands;

import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.lifestealcore.managers.MemberManager;
import io.github.bycubed7.lifestealcore.units.Member;

public class CommandRevive extends Action {

	private String feedbackMessage = "Revived PLAYER!";

	public CommandRevive(JavaPlugin _plugin) {
		super("Revive", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
	}

	@Override
	protected ActionFailed approved(Player player, String[] args) {
		// Revive <Username> [hearts]
		
		if (args.length == 0) return ActionFailed.ARGUMENTLENGTH;

		Integer transferAmount = 2;
		
		// Check the argument is a number
		if (args.length > 2) {
			try {
				transferAmount = Integer.parseInt(args[1]);
			} 
	        catch (NumberFormatException ex) {
	        	return ActionFailed.USAGE;
	        }
		}
		
		Member member = MemberManager.getMember(player);
		
		// Does the player have enough hearts?
		if (member.getHearts() < transferAmount) {
			Tell.player(player, "You don't have any enough hearts to revive with!");
			return ActionFailed.OTHER;
		}
		
		// Check the member exists
		
		Player otherPlayer = Bukkit.getPlayer(args[0]);
		
		if (otherPlayer == null) {
			Tell.player(player, "Can't find player to revive!");
			return ActionFailed.OTHER;
		}
		Member possibleMember = MemberManager.getMember(otherPlayer);
		
		// Is the other member in
		if (possibleMember.getHearts() > 0) {
			Tell.player(player, "The member is still alive!");
			return ActionFailed.OTHER;
		}
		
		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {
		Member member = MemberManager.getMember(player);
		Member memberToRevive = MemberManager.getMember(Bukkit.getPlayer(args[0]));

		// Get the transfer amount
		Integer transferAmount = 2;
		if (args.length > 2)
			transferAmount = Integer.parseInt(args[1]);
		
		// Transfer the hearts
		member.removeHearts(transferAmount);
		memberToRevive.addHearts(transferAmount);

		member.updateGamemode();
		memberToRevive.updateGamemode();

		MemberManager.setMember(member);
		MemberManager.setMember(memberToRevive);
		
		MemberManager.saveToConfig();	
		
		// Feedback
		Tell.player(player, feedbackMessage.replaceAll("PLAYER", args[0]));
		
		// Tell other player, if they are on
		Optional<Player> possibleOtherPlayer = memberToRevive.getPlayer();
		if (possibleOtherPlayer.isPresent())
			Tell.player(possibleOtherPlayer.get(), player.getDisplayName() + " has revived you!");
		
		return true;
	}

	@Override
	protected List<String> tab(Player player, Command command, String[] args) {
		return null;
	}

}
