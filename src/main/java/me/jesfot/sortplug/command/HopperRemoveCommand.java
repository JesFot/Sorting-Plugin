package me.jesfot.sortplug.command;

import java.util.List;

import me.jesfot.sortplug.SortPlug;
import me.jesfot.sortplug.listeners.HopperComListener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HopperRemoveCommand implements CommandExecutor
{
	private SortPlug sp;
	private String usageMessage = "Usage : /removefilter [x, y, z] <itemType>";
	
	private Material assigna(boolean b)
	{
		b = false;
		return null;
	}
	
	public HopperRemoveCommand(SortPlug p_sp)
	{
		this.sp = p_sp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("removefilter"))
		{
			if(args.length == 1)
			{
				if(!(sender instanceof Player)) // The console cannot click anywhere
				{
					sender.sendMessage(ChatColor.RED + "Sorry, the console must use coordinates.");
					return true;
				}
				Player player = (Player)sender;
				String mat = args[0];
				boolean t = (true);
				Material material = Material.matchMaterial(mat)!=null ? Material.matchMaterial(mat) : assigna(t);
				if(!t)
				{
					player.sendMessage(ChatColor.RED + "The type must be a realone. (like baked_potato)");
					return true;
				}
				player.sendMessage("Do a left-click on the hooper you want to configure.");
				HopperComListener listener = new HopperComListener(player, this.sp, material, false);
				this.sp.getServer().getPluginManager().registerEvents(listener, this.sp.getPlugin());
				return true;
			}
			else if(args.length == 4)
			{
				String itype = args[3]; // Get the item type
				World world = null;
				if(sender instanceof Player) // If it's a player, get him world
				{
					Player p = (Player)sender;
					world = p.getWorld();
				}
				else // else get the first world // to be configured
				{
					world = this.sp.getServer().getWorlds().get(0);
				}
				// Create the given location
				Location loc = new Location(world, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
				if(!this.sp.getConfig().getCustomConfig().contains("filters.ids")) // Verify that we are not on the first ID
				{
					this.sp.getConfig().getCustomConfig().createSection("filters.ids");
					this.sp.getConfig().getCustomConfig().set("filters.ids", 0);
					sender.sendMessage("This location was no filters.");
					return true;
				}
				int ids = this.sp.getConfig().getCustomConfig().getInt("filters.ids"); // Get the total ids
				int id = -1;
				for(int i=0; i<ids; i++)
				{
					Location loca = this.sp.getConfig().getLoc("filters.hoppers.id"+i+".location");
					if(loca != null && loc.equals(loca))
					{
						id = ids;
						break;
					}
				}
				if(id == -1)
				{
					sender.sendMessage("This location was no filters.");
					return true;
				}
				if(!this.sp.getConfig().getCustomConfig().contains("filters.hoppers.id"+id+".items"))
				{
					sender.sendMessage("Good..., no items to this location.");
					this.sp.getConfig().getCustomConfig().set("filters.hoopers.id"+id, null);
					return true;
				}
				List<String> filter = this.sp.getConfig().getCustomConfig().getStringList("filters.hoppers.id"+id+".items");
				if(filter.contains(itype))
				{
					filter.remove(itype); // Remove the given material to the list
				}
				this.sp.getConfig().getCustomConfig().set("filters.hoppers.id"+id+".items", filter);
				this.sp.getConfig().saveCustomConfig(); // And save data.
				return true;
			}
			sender.sendMessage(ChatColor.RED + this.usageMessage);
			return true;
		}
		return false;
	}
}
