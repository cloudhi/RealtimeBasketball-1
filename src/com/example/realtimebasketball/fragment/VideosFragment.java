package com.example.realtimebasketball.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimebasketball.R;
import com.example.realtimebasketball.adapter.LiveAdapter;
import com.example.realtimebasketball.database.DBNewsListManage;
import com.example.realtimebasketball.model.Dates;
import com.example.realtimebasketball.model.Matches;
import com.example.realtimebasketball.net.GetMatchesService;
import com.example.realtimebasketball.net.NetUtil;
import com.example.realtimebasketball.util.LoadingDialog;
import com.example.realtimebasketball.util.Logger;

/**
 * @author dfs212 获取直播赛事fragment
 */
public class VideosFragment extends Fragment implements OnScrollListener {

	private static final int REFREASH = 0x110;
	private static final int TIP_ERROR_NO_NETWORK = 0X112;

	private FragmentActivity activity;

	private ExpandableListView expandableListView;
	/**
	 * 日期列表
	 */
	private List<Dates> dateList = new ArrayList<Dates>();
	/**
	 * 直播赛事列表数组
	 */
	private ArrayList[] lives;

	private List<Matches> matchList = new ArrayList<Matches>();

	private LiveAdapter adapter;

	/**
	 * 当前页数
	 */
	private int pageNow = 0;

	/**
	 * 管理数据库的工具类
	 */
	private DBNewsListManage manager;

