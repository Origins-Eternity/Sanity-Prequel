package com.origins_eternity.sanity.content.capability.sanity;

public interface ISanity {
    void setMax(int sanity);

    int getMax();

    void setSanity(double sanity);

    float getSanity();

    int getCoolDown();

    void setCoolDown(int coolDown);

    void removeCoolDown();

    void setEnable(boolean enable);

    boolean getEnable();

    void consumeSanity(double value);

    void recoverSanity(double value);
}