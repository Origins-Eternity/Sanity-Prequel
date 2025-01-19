package com.origins_eternity.sanity.compat;

import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class CraftTweaker {
    @ZenMethod
    @ZenGetter("sanity")
    public static float getSanity(IPlayer player) {
        ISanity sanity = CraftTweakerMC.getPlayer(player).getCapability(Capabilities.SANITY, null);
        return sanity.getSanity();
    }
}