package com.nxdx.lee.home;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.nxdx.lee.R;
import com.nxdx.lee.adapter.SwipeAdapter;
import com.nxdx.lee.bean.Note;
import com.nxdx.lee.db.DBmethod;
/**
 * 
 * @author huailiang
 * @created 2014-3-26
 * @version 1.0
 * 说明：主体类
 */
public class MainActivity extends Activity implements OnClickListener{
	
	private SwipeListView mSwipeListView ;
	private SwipeAdapter mAdapter ;
	public static int deviceWidth ;
	private List<Note> testData ;
	
	private TextView mUpdateDate;
	private ImageButton mAddText;
	private ImageButton mAddRecord;
	private ImageButton mAddPic;
	
	private DBmethod mDBmeth;
	private static boolean isExit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDBmeth = new DBmethod();
		mDBmeth.createDb(MainActivity.this);
		
		initView();
	}
	
	public void initView() {
		mUpdateDate = (TextView)findViewById(R.id.updateDate);
		mUpdateDate.setText(mDBmeth.getUpdateTime());
		
		mSwipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
		//获取数据库数据
		testData = mDBmeth.getNoteData(MainActivity.this);
		mAdapter = new SwipeAdapter(this, R.layout.package_row, testData, mSwipeListView);
		deviceWidth = getDeviceWidth();
		mSwipeListView.setAdapter(mAdapter);
		mSwipeListView.setSwipeListViewListener( new TestBaseSwipeListViewListener());
		reload();
		
		mAddText = (ImageButton)findViewById(R.id.addTextImg);
		mAddText.setOnClickListener(this);
		mAddRecord = (ImageButton)findViewById(R.id.addRecordImg);
		mAddRecord.setOnClickListener(this);
		mAddPic = (ImageButton)findViewById(R.id.addPictureImg);
		mAddPic.setOnClickListener(this);
	}

	private int getDeviceWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}

	private void reload() {
		mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		// mSwipeListView.setSwipeActionRight(settings.getSwipeActionRight());
		mSwipeListView.setOffsetLeft(deviceWidth * 1 / 3);
		// mSwipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
		mSwipeListView.setAnimationTime(0);
		mSwipeListView.setSwipeOpenOnLongPress(false);
    }
	
	class TestBaseSwipeListViewListener extends BaseSwipeListViewListener{

		@Override
		public void onClickFrontView(int position) {
			super.onClickFrontView(position);
			
		     int nid = ((Note)testData.get(position)).getId();
		     Intent intent = new Intent(MainActivity.this, AddTextActivity.class);
		     intent.putExtra("selectedNote", mDBmeth.getContentById(MainActivity.this, nid));
		     intent.putExtra("model", "2");
		     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		     startActivity(intent);
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				testData.remove(position);
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent(getApplicationContext()
				, AddTextActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		switch(v.getId()) {
		case R.id.addTextImg:
			intent.putExtra("type", Note.NOTE_TEXT);
			intent.putExtra("model", "1");
			startActivity(intent);
			break;
		case R.id.addRecordImg:
			intent.putExtra("type", Note.NOTE_TEXT);
			//intent.putExtra("type", Note.NOTE_RECORD);
			intent.putExtra("model", "1");
			startActivity(intent);
			break;
		case R.id.addPictureImg:
			intent.putExtra("type", Note.NOTE_TEXT);
			//intent.putExtra("type", Note.NOTE_PICTURE);
			intent.putExtra("model", "1");
			startActivity(intent);		
			break;
		default:
			break;
		}	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	// 双击退出
	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};
}
