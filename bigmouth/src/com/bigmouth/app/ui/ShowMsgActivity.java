package com.bigmouth.app.ui;

import com.bigmouth.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShowMsgActivity extends Activity {
	private TextView tvTitle, tvContent;
	private String title, content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitity_showmsg);
		tvTitle = (TextView) findViewById(R.id.tv_msg_title);
		tvContent = (TextView) findViewById(R.id.tv_msg_con);
		title = getIntent().getStringExtra("title");
		content = getIntent().getStringExtra("content");
		if (title != null) {
			tvTitle.setText(title);
		}
		if (content != null) {

			tvContent.setText(Html.fromHtml(content));
		}
		findViewById(R.id.tv_msg_quit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish();
			}
		});
		findViewById(R.id.tv_msg_index).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ShowMsgActivity.this, LoginActivity.class));
				finish();
			}
		});

	}

}
