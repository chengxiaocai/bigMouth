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
import com.bigmouth.app.bean.Readings;
import com.bigmouth.app.ui.StudyActivity1;
import com.bigmouth.app.util.DialogUtil;
import com.bigmouth.app.util.HttpHandle;
import com.bigmouth.app.util.PersistentUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
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
	private int GussNmu = 0, totalNum = 0;
	private RelativeLayout reCoffee, reTea, reFeast;
	private LinearLayout lineUi1, lineUi2, lineUi3, lineUi4, lineUi5;
	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<TextView> tvList = new ArrayList<TextView>();
	private String chinese, usa;
	private TextView tvWord, tvChineseRight, tvEnglishRight, tvChoose,
			tvJingdu;
	private View contentView;
	private LinearLayout lineWord;
	private LinearLayout lineGuessWord;
	private LinearLayout lineRight;
	private TextView tvWord1, tvWord2, tvWord3, tvWord4, tvWord5, tvWord6,
			tvTime, tvShow, tvResultEnglist, tvResultChinese, tvResultEnglist1,
			tvResultChinese1;
	private Timer time;
	private TextView tvFinalShow;
	private ImageView ivSuccess;
	private TextView tvTotal;
	private int i = 6;
	private int type;
	StudyActivity1 ac;
	final Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {
			i--;

			if (i == 2) {
				time.cancel();
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
								R.drawable.bg_text__all_coner_gray));
				tvList.get(n).setBackground(
						getResources().getDrawable(
								R.drawable.bg_text__all_coner_gray));
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

			@SuppressLint("NewApi")
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
					tvWord2.setText(list.get(0));
					tvWord3.setText(list.get(1));
					tvWord4.setText(list.get(2));
					tvWord5.setText(list.get(3));
					tvWord6.setText(list.get(4));
					tvWord2.setBackground(getResources().getDrawable(
							R.drawable.bg_text__all_coner_tooblue));
					tvWord3.setBackground(getResources().getDrawable(
							R.drawable.bg_text__all_coner_tooblue));
					tvWord4.setBackground(getResources().getDrawable(
							R.drawable.bg_text__all_coner_tooblue));
					tvWord5.setBackground(getResources().getDrawable(
							R.drawable.bg_text__all_coner_tooblue));
					tvWord6.setBackground(getResources().getDrawable(
							R.drawable.bg_text__all_coner_tooblue));
					time = new Timer(true);
					TimerTask task = new TimerTask() {
						public void run() {
							Message message = new Message();
							message.what = 5;
							handler.sendMessage(message);
						}
					};
					time.schedule(task, 1000, 1000);
					i = 6;
				} catch (Exception e) {
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

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		ac = (StudyActivity1) activity;

	}

	private void initView() {
		tvTotal = (TextView) contentView.findViewById(R.id.tv_pratise_total);

		ivSuccess = (ImageView) contentView
				.findViewById(R.id.iv_pratise_success);
		tvFinalShow = (TextView) contentView.findViewById(R.id.finalshow);
		tvJingdu = (TextView) contentView.findViewById(R.id.tv_pratise_jindu);
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
				if(time!=null){
					time.cancel();
				}
			}
		});
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "请稍后！");
		tvWord2 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word2);
		tvWord3 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word3);
		tvWord4 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word4);
		tvWord5 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word5);
		tvWord6 = (TextView) contentView
				.findViewById(R.id.btn_pratise_wrong_word6);
		tvList.add(tvWord2);
		tvList.add(tvWord3);
		tvList.add(tvWord4);
		tvList.add(tvWord5);
		tvList.add(tvWord6);
		for (int i = 0; i < tvList.size(); i++) {
			final TextView tv = tvList.get(i);
			tv.setOnClickListener(new OnClickListener() {

				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					time.cancel();
					if (chinese.equals(tv.getText().toString())) {
						totalNum++;
						Log.i(GussNmu + "", totalNum + "");
						if (GussNmu == totalNum) {
							lineUi2.setVisibility(View.GONE);
							lineUi5.setVisibility(View.VISIBLE);
							if (type == 1) {
								UpLoadPoint();
								tvFinalShow.setText("完成了Coffee Break挑战练习");
								ivSuccess.setBackground(getActivity()
										.getResources().getDrawable(
												R.drawable.success3));
							} else if (type == 2) {
								UpLoadPoint();

								tvFinalShow.setText("完成了Afternoon Tea挑战练习");
								ivSuccess.setBackground(getActivity()
										.getResources().getDrawable(
												R.drawable.success2));

							} else {
								UpLoadPoint();

								tvFinalShow.setText("完成了Word Feast挑战练习");
								ivSuccess.setBackground(getActivity()
										.getResources().getDrawable(
												R.drawable.success1));

							}
							totalNum = 0;

							return;
						}
						lineUi2.setVisibility(View.GONE);
						lineUi3.setVisibility(View.VISIBLE);
						tvResultChinese.setText(chinese);
						tvResultEnglist.setText(usa);
						tvJingdu.setText("已完成进度：" + totalNum + "/" + GussNmu);

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
		lineUi5 = (LinearLayout) contentView.findViewById(R.id.ll_ui5);
		contentView.findViewById(R.id.re_practise_coffee).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getData();
						lineUi1.setVisibility(View.GONE);
						lineUi2.setVisibility(View.VISIBLE);
						GussNmu = 5;
						type = 1;
						ac.setPractise(true);
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
						type = 2;

						ac.setPractise(true);

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
						type = 3;

						ac.setPractise(true);

					}
				});
		contentView.findViewById(R.id.tv_pratise_next).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						time.cancel();
						lineUi3.setVisibility(View.GONE);
						lineUi2.setVisibility(View.VISIBLE);
						getData();
					}
				});
		contentView.findViewById(R.id.tv_pratise_next1).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						lineUi4.setVisibility(View.GONE);
						lineUi2.setVisibility(View.VISIBLE);
						getData();
					}
				});
		contentView.findViewById(R.id.tv_pratise_again).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Show();
					}
				});
	}

	public void Show() {
		lineUi5.setVisibility(View.GONE);
		lineUi1.setVisibility(View.VISIBLE);
	}

	public void ShowVoice() {
		MediaPlayer mediaPlayer = new MediaPlayer();
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.reset();// 重置为初始状态
		}

	}
	public void UpdateUi(){
		if(time!=null){
			
			time.cancel();
		}

		lineUi1.setVisibility(View.VISIBLE);
		lineUi2.setVisibility(View.GONE);
		lineUi3.setVisibility(View.GONE);
		lineUi4.setVisibility(View.GONE);
		lineUi5.setVisibility(View.GONE);

	}

	public void UpLoadPoint() {
		RequestParams rp = new RequestParams();
		rp.put("UserID",PersistentUtil.getInstance().readString(getActivity(), "id", ""));
		rp.put("Type", "4");
		reqhandle = ahc.post("http://app.01teacher.cn/App/PostUserPoints",

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
				Log.i("cc", content);
				try {
					obj = new JSONObject(content);
					if(obj.optBoolean("success")){
						
					
						tvTotal.setText(obj.optString("point") + "  points");
						Intent mIntent = new Intent("com.cc.getnum");

						// 发送广播
						getActivity().sendBroadcast(mIntent);

					}else{
						Toast.makeText(getActivity(), "获取积分失败！", 0).show();

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
				Toast.makeText(getActivity(), "上传积分失败！", 0).show();
			
			}

		});

	}

	
}
