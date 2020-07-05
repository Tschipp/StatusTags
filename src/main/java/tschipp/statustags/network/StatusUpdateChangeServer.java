package tschipp.statustags.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tschipp.statustags.StatusTags;

public class StatusUpdateChangeServer implements IMessage, IMessageHandler<StatusUpdateChangeServer, IMessage>
{
	public StatusUpdateChangeServer()
	{
	}

	private String uuid;
	private String status;
	private boolean stop; 

	public StatusUpdateChangeServer(String uuid, String status)
	{
		this.uuid = uuid;
		this.status = status;
		this.stop = false;
	}

	public StatusUpdateChangeServer(String uuid, String status, boolean stop)
	{
		this.uuid = uuid;
		this.status = status;
		this.stop = stop;
	}

	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		status = ByteBufUtils.readUTF8String(buf);
		uuid = ByteBufUtils.readUTF8String(buf);
		stop = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, status);
		ByteBufUtils.writeUTF8String(buf, uuid);
		buf.writeBoolean(stop);
	}

	@Override
	public IMessage onMessage(StatusUpdateChangeServer message, MessageContext ctx)
	{
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
		EntityPlayerMP player = (EntityPlayerMP) ctx.getServerHandler().player.world.getPlayerEntityByUUID(UUID.fromString(message.uuid));

		mainThread.addScheduledTask(() -> {

			if (player != null)
			{
				if(message.stop)
					StatusTags.MANAGER.stopSpecificStatusUpdate(player, new ResourceLocation(message.status));
				else
					StatusTags.MANAGER.enqueueStatusUpdate(new ResourceLocation(message.status), player);
			}

		});
		return null;
	}

}
