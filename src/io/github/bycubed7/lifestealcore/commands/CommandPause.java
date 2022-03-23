package io.github.bycubed7.lifestealcore.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.ActionFailed;
import io.github.bycubed7.corecubes.managers.ConfigManager;

public class CommandPause extends Action {

	private String feedbackMessage = "Paused PLAYER!";

	public CommandPause(JavaPlugin _plugin) {
		super("Pause", _plugin);
		ConfigManager config = new ConfigManager(plugin, "LifeStealCore.yml");
		feedbackMessage = config.getString("messages."+name.toLowerCase());
	}

	@Override
	protected ActionFailed approved(Player arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean execute(Player arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected List<String> tab(Player arg0, Command arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
