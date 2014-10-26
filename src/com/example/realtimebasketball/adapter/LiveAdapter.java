package com.example.realtimebasketball.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.realtimebasketball.R;
import com.example.realtimebasketball.model.Dates;
import com.example.realtimebasketball.model.Matches;
import com.example.realtimebasketball.util.Logger;

/**
 * @author dfs212 ÷±≤•µÿ÷∑  ≈‰∆˜
 */
public class LiveAdapter extends BaseExpandableListAdapter {

	private Context context;

	private LayoutInflater mInflater;

	private ArrayList[] list;

	private List<Dates> dateList;

	public LiveAdapter(Context context, List<Dates> dateList, ArrayList[] list) {
		this.context = context;
		this.dateList = dateList;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	public void setDates(List<Dates> mDatas) {
		this.dateList.clear();
		this.dateList.addAll(mDatas);
	}

	public void setMatches(int position, List<Matches> datas,ArrayList[] list) {
		this.list = null;
		this.list=list;
		list[position] = (ArrayList) datas;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return dateList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if (list[groupPosition] != null) {
			return list[groupPosition].size();
		}
		return 0;

	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return dateList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		if (list != null) {
			return list[groupPosition].get(childPosition);
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ParentHoder parentHoder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.date_group_list, null);
			parentHoder = new ParentHoder();
			parentHoder.dateText = (TextView) convertView
					.findViewById(R.id.group_list_item_text);
			convertView.setTag(parentHoder);
		} else {
			parentHoder = (ParentHoder) convertView.getTag();
		}
		parentHoder.dateText.setText(dateList.get(groupPosition).getDates());

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		ChildHolder childHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.live_item, null);
			childHolder = new ChildHolder();
			childHolder.timeText = (TextView) convertView
					.findViewById(R.id.time);
			childHolder.teamText = (TextView) convertView
					.findViewById(R.id.team);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		childHolder.timeText.setText(((Matches) list[groupPosition]
				.get(childPosition)).getTime());
		childHolder.teamText.setText(((Matches) list[groupPosition]
				.get(childPosition)).getName());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public final class ParentHoder {
		private TextView dateText;
	}

	public final class ChildHolder {
		private TextView timeText;
		private TextView teamText;
	}
}
