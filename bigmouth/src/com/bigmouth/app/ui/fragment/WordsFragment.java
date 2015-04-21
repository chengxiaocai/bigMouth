package com.bigmouth.app.ui.fragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.HttpHandle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class WordsFragment extends Fragment {

	private WebView mWebView;
	private ListView lvWords;
	private View contentView = null;
	LayoutInflater inflater = null;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_words, container,
				false);
		initView();
		return contentView;
	}

	private void initView() {
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "请稍后！");
		inflater = LayoutInflater.from(getActivity());
		lvWords = (ListView) contentView.findViewById(R.id.lv_words_list);
		lvWords.setAdapter(new WordsAdapter());
		contentView.findViewById(R.id.tv_words_addword).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Dialog dialog = new Dialog(getActivity());
						final EditText etWord;
						final EditText etChinese;
						dialog.getWindow().requestFeature(
								Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.item_dialog_addword);
						dialog.setTitle(null);
						dialog.show();
						etChinese = (EditText) dialog
								.findViewById(R.id.et_chiniese);
						etWord = (EditText) dialog.findViewById(R.id.et_word);
						dialog.findViewById(R.id.btn_dialog_word_addword_ok)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										if (TextUtils.isEmpty(etChinese
												.getText().toString().trim())) {
											Toast.makeText(getActivity(),
													"请输入翻译内容", 0).show();
											return;
										}
										if (TextUtils.isEmpty(etWord.getText()
												.toString().trim())) {
											Toast.makeText(getActivity(),
													"请输入单词", 0).show();
											return;
										}
										dialog.dismiss();
										addWord(etWord.getText().toString()
												.trim(), etChinese.getText()
												.toString().trim());
									}
								});
						dialog.findViewById(R.id.btn_dialog_word_addword_no)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});

					}
				});

	}

	private class WordsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_words, null);
			}
			return convertView;
		}

	}

	public void addWord(String word, String chinese) {

//		RequestParams rp = new RequestParams();
//        rp.put("", file);
//		reqhandle = ahc.post("http://app.01teacher.cn/App/PostUserWords",
//				reqhandle = ahc.post("http://app.01teacher.cn/App/GetReadings?count=1",
//				rp, new AsyncHttpResponseHandler() {
//					@Override
//					public void onStart() {
//						// TODO Auto-generated method stub
//						super.onStart();
//						Log.i("cc...cars", "start...");
//						thisdialog.show();
//					}
//
//					@Override
//					public void onSuccess(String content) {
//						// TODO Auto-generated method stub
//						super.onSuccess(content);
//						Log.i("cc...cars", "success.......");
//
//					}
//
//					@Override
//					public void onFinish() {
//						// TODO Auto-generated method stub
//						super.onFinish();
//						Log.i("cc...", "finish");
//						thisdialog.dismiss();
//					}
//
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//							Throwable arg3) {
//						// TODO Auto-generated method stub
//						super.onFailure(arg0, arg1, arg2, arg3);
//						Log.i("cc...cars", "failue.......");
//						HttpHandle hh = new HttpHandle();
//						hh.handleFaile(getActivity(), arg3);
//						if (thisdialog.isShowing()) {
//							thisdialog.dismiss();
//						}
//					}
//
//				});
	}
}