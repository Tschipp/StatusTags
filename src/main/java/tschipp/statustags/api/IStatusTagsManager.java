package tschipp.statustags.api;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public interface IStatusTagsManager
{
	public void enqueueStatusUpdate(ResourceLocation name, EntityPlayerMP player);
	
	public void setStatusUpdate(ResourceLocation name, String playerid);
	
	public void stopStatusUpdate(EntityPlayerMP player);
	
	public void registerStatusUpdate(IStatusUpdate instance);
	
	public Set<ResourceLocation> getRegistredStatusUpdates();
	
	public IStatusUpdate getStatusUpdateInstance(ResourceLocation name);
	
	public IStatusUpdate getIdle();

	public void stopSpecificStatusUpdate(EntityPlayerMP player, ResourceLocation status);
}
