package com.hideandseekapps.Notesolves;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.hideandseekapps.firebase_tut.R;

public class launcher extends AppCompatActivity implements AppUpdateView {
    ActionBar actionBar;
    ForceUpdate mForceUpdate;
    ActivityResultLauncher<IntentSenderRequest> mActivityResultLauncher;
    Boolean isUpdateCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        actionBar = getSupportActionBar();
        actionBar.hide();

        mForceUpdate = new ForceUpdate(this);
        mActivityResultLauncher = this.registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        switch (result.getResultCode()) {
                            case com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED:
                                isUpdateCancelled = true;
                                break;
                            case RESULT_OK:
                                isUpdateCancelled = false;
                                break;
                            case RESULT_CANCELED:
                                isUpdateCancelled = true;
                                break;
                        }
                    }
                });
    }

    @Override
    public void updateNotAvailable() {
        startActivityIntent();
    }

    public void showUpdatePopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(launcher.this);
        builder.setTitle("Update Mandatory");
        builder.setMessage("Your app version is no longer supported. " +
                "Please update to the latest version to ensure a seamless experience.");
        builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mForceUpdate.requestAppUpdate(mActivityResultLauncher,launcher.this);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    public void update() {
        if (mForceUpdate.checkForUpdate()) {

            if (isUpdateCancelled) {
                showUpdatePopUp();
                return;
            }
            mForceUpdate.requestAppUpdate(mActivityResultLauncher,this);
        }
        else startActivityIntent();
    }

    void startActivityIntent(){

        if(ActivityCompat.checkSelfPermission(this,PermissionManager.PERMISSION_POST_NOTIFICATION) != PackageManager.PERMISSION_GRANTED){
            PermissionManager.getInstance(this)
                    .requestPermission(PermissionManager.PERMISSION_POST_NOTIFICATION);
        }
        else{
            navigatetonextscreen();
        }
    }

    private void navigatetonextscreen() {
        Intent intent = new Intent(launcher.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        navigatetonextscreen();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}