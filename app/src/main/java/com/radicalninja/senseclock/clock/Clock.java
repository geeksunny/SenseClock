package com.radicalninja.senseclock.clock;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.eon.androidthings.sensehatdriverlibrary.SenseHat;
import com.radicalninja.senseclock.hw.LedRenderer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Clock {

    public static Clock init(final Context context, final ClockConfig config) throws IOException {
        if (null != instance) {
            Log.w(TAG, "Clock instance was already initialized.");
            return instance;
        }
        instance = new Clock(context, config);
        return instance;
    }

    public static Clock getInstance() {
        if (null == instance) {
            throw new IllegalStateException("Clock has not been initialized.");
        }
        return instance;
    }

    private static final String TAG = Clock.class.getCanonicalName();

    private static Clock instance;

    private final DateFormat dateFormat = new SimpleDateFormat("hhmmss", Locale.US);
    private final Handler handler = new Handler();

    private final ClockConfig clockConfig;
    private final SenseHat senseHat;
    private final LedRenderer ledRenderer;

    private final TimePiece timeHours, timeMinutes, timeSeconds;

    private Clock(final Context context, final ClockConfig config) throws IOException {
        final SensorManager sensorManager =
                (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.senseHat = SenseHat.init(sensorManager);
        this.ledRenderer = new LedRenderer(senseHat.getLedMatrix());
        this.clockConfig = config;

        // Set initial configuration
        this.ledRenderer.setRotations(clockConfig.getRotations());

        // TODO: Revisit. this didn't seem to have an effect on first try.
//        final String timezoneId = clockConfig.getTimezone();
//        if (!TextUtils.isEmpty(timezoneId)) {
//            TimeZone.setDefault(TimeZone.getTimeZone(timezoneId));
//        }

        switch (clockConfig.getOrientation()) {
            case HORIZONTAL:
                timeHours = new CompoundTimePiece.CompoundHorizontalTimePiece(29);
                timeMinutes = new CompoundTimePiece.CompoundHorizontalTimePiece(59);
                timeSeconds = new CompoundTimePiece.CompoundHorizontalTimePiece(59);
                this.ledRenderer.addPixelGroup(timeHours, 0, 0);
                this.ledRenderer.addPixelGroup(timeMinutes, 0, 3);
                this.ledRenderer.addPixelGroup(timeSeconds, 0, 6);
                break;
            case VERTICAL:
            default:
                timeHours = new CompoundTimePiece.CompoundVerticalTimePiece(29);
                timeMinutes = new CompoundTimePiece.CompoundVerticalTimePiece(59);
                timeSeconds = new CompoundTimePiece.CompoundVerticalTimePiece(59);
                this.ledRenderer.addPixelGroup(timeHours, 0, 1);
                this.ledRenderer.addPixelGroup(timeMinutes, 3, 1);
                this.ledRenderer.addPixelGroup(timeSeconds, 6, 1);
        }

        final ClockColor hourColor = clockConfig.getColorHour();
        if (null != hourColor) {
            Log.d(TAG, String.format("Active: %s | Inactive: %s", Integer.toHexString(hourColor.getColorActive()), Integer.toHexString(hourColor.getColorInactive())));
            timeHours.setColors(hourColor.getColorActive(), hourColor.getColorInactive());
        }
        final ClockColor minuteColor = clockConfig.getColorMinute();
        if (null != minuteColor) {
            Log.d(TAG, String.format("Active: %s | Inactive: %s", Integer.toHexString(minuteColor.getColorActive()), Integer.toHexString(minuteColor.getColorInactive())));
            timeMinutes.setColors(minuteColor.getColorActive(), minuteColor.getColorInactive());
        }
        final ClockColor secondColor = clockConfig.getColorSecond();
        if (null != secondColor) {
            Log.d(TAG, String.format("Active: %s | Inactive: %s", Integer.toHexString(secondColor.getColorActive()), Integer.toHexString(secondColor.getColorInactive())));
            timeSeconds.setColors(secondColor.getColorActive(), secondColor.getColorInactive());
        }
    }

    public void start() {
        final Runnable updateTask = new Runnable() {
            @Override
            public void run() {
                update();
                handler.postDelayed(this, 1000);
            }
        };
        update();
        // TODO: start task at next immediate whole-second in the time. check system timer for this scheduling
        handler.postDelayed(updateTask, 1000);
    }

    public void update() {
        final String time = dateFormat.format(new Date());
        timeHours.setValue(time.substring(0, 2));
        timeMinutes.setValue(time.substring(2, 4));
        timeSeconds.setValue(time.substring(4, 6));
        try {
            this.ledRenderer.render(false);
        } catch (final IOException e) {
            // TODO: revisit messaging
            Log.e(TAG, "Encountered an error during LedRenderer.render(false)", e);
        }
    }

}
