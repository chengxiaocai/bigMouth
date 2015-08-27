package com.bigmouth.app.util;

import com.bigmouth.app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 加载等待框
 * 
 * @author 程才
 * @date 2014年10月10日
 */
public class LoadingProgressDialog extends ProgressDialog {
	private String message;

	private TextView define_progress_msg;

	private ImageView iv;

	private Animation operatingAnim;

	public LoadingProgressDialog(Context context) {
		super(context);
	}

	public LoadingProgressDialog(Context context, String message) {
		super(context);
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progresslayout);

		operatingAnim = AnimationUtils.loadAnimation(getContext(),
				R.anim.progress);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);

		iv = (ImageView) findViewById(R.id.dialog_progress);

		define_progress_msg = (TextView) findViewById(R.id.define_progress_msg);

		if (TextUtils.isEmpty(message)) {
			define_progress_msg.setVisibility(View.GONE);
		} else {
			define_progress_msg.setText(message);
		}
	}

	@Override
	public void show() {
		super.show();
		iv.startAnimation(operatingAnim);
	}

	@Override
	protected void onStop() {
		iv.clearAnimation();
		super.onStop();
	}

}
