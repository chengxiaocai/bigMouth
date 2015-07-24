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
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public class StudyActivity1 extends FragmentActivity implements OnClickListener {
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
	RadioButton rbWord;
	private String text;
	private LinearLayout line;
	private TextView tvWord, tvReading, tvPractise, tvSetting, tvNum;
	private String num;
	public Boolean isReading=false;
	public Boolean isWord=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_study1);

		initView();
		getNum();

	}

	private void initView() {
		findViewById(R.id.iv_studty_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(StudyActivity1.this, MainAcitivity.class));
				if(isReading){
					readingFramet.SetReadListVisible();
					isReading=false;
					
				}else
				finish();
				
			}
		});
		findViewById(R.id.tv_title_addword).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Dialog dialog = new Dialog(StudyActivity1.this);
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
											Toast.makeText(StudyActivity1.this,
													"请输入翻译内容", 0).show();
											return;
										}
										if (TextUtils.isEmpty(etWord.getText()
												.toString().trim())) {
											Toast.makeText(StudyActivity1.this,
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
		rbWord = (RadioButton) findViewById(R.id.rb_miantab_words);
		rbWord.setChecked(true);
		ahc = new AsyncHttpClient();
		line = (LinearLayout) findViewById(R.id.rg_study_line);
		tvNum = (TextView) findViewById(R.id.tv_title_point);
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

			if (practiseFragment == null) {
				practiseFragment = new PractiseFragment();

				transaction.add(R.id.frag_main_tab, practiseFragment);
			} else {
				transaction.show(practiseFragment);
			}
			break;

		case R.id.rb_miantab_reading:

			if (readingFramet == null) {
				readingFramet = new ReadingFragment();

				transaction.add(R.id.frag_main_tab, readingFramet);
			} else {
				transaction.show(readingFramet);
			}
			break;

		case R.id.rb_miantab_words:

			if (wordsFragment == null) {
				wordsFragment = new WordsFragment();

				transaction.add(R.id.frag_main_tab, wordsFragment);
			} else {
				transaction.show(wordsFragment);
			}
			break;

		case R.id.rb_miantab_setting:

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

	public void getNum() {

		RequestParams rp = new RequestParams();
		rp.put("UserID", PersistentUtil.getInstance()
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
				hh.handleFaile(StudyActivity1.this, arg3);

			}

		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent in = new Intent(this, LoginActivity.class);
		startActivity(in);
		finish();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
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
				PersistentUtil.getInstance().readString(StudyActivity1.this,
						"id", ""));
		rp.put("Word", word);
		rp.put("Translation", chinese);
		// rp.put("ReadingID","");
		ahc.post("http://app.01teacher.cn/App/PostUserWords",

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
				Toast.makeText(StudyActivity1.this, "添加成功", 0).show();
				/*
				 * Words words = new Words(); words.setChinese(chinese);
				 * words.setWord(word); listWord.add(words);
				 * 
				 * adapter.notifyDataSetChanged();
				 */
				// getWord();

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
				hh.handleFaile(StudyActivity1.this, arg3);
				if (thisdialog.isShowing()) {
					// thisdialog.dismiss();
				}
			}

		});
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i("cc", ev.getY()+"");
		if(readingFramet!=null){
			
			readingFramet.y = ev.getY();
		}
		return super.dispatchTouchEvent(ev);
	}

}
