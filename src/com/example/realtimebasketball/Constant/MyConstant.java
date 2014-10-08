package com.example.realtimebasketball.Constant;

import android.os.Environment;

public class MyConstant {
	public final static String UESRAGENT_PHONE = "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A405 Safari/8536.25";

	// 应用名称
	public static String APP_NAME = "RealtimeBasketbll";

	// 保存参数文件夹名称
	public static final String SHARED_PREFERENCE_NAME = "realtime_basketball_prefs";

	// SDCard路径
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/realtimebasketball";

	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";
}
