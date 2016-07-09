package com.example.wjm.weibo.list;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.wjm.weibo.util.AppCache;

public class GridImageList extends BaseAdapter {

	private Context context;
	private List<String> imageUrls;
	
	public GridImageList (Context context, List<String> imageUrls) {
		this.context = context;
		this.imageUrls = imageUrls;
	}

	public int getCount() {
		return imageUrls.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setPadding(10, 10, 10, 10);
		// get pic from remote
		Bitmap bitmap = AppCache.getCachedImage(context, imageUrls.get(position));
		imageView.setImageBitmap(bitmap);
		return imageView;
	}
	
}