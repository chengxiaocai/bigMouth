package com.bigmouth.app.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;
import com.bigmouth.app.service.PollingService;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShowMsgActivity extends Activity {
	private TextView tvTitle, tvContent;
	private String title, content;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitity_showmsg);
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(this, "");
		tvTitle = (TextView) findViewById(R.id.tv_msg_title);
		tvContent = (TextView) findViewById(R.id.tv_msg_con);
		
		
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
		getUnReadMsg();

	}
	public void getUnReadMsg() {

		RequestParams rp = new RequestParams();
		rp.put("UserID", PersistentUtil.getInstance().readString(this, "id", ""));
		reqhandle = ahc.post("http://app.01teacher.cn/App/GetUserMsg",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...getMsg", "start...");
			 thisdialog.show();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub

				super.onSuccess(content);
				Log.i("cc...msg", "success.......");
				// Toast.makeText(getActivity(), "添加成功", 0).show();
				try {
					obj = new JSONObject(content);
					if (obj.optBoolean("success")) {
						JSONObject objData = obj.optJSONObject("data");
						String title = objData.optString("title");
						String con = objData.getString("content");
					    Log.i("cc....con",con);
					    Log.i("cc....title",title);
					    if (title != null) {
							tvTitle.setText(title);
						}
						if (content != null) {

							tvContent.setText(Html.fromHtml(content));
						}
						
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.i("cc...", "finish");
				 thisdialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.i("cc...cars", "failue.......");
				HttpHandle hh = new HttpHandle();
				hh.handleFaile(ShowMsgActivity.this, arg3);

			}

		});
	}


}
