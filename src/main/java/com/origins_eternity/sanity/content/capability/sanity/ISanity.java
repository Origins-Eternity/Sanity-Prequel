package com.origins_eternity.sanity.content.capability.sanity;

public interface ISanity {
    void setSanity(double sanity);

    float getSanity();

    void setDown(int down);

    int getDown();

    void setUp(int up);

    int getUp();

    void setEnable(boolean enable);

    boolean getEnable();

    void consumeSanity(double value);

    void recoverSanity(double value);
}