package me.jesfot.sortplug.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Simple test command.
 */
public class TestPlugCommand implements CommandExecutor
{
	private String usageMessage = "Usage: /testspplugin";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("testspplugin"))
		{
			sender.sendMessage("This command and so the plugin functions.");
			return true;
		}
		sender.sendMessage(ChatColor.RED + this.usageMessage);
		return true;
	}
}
