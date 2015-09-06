package com.bigmouth.app.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.bigmouth.app.R;
import com.bigmouth.app.util.PersistentUtil;
import com.bigmouth.app.util.StreamTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 初始界面
 * 
 * @author 程才、
 * @date 2014-10-24
 */
public class SplashActivity extends Activity {
	int imgNum;
	ArrayList<String> imgUrl = new ArrayList<String>();

	private ImageView iv_splash;
	Boolean isFirst;
	String appVersion;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	ImageView iv_temp;
	private String localUrl;
	private String strUrl = "cc";

	@SuppressLint("HandlerLeak")
	private Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (!localUrl.equals(strUrl)) {
				PersistentUtil.getInstance().write(SplashActivity.this, "urll",
						strUrl);
				if (imgUrl.size() <= 0) {
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					return;
				}
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, GuidAcitity.class);
				intent.putStringArrayListExtra("img", imgUrl);
				startActivity(intent);
			} else {
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
			}
			finish();

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		localUrl = PersistentUtil.getInstance().readString(this, "urll", "img");
		com.bigmouth.app.util.AppShortCutUtil.addNumShortCut(
				SplashActivity.this, SplashActivity.class, true, "0", false);
		iv_temp = (ImageView) findViewById(R.id.img_temp);
		options = new DisplayImageOptions.Builder()

		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build(); // 创建配置过得DisplayImageOption对象

		isFirst = PersistentUtil.getInstance().readBoolean(this, "isFirst",
				true);
		appVersion = PersistentUtil.getInstance().readString(this, "version",
				"1.0");
		// if (isFirst || !appVersion.equals(getVersionName())) {
		LoadImgs();
		// }
		iv_splash = (ImageView) findViewById(R.id.bg_splash);
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.alpha_splash);
		iv_splash.startAnimation(animation);
		handle.sendEmptyMessageDelayed(1, 3000);

	}

	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		return version;
	}

	private void LoadImgs() {
		/*
		 * ImageLoader.getInstance().displayImage(img_url, iv_headImge, options,
		 * null);
		 */

		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result = null;
				try {

					HttpClient client = new DefaultHttpClient();

					HttpGet post = new HttpGet(params[0]);

					HttpResponse response = client.execute(post);
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						InputStream is = response.getEntity().getContent();
						result = StreamTools.readFromStream(is);

					} else {
						Toast.makeText(SplashActivity.this, "请求失败", 1).show();
					}

				} catch (Exception e) {
					e.printStackTrace();

				}
				return result;

			}

			@Override
			protected void onPostExecute(String response) {
				// TODO Auto-generated method stub
				super.onPostExecute(response);
				strUrl = response;
				if (response != null) {
					Log.i("cc....response", response);
					try {
						JSONObject json = new JSONObject(response);
						JSONArray json_url = json.optJSONArray("data");
						imgNum = json.optInt("count");
						for (int j = 0; j < json_url.length(); j++) {
							JSONObject obj = json_url.getJSONObject(j);
							ImageLoader.getInstance().displayImage(
									obj.optString("url"), iv_temp, options,
									null);
							imgUrl.add("http://app.01teacher.cn"
									+ obj.optString("url"));
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Toast.makeText(SplashActivity.this, "访问失败", 0).show();
				}
			}

		}.execute("http://app.01teacher.cn/App/GetGuideImages");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);

		JPushInterface.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);

	}

}
