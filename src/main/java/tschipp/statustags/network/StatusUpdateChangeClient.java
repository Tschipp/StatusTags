package tschipp.statustags.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tschipp.statustags.StatusTags;

public class StatusUpdateChangeClient implements IMessage, IMessageHandler<StatusUpdateChangeClient, IMessage>
{
	public StatusUpdateChangeClient()
	{
	}

	private String uuid;
	private String status;

	public StatusUpdateChangeClient(String uuid, String status)
	{
		this.uuid = uuid;
		this.status = status;
	}


	@Override
	public void fromBytes(ByteBuf buf)
	{
		status = ByteBufUtils.readUTF8String(buf);
		uuid = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, status);
		ByteBufUtils.writeUTF8String(buf, uuid);
	}

	@Override
	public IMessage onMessage(StatusUpdateChangeClient message, MessageContext ctx)
	{
		IThreadListener mainThread = Minecraft.getMinecraft();

		mainThread.addScheduledTask(() -> {

				StatusTags.MANAGER.setStatusUpdate(new ResourceLocation(message.status), message.uuid);

		});
		return null;
	}

}