	private Dialog progressDialog;
	private LinearLayout view_flotage;
	private TextView group_content;
	private int count_expand = 0;
	private int indicatorGroupHeight;
	private int indicatorGroupId = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View videoView = inflater.inflate(R.layout.videofragment, null);
		return videoView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();

	}

	public void initView() {
		activity = getActivity();
		expandableListView = (ExpandableListView) activity
				.findViewById(R.id.epd_lv);
		view_flotage = (LinearLayout) activity.findViewById(R.id.topGroup);
		group_content = (TextView) activity
				.findViewById(R.id.ag_addressbook_elv_group_title2);
	}

	public void initData() {

		manager = new DBNewsListManage();
		new GetDateListTask().execute(REFREASH);

		expandableListView.setGroupIndicator(null);
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				if (lives[groupPosition] == null) {
					new GetMatchsTask().execute(groupPosition);
					if (progressDialog == null) {
						progressDialog = LoadingDialog
								.createDialog(getActivity());
					}
					progressDialog.show();
				} else {
					setELVGroup(parent, groupPosition);
				}
				return true;
			}
		});

		expandableListView.setOnScrollListener(this);

		lives = new ArrayList[15];
		adapter = new LiveAdapter(activity, dateList, lives);

		expandableListView.setAdapter(adapter);
	}

	public void setELVGroup(ExpandableListView parent, int groupPosition) {
		if (parent.isGroupExpanded(groupPosition)) {
			expandableListView.collapseGroup(groupPosition);
		} else {
			expandableListView.expandGroup(groupPosition);
			expandableListView.setSelectedGroup(groupPosition);
		}
		count_expand = 1;
		indicatorGroupId = groupPosition;
	}

	/**
	 * 重写滚动事件
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (firstVisibleItem == 0) {
			view_flotage.setVisibility(View.GONE);
		}
		// 控制滑动时TextView的显示与隐藏
		int npos = view.pointToPosition(0, 0);
		if (npos != AdapterView.INVALID_POSITION) {
			long pos = expandableListView.getExpandableListPosition(npos);
			int childPos = ExpandableListView.getPackedPositionChild(pos);
			final int groupPos = ExpandableListView.getPackedPositionGroup(pos);
			if (childPos == AdapterView.INVALID_POSITION) {
				View groupView = expandableListView.getChildAt(npos
						- expandableListView.getFirstVisiblePosition());
				indicatorGroupHeight = groupView.getHeight();
			}

			if (indicatorGroupHeight == 0) {
				return;
			}

			if (count_expand > 0) {
				indicatorGroupId = groupPos;
				group_content.setText(adapter.getGroup(indicatorGroupId)
						.toString());
				if (indicatorGroupId != groupPos
						|| !expandableListView.isGroupExpanded(groupPos)) {
					view_flotage.setVisibility(View.GONE);
				} else {
					view_flotage.setVisibility(View.VISIBLE);
				}
			}
			if (count_expand == 0) {
				view_flotage.setVisibility(View.GONE);
			}
		}

		if (indicatorGroupId == -1) {
			return;
		}
		int showHeight = getHeight();
		MarginLayoutParams layoutParams = (MarginLayoutParams) view_flotage
				.getLayoutParams();
		// 得到悬浮的条滑出屏幕多少
		layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
		view_flotage.setLayoutParams(layoutParams);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	private int getHeight() {
		int showHeight = indicatorGroupHeight;
		int nEndPos = expandableListView.pointToPosition(0,
				indicatorGroupHeight);
		if (nEndPos != AdapterView.INVALID_POSITION) {
			long pos = expandableListView.getExpandableListPosition(nEndPos);
			int groupPos = ExpandableListView.getPackedPositionGroup(pos);
			if (groupPos != indicatorGroupId) {
				View viewNext = expandableListView.getChildAt(nEndPos
						- expandableListView.getFirstVisiblePosition());
				showHeight = viewNext.getTop();
			}
		}
		return showHeight;
	}

	/**
	 * 异步加载直播日期集合
	 * 
	 * @author dfs212
	 * 
	 */
	public class GetDateListTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			if (params[0] == REFREASH) {
				return refreshDates();
			} else {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case TIP_ERROR_NO_NETWORK:
				Toast.makeText(activity, "没有网络连接", 0).show();
				adapter.setDates(dateList);
				adapter.notifyDataSetChanged();
				break;
			default:
				adapter.notifyDataSetChanged();
			}

		}

	}

	public int refreshDates() {
		if (NetUtil.checkNet(getActivity())) {
			List<Dates> datesItems = null;
			// 如果数据库中有数据
			if (manager.getDates(activity) != null
					&& manager.getDates(activity).size() > 0) {

				String today = manager.getDates(activity).get(0).getDates()
						.substring(0, 6);
				SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
				String today2 = sdf.format(new Date());

				// 如果数据库中的第一天就是当天，则不用从网络中获取数据，直接从数据库中获取
				if (today.equals(today2)) {
					Logger.i("数据库中获取");
					datesItems = manager.getDates(activity);
				} else {
					Logger.i("数据库中的第一天不是当天，网络获取");
					// 数据库中的第一天不是当天，则从网络中获取数据
					datesItems = GetMatchesService.getLiveDate();
				}
				// 与数据适配器绑定
				adapter.setDates(datesItems);
				dateList = datesItems;

			} else {
				Logger.i("数据库中无数据，网络中获取");
				// 数据库中无数据，即第一次运行加载或数据库被清空后
				datesItems = GetMatchesService.getLiveDate();
				// 与数据适配器绑定
				adapter.setDates(datesItems);
				dateList = datesItems;
				// 清除数据库数据
				manager.clearCbaNewsList(activity);
				// 并存入数据库
				manager.addDateList(datesItems, activity);
			}

		} else {
			Log.e("xxx", "no network");
			// 从数据库中加载
			List<Dates> datesItems = manager.getDates(activity);
			dateList = datesItems;
			return TIP_ERROR_NO_NETWORK;
		}

		return -1;
	}

	/**
	 * @author dfs212 子节点异步加载
	 */
	public class GetMatchsTask extends AsyncTask<Integer, Void, Integer> {

		int position = 0;

		@Override
		protected Integer doInBackground(Integer... params) {
			position = params[0];
			return refreshMatches(position);
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case TIP_ERROR_NO_NETWORK:
				Logger.i("tip:" + TIP_ERROR_NO_NETWORK);
				Toast.makeText(activity, "没有网络连接", 0).show();
				adapter.setMatches(position, matchList, lives);
				adapter.notifyDataSetChanged();
				break;
			}
			if (position != -1) {
				adapter.notifyDataSetChanged();
				expandableListView.expandGroup(position);
				expandableListView.setSelectedGroup(position);
			}
			progressDialog.dismiss();
		}
	}

	public int refreshMatches(int position) {
		if (NetUtil.checkNet(getActivity())) {
			// 获取最新数据
			List<Matches> matchItems = GetMatchesService.getMatches(position);
			adapter.setMatches(position, matchItems, lives);
			matchList = matchItems;
			// 清除数据库数据
			manager.clearDatesList(activity);
			// 并存入数据库
			manager.addAddsList(matchItems, activity);
		} else {
			Log.e("xxx", "no network");
			// 从数据库中加载
			List<Matches> matchsItems = manager.getLives(activity);
			matchList = matchsItems;
			return TIP_ERROR_NO_NETWORK;
		}
		return -1;
	}

}
