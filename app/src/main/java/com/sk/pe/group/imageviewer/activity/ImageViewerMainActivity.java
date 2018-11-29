package com.sk.pe.group.imageviewer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.ex.group.folder.R;
import com.sk.pe.group.imageviewer.data.CustomBitmap;
import com.sk.pe.group.imageviewer.service.ImageCacheService;
import com.sk.pe.group.imageviewer.widget.ScrollableImageView;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.conf.Environ;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.ResourceUtil;
import com.skt.pe.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * 첨부 파일 및 원문을 보기 위한 클래스
 * 버퍼링 기능 추가 : 2011-08-22 by pluto248
 * @author jokim
 *
 */
public class ImageViewerMainActivity extends SKTActivity implements OnTouchListener,
        OnClickListener {
	private final String PARAM_TYPE = "type";		// 원문보기 : 0, 첨부보기 : 1
	private final String PARAM_ZOOM = "zoom";
	private final String PARAM_FILENAME = "fileName";
	private final String PARAM_PARAMS = "params";
	private final String FILENAME = "DOCU";
	private int m_nPage;
	private int m_nTotalPage;
	private int m_nType;
	private float m_zoom;
	private static final long TITLE_SHOW_TIME = 4000;
	private long showTitleStartTime;
	private String m_szFileName;
	private String mRealFileName;
	private Parameters m_params;
	private ViewAnimator m_viewer;
	private FrameLayout m_picker;
	private static CustomBitmap m_originalBitmap;
	private Bitmap m_bitmap;
	public Handler handler = new Handler(new Callback() {
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			FrameLayout frame = (FrameLayout)findViewById(R.id.LAYOUT_TITLE);
			if(frame.getVisibility() != View.GONE) {
				frame.setVisibility(View.GONE);
				frame.startAnimation(AnimationUtils.loadAnimation(ImageViewerMainActivity.this,
						R.anim.slide_up));
				m_picker.setVisibility(View.VISIBLE);
			}
			return false;
		}
	});
	
	/*
	 * 버퍼링을 위한 이미지 다운로드 서비스 연결...
	 */
	private ImageCacheService icService;
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			icService = ((ImageCacheService.MyBinder)service).getService();
			new Action("init").execute();
			Log.d("ImageView", "Service Connected");
		}
		
		public void onServiceDisconnected(ComponentName className) {
			icService = null;
			Log.d("ImageView", "Service Disconnected");
		}
	};
	
	// 암호화된 바이너리 여부
	private boolean encFlag = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreateX(Bundle savedInstanceState) {
    	Intent bindIntent = new Intent(ImageViewerMainActivity.this, ImageCacheService.class);
        bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
        
//      setContentView(R.layout.main);
//    	EnvironManager.setTestMdn("01049738555");
//    	EnvironManager.setTestCompany("MO");
//    	EnvironManager.setNeedEncPwd(true);
    	Intent intent = getIntent();
    	m_nPage = 1;
    	m_originalBitmap = null;
    	m_nType = intent.getIntExtra(PARAM_TYPE, 2);
    	m_szFileName = intent.getStringExtra(PARAM_FILENAME);
    	m_params = (Parameters)intent.getSerializableExtra(PARAM_PARAMS);
    	m_zoom = intent.getFloatExtra(PARAM_ZOOM, 0.4f);
//    	m_zoom = 1.0f;
    	initEvent();
    	Constants.Status.addErrMsg("E000");
    }
    
    /**
     * 타이틀 영역 초기화
     */
    private void initTitle() {
    	TextView title = (TextView)findViewById(R.id.FILENAME);
    	TextView page = (TextView)findViewById(R.id.PAGE);
    	TextView page2 = (TextView)findViewById(R.id.PAGE2);
    	title.setText(m_szFileName);
    	page.setText("(" + m_nPage + "/" + m_nTotalPage + ")");
    	page2.setText("(" + m_nPage + "/" + m_nTotalPage + ")");
    } // end private void initTitle()
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		ScrollableImageView siView = (ScrollableImageView)m_viewer.getCurrentView();
		siView.invisibleZoomButton();
		super.onBackPressed();
