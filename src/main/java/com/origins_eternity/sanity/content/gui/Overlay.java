package com.origins_eternity.sanity.content.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.sanity.Sanity.MOD_ID;

public class Overlay extends Gui {
    private final ResourceLocation gui = new ResourceLocation(MOD_ID, "textures/gui/sanity.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;
            if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
                GlStateManager.enableBlend();
                GlStateManager.pushMatrix();
                int posX = event.getResolution().getScaledWidth() / 2 + 82;
                int posY = event.getResolution().getScaledHeight() - GuiIngameForge.right_height;
                mc.getTextureManager().bindTexture(gui);
                drawTexture();
                GuiIngameForge.right_height += 10;
                GlStateManager.popMatrix();
                mc.getTextureManager().bindTexture(Gui.ICONS);
                GlStateManager.disableBlend();
            }
    }

    private void drawTexture() {
    }
}