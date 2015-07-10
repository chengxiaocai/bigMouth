package com.bigmouth.app.ui.fragment;

import java.util.ArrayList;
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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	LayoutInflater inflater = null;
	private ListView lvReading;
	private View contentView;
	private ReadingsAdapter adapter;
	private StudyActivity1 ac;// 父activitiy对象；
	private String id;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private int Color [] = new int[]{R.color.color1,R.color.color3,R.color.color4,R.color.color5,R.color.color6,R.color.color7,R.color.color8,R.color.color9,R.color.color10,R.color.color11,R.color.color12};


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

		lvReading = (ListView) contentView.findViewById(R.id.lv_reading_list);
		adapter = new ReadingsAdapter(readList);
		lvReading.setAdapter(adapter);
		lvReading.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent in = new Intent();

				in.putExtra("text", readList.get(position).getId());
				ac.changeReadingPage(in);
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
		public View getView(int position, View convertView, ViewGroup parent) {
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
			tvSource.setText(listReadings.get(position).getSource());
			RelativeLayout reMain = (RelativeLayout) convertView.findViewById(R.id.re_reading_main);
			reMain.setBackgroundColor(getResources().getColor(Color[new Random().nextInt(11) ]));

			return convertView;
		}

	}
}