//		showCloseDlg();
	}

	/**
	 * View 및 Event 초기화
	 */
	private void initEvent() {
    	m_viewer = (ViewAnimator)findViewById(R.id.IMAGE_CONTAINER);
    	m_viewer.setOnTouchListener(this);
    	m_picker = (FrameLayout)findViewById(R.id.FRAME_PICKER);
    	m_picker.setOnClickListener(this);
    	ImageView prev = (ImageView)findViewById(R.id.PREV_BTN);
    	prev.setOnClickListener(this);
    	ImageView next = (ImageView)findViewById(R.id.NEXT_BTN);
    	next.setOnClickListener(this);
//    	ImageView prev_p = (ImageView)findViewById(R.id.PREV_BTN_P);
//    	prev_p.setOnClickListener(this);
    	findViewById(R.id.PREV_BTN_P).setOnClickListener(this);
//    	ImageView next_p = (ImageView)findViewById(R.id.NEXT_BTN_P);
//    	next_p.setOnClickListener(this);
    	findViewById(R.id.NEXT_BTN_P).setOnClickListener(this);
    } // end private void initEvent()

	/* (non-Javadoc)
	 * 터치 이벤트 핸들러
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		ViewAnimator aniView = (ViewAnimator)view;
		ScrollableImageView siView = (ScrollableImageView)aniView.getCurrentView();
		siView.onTouchEvent(event);
		return true;
	} // end public boolean onTouch(View view, MotionEvent event)

	/* (non-Javadoc)
	 * 클릭 이벤트 핸들러<br>
	 * - 이전 버튼<br>
	 * - 다음 버튼<br>
	 * - 상단 타이틀
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId() == R.id.PREV_BTN || view.getId() == R.id.PREV_BTN_P) {
			if(view.getId() == R.id.PREV_BTN) {
				setShowTitleStartTime(System.currentTimeMillis());
				ScrollableImageView siView = (ScrollableImageView)m_viewer.getCurrentView();
				siView.setShowTitleStartTime(System.currentTimeMillis());
			}
			if (this.m_nTotalPage != 1)
				prevImage();
		} // end if
		else if(view.getId() == R.id.NEXT_BTN || view.getId() == R.id.NEXT_BTN_P) {
			if(view.getId() == R.id.NEXT_BTN) {
				setShowTitleStartTime(System.currentTimeMillis());
				ScrollableImageView siView = (ScrollableImageView)m_viewer.getCurrentView();
				siView.setShowTitleStartTime(System.currentTimeMillis());
			}
			if (this.m_nTotalPage != 1)
				nextImage();
		} // end else if
		else if(view.getId() == R.id.FRAME_PICKER) {
			slideDownTitle();
		}
	} // end public void onClick(View view)
	
	/**
	 * 상단 타이틀 영역 슬라이드 다운 애니메이션
	 */
	public void slideDownTitle() {
		FrameLayout title = (FrameLayout)findViewById(R.id.LAYOUT_TITLE);
		title.setVisibility(View.VISIBLE);
		title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_down));
		showTitleStartTime = System.currentTimeMillis() + TITLE_SHOW_TIME;
		Thread autoDismiss = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				while(showTitleStartTime > System.currentTimeMillis()) {}
				handler.sendEmptyMessage(0);
			}
		});
		autoDismiss.start();
	}
	
	/**
	 * 슬라이드 애니메이션 타임 아웃 설정
	 * @param time 밀리초 단위 시간
	 */
	public void setShowTitleStartTime(long time) {
		showTitleStartTime = time + TITLE_SHOW_TIME;
	}
	
	/**
	 * 이미지 확대/축소 함수
	 * @param scale 확대/축소 값
	 * @param initAxis 좌표 초기화 여부
	 */
	public void setZoom(float scale, boolean initAxis) {
		//Bitmap bitmap = modifyBitmap(m_originalBitmap, 0, scale);
		m_originalBitmap.setScale(scale);
		ScrollableImageView siView = (ScrollableImageView)m_viewer.getCurrentView();
		//siView.setBitmap(bitmap);
		siView.setBitmap(m_originalBitmap.getBitmap());
		siView.setScale(m_originalBitmap.getScale());
		if(initAxis) {
			siView.initAxis();
		}
		siView.invalidate();
	}
	
	/**
	 * 이미지 확대/축소 함수
	 * @param scale 확대/축소 값
	 */
	public void setZoom(float scale) {
		setZoom(scale, false);
	}
	
	/**
	 * 임시 파일 존재 여부
	 * @param a_nPage 페이지
	 * @return 임시 파일 존재 여부
	 */
	private boolean isExistBitmapFile(int a_nPage) {
		String[] files = fileList();
		
		if(files == null) {
			return false;
		}
		else {
			for(String file: files) {
				if(file.equals(FILENAME + a_nPage)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 이미지를 캐쉬하기 위해 임시로 파일로 저장
	 * @param imageString Base64 인코딩된 이미지 스트링
	 * @param a_nPage 페이지
	 * @return 성공 여부
	 */
	public boolean saveBitmap(String imageString, int a_nPage) {
		FileOutputStream fos  = null;
		//FileOutputStream ff = null;
		
		try {
			fos = openFileOutput(FILENAME + a_nPage, Context.MODE_PRIVATE);
			//ff = new FileOutputStream(new File("/tmp/" + FILENAME + a_nPage));

			OutputStreamWriter osw = null;
			try {
				osw = new OutputStreamWriter(fos);
				osw.write(imageString);
			} catch(Exception e) {
				
			} finally {
				if(osw != null) {
					try {
						osw.close();
					} catch(Exception e) { }
				}
			}
		} catch(Exception e) {
			Log.e("file", e.getMessage());
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch(Exception e) { }
			}
//			if(ff != null) {
//				try {
//					ff.close();
//				} catch(Exception e) { }
//			}
		}

		return true; 
	}

	/**
	 * 캐쉬된 이미지를 불러오는 함수
	 * @param a_nPage 페이지
	 * @return 이미지
	 */
	private Bitmap readBitmapFile(int a_nPage) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		//Bitmap b = null;
		try {
			fis = openFileInput(FILENAME + a_nPage);
			baos = new ByteArrayOutputStream();
			
			// 파일을 바이트배열로
            byte[] byteBuffer = new byte[1024];
            byte[] byteData = null;
            int nLength = 0;
            while((nLength = fis.read(byteBuffer)) > 0) {
                baos.write(byteBuffer, 0, nLength);
            }
            byteData = baos.toByteArray();
            
			// 복호화
            if(encFlag) {
            	byteData = SKTUtil.decryptAESBinary(this, byteData);
            }

			// 이미지로 디코드
            //b = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
            if(m_bitmap != null && !m_bitmap.isRecycled()) {
            	m_bitmap.recycle();
            	m_bitmap = null;
            	System.gc();
            	Log.i("bitmap", "bitmap is recycled");
            }
            
//            BitmapFactory.Options option = new BitmapFactory.Options();
//            option.inPreferredConfig = Bitmap.Config.RGB_565;
            
            m_bitmap = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
		} catch(Exception e) {
			Log.e("file", e.getMessage());
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch(Exception e) { }
			}
			if(baos != null) {
				try {
					baos.close();
				} catch(Exception e) { }
			}
		}

		//return b;
		return m_bitmap;
	}
	
	/**
	 * 캐쉬된 이미지를 불러오는 함수
	 * @parama_nPage 페이지
	 * @return 이미지
	 */
	private Bitmap readBitmapFile(String fileName) throws SKTException {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		//Bitmap b = null;
		try {
			fis = openFileInput(fileName);
			baos = new ByteArrayOutputStream();
			
			// 파일을 바이트배열로
            byte[] byteBuffer = new byte[1024];
            byte[] byteData = null;
            int nLength = 0;
            while((nLength = fis.read(byteBuffer)) > 0) {
                baos.write(byteBuffer, 0, nLength);
            }
            byteData = baos.toByteArray();
            
            if(m_nType == 0 || m_nType == 2) {
				if(m_nType == 0) {
					m_nTotalPage = 1;
				}
				encFlag = false;
			} // end if
			else {
				encFlag = true;
			} // end else
            
            
            
			// 복호화
            if(encFlag) {
            	byteData = SKTUtil.decryptAESBinary(this, byteData);
            }

			// 이미지로 디코드
            //b = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
            if(m_bitmap != null && !m_bitmap.isRecycled()) {
            	m_bitmap.recycle();
            	m_bitmap = null;
            	System.gc();
            	Log.i("bitmap", "bitmap is recycled");
            }
            
//            BitmapFactory.Options option = new BitmapFactory.Options();
//            option.inPreferredConfig = Bitmap.Config.RGB_565;
            
            m_bitmap = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
		} catch(SKTException e) {
			throw e;
		} catch(Exception e) {
			throw new SKTException(e);
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch(Exception e) { }
			}
			if(baos != null) {
				try {
					baos.close();
				} catch(Exception e) { }
			}
		}

		//return b;
		return m_bitmap;
	}
	
	/**
	 * 다음 이미지 요청
	 */
	private void nextImage() {
		int b_nPage = m_nPage;

		if(++m_nPage > m_nTotalPage) {
			m_nPage = 1;
		} // end if
		new Action("next").execute();
//-		if(!isExistBitmapFile(m_nPage)) {
//			new Action("next").execute();
//		} // end if
//		else if(b_nPage != m_nPage){
//			new Action("nextFromFile").execute();
//		} // end else
	} // end private void nextImage()
	
	/**
	 * 이전 이미지 요청
	 */
	private void prevImage() {
//		int b_nPage = m_nPage;

		if(--m_nPage <= 0) {
			m_nPage = m_nTotalPage;
		} // end if
		new Action("prev").execute();
//		if(!isExistBitmapFile(m_nPage)) {
//			new Action("prev").execute();
//		} // end if
//		else if(b_nPage != m_nPage){
//			new Action("prevFromFile").execute();
//		} // end else
	} // end private void prevImage()
	
	/**
	 * 서버로 부터 XmlData를 얻어온다.
	 * @param a_context context
	 * @param params Parameters
	 * @return Response XmlData
	 */
	public XMLData getXmlData(Context a_context, Parameters params) throws SKTException {
		Controller controller = new Controller(a_context);
		XMLData xmlData = null;
		
		try {
			if(m_nType == 0) {
				xmlData = controller.request(params, false, Environ.FILE_CONTENTIMG);
			} // end if
			else if(m_nType == 1) {
				xmlData = controller.request(params, false, Environ.FILE_ATTACHMENT);
			} // end else
			else if(m_nType == 2) {
				xmlData = controller.request(params, false, Environ.FILE_COMPANYIMG);
				String totalPage = params.get("totalPage");
				if(totalPage == null || totalPage.equals("")) {
					m_nTotalPage = 1;
				} else {
					m_nTotalPage = Integer.parseInt(totalPage);
				}
			}
		} // end try
		catch(SKTException e) {
			throw e;
		} // end catch
		
		return xmlData;
	} // end public XMLData getXmlData(Context a_context, Parameters params)
	
	/*
	 * ImageCachedService에게 이미지를 요청한다.
	 */
	public String getImage(Context a_context, Parameters params) throws SKTException {
		String fileName = null;
		String url = null;
		String primitive = params.getPrimitive();
		String docId = params.get("docId");
		String pageNo = params.get("pageNum");
		
		try {
			switch (m_nType) {
				case 0:
					url = Environ.FILE_CONTENTIMG;
					break;
				case 1:
					url = Environ.FILE_ATTACHMENT;
					break;
				case 2:
					url = Environ.FILE_COMPANYIMG;
					break;
				default:
					url = Environ.FILE_SERVICE;
			}
			
			while (fileName == null) {
				fileName = icService.requestImage(primitive, url, docId, StringUtil.intValue(pageNo, 1));
				try {
					if (fileName == null)
						Thread.sleep(500);
				} catch (InterruptedException e) {
					Log.e("ImageView", e.getStackTrace().toString());
				}
			}
			m_nTotalPage = icService.getTotalPage();
			/*
			if(m_nType == 0) {
				xmlData = controller.request(params, false, Environ.FILE_CONTENTIMG);
			} // end if
			else if(m_nType == 1) {
				xmlData = controller.request(params, false, Environ.FILE_ATTACHMENT);
			} // end else
			else if(m_nType == 2) {
				xmlData = controller.request(params, false, Environ.FILE_COMPANYIMG);
				String totalPage = params.get("totalPage");
				if(totalPage == null || totalPage.equals("")) {
					m_nTotalPage = 1;
				} else {
					m_nTotalPage = Integer.parseInt(totalPage);
				}
			}
			*/
		} // end try
		catch(SKTException e) {
			throw e;
		} // end catch
		
		return fileName;
	}
	
	/**
	 * Xml 데이터 파싱
	 * @param a_xmlData Xml 데이터
	 * @return 이미지 데이터
	 */
	public Bitmap parseXml(XMLData a_xmlData) throws SKTException {
		//Bitmap imageData = null;
		
		try {
			String totalPage = a_xmlData.get("totalPage"); //TODO
			if(m_nType != 2) {
				if(totalPage != null) {
					m_nTotalPage = Integer.parseInt(totalPage);
				} // end if
				else {
					m_nTotalPage = 0;
				} // end else
			}
			String imageString = a_xmlData.get("image");
			
			// 임시파일 저장
			saveBitmap(imageString, m_nPage);
			
            if(m_bitmap != null && !m_bitmap.isRecycled()) {
            	m_bitmap.recycle();
            	m_bitmap = null;
            	System.gc();
            	Log.i("bitmap", "bitmap is recycled");
            }
            
//            BitmapFactory.Options option = new BitmapFactory.Options();
//            option.inPreferredConfig = Bitmap.Config.ARGB_8888;
            
			if(m_nType == 0 || m_nType == 2) {
				if(m_nType == 0) {
					m_nTotalPage = 1;
				}
				encFlag = false;
				//imageData = ResourceUtil.decodeBitmap(imageString);
				m_bitmap = ResourceUtil.decodeBitmap(imageString);
			} // end if
			else {
				encFlag = true;
				byte[] dec = SKTUtil.decryptAES(this, imageString);
//				byte[] dec = SKTUtil.decrypt3DES(this, imageString);
				//imageData = ResourceUtil.decodeBitmap(dec);
				m_bitmap = ResourceUtil.decodeBitmap(dec);
			} // end else
		} // end try
		catch (SKTException e) {
			// TODO Auto-generated catch block
			//e.alert(this);
			e.setErrCode("E000");
			throw e;
		} // end catch
		
		//return imageData;
		return m_bitmap;
	} // end public Bitmap parseXml(XMLData a_xmlData)
	
	/**
	 * 액션 처리 핸들러<br>
	 * - 이전 페이지 요청<br>
	 * - 다음 페이지 요청
	 * @see SKTActivity#onAction(String, String[])
	 */
	@Override
	protected XMLData onAction(String primitive, String... args)
			throws SKTException {
		try {
			// TODO Auto-generated method stub
//			if("nextFromFile".equals(primitive) || "prevFromFile".equals(primitive)) {
//				float scale;
//				if(m_originalBitmap == null) {
//					scale = m_zoom;
//				}
//				else {
//					scale = m_originalBitmap.getScale();
//				}
//				m_originalBitmap = new CustomBitmap(this, readBitmapFile(m_nPage), m_nPage, false,
//						scale);
//			}
//			else {
				if(m_nType == 1 || m_nType == 2) {
					m_params.put("pageNum", String.valueOf(m_nPage));
				} // end if
				
				/*
				 * Thread로 동작을 하기 때문에 Service가 bind될때까지 기다린다.
				 */
				for (int i=0; i<10; i++) {
					if (icService != null)
						break;
					Thread.sleep(50);
				}
				
				if(m_nType == 1) {
					mRealFileName = getImage(this, m_params);
				} // end if
				else {
					return getXmlData(this, m_params);
				} 
				mRealFileName = getImage(this, m_params);
//				return getXmlData(this, m_params);
//			}
		} catch(SKTException e) {
//			e.setErrCode("E000");
			throw e;
		} catch(Exception e) {
			SKTException ex = new SKTException(e);
			ex.setErrCode("E000");
			throw ex;
		}
		return null;
	}

	/**
	 * 액션 처리 후 UI 세팅<br>
	 * - 이전 페이지 요청<br>
	 * - 다음 페이지 요청
	 * @see SKTActivity#onActionPost(String, XMLData, SKTException)
	 */
	@Override
	protected void onActionPost(final String primitive, XMLData result, final SKTException e) {
		// TODO Auto-generated method stub
		if(e != null) {
			e.alert(this, new DialogButton(0) {
				public void onClick(DialogInterface dialog, int which) {
					// 초기시 에러는 종료하는 것으로 함(변환에러 포함)
					if("init".equals(primitive)) {
						Intent intent = new Intent();
						if(e.getErrCode().equals("8007")) {
							e.setErrCode("8002");
						}
						intent.putExtra("errCode", e.getErrCode());
						setResult(Constants.RES_HISTORY, intent);
						finish();
					} else if("prev".equals(primitive)) {
						if(++m_nPage > m_nTotalPage) {
							m_nPage = 1;
						}
					} else if("next".equals(primitive)) {
						if(--m_nPage <= 0) {
							m_nPage = m_nTotalPage;
						}
					}
				}
			});
		} // end if
		else {
			if("nextFromFile".equals(primitive) || "prevFromFile".equals(primitive)) {
				initTitle();
				setZoom(m_originalBitmap.getScale(), true);
			}
			else {
				try {
					Bitmap imageData;
					if(m_nType == 1) {
						imageData = this.readBitmapFile(this.mRealFileName);
					} // end if
					else {
						imageData = parseXml(result);
					} 
					
					initTitle();
					if(imageData != null) {
						float scale;
						if(m_originalBitmap == null) {
							scale = m_zoom;
						}
						else {
							scale = m_originalBitmap.getScale();
						}
			    		m_originalBitmap = new CustomBitmap(this, imageData, m_nPage, scale);
			    		setZoom(scale, true);
					} // end if
				} catch(SKTException ex) {
					ex.alert(this, new DialogButton(0) {
						public void onClick(DialogInterface dialog, int which) {
							// 초기시 에러는 종료하는 것으로 함(변환에러 포함)
							if("init".equals(primitive)) {
								finish();
							}
						}
					});
				}
			}
			
			if("init".equals(primitive)) {
				final long showTitleStartTime = System.currentTimeMillis() + 500;
				Thread autoDismiss = new Thread(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						while(showTitleStartTime > System.currentTimeMillis()) {}
						handler.sendEmptyMessage(0);
					}
				});
				autoDismiss.start();
			}
		} // end else
	}
	
	/**
	 * 이미지 크기를 원본 크기로 조정
	 */
	public Bitmap autoFit() {
		CustomBitmap cb = m_originalBitmap;
		float rate = m_zoom;
		
		cb.setScale(rate);
		return m_originalBitmap.getBitmap();
	}
	
	/**
	 * 이미지의 현재 스케일값 리턴 
	 * @return 스케일
	 */
	public float getScale() {
		if(m_originalBitmap != null) {
			return m_originalBitmap.getScale();
		}
		else {
			return 0;
		}
	}
	
	@Override
	protected int onActionPre(String primitive) {
		// TODO Auto-generated method stub
		return Action.SERVICE_RETRIEVING;
	}

	@Override
	protected void onActivityResultX(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStartX() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		try {
			String[] list = fileList();
			if(list != null) {
				for(String file: list) {
					deleteFile(file);
				}
			}
		} catch(Exception e) {
			Log.e("ImageView", "onDestory", e);
		}
		this.unbindService(mConnection);
		super.onDestroy();
	}

	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.viewer;
	}
}