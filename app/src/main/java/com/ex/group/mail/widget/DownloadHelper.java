package com.ex.group.mail.widget;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.dialog.SKTDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.ex.group.folder.R;

/**
 * 설치 헬퍼 클래스
 * @author june
 *
 */
public class DownloadHelper {
	private Activity activity;
	private boolean  bindFlag = false;
	private ProgressDialog progressDialog;
	private BroadcastReceiver r;
	private IntentFilter f;
	private String file;
	
	public final static Map<String,String> MIME_TYPE = new HashMap<String, String>();
	
	static {
		MIME_TYPE.put("pdf", 	"application/pdf");
		MIME_TYPE.put("doc", 	"application/msword");
		MIME_TYPE.put("docx", 	"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		MIME_TYPE.put("xls", 	"application/vnd.ms-excel");
		MIME_TYPE.put("xlsx", 	"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		MIME_TYPE.put("ppt", 	"application/vnd.ms-powerpoint");
		MIME_TYPE.put("pptx", 	"application/vnd.openxmlformats-officedocument.presentationml.presentation");
		MIME_TYPE.put("bmp", 	"image/bmp");
		MIME_TYPE.put("jpg", 	"image/jpeg");
		MIME_TYPE.put("gif", 	"image/gif");
		MIME_TYPE.put("png", 	"image/png");
		MIME_TYPE.put("wmf", 	"image/wmf");
		MIME_TYPE.put("emf", 	"image/emf");
		MIME_TYPE.put("txt", 	"text/plain");
		MIME_TYPE.put("htm", 	"text/html");
		MIME_TYPE.put("html", 	"text/html");
		MIME_TYPE.put("hwp", 	"application/haansofthwp"); //"application/x-hwp"
	}

	public DownloadHelper(Activity activity) {
		
		this.activity = activity;

		progressDialog = new ProgressDialog(activity);
		progressDialog.setOwnerActivity(activity);
		progressDialog.setCancelable(false);
		
		r = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(Constants.Action.DOWNLOADED_SERVICE.equals(intent.getAction())) {
					String filePath = intent.getStringExtra(Constants.KEY_APP_FILE);
					onDownloaded(filePath);
				} else if(Constants.Action.DOWNLOAD_FAILED_SERVICE.equals(intent.getAction())) {
					onDownloadFailed();
				}
			}
		};

		f = new IntentFilter(Constants.Action.DOWNLOADED_SERVICE);
		f.addAction(Constants.Action.DOWNLOAD_FAILED_SERVICE);
	}

	/**
	 * 설치 헬퍼 연결
	 */
	public void bind() {
		if(!bindFlag) {
	        activity.registerReceiver(r, f);
	        bindFlag = true;
		}
	}

	/**
	 * 설치 헬퍼 해제 
	 */
	public void unbind() {
		if(bindFlag) {
			activity.unregisterReceiver(r);
			bindFlag = false;
			
		}
	}

	/**
	 * 설치 진행 프로그래스바 보여주기
	 */
	public void showProgress() {
		//progressDialog.setMessage(activity.getResources().getString(R.string.progress_downloading));
		progressDialog.setMessage("Downloading...");
		progressDialog.show();
	}

	public void dismissProgress() {
		progressDialog.dismiss();
	}

	/**
	 * 다운 완료되었을 때 동작
	 * @param appId 어플아이디
	 * @param appInfo 어플정보
	 */
	public void onDownloaded(String filePath) {
		Log.d("=====================", filePath + "xxx");
		try {
			progressDialog.dismiss();
			Uri uri = Uri.fromFile(new File(filePath));
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			it.setData(uri);
			
			int offset = filePath.lastIndexOf(".");
			if(offset != -1) {
				Log.d("=============fffffffffff========",  "   " + filePath.substring(offset + 1).toLowerCase());
				Log.e("==========================", MIME_TYPE.get(filePath.substring(offset + 1).toLowerCase()));
				String ext = filePath.substring(offset + 1).toLowerCase();

				if(MIME_TYPE.containsKey(ext)) {
					it.setDataAndType(uri, MIME_TYPE.get(ext));					
				}
			}
			it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			new SKTDialog(activity).getDialog(activity.getResources().getString(R.string.mail_dialog_ok),activity.getResources().getString(R.string.mail_NO_VIEWER)).show();
		}
	}
	
	/**
	 * 설치 실패시 동작
	 * @param appId 어플아이디
	 * @param appInfo 어플정보
	 */
	public void onDownloadFailed() {
		progressDialog.dismiss();
	}
	
}
