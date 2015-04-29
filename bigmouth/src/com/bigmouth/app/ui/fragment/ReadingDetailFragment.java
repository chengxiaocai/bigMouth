package com.bigmouth.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.bigmouth.app.R;
import com.bigmouth.app.bean.Means;
import com.bigmouth.app.bean.Readings;
import com.bigmouth.app.ui.StudyActivity;
import com.bigmouth.app.util.Constant;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView.BufferType;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadingDetailFragment extends Fragment implements OnClickListener,
		SpeechSynthesizerListener {

	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;
	private ArrayList<Readings> readList = new ArrayList<Readings>();
	LayoutInflater inflater = null;
	private ListView lvReading;
	private View contentView;
	private ArrayList<String> allWords = new ArrayList<String>();
	private TextView tvText;
	private StudyActivity ac;
   private String strTransWords;
	private TextView tvSrcWord, tvResultWrod;
	private LinearLayout line;
	private ImageView btnLound;
	private SpeechSynthesizer speechSynthesizer;
	private ArrayList<Means> meanList = new ArrayList<Means>();
	private TextView tvWordType, tvWordeFirst;
	private LinearLayout llTranslaitonMore;
	private GridView grid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_reading_detail,
				container, false);
		// initView();
		// getReading();
		grid = (GridView) contentView.findViewById(R.id.grid_word);
		tvWordType = (TextView) contentView
				.findViewById(R.id.tv_result_word_type);
		// llTranslaitonMore = (LinearLayout)
		// contentView.findViewById(R.id.ll_translation_more);
		tvWordeFirst = (TextView) contentView.findViewById(R.id.tv_word_first);
		tvText = (TextView) contentView.findViewById(R.id.tv_read_content);
		// tvText.setText(ac.getText());
		speechSynthesizer = new SpeechSynthesizer(getActivity()
				.getApplicationContext(), "holder", this);
		// 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
		speechSynthesizer.setApiKey("OsxkbrvCtRuxA6ptGMbAIQTF",
				"1UrKFZwaxAllo5uP9007QduXhkwyvvmG");
		speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

		btnLound = (ImageView) contentView.findViewById(R.id.lound);
		btnLound.setOnClickListener(this);

		line = (LinearLayout) contentView.findViewById(R.id.line_read);
		tvResultWrod = (TextView) contentView.findViewById(R.id.tv_result_word);
		tvSrcWord = (TextView) contentView.findViewById(R.id.tv_src_word);
		tvText.setText(ac.getText(), BufferType.SPANNABLE);
		getEachWord(tvText);
		tvText.setMovementMethod(LinkMovementMethod.getInstance());
		ahc = new AsyncHttpClient();
		contentView.findViewById(R.id.iv_readtail_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ac.changeReadingPage(null);
					}
				});
		contentView.findViewById(R.id.iv_readdetial_share).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShareSDK.initSDK(getActivity());
				OnekeyShare oks = new OnekeyShare();
				// 关闭sso授权
				oks.disableSSOWhenAuthorize();

				// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
				// oks.setNotification(R.drawable.ic_launcher,
				// getString(R.string.app_name));
				// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
				
				// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
				
				// text是分享文本，所有平台都需要这个字段
				oks.setText(ac.getText().toString());
				// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			
				// url仅在微信（包括好友和朋友圈）中使用
			
              
				// 启动分享GUI
				oks.show(getActivity());
			}
		});
		return contentView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		ac = (StudyActivity) activity;
	}

	public void getEachWord(TextView textView) {
		Spannable spans = (Spannable) textView.getText();
		Integer[] indices = getIndices(textView.getText().toString().trim(),
				' ');
		int start = 0;
		int end = 0;
		// to cater last/only word loop will run equal to the length of
		// indices.length
		for (int i = 0; i <= indices.length; i++) {
			ClickableSpan clickSpan = getClickableSpan();
			// to cater last/only word
			end = (i < indices.length ? indices[i] : spans.length());
			spans.setSpan(clickSpan, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = end + 1;
		}
		// 改变选中文本的高亮颜色
		textView.setHighlightColor(Color.TRANSPARENT);
		tvText.setTextColor(Color.RED);

	}

	private ClickableSpan getClickableSpan() {
		return new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				try {
					TextView tv = (TextView) widget;
					tv.setTextColor(Color.RED);
					strTransWords = tv
							.getText()
							.subSequence(tv.getSelectionStart(),
									tv.getSelectionEnd()).toString();
					Log.d(tv.getSelectionStart() + "", tv.getSelectionEnd()
							+ "");
					SpannableStringBuilder style = new SpannableStringBuilder(
							tv.getText().toString());
					style.setSpan(new BackgroundColorSpan(getResources()
							.getColor(R.color.green)), tv.getSelectionStart(),
							tv.getSelectionEnd(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

					tv.setText(style);

					getEachWord(tvText);

					Log.d("tapped on:", strTransWords);
					Load(strTransWords);
					// Toast.makeText(getActivity(), s, 0).show();

				} catch (Exception e) {
					// TODO: handle exception
					Log.i("cc", "hahahfhahdfiaofhioehfieo");
					Toast.makeText(getActivity(), "haha", 0).show();
				}

			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setColor(Color.BLACK);
				ds.setUnderlineText(false);
			}
		};
	}

	public static Integer[] getIndices(String s, char c) {
		int pos = s.indexOf(c, 0);
		List<Integer> indices = new ArrayList<Integer>();
		while (pos != -1) {
			indices.add(pos);
			pos = s.indexOf(c, pos + 1);
		}
		return (Integer[]) indices.toArray(new Integer[0]);
	}

	public void Study() {

		if (line.getVisibility() == View.VISIBLE) {
			// AlphaAnimation animation = new
			// AlphaAnimation(1.0F, 0F);
			// TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
			// -400);
			// animation.setDuration(100);
			// animation.start();
			// line.setAnimation(animation);
			line.setVisibility(View.GONE);

		} else {
			/*
			 * TranslateAnimation tra= new TranslateAnimation(0, 0, 0.0F, 1.0F);
			 * tra.setDuration(5000); //tra.start(); tv.setAnimation(tra);
			 * tv.startAnimation(tra);
			 */
			// TranslateAnimation animation = new TranslateAnimation(0, 0, -400,
			// 0);
			// animation.setDuration(100);
			// animation.start();
			// line.setAnimation(animation);

			line.setVisibility(View.VISIBLE);
		}

		// if (tv.VISIBLE==View.INVISIBLE) {
		// tv.setVisibility(View.VISIBLE);

		// }

	}

	public void Load(String word) {
		if (word.contains(",")) {
			int index = word.indexOf(",");
			word = word.substring(0, index);
		}
		if (word.contains(".")) {
			int index = word.indexOf(".");
			word = word.substring(0, index);
		}
		tvResultWrod.setText(word);

		RequestParams rp = new RequestParams();
		rp.put("client_id", Constant.BAIDU_APP_KEY);
		rp.put("q", word);
		rp.put("from", "en");
		rp.put("to", "zh");
		// rp.put("ReadingID","");
		reqhandle = ahc.get(
				"http://openapi.baidu.com/public/2.0/translate/dict/simple",

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
						try {
							JSONObject obj1 = new JSONObject(content);
							JSONObject data = obj1.optJSONObject("data");
							JSONArray array = data.getJSONArray("symbols");
							JSONObject obj2 = array.getJSONObject(0);
							JSONArray mean = obj2.getJSONArray("parts");
							meanList.clear();
							for (int i = 0; i < mean.length(); i++) {
								Means means = new Means();
								means.setPart(mean.getJSONObject(i).optString(
										"part"));
								JSONArray arr = mean.getJSONObject(i)
										.optJSONArray("means");
								for (int j = 0; j < arr.length(); j++) {
									String isLong = arr.getString(j);
									// if(isLong.length()<10){

									means.getList().add(isLong);
									// }
								}
								meanList.add(means);
							}
							UpdateUi();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							line.setVisibility(View.GONE);
							Toast.makeText(getActivity(), "获取失败", 0).show();
							tvSrcWord.setText("获取失败");
							tvWordType.setText("");

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

					}

				});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				setParams();
				int ret = speechSynthesizer.speak(tvResultWrod.getText()
						.toString());
				if (ret != 0) {
					Log.i("cc......", "hecheng faile!!");
				}
			}
		}).start();
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

	public void UpdateUi() {
		
		String type;
		Boolean isHave = false;
		Boolean isMore = false;
		String finaleWord = null;
		allWords.clear();
		// llTranslaitonMore.removeAllViews();

		for (int i = 0; i < meanList.size(); i++) {
			if (isHave) {
				break;
			}

			Means mean = meanList.get(i);
			for (int j = 0; j < mean.getList().size(); j++) {
				finaleWord = mean.getList().get(j);
				if (finaleWord.length() < 10) {
					tvSrcWord.setText(finaleWord);
					tvWordeFirst.setText(finaleWord);
					isHave = true;
					type = mean.getPart();
					if ("n.".equals(type))
						tvWordType.setText("(名词)");

					if ("pron.".equals(type))
						tvWordType.setText("(代词)");

					if ("adj.".equals(type))
						tvWordType.setText("(形容词)");

					if ("num.".equals(type))
						tvWordType.setText("(数词)");

					if ("art.".equals(type))
						tvWordType.setText("(冠词)");

					if ("prep.".equals(type))
						tvWordType.setText("(介词)");

					if ("conj.".equals(type))
						tvWordType.setText("(连词)");

					if ("interj.".equals(type))
						tvWordType.setText("(感叹词)");
					break;
				}

				// TextView tvView = new TextView(getActivity());
				// tvView.setText(mean.getList().get(j));
				// llTranslaitonMore.addView(tvView);

			}
           upLoadWord(finaleWord, strTransWords);
			Study();

		}
		for (int p = 0; p < meanList.size(); p++) {
			Means mean1 = meanList.get(p);

			if (isMore) {
				break;
			}
			for (int q = 0; q < mean1.getList().size(); q++) {

				String word = mean1.getList().get(q);
				if (allWords.size()>5) {

					isMore = true;
					break;
				}
				if (word.length() < 5) {
					if (!word.equals(finaleWord)) {

						allWords.add(word);
					}
				}
			}
		}
		grid.setAdapter(new Mydapater());
	}

	private class Mydapater extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allWords.size();
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
				LayoutInflater li = LayoutInflater.from(getActivity());
				convertView = li.inflate(R.layout.item_grid_extra, null);

			}
			TextView tvExtraWord = (TextView) convertView
					.findViewById(R.id.tv_word_extra);
			tvExtraWord.setText(allWords.get(position));
			return convertView;
		}

	}
	public void upLoadWord(String chinese,String usa){
		if(chinese==null||usa==null){
			return;
		}
		RequestParams rp = new RequestParams();
		rp.put("UserID",
				PersistentUtil.getInstance()
						.readString(getActivity(), "id", ""));
		rp.put("Word", usa);
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
				Intent mIntent = new Intent("com.cc.getword");  
                
                  
                //发送广播  
                getActivity().sendBroadcast(mIntent); 

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
