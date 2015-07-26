package com.bigmouth.app.ui;

import java.util.LinkedHashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.bigmouth.app.R;
import com.bigmouth.app.scan.MipcaCaptureActivity;
import com.bigmouth.app.util.PersistentUtil;

public class LoginActivity extends Activity {
	private WebView mWebView;
	Boolean isLogin;
	private ImageView ivScan;
	private RelativeLayout reTtile;
	private TextView tvIndex;
	private String returnUrl;
	private final String FILENAME = "bigmouth";
	private String type;
	private Boolean isFromCode = false;
	
	private static final int MSG_SET_TAGS = 1002;
	private static final int MSG_SET_ALIAS = 1001;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitity_login);
		reTtile = (RelativeLayout) findViewById(R.id.re_login_title);
		if (PersistentUtil.getInstance().readString(LoginActivity.this, "id",
				null) != null) {
			type = PersistentUtil.getInstance().readString(this, "type", "0");
			reTtile.setVisibility(View.VISIBLE);
			if ("2".equals(type)) {
				findViewById(R.id.iv_login_read).setVisibility(
					View.GONE);
				findViewById(R.id.iv_login_scan).setVisibility(
						View.VISIBLE);
			} if("1".equals(type)){
				findViewById(R.id.iv_login_scan).setVisibility(
						View.GONE);
				findViewById(R.id.iv_login_read).setVisibility(
						View.VISIBLE);

			}
		}
		tvIndex = (TextView) findViewById(R.id.tv_lging_index);

		getLogin();
		tvIndex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.loadUrl("http://app.01teacher.cn/");
				v.setVisibility(View.GONE);
			}
		});
		findViewById(R.id.iv_login_scan).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(LoginActivity.this,
								MipcaCaptureActivity.class);

						startActivityForResult(intent, 1);
					}
				});
		findViewById(R.id.iv_login_read).setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent3 = new Intent(LoginActivity.this,
								StudyActivity1.class);
						
						startActivity(intent3);
						finish();
					}
				});
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
						"http://app.01teacher.cn/Account/LogOn")) {
					reTtile.setVisibility(View.GONE);

				} else if (view.getUrl().equals(
						"http://app.01teacher.cn/Account/LogOff")) {
					reTtile.setVisibility(View.GONE);
					SharedPreferences sp = getSharedPreferences(FILENAME,
							Context.MODE_PRIVATE);

					sp.edit().remove("id").commit();
					sp.edit().remove("type").commit();
				}

				else if (view.getUrl().equals("http://app.01teacher.cn/")) {
					reTtile.setVisibility(View.VISIBLE);
					type = PersistentUtil.getInstance().readString(LoginActivity.this,
							"type", "0");


				}

				else {

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
				if (CookieStr != null
						&& CookieStr.contains("LinguaApp_UserType")) {
					String str[] = CookieStr.split(";");
					Log.i("id", str[1] == null ? "" : str[1].split("=")[1]);
					Log.i("type", str[2] == null ? "" : str[2].split("=")[1]);
					PersistentUtil.getInstance().write(LoginActivity.this,
							"id", str[1] == null ? "" : str[1].split("=")[1]);
					PersistentUtil.getInstance().write(LoginActivity.this,
							"type", str[2] == null ? "" : str[2].split("=")[1]);
					type = PersistentUtil.getInstance().readString(LoginActivity.this,
							"type", "0");
					setAlias(str[1].split("=")[1]);
					if ("2".equals(type)) {
						findViewById(R.id.iv_login_read).setVisibility(
								View.GONE);
						findViewById(R.id.iv_login_scan).setVisibility(
								View.VISIBLE);
						
					} if("1".equals(type)){
						findViewById(R.id.iv_login_scan).setVisibility(
								View.GONE);
						findViewById(R.id.iv_login_read).setVisibility(
								View.VISIBLE);
						

					}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}

		returnUrl = data.getStringExtra("result");
		Intent intent = new Intent(this, CodeAcitivity.class);
		intent.putExtra("url", returnUrl);
		startActivity(intent);
	}
	public void setAlias(String userid ){
		Log.i("cc", "set alias"+userid);
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, userid));

	}
	 @SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
	        @SuppressWarnings("unchecked")
			@Override
	        public void handleMessage(android.os.Message msg) {
	            super.handleMessage(msg);
	            switch (msg.what) {
	            case MSG_SET_ALIAS:
	                Log.d("ccs", "Set alias in handler.");
	                JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
	                break;
	                
	           
	                
	            default:
	                Log.i("cc", "Unhandled msg - " + msg.what);
	            }
	        }
	    };
	   
	    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

	        @Override
	        public void gotResult(int code, String alias, Set<String> tags) {
	            String logs ;
	            switch (code) {
	            case 0:
	                logs = "Set tag and alias success";
	                Log.i("cc", logs);
	                break;
	                
	            case 6002:
	                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
	                Log.i("cc", logs);
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);

	               
	                break;
	            
	            default:
	                logs = "Failed with errorCode = " + code;
	                
	            }
	            
	            
	        }
		    
		};

}
