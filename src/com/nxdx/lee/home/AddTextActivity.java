package com.nxdx.lee.home;

import java.io.File;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nxdx.lee.R;
import com.nxdx.lee.bean.Note;
import com.nxdx.lee.db.DBmethod;

/**
 * @author huailiang
 * @version 1.0
 * @created 2014-3-26上午10:06:08 
 * 添加文本类笔记
 */
public class AddTextActivity extends Activity {

	private EditText mTitle;
	private EditText mContent;
	private ProgressDialog show;

	private ImageView mPic;
	private ImageView mRec;

	ActionMode.Callback mCallBack;
	ActionMode mMode;
	private ActionBar mActionBar;

	private String pFilePath = "";
	private String rFilePath = "";
	private int type;
	public int id = 0;
	private DBmethod mDBMethod = new DBmethod();

	public static final int COMPLETED = 1;
	public static final int NULL_ERROR = 2;
	public static final String NULL_ERROR_MSG = "内容为空,便签保存失败";
	public static final String WRONG_MSG = "保存失败";
	public static final String SUCCESS_MSG = "保存成功";
	public static final String OPEN_ERROR_MSG = "文件打开异常";
	public static final int INTENT_CODE_CAMERA = 0x3;
	public static final int INTENT_CODE_RECODER = 0x4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_text);

		mActionBar = getActionBar();
		Resources res = getResources();
		Drawable dra = res.getDrawable(R.drawable.BackBar);
		mActionBar.setBackgroundDrawable(dra);
		mActionBar.setTitle("添加便签");
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.show();

		mCallBack = new ActionMode.Callback() {

			/**
			 * Called each time the action mode is shown. Always called after
			 * onCreateActionMode, but may be called multiple times if the mode
			 * is invalidated.
			 */
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// Return false if nothing is done
				return false;
			}

			/**
			 * Called when the user exits the action mode but we used in save
			 * data
			 */
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				mode = null;

				show = new ProgressDialog(AddTextActivity.this);
				show.setMessage("保存中");
				show.setCancelable(false);
				show.show();

				Thread add = new Thread(new AddNoteThread());
				add.start();
			}

			/**
			 * Called when the action mode is created; startActionMode() was
			 * called
			 */
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// mode.setTitle("");
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.actionar, menu);
				return true;
			}

			/** Called when the user selects a contextual menu item */
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_cancle:
					mode.finish();
					return true;
				case R.id.action_takephoto:

					// 调用拍照图片并保存至本地
					pFilePath = "/mnt/sdcard/DCIM/Camera/"
							+ System.currentTimeMillis() + ".png";
					final File pFile = new File(pFilePath);
					final Uri pUriImage = Uri.fromFile(pFile);

					Intent pIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					pIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
							pUriImage);
					startActivityForResult(pIntent, INTENT_CODE_CAMERA);

					return true;
				case R.id.action_record:
					Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
					startActivityForResult(intent, INTENT_CODE_RECODER);
					
					return true;
				default:
					System.out.println("对勾3");
					return false;
				}
			}
		};

		mMode = startActionMode(mCallBack);

		initView();
	}

	public void initView() {
		mTitle = (EditText) findViewById(R.id.tNoteTitle);
		mContent = (EditText) findViewById(R.id.tNoteContent);

		mPic = (ImageView) findViewById(R.id.pictureImg);
		/*** 打开图片附件 */
		mPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!pFilePath.equals("")) {
					File file = new File(pFilePath);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(file), "image/*");
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), OPEN_ERROR_MSG,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		mRec = (ImageView) findViewById(R.id.recoderImg);
		/** 打开音频附件 */
		mRec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!rFilePath.equals("")) {
					File file = new File(rFilePath);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(file), "audio/amr");
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), OPEN_ERROR_MSG,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		setContext();
	}

	/** 初始化内容 */
	public void setContext() {
		
		Note note = null;
		
		String model = getIntent().getStringExtra("model");
		/**
		 * model:1添加模式
		 * model:2编辑模式
		 */
		if(model.equals("2")) {
			note = getIntent().getParcelableExtra("selectedNote");
			id = note.getId();
			type = note.getType();
			mTitle.setText(note.getTitle());
			mContent.setText(note.getContent());
			pFilePath = note.getImageURL();
			rFilePath = note.getAudioURL();

			if (type == Note.NOTE_PICTURE) {
				mPic.setVisibility(View.VISIBLE);
			} else {
			}
			
			if (type == Note.NOTE_RECORD) {
				mRec.setVisibility(View.VISIBLE);
			} else {
			}
			
			if(rFilePath.length() > 5 && pFilePath.length() > 5) {
				mPic.setVisibility(View.VISIBLE);
				mRec.setVisibility(View.VISIBLE);
			}
		}	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == INTENT_CODE_CAMERA) {
				/**
				try {
					  InputStream is = new FileInputStream(pFilePath);
					  BitmapFactory.Options option = new
					  BitmapFactory.Options(); option.inJustDecodeBounds =
					  false; 展示原图的十分之一 option.inSampleSize = 2; Bitmap btp =
					  BitmapFactory.decodeStream(is, null, option);
					  imgView.setImageBitmap(btp);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				*/
				type = Note.NOTE_PICTURE;
				mPic.setVisibility(View.VISIBLE);
				
			} else if (requestCode == INTENT_CODE_RECODER) {
				Uri uriRecorder = data.getData();
				Cursor cursor = this.getContentResolver().query(uriRecorder
						,null, null, null, null);
				if (cursor.moveToNext()) {
					/** _data：文件的绝对路径 ，_display_name：文件名 */
					rFilePath = cursor.getString(cursor.getColumnIndex("_data"));
				}
				
				type = Note.NOTE_RECORD;
				mRec.setVisibility(View.VISIBLE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class AddNoteThread implements Runnable {

		@Override
		public void run() {
			Looper.prepare();
			
			Note iNote = new Note();
			// 获取参数，保存笔记
			if (type == Note.NOTE_PICTURE) {
				iNote.setType(type);
			} else if (type == Note.NOTE_RECORD) {
				iNote.setType(type);
			} else {
				iNote.setType(getIntent().getIntExtra("type", 1));
			}
			String title = mTitle.getText().toString();
			String context = mContent.getText().toString();

			Message msg = new Message();

			if (!(title.equals("") && context.equals("")
					&& pFilePath.equals("") && rFilePath.equals(""))) {
				iNote.setTitle(title);
				iNote.setContent(context);
				iNote.setImageURL(pFilePath);
				iNote.setAudioURL(rFilePath);

				/** 新建笔记 */
				if (id == 0) {
					mDBMethod.addNote(AddTextActivity.this, iNote);

					/** 编辑笔记 */
				} else {
					iNote.setId(id);
					mDBMethod.updateNote(AddTextActivity.this, iNote);
				}

				msg.what = COMPLETED;
			} else {
				msg.what = NULL_ERROR;
			}
			addHandler.sendMessage(msg);

			Looper.loop();
		}
	}

	Handler addHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {

				show.dismiss();
				Toast.makeText(getApplicationContext(), SUCCESS_MSG,
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AddTextActivity.this
						,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else if (msg.what == NULL_ERROR) {

				show.dismiss();
				Toast.makeText(getApplicationContext(), NULL_ERROR_MSG,
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AddTextActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {

				show.dismiss();
				Toast.makeText(getApplicationContext(), WRONG_MSG,
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AddTextActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}
	};
}