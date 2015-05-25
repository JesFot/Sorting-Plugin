package me.jesfot.sortplug.bukkit;

import java.util.logging.Level;

import me.jesfot.sortplug.Refs;
import me.jesfot.sortplug.SortPlug;
import me.jesfot.sortplug.listeners.InventoryListener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin
{
	private SortPlug sp; // Here is my plugin
	
	@Override
	public void onLoad()
	{
		SortPlug.onLoad();
	}
	
	@Override
	public void onEnable() // Starting plugin...
	{
		this.reloadConfig(); // Just to be safe
		final PluginManager pm = this.getServer().getPluginManager();
		this.sp = new SortPlug(this.getServer(), this.getLogger(), this); // Call my main class plugin
		
		try
		{
			this.sp.onEnable();
		}
		catch(RuntimeException ex) // If errors
		{
			this.getLogger().log(Level.SEVERE, "The file is broken and " + Refs.NAME + " cannot open it. " + Refs.NAME + " is now disabled"); // A log to informe the server
			this.getLogger().log(Level.SEVERE, ex.toString()); // log basic line
			if(Refs.DEBUG)
			{
				this.getLogger().log(Level.SEVERE, "Error :", ex); // If debug is on, we log the entire error.
			}
			if(Refs.PLAYERSINFOS)
			{
				for(Player player : this.getServer().getOnlinePlayers())
				{
					if(!Refs.ONLYOP)
					{
						player.sendMessage(Refs.NAME + " failed to load, read the log file"); // Inform all players
					}
					else if(player.isOp())
					{
						player.sendMessage(Refs.NAME + " failed to load, read the log file"); // Inform only oped players.
					}
				}
			}
			stopPlugin(); // Stop the plugin (if crashed ...)
			return;
		}
		
		// Registering events by general classes :
		InventoryListener inventorylistener = new InventoryListener(this.sp);
		
		pm.registerEvents(inventorylistener, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String aliase, String[] args)
	{
		return this.sp.onCommand(sender, command, aliase, args);
	}
	/**
	 * Plugin disabling event
	 */
	@Override
	public void onDisable()
	{
		this.sp.onDisable();
	}
	/**
	 * Command to stop the plugin
	 */
	public void stopPlugin()
	{
		this.setEnabled(false);
	}
}