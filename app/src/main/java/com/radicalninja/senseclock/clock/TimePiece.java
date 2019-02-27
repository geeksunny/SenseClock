package com.radicalninja.senseclock.clock;

import android.graphics.Color;

import com.radicalninja.senseclock.hw.Pixel;
import com.radicalninja.senseclock.hw.PixelGroup;

import androidx.annotation.ColorInt;

public abstract class TimePiece extends PixelGroup<String> {

    private final int displayValue;
    private final Pixel[] pixels;

    @ColorInt protected int colorActive = Color.CYAN;
    @ColorInt protected int colorInactive = Color.BLUE;

    public TimePiece(final int displayValue) {
        if (displayValue < this.minValue() || displayValue > this.maxValue()) {
            throw new IllegalArgumentException(String.format(
                    "Display value must be within %d-%d", this.minValue(), this.maxValue()));
        }
        this.displayValue = displayValue;
        this.pixels = initPixels(displayValue);
    }

    public int getDisplayValue() {
        return displayValue;
    }

    public void setColors(@ColorInt final int colorActive, @ColorInt final int colorInactive) {
        this.colorActive = colorActive;
        this.colorInactive = colorInactive;
    }

    protected Pixel[] initPixels(final int displayValue) {
        return this.createPixels();
    }

    @Override
    public void updatePixels(final Pixel[] pixels, final String value) {
        final int v = Integer.parseInt(value);
        updatePixels(pixels, v);
    }

    public abstract void updatePixels(final Pixel[] pixels, final int value);

    public abstract void setOffset(final int offset);

    abstract int minValue();
    abstract int maxValue();

    abstract Pixel[] createPixels();

    @Override
    public Pixel[] getPixels() {
        // TODO: use this.getValue() to set colors of pixels before return
        return this.pixels;
    }

}
