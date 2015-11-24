package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class AddExecutiveExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Player p = ctx.<Player> getOne("player").get();
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;
			
			if (ConfigManager.getTeams().contains(townName))
			{
				if (ConfigManager.getLeader(townName).equals(player.getUniqueId().toString()))
				{
					try
					{
						if (ConfigManager.getMembers(townName).contains(p.getUniqueId().toString()))
						{
							ConfigManager.addTeamExecutive(townName, p.getUniqueId().toString());
							player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Added " + p.getName() + " as an exeuctive of town " + townName));
						}
						else
						{
							player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Player is not a member of this town!"));
						}
					}
					catch (NullPointerException e)
					{
						player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Failed to add town executive!"));
					}
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You aren't the leader of this town!"));
				}
			}
			else
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Town does not exist!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /addexecutive!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /addexecutive!"));
		}

		return CommandResult.success();
	}
}