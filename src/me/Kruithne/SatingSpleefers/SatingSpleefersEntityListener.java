package me.Kruithne.SatingSpleefers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

public class SatingSpleefersEntityListener implements Listener
{
	SatingSpleefers rPlugin = null;
	
	public SatingSpleefersEntityListener(SatingSpleefers plugin)
	{
		this.rPlugin = plugin;
        this.rPlugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntity().getWorld() == this.rPlugin.spleefArena_L1.getWorld())
		{
			CuboidSelection arena = new CuboidSelection(this.rPlugin.spleefBounds_1.getWorld(), this.rPlugin.spleefBounds_1, this.rPlugin.spleefBounds_2);
			
			if (arena.contains(event.getEntity().getLocation())) // Inside arena before a game started.
			{
				event.setCancelled(true);	
			}
		}
	}
	
}
