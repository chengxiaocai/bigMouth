package com.bigmouth.app.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigmouth.app.ui.fragment.PractiseFragment;
import com.bigmouth.app.ui.fragment.ReadingDetailFragment;
import com.bigmouth.app.ui.fragment.ReadingFragment;
import com.bigmouth.app.ui.fragment.SettingFragment;
import com.bigmouth.app.ui.fragment.TempFragment;
import com.bigmouth.app.ui.fragment.WordsFragment;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class StudyActivity extends FragmentActivity implements OnClickListener {
	private AsyncHttpClient ahc; // 异步处理
	private Dialog thisdialog;
	private JSONObject obj;
	FragmentTransaction transaction;
	ReadingFragment readingFramet;
	PractiseFragment practiseFragment;
	SettingFragment settingFragment;
	ReadingDetailFragment readingDetailFragment;
	WordsFragment wordsFragment;
	TempFragment temFragment;
	private String text;
	private LinearLayout line;
	private TextView tvWord, tvReading, tvPractise, tvSetting,tvNum;
    private String num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_study);

		initView();
		getNum();

	}

	private void initView() {
		ahc = new AsyncHttpClient();
		line = (LinearLayout) findViewById(R.id.rg_study_line);
		tvNum = (TextView) findViewById(R.id.rb_maintab_num);
		tvPractise = (TextView) findViewById(R.id.rb_miantab_practise);
		tvReading = (TextView) findViewById(R.id.rb_miantab_reading);
		tvSetting = (TextView) findViewById(R.id.rb_miantab_setting);
		tvWord = (TextView) findViewById(R.id.rb_miantab_words);
		transaction = getSupportFragmentManager().beginTransaction();
		findViewById(R.id.rb_miantab_words).setOnClickListener(this);

		findViewById(R.id.rb_miantab_reading).setOnClickListener(this);
		findViewById(R.id.rb_miantab_setting).setOnClickListener(this);
		findViewById(R.id.rb_miantab_practise).setOnClickListener(this);

		if (wordsFragment == null) {
			wordsFragment = new WordsFragment();
			transaction.add(R.id.frag_main_tab, wordsFragment);
		} else {
			transaction.show(wordsFragment);
		}
		transaction.commit();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		transaction = getSupportFragmentManager().beginTransaction();
		hideFragments(transaction);
		int id = v.getId();
		switch (id) {
		case R.id.rb_miantab_practise:
			tvPractise.setTextColor(getResources().getColor(R.color.gray));
			tvReading.setTextColor(getResources().getColor(R.color.yellow));

			tvSetting.setTextColor(getResources().getColor(R.color.yellow));
			tvWord.setTextColor(getResources().getColor(R.color.yellow));
			Log.i("ccc", "1111");
			line.setBackground(getResources().getDrawable(R.drawable.bg_3));

			if (practiseFragment == null) {
				practiseFragment = new PractiseFragment();

				transaction.add(R.id.frag_main_tab, practiseFragment);
			} else {
				transaction.show(practiseFragment);
			}
			break;

		case R.id.rb_miantab_reading:
			tvPractise.setTextColor(getResources().getColor(R.color.yellow));
			tvReading.setTextColor(getResources().getColor(R.color.gray));

			tvSetting.setTextColor(getResources().getColor(R.color.yellow));
			tvWord.setTextColor(getResources().getColor(R.color.yellow));
			Log.i("ccc", "2222");
			line.setBackground(getResources().getDrawable(R.drawable.bg_2));

			if (readingFramet == null) {
				readingFramet = new ReadingFragment();

				transaction.add(R.id.frag_main_tab, readingFramet);
			} else {
				transaction.show(readingFramet);
			}
			break;

		case R.id.rb_miantab_words:
			tvPractise.setTextColor(getResources().getColor(R.color.yellow));
			tvReading.setTextColor(getResources().getColor(R.color.yellow));

			tvSetting.setTextColor(getResources().getColor(R.color.yellow));
			tvWord.setTextColor(getResources().getColor(R.color.gray));
			line.setBackground(getResources().getDrawable(R.drawable.bg_1));
			Log.i("ccc", "3333");
			if (wordsFragment == null) {
				wordsFragment = new WordsFragment();
				
				transaction.add(R.id.frag_main_tab, wordsFragment);
			} else {
				transaction.show(wordsFragment);
			}
			break;

		case R.id.rb_miantab_setting:
			tvPractise.setTextColor(getResources().getColor(R.color.yellow));
			tvReading.setTextColor(getResources().getColor(R.color.yellow));

			tvSetting.setTextColor(getResources().getColor(R.color.gray));
			tvWord.setTextColor(getResources().getColor(R.color.yellow));
			Log.i("ccc", "44444");
			line.setBackground(getResources().getDrawable(R.drawable.bg_4));

			if (settingFragment == null) {
				settingFragment = new SettingFragment();
				transaction.add(R.id.frag_main_tab, settingFragment);
			} else {
				transaction.show(settingFragment);
			}
			break;

		}
		transaction.commit();

	}

	private void hideFragments(FragmentTransaction transaction) {

		if (readingFramet != null) {

			transaction.hide(readingFramet);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}

		if (wordsFragment != null) {
			transaction.hide(wordsFragment);
		}

		if (practiseFragment != null) {
			transaction.hide(practiseFragment);
		}
		if (readingDetailFragment != null) {
			transaction.hide(readingDetailFragment);
		}

	}

	public void changeReadingPage(Intent in) {
		transaction = getSupportFragmentManager().beginTransaction();
		hideFragments(transaction);
		if (in == null) {
			if (readingFramet == null) {
				readingFramet = new ReadingFragment();

				transaction.add(R.id.frag_main_tab, readingFramet);
			} else {
				transaction.show(readingFramet);
			}
			transaction.commit();
		} else {
			text = in.getStringExtra("text");
			// if (readingDetailFragment == null) {
			readingDetailFragment = new ReadingDetailFragment();

			transaction.add(R.id.frag_main_tab, readingDetailFragment);
			// } else {
			// transaction.show(readingDetailFragment);
			// }

			transaction.commit();

		}

	}

	public String getText() {
		return text;
	}
	public void getNum(){

		RequestParams rp = new RequestParams();
		rp.put("UserID",
				PersistentUtil.getInstance()
						.readString(this, "id", ""));
		
		 ahc.post("http://app.01teacher.cn/App/GetUserPoints",

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
					obj = new JSONObject(content);
					JSONObject data = obj.optJSONObject("data");
					tvNum.setText(data.optString("point"));
					
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
				// thisdialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.i("cc...cars", "failue.......");
				HttpHandle hh = new HttpHandle();
				hh.handleFaile(StudyActivity.this, arg3);
				
			}

		});
	}

}
