package com.bigmouth.app.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * ���ص�λת������
 * 
 * @author �̲�
 * @date 2014��5��8��
 */
public class DisplayUtil {
	/**
	 * ��pxֵת��Ϊdip��dpֵ����֤�ߴ��С����
	 * 
	 * @param pxValue
	 * @param scale
	 *            ��DisplayMetrics��������density��
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ��dip��dpֵת��Ϊpxֵ����֤�ߴ��С����
	 * 
	 * @param dipValue
	 * @param scale
	 *            ��DisplayMetrics��������density��
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * ��pxֵת��Ϊspֵ����֤���ִ�С����
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * ��spֵת��Ϊpxֵ����֤���ִ�С����
	 * 
	 * @param spValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int sp2px(Activity context, float spValue) {
		// final float fontScale =
		// context.getResources().getDisplayMetrics().scaledDensity;
		// return (int) (spValue * fontScale + 0.5f);
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spValue, metric);
	}

	public static int getWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		return wm.getDefaultDisplay().getWidth();
	

	}
	public static int getHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		
		return wm.getDefaultDisplay().getHeight();
		
		
	}
}
