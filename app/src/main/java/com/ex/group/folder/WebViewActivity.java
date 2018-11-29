package com.ex.group.folder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ex.group.folder.dialog.CustomprogressDialog;
import com.ex.group.folder.jsinterfaces.MemberJsInterface;
import com.ex.group.folder.R;

public class WebViewActivity extends AppCompatActivity {
    String TAG = "WebViewActivity";
    final String MEMBER_URL = "http://mg.ex.co.kr/member/";
    String hybridUrl = "";
    String parentDeptCode;

    WebView webView;

    private boolean flag = false;
    Handler backHandler;

    CustomprogressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Log.i(TAG, "onCreate");
        Intent intent = getIntent();
        hybridUrl = intent.getStringExtra("hybridUrl");
//		Log.i(TAG, "hybridUrl :"+hybridUrl);
//		Toast.makeText(WebViewActivity.this, "url : "+hybridUrl, Toast.LENGTH_LONG).show();

        progress = new CustomprogressDialog(WebViewActivity.this,null);

		/*		Uri uri = Uri.parse(hybridUrl);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);*/

        backHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==0){
                    flag = false;
                }
            }
        };


        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(hybridUrl);

        WebSettings webSettings = webView.getSettings();
        webView.clearHistory();
        webView.clearCache(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);


        if(hybridUrl.contains(MEMBER_URL)){
            webView.addJavascriptInterface(new MemberJsInterface(WebViewActivity.this, webView), "memberWeb");
        }


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
            {
                new AlertDialog.Builder(WebViewActivity.this).setTitle("알림").setMessage(message).setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                result.confirm();
                            }
                        }).setCancelable(false).create().show();
                return true;
            };
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result){
                new AlertDialog.Builder(WebViewActivity.this).setTitle("알림").setMessage(message).setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                result.confirm();

                            }
                        }).setCancelable(false).create().show();
                return true;
            };
        });



        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // url 주소에 해당하는 웹페이지를 로딩
//
                if (url.startsWith("tel:") || url.startsWith("sms:")|| url.startsWith("smsto:") || url.startsWith("mms:") || url.startsWith("mmsto:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
//				        view.reload();
                    return true;
                }
                view.loadUrl(url);
                return true;// true를 리턴하면 WebView는 해당 URL을 렌더하지 않는다.
//			        return false;
//			        return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // 웹 페이지 리소스들을 로딩하면서 계속해서 호출된다.
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 페이지 로딩시 호출된다.
                Log.i(TAG, "onPageFinished");
                super.onPageFinished(view, url);
                if(progress.isShowing()){
                    progress.dismiss();
                }

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 페이지 요청이 시작될 경우 호출된다.
                super.onPageStarted(view, url, favicon);
                progress.show();
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                // TODO Auto-generated method stub
                super.doUpdateVisitedHistory(view, url, isReload);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                // TODO Auto-generated method stub
                super.onFormResubmission(view, dontResend, resend);
            }


            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                // TODO Auto-generated method stub
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                // TODO Auto-generated method stub
                super.onScaleChanged(view, oldScale, newScale);
            }

            @Override
            public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
                // TODO Auto-generated method stub
                super.onTooManyRedirects(view, cancelMsg, continueMsg);
            }

            @Override
            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                // TODO Auto-generated method stub
                super.onUnhandledKeyEvent(view, event);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                // 키를 오버로딩한것인데 주로 웹페이지를 뒤,앞 등으로 이동하게 한다.
                // 왼쪽키를 누르게 되면 뒤로, 오른쪽 키는 앞으로 가게 한다.
                int keyCode = event.getKeyCode();
                Log.i(TAG, "shouldOverrideKeyEvent"+" pressed keycode BACK!!");
                Log.i(TAG, "webView.canGoBack ?? "+ webView.canGoBack());

                if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT) && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && webView.canGoForward()) {
                    webView.goForward();
                    return true;
                }
                return false;
//			        return super.shouldOverrideKeyEvent(view, event);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // 웹페이지 로딩중 에러가 발생했을때 처리
            }
        });




    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        webView.saveState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        webView.restoreState(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown");

        if(keyCode == KeyEvent.KEYCODE_BACK ){

            if(hybridUrl.contains(MEMBER_URL)){
                Log.i(TAG, "member back pressed URL ======>>>"+webView.getUrl()+"    "+webView.getOriginalUrl());
                String webviewUrl = webView.getUrl();
                if(webviewUrl.contains("tree.jsp")){
                    //상위 이동
                    webView.loadUrl("javascript:sendParentDeptCodeToApp()");
                    return false;
                }
                else if(webviewUrl.contains("main.jsp")){
                    webView.loadUrl("javascript:setFinish()");
                    return false;
                }
            }
	    		/*if(webView.canGoBackOrForward(-1) == false){
	    			finish();
	    		}
	    		else if(webView.canGoBack()){
	    			webView.goBack();
		    		return true;
	    		}*/
//	    		if(webView.canGoBack()){
//	    			webView.goBack();
//	    			return false;
//	    		}
//	    		else{
	    			/*if(!flag){
	    				Toast.makeText(WebViewActivity.this, R.string.finishApp, Toast.LENGTH_SHORT).show();
	    				flag = true;
	    				backHandler.sendEmptyMessageDelayed(0, 1500);
	    				return false;
	    			}
	    			else{
	    				finish();
	    			}*/
//	    		}
        }
        return  super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


}
