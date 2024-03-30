package com.hideandseekapps.Notesolves;

import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hideandseekapps.firebase_tut.R;

public class FirebaseRemoteConfigManager {

    static final String REMOTE_CONFIG_SCREENSHOT_KEY = "disable_screenshots";

    private static FirebaseRemoteConfigManager instance;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private FirebaseRemoteConfigManager(Context context) {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // Example: Fetch every hour
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
    }

    public static synchronized FirebaseRemoteConfigManager getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseRemoteConfigManager(context);
        }
        return instance;
    }

    public void fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        // Log.d(TAG, "Config params updated: " + updated);
                    } else {
                        // Log.w(TAG, "Fetch failed");
                    }
                });
    }

    public boolean getBoolean(String key) {
        return mFirebaseRemoteConfig.getBoolean(key);
    }



}
