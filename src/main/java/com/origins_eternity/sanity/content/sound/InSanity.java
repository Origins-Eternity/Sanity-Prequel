package com.origins_eternity.sanity.content.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.sanity.config.Configuration.Effect;
import static com.origins_eternity.sanity.event.ClientEvent.value;
import static com.origins_eternity.sanity.utils.proxy.ClientProxy.mc;

@SideOnly(Side.CLIENT)
public class InSanity extends MovingSound {
    private final EntityPlayer player;

    public InSanity(SoundEvent sound, EntityPlayer player) {
        super(sound, SoundCategory.MUSIC);
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public void update() {
        if (this.player.isDead || !this.player.isEntityAlive() || this.player != mc().player) {
            this.donePlaying = true;
            return;
        }
        if (value >= Effect.whisper) {
            if (this.volume > 0f) {
                volume -= 0.02f;
            } else {
                this.donePlaying = true;
                return;
            }
        }
        this.xPosF = (float) this.player.posX;
        this.yPosF = (float) this.player.posY;
        this.zPosF = (float) this.player.posZ;
    }
}