package com.radicalninja.senseclock.clock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import androidx.annotation.ColorInt;

public class ClockColor {

    private final String name;
    @ColorInt private final int colorActive;
    @ColorInt private final int colorInactive;

    public ClockColor(final String name,
                      @ColorInt final int colorActive,
                      @ColorInt final int colorInactive) {

        this.name = name;
        this.colorActive = colorActive;
        this.colorInactive = colorInactive;
    }

    public ClockColor(final String name, final String colorActiveHex, final String colorInactiveHex) {
        this.name = name;
        this.colorActive = (int) Long.parseLong(colorActiveHex, 16);
        this.colorInactive = (int) Long.parseLong(colorInactiveHex, 16);
    }

    public String getName() {
        return name;
    }

    @ColorInt
    public int getColorActive() {
        return colorActive;
    }

    public String getColorActiveHex() {
        return Integer.toHexString(colorActive);
    }

    @ColorInt
    public int getColorInactive() {
        return colorInactive;
    }

    public String getColorInactiveHex() {
        return Integer.toHexString(colorInactive);
    }

    public static class Adapter
            implements JsonDeserializer<ClockColor>, JsonSerializer<ClockColor> {

        private static final String KEY_ACTIVE = "active";
        private static final String KEY_INACTIVE = "inactive";

        @Override
        public ClockColor deserialize(final JsonElement json,
                                      final Type typeOfT,
                                      final JsonDeserializationContext context)
                throws JsonParseException {

            // TODO: Remove deserialize interface/method?
            return null;
        }

        @Override
        public JsonElement serialize(final ClockColor src,
                                     final Type typeOfSrc,
                                     final JsonSerializationContext context) {

            final JsonObject result = new JsonObject();
            result.addProperty(KEY_ACTIVE, src.getColorActiveHex());
            result.addProperty(KEY_INACTIVE, src.getColorInactiveHex());
            return result;
        }

    }

}
