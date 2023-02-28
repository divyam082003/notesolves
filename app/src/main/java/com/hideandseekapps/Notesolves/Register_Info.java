package com.hideandseekapps.Notesolves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hideandseekapps.firebase_tut.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class Register_Info extends AppCompatActivity {

    DatabaseReference databaseReference;
    String name,phone,college ;
    ActionBar actionBar;

    //Register Info
    @BindViews({R.id.register_name,R.id.register_phn,R.id.register_college}) List<EditText> register_info;
    @BindViews({R.id.name,R.id.phn,R.id.collegeHead}) List<TextInputLayout> register_info_layouts;

    @BindView(R.id.info_submit) Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Hello "+name);
        actionBar.setDisplayHomeAsUpEnabled(true);


        String email   = (String) getIntent().getStringExtra("email");
        String uid   = (String) getIntent().getStringExtra("uid");

        List <String> userinfo = getinfo(uid);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = register_info.get(0).getText().toString();
                String phn1 = register_info.get(1).getText().toString();
                String college1 = register_info.get(2).getText().toString();
                checkData(uid,email,name1,phn1,college1,userinfo);
            }
        });

    }

    private List<String> getinfo(String uid) {
        List <String> UserInfo = new ArrayList<String>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model obj = snapshot.getValue(Model.class);
                if (obj != null){
                    name = obj.name;
                    phone = obj.phone;
                    college = obj.college;
                    register_info.get(0).setText(name);
                    register_info.get(1).setText(phone);
                    register_info.get(2).setText(college);
                    actionBar.setTitle("Hello "+name);
                    UserInfo.add(name);
                    UserInfo.add(phone);
                    UserInfo.add(college);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG1", "onCancelled:DATA NOT Available");
            }
        });
        return UserInfo;
    }

    void checkData(String uid,String email,String name1,String phn1,String college,List <String> userinfo ){
        if(TextUtils.isEmpty(name1) || TextUtils.isEmpty(phn1) || TextUtils.isEmpty(college)){
            Toast.makeText(this, "No Data Saved\nKindly Fill All fields", Toast.LENGTH_SHORT).show();
        }
        else {
            if (phn1.length() >= 10 && Patterns.PHONE.matcher(phn1).matches()){
                addData(uid,name1,phn1,college,email);
            }
            else {
                register_info_layouts.get(1).setError("Enter Valid Contact");
            }
        }
        keyboar_close(submit);
    }


    void addData(String uid,String name,String phn,String college,String email){
            HashMap <String,Object> m = new HashMap <String,Object> ();
            m.put("name",name);
            m.put("phone",phn);
            m.put("email",email);
            m.put("college",college);
            m.put("active",true);
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(uid)
                    .setValue(m)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Register_Info.this, "Data Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register_Info.this, "Sorry\n"+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    void keyboar_close(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}