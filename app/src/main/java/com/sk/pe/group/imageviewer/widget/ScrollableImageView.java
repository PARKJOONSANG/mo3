package com.sk.pe.group.imageviewer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;

import com.ex.group.folder.R;
import com.sk.pe.group.imageviewer.activity.ImageViewerMainActivity;

import java.text.NumberFormat;

/**
 * 이미지 컨테이너 View
 * @author jokim
 *
 */
public class ScrollableImageView extends View implements OnGestureListener, OnZoomListener,
        OnDoubleTapListener, OnClickListener {
	private Bitmap m_bitmap;
	private GestureDetector mGestureDetector;
	private ZoomButtonsController mZoomController;
	private NumberFormat mZoomFormat;
	private TextView mZoomLabel;
	private int m_nX;
	private int m_nY;
	private float mScale;
	private static final float MIN_ZOOM = 0.1f;
	private static final float MAX_ZOOM = 2;
	private static final long TITLE_SHOW_TIME = 4000;
	private long showTitleStartTime;
	private double m_distance = 0;
	private boolean m_isNotLong = false;
	
	public ScrollableImageView(Context context) {
		super(context);
		initView();
	}
	
	public ScrollableImageView(Context context, AttributeSet set) {
		super(context, set);
		initView();
	}
	
	public ScrollableImageView(Context context, AttributeSet set, int defaultStyle) {
		super(context, set, defaultStyle);
		initView();
	}
	
	/**
	 * View 및 Event 초기화
	 */
	public void initView() {
		mGestureDetector = new GestureDetector(this);
		mZoomController = new ZoomButtonsController(this);
		mZoomController.setAutoDismissed(true);
		mZoomController.setOnZoomListener(this);
		mZoomController.setZoomSpeed(25);
		mZoomController.setZoomInEnabled(mScale < MAX_ZOOM);
		mZoomController.setZoomOutEnabled(mScale > MIN_ZOOM);
		makeZoomLabel(getContext(), mZoomController);
		mZoomFormat = NumberFormat.getPercentInstance();
		mZoomLabel.setText("Zoom: " + mZoomFormat.format(mScale));
		m_nX = m_nY = 0;
	}
	
	/**
	 * 좌표 초기화
	 */
	public void initAxis() {
		m_nX = m_nY = 0;
	}
	
	public void setBitmap(Bitmap bitmap) {
		m_bitmap = bitmap;
	}
	
	/**
	 * 이미지의 폭 리턴
	 * @return 이미지 폭
	 */
	public int getBitmapWidth() {
		return (int)(m_bitmap.getWidth() * mScale);
	}
	
	/**
	 * 이미지의 높이 리턴
	 * @return 이미지 높이
	 */
	public int getBitmapHeight() {
		return (int)(m_bitmap.getHeight() * mScale);
	}
	
	/**
	 * 슬라이드 타임아웃 설정
	 * @param time 밀리초 단위
	 */
	public void setShowTitleStartTime(long time) {
		showTitleStartTime = time + TITLE_SHOW_TIME;
	}
	
	public int getViewWidth() {
		return getWidth();
	}
	
	public int getViewHeight() {
		return getHeight();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	public void onDraw(Canvas canvas) {
		if(m_bitmap == null || m_bitmap.isRecycled()) {
			return;
		}
		
		Picture picture = new Picture();
		Canvas c = picture.beginRecording(getBitmapWidth(), getBitmapHeight());

		if(m_nX < 0) {
			m_nX = 0;
		}
		if(m_nY < 0) {
			m_nY = 0;
		}
		if(getBitmapWidth() - getViewWidth() < m_nX) {
			m_nX = getBitmapWidth() - getViewWidth();
			if(m_nX < 0) {
				m_nX = 0;
			}
		}
		if(getBitmapHeight() - getViewHeight() < m_nY) {
			m_nY = getBitmapHeight() - getViewHeight();
			if(m_nY < 0) {
				m_nY = 0;
			}
		}
		if(getBitmapWidth() < getViewWidth()) {
			m_nX = (getViewWidth() - getBitmapWidth()) / 2;
		}
		if(getBitmapHeight() < getViewHeight()) {
			m_nY = (getViewHeight() - getBitmapHeight()) / 2;
		}
		if(getBitmapWidth() < getViewWidth() && getBitmapHeight() < getViewHeight()) {
			c.translate(m_nX, m_nY);
		}
		else if(getBitmapWidth() < getViewWidth()) {
			c.translate(m_nX, -m_nY);
		}
		else if(getBitmapHeight() < getViewHeight()) {
			c.translate(-m_nX, m_nY);
		}
		else {
			c.translate(-m_nX, -m_nY);
		}
		c.scale(mScale, mScale);
		c.drawBitmap(m_bitmap, 0, 0, null);
		picture.endRecording();
		picture.draw(canvas);
	}

	/* (non-Javadoc)
	 * 타이틀을 클릭했을 때의 이벤트 처리
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
	 */
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		ImageViewerMainActivity context = (ImageViewerMainActivity)getContext();
		FrameLayout title = (FrameLayout)context.findViewById(R.id.LAYOUT_TITLE);
		if(title.getVisibility() != View.GONE) {
			title.setVisibility(View.GONE);
			title.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up));
			context.findViewById(R.id.FRAME_PICKER).setVisibility(View.VISIBLE);
		}
		mZoomController.setVisible(false);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * 화면을 Long Press했을 때의 이벤트 처리
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		if(m_isNotLong) {
			return;
		}
		final ImageViewerMainActivity context = (ImageViewerMainActivity)getContext();
		context.findViewById(R.id.FRAME_PICKER).setVisibility(View.GONE);
		FrameLayout title = (FrameLayout)context.findViewById(R.id.LAYOUT_TITLE);
		title.setVisibility(View.VISIBLE);
		title.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down));
		showTitleStartTime = System.currentTimeMillis() + TITLE_SHOW_TIME;
		Thread autoDismiss = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				while(showTitleStartTime > System.currentTimeMillis()) {}
				context.handler.sendEmptyMessage(0);
			}
		});
		autoDismiss.start();
		mZoomController.setVisible(true);
	}

	/* (non-Javadoc)
	 * 화면을 스크롤했을 때의 이벤트 처리
	 * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
		// TODO Auto-generated method stub
		if(m_bitmap == null) {
			return false;
		}
		if(getWidth() < getBitmapWidth()) {
			m_nX += distanceX;
		}
		if(getHeight() < getBitmapHeight()) {
			m_nY += distanceY;
		}
		if(m_nX <= 0) {
			m_nX = 0;
		}
		if(m_nY <= 0) {
			m_nY = 0;
		}
		if(m_nX >= getBitmapWidth() - getWidth()) {
			m_nX = getBitmapWidth() - getWidth();
		}
		if(m_nY >= getBitmapHeight() - getHeight()) {
			m_nY = getBitmapHeight() - getHeight();
		}
		invalidate();
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * Pinch zoom 이벤트 처리
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {
		float x = 0;
		float y = 0;
		float dx = 0;
		float dy = 0;
		int eventCnt = event.getPointerCount();
		
		if(eventCnt > 2) {
			eventCnt = 2;
		}
		if(eventCnt == 2) {
			for(int i = 0; i < eventCnt; i++) {
				int pointerId = event.getPointerId(i);
				int pointerIdx = event.findPointerIndex(pointerId);
				
				if(i == 0) {
					x = event.getX(pointerIdx);
					y = event.getY(pointerIdx);
				}
				if(i == 1) {
					dx = Math.abs(event.getX(pointerIdx) - x);
					dy = Math.abs(event.getY(pointerIdx) - y);
				}
			}
			
			if(event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
				m_distance = Math.sqrt((double)(dx * dx + dy * dy));
				m_isNotLong = true;
			}
			else if(event.getAction() == MotionEvent.ACTION_MOVE) {
				double distance = Math.sqrt((double)(dx * dx + dy * dy));
				double dd = m_distance - distance;
				m_distance = distance;
				setZoom((float)(dd * -0.001), true);
				invalidate();
				
				return true;
			}
			else if(event.getAction() == MotionEvent.ACTION_POINTER_2_UP
					|| event.getAction() == MotionEvent.ACTION_POINTER_1_UP) {
				m_isNotLong = false;
			}
		}
		return mGestureDetector.onTouchEvent(event);
	}

	public void onVisibilityChanged(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * 화면 확대/축소
	 * @see android.widget.ZoomButtonsController.OnZoomListener#onZoom(boolean)
	 */
	public void onZoom(boolean zoomIn) {
		// TODO Auto-generated method stub
		changeZoom(zoomIn, 0.1f);
	}

	/**
	 * 화면 확대/축소
	 * @param zoomIn true이면 확대, false이면 축소
	 * @param delta 스케일
	 */
	private void changeZoom(boolean zoomIn, float delta) {
		setZoom((zoomIn? delta : -delta), true);
	}
	
	/**
	 * 화면 확대/축소
	 * @param zoom 스케일
	 * @param adjust 스케일 제한 여부
	 */
	private void setZoom(float zoom, boolean adjust) {
        mScale += zoom;
        if(mScale > MAX_ZOOM) {
        	mScale = MAX_ZOOM;
        }
        else if(mScale < MIN_ZOOM) {
        	mScale = MIN_ZOOM;
        }
        if (adjust) {
            mScale = Math.min(MAX_ZOOM, Math.max(MIN_ZOOM, mScale));
        }
        mZoomLabel.setText("Zoom: " + mZoomFormat.format(mScale));
        mZoomController.setZoomInEnabled(mScale < MAX_ZOOM);
        mZoomController.setZoomOutEnabled(mScale > MIN_ZOOM);
        disableZoomButton();
        ImageViewerMainActivity context = (ImageViewerMainActivity)getContext();
        context.setZoom(mScale);
    }
	
	/**
	 * 확대/축소 버튼 비활성
	 */
	private void disableZoomButton() {
		ViewGroup container = mZoomController.getContainer();
		if(mScale <= MIN_ZOOM) {
			Button zoomin = (Button)container.findViewWithTag("zoomin");
			zoomin.setBackgroundResource(R.drawable.control_btn_zoomin_selector);
			Button zoomout = (Button)container.findViewWithTag("zoomout");
			zoomout.setBackgroundResource(R.drawable.control_btn_zoomout_dim);
		}
		else if(mScale >= MAX_ZOOM) {
			Button zoomin = (Button)container.findViewWithTag("zoomin");
			zoomin.setBackgroundResource(R.drawable.control_btn_zoomin_dim);
			Button zoomout = (Button)container.findViewWithTag("zoomout");
			zoomout.setBackgroundResource(R.drawable.control_btn_zoomout_selector);
		}
		else {
			Button zoomin = (Button)container.findViewWithTag("zoomin");
			zoomin.setBackgroundResource(R.drawable.control_btn_zoomin_selector);
			Button zoomout = (Button)container.findViewWithTag("zoomout");
			zoomout.setBackgroundResource(R.drawable.control_btn_zoomout_selector);
		}
	}
	
	/**
	 * 확대/축소 라벨 표시
	 * @param context Context
	 * @param zoomController ZoomController
	 */
	private void makeZoomLabel(Context context, ZoomButtonsController zoomController) {
		ViewGroup container = zoomController.getContainer();
		View controls = zoomController.getZoomControls();
		LayoutParams p0 = controls.getLayoutParams();
		container.removeView(controls);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		mZoomLabel = new TextView(context);
		mZoomLabel.setPadding(12, 0, 12, 0);
		mZoomLabel.setTypeface(Typeface.DEFAULT_BOLD);
		mZoomLabel.setTextColor(0xff000000);
		PaintDrawable d = new PaintDrawable(0xeeffffff);
		d.setCornerRadius(6);
		mZoomLabel.setBackgroundDrawable(d);
		mZoomLabel.setTextSize(20);
		mZoomLabel.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		p1.gravity = Gravity.CENTER_HORIZONTAL;
		layout.addView(mZoomLabel, p1);
		LinearLayout layout1 = new LinearLayout(context);
		layout1.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		Button zoomout = new Button(context);
		zoomout.setBackgroundResource(R.drawable.control_btn_zoomout_selector);
		zoomout.setOnClickListener(this);
		zoomout.setTag("zoomout");
		Button zoomin = new Button(context);
		zoomin.setBackgroundResource(R.drawable.control_btn_zoomin_selector);
		zoomin.setOnClickListener(this);
		zoomin.setTag("zoomin");
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		param.gravity = Gravity.RIGHT;
		ll.addView(zoomout, param);
		param = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		param.weight = 1.0f;
		layout1.addView(ll, param);
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		param.gravity = Gravity.LEFT;
		ll.addView(zoomin, param);
		param = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		param.weight = 1.0f;
		layout1.addView(ll, param);
		layout.addView(layout1);
		//layout.addView(controls);
		container.addView(layout, p0);
	}
	
	public float getScale() {
		return mScale;
	}
	
	public void setScale(float scale) {
		mScale = scale;
        if(mScale > MAX_ZOOM) {
        	mScale = MAX_ZOOM;
        }
        else if(mScale < MIN_ZOOM) {
        	mScale = MIN_ZOOM;
        }
		mZoomLabel.setText("Zoom: " + mZoomFormat.format(mScale));
		mZoomController.setZoomInEnabled(mScale < MAX_ZOOM);
		mZoomController.setZoomOutEnabled(mScale > MIN_ZOOM);
        disableZoomButton();
	}

	/* (non-Javadoc)
	 * 이미지 더블 탭 이벤트 처리
	 * @see android.view.GestureDetector.OnDoubleTapListener#onDoubleTap(android.view.MotionEvent)
	 */
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		ImageViewerMainActivity context = (ImageViewerMainActivity)getContext();
		m_bitmap = context.autoFit();
        setScale(context.getScale());
        invalidate();
		return true;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 확대/축소 버튼 감추기
	 */
	public void invisibleZoomButton() {
		mZoomController.setVisible(false);
	}

	/* (non-Javadoc)
	 * 클릭 이벤트 핸들러<br>
	 * - 확대 버튼<br>
	 * - 축소 버튼
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		setShowTitleStartTime(System.currentTimeMillis());
		if(v.getTag().equals("zoomout")) {
			onZoom(false);
		}
		else if(v.getTag().equals("zoomin")) {
			onZoom(true);
		}
	}
}