package com.origins_eternity.sanity.content.capability.sanity;

public interface ISanity {
    void setSanity(float sanity);

    float getSanity();

    void setWet(int wet);

    int getWet();

    void setDown(int down);

    int getDown();

    void setUp(int up);

    int getUp();

    boolean getGarland();

    void setGarland(boolean garland);

    void consumeSanity(double value);

    void recoverSanity(double value);
}