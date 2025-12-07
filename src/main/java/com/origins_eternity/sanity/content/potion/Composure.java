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

import static com.origins_eternity.sanity.utils.proxy.ClientProxy.icon;

public class Composure extends Potion {
    private static final String name = "composure";

    public Composure() {
        super(false, 0x178EB0);
        setPotionName("effect." + name);
        setRegistryName(name);
        setBeneficial();

        Potions.POTIONS.add(this);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (!entity.world.isRemote) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                ISanity sanity = player.getCapability(Capabilities.SANITY, null);
                sanity.recoverSanity(1);
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int interval = 50 >> amplifier;
        if (interval <= 0) {
            interval = 1;
        }
        return duration % interval == 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.getTextureManager().bindTexture(icon);
        Gui.drawModalRectWithCustomSizedTexture(x + 7, y + 8, 0, 0, 16, 16, 16, 16);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(icon);
        Gui.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 0, 0, 16, 16, 16, 16);
    }
}