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
	 *            Ĭ��Ϊ0���������ֱ������
	 * @return ֱ�����¼���
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

				// ץȡ��������
				Elements contents = e.getElementsByClass("content");

				Elements teams = contents.get(day).getElementsByTag("li");
				String importantMatch = "";
				for (Element team : teams) {
					count++;
					if (team.child(0).tag().toString().equals("b")) {
						importantMatch = team.child(0).ownText();
					}

					String match = team.ownText() + " " + importantMatch;

					// if (match.contains("��") || match.contains("CBA")
					// || match.contains("NBA")) {
					basketballMatchs.add(match);
					// ץȡ����ֱ�����Ӽ���
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
	 * @return ֱ���������ڼ���
	 */
	public static List<Dates> getLiveDate() {
		List<Dates> dateList = new ArrayList<Dates>();
		try {
			Document document = Jsoup.connect("http://www.zhibo8.cc/").get();
			if (document != null) {
				Element e = document.getElementById("body");

				// ץȡ����
				Elements dates = e.getElementsByClass("titlebar");
				//��Ϊֻ��ȡ����µ����ݣ������������
				int sign=0;
				for (Element date : dates) {
					if(date.text().contains("����") && sign<15){
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
