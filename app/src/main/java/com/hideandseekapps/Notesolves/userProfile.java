package com.hideandseekapps.Notesolves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class userProfile extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    String uid,name,email,phn,cllg;
    DatabaseReference databaseReference;


    @BindView(R.id.userName)
    TextView nametv;
    @BindView(R.id.userEmail)
    TextView emailtv;
    @BindView(R.id.userPhone)
    TextView phntv;
    @BindView(R.id.userCollege)
    TextView cllgtv;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        uid = getIntent().getStringExtra("uid");
        setInfo(uid);

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

                    nametv.setText(name);
                    emailtv.setText(email);
                    phntv.setText(phn);
                    cllgtv.setText(cllg);
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

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle1 = new Bundle();
        bundle1.putString(GAManager.activity_name,"UserProfile");
        GAManager.logEvent(this,GAManager.open_screen,bundle1);
    }
}