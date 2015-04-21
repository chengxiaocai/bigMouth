package com.bigmouth.app.ui.fragment;



import com.bigmouth.app.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class WordsFragment extends Fragment {

	private WebView mWebView;
	private ListView  lvWords;
	private View contentView=null;
    LayoutInflater inflater =null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		   contentView=inflater.inflate(R.layout.fragment_words,container, false);
            initView();
		return contentView;
	}
	private void initView(){
		inflater= LayoutInflater.from(getActivity());
		lvWords = (ListView) contentView.findViewById(R.id.lv_words_list);
		lvWords.setAdapter(new WordsAdapter());
		contentView.findViewById(R.id.tv_words_addword)
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(
					getActivity());
                final EditText etWord;
				final EditText etChinese;
				dialog.getWindow().requestFeature(
						Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.item_dialog_addword);
				dialog.setTitle(null);
				dialog.show();
				etChinese = (EditText) dialog.findViewById(R.id.et_chiniese);
				etWord = (EditText) dialog.findViewById(R.id.et_word);
				dialog.findViewById(R.id.btn_dialog_word_addword_ok).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(TextUtils.isEmpty(etChinese.getText().toString().trim())){
							Toast.makeText(getActivity(),"请输入翻译内容", 0).show();
							return;
						}
						if(TextUtils.isEmpty(etWord.getText().toString().trim())){
							Toast.makeText(getActivity(),"请输入单词", 0).show();
							return;
						}
						dialog.dismiss();
						addWord(etWord.getText().toString().trim(),etChinese.getText().toString().trim());
					}
				});
				dialog.findViewById(R.id.btn_dialog_word_addword_no).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				

			}
		});

	}
	private class  WordsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
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
			if(convertView==null){
				convertView = inflater.inflate(R.layout.item_words, null);
			}
			return convertView;
		}
		
	}
	public void addWord(String word,String chinese){
		
	}
}
