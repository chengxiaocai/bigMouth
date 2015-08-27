package com.bigmouth.app.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.bigmouth.app.R;
import com.bigmouth.app.bean.Readings;
import com.bigmouth.app.bean.Words;
import com.bigmouth.app.ui.DialogBaiduFanyiActivity;
import com.bigmouth.app.ui.DialogBaiduFanyiUpActivity;
import com.bigmouth.app.ui.MainAcitivity;
import com.bigmouth.app.ui.StudyActivity;
import com.bigmouth.app.ui.StudyActivity1;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.DisplayUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView.BufferType;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReadingFragment extends Fragment {

	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;
	private ArrayList<Readings> readList = new ArrayList<Readings>();
	private ArrayList<TextView> tv = new ArrayList<TextView>();
	LayoutInflater inflater = null;
	private ListView lvReading;
	private View contentView;
	private ReadingsAdapter adapter;
	private StudyActivity1 ac;// 父activitiy对象；
	private String id;
	private String strTransWords;
	private TextView tvTile, tvData, tvSource, tvReads, tvReads1, tvReads2,
			tvReads3;
	private LinearLayout llShowReads;
	public float y = 0;
	private FrameLayout fra;
	String st = "";
	String str1, str2, str3, str4;
	int sq1, sq2, sq3;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private int Colors[] = new int[] { R.color.color1, R.color.color3,
			R.color.color4, R.color.color5, R.color.color6, R.color.color7,
			R.color.color8, R.color.color9, R.color.color10, R.color.color11,
			R.color.color12 };
	private Boolean isCanClick = true;
	private ImageSize size;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_reading, container,
				false);
		initView();
		getReading();

		return contentView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		ac = (StudyActivity1) activity;

	}

	public void initView() {
		size = new ImageSize(DisplayUtil.dip2px(getActivity(), 60),
				DisplayUtil.dip2px(getActivity(), 60));

		tvReads = (TextView) contentView.findViewById(R.id.tv_reads_reads);
		tvReads1 = (TextView) contentView.findViewById(R.id.tv_reads_reads1);
		tvReads2 = (TextView) contentView.findViewById(R.id.tv_reads_reads2);
		tvReads3 = (TextView) contentView.findViewById(R.id.tv_reads_reads3);
		// tvReads.setMovementMethod(ScrollingMovementMethod.getInstance());
		contentView.findViewById(R.id.iv_reads_share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ShareSDK.initSDK(getActivity());
						OnekeyShare oks = new OnekeyShare();

						oks.addHiddenPlatform(SinaWeibo.NAME);
						oks.addHiddenPlatform(QQ.NAME);
						oks.setCallback(new PlatformActionListener() {
							
							@Override
							public void onError(Platform arg0, int arg1, Throwable arg2) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onCancel(Platform arg0, int arg1) {
								// TODO Auto-generated method stub
								
							}
						});
						// 关闭sso授权
						oks.disableSSOWhenAuthorize();
        
						// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
						// oks.setNotification(R.drawable.ic_launcher,
						// getString(R.string.app_name));
						// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

						// titleUrl是标题的网络链接，仅在人人网和QQ空间使用

						// text是分享文本，所有平台都需要这个字段
						oks.setText(tvReads.getText().toString());
						// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

						// url仅在微信（包括好友和朋友圈）中使用

						// 启动分享GUI
						oks.show(getActivity());
					}
				});
		// tvReadss = (TextView) contentView.findViewById(R.id.tv_reads_readss);

		llShowReads = (LinearLayout) contentView
				.findViewById(R.id.ll_show_reads_detail);
		tvData = (TextView) contentView.findViewById(R.id.tv_readings_data);
		tvTile = (TextView) contentView.findViewById(R.id.tv_readings_title);
		tvSource = (TextView) contentView.findViewById(R.id.tv_readings_source);
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.log)
				.showImageForEmptyUri(R.drawable.log).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		lvReading = (ListView) contentView.findViewById(R.id.lv_reading_list1);
		adapter = new ReadingsAdapter(readList);
		lvReading.setAdapter(adapter);
		lvReading.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				thisdialog.show();

				lvReading.setVisibility(View.GONE);
				llShowReads.setVisibility(View.VISIBLE);

				// TODO Auto-generated method stub
				// Intent in = new Intent();
				//
				// in.putExtra("text", readList.get(position).getId());
				// ac.changeReadingPage(in);
				ac.isReading = true;
				tvData.setText(readList.get(position).getDate());
				tvSource.setText(readList.get(position).getSource());
				tvTile.setText(readList.get(position).getTitle());
				getReadingDeatail(readList.get(position).getId());

			}
		});
		inflater = LayoutInflater.from(getActivity());

		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "");
	}

	public void getReading() {

		RequestParams rp = new RequestParams();

		reqhandle = ahc.post("http://app.01teacher.cn/App/GetReadings",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...cars", "start...");
				 thisdialog.show();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.i("cc...cars", "success.......");
				// Toast.makeText(getActivity(), "添加成功", 0).show();
				try {
					obj = new JSONObject(content);

					JSONArray array = obj.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {

						Readings read = new Readings();
						read.setRandomColor(new Random().nextInt(11));
						read.setId(array.getJSONObject(i).optString("id"));
						String str = array.getJSONObject(i).optString("title");
						if (str != null && str.contains("&apos")) {

							read.setTitle(str.replace("&apos;", "'"));

						} else {

							read.setTitle(str);
						}
						read.setDate(array.getJSONObject(i).optString("date"));
						read.setImg(array.getJSONObject(i).optString("img"));
						read.setSource(array.getJSONObject(i).optString(
								"source"));

						readList.add(read);
					}
					adapter.notifyDataSetChanged();

				} catch (JSONException e) {
					Toast.makeText(getActivity(), "获取文章列表失败", 0).show();
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
				hh.handleFaile(getActivity(), arg3);
				if (thisdialog.isShowing()) {
					// thisdialog.dismiss();
				}
			}

		});
	}

	private class ReadingsAdapter extends BaseAdapter {
		ArrayList<Readings> listReadings;

		public ReadingsAdapter(ArrayList<Readings> listReadings) {
			super();
			this.listReadings = listReadings;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listReadings.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			if (listReadings.size() < 1) {
				return null;
			}
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_reads, null);

			}
			final ImageView img = (ImageView) convertView
					.findViewById(R.id.iv_reads_img);

			// ImageLoader.getInstance().displayImage(listReadings.get(position).getImg(),
			// img, options, null);

			ImageLoader.getInstance().loadImage(
					listReadings.get(position).getImg(), size, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

							img.setImageBitmap(loadedImage);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});
			TextView tvText = (TextView) convertView
					.findViewById(R.id.tv_reading_title);
			tvText.setText(listReadings.get(position).getTitle());
			TextView tvDate = (TextView) convertView
					.findViewById(R.id.tv_reading_data);
			tvDate.setText(listReadings.get(position).getDate());

			TextView tvSource = (TextView) convertView
					.findViewById(R.id.tv_reading_source);

			// tvReads.setText(listReadings.get(position).getText(),BufferType.SPANNABLE);
			// getEachWord(tvReads);
			// tvReads.setTextColor(Color.WHITE);
			// tvReads.setMovementMethod(LinkMovementMethod.getInstance());
			// tvReads.setText(listReadings.get(position).getText());
			// if (listReadings.get(position).getIsShowReads()) {
			// tvReads.setVisibility(View.VISIBLE);
			// } else {
			// tvReads.setVisibility(View.GONE);
			//
			// }
			tvSource.setText(listReadings.get(position).getSource());
			RelativeLayout reMain = (RelativeLayout) convertView
					.findViewById(R.id.re_reading_main);

			// reMain.setBackgroundColor(getResources().getColor(
			// Colors[listReadings.get(position).getRandomColor()]));
			// tvReads.setBackgroundColor(getResources().getColor(
			// Colors[listReadings.get(position).getRandomColor()]));

			// reMain.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// if (listReadings.get(position).getIsShowReads()) {
			// tvReads.setVisibility(View.GONE);
			// listReadings.get(position).setIsShowReads(false);
			// } else {
			// tvReads.setVisibility(View.VISIBLE);
			// listReadings.get(position).setIsShowReads(true);
			//
			// }
			// if (listReadings.get(position).getText() == "") {
			//
			// getReadingDeatail(listReadings.get(position).getId(),
			// tvReads, position);
			// }
			// }
			// });
			return convertView;
		}

	}

	public void getReadingDeatail(String id) {

		RequestParams rp = new RequestParams();
		rp.put("id", id);

		reqhandle = ahc.post("http://app.01teacher.cn/App/GetReadingsByID",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...getreads", "start...");
				tvReads.setText("");
				tvReads1.setText("");
				tvReads2.setText("");
				tvReads3.setText("");

			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.i("cc...cars", "success.......");
				// Toast.makeText(getActivity(), "添加成功", 0).show();
				try {
					obj = new JSONObject(content);

					String str = obj.optString("text");
					if (str != null) {
						if (str.length() > 2500 && str.length() <= 5000) {
							getCharacterPosition2(str);

							str1 = str.substring(0, sq1);
							str2 = str.substring(sq1 + 1, str.length() - 1);
							str2 = f(str2);

							tvReads.setText(str1, BufferType.SPANNABLE);

							getEachWord(tvReads);
							tvReads.setMovementMethod(LinkMovementMethod
									.getInstance());

							tvReads1.setText(str2, BufferType.SPANNABLE);
							getEachWord(tvReads1);
							tvReads1.setMovementMethod(LinkMovementMethod
									.getInstance());

						} else if (str.length() > 5000 && st.length() <= 7000) {
							getCharacterPosition3(str);

							str1 = str.substring(0, sq1);

							str2 = str.substring(sq1 + 1, sq2);
							str2 = f(str2);

							str3 = str.substring(sq2, str.length() - 1);
							str3 = f(str3);

							tvReads.setText(str1, BufferType.SPANNABLE);

							getEachWord(tvReads);
							tvReads.setMovementMethod(LinkMovementMethod
									.getInstance());

							tvReads1.setText(str2, BufferType.SPANNABLE);
							getEachWord(tvReads1);
							tvReads1.setMovementMethod(LinkMovementMethod
									.getInstance());

							tvReads2.setText(str3, BufferType.SPANNABLE);
							getEachWord(tvReads2);
							tvReads2.setMovementMethod(LinkMovementMethod
									.getInstance());

						} else if (st.length() > 7000) {
							getCharacterPosition4(str);

							str1 = str.substring(0, sq1);

							str2 = str.substring(sq1 + 1, sq2);
							str2 = f(str2);

							str3 = str.substring(sq2 + 1, sq3);
							str3 = f(str3);

							str4 = str.substring(sq3 + 1, str.length());
							str4 = f(str4);

							tvReads.setText(str1, BufferType.SPANNABLE);

							getEachWord(tvReads);
							tvReads.setMovementMethod(LinkMovementMethod
									.getInstance());

							tvReads1.setText(str2, BufferType.SPANNABLE);
							getEachWord(tvReads1);
							tvReads1.setMovementMethod(LinkMovementMethod
									.getInstance());

							tvReads2.setText(str3, BufferType.SPANNABLE);
							getEachWord(tvReads2);
							tvReads2.setMovementMethod(LinkMovementMethod
									.getInstance());

							tvReads3.setText(str4, BufferType.SPANNABLE);
							getEachWord(tvReads3);
							tvReads3.setMovementMethod(LinkMovementMethod
									.getInstance());

						} else {
							tvReads.setText(str, BufferType.SPANNABLE);

							getEachWord(tvReads);
							tvReads.setMovementMethod(LinkMovementMethod
									.getInstance());
						}
					}

				} catch (JSONException e) {
					Toast.makeText(getActivity(), "获取文章失败", 0).show();
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
				hh.handleFaile(getActivity(), arg3);
				if (thisdialog.isShowing()) {
					// thisdialog.dismiss();
				}
			}

		});
	}

	public static Integer[] getIndices(String s, char c) {
		int pos = s.indexOf(c, 0);
		List<Integer> indices = new ArrayList<Integer>();
		while (pos != -1) {
			indices.add(pos);
			pos = s.indexOf(c, pos + 1);
		}
		return (Integer[]) indices.toArray(new Integer[0]);
	}

	public void getEachWord(TextView textView) {
		Spannable spans = (Spannable) textView.getText();
		Integer[] indices = getIndices(textView.getText().toString().trim(),
				' ');
		int start = 0;
		int end = 0;
		// to cater last/only word loop will run equal to the length of
		// indices.length
		for (int i = 0; i <= indices.length; i++) {
			ClickableSpan clickSpan = getClickableSpan(textView);
			// to cater last/only word
			end = (i < indices.length ? indices[i] : spans.length());
			spans.setSpan(clickSpan, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = end + 1;
		}

	}

	public void getEachWord1(TextView textView) {
		Spannable spans = (Spannable) textView.getText();
		Integer[] indices = getIndices(textView.getText().toString().trim(),
				' ');
		int start = 0;
		int end = 0;
		// to cater last/only word loop will run equal to the length of
		// indices.length
		for (int i = 0; i <= indices.length; i++) {
			ClickableSpan clickSpan = getClickableSpan1(textView);
			// to cater last/only word
			end = (i < indices.length ? indices[i] : spans.length());
			spans.setSpan(clickSpan, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = end + 1;
		}

	}

	private ClickableSpan getClickableSpan(final TextView tvText) {
		return new ClickableSpan() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View widget) {
				try {
					if (isCanClick) {
						isCanClick = false;
					} else {
						return;
					}

					TextView tv = (TextView) widget;

					strTransWords = tv
							.getText()
							.subSequence(tv.getSelectionStart(),
									tv.getSelectionEnd()).toString();
					Log.d(tv.getSelectionStart() + "", tv.getSelectionEnd()
							+ "");
					SpannableStringBuilder style = new SpannableStringBuilder(
							tv.getText().toString());
					style.setSpan(new BackgroundColorSpan(getResources()
							.getColor(R.color.green)), tv.getSelectionStart(),
							tv.getSelectionEnd(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

					tv.setText(style);

					getEachWord(tvText);

					Log.d("tapped on:", strTransWords);
					if (y > (DisplayUtil.getHeight(getActivity()) / 2)) {

						Intent intent = new Intent();
						intent.putExtra("word", strTransWords);
						intent.setClass(getActivity(),
								DialogBaiduFanyiActivity.class);
						startActivityForResult(intent, 1);
						getActivity().overridePendingTransition(
								R.anim.push_up_in, 0);

					} else {

						Intent intent = new Intent();
						intent.putExtra("word", strTransWords);
						intent.setClass(getActivity(),
								DialogBaiduFanyiUpActivity.class);
						startActivityForResult(intent, 1);

						getActivity().overridePendingTransition(
								R.anim.push_down_out, 0);

					}

				} catch (Exception e) {
					// TODO: handle exception
					Log.i("cc", "hahahfhahdfiaofhioehfieo");

				}

			}

			@Override
			public void updateDrawState(TextPaint ds) {
				// ds.setColor(Color.BLACK);
				ds.setUnderlineText(false);
			}
		};
	}

	private ClickableSpan getClickableSpan1(final TextView tvText) {
		return new ClickableSpan() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View widget) {
				try {
					TextView tv = (TextView) widget;

					strTransWords = tv
							.getText()
							.subSequence(tv.getSelectionStart(),
									tv.getSelectionEnd()).toString();
					Log.d(tv.getSelectionStart() + "", tv.getSelectionEnd()
							+ "");
					SpannableStringBuilder style = new SpannableStringBuilder(
							tv.getText().toString());
					style.setSpan(new BackgroundColorSpan(getResources()
							.getColor(R.color.green)), tv.getSelectionStart(),
							tv.getSelectionEnd(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

					tv.setText(style);

					getEachWord1(tvText);

					Log.d("tapped on:", strTransWords);
					if (y > (DisplayUtil.getHeight(getActivity()) / 2)) {

						Intent intent = new Intent();
						intent.putExtra("word", strTransWords);
						intent.setClass(getActivity(),
								DialogBaiduFanyiActivity.class);
						startActivityForResult(intent, 1);

						getActivity().overridePendingTransition(
								R.anim.push_up_in, 0);
					} else {
						Intent intent = new Intent();
						intent.putExtra("word", strTransWords);
						intent.setClass(getActivity(),
								DialogBaiduFanyiUpActivity.class);
						startActivityForResult(intent, 1);
						getActivity().overridePendingTransition(
								R.anim.push_down_out, 0);
					}

				} catch (Exception e) {
					// TODO: handle exception
					Log.i("cc", "hahahfhahdfiaofhioehfieo");

				}

			}

			@Override
			public void updateDrawState(TextPaint ds) {
				// ds.setColor(Color.BLACK);
				ds.setUnderlineText(false);
			}
		};
	}

	public void SetReadListVisible() {
		tvReads.setText("");
		tvReads1.setText("");
		tvReads2.setText("");
		tvReads3.setText("");
		lvReading.setVisibility(View.VISIBLE);
		llShowReads.setVisibility(View.GONE);
	}

	public void getCharacterPosition2(String string) {
		// 这里是获取"/"符号的位置
		int total = 0;
		int count = 0;
		Matcher slashMatcher = Pattern.compile(",").matcher(string);

		while (slashMatcher.find()) {
			total++;
			// 当"/"符号第三次出现的位置

		}
		int perTotal = total / 2;
		Matcher slashMatcher1 = Pattern.compile(",").matcher(string);
		while (slashMatcher1.find()) {
			count++;
			if (count == perTotal) {
				sq1 = slashMatcher1.start();
			}

		}

	}

	public void getCharacterPosition3(String string) {
		// 这里是获取"/"符号的位置
		int total = 0;
		int count = 0;
		Matcher slashMatcher = Pattern.compile(",").matcher(string);

		while (slashMatcher.find()) {
			total++;
			// 当"/"符号第三次出现的位置

		}
		int perTotal = total / 3;
		Matcher slashMatcher1 = Pattern.compile(",").matcher(string);
		while (slashMatcher1.find()) {
			count++;
			if (count == perTotal) {
				sq1 = slashMatcher1.start();
			}
			if (count == 2 * perTotal) {
				sq2 = slashMatcher1.start();
			}

		}

	}

	public void getCharacterPosition4(String string) {
		// 这里是获取"/"符号的位置
		int total = 0;
		int count = 0;
		Matcher slashMatcher = Pattern.compile(",").matcher(string);

		while (slashMatcher.find()) {
			total++;
			// 当"/"符号第三次出现的位置

		}
		int perTotal = total / 4;
		Matcher slashMatcher1 = Pattern.compile(",").matcher(string);
		while (slashMatcher1.find()) {
			count++;
			if (count == perTotal) {
				sq1 = slashMatcher1.start();
			}

			if (count == 2 * perTotal) {
				sq2 = slashMatcher1.start();
			}

			if (count == 3 * perTotal) {
				sq3 = slashMatcher1.start();
			}

		}

	}

	public String f(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {

				s = s.substring(i, s.length());

				break;

			}

		}

		return s;

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		isCanClick = true;
	}

}
