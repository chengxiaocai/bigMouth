package com.bigmouth.app.ui;

import com.bigmouth.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class Main1Acitivity extends FragmentActivity implements OnClickListener {
	private TitlePopup titlePopup;
	private ImageButton titleBtn;
	FragmentTransaction transaction;
	private Fragment currentFragment = new Fragment();
	IndexFragment indexFramet;
	CalenderFragment calenderFragment;
	ClassTimeFragment classTimeFragment;
	ClassRecordFragment classRecordragment;
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
				/*
				 * startActivity(new Intent(MainAcitivity.this,
				 * DialogActivity.class));
				 */
				titlePopup.show(v);
			}
		});

		indexFramet = new IndexFragment();

		currentFragment = indexFramet;
		transaction.replace(R.id.frag_main_tab, indexFramet);
		transaction.commit();

	}

	@Override
	public void onClick(View v) {
		transaction = getSupportFragmentManager().beginTransaction();
		int id = v.getId();
		switch (id) {
		case R.id.rb_miantab_invite:

			if (currentFragment == indexFramet)
				return;
            if(indexFramet==null){
            	
            	indexFramet = new IndexFragment();
            }

			currentFragment = indexFramet;
			transaction.replace(R.id.frag_main_tab, indexFramet);
			transaction.commit();
			break;
		case R.id.rb_miantab_me:
			if (currentFragment == calenderFragment)
				return;
			if(calenderFragment==null){
				
				calenderFragment = new CalenderFragment();
			}

			currentFragment = calenderFragment;
			transaction.replace(R.id.frag_main_tab, calenderFragment);
			transaction.commit();
			break;
		case R.id.rb_miantab_player:
			if (currentFragment == calenderFragment)
				return;
			if(classTimeFragment==null){
				
				classTimeFragment = new ClassTimeFragment();
			}

			currentFragment = classTimeFragment;
			transaction.replace(R.id.frag_main_tab, classTimeFragment);
			transaction.commit();
			break;
		case R.id.rb_miantab_playing:
			if (currentFragment == classRecordragment)
				return;
			if(classRecordragment==null){
				
				classRecordragment = new ClassRecordFragment();
			}

			currentFragment = classRecordragment;
			transaction.replace(R.id.frag_main_tab, classRecordragment);
			transaction.commit();
			break;

		default:
			break;
		}

	}

	private void init() {

		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, this);
		titlePopup.addAction(new ActionItem(this, "分享",
				R.drawable.mm_title_btn_receiver_normal));
		titlePopup.addAction(new ActionItem(this, "扫描",
				R.drawable.mm_title_btn_set_normal));
		titlePopup.addAction(new ActionItem(this, "阅读",
				R.drawable.mm_title_btn_share_normal));
	}

}
