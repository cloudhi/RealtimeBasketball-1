package com.example.realtimebasketball.adapter;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.realtimebasketball.R;
import com.example.realtimebasketball.model.CbaNews;
import com.example.realtimebasketball.net.GetBitmapUtil;

public class CbaNewsAdapter extends BaseAdapter {

	private List<CbaNews> newss;
	private Context context;
	private HashMap<String, SoftReference<Bitmap>> hashMap = new HashMap<String, SoftReference<Bitmap>>();

	public CbaNewsAdapter(List<CbaNews> newss, Context context) {
		this.newss = newss;
		this.context = context;
	}

	@Override
	public int getCount() {
		return newss.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout relativeLayout = null;

		if (convertView == null) {
			relativeLayout = (RelativeLayout) View.inflate(context,
					R.layout.listview_item_news, null);
		} else {
			relativeLayout = (RelativeLayout) convertView;
		}

		((TextView) relativeLayout.findViewById(R.id.main_listview_text_title))
				.setText(newss.get(position).getTitle());
		((TextView) relativeLayout.findViewById(R.id.main_listview_text_source))
				.setText(newss.get(position).getSource());
		((TextView) relativeLayout.findViewById(R.id.main_listview_text_date))
				.setText(newss.get(position).getDate());

		/*
		 * 图片加载处理（判断是否包含图片，异步加载）
		 */
		ImageView imageView = (ImageView) relativeLayout
				.findViewById(R.id.main_listview_image);
		imageView.setImageBitmap(null);
		imageView.setTag(newss.get(position).getPhotoUrl() + (position));
		if (newss.get(position).getPhotoUrl() == null
				|| newss.get(position).getPhotoUrl().equals("")) {
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.VISIBLE);
			if (hashMap.get(newss.get(position).getPhotoUrl()) != null
					&& hashMap.get(newss.get(position).getPhotoUrl()).get() != null) {
				imageView.setImageBitmap(hashMap.get(
						newss.get(position).getPhotoUrl()).get());
			} else {
				MyAsyncTask asyncTask = new MyAsyncTask();
				asyncTask.imageView = imageView;
				asyncTask.execute(position);
			}
		}

		return relativeLayout;
	}

	/*
	 * 异步加载图片
	 */
	class MyAsyncTask extends AsyncTask<Integer, String, Bitmap> {
		ImageView imageView = null;
		int index = 0;

		@Override
		protected Bitmap doInBackground(Integer... params) {
			index = params[0];
			Bitmap bitmap = new GetBitmapUtil().getBitmapByUrl(newss.get(index)
					.getPhotoUrl());
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			hashMap.put(newss.get(index).getPhotoUrl(),
					new SoftReference<Bitmap>(bitmap));
			// 防止图片错误，会重用imageView判断是否是先前位置的imageView
			if (imageView.getTag().toString()
					.equals(newss.get(index).getPhotoUrl() + index)) {
				imageView.setImageBitmap(hashMap.get(
						newss.get(index).getPhotoUrl()).get());
			} else {
				// System.out.println("error");
			}
		}
	}

	public void addAll(List<CbaNews> mDatas) {
		this.newss.addAll(mDatas);
	}

	public void setDatas(List<CbaNews> mDatas) {
		this.newss.clear();
		this.newss.addAll(mDatas);
	}

}
