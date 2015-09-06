package com.bigmouth.app.ui;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import cn.jpush.android.api.JPushInterface;

import com.bigmouth.app.service.PollingService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class BigMouthApplication extends Application implements
		Thread.UncaughtExceptionHandler {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {

		super.onCreate();
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		/*
		 * PollingUtils.startPollingService(this, 5, PollingService.class,
		 * PollingService.ACTION);
		 */
		Thread.setDefaultUncaughtExceptionHandler(this);
		initImageLoader(getApplicationContext());

	}

	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app

				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
	
		System.exit(0);

	}

}