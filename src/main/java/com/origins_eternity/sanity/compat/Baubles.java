package com.origins_eternity.sanity.compat;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import com.origins_eternity.sanity.Sanity;
import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.content.sound.Sounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static com.origins_eternity.sanity.content.tab.CreativeTab.SANITY;
import static com.origins_eternity.sanity.utils.Utils.isDangerous;
import static com.origins_eternity.sanity.utils.Utils.isWet;

public class Baubles extends ItemArmor implements IBauble {
    public Baubles(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlot, String name, int maxdamage) {
        super(material, renderIndex, equipmentSlot);
        setTranslationKey(Sanity.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxDamage(maxdamage);
        setNoRepair();
        setCreativeTab(SANITY);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.HEAD;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
            if ((baubles.getStackInSlot(4) == null || baubles.getStackInSlot(4).isEmpty()) && baubles.isItemValidForSlot(4, player.getHeldItem(hand), player)) {
                baubles.setStackInSlot(4, player.getHeldItem(hand).copy());
                if (!player.capabilities.isCreativeMode) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                }
                onEquipped(player.getHeldItem(hand), player);
            }
        } else {
            player.playSound(Sounds.FLOWERS_EQUIP, .75F, 2f);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        EntityPlayer player = (EntityPlayer) entity;
        if (player.ticksExisted % 10 == 0) {
            ISanity sanity = player.getCapability(Capabilities.SANITY, null);
            if (sanity.getDown() == 0f) {
                sanity.recoverSanity(Configuration.garland);
            }
            if (isWet(player) || isDangerous(player)) {
                if (player.ticksExisted % 20 == 0) {
                    itemstack.damageItem(1, player);
                }
            }
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (player.ticksExisted % 10 == 0) {
            ISanity sanity = player.getCapability(Capabilities.SANITY, null);
            if (player.inventory.armorItemInSlot(3).getItem().equals(this)) {
                ItemStack bauble = BaublesApi.getBaublesHandler(player).getStackInSlot(4);
                if (sanity.getDown() == 0f && !bauble.getItem().equals(this)) {
                    sanity.recoverSanity(Configuration.garland);
                }
                if (player.isWet()) {
                    if (player.ticksExisted % 20 == 0) {
                        stack.damageItem(1, player);
                    }
                }
                super.onArmorTick(world, player, stack);
            }
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack ItemStack) {
        return EnumRarity.COMMON;
    }
}