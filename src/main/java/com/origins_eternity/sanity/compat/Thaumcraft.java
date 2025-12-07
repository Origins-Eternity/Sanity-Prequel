package com.origins_eternity.sanity.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

public class Thaumcraft {
    @Optional.Method(modid = "thaumcraft")
    public static int getWarp(EntityPlayer player) {
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
        int value = warp.get(IPlayerWarp.EnumWarpType.PERMANENT) + warp.get(IPlayerWarp.EnumWarpType.NORMAL);
        return Math.min(value, 50);
    }
}