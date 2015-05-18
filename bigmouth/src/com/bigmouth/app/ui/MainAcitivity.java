package com.bigmouth.app.ui;

import com.bigmouth.app.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.bigmouth.app.ui.fragment.IndexFragment;
import com.bigmouth.app.ui.fragment.CalenderFragment;
import com.bigmouth.app.ui.fragment.ClassTimeFragment;
import com.bigmouth.app.ui.fragment.ClassRecordFragment;
import com.bigmouth.app.ui.fragment.TempFragment;
import com.bigmouth.app.util.PersistentUtil;

public class MainAcitivity extends FragmentActivity implements OnClickListener {
	private TitlePopup titlePopup;
	private ImageButton titleBtn;
	FragmentTransaction transaction;
	IndexFragment inviteFramet;
	CalenderFragment meFragment;
	ClassTimeFragment playerFragment;
	ClassRecordFragment playingFragment;
	TempFragment temFragment;
	private RadioButton rbMain, rbTime, rbRecord, rbCal;
	private String type;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		type = PersistentUtil.getInstance().readString(this, "type", "0");
		if ("2".equals(type)) {
			setContentView(R.layout.activity_main_teacther);

		} else {

			setContentView(R.layout.activity_main);
		}
		init();
		initView();

	}

	private void initView() {
		if ("2".equals(type)) {

			rbCal = (RadioButton) findViewById(R.id.rb_miantab_me);
			rbCal.setText("Calendar");
			rbMain = (RadioButton) findViewById(R.id.rb_miantab_invite);
			rbMain.setText("Home");
			rbRecord = (RadioButton) findViewById(R.id.rb_miantab_player);
			rbRecord.setText("Message");

			rbTime = (RadioButton) findViewById(R.id.rb_miantab_playing);

			rbTime.setText("Student");
		}
		url = getIntent().getStringExtra("url");
		transaction = getSupportFragmentManager().beginTransaction();
		findViewById(R.id.rb_miantab_invite).setOnClickListener(this);
		RadioButton rb_invite = (RadioButton) findViewById(R.id.rb_miantab_invite);
		rb_invite.setChecked(true);
		findViewById(R.id.rb_miantab_me).setOnClickListener(this);
		findViewById(R.id.rb_miantab_player).setOnClickListener(this);
		findViewById(R.id.rb_miantab_playing).setOnClickListener(this);

		titleBtn = (ImageButton) findViewById(R.id.title_btn);
		titleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * startActivity(new Intent(MainAcitivity.this,
				 * DialogActivity.class));
				 */
				titlePopup.show(v);
			}
		});

		if (temFragment == null) {
			temFragment = new TempFragment();
			transaction.add(R.id.frag_main_tab, temFragment);
		} else {
			transaction.show(temFragment);
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
		case R.id.rb_miantab_invite:
			Log.i("ccc", "1111");
			Intent in = new Intent(this, LoginActivity.class);
			startActivity(in);
			finish();
			/*
			 * if (inviteFramet == null) { inviteFramet = new IndexFragment();
			 * 
			 * transaction.add(R.id.frag_main_tab, inviteFramet); } else {
			 * transaction.show(inviteFramet); }
			 */
			break;

		case R.id.rb_miantab_me:
			Log.i("ccc", "2222");
			if (meFragment == null) {
				meFragment = new CalenderFragment();

				transaction.add(R.id.frag_main_tab, meFragment);
			} else {
				transaction.show(meFragment);
			}
			break;

		case R.id.rb_miantab_player:
			Log.i("ccc", "3333");
			if (playingFragment == null) {
				playingFragment = new ClassRecordFragment();
				transaction.add(R.id.frag_main_tab, playingFragment);
			} else {
				transaction.show(playingFragment);
			}
			break;

		case R.id.rb_miantab_playing:
			Log.i("ccc", "44444");

			if (playerFragment == null) {
				playerFragment = new ClassTimeFragment();
				transaction.add(R.id.frag_main_tab, playerFragment);
			} else {
				transaction.show(playerFragment);
			}

			break;

		}
		transaction.commit();

	}

	private void hideFragments(FragmentTransaction transaction) {

		if (inviteFramet != null) {
			transaction.hide(inviteFramet);
		}
		if (temFragment != null) {
			transaction.hide(temFragment);
		}

		if (meFragment != null) {
			transaction.hide(meFragment);
		}

		if (playerFragment != null) {
			transaction.hide(playerFragment);
		}

		if (playingFragment != null) {
			transaction.hide(playingFragment);
		}
	}

	public void init() {
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, this);
		titlePopup.addAction(new ActionItem(this, "分享",
				R.drawable.mm_title_btn_receiver_normal));
		if ("2".equals(type)) {

			titlePopup.addAction(new ActionItem(this, "扫描",
					R.drawable.mm_title_btn_set_normal));
		}
		if ("1".equals(type)) {

			titlePopup.addAction(new ActionItem(this, "阅读",
					R.drawable.mm_title_btn_share_normal));
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}

		String returnUrl = data.getStringExtra("result");
		if (returnUrl != null) {
			url = returnUrl;
			transaction = getSupportFragmentManager().beginTransaction();
			hideFragments(transaction);
			//if (temFragment == null) {
				temFragment = new TempFragment();
				transaction.add(R.id.frag_main_tab, temFragment);
		//	} else {
				transaction.show(temFragment);
		//	}
			transaction.commit();
		}
	}

}
