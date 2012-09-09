package io.clever;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.webkit.*;
import android.webkit.WebSettings.*;


public class MainActivity extends Activity {

	WebView webView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        webView = (WebView) findViewById(R.id.webView1);
        
        
        WebSettings webSettings = webView.getSettings();
        
        webView.setWebChromeClient(new WebChromeClient()); 
        webView.setWebViewClient(new WebViewClient());
        
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setBuiltInZoomControls(false);
        //webSettings.setSupportZoom(false);
        //webSettings.setTextZoom(80);   
        //webSettings.setDefaultZoom(ZoomDensity.FAR);
        webSettings.setSaveFormData(false);
        webSettings.setDatabaseEnabled(true); 
        webSettings.setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        
        webSettings.setLightTouchEnabled(false);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webSettings.setUserAgentString("Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Large Screen Safari/534.24 GoogleTV");

        webView.setInitialScale(90);
        
        webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        
        webView.loadUrl("file:///android_asset/www/index.html");
    
    }

    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack()){
            
        	webView.goBack();
            webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        
        } else {
        
        	super.onBackPressed();
        
        }
    }
    
    
}
