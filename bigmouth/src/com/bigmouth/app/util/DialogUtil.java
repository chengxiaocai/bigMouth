package com.bigmouth.app.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.View.OnClickListener;

/**
 * 提示框工具类
 * 
 * @author 程才
 * @date 2014-4-3
 */
public final class DialogUtil {
	private DialogUtil() {

	}

	public static LoadingProgressDialog getLoadDialog(Context ctx,
			String message) {
		return new LoadingProgressDialog(ctx, message);
	}

}
