package com.nxdx.lee.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.nxdx.lee.R;
import com.nxdx.lee.bean.Note;
import com.nxdx.lee.db.DBmethod;

public class SwipeAdapter extends ArrayAdapter<Note> {

	private LayoutInflater mInflater;
	private SwipeListView mSwipeListView;
	private Resources tempRes;
	private DBmethod mDBmeth = new DBmethod();
	private Context context;
	
	int id;
	private List<Note> mList;
	
	public SwipeAdapter(Context context, int textViewResourceId
			 ,List<Note> noteList, SwipeListView mSwipeListView) {
		super(context, textViewResourceId, noteList);
		
		this.context = context;
		this.mList = noteList;
		this.mSwipeListView = mSwipeListView;
		
		//获取当前场景下资源，比如笔记表示图片
		tempRes = context.getResources();
		mInflater = (LayoutInflater) context.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.package_row, parent, false);
			holder = new ViewHolder();
	
			holder.noteImg = (ImageView) convertView.findViewById(R.id.noteImg);
			holder.noteTitle = (TextView) convertView.findViewById(R.id.noteTitle);
			holder.noteCreateDate = (TextView) convertView.findViewById(R.id.noteCreateDate);
			
			holder.mBackPlay = (ImageButton) convertView.findViewById(R.id.play);
			holder.mBackDel = (ImageButton) convertView.findViewById(R.id.delete);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String imgName = "";
		String audioName = "";
		id = getItem(position).getId();
		
		String tempTitle = getItem(position).getTitle();
		int tempType = getItem(position).getType();
		String tempContent = getItem(position).getContent();
		String tempCreateDate = getItem(position).getCreateTime();
		
		/**获取图片附件名称*/
		String tempImgStr = getItem(position).getImageURL()+"";
		if(!tempImgStr.equals("")) {
			String []imgArr = tempImgStr.split("/");
			imgName = imgArr[imgArr.length - 1];
		}
		/**获取录音附件名称*/
		String tempAudioStr = getItem(position).getAudioURL()+"";
		if(!tempAudioStr.equals("")) {
			String []audArr = tempAudioStr.split("/");
			audioName = audArr[audArr.length - 1];
		}
		
		Drawable tempDra = null;
	
		/**笔记表示图片处理*/
		if(tempType == 1) {
			tempDra = tempRes.getDrawable(R.drawable.text);
		} else if(tempType == 2) {
			tempDra = tempRes.getDrawable(R.drawable.picture);
		} else if(tempType == 3) {
			tempDra = tempRes.getDrawable(R.drawable.recoder);
		}
		
		holder.noteImg.setBackgroundDrawable(tempDra);
		
		/**笔记内容处理*/
		if(tempTitle.equals("")) {
		if(tempContent.equals("")){
		if(imgName.equals("")) {
		if(audioName.equals("")) {
			holder.noteTitle.setText("未取到内容");
		} else {
			holder.noteTitle.setText(audioName);
				}
		} else {
			holder.noteTitle.setText(imgName);
				}
		} else {
			holder.noteTitle.setText(
				tempContent.length() > 20 ? tempContent.subSequence(0, 20) : tempContent);
			   }
		} else {
			holder.noteTitle.setText(tempTitle);
		}
		holder.noteCreateDate.setText(tempCreateDate);
		
		/***首页操作*/
		//播放音频文件
		holder.mBackPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	
		//删除操作
		holder.mBackDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDBmeth.delete(context, id);
				mSwipeListView.closeAnimate(position);
				mSwipeListView.dismiss(position);
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		ImageView noteImg;
		TextView noteTitle;
		TextView noteCreateDate;
		
		ImageButton mBackDel;
		ImageButton mBackPlay;
	}
}
