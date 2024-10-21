package com.origins_eternity.sanity.content.capability.sanity;

public class Sanity implements ISanity {
    private float sanity = 20;

    private boolean exhausted = false;

    private boolean tired = false;

    @Override
    public void setSanity(float sanity) {
        this.sanity = sanity;
    }

    @Override
    public float getSanity() {
        return sanity;
    }

    @Override
    public boolean isExhausted() {
        return exhausted;
    }

    @Override
    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    @Override
    public boolean isTired() {
        return tired;
    }

    @Override
    public void setTired(boolean tired) {
        this.tired = tired;
    }

    @Override
    public void consumeSanity(int value) {
        sanity -= value;
        if (sanity <= 5) {
            if (!tired) {
                setTired(true);
            }
            if (sanity <= 0) {
                sanity = 0;
                if (!exhausted) {
                    setExhausted(true);
                }
            }
        }
    }

    @Override
    public void recoverSanity(int value) {
            sanity += value;
            if (sanity > 0) {
                if (exhausted) {
                    exhausted = false;
                }
                if (sanity >= 5) {
                    if (tired) {
                        setTired(false);
                    }
                }
            }
        }
    }