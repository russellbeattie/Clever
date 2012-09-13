package io.clever;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.*;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.*;
import android.webkit.WebSettings.*;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;

public class MainActivity extends Activity {

    WebView webView;
    EditText urlField;
    Button goButton;
    LinearLayout linearLayout, navbar;
    Animation slideUp, slideDown;

    public static final String HOME_PAGE = "file:///android_asset/www/index.html";
    public static final String PREFS_NAME = "CleverPrefs";

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main);

	linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
	linearLayout.setBackgroundColor(Color.BLACK);

	// Navbar setup

	navbar = (LinearLayout) findViewById(R.id.navbar);
	navbar.setBackgroundColor(Color.GRAY);

	goButton = (Button) findViewById(R.id.go_button);
	urlField = (EditText) findViewById(R.id.url);

	goButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		doNav();
	    }
	});

	urlField.setOnEditorActionListener(new OnEditorActionListener() {
	    @Override
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		boolean handled = false;
		if (actionId == EditorInfo.IME_ACTION_GO) {

		    doNav();

		    handled = true;
		}
		return handled;
	    }
	});

	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	String lastUrl = settings.getString("lastUrl", "");

	urlField.setText(lastUrl);

	// Navbar animation settings

	AnimationListener slideListener = new AnimationListener() {
	    @Override
	    public void onAnimationEnd(Animation animation) {
		if (animation.equals(slideUp)) {
		    navbar.setVisibility(View.GONE);
		}
	    }

	    @Override
	    public void onAnimationRepeat(Animation animation) {
	    }

	    @Override
	    public void onAnimationStart(Animation animation) {
		if (animation.equals(slideDown)) {
		    navbar.setVisibility(View.VISIBLE);
		}
	    }
	};

	slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
	slideUp.setDuration(500);
	slideUp.setAnimationListener(slideListener);

	slideDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
	slideDown.setDuration(500);
	slideDown.setAnimationListener(slideListener);

	// WebView setup

	webView = (WebView) findViewById(R.id.webView);

	WebSettings webSettings = webView.getSettings();

	webView.setWebChromeClient(new MyWebChromeClient());

	webView.setWebViewClient(new WebViewClient() {

	    @Override
	    public void onPageFinished(WebView view, String url) {

		//Log.d("scale", view.getScale() + "");

		//Log.d("pageFinished", url);

		if(view.getScale() > 0.7){
		    	Log.d("scale reset", "");
			view.setInitialScale(70);
		}

		if (url.equalsIgnoreCase(HOME_PAGE)) {
		    navbar.startAnimation(slideDown);
		} else {
		    urlField.setText(url); 
		}

	    }

	    @Override
	    public void onScaleChanged(WebView view, float oldScale, float newScale) {

		Log.d("scale changed", oldScale + " - " + newScale);

	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.d("Loading URL", url);

		if(view.getScale() > 0.7){
		    	Log.d("scale reset", "");
			view.setInitialScale(70);
		}

		return super.shouldOverrideUrlLoading(view, url);
	    }

	});
	


	webView.setOnLongClickListener(new OnLongClickListener() {
	    @Override
	    public boolean onLongClick(View view) {

		navbar.startAnimation(slideDown);

		return true;
	    }
	});

	webView.setInitialScale(70);

	webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	webView.setVerticalScrollBarEnabled(false);
	webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

	webSettings.setBuiltInZoomControls(true);
	webSettings.setDisplayZoomControls(false);

	webSettings.setSupportZoom(true);

	webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
	webSettings.setPluginState(WebSettings.PluginState.ON);
	webSettings.setJavaScriptEnabled(true);
	webSettings.setDomStorageEnabled(true);
	webSettings.setDatabaseEnabled(true);
	webSettings.setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
	// webSettings.setSaveFormData(false);
	webSettings.setLightTouchEnabled(false);
	webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
	webSettings.setRenderPriority(RenderPriority.HIGH);
	webSettings.setUserAgentString("Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Large Screen Safari/534.24 GoogleTV");

	final Intent intent = getIntent();
	if ((intent.getAction() == Intent.ACTION_VIEW) && (intent.getData() != null)) {
	    final String url = intent.getDataString();
	    urlField.setText(url);
	    webView.loadUrl(url);
	    navbar.setVisibility(View.GONE);
	} else {
	    webView.loadUrl(HOME_PAGE);
	}

	webView.requestFocus();
    }

    @Override
    protected void onStop() {

	super.onStop();

	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putString("lastUrl", urlField.getText().toString());

	editor.commit();

    }

    @Override
    public void onBackPressed() {
	if (webView.canGoBack()) {

	    webView.goBack();
	    webView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

	} else {

	    super.onBackPressed();

	}
    }

    
    public void doNav() {

	String url = urlField.getText().toString();

	if (URLUtil.isValidUrl(url) == false) {
	    url = "http://" + url;
	}

	urlField.setText(url);

	webView.requestFocus();

	webView.loadUrl(url);

	navbar.startAnimation(slideUp);

	Toast.makeText(getApplicationContext(), "Press and hold to display the URL bar.", Toast.LENGTH_SHORT).show();

    }

    private class MyWebChromeClient extends WebChromeClient implements OnCompletionListener, OnErrorListener, OnPreparedListener {

	public void onProgressChanged(WebView view, int newProgress) {
	    if (newProgress == 100) {
		view.loadUrl("javascript:var vids = document.getElementsByTagName('video'); if(vids.length > 0){ vids[0].play();}");
	    }

	}

	public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
	    Log.d("onShowCustomView", "");
	}

	public void onPrepared(MediaPlayer mp) {
	    Log.d("onPrepared", "");
	}

	public void onCompletion(MediaPlayer mp) {
	    Log.d("onCompletion", "");
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
	    Log.d("onError", what + "");

	    return false;
	}

	public boolean onConsoleMessage(ConsoleMessage cm) {

	    // Log.d("onConsoleMessage", cm.message());
	    return true;

	}
    }

}
