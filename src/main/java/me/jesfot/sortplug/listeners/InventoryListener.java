package me.jesfot.sortplug.listeners;

import me.jesfot.sortplug.SortPlug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

/**
 * Inventory interactions event listener class
 *
 */
public class InventoryListener implements Listener
{
	@SuppressWarnings("unused")
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
	@EventHandler
	public void onMoveItem(InventoryMoveItemEvent event)
	{
		//
	}
}