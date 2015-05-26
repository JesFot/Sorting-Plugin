package me.jesfot.sortplug.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Utility class block static system.
 * 
 * You don't have to import (new ...)
 */
public class BlockUtils
{
	/**
	 * Get the first air block next to the given location in that order : 
	 * {@link BlockFace#NORTH}, {@link BlockFace#SOUTH}, {@link BlockFace#UP}, {@link BlockFace#DOWN}, {@link BlockFace#WEST}, {@link BlockFace#EAST}
	 * and if no air blocks : {@link BlockFace#SELF}
	 * 
	 * @param location - the center location to search next
	 * @return The first found BlockFace where we have {@link Material#AIR}
	 */
	public static BlockFace getFirstAir(Location location)
	{
		Block block = location.getBlock();
		Block bn = block.getRelative(BlockFace.NORTH);
		Block bs = block.getRelative(BlockFace.SOUTH);
		Block bu = block.getRelative(BlockFace.UP);
		Block bd = block.getRelative(BlockFace.DOWN);
		Block bw = block.getRelative(BlockFace.WEST);
		Block be = block.getRelative(BlockFace.EAST);
		if(bn.getType().equals(Material.AIR))
		{
			return BlockFace.NORTH;
		}
		if(bs.getType().equals(Material.AIR))
		{
			return BlockFace.SOUTH;
		}
		if(bu.getType().equals(Material.AIR))
		{
			return BlockFace.UP;
		}
		if(bd.getType().equals(Material.AIR))
		{
			return BlockFace.DOWN;
		}
		if(bw.getType().equals(Material.AIR))
		{
			return BlockFace.WEST;
		}
		if(be.getType().equals(Material.AIR))
		{
			return BlockFace.EAST;
		}
		return BlockFace.SELF;
	}
}