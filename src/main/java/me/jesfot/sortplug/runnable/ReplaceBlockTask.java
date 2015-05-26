package me.jesfot.sortplug.runnable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Run this task to replace a given block location by an other block / material
 */
public class ReplaceBlockTask extends BukkitRunnable
{
	private Location location;
	private Block block;
	private Material material;
	private boolean useMaterial;
	
	public ReplaceBlockTask(Location p_location, Block p_block)
	{
		this.location = p_location.clone();
		this.block = p_block;
		this.material = this.block.getType();
		this.useMaterial = false;
	}
	public ReplaceBlockTask(Location p_location, Material p_material)
	{
		this.location = p_location;
		this.block = this.location.getBlock();
		this.material = p_material;
		this.useMaterial = true;
	}
	
	/**
	 * Replace a given block by a given material / block
	 */
	@Override
	public void run()
	{
		if(this.useMaterial)
		{
			this.location.getBlock().setType(this.material);
		}
		else
		{
			this.block.getLocation().setWorld(this.location.getWorld());
			this.block.getLocation().setDirection(this.location.getDirection());
			this.block.getLocation().setPitch(this.location.getPitch());
			this.block.getLocation().setYaw(this.location.getYaw());
			this.block.getLocation().setX(this.location.getX());
			this.block.getLocation().setY(this.location.getY());
			this.block.getLocation().setZ(this.location.getZ());
		}
	}
}