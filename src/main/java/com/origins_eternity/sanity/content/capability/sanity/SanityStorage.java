package com.origins_eternity.sanity.content.capability.sanity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class SanityStorage implements Capability.IStorage<ISanity> {
        @Override
        public NBTBase writeNBT(Capability<ISanity> capability, ISanity instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("Sanity", instance.getSanity());
            compound.setInteger("Down", instance.getDown());
            compound.setInteger("Up", instance.getUp());
            compound.setBoolean("Lost", instance.isLost());
            compound.setBoolean("Dizzy", instance.isDizzy());
            return compound;
        }

        @Override
        public void readNBT(Capability<ISanity> capability, ISanity instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.setSanity(compound.getInteger("Sanity"));
                instance.setDown(compound.getInteger("Down"));
                instance.setUp(compound.getInteger("Up"));
                instance.setLost(compound.getBoolean("Lost"));
                instance.setDizzy(compound.getBoolean("Dizzy"));
            }
        }
    }
