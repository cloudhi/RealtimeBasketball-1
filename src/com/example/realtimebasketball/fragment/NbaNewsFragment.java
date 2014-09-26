package com.example.realtimebasketball.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimebasketball.R;
import com.example.realtimebasketball.Constant.BasketballTeam;
import com.example.realtimebasketball.bean.ChannelItem;
import com.example.realtimebasketball.util.BaseTools;
import com.example.realtimebasketball.util.Logger;
import com.example.realtimebasketball.views.ColumnHorizontalScrollView;

/**
 * 主新闻
 * 
 * @author Administrator
 * 
 */

public class NbaNewsFragment extends Fragment {

	private FragmentActivity activity;
	NbaNewsListViewFragment f = new NbaNewsListViewFragment(this, "");

	// ============== 水平滚动条 ===================
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView1;
	private ColumnHorizontalScrollView mColumnHorizontalScrollView2;
	LinearLayout mRadioGroup_content_west;
	LinearLayout mRadioGroup_content_east;
	private RelativeLayout rl_column1;
	private RelativeLayout rl_column2;
	/** 西部球队新闻分类列表 */
	private ArrayList<ChannelItem> west_team_list = new ArrayList<ChannelItem>();
	/** 东部球队新闻分类列表 */
	private ArrayList<ChannelItem> east_team_list = new ArrayList<ChannelItem>();
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;

	private Button getBack;

	// 判断是否正在加载，防止fragment替换时冲突导致崩溃
	public boolean isLoading = false;

	// ============== 水平滚动条结束 ===================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nbafragmnet, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
		mScreenWidth = BaseTools.getWindowsWidth(activity);
		mItemWidth = mScreenWidth / 5;

		initViews();

	}

	public void initViews() {
		// ======= 初始化 ColumnHorizontalScrollView========
		mColumnHorizontalScrollView1 = (ColumnHorizontalScrollView) activity
				.findViewById(R.id.chsv1);
		mColumnHorizontalScrollView2 = (ColumnHorizontalScrollView) activity
				.findViewById(R.id.chsv2);
		mRadioGroup_content_west = (LinearLayout) activity
				.findViewById(R.id.title_content_west);
		mRadioGroup_content_east = (LinearLayout) activity
				.findViewById(R.id.title_content_east);
		shade_left = (ImageView) activity.findViewById(R.id.shade_left);
		shade_right = (ImageView) activity.findViewById(R.id.shade_right);
		rl_column1 = (RelativeLayout) activity.findViewById(R.id.rl_column1);
		rl_column2 = (RelativeLayout) activity.findViewById(R.id.rl_column2);

		initchannel();
		// ======= 初始化 ColumnHorizontalScrollView 结束========
		getBack = (Button) activity.findViewById(R.id.getback);
		getBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLoading) {
					isLoading = true;
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					if (f != null) {
						ft.remove(f);
					}
					f = new NbaNewsListViewFragment(NbaNewsFragment.this, "");
					ft.replace(R.id.news_content, f).commit();
					getBack.setVisibility(View.INVISIBLE);
				}

			}
		});
	}

	private void initchannel() {
		// TODO Auto-generated method stub
		initColumnData();
		initTabColumn();
		initFragment();
	}

	private void initFragment() {
		// 第一次先加载NBA综合新闻
		getChildFragmentManager().beginTransaction()
				.replace(R.id.news_content, f).commit();
	}

	private void initTabColumn() {
		// TODO Auto-generated method stub
		mRadioGroup_content_west.removeAllViews();
		mRadioGroup_content_east.removeAllViews();

		int count = west_team_list.size();
		mColumnHorizontalScrollView1.setParam(activity, mScreenWidth,
				mRadioGroup_content_west, shade_left, shade_right, rl_column1);
		mColumnHorizontalScrollView2.setParam(activity, mScreenWidth,
				mRadioGroup_content_east, shade_left, shade_right, rl_column2);

		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;

			TextView columnTextView = new TextView(activity);
			columnTextView.setTextAppearance(activity,
					R.style.top_category_scroll_view_item_text);
			// columnTextView.setBackgroundResource(R.color.black);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(west_team_list.get(i).getName());
			columnTextView.setTextColor(Color.GRAY);
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);

			// 监听西部球队栏
			columnTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Logger.e("" + isLoading);
					if (!isLoading) {
						isLoading = true;
						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						if (f != null) {
							ft.remove(f);
						}

						f = new NbaNewsListViewFragment(NbaNewsFragment.this,
								west_team_list.get(v.getId()).getName());

						ft.replace(R.id.news_content, f);
						ft.commit();

						getBack.setVisibility(View.VISIBLE);
					}

				}
			});
			mRadioGroup_content_west.addView(columnTextView, i, params);
		}

		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;

			TextView columnTextView1 = new TextView(activity);
			columnTextView1.setTextAppearance(activity,
					R.style.top_category_scroll_view_item_text);
			// columnTextView.setBackgroundResource(R.color.white);
			columnTextView1.setGravity(Gravity.CENTER);
			columnTextView1.setPadding(5, 5, 5, 5);
			columnTextView1.setId(i);
			columnTextView1.setText(east_team_list.get(i).getName());
			columnTextView1.setTextColor(Color.GRAY);
			columnTextView1.setBackgroundResource(R.drawable.radio_buttong_bg);
			// 监听东部球队栏
			columnTextView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!isLoading) {
						isLoading = true;
						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						if (f != null) {
							ft.remove(f);
						}

						f = new NbaNewsListViewFragment(NbaNewsFragment.this,
								east_team_list.get(v.getId()).getName());

						ft.replace(R.id.news_content, f);
						ft.commit();

						getBack.setVisibility(View.VISIBLE);
					}
				}
			});
			mRadioGroup_content_east.addView(columnTextView1, i, params);
		}
	}

	private void initColumnData() {
		for (int i = 0; i < BasketballTeam.west.length; i++) {
			west_team_list.add(new ChannelItem(i, BasketballTeam.west[i], i));
		}

		for (int i = 0; i < BasketballTeam.east.length; i++) {
			east_team_list.add(new ChannelItem(i, BasketballTeam.east[i], i));
		}
	}

	/**
	 * 选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content_west.getChildCount(); i++) {
			View checkView = mRadioGroup_content_west.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView1.smoothScrollTo(i2, 0);

		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content_west.getChildCount(); j++) {
			View checkView = mRadioGroup_content_west.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

}
