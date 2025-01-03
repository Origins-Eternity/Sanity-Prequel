package com.origins_eternity.sanity.event;

import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.armor.Armors;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.content.capability.sanity.SanityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
    public static void LivingEntityUseItem(LivingEntityUseItemEvent.Finish event){
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            if ((!player.isCreative()) && (event.getItem().getItem() instanceof ItemFood)) {
                ISanity sanity = player.getCapability(SANITY, null);
                if (itemMatched(event.getItem()) != -1) {
                    double value = Double.parseDouble(Configuration.food[itemMatched(event.getItem())].split(";")[1]);
                    sanity.consumeSanity(value);
                } else {
                    sanity.recoverSanity(Configuration.eat);
                }
            }
        }
    }

    static long time;

    @SubscribeEvent
    public static void onPlayerSleepInBed(PlayerSleepInBedEvent event){
        if(!event.getEntityPlayer().world.isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.isCreative()) {
                time = player.world.getWorldTime();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if(!event.getEntityPlayer().world.isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.isCreative()) {
                double sleep = (player.world.getWorldTime() - time) / 12000.0;
                ISanity sanity = player.getCapability(SANITY, null);
                sanity.recoverSanity(Configuration.sleep * sleep);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(Configuration.lightning);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(Configuration.hurt * event.getAmount());
        } else {
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
                if (!player.isCreative()) {
                    ISanity sanity = player.getCapability(SANITY, null);
                    if (event.getEntity() instanceof EntityAnimal) {
                        sanity.consumeSanity(Configuration.attackAnimal);
                    } else if (event.getEntity() instanceof EntityMob) {
                        sanity.consumeSanity(Configuration.attackMob);
                    } else if (event.getEntity() instanceof EntityVillager) {
                        sanity.consumeSanity(Configuration.attackVillager);
                    } else if (event.getEntity() instanceof  EntityPlayer) {
                        sanity.consumeSanity(Configuration.attackPlayer);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            if (player.ticksExisted % 10 == 0) {
                checkStatus(player);
                if (!player.world.isRemote) {
                    tickPlayer(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        if (event.getAdvancement().getDisplay() == null || !event.getAdvancement().getDisplay().shouldAnnounceToChat()) return;
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isCreative() && !player.world.isRemote) {
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.recoverSanity(Configuration.advancement);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        EntityPlayer player = event.player;
        if (!player.isCreative() && !player.world.isRemote) {
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(Configuration.trip);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                if (sanity.getGarland()) {
                    ItemStack item = player.inventory.armorItemInSlot(3);
                    if (event.getSource() == DamageSource.LIGHTNING_BOLT) {
                        item.damageItem(30, player);
                    } else if (event.getSource().isFireDamage()) {
                        item.damageItem(20, player);
                    } else if (event.getSource().isExplosion()) {
                        item.damageItem(10, player);
                    } else {
                        item.damageItem(1, player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                if (Loader.isModLoaded("baubles")) {
                    if (event.getTo().getItem().equals(Armors.GARLAND)) {
                        sanity.setGarland(true);
                    } else if (event.getFrom().getItem().equals(Armors.GARLAND)) {
                        sanity.setGarland(false);
                    }
                } else {
                    if (event.getTo().getItem().equals(Armors.FLOWER)) {
                        sanity.setGarland(true);
                    } else if (event.getFrom().getItem().equals(Armors.FLOWER)) {
                        sanity.setGarland(false);
                    }
                }
            }
        }
    }
}