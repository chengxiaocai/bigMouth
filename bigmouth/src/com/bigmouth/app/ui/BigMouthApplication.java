package com.bigmouth.app.ui;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import cn.jpush.android.api.JPushInterface;

import com.bigmouth.app.service.PollingService;
import com.bigmouth.app.util.PollingUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class BigMouthApplication extends Application {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		/*
		 * if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >=
		 * Build.VERSION_CODES.GINGERBREAD) { StrictMode.setThreadPolicy(new
		 * StrictMode
		 * .ThreadPolicy.Builder().detectAll().penaltyDialog().build());
		 * StrictMode.setVmPolicy(new
		 * StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build()); }
		 */

		super.onCreate();
		 JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
		/*PollingUtils.startPollingService(this, 5, PollingService.class,
				PollingService.ACTION);*/

		initImageLoader(getApplicationContext());

	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app

				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}