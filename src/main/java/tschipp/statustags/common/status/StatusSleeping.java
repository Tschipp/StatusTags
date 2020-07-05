package tschipp.statustags.common.status;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import tschipp.statustags.StatusTags;

public class StatusSleeping extends BaseStatusUpdate
{

	public StatusSleeping()
	{
		super("sleeping", -1);
	}
	
	@Override
	public void onUpdate(EntityPlayerMP player, World world)
	{
		if(!player.isPlayerSleeping())
			StatusTags.MANAGER.stopStatusUpdate(player);
	}

}
