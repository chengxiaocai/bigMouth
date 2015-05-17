package com.bigmouth.app.ui.fragment;



import com.bigmouth.app.R;
import com.bigmouth.app.util.PersistentUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ClassRecordFragment extends Fragment {

	private WebView mWebView;
	private String type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView=inflater.inflate(R.layout.fragment_playing,container, false);
		type = PersistentUtil.getInstance().readString(getActivity(), "type","0");

		mWebView = (WebView) contentView.findViewById(R.id.playing_web);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		if("2".equals(type)){
			mWebView.loadUrl("http://app.01teacher.cn/Message/TMyList");

		}else{
			
			mWebView.loadUrl("http://app.01teacher.cn/Attend/MyList");
		}

		return contentView;
	}
}
