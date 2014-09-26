package com.example.realtimebasketball.fragment;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.realtimebasketball.NewsDetailsActivity;
import com.example.realtimebasketball.R;
import com.example.realtimebasketball.adapter.NbaNewsAdapter;
import com.example.realtimebasketball.database.DBNewsListManage;
import com.example.realtimebasketball.model.NbaNews;
import com.example.realtimebasketball.net.GetNBANewsListService;
import com.example.realtimebasketball.net.NetUtil;
import com.example.realtimebasketball.util.AppUtil;
import com.example.realtimebasketball.util.Logger;

public class NbaNewsListViewFragment extends Fragment implements
		IXListViewRefreshListener, IXListViewLoadMore {

	private FragmentActivity activity;
	private NbaNewsFragment nbaFragment;
	private XListView listView;
	private NbaNewsAdapter adapter;
	private List<NbaNews> newss = new ArrayList<NbaNews>();
	private String key = "";
	// ��ǰҳ��
	private int pageNow = 1;
	private static final int LOAD_MORE = 0x110;
	private static final int LOAD_REFREASH = 0x111;

	private static final int TIP_ERROR_NO_NETWORK = 0X112;
	private static final int TIP_ERROR_SERVER = 0X113;
	public static final String ToastUtil = null;

	/**
	 * �Ƿ���������
	 */
	private boolean isConnNet = false;

	/**
	 * ��ǰ�����Ƿ��Ǵ������л�ȡ��
	 */
	private boolean isLoadingDataFromNetWork = false;
	private DBNewsListManage manager;

	public NbaNewsListViewFragment(NbaNewsFragment nbaFragment, String name) {
		// TODO Auto-generated constructor stub
		this.key = name;
		this.nbaFragment = nbaFragment;
		manager = new DBNewsListManage();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View newsView = inflater.inflate(R.layout.news_list, null);

		return newsView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
		listView = (XListView) activity.findViewById(R.id.main_listview);
		adapter = new NbaNewsAdapter(newss, activity);

		listView.setAdapter(adapter);
		listView.setPullRefreshEnable(this);
		listView.setPullLoadEnable(this);
		listView.setRefreshTime(AppUtil.getRefreashTime(getActivity()));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(activity, NewsDetailsActivity.class);
				intent.putExtra("url", newss.get(position - 1).getUrl());
				startActivity(intent);

			}
		});

		listView.startRefresh();

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new LoadDatasTask().execute(LOAD_MORE);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new LoadDatasTask().execute(LOAD_REFREASH);
	}

	/**
	 * �������ݵ��첽����
	 * 
	 * @author zhy
	 * 
	 */
	class LoadDatasTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			switch (params[0]) {
			case LOAD_MORE:
				return loadMoreData();
			case LOAD_REFREASH:
				return refreashData();
			}
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			System.out.println("result-->" + result);
			switch (result) {
			case TIP_ERROR_NO_NETWORK:
				Toast.makeText(activity, "û����������", 0).show();
				adapter.setDatas(newss);
				adapter.notifyDataSetChanged();
				break;
			case TIP_ERROR_SERVER:
				Toast.makeText(activity, "����������", 0).show();
				break;

			default:
				break;
			}

			listView.setRefreshTime(AppUtil.getRefreashTime(getActivity()));
			listView.stopRefresh();
			listView.stopLoadMore();
			nbaFragment.isLoading = false;
		}
	}

	private int refreashData() {
		Logger.e(NetUtil.checkNet(getActivity()) + "");
		if (NetUtil.checkNet(getActivity())) {
			isConnNet = true;
			// ��ȡ��������
			List<NbaNews> newsItems = GetNBANewsListService.getNews(0, key);
			adapter.setDatas(newsItems);
			isLoadingDataFromNetWork = true;
			// ����ˢ��ʱ��
			AppUtil.setRefreashTime(getActivity());
			// ������ݿ�����
			manager.clearCbaNewsList(activity);
			// ���������ݿ�
			manager.addNbaNewsList(newsItems, activity);

		} else {
			Log.e("xxx", "no network");
			isConnNet = false;
			isLoadingDataFromNetWork = false;
			// �����ݿ��м���
			List<NbaNews> newsItems = new DBNewsListManage().getNbaNews(
					activity, pageNow);
			newss = newsItems;
			// mAdapter.setDatas(newsItems);
			return TIP_ERROR_NO_NETWORK;
		}

		return -1;
	}

	private int loadMoreData() {
		// ��ǰ�����Ǵ������ȡ��
		if (isLoadingDataFromNetWork) {
			pageNow += 1;
			Logger.i("pageNow--->" + pageNow);
			List<NbaNews> newsItems = GetNBANewsListService.getNews(pageNow,
					key);

			manager.addNbaNewsList(newsItems, activity);
			adapter.addAll(newsItems);
		} else
		// �����ݿ���ص�
		{
			pageNow += 1;
			List<NbaNews> newsItems = manager.getNbaNews(activity, pageNow);
			adapter.addAll(newsItems);
			return TIP_ERROR_NO_NETWORK;
		}

		return -1;
	}

}
