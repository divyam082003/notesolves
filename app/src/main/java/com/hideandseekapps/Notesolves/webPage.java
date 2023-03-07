package com.hideandseekapps.Notesolves;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hideandseekapps.firebase_tut.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class webPage extends AppCompatActivity {

    @BindView(R.id.webtext) TextView webText;
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.pg) ProgressBar pg;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid,name,email;
    Menu menu;
    MenuItem item;
    ActionBar actionBar;
    final static String URL_NOTESOLVES = "https://notesolves.hideandseekapps.com";

    private AdView mAdView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        InterstitialAd.load(this,"ca-app-pub-6906858630730365/5410181101", adRequest,
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

        if (mInterstitialAd != null) {
            mInterstitialAd.show(webPage.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        mAdView.setAdListener(new AdListener() {
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

        ButterKnife.bind(this);
        actionBar = getSupportActionBar();

        adjustWebview(webView);
        loadWebView(webView);

        firebaseAuth = FirebaseAuth.getInstance();

        uid = getIntent().getStringExtra("uid");
        setInfo(uid);


    }

    void adjustWebview(WebView webView){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
    }

    void loadWebView(WebView webView){
        webView.loadUrl(URL_NOTESOLVES);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pg.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pg.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
    }

    private void setInfo(String uid) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model obj = snapshot.getValue(Model.class);
                if (obj != null){
                    name = obj.name;
                    actionBar.setSubtitle("Welcome "+ name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(webPage.this, "Something Went Wrong\n"+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.menu_title):
            {
                setProfile(uid,webPage.this);
                break;
            }
            case (R.id.edit_Info):
            {
                editInfo(uid);
                break;
            }
            case (R.id.update_password):
            {
                updatePassword();
                break;
            }
            case (R.id.delete_account):
            {
                deleteAlert();
                break;
            }
            case (R.id.signOut):
            {
                signOutAlert();
                break;
            }
            case (R.id.terms):
            {
                setDisclaimer(webPage.this);
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setProfile(String uid,Context context) {
        if (uid.isEmpty()){
            Toast.makeText(this, "No User Exists", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(context,userProfile.class);
            intent.putExtra("uid",uid);
            startActivity(intent);
        }
    }


    void setDisclaimer(Context context){
        Intent intent = new Intent(context,disclaimer.class);
        startActivity(intent);
    }

    void signOut(){
        firebaseAuth.signOut();
        SharedPreferences shr = getSharedPreferences("User",MODE_PRIVATE);
        SharedPreferences.Editor editor = shr.edit();
        editor.putString("email","");
        editor.putString("psswd","");
        editor.putString("uid","");
        editor.apply();
        Intent intent = new Intent(webPage.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    void signOutAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(webPage.this);
        builder.setTitle("SignOut");
        builder.setMessage("Are you sure you want to SignOut");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signOut();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    void editInfo(String uid){
        if (uid.isEmpty()){
            Toast.makeText(this, "No User Exists", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Model obj = snapshot.getValue(Model.class);
                    if (obj != null){
                        email = obj.email;
                        Intent i = new Intent(webPage.this,Register_Info.class);
                        i.putExtra("email",email);
                        i.putExtra("uid",uid);
                        startActivity(i);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(webPage.this, "Something Went Wrong\n"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        setInfo(uid);
    }

    void updatePassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(webPage.this);
        builder.setPositiveButton("Done", null);
        View view = getLayoutInflater().inflate(R.layout.update_password,null);
        EditText psswd_old = view.findViewById(R.id.old_psswd);
        EditText psswd_new = view.findViewById(R.id.new_psswd);
        Button psswd_update = view.findViewById(R.id.update_password_btn);
        psswd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newpass = psswd_new.getText().toString();
                String oldpass = psswd_old.getText().toString();
                PasswordUpdate(oldpass,newpass,view);
                keyboard_close(psswd_update);
                clear(psswd_new);
                clear(psswd_old);
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                }
        );
    }

    void deleteAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(webPage.this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to Delete your Account");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAccount();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    void deleteAccount(){
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(uid).child("active").setValue(false);
                            SharedPreferences shr = getSharedPreferences("User",MODE_PRIVATE);
                            SharedPreferences.Editor editor = shr.edit();
                            editor.putString("email","");
                            editor.putString("psswd","");
                            editor.putString("uid","");
                            editor.apply();
                            Intent intent = new Intent(webPage.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    void PasswordUpdate(String oldpass,String newpass,View view){
        if (!TextUtils.isEmpty(newpass) && !TextUtils.isEmpty(oldpass)){
            FirebaseUser user = firebaseAuth.getCurrentUser();
            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(webPage.this, "Something went wrong.\nPlease try again later", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(webPage.this, "Password Successfully Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(webPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Credentials Required", Toast.LENGTH_SHORT).show();
        }
    }

    void keyboard_close(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    void clear(EditText v){
        v.setText("");
    }

    //OnStrart
    @Override
    protected void onStart() {
        super.onStart();
        setInfo(uid);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            actionBar.hide();
            mAdView.setVisibility(View.GONE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mAdView.setVisibility(View.VISIBLE);
            actionBar.show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}