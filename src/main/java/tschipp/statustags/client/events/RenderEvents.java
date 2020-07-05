package tschipp.statustags.client.events;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tschipp.statustags.StatusTags;
import tschipp.statustags.api.IStatusUpdate;
import tschipp.statustags.client.helper.StatusRenderHelper;
import tschipp.statustags.common.config.StatusTagsConfig;
import tschipp.statustags.network.StatusUpdateChangeServer;

@SideOnly(Side.CLIENT)
@EventBusSubscriber
public class RenderEvents
{

	private static final ResourceLocation TAG_TEXTURE = new ResourceLocation(StatusTags.MODID, "textures/gui/statustag.png");

	private static final double CIRCLE_SIZE = 44;

	private static final double MID_TEX_U = 45;
	private static final double TAIL_TEX_U = 47;

	private static final double TEX_HEIGHT = 44;
	private static final double TEX_WIDTH = 57;

	private static double[] beatingAnim = new double[32];

	static
	{
		Arrays.fill(beatingAnim, 1.0);
		beatingAnim[10] = 1.2;
		beatingAnim[11] = 1.35;
		beatingAnim[12] = 1.5;
		beatingAnim[13] = 1.35;
		beatingAnim[14] = 1.2;

		beatingAnim[17] = 1.1;
		beatingAnim[18] = 1.3;
		beatingAnim[19] = 1.1;
	}

