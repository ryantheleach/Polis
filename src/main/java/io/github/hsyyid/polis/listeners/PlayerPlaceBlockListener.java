package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;

public class PlayerPlaceBlockListener
{	
	@Listener
	public void onPlayerPlaceBlock(ChangeBlockEvent.Place event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();
			
			for (Transaction<BlockSnapshot> transaction : event.getTransactions())
			{
				String isClaimed = ConfigManager.isClaimed(transaction.getFinal().getLocation().get());
					
				if(!isClaimed.equals("false"))
				{
					if(isClaimed.equals("SafeZone") && player.hasPermission("polis.claim.admin.modify"))
					{
						return;
					}
					
					if(Polis.adminBypassMode.contains(player.getUniqueId()))
					{
						return;
					}
					
					String playerTeamName = null;

					for (String team : ConfigManager.getTeams())
					{
						ArrayList<String> uuids = ConfigManager.getMembers(team);
						if (uuids.contains(player.getUniqueId().toString()))
						{
							playerTeamName = team;
							break;
						}
						else if (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()))
						{
							playerTeamName = team;
							break;
						}
						else if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
						{
							playerTeamName = team;
							break;
						}
					}

					if (playerTeamName != null)
					{
						if(!(isClaimed.equals(playerTeamName)))
						{
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
							event.setCancelled(true);
							return;
						}
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));	
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
