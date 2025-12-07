package com.origins_eternity.sanity.content.capability.sanity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class Sanity implements ISanity {
    private int max = 100;

    private float sanity = 100;

    private int down = 0;

    private int up = 0;

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
    public void setDown(int down) {
        this.down = Math.max(down, 0);
    }

    @Override
    public int getDown() {
        return down;
    }

    @Override
    public void setUp(int up) {
        this.up = Math.max(up, 0);
    }

    @Override
    public int getUp() {
        return up;
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
                if (value >= 1) {
                    setDown(21);
                } else if (value > 0) {
                    setDown(15);
                }
                sanity = Math.max(sanity - (float) value, 0);
            }
        }
    }

    @Override
    public void recoverSanity(double value) {
        if (value >= 0 && enable) {
            if (sanity < max) {
                if (value >= 1) {
                    setUp(21);
                } else if (value > 0) {
                    setUp(15);
                }
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
            compound.setFloat("Sanity", instance.getSanity());
            compound.setInteger("Down", instance.getDown());
            compound.setInteger("Up", instance.getUp());
            compound.setBoolean("Enable", instance.getEnable());
            return compound;
        }

        @Override
        public void readNBT(Capability<ISanity> capability, ISanity instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.setMax(compound.getInteger("Max"));
                instance.setSanity(compound.getFloat("Sanity"));
                instance.setDown(compound.getInteger("Down"));
                instance.setUp(compound.getInteger("Up"));
                instance.setEnable(compound.getBoolean("Enable"));
            }
        }
    }
}