package com.bigmouth.app.ui;

import com.bigmouth.app.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.bigmouth.app.ui.fragment.PractiseFragment;
import com.bigmouth.app.ui.fragment.ReadingDetailFragment;
import com.bigmouth.app.ui.fragment.ReadingFragment;
import com.bigmouth.app.ui.fragment.SettingFragment;
import com.bigmouth.app.ui.fragment.TempFragment;
import com.bigmouth.app.ui.fragment.WordsFragment;

public class StudyActivity extends FragmentActivity implements OnClickListener {

	FragmentTransaction transaction;
	ReadingFragment readingFramet;
	PractiseFragment practiseFragment;
	SettingFragment settingFragment;
	ReadingDetailFragment  readingDetailFragment;
	WordsFragment wordsFragment;
	TempFragment temFragment;
	private String text;
	private LinearLayout line;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_study);

		initView();

	}

	private void initView() {
		line = (LinearLayout) findViewById(R.id.rg_study_line);

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
		if(in==null){
			if (readingFramet == null) {
				readingFramet = new ReadingFragment();

				transaction.add(R.id.frag_main_tab, readingFramet);
			} else {
				transaction.show(readingFramet);
			}
			transaction.commit();
		}else{
			text  = in.getStringExtra("text");
			//if (readingDetailFragment == null) {
				readingDetailFragment = new  ReadingDetailFragment();
				

				transaction.add(R.id.frag_main_tab, readingDetailFragment);
			//} else {
		//		transaction.show(readingDetailFragment);
		//	}
			
			transaction.commit();
			
		}

	}
	public String getText(){
		 return text;
	}

}
