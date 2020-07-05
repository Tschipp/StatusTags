package tschipp.statustags.common.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import tschipp.statustags.StatusTags;
import tschipp.statustags.api.IStatusTagsManager;
import tschipp.statustags.api.IStatusUpdate;
import tschipp.statustags.common.status.StatusAttacking;
import tschipp.statustags.common.status.StatusBreakingBlock;
import tschipp.statustags.common.status.StatusDamaged;
import tschipp.statustags.common.status.StatusEating;
import tschipp.statustags.common.status.StatusGui;
import tschipp.statustags.common.status.StatusIdle;
import tschipp.statustags.common.status.StatusLowHealth;
import tschipp.statustags.common.status.StatusPlacingBlock;
import tschipp.statustags.common.status.StatusSleeping;
import tschipp.statustags.network.StatusUpdateChangeClient;

public class StatusTagManager implements IStatusTagsManager
{
	public static final IStatusUpdate IDLE = StatusIdle.INSTANCE;

	private static final Map<ResourceLocation, IStatusUpdate> statusUpdates = new HashMap<ResourceLocation, IStatusUpdate>();

	private static Map<String, IStatusUpdate> currentStatuses = new HashMap<String, IStatusUpdate>();
	private static Map<String, Long> statusStartTime = new HashMap<String, Long>();

	public void regStatusUpdates()
	{
		registerStatusUpdate(IDLE);
		registerStatusUpdate(new StatusAttacking());
		registerStatusUpdate(new StatusBreakingBlock());
		registerStatusUpdate(new StatusEating());
		registerStatusUpdate(new StatusGui());
		registerStatusUpdate(new StatusLowHealth());
		registerStatusUpdate(new StatusPlacingBlock());
		registerStatusUpdate(new StatusSleeping());
		registerStatusUpdate(new StatusDamaged());
	}

	@Override
	public void enqueueStatusUpdate(ResourceLocation name, EntityPlayerMP player)
	{
		String uuid = player.getGameProfile().getId().toString();
		IStatusUpdate update = getStatusUpdateInstance(name);

		trySetStatusUpdate(uuid, update);

		StatusTags.network.sendToAllAround(new StatusUpdateChangeClient(player.getGameProfile().getId().toString(), name.toString()), new TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 128));
	}

	@Override
	public void stopStatusUpdate(EntityPlayerMP player)
	{
		String uuid = player.getGameProfile().getId().toString();
		currentStatuses.put(uuid, IDLE);

		StatusTags.network.sendToAllAround(new StatusUpdateChangeClient(player.getGameProfile().getId().toString(), IDLE.getId().toString()), new TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 128));
	}

	@Override
	public void stopSpecificStatusUpdate(EntityPlayerMP player, ResourceLocation status)
	{
		String uuid = player.getGameProfile().getId().toString();
		IStatusUpdate current = currentStatuses.get(uuid);
		if (current.getId().equals(status))
		{

			currentStatuses.put(uuid, IDLE);

			StatusTags.network.sendToAllAround(new StatusUpdateChangeClient(player.getGameProfile().getId().toString(), IDLE.getId().toString()), new TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 128));
		}
	}

	@Override
	public void registerStatusUpdate(IStatusUpdate instance)
	{
		if (statusUpdates.containsKey(instance.getId()))
			throw new IllegalArgumentException(instance.getId() + " has already been registred as a status update!");

		statusUpdates.put(instance.getId(), instance);
	}

	@Override
	public Set<ResourceLocation> getRegistredStatusUpdates()
	{
		return statusUpdates.keySet();
	}

	@Override
	public IStatusUpdate getStatusUpdateInstance(ResourceLocation name)
	{
		return statusUpdates.get(name);
	}

	@Override
	public IStatusUpdate getIdle()
	{
		return IDLE;
	}

	@Override
	public void setStatusUpdate(ResourceLocation name, String playerid)
	{
		trySetStatusUpdate(playerid, this.getStatusUpdateInstance(name));
	}

	public void onUpdate(EntityPlayerMP player)
	{
		String uuid = player.getGameProfile().getId().toString();
		IStatusUpdate current = currentStatuses.get(uuid);

		if (current == null)
		{
			currentStatuses.put(uuid, IDLE);
			statusStartTime.put(uuid, StatusTags.proxy.getTicks());
			current = IDLE;
		}

		current.onUpdate(player, player.world);
	}

	public static IStatusUpdate getCurrentStatusUpdate(EntityPlayer player)
	{
		String uuid = player.getGameProfile().getId().toString();
		IStatusUpdate status = currentStatuses.get(uuid);
		if (status == null)
		{
			currentStatuses.put(uuid, StatusTags.MANAGER.IDLE);
			statusStartTime.put(uuid, StatusTags.proxy.getTicks());
			return StatusTags.MANAGER.IDLE;
		}

		return status;
	}

	public static void trySetStatusUpdate(String uuid, IStatusUpdate update)
	{
		IStatusUpdate current = currentStatuses.get(uuid);

		if (current != null && update != StatusTags.MANAGER.IDLE)
		{
			if (!update.getPriority().compare(current.getPriority()))
				return;
		}

		currentStatuses.put(uuid, update);
		statusStartTime.put(uuid, StatusTags.proxy.getTicks());
	}

	public static void checkStatusTimes()
	{
		for (Entry<String, IStatusUpdate> entry : currentStatuses.entrySet())
		{
			String uuid = entry.getKey();
			long start = statusStartTime.get(uuid);

			if (entry.getValue().getDuration() < 0)
				continue;

			if (StatusTags.proxy.getTicks() - start > entry.getValue().getDuration())
			{
				currentStatuses.put(uuid, StatusTags.MANAGER.IDLE);
				statusStartTime.put(uuid, StatusTags.proxy.getTicks());
			}
		}
	}

	public static void clear()
	{
		currentStatuses.clear();
		statusStartTime.clear();
	}

}
