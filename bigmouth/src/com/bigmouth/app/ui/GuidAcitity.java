package com.bigmouth.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.bigmouth.app.R;
import com.bigmouth.app.util.PersistentUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class GuidAcitity extends Activity {

	private ViewPager viewPager = null;
	private ImageView img1, img2, img3;
	private ArrayList<String> titles;
	private ArrayList<ImageView> ivIndexList;
	private int currIndex = -1;
	private LinearLayout llIndexImg;
	ArrayList<String> imgUrl = new ArrayList<String>();
	final ArrayList<View> views = new ArrayList<View>();
	private TextView tvEnter;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guid);
		tvEnter = (TextView) findViewById(R.id.tv_guid_enter);
		tvEnter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(GuidAcitity.this, LoginActivity.class));
				finish();
			}
		});
		options = new DisplayImageOptions.Builder()

		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build(); // 创建配置过得DisplayImageOption对象
		llIndexImg = (LinearLayout) findViewById(R.id.ll_guid_indeximg);
		ivIndexList = new ArrayList<ImageView>();
		imgUrl = getIntent().getStringArrayListExtra("img");
		//PersistentUtil.getInstance().write(this, "isFirst", false);
		//PersistentUtil.getInstance().write(this, "version", getVersionName());
		initWidgets();
		// 把要显示的View装入数组
		LayoutInflater li = LayoutInflater.from(this);

		if (imgUrl != null) {
			for (int i = 0; i < imgUrl.size(); i++) {

				View view = li.inflate(R.layout.pager1, null);
				ImageView imgView = (ImageView) view
						.findViewById(R.id.index_img);
				LayoutParams para = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				para.setMargins(15, 15, 15, 5);
				ImageView imgIndex = new ImageView(this);

				if (i == 0) {

					imgIndex.setBackgroundResource(R.drawable.page_icon_sel);
				} else {

					imgIndex.setBackgroundResource(R.drawable.page_icon);
				}
				llIndexImg.addView(imgIndex, para);

				ImageLoader.getInstance().displayImage(imgUrl.get(i), imgView,
						options, null);
				ivIndexList.add(imgIndex);
				views.add(view);

			}
		}

		picViewPagerAdapter pagerAdapter = new picViewPagerAdapter(views);
		viewPager.setAdapter(pagerAdapter);
		currIndex = 0;
		viewPager.setCurrentItem(currIndex);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 == ivIndexList.size()-1){
					llIndexImg.removeAllViews();
					tvEnter.setVisibility(View.VISIBLE);
					return;
				}
				for (int i = 0; i < ivIndexList.size(); i++) {

					ivIndexList.get(arg0).setBackgroundResource(
							R.drawable.page_icon_sel);
					for (int j = 0; j < ivIndexList.size(); j++){
						if(j==arg0)
							continue;
						else
						ivIndexList.get(j).setBackgroundResource(R.drawable.page_icon);
					}

				}
				currIndex = arg0;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initWidgets() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);

	}

	/**
	 * 为ViewPager添加适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class picViewPagerAdapter extends PagerAdapter {

		private List<View> listViews;

		public picViewPagerAdapter(List<View> list) {
			listViews = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(listViews.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(listViews.get(position));
			return listViews.get(position);
		}
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
}
