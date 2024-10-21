package com.origins_eternity.sanity.content.capability.sanity;

public interface ISanity {
    void setSanity(float sanity);

    float getSanity();

    boolean isExhausted();

    void setExhausted(boolean exhausted);

    boolean isTired();

    void setTired(boolean tired);

    void consumeSanity(int value);

    void recoverSanity(int value);
}
