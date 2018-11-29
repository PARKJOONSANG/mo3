package com.sk.pe.group.imageviewer.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sk.pe.group.imageviewer.exception.NotExistQueueException;
import com.sk.pe.group.imageviewer.thread.ImageDownloadThread;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

/*
 * requestImage()를 통해서 이미지를 요청할때 쓰레드로 다운로드를 시도하고 결과값은 null값을 보낸다.
 * 만약 다운로드가 완료되었을 때 별도의 통보를 하지 않고 requestImage()를 호출하여 결과가 null이 아닐때까지 polling을 해야 한다.
 * 그런데  클래스는 쓰레드의 결과를 handler를 통해서 처리가 되기때문에 이 클래스를 호출하는 측은 반드시 Thread로 동작을 시켜야 한다.
 * 그렇게 처리하지 않고 호출하는쪽에서 Hang을 걸어버리면 handler가 동작을 하지 못하기 때문이다.
 * Manager에서 Service 체계로 바뀜에 따라 현재 사용하지 않음 ImageCacheService로 대처...
 */
public class ImageCacheManager {
	private final static int BUFFER_PAGE = 2;
	private static DownloadQueue queue = new DownloadQueue();
	private int totalPage = 0;
	private Context context;
	
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
	
	private Handler callerHandler;
	
	public ImageCacheManager(Handler handler) {
		callerHandler = handler;
	}
	
	public void updateTotalPage(String totalPage) {
		if (totalPage != null)
			this.totalPage = StringUtil.intValue(totalPage, 0);
	}
	
	public ImageCacheManager(Context context) {
		this.context = context;
	}
	
	/*
	 * return값이 null이면 다운로드중을 의미한다.
	 */
	public String requestImage(String primitive, String url, String docId, String pageNo) throws SKTException {
		String fileName = null;
		QueueItem reqValue = new QueueItem(url, primitive, docId, pageNo, Type.USER_REQUEST);

		try {
			QueueItem value = queue.getValue(reqValue.getUid());
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
	
	public void clear() {
		queue.clear();
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
			if (queue.isExist(value.getUid()) == false) {
				Log.d("DownloadQueue", "Buffer Download: " + value.toString());
				startDownloadImage(value);
			}
		}
	}
	
	
	private static class DownloadQueue {
		private static List<QueueItem> queue = new LinkedList<QueueItem>();
		
		public synchronized void insert(QueueItem value) {
			QueueItem newValue = new QueueItem(value);
			newValue.status = Status.DOWNLOAD_WAIT;
			queue.add(newValue);
			Log.d("DownloadQueue", "insert: " + newValue.toString());
		}
		
		public synchronized void delete(String uid) {
			QueueItem value = searchValue(uid);
			queue.remove(value);
			Log.d("DownloadQueue", "delete: " + uid);
		}
		
		public synchronized void updateStatus(String uid, int status) {
			QueueItem value = searchValue(uid);
			value.status = status;
			Log.d("DownloadQueue", "update: " + value.toString());
		}
		
		public synchronized void updateStatus(String uid, int status, Exception exp) {
			QueueItem value = searchValue(uid);
			value.status = status;
			value.exp = exp;
			Log.d("DownloadQueue", "update: " + value.toString());
		}
		
		public QueueItem getValue(String uid) {
			QueueItem value = searchValue(uid);
			
			Log.d("DownloadQueue", "getValue: " + uid);
			
			return new QueueItem(value);
		}
		
		public boolean isExist(String uid) {
			boolean ret = false;
			
			try {
				searchValue(uid);
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
