package com.bigmouth.app.ui.fragment;


import com.bigmouth.app.R;
import com.bigmouth.app.ui.MainAcitivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IndexFragment extends Fragment {
	private WebView mWebView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView=inflater.inflate(R.layout.fragment_invite,
		 container, false);
		
		mWebView = (WebView) contentView.findViewById(R.id.invit_web);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl("http://wap.management.01teacher.cn/");

		return contentView;
	}
	
}
