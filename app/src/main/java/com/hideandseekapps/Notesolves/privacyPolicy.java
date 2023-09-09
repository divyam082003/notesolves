package com.hideandseekapps.Notesolves;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hideandseekapps.firebase_tut.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class privacyPolicy extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    ActionBar actionBar;

    //webview
    @BindView(R.id.privacyWeb) WebView privacyWebView;

    final static String URL_PRIVACY = "https://docs.google.com/document/d/13ItPlMpMQ4kK-FHZ61jXQeaybBlQNvAcgvX3Hzwj8LY/edit?usp=share_link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();
        actionBar.hide();


        adjustWebview(privacyWebView);
        loadWebView(privacyWebView,URL_PRIVACY);



    }

    private void loadWebView(WebView privacyWebView, String urlPrivacy) {
        privacyWebView.loadUrl(urlPrivacy);
        privacyWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    void adjustWebview(WebView webView){
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle1 = new Bundle();
        bundle1.putString(GAManager.activity_name,"PrivacyPolicy");
        GAManager.logEvent(this,GAManager.open_screen,bundle1);
    }
}