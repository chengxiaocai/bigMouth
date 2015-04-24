package com.bigmouth.app.ui.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;
import com.bigmouth.app.bean.Words;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author tusm
 *
 */
public class WordsFragment extends Fragment {

	private WebView mWebView;
	private ListView lvWords;
	private View contentView = null;
	LayoutInflater inflater = null;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;
	private String strWordNum; // 返回单词数量
	private ArrayList<Words> listWord = new ArrayList<Words>(); // 存放获取的单词
	private WordsAdapter adapter;
	private TextView tvWordNum; // 显示返回的单词数目

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_words, container,
				false);
		initView();
		getWord();
		return contentView;
	}

	private void initView() {
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "请稍后！");
		inflater = LayoutInflater.from(getActivity());
		tvWordNum = (TextView) contentView
				.findViewById(R.id.tv_words_show_word_num);
		lvWords = (ListView) contentView.findViewById(R.id.lv_words_list);
		adapter = new WordsAdapter(listWord);
		lvWords.setAdapter(adapter);
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
		ArrayList<Words> listWord;

		public WordsAdapter(ArrayList<Words> listWord) {
			super();
			this.listWord = listWord;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listWord.size();
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
			if (listWord.size() < 1) {
				return null;
			}
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_words, null);
				TextView tvChinse = (TextView) convertView
						.findViewById(R.id.tv_words_chinese);
				tvChinse.setText(listWord.get(position).getChinese());

				TextView tvUsa = (TextView) convertView
						.findViewById(R.id.tv_words_usa);
				tvUsa.setText(listWord.get(position).getWord());
			}
			return convertView;
		}

	}

	/**
	 * 添加单词
	 * 
	 * @param word
	 *            单词
	 * @param chinese
	 *            翻译
	 */
	public void addWord(final String word, final String chinese) {

		RequestParams rp = new RequestParams();
		rp.put("UserID",
				PersistentUtil.getInstance()
						.readString(getActivity(), "id", ""));
		rp.put("Word", word);
		rp.put("Translation", chinese);
		// rp.put("ReadingID","");
		reqhandle = ahc.post("http://app.01teacher.cn/App/PostUserWords",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...cars", "start...");
				// thisdialog.show();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.i("cc...cars", "success.......");
				Toast.makeText(getActivity(), "添加成功", 0).show();
				getWord();

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.i("cc...", "finish");
				// thisdialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.i("cc...cars", "failue.......");
				HttpHandle hh = new HttpHandle();
				hh.handleFaile(getActivity(), arg3);
				if (thisdialog.isShowing()) {
					// thisdialog.dismiss();
				}
			}

		});
	}

	/**
	 * 
	 * 获取单词
	 */
	public void getWord() {

		listWord.clear();
		RequestParams rp = new RequestParams();
		rp.put("UserID",
				PersistentUtil.getInstance()
						.readString(getActivity(), "id", ""));

		reqhandle = ahc.post("http://app.01teacher.cn/App/GetUserWords",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...cars", "start...");
				// thisdialog.show();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.i("cc...cars", "success.......");
				// Toast.makeText(getActivity(), "添加成功", 0).show();
				try {
					obj = new JSONObject(content);
					strWordNum = obj.optString("count");
					tvWordNum.setVisibility(View.VISIBLE);
					tvWordNum.setText(strWordNum + "收集的单词");
					JSONArray array = obj.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {

						Words word = new Words();
						word.setChinese(array.getJSONObject(i).optString(
								"translation"));
						word.setId(array.getJSONObject(i).optString("id"));
						word.setWord(array.getJSONObject(i).optString("word"));

						listWord.add(word);
					}
					
					adapter.notifyDataSetChanged();

				} catch (JSONException e) {
					Toast.makeText(getActivity(), "获取单词列表失败", 0).show();
					e.printStackTrace();
				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.i("cc...", "finish");
				// thisdialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.i("cc...cars", "failue.......");
				HttpHandle hh = new HttpHandle();
				hh.handleFaile(getActivity(), arg3);
				if (thisdialog.isShowing()) {
					// thisdialog.dismiss();
				}
			}

		});
	}
}