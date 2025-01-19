package com.origins_eternity.sanity.content.capability;

import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import com.origins_eternity.sanity.content.capability.sanity.Sanity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
    @CapabilityInject(ISanity.class)
    public static final Capability<ISanity> SANITY = null;

    public static void registerCapability(CapabilityManager manager) {
        manager.register(ISanity.class, new Sanity.SanityStorage(), Sanity::new);
    }
}