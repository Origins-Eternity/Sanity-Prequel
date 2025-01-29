package com.origins_eternity.sanity.event;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
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

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ClientEvent {
    private static final SoundEvent[] SOUNDS = new SoundEvent[]{
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
            SoundEvents.ENTITY_WOLF_GROWL
    };

    private static final Random rand = new Random();

    static int confusing;
    static int whisper;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.isCreative() && !player.isSpectator() && player == mc().player) {
            ISanity sanity = player.getCapability(SANITY, null);
            if (sanity.getSanity() <= 50f) {
                confusing--;
                if (confusing <= 0) {
                    player.playSound(SOUNDS[rand.nextInt(SOUNDS.length)], 1f, 0.5f);
                    confusing = rand.nextInt(600) + 800;
                }
                if (sanity.getSanity() <= 45f) {
                    whisper--;
                    ISound insanity = PositionedSoundRecord.getMasterRecord(INSANITY, 1f);
                    if (whisper <= 0) {
                        mc().getSoundHandler().playSound(insanity);
                        whisper = 680;
                    }
                }
            }
        }
    }

    private static final String DECONVERGE = "shaders/post/deconverge.json";
    private static final String NOTCH = "shaders/post/notch.json";
    private static final String BITS = "shaders/post/bits.json";

    static boolean deconverge = false;
    static boolean notch = false;
    static boolean bits = false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        EntityPlayer player = mc().player;
        if (event.phase == TickEvent.Phase.END && player != null && OpenGlHelper.shadersSupported) {
            ISanity sanity = player.getCapability(SANITY, null);
            if (sanity.getSanity() <= 10f) {
                if (!bits || !mc().entityRenderer.isShaderActive()) {
                    mc().entityRenderer.loadShader(new ResourceLocation(BITS));
                    bits = true;
                    deconverge = false;
                    notch = false;
                }
            } else if (sanity.getSanity() <= 40f) {
                if (!notch || !mc().entityRenderer.isShaderActive()) {
                    mc().entityRenderer.loadShader(new ResourceLocation(NOTCH));
                    notch = true;
                    deconverge = false;
                    bits = false;
                }
            } else if (sanity.getSanity() <= 60f) {
                if (!deconverge || !mc().entityRenderer.isShaderActive()) {
                    mc().entityRenderer.loadShader(new ResourceLocation(DECONVERGE));
                    deconverge = true;
                    notch = false;
                    bits = false;
                }
            } else if (deconverge || notch || bits) {
                mc().entityRenderer.stopUseShader();
            }
        }
    }
}