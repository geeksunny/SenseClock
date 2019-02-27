package com.radicalninja.senseclock.clock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

import androidx.annotation.Nullable;

public class ClockConfig {

    public static final String TAG = ClockConfig.class.getCanonicalName();

    public enum Orientation {
        @SerializedName("horizontal")
        HORIZONTAL,
        @SerializedName("vertical")
        VERTICAL
    }

    public interface OnValueChangedListener {
        void on24HourModeChanged(final boolean is24HourMode);
        void onOrientationChanged(final Orientation orientation);
        void onRotationsChanged(final int rotations);
        void onTimezoneChanged(final String timezone);
        void onColorHourChanged(final ClockColor hourColor);
        void onColorMinuteChanged(final ClockColor minuteColor);
        void onColorSecondChanged(final ClockColor secondColor);
    }

    private boolean is24HourMode;
    private Orientation orientation;
    private int rotations;
    private String timezone;
    private Colors colors;

    private OnValueChangedListener listener;

    public ClockConfig() {
        //
    }

    public void setOnValueChangedListener(final OnValueChangedListener listener) {
        this.listener = listener;
    }

    public void clearOnValueChangedListener() {
        this.listener = null;
    }

    public boolean is24HourMode() {
        return is24HourMode;
    }

    public void set24HourMode(final boolean is24HourMode) {
        this.is24HourMode = is24HourMode;
        if (null != listener) {
            listener.on24HourModeChanged(is24HourMode);
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(final Orientation orientation) {
        this.orientation = orientation;
        if (null != listener) {
            listener.onOrientationChanged(orientation);
        }
    }

    public int getRotations() {
        return rotations;
    }

    public void setRotations(final int rotations) {
        this.rotations = rotations;
        if (null != listener) {
            listener.onRotationsChanged(rotations);
        }
    }

    @Nullable
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    @Nullable
    public ClockColor getColorHour() {
        return (null != colors) ? colors.hour : null;
    }

    public void setColorHour(final ClockColor hourColor) {
        if (null == colors) {
            colors = new Colors();
        }
        colors.hour = hourColor;
        if (null != listener) {
            listener.onColorHourChanged(hourColor);
        }
    }

    @Nullable
    public ClockColor getColorMinute() {
        return (null != colors) ? colors.minute : null;
    }

    public void setColorMinute(final ClockColor minuteColor) {
        if (null == colors) {
            colors = new Colors();
        }
        colors.minute = minuteColor;
        if (null != listener) {
            listener.onColorMinuteChanged(minuteColor);
        }
    }

    @Nullable
    public ClockColor getColorSecond() {
        return (null != colors) ? colors.second : null;
    }

    public void setColorSecond(final ClockColor secondColor) {
        if (null == colors) {
            colors = new Colors();
        }
        colors.second = secondColor;
        if (null != listener) {
            listener.onColorSecondChanged(secondColor);
        }
    }

    public static class Colors {
        private ClockColor hour;
        private ClockColor minute;
        private ClockColor second;
    }

    public static class Adapter
            implements JsonDeserializer<ClockConfig>, JsonSerializer<ClockConfig> {

        private static final String KEY_24_HOUR = "24hour";
        private static final String KEY_ORIENTATION = "orientation";
        private static final String KEY_ROTATIONS = "rotations";
        private static final String KEY_TIMEZONE = "timezone";
        private static final String KEY_COLORS = "colors";
        private static final String KEY_COLORS_HOUR = "hour";
        private static final String KEY_COLORS_MINUTE = "minute";
        private static final String KEY_COLORS_SECOND = "second";

        @Override
        public ClockConfig deserialize(final JsonElement json,
                                       final Type typeOfT,
                                       final JsonDeserializationContext context)
                throws JsonParseException {

            final JsonObject jsonObject = json.getAsJsonObject();
            final ClockConfig result = new ClockConfig();
            // TODO: Try / catch
            // 24 hour mode
            if (jsonObject.has(KEY_24_HOUR)) {
                final JsonElement json24Hour = jsonObject.get(KEY_24_HOUR);
                if (json24Hour.isJsonPrimitive()) {
                    result.is24HourMode = json24Hour.getAsBoolean();
                }
            }
            // Orientation
            if (jsonObject.has(KEY_ORIENTATION)) {
                final JsonElement jsonOrientation = jsonObject.get(KEY_ORIENTATION);
                result.orientation = context.deserialize(jsonOrientation, Orientation.class);
            }
            // Rotations
            if (jsonObject.has(KEY_ROTATIONS)) {
                final JsonElement jsonRotations = jsonObject.get(KEY_ROTATIONS);
                if (jsonRotations.isJsonPrimitive()) {
                    result.rotations = jsonRotations.getAsInt();
                }
            }
            // Timezone
            if (jsonObject.has(KEY_TIMEZONE)) {
                final JsonElement jsonTimezone = jsonObject.get(KEY_TIMEZONE);
                if (jsonTimezone.isJsonPrimitive()) {
                    result.timezone = jsonTimezone.getAsString();
                }
            }
            // Colors
            if (jsonObject.has(KEY_COLORS)) {
                final Colors colors = new Colors();
                final JsonObject colorsJson = jsonObject.getAsJsonObject(KEY_COLORS);
                final ClockColors clockColors = ClockColors.getInstance();
                if (colorsJson.has(KEY_COLORS_HOUR)) {
                    final String hourName = colorsJson.get(KEY_COLORS_HOUR).getAsString();
                    colors.hour = clockColors.get(hourName);
                }
                if (colorsJson.has(KEY_COLORS_MINUTE)) {
                    final String minuteName = colorsJson.get(KEY_COLORS_MINUTE).getAsString();
                    colors.minute = clockColors.get(minuteName);
                }
                if (colorsJson.has(KEY_COLORS_SECOND)) {
                    final String secondName = colorsJson.get(KEY_COLORS_SECOND).getAsString();
                    colors.second = clockColors.get(secondName);
                }
                result.colors = colors;
            }
            return result;
        }

        @Override
        public JsonElement serialize(final ClockConfig src,
                                     final Type typeOfSrc,
                                     final JsonSerializationContext context) {

            final JsonObject result = new JsonObject();
            // 24 hour mode
            result.addProperty(KEY_24_HOUR, src.is24HourMode);
            // Orientation
            result.add(KEY_ORIENTATION, context.serialize(src.orientation));
            // Rotations
            result.addProperty(KEY_ROTATIONS, src.rotations);
            // Timezone
            result.addProperty(KEY_TIMEZONE, src.timezone);
            // Colors
            final Colors colors = src.colors;
            if (null != colors) {
                final JsonObject colorsJson = new JsonObject();
                if (null != colors.hour) {
                    colorsJson.addProperty(KEY_COLORS_HOUR, colors.hour.getName());
                }
                if (null != colors.minute) {
                    colorsJson.addProperty(KEY_COLORS_MINUTE, colors.minute.getName());
                }
                if (null != colors.second) {
                    colorsJson.addProperty(KEY_COLORS_SECOND, colors.second.getName());
                }
            }
            return result;
        }
    }

}
