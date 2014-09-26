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
 * @author dfs212 ��ȡֱ������fragment
 */
public class VideosFragment extends Fragment implements OnScrollListener {

	private static final int REFREASH = 0x110;
	private static final int TIP_ERROR_NO_NETWORK = 0X112;

	private FragmentActivity activity;

	private ExpandableListView expandableListView;
	/**
	 * �����б�
	 */
	private List<Dates> dateList = new ArrayList<Dates>();
	/**
	 * ֱ�������б�����
	 */
	private ArrayList[] lives;

	private List<Matches> matchList = new ArrayList<Matches>();

	private LiveAdapter adapter;

	/**
	 * ��ǰҳ��
	 */
	private int pageNow = 0;

	/**
	 * �������ݿ�Ĺ�����
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
	 * ��д�����¼�
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (firstVisibleItem == 0) {
			view_flotage.setVisibility(View.GONE);
		}
		// ���ƻ���ʱTextView����ʾ������
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
		// �õ���������������Ļ����
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
	 * �첽����ֱ�����ڼ���
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
				Toast.makeText(activity, "û����������", 0).show();
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
			// ������ݿ���������
			if (manager.getDates(activity) != null
					&& manager.getDates(activity).size() > 0) {

				String today = manager.getDates(activity).get(0).getDates()
						.substring(0, 6);
				SimpleDateFormat sdf = new SimpleDateFormat("MM��dd��");
				String today2 = sdf.format(new Date());

				// ������ݿ��еĵ�һ����ǵ��죬���ô������л�ȡ���ݣ�ֱ�Ӵ����ݿ��л�ȡ
				if (today.equals(today2)) {
					Logger.i("���ݿ��л�ȡ");
					datesItems = manager.getDates(activity);
				} else {
					Logger.i("���ݿ��еĵ�һ�첻�ǵ��죬�����ȡ");
					// ���ݿ��еĵ�һ�첻�ǵ��죬��������л�ȡ����
					datesItems = GetMatchesService.getLiveDate();
				}
				// ��������������
				adapter.setDates(datesItems);
				dateList = datesItems;

			} else {
				Logger.i("���ݿ��������ݣ������л�ȡ");
				// ���ݿ��������ݣ�����һ�����м��ػ����ݿⱻ��պ�
				datesItems = GetMatchesService.getLiveDate();
				// ��������������
				adapter.setDates(datesItems);
				dateList = datesItems;
				// ������ݿ�����
				manager.clearCbaNewsList(activity);
				// ���������ݿ�
				manager.addDateList(datesItems, activity);
			}

		} else {
			Log.e("xxx", "no network");
			// �����ݿ��м���
			List<Dates> datesItems = manager.getDates(activity);
			dateList = datesItems;
			return TIP_ERROR_NO_NETWORK;
		}

		return -1;
	}

	/**
	 * @author dfs212 �ӽڵ��첽����
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
				Toast.makeText(activity, "û����������", 0).show();
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
			// ��ȡ��������
			List<Matches> matchItems = GetMatchesService.getMatches(position);
			adapter.setMatches(position, matchItems, lives);
			matchList = matchItems;
			// ������ݿ�����
			manager.clearDatesList(activity);
			// ���������ݿ�
			manager.addAddsList(matchItems, activity);
		} else {
			Log.e("xxx", "no network");
			// �����ݿ��м���
			List<Matches> matchsItems = manager.getLives(activity);
			matchList = matchsItems;
			return TIP_ERROR_NO_NETWORK;
		}
		return -1;
	}

}
