package com.origins_eternity.sanity.compat.thaumcraft;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

public class ThaumcraftWarp {

    private static final double MAX_SANITY = 100.0;

    public static void syncWarpFromSanity(EntityPlayer player, ISanity sanity) {
        if (!player.world.isRemote) {
            IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
            if (warp != null) {

                double sanityRatio = sanity.getSanity() / MAX_SANITY;
                int targetWarp = (int) ((1 - sanityRatio) * 50); // 理智越低，扭曲越高，最高50

                int currentWarp = warp.get(IPlayerWarp.EnumWarpType.NORMAL);
                if (currentWarp != targetWarp) {
                    warp.set(IPlayerWarp.EnumWarpType.NORMAL, targetWarp);
                    warp.sync((EntityPlayerMP) player);
                }
            }
        }
    }

    /**
     * 当玩家扭曲变化时同步理智值
     */
    public static void syncSanityFromWarp(EntityPlayer player) {
        if (!player.world.isRemote) {
            IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
            ISanity sanity = player.getCapability(com.origins_eternity.sanity.content.capability.Capabilities.SANITY, null);

            if (warp != null && sanity != null) {
                int totalWarp = warp.get(IPlayerWarp.EnumWarpType.PERMANENT) +
                        warp.get(IPlayerWarp.EnumWarpType.NORMAL) +
                        warp.get(IPlayerWarp.EnumWarpType.TEMPORARY);

                // 扭曲值影响理智（可根据需要调整比例）
                double sanityPenalty = totalWarp * 0.5; // 每点扭曲减少0.5理智
                double currentSanity = sanity.getSanity();
                double maxSanity = MAX_SANITY;

                if (currentSanity > maxSanity - sanityPenalty) {
                    sanity.setSanity(maxSanity - sanityPenalty);
                }
            }
        }
    }

    /**
     * 获取玩家的总扭曲值
     */
    public static int getTotalWarp(EntityPlayer player) {
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
        if (warp != null) {
            return warp.get(IPlayerWarp.EnumWarpType.PERMANENT) +
                    warp.get(IPlayerWarp.EnumWarpType.NORMAL) +
                    warp.get(IPlayerWarp.EnumWarpType.TEMPORARY);
        }
        return 0;
    }
}