package com.origins_eternity.sanity.content.render;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.origins_eternity.sanity.config.Configuration.Overlay;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.event.ClientEvent.flash;
import static com.origins_eternity.sanity.utils.Utils.canEnable;
import static com.origins_eternity.sanity.utils.proxy.ClientProxy.*;

public class Overlay extends Gui {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            EntityPlayerSP player = mc().player;
            if (canEnable(player)) {
                if (!player.isCreative() && !player.isSpectator()) {
                    int posX = event.getResolution().getScaledWidth();
                    int posY = event.getResolution().getScaledHeight();
                    GlStateManager.enableBlend();
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1.0F, 1.0F, 1.0F);
                    if (Overlay.blood) {
                        drawBlood(player, posX, posY);
                    }
                    posX = posX / 2 - 130 + Overlay.offX;
                    posY = posY - 29 - Overlay.offY;
                    if (Overlay.brain) {
                        drawBrain(player, posX, posY);
                    }
                    GlStateManager.popMatrix();
                    mc().getTextureManager().bindTexture(Gui.ICONS);
                    GlStateManager.disableBlend();
                }
            }
        }
    }

    private void drawBlood(EntityPlayerSP player, int posX, int posY) {
        ISanity sanity = player.getCapability(SANITY, null);
        if (sanity.getSanity() <= 60f) {
            mc().getTextureManager().bindTexture(blood);
            if (sanity.getDown() > 0) {
                if (player.ticksExisted % 24 < 12) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, player.ticksExisted % 24 / 30f + 0.2f);
                } else {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, (24 - player.ticksExisted % 24) / 30f + 0.2f);
                }
                drawScaledCustomSizeModalRect(0, 0, 0, 0, 100, 58, posX, posY, 100, 58);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    private void drawBrain(EntityPlayerSP player, int posX, int posY) {
        ISanity sanity = player.getCapability(SANITY, null);
        if (flash > 0 || Overlay.flash == -1) {
            mc().getTextureManager().bindTexture(hud);
            if (sanity.getSanity() < 40f) {
                posY += player.ticksExisted % 2;
            }
            if ((sanity.getDown() > 15 || sanity.getUp() > 15) && player.ticksExisted % 10 < 5) {
                drawTexturedModalRect(posX, posY, 0, 24, 33, 24);
            }
            float percent = sanity.getSanity() / 100f;
            int consume = 22 - (int) (percent * 22 + 0.5);
            drawTexturedModalRect(posX, posY, 0, 0, 33, 24);
            drawTexturedModalRect(posX, posY, 33, 24, 33, 24);
            drawTexturedModalRect(posX, posY + consume + 1, 33, consume + 1, 33, 22 - consume);
            if (sanity.getDown() > 0) {
                if (player.ticksExisted % 20 < 10) {
                    drawTexturedModalRect(posX + 13, posY, 66, 0, 6, 24);
                } else {
                    drawTexturedModalRect(posX + 13, posY + 1, 66, 0, 6, 24);
                }
            } else if (sanity.getUp() > 0) {
                if (player.ticksExisted % 20 < 10) {
                    drawTexturedModalRect(posX + 13, posY, 72, 0, 6, 24);
                } else {
                    drawTexturedModalRect(posX + 13, posY - 1, 72, 0, 6, 24);
                }
            }
        }
    }
}