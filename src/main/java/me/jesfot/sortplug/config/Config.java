package me.jesfot.sortplug.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.jesfot.sortplug.Refs;
import me.jesfot.sortplug.SortPlug;

public class Config
{
	private final SortPlug sp;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	
	/**
	 * Constructor
	 * 
	 * @param p_sp - The class SortPlugin
	 */
	public Config(SortPlug p_sp)
	{
		this.sp = p_sp;
	}
	
	/**
	 * This will reload the plugin config
	 */
	public void reloadCustomConfig()
	{
		if(this.customConfigFile == null)
		{
			this.customConfigFile = new File(this.sp.getPlugin().getDataFolder(), "SortConfig.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);
		
		Reader defConfigStream = null;
		try
		{
			defConfigStream = new InputStreamReader(this.sp.getPlugin().getResource("SortConfig.yml"), "UTF8");
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		if(defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
	}
	
	/**
	 * Get the plugin configuration
	 * 
	 * @return The plugin config
	 */
	public FileConfiguration getCustomConfig()
	{
		if(this.customConfig == null)
		{
			this.reloadCustomConfig();
		}
		return this.customConfig;
	}
	
	/**
	 * Save the plugin configuration in the file
	 */
	public void saveCustomConfig()
	{
		if(this.customConfig == null || this.customConfigFile == null)
		{
			return;
		}
		
		try
		{
			this.getCustomConfig().save(customConfigFile);
		}
		catch(IOException e)
		{
			if(Refs.DEBUG)
			{
				this.sp.getLogger().log(Level.SEVERE, "Could not save config to "+this.customConfigFile, e);
			}
			else
			{
				this.sp.getLogger().log(Level.SEVERE, "Could not save config to "+this.customConfigFile);
			}
		}
	}
	
	/**
	 * This will store in the configuration file a minecraft location.
	 * The name will be passed in lower case.
	 * 
	 * @param name - The name of the location (including yaml path ex:"loc.example")
	 * @param location - The location to store
	 */
	public void storeLoc(String name, Location location)
	{
		name = name.toLowerCase();
		String i = " , ";
		String locator = location.getWorld().getName() +i+ location.getX() +i+ location.getY() +i+ location.getZ();
		locator += i+ location.getPitch() +i+ location.getYaw();
		if(!this.getCustomConfig().contains(name))
		{
			this.getCustomConfig().createSection(name);
		}
		this.getCustomConfig().set(name, locator);
		this.saveCustomConfig();
	}
	
	/**
	 * This will return the strored location at 'name' or null saved by {@link {@code storeLoc(name, loc)}. 
	 * 
	 * @param name - The name of the location
	 * @return The location requested if exists
	 */
	public Location getLoc(String name)
	{
		name = name.toLowerCase();
		this.reloadCustomConfig();
		String key = this.getCustomConfig().getString(name);
		if(key == null || key == "")
		{
			if(Refs.DEBUG)
			{
				this.sp.getLogger().warning("[SortingPlugin.Config.java:138] No location stored.");
			}
			return null;
		}
		String[] split = key.split(" , ");
		if(split.length == 6)
		{
			Location location = new Location(Bukkit.getWorld(split[0]),
					Double.parseDouble(split[1]),
					Double.parseDouble(split[2]),
					Double.parseDouble(split[3]),
					Float.parseFloat(split[4]),
					Float.parseFloat(split[5]));
			this.saveCustomConfig();
			return location;
		}
		else
		{
			this.saveCustomConfig();
			if(Refs.DEBUG)
			{
				this.sp.getLogger().warning("[SortingPlugin.Config.java:160] Location corrupted.");
			}
			return null;
		}
	}
}