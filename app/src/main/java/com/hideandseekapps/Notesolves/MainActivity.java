package com.hideandseekapps.Notesolves;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import com.hideandseekapps.firebase_tut.databinding.ActivityMainBinding;
import com.hideandseekapps.firebase_tut.databinding.EmailVerifyBinding;
import com.hideandseekapps.firebase_tut.databinding.ForogtPasswordBinding;
import com.hideandseekapps.firebase_tut.databinding.LoginBinding;
import com.hideandseekapps.firebase_tut.databinding.RegisterBinding;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private boolean signup_visible, signin_visible, frgt_psswd_visible, VisibleEmailVerify = true;
    private String registerName, registerEmail, registerPassword, loginEmail, loginPassword, forgotPasswordEmail, emailVerify, passwordVerify;
    private FirebaseAuth myauth;
    private DatabaseReference firebaseDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String REGISTER = "REGISTER";
    private static final String LOGIN = "LOGIN";
    private static final String VERIFY_EMAIL = "VERIFY_EMAIL";
    private static final String FORGOT_PASSWD = "FORGOT_PSSWD";


    //register
    @BindView(R.id.register)
    TextView register;
    @BindViews({R.id.d0, R.id.d1, R.id.d})
    List<TextInputLayout> register_laytouts;
    @BindViews({R.id.register_email, R.id.register_psswd, R.id.register_name})
    List<EditText> registerInput;
    @BindView(R.id.register_btn)
    Button register_btn;

    //login
    @BindViews({R.id.d2, R.id.d3})
    List<TextInputLayout> login_laytouts;
    @BindViews({R.id.login_email, R.id.login_psswd})
    List<EditText> logininput;
    @BindView(R.id.login_btn)
    Button login_btn;

    //Verify Email
    @BindView(R.id.verify_email)
    TextView verify_email;
    @BindViews({R.id.verify_email_head, R.id.verify_email_head2})
    List<TextInputLayout> verifyEmailLayout;
    @BindViews({R.id.verify_email_email, R.id.verify_email_psswd})
    List<EditText> verifyEmailInput;
    @BindView(R.id.verify_email_btn)
    Button verifyEmailBtn;

    //Forgot Password
    @BindView(R.id.frgt_psswd)
    TextView forgot_psswd;
    @BindView(R.id.frgt_psswd_email_head)
    TextInputLayout forgotPasswordEmailLayout;
    @BindView(R.id.frgt_psswd_email)
    EditText forgotPasswordEmailInput;
    @BindView(R.id.frgt_psswd_btn)
    Button forgot_psswd_btn;
    TextView text;
    View layout;
    TextView vCode;

    //actionBar
    ActionBar actionBar;

    //ad
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    ActivityMainBinding activityMainBinding;
    LoginBinding loginBinding;
    RegisterBinding registerBinding;
    ForogtPasswordBinding forogtPasswordBinding;
    EmailVerifyBinding emailVerifyBinding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        actionBar = getSupportActionBar();
        actionBar.hide();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        String android_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder().build();

        activityMainBinding.adView.loadAd(adRequest);
        activityMainBinding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }
        });

        InterstitialAd.load(this, "ca-app-pub-6906858630730365/4541335455",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        ButterKnife.bind(this);
        myauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.toast_message));
        text = layout.findViewById(R.id.toast_message);

        //privacy
        activityMainBinding.privacy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, privacyPolicy.class);
                startActivity(intent);

                Bundle params = new Bundle();
                GAManager.logEvent(MainActivity.this,GAManager.loginPge_privacy_click,params);
            }
        });


        //Register Work
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRegister();

                //GA
                Bundle params = new Bundle();
                GAManager.logEvent(MainActivity.this,GAManager.register_button_click,params);


                keyboar_close(register);
            }
        });


        registerInput.get(1).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerName = registerInput.get(2).getText().toString();
                    registerEmail = registerInput.get(0).getText().toString();
                    registerPassword = registerInput.get(1).getText().toString();
                    if (registerName.isEmpty()) {
                        register_laytouts.get(2).setError("Required");
                    } else {
                        String isCorrect = checkCred(registerEmail, registerPassword);
                        checkCredAction(isCorrect, register_laytouts.get(0), register_laytouts.get(1), REGISTER, registerEmail, registerPassword, registerName);
                    }
                    keyboar_close(register_btn);
                }
                return false;
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                registerName = registerInput.get(2).getText().toString();
                registerEmail = registerInput.get(0).getText().toString();
                registerPassword = registerInput.get(1).getText().toString();
                if (registerName.isEmpty()) {
                    register_laytouts.get(2).setError("Required");
                } else {
                    String isCorrect = checkCred(registerEmail, registerPassword);
                    checkCredAction(isCorrect, register_laytouts.get(0), register_laytouts.get(1), REGISTER, registerEmail, registerPassword, registerName);
                }
                keyboar_close(register_btn);
            }
        });


        // login Work
        logininput.get(1).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    loginEmail = logininput.get(0).getText().toString();
                    loginPassword = logininput.get(1).getText().toString();

                    String isCorrect = checkCred(loginEmail, loginPassword);
                    checkCredAction(isCorrect, login_laytouts.get(0), login_laytouts.get(1), LOGIN, loginEmail, loginPassword, loginEmail);

                    clear(logininput.get(1));
                    keyboar_close(login_btn);
                }
                return false;
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmail = logininput.get(0).getText().toString();
                loginPassword = logininput.get(1).getText().toString();

                String isCorrect = checkCred(loginEmail, loginPassword);
                checkCredAction(isCorrect, login_laytouts.get(0), login_laytouts.get(1), LOGIN, loginEmail, loginPassword, loginEmail);

                clear(logininput.get(1));
                keyboar_close(login_btn);

            }
        });

        //Forgot password work

        forgot_psswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //GA
                Bundle params = new Bundle();
                GAManager.logEvent(MainActivity.this,GAManager.forgot_password_btn,params);

                setForgetpassword();
            }
        });

        forgotPasswordEmailInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    forgotPasswordEmail = forgotPasswordEmailInput.getText().toString();
                    String isCorrect = checkCred(forgotPasswordEmail);
                    checkCredAction(isCorrect, forgotPasswordEmailLayout, forgotPasswordEmailLayout,
                            FORGOT_PASSWD, forgotPasswordEmail, forgotPasswordEmail, forgotPasswordEmail);

                    clear(forgotPasswordEmailInput);
                    keyboar_close(forgot_psswd_btn);
                }
                return false;
            }
        });


        forgot_psswd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordEmail = forgotPasswordEmailInput.getText().toString();
                String isCorrect = checkCred(forgotPasswordEmail);
                checkCredAction(isCorrect, forgotPasswordEmailLayout, forgotPasswordEmailLayout,
                        FORGOT_PASSWD, forgotPasswordEmail, forgotPasswordEmail, forgotPasswordEmail);
                clear(forgotPasswordEmailInput);
                keyboar_close(forgot_psswd_btn);
            }
        });

        //email verify

        verify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle params = new Bundle();
                GAManager.logEvent(MainActivity.this,GAManager.verify_email_click,params);

                setEmailVerify();
            }
        });

        verifyEmailInput.get(1).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    emailVerify = verifyEmailInput.get(0).getText().toString();
                    passwordVerify = verifyEmailInput.get(1).getText().toString();
                    String isCorrect = checkCred(emailVerify, passwordVerify);
                    checkCredAction(isCorrect, verifyEmailLayout.get(0), verifyEmailLayout.get(1), VERIFY_EMAIL, emailVerify, passwordVerify, emailVerify);
                    keyboar_close(verify_email);
                }
                return false;
            }
        });

        verifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailVerify = verifyEmailInput.get(0).getText().toString();
                passwordVerify = verifyEmailInput.get(1).getText().toString();
                String isCorrect = checkCred(emailVerify, passwordVerify);
                checkCredAction(isCorrect, verifyEmailLayout.get(0), verifyEmailLayout.get(1), VERIFY_EMAIL, emailVerify, passwordVerify, emailVerify);
                keyboar_close(verify_email);

            }
        });

        activityMainBinding.disclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDisclaimer(MainActivity.this);
            }
        });

        vCode = findViewById(R.id.vCode);
        vCode.setText("| v"+getAppVersionName(this));

        setmain();
    }




    String checkCred(String email, String password) {
        String isCorrect;
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            return isCorrect = "both";
        } else if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            return isCorrect = "email";
        } else if (!TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            return isCorrect = "password";
        } else {
            return isCorrect = "ok";
        }
    }

    String checkCred(String email) {
        String isCorrect;
        if (TextUtils.isEmpty(email)) {
            return isCorrect = "email";
        } else {
            return isCorrect = "ok";
        }
    }

    void checkCredAction(String isCorrect, TextInputLayout email, TextInputLayout password, String task, String email1, String password1, String name1) {
        if (isCorrect.equals("both")) {
            Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            email.setError("Required");
            password.setError("Required");
        } else if (isCorrect.equals("email")) {
            Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            email.setError("Required");
        } else if (isCorrect.equals("password")) {
            Toast.makeText(MainActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            password.setError("Required");
        } else if (isCorrect.equals("ok")) {
            if (task == REGISTER) {
                register(email1, password1, name1);
            } else if (task == LOGIN) {
                login(email1, password1);
            } else if (task == VERIFY_EMAIL) {
                emailVerify(email1, password1);
            } else if (task == FORGOT_PASSWD) {
                resetPassword(email1, password1);
            }
        }
    }


    void setmain() {
        activityMainBinding.l0.setVisibility(View.VISIBLE);
        activityMainBinding.l1.setVisibility(View.GONE);
        activityMainBinding.l2.setVisibility(View.GONE);
        activityMainBinding.l3.setVisibility(View.GONE);
        signin_visible = true;
        signup_visible = false;
        frgt_psswd_visible = false;
        VisibleEmailVerify = false;
        register_laytouts.get(0).setError(null);
        register_laytouts.get(1).setError(null);
        forgotPasswordEmailLayout.setError(null);
        verifyEmailLayout.get(0).setError(null);
        verifyEmailLayout.get(1).setError(null);
    }

    void setRegister() {
        activityMainBinding.l1.setVisibility(View.VISIBLE);
        activityMainBinding.l0.setVisibility(View.GONE);
        activityMainBinding.l2.setVisibility(View.GONE);
        activityMainBinding.l3.setVisibility(View.GONE);
        signin_visible = false;
        signup_visible = true;
        frgt_psswd_visible = false;
        VisibleEmailVerify = false;
        login_laytouts.get(0).setError(null);
        login_laytouts.get(1).setError(null);

    }

    void setForgetpassword() {
        activityMainBinding.l2.setVisibility(View.VISIBLE);
        activityMainBinding.l0.setVisibility(View.GONE);
        activityMainBinding.l1.setVisibility(View.GONE);
        activityMainBinding.l3.setVisibility(View.GONE);
        signin_visible = false;
        signup_visible = false;
        frgt_psswd_visible = true;
        VisibleEmailVerify = false;
        login_laytouts.get(0).setError(null);
        login_laytouts.get(1).setError(null);

    }

    void setEmailVerify() {
        activityMainBinding.l3.setVisibility(View.VISIBLE);
        activityMainBinding.l0.setVisibility(View.GONE);
        activityMainBinding.l1.setVisibility(View.GONE);
        activityMainBinding.l2.setVisibility(View.GONE);
        verifyEmailInput.get(0).setText(logininput.get(0).getText().toString());
        signin_visible = false;
        signup_visible = false;
        frgt_psswd_visible = true;
        VisibleEmailVerify = true;
        login_laytouts.get(0).setError(null);
        login_laytouts.get(1).setError(null);

    }

    void setDisclaimer(Context context) {
        Intent intent = new Intent(context, disclaimer.class);
        startActivity(intent);

        Bundle params = new Bundle();
        GAManager.logEvent(this,GAManager.loginPge_disclaimer_click,params);

    }
    
    private void register(String email, String psswd, String registerName) {
        Bundle params = new Bundle();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            myauth.createUserWithEmailAndPassword(email, psswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = myauth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                //Google Analytics
                                GAManager.logEvent(MainActivity.this,GAManager.registration_success,params);

                                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(MainActivity.this, "Verification Link sent to\n" + email, Toast.LENGTH_SHORT).show();
                                                register_laytouts.get(0).setError(null);
                                                register_laytouts.get(1).setError(null);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Verification Link not sent\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                String s = firebaseUser.getUid();
                                HashMap<String, Object> m = new HashMap<String, Object>();
                                m.put("name", registerName);
                                m.put("phone", "");
                                m.put("email", email);
                                m.put("college", "");
                                m.put("active", true);
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

                            GAManager.logEvent(MainActivity.this,GAManager.register_fail,params);
                            text.setText(e.getLocalizedMessage());
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT); // Set the duration of the toast
                            toast.setView(layout); // Set the custom layout
                            toast.show();
                        }
                    });
        } else {
            Toast.makeText(this, "Enter valid Email address", Toast.LENGTH_SHORT).show();
            register_laytouts.get(0).setError("Enter valid Email address");
            register_laytouts.get(1).setError(null);
        }

    }

    private void login(String email, String psswd) {
        Bundle params = new Bundle();
        myauth.signInWithEmailAndPassword(email, psswd).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //GA
                        GAManager.logEvent(MainActivity.this,GAManager.login_success,params);

                        FirebaseUser firebaseUser = myauth.getCurrentUser();
                        if (firebaseUser.isEmailVerified()) {
                            String s = firebaseUser.getUid();
                            Toast.makeText(MainActivity.this, "SignedIn Successfully", Toast.LENGTH_SHORT).show();
                            SharedPreferences shr = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor editor = shr.edit();
                            editor.putString("email", email);
                            editor.putString("psswd", psswd);
                            editor.putString("uid", s);
                            editor.apply();
                            login_laytouts.get(0).setError(null);
                            login_laytouts.get(1).setError(null);
                            intentCall(MainActivity.this, webPage.class, email, s);
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity.this);
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "First Verify Your Email with link sent to\n" + email, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //GA
                        GAManager.logEvent(MainActivity.this,GAManager.login_fail,params);
                        text.setText(e.getLocalizedMessage());
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT); // Set the duration of the toast
                        toast.setView(layout); // Set the custom layout
                        toast.show();
                    }
                });
    }

    private void resetPassword(String reset_email, String password) {
        myauth.sendPasswordResetEmail(reset_email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Link Sent To email\n" + reset_email, Toast.LENGTH_LONG).show();
                            setmain();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT); // Set the duration of the toast
                        toast.setView(layout);
                        toast.show();
                    }
                });
    }

    void emailVerify(String email, String psswd) {
        myauth.signInWithEmailAndPassword(email, psswd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = myauth.getCurrentUser();
                if (firebaseUser.isEmailVerified()) {
                    Toast.makeText(MainActivity.this, "Already Verified", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(MainActivity.this, "Link sent to\n" + email, Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        });
        myauth.signOut();
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


    void clear(EditText v) {
        v.setText("");
    }

    void keyboar_close(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        } catch (Exception e) {

        }
    }

    void intentCall(Context context, Class c, String s, String s1) {
        Intent i = new Intent(context, c);
        i.putExtra("email", s);
        i.putExtra("uid", s1);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (signup_visible == true) {
            setmain();

            clear(registerInput.get(0));
            clear(registerInput.get(1));
            clear(logininput.get(0));
            clear(logininput.get(1));
            clear(verifyEmailInput.get(0));
            clear(verifyEmailInput.get(1));
        } else if (frgt_psswd_visible == true) {
            setmain();

            clear(forgotPasswordEmailInput);
            clear(registerInput.get(0));
            clear(registerInput.get(1));
            clear(logininput.get(0));
            clear(logininput.get(1));
            clear(verifyEmailInput.get(0));
            clear(verifyEmailInput.get(1));
        } else if (VisibleEmailVerify == true) {
            setmain();
            clear(verifyEmailInput.get(0));
            clear(verifyEmailInput.get(1));
            clear(registerInput.get(0));
            clear(registerInput.get(1));
            clear(logininput.get(0));
            clear(logininput.get(1));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences getshare = getSharedPreferences("User", MODE_PRIVATE);
        String email = getshare.getString("email", "");
        String psswd = getshare.getString("psswd", "");
        String uid = getshare.getString("uid", "");

        if (email.isEmpty() || psswd.isEmpty() || uid.isEmpty()) {
            logininput.get(0).setText(email);
            logininput.get(1).setText(psswd);
        } else {
            intentCall(MainActivity.this, webPage.class, email, uid);
            finish();
        }
        Bundle bundle1 = new Bundle();
        bundle1.putString(GAManager.activity_name,"LoginScreen");
        GAManager.logEvent(this,GAManager.open_screen,bundle1);

    }


}