package com.bigmouth.app.ui;

import com.bigmouth.app.R;

import android.app.Activity;
import android.content.Context;
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

public class MainAcitivity extends FragmentActivity implements OnClickListener {
	private TitlePopup titlePopup;
	private ImageButton titleBtn;
	FragmentTransaction transaction;
	IndexFragment inviteFramet;
	CalenderFragment meFragment;
	ClassTimeFragment playerFragment;
	ClassRecordFragment playingFragment;
	TempFragment temFragment;
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

		setContentView(R.layout.activity_main);
		 init();
		initView();

	}

	private void initView() {
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
				/*startActivity(new Intent(MainAcitivity.this,
						DialogActivity.class));*/
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
			Log.i("ccc","1111");
			Intent in = new Intent(this,LoginActivity.class);
			startActivity(in);
			finish();
			/*if (inviteFramet == null) {
				inviteFramet = new IndexFragment();

				transaction.add(R.id.frag_main_tab, inviteFramet);
			} else {
				transaction.show(inviteFramet);
			}*/
			break;

		case R.id.rb_miantab_me:
			Log.i("ccc","2222");
			if (meFragment == null) {
				meFragment = new CalenderFragment();

				transaction.add(R.id.frag_main_tab, meFragment);
			} else {
				transaction.show(meFragment);
			}
			break;

		case R.id.rb_miantab_player:
			Log.i("ccc","3333");
			if (playerFragment == null) {
				playerFragment = new ClassTimeFragment();
				transaction.add(R.id.frag_main_tab, playerFragment);
			} else {
				transaction.show(playerFragment);
			}
			break;

		case R.id.rb_miantab_playing:
			Log.i("ccc","44444");
			if (playingFragment == null) {
				playingFragment = new ClassRecordFragment();
				transaction.add(R.id.frag_main_tab, playingFragment);
			} else {
				transaction.show(playingFragment);
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
	private void init(){
		
		
		
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,this);
		titlePopup.addAction(new ActionItem(this, "分享", R.drawable.mm_title_btn_receiver_normal));
		titlePopup.addAction(new ActionItem(this, "扫描", R.drawable.mm_title_btn_set_normal));
		titlePopup.addAction(new ActionItem(this, "阅读", R.drawable.mm_title_btn_share_normal));
	}

}
