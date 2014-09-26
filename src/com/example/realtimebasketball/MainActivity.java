package com.example.realtimebasketball;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.realtimebasketball.Constant.Constants;
import com.example.realtimebasketball.fragment.CbaNewsFragment;
import com.example.realtimebasketball.fragment.NbaNewsFragment;
import com.example.realtimebasketball.fragment.VideosFragment;
import com.example.realtimebasketball.image.ImageLoaderConfig;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private ProgressBar top_refresh;
	private RelativeLayout rl_nba;
	private RelativeLayout rl_cba;
	private RelativeLayout rl_video;
	private NbaNewsFragment nbaNewsFragment;
	private CbaNewsFragment cbaNewsFragment;
	private VideosFragment videosFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
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

		setTabSelection(0);
	}

	// 退出程序
	private long mExitTime;

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
			setTabSelection(0);
			break;
		case R.id.rl_cba:
			setTabSelection(1);
			break;
		case R.id.rl_video:
			setTabSelection(2);
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示nba，1表示cba，2表示视频，
	 */
	private void setTabSelection(int index) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:

			if (nbaNewsFragment == null) {
				// 如果nbaNewsFragment为空，则创建一个并添加到界面上
				nbaNewsFragment = new NbaNewsFragment();
				transaction.add(R.id.container, nbaNewsFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(nbaNewsFragment);
			}
			break;
		case 1:

			if (cbaNewsFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				cbaNewsFragment = new CbaNewsFragment();
				transaction.add(R.id.container, cbaNewsFragment);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(cbaNewsFragment);
			}
			break;
		case 2:

			if (videosFragment == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				videosFragment = new VideosFragment();
				transaction.add(R.id.container, videosFragment);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(videosFragment);
			}
			break;
		}

		transaction.commit();
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (nbaNewsFragment != null) {
			transaction.hide(nbaNewsFragment);
		}
		if (cbaNewsFragment != null) {
			transaction.hide(cbaNewsFragment);
		}
		if (videosFragment != null) {
			transaction.hide(videosFragment);
		}

	}
}
