package com.hideandseekapps.Notesolves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.hideandseekapps.firebase_tut.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Boolean signup_visible,signin_visible,frgt_psswd_visible = true;
    private String registerEmail,registerPassword,loginEmail,loginPassword,forgotPasswordEmail,emailVerify,passwordVerify;
    private FirebaseAuth myauth;
    private DatabaseReference firebaseDatabase;


    //main activtity frames
    @BindViews({
            R.id.l0,    // back_btn
            R.id.l1,    // unused signUpIn
            R.id.l2,    // login
            R.id.l3,    // register
            R.id.l4     // resetPassword
    }) List<FrameLayout> layouts;


    @BindView(R.id.back_btn) ImageView back_btn;

    //register
    @BindView(R.id.register) TextView register;
    @BindViews({R.id.d0,R.id.d1}) List <TextInputLayout> register_laytouts;
    @BindViews({R.id.register_email,R.id.register_psswd}) List<EditText> registerInput;
    @BindView(R.id.register_btn) Button register_btn;

    //login
    @BindViews({R.id.d2,R.id.d3}) List <TextInputLayout> login_laytouts;
    @BindViews({R.id.login_email,R.id.login_psswd}) List<EditText> logininput;
    @BindView(R.id.login_btn) Button login_btn;

    //Verify Email
    @BindView(R.id.verify_email) TextView verify_email;

    //Forgot Password
    @BindView(R.id.frgt_psswd) TextView forgot_psswd;
    @BindView(R.id.frgt_psswd_email_head) TextInputLayout forgotPasswordEmailLayout;
    @BindView(R.id.frgt_psswd_email) EditText forgotPasswordEmailInput;
    @BindView(R.id.frgt_psswd_btn) Button forgot_psswd_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) getSupportActionBar().hide();


        myauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setRegister();
               keyboar_close(register);
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerEmail = registerInput.get(0).getText().toString();
                registerPassword = registerInput.get(1).getText().toString();
                String isCorrect = checkCred(registerEmail,registerPassword);
                checkCredAction(isCorrect,register_laytouts.get(0),register_laytouts.get(1),
                        (x, y)-> register(x,y),registerEmail,registerPassword);
                keyboar_close(register_btn);
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmail = logininput.get(0).getText().toString();
                loginPassword = logininput.get(1).getText().toString();

                String isCorrect = checkCred(loginEmail,loginPassword);
                checkCredAction(isCorrect,login_laytouts.get(0),login_laytouts.get(1),
                        (x, y)-> login(x,y),loginEmail,loginPassword);

                clear(logininput.get(1));
                keyboar_close(login_btn);
            }
        });

        forgot_psswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForgetpassword();
            }
        });

        forgot_psswd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordEmail = forgotPasswordEmailInput.getText().toString();
                String isCorrect = checkCred(forgotPasswordEmail);
                checkCredAction(isCorrect,forgotPasswordEmailLayout,forgotPasswordEmailLayout,
                        (x, y)-> resetPassword(x,y),forgotPasswordEmail,forgotPasswordEmail);

                clear(forgotPasswordEmailInput);
                keyboar_close(forgot_psswd_btn);
            }
        });

        verify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailVerify    = logininput.get(0).getText().toString();
                passwordVerify = logininput.get(1).getText().toString();
                String isCorrect = checkCred(emailVerify,passwordVerify);
                checkCredAction(isCorrect,login_laytouts.get(0),login_laytouts.get(1),
                        (x, y)-> emailVerify(x,y),emailVerify,passwordVerify);
                keyboar_close(verify_email);
            }
        });
    }

    String checkCred(String email,String password){
        String isCorrect;
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            return isCorrect="both";
        }
        else if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            return isCorrect="email";
        }
        else if (!TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            return isCorrect="password";
        }
        else {
            return isCorrect="ok";
        }
    }

    String checkCred(String email){
        String isCorrect;
        if (TextUtils.isEmpty(email)){
            return isCorrect="email";
        }
        else {
            return isCorrect= "ok";
        }
    }

    void checkCredAction(String isCorrect, TextInputLayout email, TextInputLayout password, BiConsumer<String,String> consumer,String email1,String password1){
        if (isCorrect.equals("both")){
            Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            email.setError("Required");
            password.setError("Required");
        }
        else if(isCorrect.equals("email")){
            Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            email.setError("Required");
        }
        else if(isCorrect.equals("password")){
            Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            password.setError("Required");
        }
        else if(isCorrect.equals("ok")){
            consumer.accept(email1,password1);
        }
    }


    void setmain(){
        layouts.get(2).setVisibility(View.VISIBLE);
        layouts.get(3).setVisibility(View.GONE);
        layouts.get(4).setVisibility(View.GONE);
        signin_visible = true;
        signup_visible = false;
        frgt_psswd_visible = false;
        register_laytouts.get(0).setError(null);
        register_laytouts.get(1).setError(null);
    }

    void setRegister(){
        layouts.get(3).setVisibility(View.VISIBLE);
        layouts.get(2).setVisibility(View.GONE);
        layouts.get(4).setVisibility(View.GONE);
        signin_visible = false;
        signup_visible = true;
        frgt_psswd_visible = false;
        login_laytouts.get(0).setError(null);
        login_laytouts.get(1).setError(null);
    }

    void setForgetpassword(){
        layouts.get(4).setVisibility(View.VISIBLE);
        layouts.get(3).setVisibility(View.GONE);
        layouts.get(2).setVisibility(View.GONE);
        signin_visible = false;
        signup_visible = false;
        frgt_psswd_visible = true;
    }

    private void register(String email,String psswd){
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            myauth.createUserWithEmailAndPassword(email,psswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser firebaseUser = myauth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(MainActivity.this, "Verification Link sent to\n"+email, Toast.LENGTH_SHORT).show();
                                                register_laytouts.get(0).setError(null);
                                                register_laytouts.get(1).setError(null);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Verification Link not sent\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                String s = firebaseUser.getUid();
                                HashMap<String,Object> m = new HashMap <String,Object> ();
                                m.put("name",email);
                                m.put("phone","");
                                m.put("email",email);
                                m.put("college","");
                                m.put("active",true);
                                firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                                firebaseDatabase.child("Users").child(s).setValue(m);
//                              intentCall(MainActivity.this,Register_Info.class,email,s);
                                setmain();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this, "Enter valid Email address", Toast.LENGTH_SHORT).show();
            register_laytouts.get(0).setError("Enter valid Email address");
            register_laytouts.get(1).setError(null);
        }

    }
    private void login(String email,String psswd){
        myauth.signInWithEmailAndPassword(email,psswd).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = myauth.getCurrentUser();
                if (firebaseUser.isEmailVerified()){
                    String s = firebaseUser.getUid();
                    Toast.makeText(MainActivity.this, "SignedIn Successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences shr = getSharedPreferences("User",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shr.edit();
                    editor.putString("email",email);
                    editor.putString("psswd",psswd);
                    editor.putString("uid",s);
                    editor.apply();
                    login_laytouts.get(0).setError(null);
                    login_laytouts.get(1).setError(null);
                    intentCall(MainActivity.this,webPage.class,email,s);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "First Verify Your Email with link sent to\n"+email, Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed \n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword(String reset_email,String password){
        myauth.sendPasswordResetEmail(reset_email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Link Sent To email\n"+reset_email,Toast.LENGTH_LONG).show();
                    setmain();
                }
            }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    void emailVerify(String email , String psswd){
        myauth.signInWithEmailAndPassword(email,psswd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = myauth.getCurrentUser();
                if (firebaseUser.isEmailVerified()){
                    Toast.makeText(MainActivity.this, "Already Verified", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(MainActivity.this,"Link sent to\n"+email,Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
        myauth.signOut();
    }



    void clear(EditText v){
        v.setText("");
    }

    void keyboar_close(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    void intentCall(Context context, Class c,String s,String s1){
        Intent i = new Intent(context,c);
        i.putExtra("email",s);
        i.putExtra("uid",s1);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (signup_visible==true){
            setmain();
            keyboar_close(back_btn);
            clear(registerInput.get(0));
            clear(registerInput.get(1));
            clear(logininput.get(0));clear(logininput.get(1));
        }
        else if (frgt_psswd_visible==true){
            setmain();
            keyboar_close(back_btn);
            clear(forgotPasswordEmailInput);
            clear(registerInput.get(0));
            clear(registerInput.get(1));
            clear(logininput.get(0));clear(logininput.get(1));
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences getshare = getSharedPreferences("User",MODE_PRIVATE);
        String email = getshare.getString("email", "");
        String psswd = getshare.getString("psswd", "");
        String uid = getshare.getString("uid", "");

        if (email.isEmpty() || psswd.isEmpty() || uid.isEmpty()){
            logininput.get(0).setText(email);
            logininput.get(1).setText(psswd);
        }
        else{
            intentCall(MainActivity.this,webPage.class,email,uid);
            finish();
        }
    }
}