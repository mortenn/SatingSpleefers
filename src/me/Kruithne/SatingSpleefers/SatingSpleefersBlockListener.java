package me.Kruithne.SatingSpleefers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

public class SatingSpleefersBlockListener implements Listener
{
	SatingSpleefers rPlugin = null;
	
	public SatingSpleefersBlockListener(SatingSpleefers plugin)
	{
		this.rPlugin = plugin;
        this.rPlugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public boolean onBlockBreak(BlockBreakEvent event)
	{
		if (event.getPlayer().getWorld() == this.rPlugin.spleefArena_L1.getWorld() && !this.rPlugin.isSpleefOp(event.getPlayer()))
		{
			CuboidSelection arena = new CuboidSelection(this.rPlugin.spleefBounds_1.getWorld(), this.rPlugin.spleefBounds_1, this.rPlugin.spleefBounds_2);
			
			if (arena.contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);	
			}
		}
		return true;
	}
	
	@EventHandler
	public boolean onBlockPlace(BlockPlaceEvent event)
	{
		if (event.getPlayer().getWorld() == this.rPlugin.spleefArena_L1.getWorld() && !this.rPlugin.isSpleefOp(event.getPlayer()))
		{
			CuboidSelection arena = new CuboidSelection(this.rPlugin.spleefBounds_1.getWorld(), this.rPlugin.spleefBounds_1, this.rPlugin.spleefBounds_2);
			
			if (arena.contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);	
			}
		}
		return true;
	}
	
}
