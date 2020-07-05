package tschipp.statustags.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class AbstractStatusUpdate implements IStatusUpdate
{
	protected ResourceLocation id;
	protected ResourceLocation icon;
	protected int duration;
	
	public AbstractStatusUpdate(String modid, String name, int duration)
	{
		this.duration = duration;
		this.id = new ResourceLocation(modid, name);
		this.icon = new ResourceLocation(modid, "textures/gui/status/" + name + ".png");
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public ResourceLocation getIconLocation()
	{
		return icon;
	}

	@Override
	public int getDuration()
	{
		return duration;
	}
}
