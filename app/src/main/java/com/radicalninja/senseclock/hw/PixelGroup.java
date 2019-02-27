package com.radicalninja.senseclock.hw;

public abstract class PixelGroup<T> {

    private boolean needsRefresh = true;

    private T value;

    public PixelGroup() {
        //
    }

    public abstract Pixel[] getPixels();

    public abstract void updatePixels(final Pixel[] pixels, final T value);

    boolean needsRefresh() {
        return needsRefresh;
    }

    public void setValue(final T value) {
        if (null == this.value || !this.value.equals(value)) {
            this.value = value;
            this.needsRefresh = true;
            updatePixels(getPixels(), value);
        }
    }

    public T getValue() {
        return value;
    }

}
