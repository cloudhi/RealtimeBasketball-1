package com.example.realtimebasketball.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.realtimebasketball.model.CbaNews;
import com.example.realtimebasketball.net.HttpUtil;

public class GetCBANewsListService {

	public static List<CbaNews> getNews(int page) {
		List<CbaNews> newss = new ArrayList<CbaNews>();
		try {
			String response = HttpUtil
					.postAndGetDaet("http://m.baidu.com/news?tn=bdapisearch&word=cba&pn="
							+ (page - 1) * 20 + "&rn=20&clk=sortbytime");
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				CbaNews news = new CbaNews();
				news.setTitle(jsonObject.getString("title"));
				news.setSource(jsonObject.getString("author"));
				news.setUrl(jsonObject.getString("url"));
				news.setPhotoUrl(jsonObject.getString("imgUrl"));
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				news.setDate(dateFormat.format(new Date(jsonObject
						.getLong("sortTime") * 1000)));
				System.out.println("news--->" + news);
				newss.add(news);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newss;
	}
}
