package io.github.bycubed7.lifestealcore.commands;

import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.lifestealcore.managers.MemberManager;
import io.github.bycubed7.lifestealcore.units.Member;

public class CommandCheck extends Action {

	private String feedbackMessage = "PLAYER has VALUE hearts!";
	
	public CommandCheck(JavaPlugin _plugin) {
		super("Check", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
	}

	@Override
	protected ActionFailed approved(Player player, String[] args) {
		if (args.length == 0) return ActionFailed.ARGUMENTLENGTH;
		
		Optional<Member> possibleMember = MemberManager.findMemberByName(args[0]);
		if (possibleMember.isEmpty()) {
			Tell.player(player, "Can't find player!");
			return ActionFailed.OTHER;
		}
		
		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {
		Integer heartCount = MemberManager.findMemberByName(args[0]).get().getHearts();		
		Tell.player(player, feedbackMessage
				.replaceAll("PLAYER", args[0])
				.replaceAll("VALUE", heartCount.toString())
		);
		return true;
	}

	@Override
	protected List<String> tab(Player player, Command command, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}