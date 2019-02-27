package com.radicalninja.senseclock.hw;

import android.graphics.Color;
import android.util.Log;

import com.eon.androidthings.sensehatdriverlibrary.devices.LedMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class LedRenderer {

    private static final String TAG = LedRenderer.class.getCanonicalName();

    private final LedMatrix ledMatrix;
    private final List<PixelGroup> pixelGroups = new ArrayList<>();

    public LedRenderer(@NonNull final LedMatrix ledMatrix) {
        if (null == ledMatrix) {
            throw new IllegalArgumentException("LED Matrix must not be null.");
        }
        this.ledMatrix = ledMatrix;
        try {
            ledMatrix.draw(Color.TRANSPARENT);
        } catch (IOException e) {
            Log.e(TAG, "Encountered error during initial LED Matrix blanking.", e);
        }
    }

    public void addPixelGroup(@NonNull final PixelGroup pixelGroup,
                              @IntRange(from = 0, to = 7) final int startX,
                              @IntRange(from = 0, to = 7) final int startY) {

        if (null == pixelGroup) {
            throw new IllegalArgumentException("PixelGroup must not be null.");
        } else if ((startX < 0 || startX > 7) || (startY < 0 || startY > 7)) {
            throw new IllegalArgumentException("Starting position must be within (0-7,0-7)");
        }
        // TODO: Use startX/Y here...
        if (!pixelGroups.contains(pixelGroup)) {
            for (final Pixel pixel : pixelGroup.getPixels()) {
                pixel.setX(startX + pixel.getX());
                pixel.setY(startY + pixel.getY());
            }
            pixelGroups.add(pixelGroup);
        }
    }

    public void render(final boolean forceRefresh) throws IOException {
        for (final PixelGroup pixelGroup : pixelGroups) {
            if (pixelGroup.needsRefresh() || forceRefresh) {
                // TODO: Pixel group "offsets"
                final Pixel[] pixels = pixelGroup.getPixels();
                for (final Pixel pixel : pixels) {
                    if (pixel.getColor() == Pixel.COLOR_EMPTY) {
//                        Log.d(TAG, "Skipping pixel at ("+pixel.getX()+","+pixel.getY()+")");
                        continue;
                    }
                    // TODO: loop logic to build stacks of Points paired with each unique color
//                    Log.d(TAG, String.format("Queuing color [%d] to [%d, %d]", pixel.getColor(), pixel.getX(), pixel.getY()));
                    ledMatrix.queue(pixel.getColor(), pixel.getX(), pixel.getY());
                }
//                Log.d(TAG, "Drawing queued buffer to LED matrix.");
                ledMatrix.drawBuffer();
            }
        }
    }

    public void setRotations(final int rotations) {
        this.ledMatrix.setRotation(rotations);
    }

}
