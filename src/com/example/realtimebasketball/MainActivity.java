package com.example.realtimebasketball;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.realtimebasketball.fragment.CbaNewsFragment;
import com.example.realtimebasketball.fragment.NbaNewsFragment;
import com.example.realtimebasketball.fragment.VideosFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private RelativeLayout rl_nba;
	private RelativeLayout rl_cba;
	private RelativeLayout rl_video;
	private NbaNewsFragment nbaNewsFragment;
	private CbaNewsFragment cbaNewsFragment;
	private VideosFragment videosFragment;
	private List<Fragment> fragmentList;
	// 退出程序
	private long mExitTime;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		rl_nba = (RelativeLayout) findViewById(R.id.rl_nba);
		rl_nba.setOnClickListener(this);
		rl_cba = (RelativeLayout) findViewById(R.id.rl_cba);
		rl_cba.setOnClickListener(this);
		rl_video = (RelativeLayout) findViewById(R.id.rl_video);
		rl_video.setOnClickListener(this);
		fragmentList = new ArrayList<Fragment>();
		nbaNewsFragment = new NbaNewsFragment();
		cbaNewsFragment = new CbaNewsFragment();
		videosFragment = new VideosFragment();
		fragmentList.add(nbaNewsFragment);
		fragmentList.add(cbaNewsFragment);
		fragmentList.add(videosFragment);

		mViewPager = (ViewPager) findViewById(R.id.container);
		MyPagerAdapter myPagerAdapter = new MyPagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}

			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_nba:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.rl_cba:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.rl_video:
			mViewPager.setCurrentItem(2);
			break;
		}
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

	}

}
