package com.bigmouth.app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bigmouth.app.R;
import com.bigmouth.app.util.PersistentUtil;

public class LoginActivity extends Activity {
	private WebView mWebView;
	Boolean isLogin;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitity_login);
		getLogin();
		mWebView = (WebView) findViewById(R.id.login);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.getSettings().setLightTouchEnabled(true);
		final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(
				this);
		mWebView.addJavascriptInterface(myJavaScriptInterface,
				"AndroidFunction");
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				if (view.getUrl().equals(
						"http://app.01teacher.cn/Account/LogOn")||view.getUrl().equals(
								"http://app.01teacher.cn/Account/LogOff")) {
				

				} else if (view.getUrl().equals(
						"http://app.01teacher.cn/")) {
					
					
				} else {

					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainAcitivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
					finish();
				}
				Log.d("cc......", view.getUrl());

				return true;
			}
			public void onPageFinished(WebView view, String url) {
	            CookieManager cookieManager = CookieManager.getInstance();
	            String CookieStr = cookieManager.getCookie(url);
	            Log.e("sunzn", "Cookies = " + CookieStr);
	            super.onPageFinished(view, url);
	            if(CookieStr!=null&&CookieStr.contains("LinguaApp_UserType")){
	            	String str [] = CookieStr.split(";");
	            	Log.i("id", str [1]==null?"":str[1].split("=")[1]);
	            	Log.i("type", str [2]==null?"":str[2].split("=")[1]);
	            	PersistentUtil.getInstance().write(LoginActivity.this, "id",str [1]==null?"":str[1].split("=")[1]);
	            	PersistentUtil.getInstance().write(LoginActivity.this, "type",str [2]==null?"":str[2].split("=")[1]);
	            }
	        }
			
			
		});
		mWebView.loadUrl("http://app.01teacher.cn/");

	}

	public class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context c) {
			mContext = c;
		}

		public void showToast(String webMessage) {
			final String msgeToast = webMessage;
			Log.i("cc.........", webMessage);
			/*
			 * myHandler.post(new Runnable() {
			 * 
			 * @Override public void run() { // This gets executed on the UI
			 * thread so it can safely modify Views
			 * myTextView.setText(msgeToast); } });
			 */

			Toast.makeText(mContext, webMessage, Toast.LENGTH_SHORT).show();
		}
	}

	void getLogin() {
		SharedPreferences sharedPreferences = getSharedPreferences("isLogin",
				Context.MODE_PRIVATE); // 私有数据

		isLogin = sharedPreferences.getBoolean("isLogin", false);

	}

}
