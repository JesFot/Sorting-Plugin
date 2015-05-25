package me.jesfot.sortplug.listeners;

import java.util.ArrayList;
import java.util.List;

import me.jesfot.sortplug.Refs;
import me.jesfot.sortplug.SortPlug;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Inventory interactions event listener class
 *
 */
public class InventoryListener implements Listener
{
	private SortPlug sp;
	
	public InventoryListener(SortPlug p_sp)
	{
		this.sp = p_sp;
	}
	
	/**
	 * Detect when an item pass in a hopper and sort it.
	 * 
	 * @param event - The Inventory Move Item event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMoveItem(InventoryMoveItemEvent event)
	{
		event.getDestination().clear();
		event.getSource().clear();
		boolean pass = false;
		Material item = event.getItem().getType();
		InventoryHolder holder = event.getSource().getHolder();
		if(!(holder instanceof Hopper))
		{
			return;
		}
		Hopper hopper = (Hopper)holder;
		Location hlocation = hopper.getLocation();
		for(int i=0; i<=this.sp.getConfig().getCustomConfig().getInt("filters.ids"); i++)
		{
			Location actu = this.sp.getConfig().getLoc("filters.hoppers.id"+i+".location");
			if(actu == null)
			{
				continue;
			}
			List<String> whitelist = this.sp.getConfig().getCustomConfig().getStringList("filters.hoppers.id"+i+".items");
			if(whitelist == null)
			{
				continue;
			}
			List<Material> mats = new ArrayList<Material>();
			for(String str : whitelist)
			{
				mats.add(Material.valueOf(str));
			}
			if(hlocation.equals(actu))
			{
				if(mats.contains(item))
				{
					Location down = actu;
					down.setY(down.getY()-1);
					Block b = down.getBlock();
					if(Refs.DEBUG)
					{
						this.sp.getLogger().info("[DEBUG] Ferify if down is an inventory: ");
						for(ItemStack its : event.getDestination().getContents())
						{
							if(its != null)
							{
								this.sp.getLogger().info(its.getClass().toString());
							}
							else
							{
								this.sp.getLogger().info("void");
							}
						}
						this.sp.getLogger().info("[DEBUG] Ferify if down is an inventory: ");
						for(ItemStack its : event.getSource().getContents())
						{
							if(its != null)
							{
								this.sp.getLogger().info(its.getClass().toString());
							}
							else
							{
								this.sp.getLogger().info("void");
							}
						}
					}
					if(b.getType() == Material.CHEST || b.getType() == Material.HOPPER)
					{
						InventoryHolder h = (InventoryHolder)b.getState();
						h.getInventory().addItem(new ItemStack(item));
						pass = true;
						if(Refs.DEBUG)
						{
							this.sp.getLogger().info("[DEBUG] item passed to an other inventory.");
						}
					}
				}
			}
		}
		if(!pass)
		{
			event.getDestination().addItem(new ItemStack(item));
		}
	}
}