package com.example.realtimebasketball.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.realtimebasketball.util.Logger;

import android.util.Log;

/**
 * @author dfs212 从虎扑NBA首页获取图片
 */
public class GetPhotoService {
	public static List<String> getPhotoUrl() {
		List<String> urls = new ArrayList<String>();

		String baseurl = "http://nba.hupu.com/";

		try {
			Document doc = Jsoup.connect(baseurl).get();
			if (doc != null) {
				Element e = doc.getElementById("ifocus_piclist");
				Elements photos = e.child(0).children();
				for (int i = 0; i < photos.size(); i++) {
					System.out.println("photos.size()--->" + photos.size());
					String url = photos.get(i).child(0).child(0).attr("src");
					Logger.i("url--->" + url);
					urls.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urls;
	}
}
