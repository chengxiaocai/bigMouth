package com.bigmouth.app.ui;

import com.bigmouth.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.bigmouth.app.R;
import com.bigmouth.app.ui.fragment.InviteFragment;
import com.bigmouth.app.ui.fragment.MeFragment;
import com.bigmouth.app.ui.fragment.PlayerFragment;
import com.bigmouth.app.ui.fragment.PlayingFragment;

public class MainAcitivity extends FragmentActivity implements OnClickListener {
	private TitlePopup titlePopup;
	private ImageButton titleBtn;
	FragmentTransaction transaction;
	InviteFragment inviteFramet;
	MeFragment meFragment;
	PlayerFragment playerFragment;
	PlayingFragment playingFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		 init();
		initView();

	}

	private void initView() {

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

		if (inviteFramet == null) {
			inviteFramet = new InviteFragment();

			transaction.add(R.id.frag_main_tab, inviteFramet);
		} else {
			transaction.show(inviteFramet);
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
			if (inviteFramet == null) {
				inviteFramet = new InviteFragment();

				transaction.add(R.id.frag_main_tab, inviteFramet);
			} else {
				transaction.show(inviteFramet);
			}
			break;

		case R.id.rb_miantab_me:
			if (meFragment == null) {
				meFragment = new MeFragment();

				transaction.add(R.id.frag_main_tab, meFragment);
			} else {
				transaction.show(meFragment);
			}
			break;

		case R.id.rb_miantab_player:
			if (playerFragment == null) {
				playerFragment = new PlayerFragment();
				transaction.add(R.id.frag_main_tab, playerFragment);
			} else {
				transaction.show(playerFragment);
			}
			break;

		case R.id.rb_miantab_playing:
			if (playingFragment == null) {
				playingFragment = new PlayingFragment();
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
		titlePopup.addAction(new ActionItem(this, "测试3", R.drawable.mm_title_btn_share_normal));
		titlePopup.addAction(new ActionItem(this, "测试4", R.drawable.mm_title_btn_speaker_normal));
	}

}
