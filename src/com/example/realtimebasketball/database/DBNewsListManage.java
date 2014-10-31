package com.example.realtimebasketball.database;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.content.Context;

import com.example.realtimebasketball.model.CbaNews;
import com.example.realtimebasketball.model.Dates;
import com.example.realtimebasketball.model.Matches;
import com.example.realtimebasketball.model.NbaNews;

/**
 * @author dfs212 ���ݿ������
 */
public class DBNewsListManage {

	/**
	 * �����ݿ����NBAnews����
	 * 
	 * @param newss
	 * @param context
	 */
	public void addNbaNewsList(List<NbaNews> newss, Context context) {
		for (NbaNews news : newss) {
			addNbaNews(news, context);
		}
	}

	/**
	 * �����ݿ����CBAnews����
	 * 
	 * @param newss
	 * @param context
	 */
	public void addCbaNewsList(List<CbaNews> newss, Context context) {
		for (CbaNews news : newss) {
			addCbaNews(news, context);
		}
	}

	/**
	 * �����ݿ�������ڼ���
	 * 
	 * @param dl
	 * @param context
	 */
	public void addDateList(List<Dates> dl, Context context) {
		if (dl.size() > 0) {
			clearDatesList(context);
		}
		for (Dates s : dl) {
			addDates(s, context);
		}
	}

	/**
	 * �����ݿ����ֱ����ַ����
	 * 
	 * @param address
	 * @param context
	 */
	public void addAddsList(List<Matches> address, Context context) {
		if (address.size() > 0) {
			clearMatchsList(context);
		}
		for (Matches adds : address) {
			addLives(adds, context);
		}
	}

	/**
	 * �����ݿ��м���һ��NBA���ż�¼
	 * 
	 * @param news
	 *            һ��news
	 * @param context
	 */
	public void addNbaNews(NbaNews news, Context context) {
		news.save();
	}

	/**
	 * �����ݿ��м���һ��CBA���ż�¼
	 * 
	 * @param news
	 *            һ��news
	 * @param context
	 */
	public void addCbaNews(CbaNews news, Context context) {
		news.save();
	}

	/**
	 * �����ݿ��м���һ��ֱ����ַ��¼
	 * 
	 * @param adds
	 * 
	 * @param context
	 */
	public void addLives(Matches adds, Context context) {
		adds.save();
	}

	/**
	 * �����ݿ��м���һ��ֱ�����ڼ�¼
	 * 
	 * @param dtime
	 *            ֱ������
	 * 
	 * @param context
	 */
	public void addDates(Dates dates, Context context) {
		dates.save();
	}

	/**
	 * �����ݿ���ȡ��10�����ż�������
	 * 
	 * @param context
	 * @return
	 */
	public List<NbaNews> getNbaNews(Context context, int currentPage) {
		List<NbaNews> newss = new ArrayList<NbaNews>();
		newss = DataSupport.limit(20).offset((currentPage - 1) * 20)
				.find(NbaNews.class);
		return newss;
	}

	/**
	 * �����ݿ���ȡ��10����������
	 * 
	 * @param context
	 * @return
	 */
	public List<CbaNews> getCbaNews(Context context, int currentPage) {
		List<CbaNews> newss = new ArrayList<CbaNews>();
		newss = DataSupport.limit(20).offset((currentPage - 1) * 20)
				.find(CbaNews.class);
		return newss;
	}

	/**
	 * ȡ��ֱ����ַ��������
	 * 
	 * @param context
	 * @return
	 */
	public List<Matches> getLives(Context context) {
		List<Matches> address = new ArrayList<Matches>();
		address = DataSupport.findAll(Matches.class);
		return address;
	}

	/**
	 * ȡ�����ڼ�������
	 * 
	 * @param context
	 * @return
	 */
	public List<Dates> getDates(Context context) {
		List<Dates> dates = new ArrayList<Dates>();
		dates = DataSupport.findAll(Dates.class);
		return dates;
	}

	/**
	 * �����������
	 * 
	 * @param context
	 */
	public void clearNbaNewsList(Context context) {
		DataSupport.deleteAll(NbaNews.class);
	}

	/**
	 * �����������
	 * 
	 * @param context
	 */
	public void clearCbaNewsList(Context context) {
		DataSupport.deleteAll(CbaNews.class);
	}

	/**
	 * ���ֱ����������
	 * 
	 * @param context
	 */
	public void clearMatchsList(Context context) {
		DataSupport.deleteAll(Matches.class);
	}

	/**
	 * �����������
	 * 
	 * @param context
	 */
	public void clearDatesList(Context context) {
		DataSupport.deleteAll(Dates.class);
	}
}
