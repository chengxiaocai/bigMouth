package com.bigmouth.app.ui;

import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.bigmouth.app.R;
import com.bigmouth.app.bean.Means;
import com.bigmouth.app.util.Constant;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.EncryptUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangyu
 */
public class DialogBaiduFanyiActivity extends Activity implements
		SpeechSynthesizerListener {
	private String word;
	private ImageView btnLound;

	private TextView tvEnglish, tvChinese;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private SpeechSynthesizer speechSynthesizer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_baidufanyi);

		initView();
	}

	/**
	 * 
	 */
	private void initView() {
		btnLound = (ImageView) findViewById(R.id.lound);
		btnLound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						setParams();
						int ret = speechSynthesizer.speak(word);
						if (ret != 0) {
							Log.i("cc......", "hecheng faile!!");
						}
					}
				}).start();
			}
		});

		speechSynthesizer = new SpeechSynthesizer(this.getApplicationContext(),
				"holder", this);
		// 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
		speechSynthesizer.setApiKey("OsxkbrvCtRuxA6ptGMbAIQTF",
				"1UrKFZwaxAllo5uP9007QduXhkwyvvmG");
		speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(this, "");

		tvEnglish = (TextView) findViewById(R.id.tv_baidu_eng);
		tvChinese = (TextView) findViewById(R.id.tv_baidu_chinese);
		word = getIntent().getStringExtra("word");
		tvEnglish.setText(word);
		findViewById(R.id.iv_baidu_down).setOnClickListener(
				new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						overridePendingTransition(R.anim.push_top_out, 0);
						finish();

					}
				});
		Load(word);

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

	public void Load(final String word) {
        int salt = new Random().nextInt(1000000);

		RequestParams rp = new RequestParams();
		rp.put("appid", Constant.BAIDU_TRANSLATE_APPID);
		rp.put("q", word);
		rp.put("from", "en");
		rp.put("to", "zh");
		rp.put("salt", salt+"");
		  String  sign1 =Constant.BAIDU_TRANSLATE_APPID+word+salt+""+Constant.BAIDU_TRANSLATE_SECRET;
	      String sign = EncryptUtil.md5(sign1);
	     rp.put("sign", sign);
		reqhandle = ahc.get(
     //20151130000007000South4978164dd35boO7YiCSu1c4Kq8
		"http://api.fanyi.baidu.com/api/trans/vip/translate",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...translant", "start...");
				thisdialog.show();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.i("cc...cars", "success.......");
				try {
					JSONObject obj1 = new JSONObject(content);
					JSONArray arrya = obj1.optJSONArray("trans_result");
					StringBuffer str = new StringBuffer();
					for (int i = 0; i < arrya.length(); i++) {
						JSONObject obj = arrya.optJSONObject(i);
						str.append(obj.optString("dst") + " ");
					}
					tvChinese.setText(str.toString());
					upLoadWord(str.toString(), word);

				}

				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();

				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.i("cc...", "finish");
				thisdialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.i("cc...cars", "failue.......");
				HttpHandle hh = new HttpHandle();
				hh.handleFaile(DialogBaiduFanyiActivity.this, arg3);

			}

		});

	}

	public void upLoadWord(String chinese, String usa) {
		if (chinese == null || usa == null) {
			return;
		}
		RequestParams rp = new RequestParams();
		rp.put("UserID",
				PersistentUtil.getInstance().readString(
						DialogBaiduFanyiActivity.this, "id", ""));
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
				Toast.makeText(DialogBaiduFanyiActivity.this, "陌生单词，已添加到单词库", 0)
						.show();
				UpLoadPoint();
				Intent mIntent = new Intent("com.cc.getword");

				// 发送广播
				sendBroadcast(mIntent);

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
				hh.handleFaile(DialogBaiduFanyiActivity.this, arg3);
				if (thisdialog.isShowing()) {
					// thisdialog.dismiss();
				}
			}

		});

	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}


		public void UpLoadPoint() {
			
			RequestParams rp = new RequestParams();
			rp.put("UserID",PersistentUtil.getInstance()
							.readString(this, "id", ""));
			rp.put("Type", "2");
			reqhandle = ahc.post("http://app.01teacher.cn/App/PostUserPoints",

			rp, new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					Log.i("cc...cars", "start...");
					thisdialog.show();
				}

				@Override
				public void onSuccess(String content) {
					// TODO Auto-generated method stub
					super.onSuccess(content);
					Log.i("cc", content);
					try {
						JSONObject obj = new JSONObject(content);
						if (obj.optBoolean("success")) {
							Intent mIntent = new Intent("com.cc.getnum");

							// 发送广播
							sendBroadcast(mIntent);

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					Log.i("cc...", "finish");
					if (thisdialog.isShowing()) {
						thisdialog.dismiss();
					}

				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					super.onFailure(arg0, arg1, arg2, arg3);
					Log.i("cc...cars", "failue.......");
					HttpHandle hh = new HttpHandle();
					hh.handleFaile(DialogBaiduFanyiActivity.this, arg3);

				}

			});

		}


}
