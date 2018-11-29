package com.sk.pe.group.imageviewer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.sk.pe.group.imageviewer.exception.NotExistQueueException;
import com.sk.pe.group.imageviewer.manager.QueueItem;
import com.sk.pe.group.imageviewer.thread.ImageDownloadThread;
import com.skt.pe.common.exception.SKTException;

import java.util.LinkedList;
import java.util.List;

public class ImageCacheService extends Service {
	private final IBinder binder = new MyBinder();
	private Context context;

	@Override
	public void onCreate() {
		Log.d("DownloadQueue", "Service: onCreate");
		super.onCreate();
		this.context = getApplicationContext();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("DownloadQueue", "Service: onBind");
		return binder;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("DownloadQueue", "Service: onStartCommand");
		return Service.START_STICKY;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("DownloadQueue", "Service: onUnbind");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		Log.d("DownloadQueue", "Service: onDestroy");
		queue.clear();
		super.onDestroy();
	}
	
	public class MyBinder extends Binder {
		public ImageCacheService getService() {
			return ImageCacheService.this;
		}
	}
	
	/*
	 * Biz Logic Area
	 */
	private final static int BUFFER_PAGE = 2;
	private static DownloadQueue queue = new DownloadQueue();
	private int totalPage = 0;
	
	
	private Handler handler = new Handler(new Handler.Callback() {
		public boolean handleMessage(Message msg) {
			boolean ret = false;
			
			// 현재는 What.UPDATE_STATUS만 존재함.
			if(msg.what == What.UPDATE_STATUS) {
				int status = msg.arg1;
				QueueItem value = (QueueItem)msg.obj;
				
				String uid = value.getUid();
				
				if (uid != null) {
					int recvTotalPage = msg.arg2;
					if (recvTotalPage > 0) {
						totalPage = recvTotalPage;
					}
					switch (status) {
						case Status.DOWNLOAD_ERROR:
							Exception exp = value.exp;
							queue.updateStatus(uid, status, exp);
							break;
						case Status.DOWNLOAD_DONE:
							queue.updateStatus(uid, status);
							if (value.type == Type.USER_REQUEST) {
								startDownloadCacheImage(value);
							}
							break;
					}
					ret = true;
				}
			} 
			
			return ret;
		}
	});
	
//	public void updateTotalPage(String totalPage) {
//		if (totalPage != null)
//			this.totalPage = StringUtil.intValue(totalPage, 0);
//	}
	
	/*
	 * return값이 null이면 다운로드중을 의미한다.
	 */
	public String requestImage(String primitive, String url, String docId, int pageNo) throws SKTException {
		String fileName = null;
		
		if (this.totalPage > 0) {
			if (this.totalPage < pageNo) {
				pageNo = this.totalPage;
			}
		}
		
		String reqPage = String.valueOf(pageNo);
		
		
		QueueItem reqValue = new QueueItem(url, primitive, docId, reqPage, Type.USER_REQUEST);

		Log.d("DownloadQueue", "requestImage: " + reqValue.toString());
		
		try {
			QueueItem value = queue.getValue(reqValue);
			// 버퍼링이 아닌 사용자가 요청한 파일이 다운로드가 완료되었을 경우에만 path를 리턴하다.
			switch (value.status) {
				case Status.DOWNLOAD_DONE:
					fileName = value.getUid();
					if (this.totalPage > 0) 
						startDownloadCacheImage(value);
					break;
				case Status.DOWNLOAD_ERROR:
					queue.delete(value.getUid());
					throw (SKTException) value.exp;
			}
		} catch (NotExistQueueException e) {
			Log.d("DownloadQueue", "User Request Download: " + reqValue.toString());
			startDownloadImage(reqValue);
		}
		
		return fileName;
	}
	
	public int getTotalPage() {
		return this.totalPage;
	}
	
	public String toString() {
		return queue.toString();
	}
	
	private void startDownloadImage(QueueItem value) {
		queue.insert(value);
		ImageDownloadThread thread = new ImageDownloadThread(this.context, value, this.handler);
		thread.start();
	}
	
	private void startDownloadCacheImage(QueueItem value) {
		int currentPage = Integer.parseInt(value.pageNo);
		int bufferPage;
		
		for (int i=1; i<=BUFFER_PAGE; i++) {
			bufferPage = currentPage + i;
			if (bufferPage > this.totalPage) break;
			value.pageNo = String.valueOf(bufferPage);
			value.type = Type.BUFFER;
			if (queue.isExist(value) == false) {
				Log.d("DownloadQueue", "Buffer Download: " + value.toString());
				startDownloadImage(value);
			}
		}
	}
	
	
	private static class DownloadQueue {
		private static List<QueueItem> queue = new LinkedList<QueueItem>();
		
		public synchronized void insert(QueueItem value) {
			value.setUid();
			
			QueueItem newValue = new QueueItem(value);
			newValue.status = Status.DOWNLOAD_WAIT;
			
			queue.add(newValue);
			Log.d("DownloadQueue", "insert: " + newValue.toString());
		}
		
		public synchronized void delete(String uid) {
			try {
				QueueItem value = searchValue(uid);
				queue.remove(value);
				Log.d("DownloadQueue", "delete: " + value.toString());
			} catch (NotExistQueueException e) {
				Log.d("DownloadQueue", "delete: " + e.getMessage());
			}
		}
		
		public synchronized void updateStatus(String uid, int status) {
			try {
				QueueItem value = searchValue(uid);
				value.status = status;
				Log.d("DownloadQueue", "update: " + value.toString());
			} catch (NotExistQueueException e) {
				Log.d("DownloadQueue", "update: " + e.getMessage());
			}
		}
		
		public synchronized void updateStatus(String uid, int status, Exception exp) {
			try {
				QueueItem value = searchValue(uid);
				value.status = status;
				value.exp = exp;
				Log.d("DownloadQueue", "update: " + value.toString());
			} catch (NotExistQueueException e) {
				Log.d("DownloadQueue", "update: " + e.getMessage());
			}
		}
		
		public QueueItem getValue(QueueItem value) {
			QueueItem item = null;
			
			for (int i=0; i<queue.size(); i++) {
				item = queue.get(i);
				if (value.equalFile(item) == true) {
					return new QueueItem(item);
				}
			}
			
			throw new NotExistQueueException();
		}
		
		public boolean isExist(QueueItem value) {
			boolean ret = false;
			
			try {
				getValue(value);
				ret = true;
			} catch (NotExistQueueException e) {
				ret = false;
			}
			
			return ret;
		}
		
		public void clear() {
			queue.clear();
		}
		
		private QueueItem searchValue(String uid) {
			QueueItem value = null;
			
			for (int i=0; i<queue.size(); i++) {
				value = queue.get(i);
				if (uid.equals(value.getUid()) == true) {
					return value;
				}
			}
			
			throw new NotExistQueueException();
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			QueueItem value = null;
			sb.append("DownloadQueue Status...\n");
			for (int i=0; i<queue.size(); i++) {
				value = queue.get(i);
				sb.append(i + ": " + value.toString() + "\n");
			}
			return sb.toString();
		}
	}
	
	public static class Status {
		public final static int DOWNLOAD_WAIT = 1;
		public final static int DOWNLOAD_ING = 2;
		public final static int DOWNLOAD_DONE = 3;
		public final static int DOWNLOAD_ERROR = 4;
	}
	
	public static class What {
		public final static int UPDATE_STATUS = 1;
	}
	
	public static class Type {
		public final static int USER_REQUEST = 1;
		public final static int BUFFER = 2;
	}
}
