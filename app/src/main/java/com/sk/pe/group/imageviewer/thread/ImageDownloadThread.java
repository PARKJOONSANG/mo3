package com.sk.pe.group.imageviewer.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sk.pe.group.imageviewer.manager.QueueItem;
import com.sk.pe.group.imageviewer.service.ImageCacheService;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * 메일함 Thread
 * @author sjsun5318
 *
 */
public class ImageDownloadThread extends Thread {
	private Context context = null;
	private Handler handler = null;
	private QueueItem value = null;
//	private String uid;
//	private String url;
//	private String docId;
//	private String pageNo;
//	private String primitive;
	
	public ImageDownloadThread(Context context, QueueItem value, Handler handler) {
		this.context = context;
		this.handler = handler;
		// 새로운 instance를 생성하여 인수로 넘어온 value와의 상관관계를 끊어 버린다.
		this.value = new QueueItem(value);
	}

	public XMLData getXmlData() throws SKTException {

		Parameters params = new Parameters(this.value.primitive);
		//params.put("companyCd", EmailClientUtil.companyCd);
		//params.put("lang", EmailClientUtil.LANG);
		params.put("docId", this.value.docId);
		params.put("pageNum", this.value.pageNo);
		Controller controller = new Controller(this.context);
		
		return controller.request(params, false, this.value.url);
	}
	
	private void sendMessage(int status, String totalPage) {
		this.sendMessage(ImageCacheService.What.UPDATE_STATUS, status, totalPage, null);
	}

	private void sendErrorMessage(SKTException e) {
		this.sendMessage(ImageCacheService.What.UPDATE_STATUS, ImageCacheService.Status.DOWNLOAD_ERROR, null, e);
	}
	
	private void sendMessage(int what, int status, String totalPage, SKTException e) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.arg1 = status;
		msg.arg2 = StringUtil.intValue(totalPage, 0);
		if (e != null) {
			value.exp = e;
		}
		msg.obj = value;
		handler.sendMessage(msg);
	}
	
	private void saveImage(String image) throws IOException {
		FileOutputStream fos  = null;
		
		try {
			fos = this.context.openFileOutput(this.value.getUid(), Context.MODE_PRIVATE);

			OutputStreamWriter osw = null;
			try {
				osw = new OutputStreamWriter(fos);
				osw.write(image);
			} finally {
				if(osw != null) {
					osw.close();
				}
			}
		} finally {
			if(fos != null) {
				fos.close();
			}
		}
	}
	
	/**
	 * Therad 실행
	 */
	@Override
	public void run() {
		try {
			sendMessage(ImageCacheService.Status.DOWNLOAD_ING, null);
			XMLData xmlData = getXmlData();
			String totalPage = xmlData.get("totalPage");
			String image = xmlData.get("image");
			
			saveImage(image);
			
			Log.d("IMAGEVIEW", "DocID: " + this.value.docId);
			Log.d("IMAGEVIEW", "PageNo: " + this.value.pageNo);
			Log.d("IMAGEVIEW", "Image: " + image);
			/*
			 * image 내용을 그대로 파일에 저장한다.
			 */
			sendMessage(ImageCacheService.Status.DOWNLOAD_DONE, totalPage);
		} catch (SKTException e) {
			sendErrorMessage(e);
		} catch (Exception e) {
			sendErrorMessage(new SKTException(e));
		}
		
	}

}
