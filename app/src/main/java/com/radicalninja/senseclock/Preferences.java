package com.radicalninja.senseclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radicalninja.senseclock.clock.ClockColor;
import com.radicalninja.senseclock.clock.ClockColors;
import com.radicalninja.senseclock.clock.ClockConfig;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Preferences {

    private static final String PREFS_FILE = "ClockPrefs";

    private static final String KEY_CLOCK_CONFIG_JSON = "ClockConfigJson";

    private final Gson gson;
    private final Resources resources;
    private final SharedPreferences prefs;

    Preferences(final Context context) {
        gson = initGson();
        resources = context.getResources();

        Log.d("GSON", gson.toJson(ClockConfig.Orientation.HORIZONTAL, ClockConfig.Orientation.class));

        ClockColors.init(resources, gson);
        prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    private Gson initGson() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ClockConfig.class, new ClockConfig.Adapter());
        gsonBuilder.registerTypeAdapter(ClockColor.class, new ClockColor.Adapter());
        gsonBuilder.registerTypeAdapter(ClockColors.class, new ClockColors.Adapter());
        return gsonBuilder.create();
    }

    public ClockConfig readConfig() {
        final String configJson = prefs.getString(KEY_CLOCK_CONFIG_JSON, null);
        if (TextUtils.isEmpty(configJson)) {
            final InputStream inputStream = resources.openRawResource(R.raw.default_config);
            return gson.fromJson(new InputStreamReader(inputStream), ClockConfig.class);
        } else {
            return gson.fromJson(configJson, ClockConfig.class);
        }
    }

    public void saveConfig(final ClockConfig config) {
        prefs.edit().putString(KEY_CLOCK_CONFIG_JSON, gson.toJson(config)).apply();
    }

}
