package com.bigmouth.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.bigmouth.app.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class GuidAcitity extends Activity {

	private ViewPager viewPager = null;
	private ImageView img1, img2, img3;
	private ArrayList<String> titles;
	private int currIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guid);
		initWidgets();
		// 把要显示的View装入数组
		LayoutInflater li = LayoutInflater.from(this);
		View view1 = li.inflate(R.layout.pager1, null);
		View view2 = li.inflate(R.layout.pager2, null);
		View view3 = li.inflate(R.layout.pager3, null);
		view3.findViewById(R.id.enter).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GuidAcitity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// 添加页面
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);

		

		picViewPagerAdapter pagerAdapter = new picViewPagerAdapter(views);
		viewPager.setAdapter(pagerAdapter);
		currIndex = 0;
		viewPager.setCurrentItem(currIndex);
		img1.setImageResource(R.drawable.page_icon_sel);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					img1.setImageResource(R.drawable.page_icon_sel);
					img2.setImageResource(R.drawable.page_icon);
					img3.setImageResource(R.drawable.page_icon);
					break;
				case 1:
					img2.setImageResource(R.drawable.page_icon_sel);
					img1.setImageResource(R.drawable.page_icon);
					img3.setImageResource(R.drawable.page_icon);
					break;
				case 2:
					img3.setImageResource(R.drawable.page_icon_sel);
					img2.setImageResource(R.drawable.page_icon);
					img1.setImageResource(R.drawable.page_icon);
					break;
				default:
					break;
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
		img1 = (ImageView) findViewById(R.id.icon_1);
		img2 = (ImageView) findViewById(R.id.icon_2);
		img3 = (ImageView) findViewById(R.id.icon_3);
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
}
