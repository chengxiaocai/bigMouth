package com.bigmouth.app.ui;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bigmouth.app.R;
import com.bigmouth.app.util.Constant;
import com.bigmouth.app.util.StreamTools;

import android.app.Activity;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

public class ReadActivity extends Activity {
	private TextView testText = null;
	private TextView  tvSrcWord,tvResultWrod;
	private LinearLayout line;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);
		line = (LinearLayout) findViewById(R.id.line_read);
		testText = (TextView) findViewById(R.id.tv_read_content);
		tvResultWrod = (TextView) findViewById(R.id.tv_result_word);
		tvSrcWord = (TextView) findViewById(R.id.tv_src_word);
		testText.setText(getResources().getString(R.string.text),
				BufferType.SPANNABLE);
		getEachWord(testText);
		testText.setMovementMethod(LinkMovementMethod.getInstance());
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
			ClickableSpan clickSpan = getClickableSpan();
			// to cater last/only word
			end = (i < indices.length ? indices[i] : spans.length());
			spans.setSpan(clickSpan, start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = end + 1;
		}
		// 改变选中文本的高亮颜色
		textView.setHighlightColor(Color.TRANSPARENT);

	}

	private ClickableSpan getClickableSpan() {
		return new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				TextView tv = (TextView) widget;
				String s = tv
						.getText()
						.subSequence(tv.getSelectionStart(),
								tv.getSelectionEnd()).toString();
				Log.d(tv.getSelectionStart() + "", tv.getSelectionEnd() + "");
				SpannableStringBuilder style = new SpannableStringBuilder(tv
						.getText().toString());
				style.setSpan(new BackgroundColorSpan(Color.GRAY),
						tv.getSelectionStart(), tv.getSelectionEnd(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tv.setText(style);
				getEachWord(testText);

				Log.d("tapped on:", s);
				Study(s);
				Toast.makeText(ReadActivity.this, s, 0).show();
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setColor(Color.BLACK);
				ds.setUnderlineText(false);
			}
		};
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

	public void Study(String word) {
		Load(word);
		if (line.getVisibility() == View.VISIBLE) {
			// AlphaAnimation animation = new
			// AlphaAnimation(1.0F, 0F);
			TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -400);
			animation.setDuration(100);
			animation.start();
			line.setAnimation(animation);
			line.setVisibility(View.GONE);

		} else {
			/*
			 * TranslateAnimation tra= new TranslateAnimation(0, 0, 0.0F, 1.0F);
			 * tra.setDuration(5000); //tra.start(); tv.setAnimation(tra);
			 * tv.startAnimation(tra);
			 */
			TranslateAnimation animation = new TranslateAnimation(0, 0, -400, 0);
			animation.setDuration(100);
			animation.start();
			line.setAnimation(animation);

			line.setVisibility(View.VISIBLE);
		}

		// if (tv.VISIBLE==View.INVISIBLE) {
		// tv.setVisibility(View.VISIBLE);

		// }

	}

	public void Load(final String word) {

		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result=null;
				try {

					HttpClient client = new DefaultHttpClient();

					HttpPost post = new HttpPost(params[0]);

					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("from", "en"));
					parameters.add(new BasicNameValuePair("q", word));
					parameters.add(new BasicNameValuePair("to", "zh"));
					parameters.add(new BasicNameValuePair("client_id",
							Constant.BAIDU_APP_KEY));
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							parameters, "utf-8");
					post.setEntity(entity);

					HttpResponse response = client.execute(post);
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						InputStream is = response.getEntity().getContent();
						result = StreamTools.readFromStream(is);
						
					} else {
						Toast.makeText(ReadActivity.this, "服务器异常", 1).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;

			}

			@Override
			protected void onPostExecute(String response) {
				// TODO Auto-generated method stub
				super.onPostExecute(response);
				if(response!=null){
					Log.i("cc....response", response);
					try {
						JSONObject json = new JSONObject(response);
						JSONArray json1 = json.optJSONArray("trans_result");
						JSONObject json2 = json1.getJSONObject(0);
						String dst = json2.optString("dst");
						String src = json2.optString("src");
						tvResultWrod.setText(dst);
						tvSrcWord.setText(src);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					} else {
						Toast.makeText(ReadActivity.this, "访问失败", 0).show();
					}
				}
			
		}.execute(Constant.BAIDU_TRANSLATE_URL);
	}

}
