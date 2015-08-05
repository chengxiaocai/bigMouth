package com.bigmouth.app.ui;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.bigmouth.app.R;
import com.bigmouth.app.bean.Means;
import com.bigmouth.app.util.Constant;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.DisplayUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangyu
 */
public class ShowWordsActivity extends Activity implements
		SpeechSynthesizerListener {
	private String word, chinese;
	private int color;
	private ImageView btnLound;
	private RelativeLayout reShowWord;
	private TextView tvEnglish, tvChinese;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private int x, y,bar;
	private View view;
	private SpeechSynthesizer speechSynthesizer;
	private int Color[] = new int[] { R.color.color1, R.color.color3,
			R.color.color4, R.color.color5, R.color.color6, R.color.color7,
			R.color.color8, R.color.color9, R.color.color10, R.color.color11,
			R.color.color12 };
	private RelativeLayout reWordContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_words_dialog);

		initView();

	}

	/**
	 * 
	 */
	private void initView() {
		reWordContainer = (RelativeLayout) findViewById(R.id.re_show_word_container);
		x = getIntent().getIntExtra("x", 0);
		y = getIntent().getIntExtra("y", 0);
		bar= getIntent().getIntExtra("bar", 0);
		view = getLayoutInflater().from(this).inflate(
				R.layout.item_baidu_voice, null);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(((DisplayUtil
				.getWidth(this) - 160) / 3) * 2 + 40, ((DisplayUtil
				.getWidth(this) - 160) / 3) * 2 + 40); 

//		view.setLayoutParams(new AbsListView.LayoutParams(((DisplayUtil
//				.getWidth(this) - 160) / 3) * 2 + 40, ((DisplayUtil
//				.getWidth(this) - 160) / 3) * 2 + 40));// 动态设置item的高度
		layoutParams.topMargin=y-bar;
		if(x>((DisplayUtil.getWidth(this) - 160) / 3) * 2 + 80){
			x=x-(((DisplayUtil.getWidth(this) - 160) / 3) + 40);
		}
		layoutParams.leftMargin=x;
		reWordContainer.addView(view,layoutParams);
		reShowWord = (RelativeLayout) findViewById(R.id.re_show_word);
		chinese = getIntent().getStringExtra("chinese");
		word = getIntent().getStringExtra("word");
		color = getIntent().getIntExtra("color", 1);
		
		//setLayoutY(view, y);
		//setLayout(view, x, y);
		view.setBackgroundResource(Color[color]);
		TextView tvChinse = (TextView) view.findViewById(R.id.tv_words_chinese);
		tvChinse.setText(chinese);

		TextView tvUsa = (TextView) view.findViewById(R.id.tv_words_usa);
		tvUsa.setText(word);
		btnLound = (ImageView) view.findViewById(R.id.lound);
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
		findViewById(R.id.left_up).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		speechSynthesizer = new SpeechSynthesizer(this.getApplicationContext(),
				"holder", this);
		// 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
		speechSynthesizer.setApiKey("OsxkbrvCtRuxA6ptGMbAIQTF",
				"1UrKFZwaxAllo5uP9007QduXhkwyvvmG");
		speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

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
	public static void setLayoutY(View view,int y) 
	{ 
	MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams()); 
	margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height); 
	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin); 
	view.setLayoutParams(layoutParams); 
	} 
	/* 
	* 设置控件所在的位置YY，并且不改变宽高， 
	* XY为绝对位置 
	*/ 
	public static void setLayout(View view,int x,int y) 
	{ 
	MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams()); 
	margin.setMargins(x,y, x+margin.width, y+margin.height); 
	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin); 
	view.setLayoutParams(layoutParams); 
	} 
	

}
