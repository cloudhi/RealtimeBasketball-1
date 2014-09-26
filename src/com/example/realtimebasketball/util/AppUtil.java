package com.example.realtimebasketball.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;

/**
 * 
 * @author zhy
 * 
 */
public class AppUtil
{
	/**
	 * ��ȡ�ϴθ��µ�ʱ��
	 * 
	 * @param newType
	 * @return
	 */
	public static String getRefreashTime(Context context)
	{
		String timeStr = PreferenceUtil.readString(context, "NEWS_");
		if (TextUtils.isEmpty(timeStr))
		{
			return "�Һñ���������...";
		}
		return timeStr;
	}
	
	
	/**
	 * �����ϴθ��µ�ʱ��
	 * 
	 * @param newType
	 * @return
	 */
	public static void setRefreashTime(Context context)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PreferenceUtil.write(context, "NEWS_",df.format(new Date()));
	}
}