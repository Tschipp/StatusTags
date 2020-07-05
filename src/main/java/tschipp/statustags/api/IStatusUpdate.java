package tschipp.statustags.api;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import tschipp.statustags.common.config.StatusTagsConfig;

/**
 * Status Update interface.
 * Status Updates should be singletons, so don't store any information that shouldn't persist on them.
 * @author Tschipp
 *
 */
public interface IStatusUpdate
{
	/**
	 * ID of the Status Update. Must be unique.
	 * 
	 * @return the id
	 */
	public ResourceLocation getId();

	/**
	 * The Icon ID (path) of the Status Update
	 * 
	 * @return the icon path
	 */
	public ResourceLocation getIconLocation();

	/**
	 * @return the duration in ticks after which this status will automatically
	 *         be invalidated, or -1 if the status must be invalidated manually
	 */
	public int getDuration();

	/**
	 * Performed every tick while this status is active. Can be used to invalidate status.
	 * @param player the player
	 * @param world the world
	 */
	default void onUpdate(EntityPlayerMP player, World world)
	{
	}

	/**
	 * @return the priority of the status update
	 */
	default StatusPriority getPriority()
	{
		return StatusPriority.NORMAL;
	}

	/**
	 * @return the color of the status tag, when this update is active.
	 */
	default Color getTagColor()
	{
		return new Color(StatusTagsConfig.settings.defaultColor);
	}
}
