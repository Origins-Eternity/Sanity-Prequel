package com.origins_eternity.sanity.event;

import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.content.capability.sanity.SanityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.concurrent.ThreadLocalRandom;

import static com.origins_eternity.sanity.Sanity.MOD_ID;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.utils.Utils.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonEvent {
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(MOD_ID, "sanity"), new SanityProvider(Capabilities.SANITY));
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        EntityPlayer old = event.getOriginal();
        EntityPlayer clone = event.getEntityPlayer();
        if (!clone.world.isRemote) {
            Capability<ISanity> capability = Capabilities.SANITY;
            ISanity origin = old.getCapability(capability, null);
            ISanity present = clone.getCapability(capability, null);
            if (!event.isWasDeath()) {
                capability.getStorage().readNBT(capability, present, null, capability.getStorage().writeNBT(capability, origin, null));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().world.isRemote || event.getSource() == null) return;
        Entity killer = event.getSource().getTrueSource();
        if ((killer != null) && (killer instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) killer;
            if (!player.isCreative()) {
                ISanity sanity = player.getCapability(SANITY, null);
                sanity.consumeSanity(ThreadLocalRandom.current().nextInt(1, Configuration.kill + 1));
            }
        }
    }

    @SubscribeEvent
    public static void LivingEntityUseItem(LivingEntityUseItemEvent.Finish event){
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            if ((!player.isCreative()) && (event.getItem().getItem() instanceof ItemFood)) {
                ISanity sanity = player.getCapability(SANITY, null);
                if (itemMatched(event.getItem())) {
                    sanity.consumeSanity(ThreadLocalRandom.current().nextInt(1, Configuration.eat + 1));
                } else {
                    sanity.recoverSanity(ThreadLocalRandom.current().nextInt(1, 6));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event){
        if(!event.getEntityPlayer().world.isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.isCreative()) {
                ISanity sanity = player.getCapability(SANITY, null);
                sanity.recoverSanity(100f - sanity.getSanity());
            }
        }
    }

    @SubscribeEvent
    public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(ThreadLocalRandom.current().nextInt(10, Configuration.lightning + 1));
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(1f);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        OreDictionary.registerOre(Configuration.food, Items.ROTTEN_FLESH);
        OreDictionary.registerOre(Configuration.food, Items.CHICKEN);
        OreDictionary.registerOre(Configuration.food, Items.PORKCHOP);
        OreDictionary.registerOre(Configuration.food, Items.MUTTON);
        OreDictionary.registerOre(Configuration.food, Items.BEEF);
        OreDictionary.registerOre(Configuration.food, Items.RABBIT);
        OreDictionary.registerOre(Configuration.food, Items.POISONOUS_POTATO);

    }

    static int counter;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            counter++;
            if (counter > 10) {
                checkStatus(player);
                if (!player.world.isRemote) {
                    tickPlayer(player);
                }
                counter = 0;
            }
        }
    }
}