package me.jesfot.sortplug.listeners;

import java.util.ArrayList;
import java.util.List;

import me.jesfot.sortplug.Refs;
import me.jesfot.sortplug.SortPlug;
import me.jesfot.sortplug.runnable.ReplaceBlockTask;
import me.jesfot.sortplug.utils.BlockUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;

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
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void onMoveItem(InventoryMoveItemEvent event)
	{
		// Get items and inventory
		Material item = event.getItem().getType();
		InventoryHolder holder = event.getSource().getHolder();
		if(!(holder instanceof Hopper))
		{
			return; // If is ot an hopper, pass.
		}
		Hopper hopper = (Hopper)holder;
		Location hlocation = hopper.getLocation();
		// For all registered hoppers locations
		for(int i=0; i<=this.sp.getConfig().getCustomConfig().getInt("filters.ids"); i++)
		{
			Refs.DEBUG = false;
			Location actu = this.sp.getConfig().getLoc("filters.hoppers.id"+i+".location");
			Refs.DEBUG = Refs.DEBUG2;
			if(actu == null)
			{
				continue; // Error ?
			}
			// Get authorised items.
			List<String> whitelist = this.sp.getConfig().getCustomConfig().getStringList("filters.hoppers.id"+i+".items");
			if(whitelist == null)
			{
				continue; // Error ?
			}
			//Transform it to materials
			List<Material> mats = new ArrayList<Material>();
			for(String str : whitelist)
			{
				mats.add(Material.valueOf(str));
			}
			// If we are at the same location as registered do
			if(hlocation.equals(actu))
			{
				// Get datas for downer block
				Location down = actu;
				down.setY(down.getY()-1);
				Block b = down.getBlock();
				if(b.getType() == Material.HOPPER)
				{
					if(mats.contains(item))
					{
						continue; // If it's good items, nothing to do, let gravity do it jobs.
					}
					else if(((Hopper)(InventoryHolder)event.getDestination().getHolder()).equals((Hopper)(InventoryHolder)b.getState()))
					{
						hopper.removeItem(item);
						BlockFace face = BlockUtils.getFirstAir(b.getLocation());
						if(face == BlockFace.SELF)
						{
							if(Refs.DEBUG)
							{
								this.sp.getLogger().warning("[InventoryListener:94]Cannot filter item because there are no disponible blocks");
							}
							continue;
						}
						// Place a redstone bock to stop the item and remove the redstone block, will be removed
						// I don't want to touch aroud blocks
						new ReplaceBlockTask(b.getRelative(face).getLocation(), Material.AIR).runTaskLater(this.sp.getPlugin(), 10);
						b.getRelative(face).setType(Material.REDSTONE_BLOCK);
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
