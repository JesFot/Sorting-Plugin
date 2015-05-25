package me.jesfot.sortplug.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jesfot.sortplug.SortPlug;

public class ACommands
{
	private final SortPlug sp;
	
	public ACommands(SortPlug p_sp)
	{
		this.sp = p_sp;
	}
	
	/**
	 * Register all (or most of them) plugin's commands
	 */
	public void regCommands()
	{
		this.sp.getPlugin().getCommand("testspplugin").setExecutor(new TestPlugCommand());
	}
	
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return false;
	}
	
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		return null;
	}
}