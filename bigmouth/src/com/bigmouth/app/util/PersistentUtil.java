
package com.bigmouth.app.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public final class PersistentUtil
{
    /**
     * �ļ����
     */
    private final String FILENAME = "bigmouth";
    
    /**
     * �û���¼�˺�KEY
     */
    public static final String KEY_LOGIN_NAME = "username";
    
    /**
     * �û���¼����KEY
     */
    public static final String KEY_LOGIN_PWD = "userpwd";
    
    /**
     * �û���ѯ��¼KEY
     */
    public static final String KEY_SEARCH_RECORD = "searchrecode";
    
    /**
     * �״�����
     */
    public static final String KEY_FIRST_LAUNCH = "firstlaunch";
    
    /**
     * �洢�İ汾��
     */
    public static final String KEY_VERSION = "version";
    
    /**
     * ������¼�ָ���
     */
    public static final String SPLIT_SEARCH_RECORD = "/";
    
    /**
     * �û���¼��ס����KEY
     */
    public static final String KEY_LOGIN_REMEMBER_PWD = "isRememberPWD";
    
    /**
     * �Ƿ����̳���Ϣ
     */
    public static final String KEY_HASNO_COMPLETE_INFO = "hascominfo";
    
    private static final PersistentUtil INSTANCE = new PersistentUtil();
    
    private PersistentUtil()
    {
        
    }
    
    public static PersistentUtil getInstance()
    {
        return INSTANCE;
    }
    
    public void write(Context ctx, String key, String value)
    {
        if (ctx == null)
        {
            return;
        }
        
        SharedPreferences sp = ctx.getSharedPreferences(FILENAME,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        
        editor.commit();
    }
    
    public void write(Context ctx, String key, boolean value)
    {
        if (ctx == null)
        {
            return;
        }
        
        SharedPreferences sp = ctx.getSharedPreferences(FILENAME,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    public String readString(Context ctx, String key, String defValue)
    {
        if (ctx == null)
        {
            return "";
        }
        
        SharedPreferences sp = ctx.getSharedPreferences(FILENAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }
    
    public boolean readBoolean(Context ctx, String key, boolean defValue)
    {
        if (ctx == null)
        {
            return false;
        }
        
        SharedPreferences sp = ctx.getSharedPreferences(FILENAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
    
    public String md5(String string)
    {
        byte[] hash;
        
        try
        {
            hash = MessageDigest.getInstance("MD5")
                    .digest(string.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("MD5 not be supported!", e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("UTF-8 not be supported!", e);
        }
        
        StringBuilder hex = new StringBuilder(hash.length * 2);
        
        for (byte b : hash)
        {
            int i = (b & 0xFF);
            if (i < 0x10)
                hex.append('0');
            hex.append(Integer.toHexString(i));
        }
        
        return hex.toString().toUpperCase(Locale.getDefault());
    }
    
}
