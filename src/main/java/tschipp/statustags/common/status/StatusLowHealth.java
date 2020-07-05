package tschipp.statustags.common.status;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import tschipp.statustags.StatusTags;
import tschipp.statustags.api.StatusPriority;
import tschipp.statustags.common.config.StatusTagsConfig;

public class StatusLowHealth extends BaseStatusUpdate
{

	public StatusLowHealth()
	{
		super("low_health", -1);
	}
	
	@Override
	public void onUpdate(EntityPlayerMP player, World world)
	{
		if(player.getHealth() > 6)
			StatusTags.MANAGER.stopStatusUpdate(player);
	}

	@Override
	public StatusPriority getPriority()
	{
		return StatusPriority.HIGHEST;
	}
	
	@Override
	public Color getTagColor()
	{
		return new Color(StatusTagsConfig.settings.dangerColor);
	}
}
