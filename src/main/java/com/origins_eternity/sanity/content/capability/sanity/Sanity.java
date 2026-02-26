package com.origins_eternity.sanity.content.capability.sanity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class Sanity implements ISanity {
    private int max = 100;

    private int coolDown = 0;

    private float sanity = 100;

    private boolean enable = true;

    @Override
    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public void setSanity(double sanity) {
        this.sanity = (float) sanity;
    }

    @Override
    public float getSanity() {
        return sanity;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    @Override
    public void coolDown() {
        coolDown -= 10;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public boolean getEnable() {
        return enable;
    }

    @Override
    public void consumeSanity(double value) {
        if (value >= 0 && enable) {
            if (sanity > 0f) {
                sanity = Math.max(sanity - (float) value, 0);
                setCoolDown(60);
            }
        }
    }

    @Override
    public void recoverSanity(double value) {
        if (value >= 0 && enable) {
            if (sanity < max) {
                sanity = Math.min(sanity + (float) value, max);
            }
        }
    }

    public static class SanityProvider implements ICapabilitySerializable<NBTBase> {
        final Capability<ISanity> capability;
        final ISanity instance;

        public SanityProvider(Capability<ISanity> sanity) {
            this.capability = sanity;
            this.instance = capability.getDefaultInstance();
        }

        @Override
        public boolean hasCapability(Capability<?> sanity, EnumFacing facing) {
            return sanity == this.capability;
        }

        @Override
        public <T> T getCapability(Capability<T> sanity, EnumFacing facing) {
            if (sanity == this.capability) {
                return capability.cast(this.instance);
            }
            return null;
        }

        @Override
        public NBTBase serializeNBT() {
            return this.capability.writeNBT(this.instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            this.capability.readNBT(this.instance, null, nbt);
        }
    }

    public static class SanityStorage implements Capability.IStorage<ISanity> {
        @Override
        public NBTBase writeNBT(Capability<ISanity> capability, ISanity instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("Max", instance.getMax());
            compound.setInteger("CoolDown", instance.getCoolDown());
            compound.setFloat("Sanity", instance.getSanity());
            compound.setBoolean("Enable", instance.getEnable());
            return compound;
        }

        @Override
        public void readNBT(Capability<ISanity> capability, ISanity instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.setMax(compound.getInteger("Max"));
                instance.setCoolDown(compound.getInteger("CoolDown"));
                instance.setSanity(compound.getFloat("Sanity"));
                instance.setEnable(compound.getBoolean("Enable"));
            }
        }
    }
}