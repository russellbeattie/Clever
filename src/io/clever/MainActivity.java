package io.clever;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.webkit.WebSettings.*;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

	WebView webView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        
        linearLayout.setBackgroundColor(Color.BLACK);
        
        
        webView = (WebView) findViewById(R.id.webView1);
        
        WebSettings webSettings = webView.getSettings();
        
        webView.setWebChromeClient(new WebChromeClient()); 
        
        webView.setWebViewClient(new WebViewClient() {

        	   public void onPageFinished(WebView view, String url) {
         		   
        		   Log.d("scale", view.getScale() + "");
      	    
        	   }
        	   
        	   public void onScaleChanged (WebView view, float oldScale, float newScale){
        		   
        		   Log.d("scale changed", oldScale + " - " + newScale);
        		   
        		   if(newScale > 0.7){
        		   
        		   		Log.d("scale","reset");
        				//view.setInitialScale(70);
  			   
        		   }
        		   
        	   }
        
        });
        
        webView.setInitialScale(70);
       
        webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        webView.setVerticalScrollBarEnabled(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true); 
        webSettings.setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        webSettings.setSaveFormData(false);      
        webSettings.setLightTouchEnabled(false);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webSettings.setUserAgentString("Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Large Screen Safari/534.24 GoogleTV");

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
