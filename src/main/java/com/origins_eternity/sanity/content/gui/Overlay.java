package com.origins_eternity.sanity.content.gui;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.sanity.Sanity.MOD_ID;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;

public class Overlay extends Gui {
    private final ResourceLocation gui = new ResourceLocation(MOD_ID, "textures/gui/sanity.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
            GlStateManager.enableBlend();
            GlStateManager.pushMatrix();
            int posX = event.getResolution().getScaledWidth() / 2 - 130;
            int posY = event.getResolution().getScaledHeight() - GuiIngameForge.right_height + 30;
            mc.getTextureManager().bindTexture(gui);
            drawTexture(player, posX, posY);
            GlStateManager.popMatrix();
            mc.getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.disableBlend();
        }
    }

    private void drawTexture(EntityPlayerSP player, int posX, int posY) {
        ISanity sanity = player.getCapability(SANITY, null);
        float percent = sanity.getSanity() / 100f;
        int consume = 24 - (int) (percent * 24);
        drawTexturedModalRect(posX, posY, 0, 0, 33, 24);
        int heath = posY + consume;
        if (sanity.getDown() > 0) {
            if (sanity.getDown() % 2 == 0) {
                drawTexturedModalRect(posX + 13, posY, 105, 0, 6, consume);
                drawTexturedModalRect(posX, heath, 33, consume, 33, 24 - consume);
                drawTexturedModalRect(posX + 13, heath, 99, consume, 6, 24 - consume);
            } else {
                drawTexturedModalRect(posX + 13, posY + 1, 105, 0, 6, consume - 1);
                drawTexturedModalRect(posX, heath, 33, consume, 33, 24 - consume);
                drawTexturedModalRect(posX + 13, heath, 99, consume - 1, 6, 25 - consume);
            }
        } else if (sanity.getUp() > 0) {
            if (sanity.getUp() % 2 == 0) {
                drawTexturedModalRect(posX + 13, posY, 117, 0, 6, consume);
                drawTexturedModalRect(posX, heath, 33, consume, 33, 24 - consume);
                drawTexturedModalRect(posX + 13, heath, 101, consume, 6, 24 - consume);
            } else {
                drawTexturedModalRect(posX + 13, posY - 1, 117, 0, 6, consume + 1);
                drawTexturedModalRect(posX, heath, 33, consume, 33, 24 - consume);
                drawTexturedModalRect(posX + 13, heath, 101, consume + 1, 6, 23 - consume);
            }
        } else {
            drawTexturedModalRect(posX, heath, 33, consume, 33, 24 - consume);
        }
    }
}