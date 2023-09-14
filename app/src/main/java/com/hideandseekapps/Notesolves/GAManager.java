package com.hideandseekapps.Notesolves;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

public class GAManager {

    //EVENT NAME
    final static String open_screen = "open_screen";
    final static String menu_click = "menu_click";
    final static String menu_item_click = "menu_item_click";
    final static String edit_profile = "edit_profile";
    final static String delete_account_yes = "delete_account_yes";
    final static String delete_account_no = "delete_account_no";
    final static String singOut_yes = "signout_yes";
    final static String signOut_no = "signout_no";
    final static String registration_success = "registration_success";
    final static String register_fail = "registration_fail";
    final static String register_button_click = "register_button_click";
    final static String verify_email_click = "verify_email_click";
    final static String forgot_password_btn = "forgot_password_btn";
    final static String loginPge_disclaimer_click = "loginPge_disclaimer_click";
    final static String loginPge_privacy_click = "loginPge_privacy_click";
    final static String login_success = "login_success";
    final static String login_fail = "login_fail";

    //PARAMS
    final static String full_name = "full_name";
    final static String phone_number = "phone_number";
    final static String college = "college";

    //EVENT PARAMETER
    final static String activity_name = "activity_name";
    final static String menu_item_name = "menu_item_name";
    final static String edited_field = "edited_field";






    static public void logEvent(Context context, String eventName, Bundle params) {
        FirebaseAnalytics.getInstance(context).logEvent(eventName, params);
        String details = null;
        for (String key : params.keySet()){
            details = key+" : " + params.get(key) + "\n";
        }
        Log.d("GaEvent", "eventName: "+eventName+"\nparams:"+ "{" +details + "}");
    }

}
