package com.origins_eternity.sanity.compat;

import com.origins_eternity.sanity.capability.Capabilities;
import com.origins_eternity.sanity.capability.sanity.ISanity;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class CraftTweaker {
    @ZenMethod
    @ZenGetter("sanity")
    public static float getSanity(IPlayer player) {
        ISanity sanity = CraftTweakerMC.getPlayer(player).getCapability(Capabilities.SANITY, null);
        return sanity.getSanity();
    }

    @ZenMethod
    @ZenSetter("sanity")
    public static void setSanity(IPlayer player, float value) {
        ISanity sanity = CraftTweakerMC.getPlayer(player).getCapability(Capabilities.SANITY, null);
        float delta = value - sanity.getSanity();
        if (delta > 0) {
            sanity.recoverSanity(delta);
        } else if (delta < 0) {
            sanity.consumeSanity(-delta);
        }
    }
}