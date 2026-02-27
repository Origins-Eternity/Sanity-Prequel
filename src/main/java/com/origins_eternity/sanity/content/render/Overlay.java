package com.origins_eternity.sanity.content.render;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.sanity.Sanity.MOD_ID;
import static com.origins_eternity.sanity.config.Configuration.Overlay;
import static com.origins_eternity.sanity.event.ClientEvent.*;
import static com.origins_eternity.sanity.utils.Utils.isAwake;
import static com.origins_eternity.sanity.utils.proxy.ClientProxy.mc;

@SideOnly(Side.CLIENT)
public class Overlay extends Gui {
    private static final ResourceLocation blood = new ResourceLocation(MOD_ID, "textures/gui/blood.png");
    public static final ResourceLocation indicator = new ResourceLocation(MOD_ID, "textures/gui/indicator.png");

    @SubscribeEvent()
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            EntityPlayerSP player = mc().player;
            if (!player.isCreative() && !player.isSpectator()) {
                int posX = event.getResolution().getScaledWidth();
                int posY = event.getResolution().getScaledHeight();
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                drawBlood(player, posX, posY);
                int offX = ((player.getPrimaryHand() == EnumHandSide.RIGHT && !player.getHeldItemOffhand().isEmpty())
                        || (player.getPrimaryHand() == EnumHandSide.LEFT && player.getHeldItemOffhand().isEmpty())) && Overlay.check ? 97 - Overlay.offX : -130 + Overlay.offX;
                posX = posX / 2 + offX;
                posY = posY - 29 - Overlay.offY;
                drawBrain(player, posX, posY);
                mc().getTextureManager().bindTexture(Gui.ICONS);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    private void drawBlood(EntityPlayerSP player, int posX, int posY) {
        mc().getTextureManager().bindTexture(blood);
        if (!isAwake(player) && value < Overlay.blood && down > -1) {
            float alpha = down % 60 > 30 ? (60 - down) * 0.03f : down * 0.03f;
            GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
            drawScaledCustomSizeModalRect(0, 0, 0, 0, 100, 58, posX, posY, 100, 58);
        }
    }

    private void drawBrain(EntityPlayerSP player, int posX, int posY) {
        if (flash > 0 || Overlay.flash == -1) {
            mc().getTextureManager().bindTexture(indicator);
            if (value < Overlay.shake) {
                posY += player.ticksExisted % 2;
            }
            double percent = value / 100;
            int consume = 22 - (int) (percent * 22 + 0.5);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            drawTexturedModalRect(posX, posY, 0, 0, 33, 24);
            if (glow > -1 && glow % 10 < 5) {
                drawTexturedModalRect(posX, posY, 34, 0, 33, 24);
            }
            drawTexturedModalRect(posX + 1, posY + consume + 1, 68, consume + 1, 31, 22 - consume);
            if (down > -1) {
                posY += down % 20 < 10 ? 1 : 0;
                drawTexturedModalRect(posX + 13, posY + 8, 100, 8, 6, 7);
            } else if (up > -1) {
                posY -= up % 20 < 10 ? 1 : 0;
                drawTexturedModalRect(posX + 13, posY + 9, 107, 8, 6, 7);
            }
        }
    }
}