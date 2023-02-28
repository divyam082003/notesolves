package com.hideandseekapps.Notesolves;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hideandseekapps.firebase_tut.R;

public class disclaimer extends AppCompatActivity {
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        actionBar = getSupportActionBar();
        actionBar.hide();
    }
}