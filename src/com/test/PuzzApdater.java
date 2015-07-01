package com.test;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PuzzApdater extends BaseAdapter {

	private int[]  imgs;
	private Context  context;
	public PuzzApdater(Context context, int[] imgs){
		this.context = context;
		this.imgs = imgs;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgs !=null ? imgs.length : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgs != null ? imgs[position] : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView  imageView = new ImageView(context);
		imageView.setImageResource((Integer)(getItem(position)));
		return imageView;
	}

}
