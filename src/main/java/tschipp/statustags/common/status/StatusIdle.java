package tschipp.statustags.common.status;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tschipp.statustags.api.StatusPriority;

public class StatusIdle extends BaseStatusUpdate
{
	public static StatusIdle INSTANCE = new StatusIdle();
	
	private StatusIdle()
	{
		super("idle", -1);
	}

	@Override
	public StatusPriority getPriority()
	{
		return StatusPriority.LOWEST;
	}
}
