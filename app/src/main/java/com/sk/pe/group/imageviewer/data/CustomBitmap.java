package com.sk.pe.group.imageviewer.data;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 이미지 컨테이너 클래스
 * @author jokim
 *
 */
public class CustomBitmap {
//	private final String FILENAME = "DOCU";
	//private Context context;
	private Bitmap bitmap;
	private float scale;
	private int rotate;
	private int page;

	public CustomBitmap(Context context, Bitmap bitmap, int page) {
		//this.context = context;
		setBitmap(bitmap);
		setScale(1.0f);
		setRotate(0);
		setPage(page);
	}
	
	public CustomBitmap(Context context, Bitmap bitmap, int page, float scale) {
		//this.context = context;
		setBitmap(bitmap);
		setScale(scale);
		setRotate(0);
		setPage(page);
	}
	
	public CustomBitmap(Context context, Bitmap bitmap, int page, boolean isSave) {
		//this.context = context;
		setBitmap(bitmap);
		setScale(1.0f);
		setRotate(0);
		setPage(page);
	}
	
	public CustomBitmap(Context context, Bitmap bitmap, int page, boolean isSave, float scale) {
		//this.context = context;
		setBitmap(bitmap);
		setScale(scale);
		setRotate(0);
		setPage(page);
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setScale(float scale) {
		if(scale <= 0) {
			return;
		}
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public void setRotate(int rotate) {
		if(rotate >= 360) {
			rotate -= 360;
		}
		else if(rotate <= -360) {
			rotate += 360;
		}
		this.rotate = rotate;
	}

	public int getRotate() {
		return rotate;
	}
	
	public int getWidth() {
		if(bitmap != null) {
			return bitmap.getWidth();
		}
		
		return 0;
	}
	
	public int getHeight() {
		if(bitmap != null) {
			return bitmap.getHeight();
		}
		
		return 0;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

}