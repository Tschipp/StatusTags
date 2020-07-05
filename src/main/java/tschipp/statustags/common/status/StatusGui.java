package tschipp.statustags.common.status;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import tschipp.statustags.StatusTags;

public class StatusGui extends BaseStatusUpdate
{

	public StatusGui()
	{
		super("gui", -1);
	}

	@Override
	public void onUpdate(EntityPlayerMP player, World world)
	{
		if(player.openContainer == null)
			StatusTags.MANAGER.stopStatusUpdate(player);
	}

}
