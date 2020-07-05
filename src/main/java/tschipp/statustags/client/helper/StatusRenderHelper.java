package tschipp.statustags.client.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class StatusRenderHelper
{
	public static Vec3d getExactPos(Entity entity, float partialticks)
	{
		return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialticks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialticks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialticks);
	}

	public static int getTagWidth(String name)
	{
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(name) * 2 + 44 + 10 + 7;
	}

	public static int getMiddleWidth(String name)
	{
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(name) * 2 + 7;
	}

	public static double cubicInterpolate(double p1, double p2, double left, double right, double factor)
	{
		double a0, a1, a2, factor2;
		
		factor2 = factor * factor;
		a0 = right - left - p1 + p2;
		a1 = p1 - p2 - a0;
		a2 = left - p1;
		
		return a0 * factor * factor2 + a1 * factor2 + a2 * factor + p2;
	}
}
