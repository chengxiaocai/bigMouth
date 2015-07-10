package com.bigmouth.app.ui.fragment;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class PractiseFragment extends Fragment {
	private ImageView ivUnknow;
	private int GussNmu = 0,totalNum=0;
	private RelativeLayout reCoffee, reTea, reFeast;
	private LinearLayout lineUi1, lineUi2, lineUi3, lineUi4;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<TextView> tvList = new ArrayList<TextView>();
	private String chinese, usa;
	private TextView tvWord, tvChineseRight, tvEnglishRight, tvChoose;
	private View contentView;
	private LinearLayout lineWord;
	private LinearLayout lineGuessWord;
	private LinearLayout lineRight;
	private TextView tvWord1, tvWord2, tvWord3, tvWord4, tvWord5, tvTime,
			tvShow, tvResultEnglist, tvResultChinese, tvResultEnglist1, tvResultChinese1;
	private Timer time;
	private int i = 6;
	final Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {

			i--;
			tvTime.setText(i + "");
			if (i == 0) {
				time.cancel();
				lineGuessWord.setVisibility(View.GONE);
				lineRight.setVisibility(View.VISIBLE);
				tvChineseRight.setText(chinese);
				tvEnglishRight.setText(usa);
				tvShow.setText("不要担心，下次谨记");

			}
			if (i == 3) {
				int m, n;
				while (true) {
					m = new Random().nextInt(5);
					if (!list.get(m).equals(chinese)) {
						break;
					}
				}
				while (true) {
					n = new Random().nextInt(5);
					if (!list.get(n).equals(chinese) && n != m) {
						break;
					}
				}

				tvList.get(m).setBackground(
						getResources().getDrawable(
								R.drawable.bg_addword_color_gray));
				tvList.get(n).setBackground(
						getResources().getDrawable(
								R.drawable.bg_addword_color_gray));
			}

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_practise_pre,
				container, false);
		// initVeiw();

		// getData();
		initView();
		return contentView;
	}

	private void initVeiw() {
		tvShow = (TextView) contentView.findViewById(R.id.tv_practise_show);
		tvChoose = (TextView) contentView
				.findViewById(R.id.tv_pratise_choose_word);
		lineRight = (LinearLayout) contentView
				.findViewById(R.id.line_result_word);
		tvChineseRight = (TextView) contentView
				.findViewById(R.id.tv_right_chinese);
		tvEnglishRight = (TextView) contentView.findViewById(R.id.tv_right_usa);

		tvTime = (TextView) contentView.findViewById(R.id.tv_practise_time);

		tvWord2 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word2);
		tvWord3 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word3);
		tvWord4 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word4);
		tvWord5 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word5);
		tvList.add(tvWord2);
		tvList.add(tvWord3);
		tvList.add(tvWord4);
		tvList.add(tvWord5);
		lineGuessWord = (LinearLayout) contentView
				.findViewById(R.id.line_guess_word);
		lineWord = (LinearLayout) contentView.findViewById(R.id.line_konw_word);
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "请稍后！");
		tvWord = (TextView) contentView.findViewById(R.id.tv_pratise_word);
		contentView.findViewById(R.id.btn_pratise_no).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// time.cancel();
						// lineGuessWord.setVisibility(View.GONE);
						lineWord.setVisibility(View.GONE);
						lineRight.setVisibility(View.VISIBLE);
						tvChineseRight.setText(chinese);
						tvEnglishRight.setText(usa);
						tvShow.setText("不要担心，下次谨记");

					}
				});
		contentView.findViewById(R.id.btn_pratise_tonext).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						lineWord.setVisibility(View.VISIBLE);
						lineRight.setVisibility(View.GONE);
						getData();
					}
				});
		contentView.findViewById(R.id.btn_practis_yes).setOnClickListener(
				new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						i = 6;
						tvChoose.setText(usa);
						tvTime.setText(i + "");
						tvWord1.setBackground(getResources().getDrawable(
								R.drawable.bg_text__all_coner_tooblue));
						tvWord2.setBackground(getResources().getDrawable(
								R.drawable.bg_text__all_coner_tooblue));
						tvWord3.setBackground(getResources().getDrawable(
								R.drawable.bg_text__all_coner_tooblue));
						tvWord4.setBackground(getResources().getDrawable(
								R.drawable.bg_text__all_coner_tooblue));
						tvWord5.setBackground(getResources().getDrawable(
								R.drawable.bg_text__all_coner_tooblue));
						lineWord.setVisibility(View.GONE);
						lineGuessWord.setVisibility(View.VISIBLE);
						tvWord1.setText(list.get(0));
						tvWord2.setText(list.get(1));
						tvWord3.setText(list.get(2));
						tvWord4.setText(list.get(3));
						tvWord5.setText(list.get(4));
						time = new Timer(true);
						TimerTask task = new TimerTask() {
							public void run() {
								Message message = new Message();
								message.what = 5;
								handler.sendMessage(message);
							}
						};
						time.schedule(task, 1000, 1000);
					}
				});
		for (int i = 0; i < tvList.size(); i++) {
			final TextView tv = tvList.get(i);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					time.cancel();
					lineGuessWord.setVisibility(View.GONE);
					lineRight.setVisibility(View.VISIBLE);
					tvChineseRight.setText(chinese);
					tvEnglishRight.setText(usa);
					if (chinese.equals(tv.getText().toString())) {
						tvShow.setText("不错，答对啦");
					} else {
						tvShow.setText("不要担心，下次谨记");
					}
				}
			});
		}
	}

	/**
	 * 获取随即单词
	 */
	public void getData() {
		RequestParams rp = new RequestParams();
		rp.put("UserID",
				PersistentUtil.getInstance()
						.readString(getActivity(), "id", ""));

		reqhandle = ahc.post("http://app.01teacher.cn/App/GetUserWord",

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
					JSONObject data = obj.optJSONObject("data");
					chinese = data.optString("translation");
					usa = data.optString("word");
					tvWord.setText(usa);
					JSONArray array = data.optJSONArray("rand");
					list.clear();
					for (int i = 0; i < array.length(); i++) {
						list.add(array.getString(i));
					}
					tvWord2.setText(list.get(1));
					tvWord3.setText(list.get(2));
					tvWord4.setText(list.get(3));
				} catch (JSONException e) {
					Toast.makeText(getActivity(), "获取单词失败", 0).show();
					e.printStackTrace();
				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.i("cc...", "finish");
				if (thisdialog.isShowing()) {
					thisdialog.dismiss();
				}
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
					thisdialog.dismiss();
				}
			}

		});
	}

	private void initView() {
		tvResultChinese = (TextView) contentView
				.findViewById(R.id.tv_result_chinese);
		tvResultEnglist = (TextView) contentView
				.findViewById(R.id.tv_result_englisg);
		tvResultChinese1 = (TextView) contentView
				.findViewById(R.id.tv_result_chinese1);
		tvResultEnglist1 = (TextView) contentView
				.findViewById(R.id.tv_result_englisg1);
		tvWord = (TextView) contentView.findViewById(R.id.tv_pratise_word);
		ivUnknow = (ImageView) contentView
				.findViewById(R.id.iv_practise_unkonw);
		ivUnknow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getData();
			}
		});
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "");
		tvWord2 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word2);
		tvWord3 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word3);
		tvWord4 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word4);
		tvList.add(tvWord4);
		tvList.add(tvWord3);
		tvList.add(tvWord2);
		for (int i = 0; i < tvList.size(); i++) {
			final TextView tv = tvList.get(i);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (chinese.equals(tv.getText().toString())) {
						totalNum++;
						if(GussNmu==totalNum){
							
						}
						lineUi2.setVisibility(View.GONE);
						lineUi3.setVisibility(View.VISIBLE);
						tvResultChinese.setText(chinese);
						tvResultEnglist.setText(usa);

					} else {
						lineUi2.setVisibility(View.GONE);
						lineUi4.setVisibility(View.VISIBLE);
						tvResultChinese1.setText(chinese);
						tvResultEnglist1.setText(usa);
					}
				}
			});
		}
		lineUi1 = (LinearLayout) contentView.findViewById(R.id.ll_ui1);
		lineUi2 = (LinearLayout) contentView.findViewById(R.id.ll_ui2);
		lineUi3 = (LinearLayout) contentView.findViewById(R.id.ll_ui3);
		lineUi4 = (LinearLayout) contentView.findViewById(R.id.ll_ui4);
		contentView.findViewById(R.id.re_practise_coffee).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getData();
						lineUi1.setVisibility(View.GONE);
						lineUi2.setVisibility(View.VISIBLE);
						GussNmu = 5;
					}
				});
		contentView.findViewById(R.id.re_practise_tea).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getData();
						lineUi1.setVisibility(View.GONE);
						lineUi2.setVisibility(View.VISIBLE);
						GussNmu = 10;
					}
				});
		contentView.findViewById(R.id.re_practise_feast).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getData();
						lineUi1.setVisibility(View.GONE);
						lineUi2.setVisibility(View.VISIBLE);
						GussNmu = 20;
					}
				});
		contentView.findViewById(R.id.tv_pratise_next).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lineUi3.setVisibility(View.GONE);
				lineUi2.setVisibility(View.VISIBLE);
				getData();
			}
		});
		contentView.findViewById(R.id.tv_pratise_next1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lineUi3.setVisibility(View.GONE);
				lineUi2.setVisibility(View.VISIBLE);
				getData();
			}
		});
	}
}
