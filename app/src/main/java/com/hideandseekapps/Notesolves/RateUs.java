package com.hideandseekapps.Notesolves;

import static android.content.Context.MODE_PRIVATE;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Timestamp;

public class RateUs {

    Context c;
    Boolean rateUsFlag;
    Boolean remindLaterFlag;
    Boolean neverShowFlag;
    private static final String PREF_NAME = "NoteSolves";
    private static final String RATE_US_FLAG = "rateUsFlag";
    private static final String REMIND_LATER_FLAG = "remindLaterFlag";
    private static final String NEVER_SHOW_FLAG = "neverShowFlag";
    private static final String LAST_POPUP_TIMESTAMP = "lastPopupTimestamp";

    long installDate;
    long lastPopUpTimeStamp;
    boolean isFirstRun;
    SharedPreferences preferences = null;

//    public RateUs(Boolean rateNow, Boolean remindLater, Boolean noThanks, Timestamp timestamp) {
//        this.rateNow = rateNow;
//        this.remindLater = remindLater;
//        this.noThanks = noThanks;
//        this.timestamp = timestamp;
//    }


    public RateUs(Context c) {
        this.c = c;
    }

    public int isFirstRun(Context c) {
        preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            // This is the first run; record the first run timestamp
            installDate  = System.currentTimeMillis();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("installDate", installDate).commit();
            editor.putBoolean("isFirstRun", false).commit();
            editor.apply();
            return 1;
        }
        else {
            // Check if two or more weeks have passed
            installDate = preferences.getLong("installDate", 0);
            long currentDate = System.currentTimeMillis();
            long weeksPassed = (installDate - currentDate) / (7 * 24 * 60 * 60 * 1000);
            if (weeksPassed >= 2) {
                int result = flagsAlreadyStored(c);
                boolean decide = decidePopUp(result);
                if (decide) {
                    showPopUp();
                    return 2;
                }
            }
            return 0;
        }
    }

    private int flagsAlreadyStored(Context c) {
        preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        rateUsFlag = preferences.getBoolean(RATE_US_FLAG,false);
        neverShowFlag = preferences.getBoolean(NEVER_SHOW_FLAG,false);
        remindLaterFlag = preferences.getBoolean(REMIND_LATER_FLAG,false);
        if (rateUsFlag) return 1;
        else if (remindLaterFlag) return 2;
        else if (neverShowFlag) return 3;
        return 0;
    }

    private boolean decidePopUp(int x) {
        preferences = c.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        long currentDate = System.currentTimeMillis();
        lastPopUpTimeStamp = preferences.getLong(LAST_POPUP_TIMESTAMP, 0);
        long weeksPassed = (lastPopUpTimeStamp - currentDate) / (7 * 24 * 60 * 60 * 1000);
        if (x==0){
            return true;
        }
        else if(x==3){
            return false;
        }
        else if(x==2){
            if (weeksPassed>=3) return true;
        }
        else if (x==1){
            if (weeksPassed>=2) return true;
        }
        return false;
    }

    private void showPopUp() {
        preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(RATE_US_FLAG, false);
        editor.putBoolean(REMIND_LATER_FLAG, false);
        editor.putBoolean(NEVER_SHOW_FLAG, false);
    }


    public void saveRateUsFlagAndTimestamp() {
        preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(RATE_US_FLAG, true);
        editor.putLong(LAST_POPUP_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    public void saveRemindLaterFlag() {
        preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(REMIND_LATER_FLAG, true);
        editor.putLong(LAST_POPUP_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    public void saveNeverShowFlag() {
        preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(NEVER_SHOW_FLAG, true);
        editor.putLong(LAST_POPUP_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    public void openAppRating() {
        try {
            // Create an intent to open the Play Store rating page for the app
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.hideandseekapps.firebase_tut&hl=en_US");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // Ensure that the Play Store is installed on the device
            if (intent.resolveActivity(c.getPackageManager()) != null) {
                c.startActivity(intent);
            } else {
                // If the Play Store is not installed, open the Play Store website
                uri = Uri.parse("https://play.google.com/store/apps/details?id=com.hideandseekapps.firebase_tut&hl=en_US");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                c.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            // Handle the exception as needed
            e.printStackTrace();
        }
    }

}





