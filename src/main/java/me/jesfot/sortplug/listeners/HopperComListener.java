package me.jesfot.sortplug.listeners;

import java.util.ArrayList;
import java.util.List;

import me.jesfot.sortplug.SortPlug;
import me.jesfot.sortplug.config.Config;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HopperComListener implements Listener
{
	private Player player;
	private SortPlug sp;
	private Config conf;
	private boolean add;
	private Material mat;
	
	public HopperComListener(Player p_player, SortPlug p_sp, Material material)
	{
		this.add = true;
		this.player = p_player;
		this.sp = p_sp;
		this.conf = this.sp.getConfig();
		this.mat = material;
	}
	public HopperComListener(Player p_player, SortPlug p_sp, Material material, boolean add)
	{
		this.add = add;
		this.player = p_player;
		this.sp = p_sp;
		this.conf = this.sp.getConfig();
		this.mat = material;
	}
	
	@EventHandler
	public void onPlayerClickAdd(final PlayerInteractEvent event)
	{
		if(!add)
		{
			return;
		}
		Action action = event.getAction();
		Action leftClickBlock = Action.LEFT_CLICK_BLOCK;
		if(event.getPlayer() == this.player)
		{
			if(action == leftClickBlock)
			{
				Block block = event.getClickedBlock();
				if(block.getType().equals(Material.HOPPER))
				{
					Location location = block.getLocation();
					String itype = mat.name(); // Get the item type name
					if(!this.conf.getCustomConfig().contains("filters.ids")) // Verify that we are not on the first ID
					{
						this.conf.getCustomConfig().createSection("filters.ids");
						this.conf.getCustomConfig().set("filters.ids", 0);
					}
					int ids = this.conf.getCustomConfig().getInt("filters.ids"); // Get the total ids
					int id = -1;
					for(int i=0; i<ids; i++)
					{
						Location loca = this.conf.getLoc("filters.hoppers.id"+i+".location");
						if(loca != null && location.equals(loca))
						{
							id = ids;
							break;
						}
					}
					if(id == -1)
					{
						id = ids; // If we don't already have this location stored, store a new one
					}
					this.conf.storeLoc("filters.hoppers.id"+id+".location", location); // Save the location at this id
					this.conf.getCustomConfig().set("filters.ids", (id+1)); // Store the id+1 for the next registration
					if(!this.conf.getCustomConfig().contains("filters.hoppers.id"+id+".items"))
					{
						this.conf.getCustomConfig().createSection("filters.hoppers.id"+id+".items");
						this.conf.getCustomConfig().set("filters.hoppers.id"+id+".items", new ArrayList<String>()); // If there is no list, create one
					}
					List<String> filter = this.conf.getCustomConfig().getStringList("filters.hoppers.id"+id+".items");
					if(!filter.contains(itype))
					{
						filter.add(itype); // Add the given material to the list
					}
					this.conf.getCustomConfig().set("filters.hoppers.id"+id+".items", filter);
					this.conf.saveCustomConfig(); // And save data.
					this.player.sendMessage("Okay, block registered.");
					event.setCancelled(true);
				}
				else
				{
					this.player.sendMessage("You didn't click on a hopper, try again.");
				}
				PlayerInteractEvent.getHandlerList().unregister(this);
			}
		}
	}
	
	@EventHandler
	public void onPlayerClickRemove(final PlayerInteractEvent event)
	{
		Player player2 = event.getPlayer();
		if(player2.equals(this.player))
		{
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK))
			{
				String itype = this.mat.name(); // Get the item type
				// Create the given location
				Location loc = event.getClickedBlock().getLocation();
				if(!this.sp.getConfig().getCustomConfig().contains("filters.ids")) // Verify that we are not on the first ID
				{
					this.sp.getConfig().getCustomConfig().createSection("filters.ids");
					this.sp.getConfig().getCustomConfig().set("filters.ids", 0);
					player2.sendMessage("This location was no filters.");
					return;
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
					player2.sendMessage("This location was no filters.");
					return;
				}
				if(!this.sp.getConfig().getCustomConfig().contains("filters.hoppers.id"+id+".items"))
				{
					player2.sendMessage("Good..., no items to this location.");
					this.sp.getConfig().getCustomConfig().set("filters.hoopers.id"+id, null);
					return;
				}
				List<String> filter = this.sp.getConfig().getCustomConfig().getStringList("filters.hoppers.id"+id+".items");
				if(filter.contains(itype))
				{
					filter.remove(itype); // Remove the given material to the list
				}
				this.sp.getConfig().getCustomConfig().set("filters.hoppers.id"+id+".items", filter);
				this.sp.getConfig().saveCustomConfig(); // And save data.
				PlayerInteractEvent.getHandlerList().unregister(this);
			}
		}
	}
}