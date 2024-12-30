package com.origins_eternity.sanity.event;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

import static com.origins_eternity.sanity.Sanity.MOD_ID;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.content.sound.Sounds.INSANITY;
import static com.origins_eternity.sanity.utils.proxy.ClientProxy.mc;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = MOD_ID)
public class ClientEvent {
    private static final net.minecraft.util.SoundEvent[] SOUNDS = new SoundEvent[]{
            SoundEvents.ENTITY_CREEPER_PRIMED,
            SoundEvents.ENTITY_TNT_PRIMED,
            SoundEvents.ENTITY_SKELETON_AMBIENT,
            SoundEvents.ENTITY_SKELETON_STEP,
            SoundEvents.ENTITY_ZOMBIE_AMBIENT,
            SoundEvents.ENTITY_ZOMBIE_STEP,
            SoundEvents.ENTITY_ENDERMEN_AMBIENT,
            SoundEvents.ENTITY_HOSTILE_BIG_FALL,
            SoundEvents.BLOCK_CHEST_OPEN,
            SoundEvents.BLOCK_CHEST_CLOSE,
            SoundEvents.BLOCK_WOODEN_DOOR_OPEN,
            SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN,
            SoundEvents.ENTITY_WOLF_GROWL,
            SoundEvents.ENTITY_SPLASH_POTION_BREAK,
            SoundEvents.ENTITY_SPIDER_AMBIENT
    };

    static int confusing;
    static int whisper;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.isCreative() && !player.isSpectator()) {
            ISanity sanity = player.getCapability(SANITY, null);
            ISound insanity = PositionedSoundRecord.getMusicRecord(INSANITY);
            if (sanity.getSanity() >= 40f) {
                if (whisper > 0) {
                    mc().getSoundHandler().stopSounds();
                    whisper = 0;
                }
            }
            if (sanity.getSanity() < 50f) {
                confusing--;
                if (confusing <= 0) {
                    player.world.playSound(player.posX, player.posY, player.posZ, SOUNDS[new Random().nextInt(SOUNDS.length)], SoundCategory.AMBIENT, 1f, 0.5f, false);
                    confusing = new Random().nextInt(600) + 800;
                }
                if (sanity.getSanity() < 40f) {
                    whisper--;
                    if (whisper <= 0) {
                        mc().getSoundHandler().playSound(insanity);
                        whisper = 680;
                    }
                }
            }
        }
    }
}