package com.hideandseekapps.Notesolves;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hideandseekapps.firebase_tut.R;

public class ScreenShotDisableConfig {
    static final String REMOTE_CONFIG_KEY = "disable_screenshots";
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    Context context;

    public ScreenShotDisableConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        fetchRemoteConfigData();
    }

    public void fetchRemoteConfigData() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {
                        Log.d("screenShotDisable", "dataNotFetched");
                    }
                });
    }


    public boolean getIsScreenShotDisable() {
        return mFirebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY);
    }
}
