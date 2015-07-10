package com.bigmouth.app.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.bigmouth.app.R;
import com.bigmouth.app.bean.Words;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.DisplayUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author tusm
 *
 */
public class WordsFragment extends Fragment implements OnClickListener,
		SpeechSynthesizerListener {

	private WebView mWebView;
	private ListView lvWords;
	private GridView grWords;
	private int Color [] = new int[]{R.color.color1,R.color.color3,R.color.color4,R.color.color5,R.color.color6,R.color.color7,R.color.color8,R.color.color9,R.color.color10,R.color.color11,R.color.color12};
	
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
	private SpeechSynthesizer speechSynthesizer;

	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_words, container,
				false);
		initView();
		getWord();
		//contentView.setBackgroundColor(R.color.yellow);
		return contentView;
	}

	private void initView() {
		speechSynthesizer = new SpeechSynthesizer(getActivity()
				.getApplicationContext(), "holder", this);
		// 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
		speechSynthesizer.setApiKey("OsxkbrvCtRuxA6ptGMbAIQTF",
				"1UrKFZwaxAllo5uP9007QduXhkwyvvmG");
		speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.cc.getword");
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "请稍后！");
		inflater = LayoutInflater.from(getActivity());
		grWords = (GridView) contentView.findViewById(R.id.gv_words);
		grWords.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				view.setLayoutParams(new AbsListView.LayoutParams(((DisplayUtil.getWidth(getActivity())-160)/3)*2+40, ((DisplayUtil.getWidth(getActivity())-160)/3)*2+40));// 动态设置item的高度  

				// TODO Auto-generated method stub
//				final Dialog dialog = new Dialog(getActivity());
//				final TextView etWord;
//				final TextView etChinese;
//				dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//				dialog.setContentView(R.layout.item_words_dialog);
//				dialog.setTitle(null);
//				dialog.show();
//				TextView tvChinse = (TextView) dialog
//						.findViewById(R.id.tv_words_chinese);
//				tvChinse.setText(listWord.get(position).getChinese());
//
//				TextView tvUsa = (TextView) dialog
//						.findViewById(R.id.tv_words_usa);
//				tvUsa.setText(listWord.get(position).getWord());
//				dialog.findViewById(R.id.lound).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								new Thread(new Runnable() {
//
//									@Override
//									public void run() {
//										setParams();
//										int ret = speechSynthesizer
//												.speak(listWord.get(position)
//														.getWord());
//										if (ret != 0) {
//											Log.i("cc......", "hecheng faile!!");
//										}
//									}
//								}).start();
//							}
//						});
//
			}
		});
		adapter = new WordsAdapter(listWord);
		grWords.setAdapter(adapter);
		
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
	           convertView.setLayoutParams(new AbsListView.LayoutParams((DisplayUtil.getWidth(getActivity())-160)/3, (DisplayUtil.getWidth(getActivity())-160)/3));// 动态设置item的高度  

			}
			TextView tvChinse = (TextView) convertView
					.findViewById(R.id.tv_words_chinese);
			tvChinse.setText(listWord.get(position).getChinese());

			TextView tvUsa = (TextView) convertView
					.findViewById(R.id.tv_words_usa);
			tvUsa.setText(listWord.get(position).getWord());
			convertView.setBackgroundColor(getResources().getColor(Color[new Random().nextInt(11) ]));
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
				/*
				 * Words words = new Words(); words.setChinese(chinese);
				 * words.setWord(word); listWord.add(words);
				 * 
				 * adapter.notifyDataSetChanged();
				 */
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
				
					JSONArray array = obj.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {

						Words word = new Words();
						word.setChinese(array.getJSONObject(i).optString(
								"translation"));
						word.setId(array.getJSONObject(i).optString("id"));
						word.setWord(array.getJSONObject(i).optString("word"));
                        if(word.getChinese().length()>10){
                        	
                        }else{
                        	
                        	listWord.add(word);
                        }
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

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.cc.getword")) {
				getWord();
			}
		}

	};

	private void setParams() {
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE,
				SpeechSynthesizer.AUDIO_ENCODE_AMR);
		speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE,
				SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE,
		// SpeechSynthesizer.LANGUAGE_ZH);
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
		// speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
	}

	@Override
	public void onBufferProgressChanged(SpeechSynthesizer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(SpeechSynthesizer arg0, SpeechError arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewDataArrive(SpeechSynthesizer arg0, byte[] arg1,
			boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechFinish(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechPause(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechProgressChanged(SpeechSynthesizer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechResume(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeechStart(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartWorking(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}