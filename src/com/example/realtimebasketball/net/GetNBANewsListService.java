package com.example.realtimebasketball.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.realtimebasketball.model.NbaNews;
import com.example.realtimebasketball.net.HttpUtil;
import com.example.realtimebasketball.util.Logger;

public class GetNBANewsListService {

	public static List<NbaNews> getNews(int page, String key) {
		List<NbaNews> newss = new ArrayList<NbaNews>();
		try {
			String response = HttpUtil
					.postAndGetDaet("http://m.baidu.com/news?tn=bdapisearch&word=nba"
							+ key + "&pn=" + page * 20);
			Logger.e(response);
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String source = jsonObject.getString("author");
				String title = jsonObject.getString("title");
				// 去除部分干扰信息
				if (!title.contains("视频")
						&& (source.contains("体育") || source.contains("东方")
								|| source.contains("凤凰")
								|| source.contains("新浪")
								|| source.contains("nba") || source
									.contains("虎扑") || source
									.contains("网易")|| source
									.contains("21CN"))) {
					NbaNews news = new NbaNews();
					news.setTitle(jsonObject.getString("title"));
					news.setSource(jsonObject.getString("author"));
					news.setUrl(jsonObject.getString("url"));
					news.setPhotoUrl(jsonObject.getString("imgUrl"));
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					news.setDate(dateFormat.format(new Date(jsonObject
							.getLong("sortTime") * 1000)));

					newss.add(news);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newss;
	}
}
