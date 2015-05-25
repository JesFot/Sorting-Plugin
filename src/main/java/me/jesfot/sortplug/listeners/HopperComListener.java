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
	private Material mat;
	
	public HopperComListener(Player p_player, SortPlug p_sp, Material material)
	{
		this.player = p_player;
		this.sp = p_sp;
		this.conf = this.sp.getConfig();
		this.mat = material;
	}
	
	@EventHandler
	public void onPlayerClick(final PlayerInteractEvent event)
	{
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
					int id = this.sp.getConfig().getCustomConfig().getInt("filters.ids"); // Get the next id
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
}