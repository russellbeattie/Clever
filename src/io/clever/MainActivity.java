package io.clever;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.*;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.*;
import android.webkit.WebSettings.*;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;

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
		goButton = (Button) findViewById(R.id.go_button);
		urlField = (EditText) findViewById(R.id.url);

		goButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				doNav();
			}
		});

		urlField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
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

		slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		slideUp.setDuration(500);
		slideUp.setAnimationListener(slideListener);

		slideDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		slideDown.setDuration(500);
		slideDown.setAnimationListener(slideListener);

		// WebView setup

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();

		webView.setWebChromeClient(new WebChromeClient());

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {

				Log.d("scale", view.getScale() + "");

				Log.d("pageFinished", url);

				if (url.equalsIgnoreCase(HOME_PAGE)) {
					// navbar.setVisibility(View.VISIBLE);
					navbar.startAnimation(slideDown);
				}

			}

			@Override
			public void onScaleChanged(WebView view, float oldScale,
					float newScale) {

				Log.d("scale changed", oldScale + " - " + newScale);

				if (newScale > 0.7) {

					Log.d("scale", "reset");
					// view.setInitialScale(70);

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
		webSettings.setDatabasePath("/data/data/"
				+ webView.getContext().getPackageName() + "/databases/");
		webSettings.setSaveFormData(false);
		webSettings.setLightTouchEnabled(false);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		webSettings.setRenderPriority(RenderPriority.HIGH);
		webSettings
				.setUserAgentString("Mozilla/5.0 (X11; Linux i686) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.77 Large Screen Safari/534.24 GoogleTV");

		final Intent intent = getIntent();
		if ((intent.getAction() == Intent.ACTION_VIEW)
				&& (intent.getData() != null)) {
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

	}

}
