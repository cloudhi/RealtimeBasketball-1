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
import com.example.realtimebasketball.adapter.CbaNewsAdapter;
import com.example.realtimebasketball.database.DBNewsListManage;
import com.example.realtimebasketball.model.CbaNews;
import com.example.realtimebasketball.net.GetCBANewsListService;
import com.example.realtimebasketball.net.NetUtil;
import com.example.realtimebasketball.util.AppUtil;
import com.example.realtimebasketball.util.Logger;

public class CbaNewsFragment extends Fragment implements
		IXListViewRefreshListener, IXListViewLoadMore {

	private static final int LOAD_MORE = 0x110;
	private static final int LOAD_REFREASH = 0x111;

	private static final int TIP_ERROR_NO_NETWORK = 0X112;
	private static final int TIP_ERROR_SERVER = 0X113;
	/**
	 * �Ƿ��ǵ�һ�ν���
	 */
	private boolean isFirstIn = true;

	/**
	 * �Ƿ���������
	 */
	private boolean isConnNet = false;

	/**
	 * ��ǰ�����Ƿ��Ǵ������л�ȡ��
	 */
	private boolean isLoadingDataFromNetWork;
	private FragmentActivity activity;
	private XListView listView;
	private CbaNewsAdapter adapter;
	private List<CbaNews> newss = new ArrayList<CbaNews>();
	private LoadDatasTask asyntask;
	// ��ǰҳ��
	private int pageNow = 1;

	/**
	 * ���ݿ������
	 */
	private DBNewsListManage manager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cbaView = inflater.inflate(R.layout.cbafragment, container, false);
		return cbaView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
		listView = (XListView) getView().findViewById(R.id.cba_listview);
		manager = new DBNewsListManage();

		adapter = new CbaNewsAdapter(newss, activity);

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

		if (isFirstIn) {
			/**
			 * ����ʱֱ��ˢ��
			 */
			listView.startRefresh();
			isFirstIn = false;
		} else {
			listView.NotRefreshAtBegin();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		asyntask = new LoadDatasTask();
		asyntask.execute(LOAD_MORE);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		asyntask = new LoadDatasTask();
		asyntask.execute(LOAD_REFREASH);
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
		}
	}

	private int refreashData() {
		Logger.e(NetUtil.checkNet(getActivity()) + "");
		if (NetUtil.checkNet(getActivity())) {
			isConnNet = true;
			// ��ȡ��������
			List<CbaNews> newsItems = GetCBANewsListService.getNews(0);
			adapter.setDatas(newsItems);
			isLoadingDataFromNetWork = true;
			// ����ˢ��ʱ��
			AppUtil.setRefreashTime(getActivity());
			// ������ݿ�����
			manager.clearCbaNewsList(activity);
			// ���������ݿ�
			manager.addCbaNewsList(newsItems, activity);

		} else {
			Log.e("xxx", "no network");
			isConnNet = false;
			isLoadingDataFromNetWork = false;
			// �����ݿ��м���
			List<CbaNews> newsItems = new DBNewsListManage().getCbaNews(
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
			List<CbaNews> newsItems = GetCBANewsListService.getNews(pageNow);

			manager.addCbaNewsList(newsItems, activity);
			adapter.addAll(newsItems);
		} else
		// �����ݿ���ص�
		{
			pageNow += 1;
			List<CbaNews> newsItems = manager.getCbaNews(activity, pageNow);
			adapter.addAll(newsItems);
			return TIP_ERROR_NO_NETWORK;
		}

		return -1;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Logger.e("asyntask cancle!!!");
		asyntask.cancel(true);
	}

}