	@SubscribeEvent
	public static void onContainer(InitGuiEvent.Pre event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null)
			StatusTags.network.sendToServer(new StatusUpdateChangeServer(player.getGameProfile().getId().toString(), "statustags:gui", false));
	}

	@SubscribeEvent
	public static void onTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase == Phase.START)
			StatusTags.proxy.incTicks();

		StatusTags.MANAGER.checkStatusTimes();

		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null)
		{
			if (Minecraft.getMinecraft().currentScreen == null)
				StatusTags.network.sendToServer(new StatusUpdateChangeServer(player.getGameProfile().getId().toString(), "statustags:gui", true));
		}
	}

	@SubscribeEvent
	public static void onNametagRender(RenderLivingEvent.Specials.Pre<? extends EntityLivingBase> event)
	{
		EntityLivingBase entity = event.getEntity();
		if (entity instanceof EntityPlayer && event.isCancelable())
		{
			if (StatusTagsConfig.settings.onlyShowSameTeam)
			{
				if (entity.getTeam() != null && entity.getTeam() == Minecraft.getMinecraft().player.getTeam())
					event.setCanceled(true);
				else
					return;
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void renderStatusTags(RenderWorldLastEvent event)
	{
		float partialticks = event.getPartialTicks();
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer self = mc.player;
		Entity camera = mc.getRenderViewEntity() == null ? self : mc.getRenderViewEntity();
		World world = mc.world;

		Vec3d selfpos = StatusRenderHelper.getExactPos(camera, partialticks);

		float yaw = self.isPlayerSleeping() ? mc.getRenderManager().playerViewX : camera.rotationYaw;
		float pitch = self.isPlayerSleeping() ? mc.getRenderManager().playerViewY : camera.rotationPitch;

		// Sort by furthest players
		List<EntityPlayer> players = new ArrayList<EntityPlayer>(world.playerEntities);
		players.sort((p1, p2) -> {
			return -Float.compare(self.getDistance(p1), self.getDistance(p2));
		});

		for (EntityPlayer other : players)
		{
			if (other != self)
			{
				if (StatusTagsConfig.settings.onlyShowSameTeam ? other.getTeam() != null && other.getTeam() == self.getTeam() : true)
					drawStatusTag(other, selfpos, yaw, pitch, partialticks);
			}
		}
	}

	private static void drawStatusTag(EntityPlayer forPlayer, Vec3d camerapos, float yaw, float pitch, float partialticks)
	{
		Minecraft mc = Minecraft.getMinecraft();
		TextureManager tex = Minecraft.getMinecraft().getTextureManager();

		Vec3d otherpos = StatusRenderHelper.getExactPos(forPlayer, partialticks);
		otherpos = otherpos.addVector(0, forPlayer.height + 0.8, 0);

		Vec3d offset = otherpos.subtract(camerapos);

		IStatusUpdate status = StatusTags.MANAGER.getCurrentStatusUpdate(forPlayer);

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();

		GlStateManager.translate(offset.x, offset.y, offset.z);

		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);

		// Rotate 180 if in frontal view
		if (mc.gameSettings.thirdPersonView == 2)
			GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);

		GlStateManager.scale(-0.015F, -0.015F, 0.015F); // Base scale

		double len = offset.lengthSquared();
		// sqrt function to scale players further back less
		double scale = Math.max(0.8, Math.sqrt(Math.sqrt(len)) * 0.4);

		GlStateManager.scale(scale, scale, scale);
		GlStateManager.translate(0, -scale * 7, 0);

		if (status.getId().toString().equals("statustags:low_health"))
		{
			int i = (int) (StatusTags.proxy.getTicks() % 29) + 1;
			scale = MathHelper.clampedLerp(beatingAnim[i], beatingAnim[i + 1], partialticks);
			GlStateManager.scale(scale, scale, scale);
		}

		double width = StatusRenderHelper.getTagWidth(forPlayer.getName());
		double half = width / 2.0;
		int middleWidth = StatusRenderHelper.getMiddleWidth(forPlayer.getName());
		int stringWidth = mc.fontRenderer.getStringWidth(forPlayer.getName());

		tex.bindTexture(TAG_TEXTURE);

		Color col = status.getTagColor();
		float[] rgba = col.getRGBComponents(new float[4]);

		GlStateManager.color(rgba[0], rgba[1], rgba[2], rgba[3]);

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buf = tess.getBuffer();

		// Circle
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(-half, CIRCLE_SIZE, 0).tex(0, 0).endVertex();
		buf.pos(-half + CIRCLE_SIZE, CIRCLE_SIZE, 0).tex(CIRCLE_SIZE / TEX_WIDTH, 0).endVertex();
		buf.pos(-half + CIRCLE_SIZE, 0, 0).tex(CIRCLE_SIZE / TEX_WIDTH, CIRCLE_SIZE / TEX_HEIGHT).endVertex();
		buf.pos(-half, 0, 0).tex(0, CIRCLE_SIZE / TEX_HEIGHT).endVertex();

		// Tail
		buf.pos(-half + CIRCLE_SIZE + middleWidth, CIRCLE_SIZE, 0).tex(TAIL_TEX_U / TEX_WIDTH, 0).endVertex();
		buf.pos(-half + CIRCLE_SIZE + middleWidth + 10, CIRCLE_SIZE, 0).tex(1, 0).endVertex();
		buf.pos(-half + CIRCLE_SIZE + middleWidth + 10, 0, 0).tex(1, 1).endVertex();
		buf.pos(-half + CIRCLE_SIZE + middleWidth, 0, 0).tex(47 / TEX_WIDTH, 1).endVertex();

		// Middle
		buf.pos(-half + CIRCLE_SIZE - 0.001, CIRCLE_SIZE, 0).tex(MID_TEX_U / TEX_WIDTH, 0).endVertex();
		buf.pos(-half + CIRCLE_SIZE + middleWidth + 0.001, CIRCLE_SIZE, 0).tex((MID_TEX_U + 1) / TEX_WIDTH, 0).endVertex();
		buf.pos(-half + CIRCLE_SIZE + middleWidth + 0.001, 0, 0).tex((MID_TEX_U + 1) / TEX_WIDTH, 1).endVertex();
		buf.pos(-half + CIRCLE_SIZE - 0.001, 0, 0).tex(MID_TEX_U / TEX_WIDTH, 1).endVertex();

		tess.draw();

		GlStateManager.color(1, 1, 1, 1);
		tex.bindTexture(status.getIconLocation());
		// Icon
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(-half + 3, CIRCLE_SIZE - 3, 0).tex(0, 1).endVertex();
		buf.pos(-half + CIRCLE_SIZE - 3, CIRCLE_SIZE - 3, 0).tex(1, 1).endVertex();
		buf.pos(-half + CIRCLE_SIZE - 3, 3, 0).tex(1, 0).endVertex();
		buf.pos(-half + 3, 3, 0).tex(0, 0).endVertex();
		tess.draw();

		GlStateManager.scale(2, 2, 2);

		float[] hsb = Color.RGBtoHSB((int) (rgba[0] * 255), (int) (rgba[1] * 255), (int) (rgba[2] * 255), new float[3]);

		mc.fontRenderer.drawString(forPlayer.getName(), -(stringWidth / 2) + 10, 7, hsb[2] > 0.5 ? 0 : 0xFFFFFF);

		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

}
