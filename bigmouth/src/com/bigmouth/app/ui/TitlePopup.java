package com.bigmouth.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.bigmouth.app.R;
import com.bigmouth.app.scan.MipcaCaptureActivity;
import com.bigmouth.app.util.PersistentUtil;
import com.bigmouth.app.util.ScreenShot;
import com.bigmouth.app.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class TitlePopup extends PopupWindow {
	public static final int TITLE_LEFT = 0;
	public static final int TITLE_RIGHT = 1;
	protected final int LIST_PADDING = 10;
    private Activity ac;
	private Context mContext;
	private Rect mRect = new Rect();
	private final int[] mLocation = new int[2];
    private String Type ;
	private int mScreenWidth;
	private int mScreenHeight;
	private boolean mIsDirty;
   
	private int popupGravity = Gravity.NO_GRAVITY;

	private int mDirection = TITLE_RIGHT;

	private OnItemOnClickListener mItemOnClickListener;

	private ListView mListView;
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

	public static interface OnItemOnClickListener {
		public void onItemClick(ActionItem item, int position);
	}

	public TitlePopup(Context context,Activity ac) {
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,ac);
	}

	public TitlePopup(Context context, int width, int height,Activity ac) {
		this.ac = ac;
		this.mContext = context;
		Type = PersistentUtil.getInstance().readString(context, "type","0");

		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		mScreenWidth = Util.getScreenWidth(mContext);
		mScreenHeight = Util.getScreenHeight(mContext);
		setWidth(width);
		setHeight(height);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.title_popup, null));
		setAnimationStyle(R.style.AnimationPreview);
		initUI();
	}

	private void initUI() {
		mListView = (ListView) getContentView().findViewById(R.id.title_list);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				dismiss();
				if (mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(index),
							index);
				if (index == 0) {
					String filePath = Environment.getExternalStorageDirectory() + "/DCIM/"
							+ "aaaa.png";
					ScreenShot.shoot(ac, new File(filePath));
					Toast.makeText(mContext, "分享获取积分", 0).show();
					ShareSDK.initSDK(mContext);
					OnekeyShare oks = new OnekeyShare();
					// 关闭sso授权
					oks.disableSSOWhenAuthorize();

					// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
					// oks.setNotification(R.drawable.ic_launcher,
					// getString(R.string.app_name));
					// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
					oks.setTitle(mContext.getString(R.string.share));
					// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
					oks.setTitleUrl("http://sharesdk.cn");
					// text是分享文本，所有平台都需要这个字段
					oks.setText("我是分享文本");
					// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
					oks.setImagePath(filePath);// 确保SDcard下面存在此张图片
					// url仅在微信（包括好友和朋友圈）中使用
					oks.setUrl("http://sharesdk.cn");
					// comment是我对这条分享的评论，仅在人人网和QQ空间使用
					oks.setComment("我是测试评论文本");
					// site是分享此内容的网站名称，仅在QQ空间使用
					oks.setSite(mContext.getString(R.string.app_name));
					// siteUrl是分享此内容的网站地址，仅在QQ空间使用
					oks.setSiteUrl("http://sharesdk.cn");
                  
					// 启动分享GUI
					oks.show(mContext);
					// 获取已经注册到SDK的平台实例列表
					Platform[] platformList = ShareSDK.getPlatformList();
					for(int i =0;i<platformList.length;i++){
						platformList[i].setPlatformActionListener(new PlatformActionListener() {
							
							@Override
							public void onError(Platform arg0, int arg1, Throwable arg2) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
								// TODO Auto-generated method stub
								Toast.makeText(mContext, "获取积分成功！", 0).show();
							}
							
							@Override
							public void onCancel(Platform arg0, int arg1) {
								// TODO Auto-generated method stub
								
							}
						});
					}

					
				
				} 
				else if(index==1) {
					if("2".equals(Type)){
					Intent intent3 = new Intent(mContext,
							MipcaCaptureActivity.class);
					
					ac.startActivityForResult(intent3, 1);
					}
					if("1".equals(Type)){
						Intent intent3 = new Intent(mContext,
								StudyActivity.class);
						
						ac.startActivityForResult(intent3, 1);
					}
				}
			}
		});
	}

	public void addAction(ActionItem action) {
		if (action != null) {
			mActionItems.add(action);
			mIsDirty = true;
		}
	}

	public void cleanAction() {
		if (mActionItems.isEmpty()) {
			mActionItems.clear();
			mIsDirty = true;
		}
	}

	public ActionItem getAction(int position) {
		if (position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}

	public void setDirection(int direction) {
		this.mDirection = direction;
	}

	public void setItemOnClickListener(
			OnItemOnClickListener onItemOnClickListener) {
		this.mItemOnClickListener = onItemOnClickListener;
	}

	private void populateActions() {
		mIsDirty = false;

		mListView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = null;

				if (convertView == null) {
					textView = new TextView(mContext);
					textView.setTextColor(mContext.getResources().getColor(
							android.R.color.white));
					textView.setTextSize(18);
					textView.setPadding(0, 10, 0, 10);
					textView.setSingleLine(true);
				} else {
					textView = (TextView) convertView;
				}

				ActionItem item = mActionItems.get(position);

				textView.setText(item.mTitle);
				textView.setCompoundDrawablePadding(10);
				textView.setCompoundDrawablesWithIntrinsicBounds(
						item.mDrawable, null, null, null);
				return textView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}

			@Override
			public int getCount() {
				return mActionItems.size();
			}
		});
	}

	public void show(View view) {
		view.getLocationOnScreen(mLocation);
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
				mLocation[1] + view.getHeight());
		if (mIsDirty) {
			populateActions();
		}

		showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING
				- (getWidth() / 2), mRect.bottom);
	}

}
