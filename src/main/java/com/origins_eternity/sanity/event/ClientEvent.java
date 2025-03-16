package com.origins_eternity.sanity.event;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

import static com.origins_eternity.sanity.Sanity.MOD_ID;
import static com.origins_eternity.sanity.config.Configuration.*;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.content.sound.Sounds.INSANITY;
import static com.origins_eternity.sanity.utils.proxy.ClientProxy.mc;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ClientEvent {
    private static final Random rand = new Random();

    static int confusing;
    static int whisper;
    public static int flash = Overlay.flash;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.isCreative() && !player.isSpectator() && player == mc().player) {
            ISanity sanity = player.getCapability(SANITY, null);
            if (sanity.getSanity() <= 50f) {
                confusing--;
                if (confusing <= 0) {
                    int number = rand.nextInt(Mechanics.sounds.length);
                    SoundEvent sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(Mechanics.sounds[number]));
                    if (sound != null) {
                        player.playSound(sound, 1f, 0.5f);
                    }
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
            if (Overlay.flash != -1) {
                if (sanity.getDown() >= 15 || sanity.getUp() >= 15) {
                    flash = Overlay.flash * 20;
                } else if (flash > 0) {
                    flash--;
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
            EntityRenderer renderer = mc().entityRenderer;
            if (sanity.getSanity() <= Shader.bits) {
                if (!bits || !renderer.isShaderActive()) {
                    renderer.loadShader(new ResourceLocation(BITS));
                    bits = true;
                    deconverge = false;
                    notch = false;
                }
            } else if (sanity.getSanity() <= Shader.notch) {
                if (!notch || !renderer.isShaderActive()) {
                    renderer.loadShader(new ResourceLocation(NOTCH));
                    notch = true;
                    deconverge = false;
                    bits = false;
                }
            } else if (sanity.getSanity() <= Shader.deconverge) {
                if (!deconverge || !renderer.isShaderActive()) {
                    renderer.loadShader(new ResourceLocation(DECONVERGE));
                    deconverge = true;
                    notch = false;
                    bits = false;
                }
            } else if (deconverge || notch || bits) {
                renderer.stopUseShader();
                deconverge = false;
                notch = false;
                bits = false;
            }
        }
    }
}