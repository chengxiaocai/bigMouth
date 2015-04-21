package com.bigmouth.app.ui;

import com.bigmouth.app.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.bigmouth.app.ui.fragment.PractiseFragment;
import com.bigmouth.app.ui.fragment.ReadingFragment;
import com.bigmouth.app.ui.fragment.SettingFragment;
import com.bigmouth.app.ui.fragment.TempFragment;
import com.bigmouth.app.ui.fragment.WordsFragment;

public class StudyActivity extends FragmentActivity implements OnClickListener {

	FragmentTransaction transaction;
	ReadingFragment readingFramet;
	PractiseFragment practiseFragment;
	SettingFragment settingFragment;
	WordsFragment wordsFragment;
	TempFragment temFragment;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_study);
		
		initView();

	}

	private void initView() {
		
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		transaction = getSupportFragmentManager().beginTransaction();
		hideFragments(transaction);
		int id = v.getId();
		switch (id) {
		case R.id.rb_miantab_practise:
			Log.i("ccc", "1111");

			if (practiseFragment == null) {
				practiseFragment = new PractiseFragment();

				transaction.add(R.id.frag_main_tab, practiseFragment);
			} else {
				transaction.show(practiseFragment);
			}
			break;

		case R.id.rb_miantab_reading:
			Log.i("ccc", "2222");
			if (readingFramet == null) {
				readingFramet = new ReadingFragment();

				transaction.add(R.id.frag_main_tab, readingFramet);
			} else {
				transaction.show(readingFramet);
			}
			break;

		case R.id.rb_miantab_words:
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

	}

	

}