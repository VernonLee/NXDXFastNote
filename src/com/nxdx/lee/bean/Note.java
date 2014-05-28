package com.nxdx.lee.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author huailiang
 * @version 1.0
 * @created 2014-3-23上午10:45:55
 * 类说明：便签实体类
 */
public class Note implements Parcelable{

	public int id;
	public int type;
	public String title = "";
	public String content = "";
	public String audioURL = "";
	public String imageURL = "";
	public String createTime = "";
	public String updateTime = "";
	
	//文本类笔记
	public static final int NOTE_TEXT = 1;
	//图片类笔记
	public static final int NOTE_PICTURE = 2;
	//录音类笔记
	public static final int NOTE_RECORD = 3;
	
	public static final Parcelable.Creator<Note> CREATOR = new Creator<Note>() {
		
		@Override
		public Note[] newArray(int size) {
			return new Note[size];
		}
		
		@Override
		public Note createFromParcel(Parcel source) {
			Note note = new Note();
			note.setId(source.readInt());
			note.setType(source.readInt());
			note.setTitle(source.readString());
			note.setContent(source.readString());
			note.setAudioURL(source.readString());
			note.setImageURL(source.readString());
			note.setCreateTime(source.readString());
			note.setUpdateTime(source.readString());
			return note;
		}
	};
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAudioURL() {
		return audioURL;
	}

	public void setAudioURL(String audioURL) {
		this.audioURL = audioURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(type);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(audioURL);
		dest.writeString(imageURL);
		dest.writeString(createTime);
		dest.writeString(updateTime);
	}
}
