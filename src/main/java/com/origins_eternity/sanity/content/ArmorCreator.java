package com.origins_eternity.sanity.content;

import com.origins_eternity.sanity.Sanity;
import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static com.origins_eternity.sanity.content.tab.CreativeTab.SANITY;
import static com.origins_eternity.sanity.utils.Utils.dangerHead;
import static com.origins_eternity.sanity.utils.Utils.isWet;

public class ArmorCreator extends ItemArmor {
    public ArmorCreator(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlot, String name, int maxdamage) {
        super(material, renderIndex, equipmentSlot);
        setTranslationKey(Sanity.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxDamage(maxdamage);
        setCreativeTab(SANITY);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (player.ticksExisted % 10 == 0) {
            ISanity sanity = player.getCapability(Capabilities.SANITY, null);
            if (player.inventory.armorItemInSlot(3).getItem().equals(this)) {
                if (sanity.getDown() == 0f) {
                    sanity.recoverSanity(Configuration.garland);
                }
                if (isWet(player) || dangerHead(player)) {
                    if (player.ticksExisted % 20 == 0) {
                        stack.damageItem(1, player);
                    }
                }
                super.onArmorTick(world, player, stack);
            }
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
}