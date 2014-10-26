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
 * @author dfs212 数据库操作类
 */
public class DBNewsListManage {

	/**
	 * 向数据库加入NBAnews集合
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
	 * 向数据库加入CBAnews集合
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
	 * 向数据库加入日期集合
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
	 * 向数据库加入直播地址集合
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
	 * 向数据库中加入一条NBA新闻记录
	 * 
	 * @param news
	 *            一个news
	 * @param context
	 */
	public void addNbaNews(NbaNews news, Context context) {
		news.save();
	}

	/**
	 * 向数据库中加入一条CBA新闻记录
	 * 
	 * @param news
	 *            一个news
	 * @param context
	 */
	public void addCbaNews(CbaNews news, Context context) {
		news.save();
	}

	/**
	 * 向数据库中加入一条直播地址记录
	 * 
	 * @param adds
	 * 
	 * @param context
	 */
	public void addLives(Matches adds, Context context) {
		adds.save();
	}

	/**
	 * 向数据库中加入一条直播日期记录
	 * 
	 * @param dtime
	 *            直播日期
	 * 
	 * @param context
	 */
	public void addDates(Dates dates, Context context) {
		dates.save();
	}

	/**
	 * 从数据库中取出10条新闻集合数据
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
	 * 从数据库中取出10条新闻数据
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
	 * 取出直播地址集合数据
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
	 * 取出日期集合数据
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
	 * 清空新闻数据
	 * 
	 * @param context
	 */
	public void clearNbaNewsList(Context context) {
		DataSupport.deleteAll(NbaNews.class);
	}

	/**
	 * 清空新闻数据
	 * 
	 * @param context
	 */
	public void clearCbaNewsList(Context context) {
		DataSupport.deleteAll(CbaNews.class);
	}

	/**
	 * 清空直播赛事数据
	 * 
	 * @param context
	 */
	public void clearMatchsList(Context context) {
		DataSupport.deleteAll(Matches.class);
	}

	/**
	 * 清空日期数据
	 * 
	 * @param context
	 */
	public void clearDatesList(Context context) {
		DataSupport.deleteAll(Dates.class);
	}
}
