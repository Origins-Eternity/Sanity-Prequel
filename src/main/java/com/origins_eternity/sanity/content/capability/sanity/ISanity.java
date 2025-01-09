package com.origins_eternity.sanity.content.capability.sanity;

public interface ISanity {
    void setSanity(float sanity);

    float getSanity();

    void setDown(int down);

    int getDown();

    void setUp(int up);

    int getUp();

    void consumeSanity(double value);

    void recoverSanity(double value);
}