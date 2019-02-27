package com.radicalninja.senseclock.clock;

import com.radicalninja.senseclock.hw.Pixel;
import com.radicalninja.senseclock.util.ArrayUtils;

import androidx.annotation.IntRange;

public abstract class CompoundTimePiece<T extends TimePiece> extends TimePiece {

    private static final String TAG = CompoundTimePiece.class.getCanonicalName();

    private TimePiece primaryDigit;
    private TimePiece secondaryDigit;

    public CompoundTimePiece(@IntRange(from = 11, to = 59) final int displayValue) {
        super(displayValue);
    }

    abstract T createTimePiece(@IntRange(from = 1, to = 9) final int displayValue);

    @Override
    int minValue() {
        return 11;
    }

    @Override
    int maxValue() {
        return 59;
    }

    @Override
    public void setOffset(final int offset) {
        primaryDigit.setOffset(offset);
        secondaryDigit.setOffset(offset + 1);
    }

    @Override
    Pixel[] createPixels() {
        final int displayValue = getDisplayValue();
        primaryDigit = createTimePiece(displayValue / 10);
        final Pixel[] primaryPixels = primaryDigit.getPixels();
        secondaryDigit = createTimePiece(displayValue % 10);
        secondaryDigit.setOffset(1);
        final Pixel[] secondaryPixels = secondaryDigit.getPixels();
        return ArrayUtils.combine(primaryPixels, secondaryPixels);
    }

    @Override
    public void setColors(final int colorActive, final int colorInactive) {
        //super.setColors(colorActive, colorInactive);
        this.primaryDigit.setColors(colorActive, colorInactive);
        this.secondaryDigit.setColors(colorActive, colorInactive);
    }

    @Override
    public void setValue(final String value) {
        super.setValue(value);
        if (value.length() == 1) {
            primaryDigit.setValue("0");
            secondaryDigit.setValue(value);
        } else {
            primaryDigit.setValue(value.substring(0, 1));
            secondaryDigit.setValue(value.substring(1, 2));
        }
    }

    @Override
    public void updatePixels(final Pixel[] pixels, final int value) {
        // TODO: is anything necessary here?
    }

    public static class CompoundHorizontalTimePiece
            extends CompoundTimePiece<SingleDigitTimePiece.HorizontalTimePiece> {

        public CompoundHorizontalTimePiece(final int maxValue) {
            super(maxValue);
        }

        @Override
        SingleDigitTimePiece.HorizontalTimePiece createTimePiece(
                @IntRange(from = 1, to = 9) final int displayValue) {

            return new SingleDigitTimePiece.HorizontalTimePiece(displayValue);
        }

    }

    public static class CompoundVerticalTimePiece
            extends CompoundTimePiece<SingleDigitTimePiece.VerticalTimePiece> {

        public CompoundVerticalTimePiece(final int maxValue) {
            super(maxValue);
        }

        @Override
        SingleDigitTimePiece.VerticalTimePiece createTimePiece(
                @IntRange(from = 1, to = 9) final int displayValue) {

            return new SingleDigitTimePiece.VerticalTimePiece(displayValue);
        }

    }

}
