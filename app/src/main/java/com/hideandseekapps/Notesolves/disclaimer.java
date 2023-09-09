package com.hideandseekapps.Notesolves;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hideandseekapps.firebase_tut.R;

public class disclaimer extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        actionBar = getSupportActionBar();
        actionBar.hide();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle1 = new Bundle();
        bundle1.putString(GAManager.activity_name,"disclaimer");
        GAManager.logEvent(this,GAManager.open_screen,bundle1);
    }
}