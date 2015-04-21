package com.bigmouth.app.util;

import java.util.concurrent.TimeoutException;





import android.content.Context;
import android.widget.Toast;



public class HttpHandle
{
    public void handleFaile(Context context,Throwable throwable)
    {
        if (context == null || throwable == null)
        {
            return;
        }
        
        if (throwable instanceof TimeoutException)
        {
           Toast.makeText(context, "请求超时", 0).show();
        }
        else
        {
        	Toast.makeText(context, "请求失败，", 0).show();
        }
    }
}
