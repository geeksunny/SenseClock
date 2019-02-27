package com.radicalninja.senseclock;

import android.app.Application;
import android.util.Log;

import com.radicalninja.senseclock.clock.Clock;
import com.radicalninja.senseclock.clock.ClockConfig;

import java.io.IOException;

public class App extends Application {

    private static final String TAG = App.class.getCanonicalName();

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    private Clock clock;
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // TODO: Would it make more sense to move this to MainActivity and eliminate App class?
        preferences = new Preferences(this);
        final ClockConfig config = preferences.readConfig();
        try {
            clock = Clock.init(this, config);
            clock.start();
        } catch (IOException e) {
            Log.e(TAG, "Encountered an error initializing the Clock.", e);
        }
    }

}
