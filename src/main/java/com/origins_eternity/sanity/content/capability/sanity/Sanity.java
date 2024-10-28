package com.origins_eternity.sanity.content.capability.sanity;

public class Sanity implements ISanity {
    private int sanity = 100;

    private int down = 0;

    private int up = 0;

    private boolean lost = false;

    private boolean dizzy = false;

    @Override
    public void setSanity(int sanity) {
        this.sanity = sanity;
    }

    @Override
    public int getSanity() {
        return sanity;
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
    public boolean isLost() {
        return lost;
    }

    @Override
    public void setLost(boolean lost) {
        this.lost = lost;
    }

    @Override
    public boolean isDizzy() {
        return dizzy;
    }

    @Override
    public void setDizzy(boolean dizzy) {
        this.dizzy = dizzy;
    }

    @Override
    public void consumeSanity(int value) {
        sanity -= value;
        if (up > 0) {
            up = 0;
        }
        down += 30;
        if (sanity < 50) {
            if (!dizzy) {
                setDizzy(true);
            }
            if (sanity < 10) {
                if (!lost) {
                    setLost(true);
                }
                if (sanity < 0) {
                    sanity = 0;
                }
            }
        }
    }

    @Override
    public void recoverSanity(int value) {
            sanity += value;
            if (down > 0) {
                down = 0;
            }
            up += 30;
            if (sanity >= 10) {
                if (lost) {
                    setLost(false);
                }
                if (sanity >= 50) {
                    if (dizzy) {
                        setDizzy(false);
                    }
                    if (sanity > 100) {
                        sanity = 100;
                    }
                }
            }
        }
    }