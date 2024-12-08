package com.origins_eternity.sanity.content.capability.sanity;

public class Sanity implements ISanity {
    private float sanity = 100;

    private int wet = 0;

    private int down = 0;

    private int up = 0;

    private boolean garland = false;

    @Override
    public void setSanity(float sanity) {
        this.sanity = sanity;
    }

    @Override
    public float getSanity() {
        return sanity;
    }

    @Override
    public void setWet(int wet) {
        this.wet = wet;
    }

    @Override
    public int getWet() {
        return wet;
    }

    @Override
    public void setDown(int down) {
        this.down = down;
    }

    @Override
    public int getDown() {
        return down;
    }

    @Override
    public void setUp(int up) {
        this.up = up;
    }

    @Override
    public int getUp() {
        return up;
    }

    @Override
    public boolean getGarland() {
        return garland;
    }

    @Override
    public void setGarland(boolean garland) {
        this.garland = garland;
    }

    @Override
    public void consumeSanity(double value) {
        if (!getGarland()) {
            if (sanity != 0f) {
                sanity -= (float) value;
                if (up > 0) {
                    up = 0;
                }
                if (value >= 1f) {
                    down = 21;
                } else {
                    down = 15;
                }
                if (sanity < 0f) {
                    sanity = 0f;
                }
            }
        }
    }

    @Override
    public void recoverSanity(double value) {
        if (sanity != 100f) {
            sanity += (float) value;
            if (down > 0) {
                down = 0;
            }
            up = 15;
            if (sanity > 100f) {
                sanity = 100f;
            }
        }
    }
}