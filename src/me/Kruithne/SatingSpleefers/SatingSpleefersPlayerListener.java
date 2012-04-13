package me.Kruithne.SatingSpleefers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

public class SatingSpleefersPlayerListener implements Listener
{
	SatingSpleefers rPlugin = null;
	
	public SatingSpleefersPlayerListener(SatingSpleefers plugin)
	{
		this.rPlugin = plugin;
        this.rPlugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		Player thePlayer = event.getPlayer();
		
		this.checkFlyZone(thePlayer);
		
		if (thePlayer.getWorld() == this.rPlugin.spleefArena_L1.getWorld() && !this.rPlugin.isSpleefOp(event.getPlayer()))
		{
			if (this.rPlugin.currentPlayers.contains(thePlayer.getEntityId()) && !this.rPlugin.preGame)
			{
				CuboidSelection arena = new CuboidSelection(thePlayer.getWorld(), this.rPlugin.spleefArena_L1, this.rPlugin.spleefArena_L2);
				
				if (!arena.contains(thePlayer.getLocation())) // Inside arena before a game started.
				{
					this.rPlugin.currentPlayers.remove((Object) thePlayer.getEntityId());
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void checkFlyZone(Player thePlayer)
	{
		if (thePlayer.getWorld() == this.rPlugin.spleefBounds_1.getWorld())
		{
			CuboidSelection arena = new CuboidSelection(this.rPlugin.spleefBounds_1.getWorld(), this.rPlugin.spleefBounds_1, this.rPlugin.spleefBounds_2);
			
			if (!this.rPlugin.isSpleefOp(thePlayer.getPlayer()))
			{
				if (arena.contains(thePlayer.getLocation())) // Inside arena before a game started.
				{
					if (thePlayer.getGameMode() == GameMode.CREATIVE)
						thePlayer.setGameMode(GameMode.SURVIVAL);
					
					thePlayer.getInventory().clear();
					thePlayer.updateInventory();
				}
				else
				{
					if (thePlayer.getGameMode() == GameMode.SURVIVAL)
						thePlayer.setGameMode(GameMode.CREATIVE);
				}
			}
			else
			{
				if (thePlayer.getGameMode() == GameMode.SURVIVAL)
					thePlayer.setGameMode(GameMode.CREATIVE);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		this.checkFlyZone(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		try
		{
			if (event.getPlayer().getWorld() == this.rPlugin.spleefArena_L1.getWorld() && !this.rPlugin.isSpleefOp(event.getPlayer()))
			{
				if (this.rPlugin.currentPlayers.contains(event.getPlayer().getEntityId()) && event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.STEP && event.getClickedBlock().getData() == Byte.parseByte("1") && (event.getClickedBlock().getY() - this.rPlugin.spleefArena_L2.getY()) < 1)
				{
					if (this.rPlugin.canBreakBlocks(event.getPlayer()))
					{
						event.getClickedBlock().setType(Material.AIR);
						this.rPlugin.spleefedBlocks.add(event.getClickedBlock().getLocation());
						this.rPlugin.debugger.debug(event.getPlayer().getName() + " broke a floor tile.");
					}
					
					if (this.rPlugin.isSuspectedOfHacking(event.getPlayer()))
					{
						this.rPlugin.spleefGlobalCast(event.getPlayer().getName() + " was removed from the arena because the server detected hacking or heavy lagg.");
						this.rPlugin.removePlayerFromArena(event.getPlayer());
					}
					
					int entityID = event.getPlayer().getEntityId();
					
					if (this.rPlugin.playerBreakCount.containsKey(entityID))
					{
						this.rPlugin.playerBreakCount.put(entityID, this.rPlugin.playerBreakCount.get(entityID) + 1);
					}
					else
					{
						this.rPlugin.playerBreakCount.put(entityID, 1);
					}
				}
				
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.WALL_SIGN)
				{
					BlockState state = event.getClickedBlock().getState();
					Player thePlayer = event.getPlayer();
					
					if (state instanceof Sign)
					{
						Sign sign = (Sign)state;
						
						
						if (event.getClickedBlock().getLocation() == this.rPlugin.spleefGetScoreSign)
						{
							ResultSet scoreData = this.rPlugin.database.getQuery(String.format("SELECT Wins FROM spleefScore WHERE ID = '%s'", thePlayer.getName()));	
							try
							{
								if (scoreData.next())
								{
									this.rPlugin.outputToPlayer(String.format(Constants.spleefCastPlayerScore, scoreData.getString("Wins")), thePlayer);
								}
							}
							catch (SQLException e)
							{
								this.rPlugin.outputToConsole(String.format(Constants.SQLError, e.getMessage()), Level.SEVERE);
							}
						}
						else
						{
							if (sign.getLine(0).equals("YELLOW"))
							{
								if (this.rPlugin.spleefSpectatorYellow != null)
								{
									thePlayer.teleport(this.rPlugin.spleefSpectatorYellow);
								}
								else
								{
									this.rPlugin.outputToPlayer(Constants.warpSignInactive, thePlayer);
								}
							}
							else if (sign.getLine(0).equals("RED"))
							{
								if (this.rPlugin.spleefSpectatorRed != null)
								{
									thePlayer.teleport(this.rPlugin.spleefSpectatorRed);
								}
								else
								{
									this.rPlugin.outputToPlayer(Constants.warpSignInactive, thePlayer);
								}
							}
							else if (sign.getLine(0).equals("BLUE"))
							{
								if (this.rPlugin.spleefSpectatorBlue != null)
								{
									thePlayer.teleport(this.rPlugin.spleefSpectatorBlue);
								}
								else
								{
									this.rPlugin.outputToPlayer(Constants.warpSignInactive, thePlayer);
								}
							}
							else if (sign.getLine(0).equals("GREEN"))
							{
								if (this.rPlugin.spleefSpectatorGreen != null)
								{
									thePlayer.teleport(this.rPlugin.spleefSpectatorGreen);
								}
								else
								{
									this.rPlugin.outputToPlayer(Constants.warpSignInactive, thePlayer);
								}
							}
						}
					}
				}
			}
		}
		catch (NullPointerException e)
		{
			//ignore this, expected if not setup.
		}
	}
	
}
