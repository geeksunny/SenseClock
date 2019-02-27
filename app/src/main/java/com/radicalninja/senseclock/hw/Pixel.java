package com.radicalninja.senseclock.hw;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class Pixel {

    public static final int COLOR_EMPTY = -255;

    private boolean blinking = false;
    @ColorInt private int color = Color.TRANSPARENT;
    private boolean positionLocked = false;
    private boolean colorLocked = false;

    // TODO: change x/y over to a Point object, use its built-in offset/negate methods for positioning

    private int x;
    private int y;

    public Pixel(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isBlinking() {
        return blinking;
    }

    public void setBlinking(final boolean blinking) {
        this.blinking = blinking;
    }

    public int getColor() {
        return (blinking) ? 0 : color;
    }

    public void setColor(@ColorInt final int color) {
        if (!colorLocked) {
            this.color = color;
        }
    }

    public boolean isColorLocked() {
        return colorLocked;
    }

    public void lockColor() {
        this.colorLocked = true;
    }

    public void unlockColor() {
        this.colorLocked = false;
    }

    public boolean isPositionLocked() {
        return positionLocked;
    }

    public void lockPosition() {
        this.positionLocked = true;
    }

    public void unlockPosition() {
        this.positionLocked = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (!positionLocked) {
            this.x = x;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (!positionLocked) {
            this.y = y;
        }
    }

}
