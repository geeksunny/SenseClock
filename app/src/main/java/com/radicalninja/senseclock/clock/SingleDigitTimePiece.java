package com.radicalninja.senseclock.clock;

import android.util.Log;

import com.radicalninja.senseclock.hw.Pixel;

import androidx.annotation.IntRange;

public abstract class SingleDigitTimePiece extends TimePiece {

    private static final String TAG = SingleDigitTimePiece.class.getCanonicalName();

    public SingleDigitTimePiece(@IntRange(from = 1, to = 9) final int displayValue) {
        super(displayValue);
    }

    @Override
    int minValue() {
        return 1;
    }

    @Override
    int maxValue() {
        return 9;
    }

    abstract void setOffset(final Pixel pixel, final int offset);

    public void setOffset(final int offset) {
        for (final Pixel pixel : getPixels()) {
            setOffset(pixel, offset);
        }
    }

    @Override
    protected Pixel[] initPixels(final int displayValue) {
        final Pixel[] pixels = super.initPixels(displayValue);
        // TODO: if displayValue is less than 9, set leading pixels to PixelGroup.COLOR_EMPTY - lock color
        Log.d(TAG, "initPixels("+displayValue+")");
        if (displayValue < 8) {
            Log.d(TAG, displayValue+" less than 8");
            pixels[0].setColor(Pixel.COLOR_EMPTY);
            pixels[0].lockColor();
            if (displayValue < 4) {
                Log.d(TAG, displayValue+" less than 4");
                pixels[1].setColor(Pixel.COLOR_EMPTY);
                pixels[1].lockColor();
                if (displayValue < 2) {
                    Log.d(TAG, displayValue+" less than 2");
                    pixels[2].setColor(Pixel.COLOR_EMPTY);
                    pixels[2].lockColor();
                }
            }
        }
        // TODO....... should this be moved into SingleDigitTimePiece?
        return pixels;
    }

    @Override
    public void updatePixels(final Pixel[] pixels, final int value) {
        pixels[0].setColor(((0B1000 & value) == 0) ? colorInactive : colorActive);
        pixels[1].setColor(((0B0100 & value) == 0) ? colorInactive : colorActive);
        pixels[2].setColor(((0B0010 & value) == 0) ? colorInactive : colorActive);
        pixels[3].setColor(((0B0001 & value) == 0) ? colorInactive : colorActive);
    }

    /**
     * TimePiece represented as a horizontal line.
     */
    public static class HorizontalTimePiece extends SingleDigitTimePiece {

        public HorizontalTimePiece(final int displayValue) {
            super(displayValue);
        }

        @Override
        Pixel[] createPixels() {
            final Pixel[] pixels = new Pixel[4];
            for (int i = 0; i < 4; i++) {
                pixels[i] = new Pixel((i * 2), 0);
            }
            return pixels;
        }

        @Override
        void setOffset(final Pixel pixel, final int offset) {
            pixel.setY(offset);
        }

    }

    /**
     * TimePiece represented as a vertical line.
     */
    public static class VerticalTimePiece extends SingleDigitTimePiece {

        public VerticalTimePiece(final int displayValue) {
            super(displayValue);
        }

        @Override
        Pixel[] createPixels() {
            final Pixel[] pixels = new Pixel[4];
            for (int i = 0; i < 4; i++) {
                pixels[i] = new Pixel(0, (i * 2));
            }
            return pixels;
        }

        @Override
        void setOffset(final Pixel pixel, final int offset) {
            pixel.setX(offset);
        }

    }

}
