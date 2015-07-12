package com.bigmouth.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;
import com.bigmouth.app.bean.Readings;
import com.bigmouth.app.bean.Words;
import com.bigmouth.app.ui.MainAcitivity;
import com.bigmouth.app.ui.StudyActivity;
import com.bigmouth.app.ui.StudyActivity1;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private int Colors[] = new int[] { R.color.color1, R.color.color3,
			R.color.color4, R.color.color5, R.color.color6, R.color.color7,
			R.color.color8, R.color.color9, R.color.color10, R.color.color11,
			R.color.color12 };

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
				// TODO Auto-generated method stub
				// Intent in = new Intent();
				//
				// in.putExtra("text", readList.get(position).getId());
				// ac.changeReadingPage(in);
			}
		});
		inflater = LayoutInflater.from(getActivity());

		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "请稍后！");
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
				// thisdialog.show();
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
						read.setTitle(array.getJSONObject(i).optString("title"));
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
				// thisdialog.dismiss();
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
			TextView tvText = (TextView) convertView
					.findViewById(R.id.tv_reading_title);
			tvText.setText(listReadings.get(position).getTitle());
			TextView tvDate = (TextView) convertView
					.findViewById(R.id.tv_reading_data);
			tvDate.setText(listReadings.get(position).getDate());

			TextView tvSource = (TextView) convertView
					.findViewById(R.id.tv_reading_source);
			final TextView tvReads = (TextView) convertView
					.findViewById(R.id.tv_read_reads);
			tvReads.setText(listReadings.get(position).getText(),
					BufferType.SPANNABLE);
			getEachWord(tvReads);
			//tvReads.setTextColor(Color.WHITE);
			tvReads.setMovementMethod(LinkMovementMethod.getInstance());
		//	tvReads.setText(listReadings.get(position).getText());
			if (listReadings.get(position).getIsShowReads()) {
				tvReads.setVisibility(View.VISIBLE);
			} else {
				tvReads.setVisibility(View.GONE);

			}
			tvSource.setText(listReadings.get(position).getSource());
			RelativeLayout reMain = (RelativeLayout) convertView
					.findViewById(R.id.re_reading_main);

			reMain.setBackgroundColor(getResources().getColor(
					Colors[listReadings.get(position).getRandomColor()]));
			tvReads.setBackgroundColor(getResources().getColor(
					Colors[listReadings.get(position).getRandomColor()]));

			reMain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listReadings.get(position).getIsShowReads()) {
						tvReads.setVisibility(View.GONE);
						listReadings.get(position).setIsShowReads(false);
					} else {
						tvReads.setVisibility(View.VISIBLE);
						listReadings.get(position).setIsShowReads(true);

					}
					if (listReadings.get(position).getText() == "") {

						getReadingDeatail(listReadings.get(position).getId(),
								tvReads, position);
					}
				}
			});
			return convertView;
		}

	}

	public void getReadingDeatail(String id, final TextView view,
			final int position) {

		RequestParams rp = new RequestParams();
		rp.put("id", id);

		reqhandle = ahc.post("http://app.01teacher.cn/App/GetReadingsByID",

		rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Log.i("cc...getreads", "start...");
				// thisdialog.show();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.i("cc...cars", "success.......");
				// Toast.makeText(getActivity(), "添加成功", 0).show();
				try {
					obj = new JSONObject(content);
					
					
					view.setText(obj.optString("text"));
					view.setText(obj.optString("text"),
							BufferType.SPANNABLE);
					getEachWord(view);
					readList.get(position).setText(obj.optString("text"));
					// tvText.setText(read.getText(),
					// BufferType.SPANNABLE);getEachWord(tvText);

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
				// thisdialog.dismiss();
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
		// 改变选中文本的高亮颜色
	//	textView.setHighlightColor(Color.TRANSPARENT);
		//textView.setTextColor(Color.WHITE);

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
	private ClickableSpan getClickableSpan(final TextView tvText) {
		return new ClickableSpan() {
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
					tv.setTextColor(Color.WHITE);

					getEachWord(tvText);

					Log.d("tapped on:", strTransWords);
					Toast.makeText(getActivity(), strTransWords, 1).show();
					

				} catch (Exception e) {
					// TODO: handle exception
					Log.i("cc", "hahahfhahdfiaofhioehfieo");

				}

			}

			@Override
			public void updateDrawState(TextPaint ds) {
				//ds.setColor(Color.BLACK);
				ds.setUnderlineText(false);
			}
		};
	}
}
