package com.origins_eternity.sanity.content.tool;

import com.origins_eternity.sanity.Sanity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;

import static com.origins_eternity.sanity.content.tab.CreativeTab.SANITY;

public class ToolCreator extends ItemTool {
    public ToolCreator(ToolMaterial material, String name, int maxdamage) {
        super(0f, -3.5f, material, Collections.emptySet());
        setTranslationKey(Sanity.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxStackSize(1);
        setMaxDamage(maxdamage);
        setCreativeTab(SANITY);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return 0.5f;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(5, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote && (double)state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(5, entityLiving);
        }
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem().equals(Items.LEATHER);
    }
}