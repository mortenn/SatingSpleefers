package me.Kruithne.SatingSpleefers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

public class Debugging {

	private List<Player> debugListeners = new ArrayList<Player>();
	//private Logger log = null;
	
	Debugging(Logger log)
	{
		//this.log = log;
	}
	
	public void addDebugListener(Player player)
	{
		if (!debugListeners.contains(player))
		{
			debugListeners.add(player);
		}
	}
	
	public void removeDebugListener(Player player)
	{
		if (debugListeners.contains(player))
		{
			debugListeners.add(player);
		}
	}
	
	public void toggleDebug(Player player)
	{
		if (!debugListeners.contains(player))
		{
			this.addDebugListener(player);
		}
		else
		{
			this.removeDebugListener(player);
		}
	}
	
	public void debug(String message)
	{
		Iterator<Player> debugListenersIterator = this.debugListeners.iterator();
		
		while (debugListenersIterator.hasNext())
		{
			this.debugToPlayer(message, debugListenersIterator.next());
		}
		//this.log.log(Level.INFO, message);
	}
	
	private void debugToPlayer(String message, Player player)
	{
		player.sendMessage(String.format("%s: %s", Constants.pluginOutputTag, message));
	}
	
}
