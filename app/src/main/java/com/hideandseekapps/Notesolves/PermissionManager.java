package com.hideandseekapps.Notesolves;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;

public class PermissionManager extends Activity {
    private static PermissionManager instance;
    public static final String PERMISSION_POST_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS;
    public static final int PERMISSION_REQ_CODE = 1001;

    private PermissionManager(Context context) {}

    public static synchronized PermissionManager getInstance(Context context) {
        if (instance == null) {
            instance = new PermissionManager(context);
        }
        return instance;
    }

     void requestPermission(Context context,String permission){
        if(ActivityCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, permission+" Granted", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions((Activity) context,new String[]{permission},PERMISSION_REQ_CODE);
        }
    }
}
