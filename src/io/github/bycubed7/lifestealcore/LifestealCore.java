package io.github.bycubed7.lifestealcore;
import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.lifestealcore.commands.CommandCheck;
import io.github.bycubed7.lifestealcore.commands.CommandReset;
import io.github.bycubed7.lifestealcore.commands.CommandRevive;
import io.github.bycubed7.lifestealcore.commands.CommandSet;
import io.github.bycubed7.lifestealcore.managers.DeathManager;
import io.github.bycubed7.lifestealcore.managers.MemberManager;

public class LifestealCore extends CubePlugin {

	@Override
	protected void onBoot() {
		banner.add(",--.    ,--.  ,---.                   ,--.                    ,--.");
		banner.add("|  |    `--' /  .-'  ,---.   ,---.  ,-'  '-.  ,---.   ,--,--. |  |");
		banner.add("|  |    ,--. |  `-, | .-. : (  .-'  '-.  .-' | .-. : ' ,-.  | |  |");
		banner.add("|  '--. |  | |  .-' \\   --. .-'  `)   |  |   \\   --. \\ '-'  | |  |");
		banner.add("`-----' `--' `--'    `----' `----'    `--'    `----'  `--`--' `--'");
		banner.add(" ,-----.");
		banner.add("'  .--./  ,---.  ,--.--.  ,---.");
		banner.add("|  |     | .-. | |  .--' | .-. :");
		banner.add("'  '--'\\ ' '-' ' |  |    \\   --.");
		banner.add(" `-----'  `---'  `--'     `----'");
	}

	@Override
	protected void onCommands() {
		new CommandCheck(this);
		//new CommandPause(this);
		new CommandReset(this);
		new CommandRevive(this);
		new CommandSet(this);
	}

	@Override
	protected void onListeners() {}

	@Override
	protected void onManagers() {
		new MemberManager(this);
		new DeathManager(this);
	}

	@Override
	protected void onReady() {}

	@Override
	protected void onStart() {}

}
