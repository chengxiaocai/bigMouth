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

public class TempFragment extends Fragment {

	private WebView mWebView;
	private String url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_playing,
				container, false);
		mWebView = (WebView) contentView.findViewById(R.id.playing_web);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		// mWebView.loadUrl("http://app.01teacher.cn/StudentPeriod/My");
		mWebView.loadUrl(url);
		return contentView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		MainAcitivity ac = (MainAcitivity) activity;
		url = ac.getUrl();
	}
}
