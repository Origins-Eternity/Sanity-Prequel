package com.origins_eternity.sanity.capability.sanity;

public interface ISanity {
    void setMax(int sanity);

    int getMax();

    void setSanity(double sanity);

    float getSanity();

    void setIncreaseFactor(double increase);

    float getIncreaseFactor();

    void setDecreaseFactor(double decrease);

    float getDecreaseFactor();

    int getCoolDown();

    void setCoolDown(int coolDown);

    void removeCoolDown();

    void setEnable(boolean enable);

    boolean getEnable();

    void consumeSanity(double value);

    void recoverSanity(double value);
}