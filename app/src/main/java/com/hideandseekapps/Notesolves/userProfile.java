package com.hideandseekapps.Notesolves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hideandseekapps.firebase_tut.R;
import com.hideandseekapps.firebase_tut.databinding.ActivityMainBinding;
import com.hideandseekapps.firebase_tut.databinding.ActivityUserProfileBinding;
import com.hideandseekapps.firebase_tut.databinding.ActivityWebPageBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

public class userProfile extends AppCompatActivity {


    String uid,name,email,phn,cllg;
    DatabaseReference databaseReference;


    ActivityUserProfileBinding userProfileBinding;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(userProfileBinding.getRoot());

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        uid = getIntent().getStringExtra("uid");
        setInfo(uid);

        userProfileBinding.vCode.setText("v"+getAppVersionName(this));
    }

    private void setInfo(String uid) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model obj = snapshot.getValue(Model.class);
                if (obj != null){
                    name = obj.name;
                    email = obj.email;
                    phn = obj.phone;
                    cllg = obj.college;

                    userProfileBinding.userName.setText(name);
                    userProfileBinding.userEmail.setText(email);
                    userProfileBinding.userPhone.setText(phn);
                    userProfileBinding.userCollege.setText(cllg);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userProfile.this, "Something Went Wrong\n"+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle1 = new Bundle();
        bundle1.putString(GAManager.activity_name,"UserProfile");
        GAManager.logEvent(this,GAManager.open_screen,bundle1);
    }
}