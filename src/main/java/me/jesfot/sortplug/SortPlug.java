package me.jesfot.sortplug;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.jesfot.sortplug.bukkit.BukkitPlugin;
import me.jesfot.sortplug.command.ACommands;
import me.jesfot.sortplug.config.Config;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SortPlug
{
	private final Server server; // Get the server,
	private final Logger logger; // the logger to can log messages
	private final JavaPlugin plugin; // and the plugin.
	private ACommands commands;
	private Config config;

	public SortPlug(Server p_server, Logger p_logger, JavaPlugin p_plugin) // Constructor to initiate variables
	{
		this.server = p_server;
		this.logger = p_logger;
		this.plugin = p_plugin;
	}
	/**
	 * Plugin load event
	 */
	public static void onLoad()
	{
		// Nothing here
	}
	
	/**
	 * Plugin enable event
	 */
	public void onEnable()
	{
		this.config = new Config(this);
		this.commands = new ACommands(this);
		this.commands.regCommands(); // Register commands
	}
	
	/**
	 * Plugin disable event
	 */
	public void onDisable()
	{
		logger.log(Level.INFO, "Plugin stop."); // inform the server
		this.config.saveCustomConfig(); // Saving configuration
		plugin.saveConfig(); // Saving configuration
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		//
		return null;
	}
	
	/**
	 * Broadcast a message on the server.
	 * 
	 * @param message - The message to broadcasr
	 */
	public void broad(String message)
	{
		this.server.broadcastMessage(message);
	}
	
	/**
	 * Get the server
	 * 
	 * @return - The server
	 */
	public Server getServer()
	{
		return server;
	}
	/**
	 * Get the logger.
	 * 
	 * @return - The logger
	 */
	public Logger getLogger()
	{
		return logger;
	}
	/**
	 * Get this plugin (main functions)
	 * 
	 * @return - The plugin
	 */
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	/**
	 * Get the main class for this plugin.
	 * 
	 * @return - A new instance of the main file.
	 */
	@Deprecated
	public BukkitPlugin getThisPlugin()
	{
		return new BukkitPlugin();
	}
}