package com.nxdx.lee.adapter;



import com.nxdx.lee.R;

import android.content.Context;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * @author 
 * @version 1.0
 * @created 2014-3-26下午10:37:39
 * 类说明:图片显示
 */
public class ImageAdapter extends BaseAdapter {
	
	private Context mCntext;
	 private Integer[] mImageIds = {
			 R.drawable.picture, 
			 R.drawable.text, 
			 R.drawable.recoder,
			 };
	 
	public ImageAdapter(Context c) {
		this.mCntext = c;
	}
	
	@Override
	public int getCount() {
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	public Integer[] getmImageIds() {
		return mImageIds;
	}

	public void setmImageIds(Integer[] mImageIds) {
		this.mImageIds = mImageIds;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mCntext);
		i.setImageResource(mImageIds[position]);
		i.setLayoutParams(new Gallery.LayoutParams(150,75));
		
		//打开原图
		i.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				/**
				  * 打开图片
				 File file = new File("/mnt/sdcard/DCIM/Camera/1395931731983.png"); 
				 Intent intent = new Intent(Intent.ACTION_VIEW); 
				 intent.setDataAndType(Uri.fromFile(file), "image/*");
				 mCntext.startActivity(intent);
				 */
				
				
				/**
				 * 打开音频
				 File file = new File("/mnt/sdcard/Sounds/001.amr"); 
				 Intent intent = new Intent(Intent.ACTION_VIEW); 
				 intent.setDataAndType(Uri.fromFile(file), "audio/amr");
				 mCntext.startActivity(intent);
				 */
			}
		});
		return i;
	}

}
