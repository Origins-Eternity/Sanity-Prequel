package com.origins_eternity.sanity.event;

import baubles.api.BaublesApi;
import com.origins_eternity.sanity.content.armor.Armors;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.content.capability.sanity.Sanity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.*;
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
import static com.origins_eternity.sanity.compat.Thaumcraft.getWarp;
import static com.origins_eternity.sanity.config.Configuration.Mechanics;
import static com.origins_eternity.sanity.content.capability.Capabilities.SANITY;
import static com.origins_eternity.sanity.content.umbrella.Umbrellas.UMBRELLA;
import static com.origins_eternity.sanity.utils.Utils.*;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonEvent {
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(MOD_ID, "sanity"), new Sanity.SanityProvider(Capabilities.SANITY));
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
            if (!event.isWasDeath() || !Mechanics.reset) {
                capability.getStorage().readNBT(capability, present, null, capability.getStorage().writeNBT(capability, origin, null));
            }
        }
    }

    @SubscribeEvent
    public static void LivingEntityUseItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            if ((!player.isCreative()) && (event.getItem().getItem() instanceof ItemFood)) {
                ISanity sanity = player.getCapability(SANITY, null);
                int num = stackMatched(event.getItem());
                if (num == -1) {
                    sanity.recoverSanity(Mechanics.eat);
                } else {
                    double value = Double.parseDouble(Mechanics.food[num].split(";")[1]);
                    if (value > 0) {
                        sanity.recoverSanity(value);
                    } else {
                        sanity.consumeSanity(-value);
                    }
                }
            }
        }
    }

    static long time;

    @SubscribeEvent
    public static void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        if (!event.getEntityPlayer().world.isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.isCreative()) {
                time = player.world.getWorldTime();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!event.getEntityPlayer().world.isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.isCreative()) {
                double sleep = (player.world.getWorldTime() - time) / 12000.0;
                ISanity sanity = player.getCapability(SANITY, null);
                sanity.recoverSanity(Mechanics.sleep * sleep);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(Mechanics.lightning);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(Mechanics.hurt * event.getAmount());
        } else {
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
                if (!player.isCreative()) {
                    ISanity sanity = player.getCapability(SANITY, null);
                    if (event.getEntity() instanceof EntityAnimal) {
                        sanity.consumeSanity(Mechanics.attackAnimal);
                    } else if (event.getEntity() instanceof EntityVillager) {
                        sanity.consumeSanity(Mechanics.attackVillager);
                    } else if (event.getEntity() instanceof EntityPlayer) {
                        sanity.consumeSanity(Mechanics.attackPlayer);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if ((!player.isSpectator()) && (!player.isCreative())) {
            if (player.ticksExisted % 10 == 0 && !player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                sanity.setEnable(canEnable(player));
                if (Loader.isModLoaded("thaumcraft")) {
                    sanity.setMax(100 - getWarp(player));
                    if (sanity.getSanity() > sanity.getMax()) {
                        sanity.setSanity(sanity.getMax());
                    }
                }
                if (sanity.getDown() > 0) {
                    sanity.setDown(sanity.getDown() - 1);
                }
                if (sanity.getUp() > 0) {
                    sanity.setUp(sanity.getUp() - 1);
                }
                double value = tickPlayer(player);
                if (value > 0) {
                    sanity.recoverSanity(value);
                } else if (value < 0) {
                    sanity.consumeSanity(-value);
                }
                syncSanity(player);
            }
        }
    }

    @SubscribeEvent
    public static void onBabyEntitySpawn(BabyEntitySpawnEvent event) {
        if (event.getCausedByPlayer() != null) {
            EntityPlayer player = event.getCausedByPlayer();
            if (!player.world.isRemote) {
                ISanity sanity = player.getCapability(SANITY, null);
                if (sanity.getUp() == 0) {
                    sanity.recoverSanity(Mechanics.bred);
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
            sanity.recoverSanity(Mechanics.advancement);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        EntityPlayer player = event.player;
        if (!player.isCreative() && !player.world.isRemote) {
            ISanity sanity = player.getCapability(SANITY, null);
            sanity.consumeSanity(Mechanics.trip);
        }
    }
    
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!event.getEntity().world.isRemote) {
            if (event.getEntity() instanceof EntityTameable) {
                EntityTameable entityTameable = (EntityTameable) event.getEntity();
                if (entityTameable.isTamed() && entityTameable.getOwner() != null && entityTameable.getOwner() instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entityTameable.getOwner();
                    ISanity sanity = player.getCapability(SANITY, null);
                    sanity.consumeSanity(Mechanics.lost);
                }
            } else if (event.getEntity() instanceof EntityMob && event.getSource().getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
                ISanity sanity = player.getCapability(SANITY, null);
                if (sanity.getUp() == 0) {
                    sanity.recoverSanity(Mechanics.killMob);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.world.isRemote) {
                int damage = 0;
                if (event.getSource() == DamageSource.LIGHTNING_BOLT) {
                    damage = 30;
                } else if (event.getSource().isFireDamage()) {
                    damage = 20;
                } else if (event.getSource().isExplosion()) {
                    damage = 10;
                }
                if (damage != 0) {
                    if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().getRegistryName().equals(new ResourceLocation("sanity:garland"))) {
                        player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem(damage, player);
                    }
                    if (Loader.isModLoaded("baubles") && BaublesApi.getBaublesHandler(player).getStackInSlot(4).getItem().equals(Armors.GARLAND)) {
                        BaublesApi.getBaublesHandler(player).getStackInSlot(4).damageItem(damage, player);
                    }
                    if (player.getHeldItemMainhand().getItem().equals(UMBRELLA)) {
                        player.getHeldItemMainhand().damageItem(damage, player);
                    }
                    if (player.getHeldItemOffhand().getItem().equals(UMBRELLA)) {
                        player.getHeldItemOffhand().damageItem(damage, player);
                    }
                }
            }
        }
    }
}