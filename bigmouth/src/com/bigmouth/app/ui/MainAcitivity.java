package com.bigmouth.app.ui;


import com.bigmouth.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author yangyu
 *	�����������ڶ���ʵ�ַ�ʽ,Activityʵ�ַ�ʽ
 */
public class MainAcitivity extends Activity {
	//����������ϵİ�ť
	private ImageButton titleBtn;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title1);
		
		initView();			
	}
	
	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ���������ť�����ü���
		titleBtn = (ImageButton) findViewById(R.id.title_btn);
		titleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainAcitivity.this,DialogActivity.class));
			}
		});						
	}	
		
}
