package com.hideandseekapps.Notesolves;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.common.IntentSenderForResultStarter;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hideandseekapps.firebase_tut.R;

public class ForceUpdate {
    public static final int APP_UPDATE_REQUEST_CODE = 1001;
    static final String REMOTE_CONFIG_KEY = "newVersion";
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private AppUpdateManager appUpdateManager;
    Context context;

    public ForceUpdate(Context context) {
        this.context = context;
        appUpdateManager = AppUpdateManagerFactory.create(context);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        fetchRemoteConfigData();
    }

    public void fetchRemoteConfigData() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {

                    }
                });
    }

    public int getNewVersion(String key) {
        return (int) mFirebaseRemoteConfig.getLong(key);
    }

    public int getCurrentAppVersionCode() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean checkForUpdate() {
        int currentAppVersion = getCurrentAppVersionCode();
        int remoteAppVersion = getNewVersion(REMOTE_CONFIG_KEY);
        if (remoteAppVersion > currentAppVersion) {
            return true;
        } else return false;

    }

    public void requestAppUpdate(ActivityResultLauncher activityResultLauncher, AppUpdateView appUpdateView) {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if (!(appUpdateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE)){
                appUpdateView.updateNotAvailable();
                return;
            }

            else if ( (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                ||
            appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                try {
                    IntentSenderForResultStarter starter = new IntentSenderForResultStarter() {
                        @Override
                        public void startIntentSenderForResult(@NonNull IntentSender intentSender, int i, @Nullable Intent intent, int i1, int i2, int i3, @Nullable Bundle bundle) throws IntentSender.SendIntentException {
                            IntentSenderRequest req = new IntentSenderRequest.Builder(intentSender)
                                    .setFillInIntent(intent)
                                    .setFlags(i2, i1)
                                    .build();

                            activityResultLauncher.launch(req);


                        }
                    };
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            starter,
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                            APP_UPDATE_REQUEST_CODE
                    );
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }




}
