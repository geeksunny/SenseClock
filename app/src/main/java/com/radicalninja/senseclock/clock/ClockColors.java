package com.radicalninja.senseclock.clock;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.radicalninja.senseclock.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ClockColors {

    public static final String FILENAME = "raw/colors.json";

    public static ClockColors init(final Resources resources, final Gson gson) {
        if (null != instance) {
            // TODO: log already initialized message?
            return instance;
        }
        final InputStream inputStream = resources.openRawResource(R.raw.colors);
        instance = gson.fromJson(new InputStreamReader(inputStream), ClockColors.class);
        return instance;
    }

    public static ClockColors getInstance() {
        if (null == instance) {
            // TODO: throw exception
        }
        return instance;
    }

    private static ClockColors instance;

    private final Map<String, ClockColor> colors = new HashMap<>();

    public ClockColor get(final String colorName) {
        return colors.get(colorName);
    }

    public boolean has(final String colorName) {
        return colors.containsKey(colorName);
    }

    public static class Adapter
            implements JsonDeserializer<ClockColors>, JsonSerializer<ClockColors> {

        private static final String KEY_ACTIVE = "active";
        private static final String KEY_INACTIVE = "inactive";

        @Override
        public ClockColors deserialize(final JsonElement json,
                                       final Type typeOfT,
                                       final JsonDeserializationContext context)
                throws JsonParseException {

            if (!json.isJsonObject()) {
                // TODO: throw exception?
            }
            final ClockColors result = new ClockColors();
            final JsonObject jsonObject = json.getAsJsonObject();
            for (final String colorName : jsonObject.keySet()) {
                final JsonObject colorJson = jsonObject.getAsJsonObject(colorName);
                if (!colorJson.has(KEY_ACTIVE) || !colorJson.has(KEY_INACTIVE)) {
                    // TODO: log error / warning?
                    continue;
                }
                final String colorActiveString = colorJson.get(KEY_ACTIVE).getAsString();
                final String colorInactiveString = colorJson.get(KEY_INACTIVE).getAsString();
                final ClockColor clockColor =
                        new ClockColor(colorName, colorActiveString, colorInactiveString);
                result.colors.put(colorName, clockColor);
            }
            return result;
        }

        @Override
        public JsonElement serialize(final ClockColors src,
                                     final Type typeOfSrc,
                                     final JsonSerializationContext context) {

            final JsonObject result = new JsonObject();
            for (final ClockColor clockColor : src.colors.values()) {
                final JsonElement colorJson = context.serialize(clockColor, ClockColor.class);
                result.add(clockColor.getName(), colorJson);
            }
            return result;
        }
    }

}
