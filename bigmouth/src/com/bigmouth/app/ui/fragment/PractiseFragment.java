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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PractiseFragment extends Fragment {

	private AsyncHttpClient ahc; // 异步处理
	private RequestHandle reqhandle;
	private Dialog thisdialog;
	private JSONObject obj;
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<TextView>  tvList  = new ArrayList<TextView>();
	private String chinese, usa;
	private TextView tvWord;
	private View contentView;
	private LinearLayout lineWord;
	private LinearLayout lineGuessWord;
	private TextView tvWord1,tvWord2,tvWord3,tvWord4,tvWord5,tvTime;
	private Timer time;
	private int i = 6;
	final Handler handler = new Handler(){  
		    @SuppressLint("NewApi")
			public void handleMessage(Message msg) {  
		       	  i--;
		       	  tvTime.setText(i+"");
		       	  if(i==0){
		       		  time.cancel();
		       	  }
		       	  if(i==3){
		       		  boolean isEqual=false; 
		       		  int  m = new Random().nextInt(4);
		       		  int n= new Random().nextInt(4);
		       		  if(n==m)
		       			  isEqual = true;
		       		  while(isEqual){
		       			   n= new Random().nextInt(4);
		       			   if(n!=m){
		       				   isEqual=false;
		       			   }
		       		  }
		       		  tvList.get(m).setBackground(getResources().getDrawable(R.drawable.bg_addword_color_gray));
		       		  tvList.get(n).setBackground(getResources().getDrawable(R.drawable.bg_addword_color_gray));
		       	  }
		        
         }    
	   };  
	   TimerTask task = new TimerTask(){  
		      public void run() {  
		      Message message = new Message();      
		      message.what = 5;      
		      handler.sendMessage(message);    
		   }  
		};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_practise, container,
				false);
		initVeiw();

		getData();
		return contentView;
	}

	private void initVeiw() {
		time  = new Timer(true);
		
		tvTime = (TextView) contentView.findViewById(R.id.tv_practise_time);
		tvWord1 = (TextView) contentView.findViewById(R.id.btn_pratise_wrong_word1);
		tvList.add(tvWord1);
		tvWord2 = (TextView) contentView.findViewById(R.id.btn_pratise_wrong_word2);
		tvWord3 = (TextView) contentView.findViewById(R.id.btn_pratise_wrong_word3);
		tvWord4 = (TextView) contentView.findViewById(R.id.btn_pratise_wrong_word4);
		tvWord5 = (TextView) contentView.findViewById(R.id.btn_pratise_wrong_word5);
		tvList.add(tvWord2);
		tvList.add(tvWord3);
		tvList.add(tvWord4);
		lineGuessWord = (LinearLayout) contentView.findViewById(R.id.line_guess_word);
		lineWord = (LinearLayout) contentView.findViewById(R.id.line_konw_word);
		ahc = new AsyncHttpClient();
		thisdialog = DialogUtil.getLoadDialog(getActivity(), "请稍后！");
		tvWord = (TextView) contentView.findViewById(R.id.tv_pratise_word);
		contentView.findViewById(R.id.btn_pratise_no).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getData();
			}
		});
		contentView.findViewById(R.id.btn_practis_yes).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lineWord.setVisibility(View.GONE);
				lineGuessWord.setVisibility(View.VISIBLE);
				tvWord1.setText(list.get(0));
				tvWord2.setText(list.get(1));
				tvWord3.setText(list.get(2));
				tvWord4.setText(list.get(3));
				tvWord5.setText(list.get(4));
				time.schedule(task, 1000, 1000);
			}
		});
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
					JSONObject data = obj.optJSONObject("data");
					chinese = data.optString("translation");
					usa = data.optString("word");
					tvWord.setText(usa);
					JSONArray array = data.optJSONArray("rand");
					for (int i = 0; i < array.length(); i++) {
						list.add(array.getString(i));
					}

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
}
