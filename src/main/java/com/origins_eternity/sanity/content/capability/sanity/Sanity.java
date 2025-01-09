package com.origins_eternity.sanity.content.capability.sanity;

public class Sanity implements ISanity {
    private float sanity = 100;

    private int down = 0;

    private int up = 0;

    @Override
    public void setSanity(float sanity) {
        this.sanity = sanity;
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
    public void consumeSanity(double value) {
        if (sanity != 0f) {
            if (value >= 1f) {
                down = 21;
            } else {
                down = 15;
            }
            sanity = Math.max(sanity - (float) value, 0);
        }
    }

    @Override
    public void recoverSanity(double value) {
        if (sanity != 100f) {
            if (value >= 1f) {
                up = 21;
            } else {
                up = 15;
            }
            sanity = Math.min(sanity + (float) value, 100);
        }
    }
}