package tschipp.statustags.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import tschipp.statustags.StatusTags;

@EventBusSubscriber(modid = StatusTags.MODID)
public class StatusTriggers
{

	@SubscribeEvent
	public static void onTick(TickEvent.ServerTickEvent event)
	{
		if (event.phase == Phase.START)
			if (StatusTags.proxy.getClass().getName().contains("CommonProxy"))
				StatusTags.proxy.incTicks();

		StatusTags.MANAGER.checkStatusTimes();
	}

	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event)
	{
		if (event.isCanceled())
			return;

		EntityPlayer player = event.getPlayer();
		if (player != null && !player.world.isRemote)
		{
			StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(StatusTags.MODID, "breaking_block"), (EntityPlayerMP) player);
		}
	}

	@SubscribeEvent
	public static void onBlockPlace(PlaceEvent event)
	{
		if (event.isCanceled())
			return;

		EntityPlayer player = event.getPlayer();
		if (player != null && !player.world.isRemote)
		{
			StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(StatusTags.MODID, "placing_block"), (EntityPlayerMP) player);
		}
	}

	@SubscribeEvent
	public static void onPlayerDamaged(LivingDamageEvent event)
	{
		if (event.isCanceled())
			return;

		if (event.getEntityLiving() instanceof EntityPlayerMP)
		{
			StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(StatusTags.MODID, "damaged"), (EntityPlayerMP) event.getEntityLiving());
		}
	}

	@SubscribeEvent
	public static void onEat(LivingEntityUseItemEvent.Start event)
	{
		if (event.getEntityLiving() instanceof EntityPlayerMP && event.getItem().getItem() instanceof ItemFood)
		{
			StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(StatusTags.MODID, "eating"), (EntityPlayerMP) event.getEntityLiving());
		}
	}

	@SubscribeEvent
	public static void onUpdate(LivingUpdateEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

			StatusTags.MANAGER.onUpdate((EntityPlayerMP) player);

			if (player.getHealth() <= 6)
			{
				StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(StatusTags.MODID, "low_health"), player);
			}

			if (player.isPlayerSleeping())
			{
				StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(StatusTags.MODID, "sleeping"), player);
			}

		}
	}

	@SubscribeEvent
	public static void onAttack(AttackEntityEvent event)
	{
		if (event.isCanceled())
			return;

		EntityPlayer player = event.getEntityPlayer();
		if (player instanceof EntityPlayerMP)
		{
			StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(StatusTags.MODID, "attacking"), (EntityPlayerMP) player);
		}
	}

	@SubscribeEvent
	public static void onExitWorld(WorldEvent.Unload event)
	{
		StatusTags.proxy.resetTicks();
		StatusTags.MANAGER.clear();
	}

}
