package com.origins_eternity.sanity.content.potion;

import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.sanity.config.Configuration.Mechanics;
import static com.origins_eternity.sanity.content.render.Overlay.indicator;

public class Composure extends Potion {
    public Composure(String name) {
        super(false, 0x178EB0);
        setPotionName("effect." + name);
        setRegistryName(name);
        setBeneficial();

        Potions.POTIONS.add(this);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ISanity sanity = player.getCapability(Capabilities.SANITY, null);
            sanity.recoverSanity(Mechanics.composure);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int interval = 60 >> amplifier;
        if (interval > 0) {
            return duration % interval == 0;
        } else {
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.getTextureManager().bindTexture(indicator);
        Gui.drawModalRectWithCustomSizedTexture(x + 7, y + 8, 114, 4, 16, 16, 256, 256);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(indicator);
        Gui.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 114, 4, 16, 16, 256, 256);
    }
}