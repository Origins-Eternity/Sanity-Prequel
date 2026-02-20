package com.origins_eternity.sanity.compat;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.render.IRenderBauble;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.content.sound.Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.origins_eternity.sanity.Sanity.MOD_ID;
import static com.origins_eternity.sanity.config.Configuration.Mechanics;
import static com.origins_eternity.sanity.content.tab.CreativeTab.SANITY;
import static com.origins_eternity.sanity.utils.Utils.isWet;
import static com.origins_eternity.sanity.utils.proxy.ClientProxy.mc;

public class Baubles extends ItemArmor implements IBauble, IRenderBauble {

    public Baubles(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlot, String name, int maxdamage) {
        super(material, renderIndex, equipmentSlot);
        setTranslationKey(MOD_ID + "." + name.toLowerCase());
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
        ItemStack stack = player.getHeldItem(hand);
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        int slot = BaubleType.HEAD.getValidSlots()[0];
        if (baubles.getStackInSlot(slot).isEmpty()) {
            baubles.setStackInSlot(slot, stack.copy());
            stack.setCount(0);
            onEquipped(player.getHeldItem(hand), player);
        } else if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            player.setItemStackToSlot(EntityEquipmentSlot.HEAD, stack.copy());
            stack.setCount(0);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        player.playSound(Sounds.FLOWERS_EQUIP, .75F, 2f);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        EntityPlayer player = (EntityPlayer) entity;
        if (player.ticksExisted % 10 == 0) {
            ISanity sanity = player.getCapability(Capabilities.SANITY, null);
            if (sanity.getDown() == 0f && !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(this)) {
                sanity.recoverSanity(Mechanics.garland);
            }
            if (isWet(player)) {
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
            if (sanity.getDown() == 0f) {
                sanity.recoverSanity(Mechanics.garland);
            }
            if (isWet(player)) {
                if (player.ticksExisted % 20 == 0) {
                    stack.damageItem(1, player);
                }
            }
            super.onArmorTick(world, player, stack);
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (repair.getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock) repair.getItem()).getBlock();
            ResourceLocation location = repair.getItem().getRegistryName();
            return block instanceof BlockFlower || location.equals(new ResourceLocation("futuremc:cornflower")) || location.equals(new ResourceLocation("futuremc:lily_of_the_valley"));
        }
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack ItemStack) {
        return EnumRarity.COMMON;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, ModelBiped defaultModel) {
        if (slot == EntityEquipmentSlot.HEAD) {
            return new ModelBiped(1.0F);
        }
        return null;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return MOD_ID + ":textures/models/armor/flower_layer_1.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, baubles.api.render.IRenderBauble.RenderType type, float partialTicks) {
        if (type == baubles.api.render.IRenderBauble.RenderType.HEAD) {
            ModelBiped armorModel = getArmorModel(player, stack, EntityEquipmentSlot.HEAD, null);
            if (armorModel != null && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                mc().getTextureManager().bindTexture(new ResourceLocation(getArmorTexture(stack, player, EntityEquipmentSlot.HEAD, null)));
                armorModel.bipedHead.render(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }
}