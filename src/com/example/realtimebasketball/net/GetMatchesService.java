package com.example.realtimebasketball.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.realtimebasketball.model.Dates;
import com.example.realtimebasketball.model.Matches;

/**
 * @author dfs212
 * 
 */
public class GetMatchesService {
	/**
	 * @param day
	 *            默认为0，即当天的直播赛事
	 * @return 直播赛事集合
	 */
	public static List<Matches> getMatches(int day) {

		List<Matches> adds = new ArrayList<Matches>();
		List<String> basketballMatchs = new ArrayList<String>();
		List<String> urls = new ArrayList<String>();
		List<String> dateList = new ArrayList<String>();
		String baseurl = "http://www.zhibo8.cc/";
		int count = 0;
		try {
			Document document = Jsoup.connect("http://www.zhibo8.cc/").get();
			if (document != null) {
				Element e = document.getElementById("body");

				// 抓取比赛场次
				Elements contents = e.getElementsByClass("content");

				Elements teams = contents.get(day).getElementsByTag("li");
				String importantMatch = "";
				for (Element team : teams) {
					count++;
					if (team.child(0).tag().toString().equals("b")) {
						importantMatch = team.child(0).ownText();
					}

					String match = team.ownText() + " " + importantMatch;

					// if (match.contains("篮") || match.contains("CBA")
					// || match.contains("NBA")) {
					basketballMatchs.add(match);
					// 抓取比赛直播连接集合
					urls.add(baseurl
							+ team.getElementsByTag("a").get(0).attr("href"));
					// }
					importantMatch = "";
				}

				for (int i = 0; i < count; i++) {
					if (basketballMatchs != null && basketballMatchs.size() > 0) {
						Matches matches = new Matches();
						matches.setName(basketballMatchs.get(i).substring(6));
						matches.setTime(basketballMatchs.get(i).substring(0, 5));
						matches.setUrl(urls.get(i));
						adds.add(matches);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return adds;
	}

	/**
	 * 
	 * 
	 * @return 直播赛事日期集合
	 */
	public static List<Dates> getLiveDate() {
		List<Dates> dateList = new ArrayList<Dates>();
		try {
			Document document = Jsoup.connect("http://www.zhibo8.cc/").get();
			if (document != null) {
				Element e = document.getElementById("body");

				// 抓取日期
				Elements dates = e.getElementsByClass("titlebar");
				//因为只获取半个月的数据，所以做个标记
				int sign=0;
				for (Element date : dates) {
					if(date.text().contains("星期") && sign<15){
						sign++;
						Dates d = new Dates();
						d.setDates(date.text());
						dateList.add(d);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dateList;
	}
}
