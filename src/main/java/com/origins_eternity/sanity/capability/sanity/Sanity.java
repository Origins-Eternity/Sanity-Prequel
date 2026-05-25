package com.origins_eternity.sanity.capability.sanity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class Sanity implements ISanity {
    private int max = 100;
    private float sanity = 100f;
    private float increase = 1f;
    private float decrease = 1f;
    private int coolDown = 0;
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
        this.sanity = (float) MathHelper.clamp(sanity, 0, max);
    }

    @Override
    public float getSanity() {
        return sanity;
    }

    @Override
    public void setIncreaseFactor(double increase) {
        this.increase = (float) increase;
    }

    @Override
    public float getIncreaseFactor() {
        return increase;
    }

    @Override
    public void setDecreaseFactor(double decrease) {
        this.decrease = (float) decrease;
    }

    @Override
    public float getDecreaseFactor() {
        return decrease;
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
    public void removeCoolDown() {
        if (coolDown > 0) {
            coolDown = Math.max(coolDown - 10, 0);
        }
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
                sanity = (float) Math.max(sanity - decrease * value, 0);
            }
        }
    }

    @Override
    public void recoverSanity(double value) {
        if (value >= 0 && enable) {
            if (sanity < max) {
                sanity = (float) Math.min(sanity + increase * value, max);
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
            compound.setBoolean("Enable", instance.getEnable());
            compound.setInteger("CoolDown", instance.getCoolDown());
            compound.setFloat("Increase", instance.getIncreaseFactor());
            compound.setFloat("Decrease", instance.getDecreaseFactor());
            return compound;
        }

        @Override
        public void readNBT(Capability<ISanity> capability, ISanity instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.setMax(compound.getInteger("Max"));
                instance.setSanity(compound.getFloat("Sanity"));
                instance.setEnable(compound.getBoolean("Enable"));
                instance.setCoolDown(compound.getInteger("CoolDown"));
                instance.setIncreaseFactor(compound.getFloat("Increase"));
                instance.setDecreaseFactor(compound.getFloat("Decrease"));
            }
        }
    }
}