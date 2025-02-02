package com.origins_eternity.sanity.content.capability.sanity;

import com.origins_eternity.sanity.config.Configuration;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class Sanity implements ISanity {
    private float sanity = 100;

    private int down = 0;

    private int up = 0;

    private int flash = -1;

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
    public void setFlash(int flash) {
        this.flash = Math.max(flash, -1);
    }

    @Override
    public int getFlash() {
        return flash;
    }

    @Override
    public void consumeSanity(double value) {
        if (value >= 0) {
            if (sanity != 0f) {
                if (Configuration.flash != -1) {
                    flash = Configuration.flash;
                }
                if (value >= 1f) {
                    down = 21;
                } else {
                    down = 15;
                }
                sanity = Math.max(sanity - (float) value, 0);
            }
        }
    }

    @Override
    public void recoverSanity(double value) {
        if (value >= 0) {
            if (sanity != 100f) {
                if (Configuration.flash != -1) {
                    flash = Configuration.flash;
                }
                if (value >= 1f) {
                    up = 21;
                } else {
                    up = 15;
                }
                sanity = Math.min(sanity + (float) value, 100);
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
            compound.setFloat("Sanity", instance.getSanity());
            compound.setInteger("Down", instance.getDown());
            compound.setInteger("Up", instance.getUp());
            compound.setInteger("Flash", instance.getFlash());
            return compound;
        }

        @Override
        public void readNBT(Capability<ISanity> capability, ISanity instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.setSanity(compound.getFloat("Sanity"));
                instance.setDown(compound.getInteger("Down"));
                instance.setUp(compound.getInteger("Up"));
                instance.setFlash(compound.getInteger("Flash"));
            }
        }
    }
}