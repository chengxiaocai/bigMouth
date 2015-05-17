package com.bigmouth.app.service;

import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;
import com.bigmouth.app.bean.Readings;
import com.bigmouth.app.ui.MainAcitivity;
import com.bigmouth.app.ui.ShowMsgActivity;
import com.bigmouth.app.ui.SplashActivity;
import com.bigmouth.app.util.AppShortCutUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class PollingService extends Service {
	public static final String ACTION = "com.ryantang.service.PollingService";

	private Notification mNotification;
	private NotificationManager mManager;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		ahc = new AsyncHttpClient();
		initNotifiManager();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		getUnReadMsg();
		getUnMsgNum();
		// new PollingThread().start();
	}

	// 初始化通知栏配置
	private void initNotifiManager() {
		long a[] = new long[] { 1000, 1000, 1000, 1000 };
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.log;
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = "New Message";
		// mNotification.vibrate = a;
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	// 弹出Notification
	private void showNotification(String title, String content) {
		mNotification.when = System.currentTimeMillis();
		// Navigator to the new activity when click the notification title
		Intent i = new Intent(this, ShowMsgActivity.class);
		i.putExtra("title", title);
		i.putExtra("content", content);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(this,
				getResources().getString(R.string.app_name), title,
				pendingIntent);
		mManager.notify(new Random().nextInt(), mNotification);
	}

	/**
	 * Polling thread 模拟向Server轮询的异步线程
	 * 
	 * @Author Ryan
	 * @Create 2013-7-13 上午10:18:34
	 */
	int count = 0;

	class PollingThread extends Thread {
		@Override
		public void run() {
			System.out.println("Polling...");

			// 当计数能被5整除时弹出通知
			// getReading()

			// AcquireWakeLock();
			System.out.println("New message!");

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

	public void getUnReadMsg() {

		RequestParams rp = new RequestParams();
		rp.put("UserID", PersistentUtil.getInstance()
				.readString(this, "id", ""));
		reqhandle = ahc.post("http://app.01teacher.cn/App/GetUserMsg",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...getMsg", "start...");
				// thisdialog.show();
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
						if (con.equals(PersistentUtil.getInstance().readString(PollingService.this, "content","  "))) {
							return;
						}
						PersistentUtil.getInstance().write(PollingService.this,
								"content", con);
						if (!TextUtils.isEmpty(title)
								&& !TextUtils.isEmpty(con)) {
							
						}
						showNotification(title, con);
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
				// thisdialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.i("cc...cars", "failue.......");
				HttpHandle hh = new HttpHandle();
				hh.handleFaile(PollingService.this, arg3);

			}

		});
	}

	public void getUnMsgNum() {

		RequestParams rp = new RequestParams();
		rp.put("UserID", PersistentUtil.getInstance()
				.readString(this, "id", ""));
		reqhandle = ahc.post("http://app.01teacher.cn/App/GetUserMsgNum",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...getMsg", "start...");
				// thisdialog.show();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub

				super.onSuccess(content);
				Log.i("cc...num", "success.......");
				// Toast.makeText(getActivity(), "添加成功", 0).show();
				try {
					obj = new JSONObject(content);
					if (obj.optBoolean("success")) {
						String num = obj.optString("number");
						if (!num.equals(PersistentUtil.getInstance()
								.readString(PollingService.this, "num", "  "))) {

							AppShortCutUtil.addNumShortCut(PollingService.this,
									SplashActivity.class, true, num, false);
							PersistentUtil.getInstance().write(
									PollingService.this, "num", num);
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
				// thisdialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.i("cc...cars", "failue.......");
				HttpHandle hh = new HttpHandle();
				hh.handleFaile(PollingService.this, arg3);

			}

		});
	}
}