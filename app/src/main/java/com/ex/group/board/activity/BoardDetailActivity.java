package com.ex.group.board.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.board.constants.Constants;
import com.ex.group.board.constants.Constants.RequestFeild;
import com.ex.group.board.custom.CustomLog;
import com.ex.group.board.custom.SchUtils;
import com.ex.group.board.custom.SchValidation;
import com.ex.group.board.data.BoardAttachFileInfo;
import com.ex.group.board.data.BoardDetailInfo;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.data.SKTWebUtil;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  <pre>
 *	com.ex.group.board.activity
 *	BoardDetailActivity.java
 *	</pre>
 *
 *	@Author : 박정호
 * 	@E-MAIL : yee1074@innoace.com
 *	@Date	: 2011. 11. 23. 
 *
 *	TODO
 *	상세
 */
public class BoardDetailActivity extends BoardActivity implements OnClickListener {
	
	private String tag = "BoardDetailActivity";
	private Intent intent;
	private WebView webView;
	private BoardDetailInfo boardDetailInfo;
	
	private boolean pauseFlag;
	private String BoardType;
	
	
	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.board_detail_activity;
	}


	@Override
	protected void onCreateX(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//super.onCreateX(savedInstanceState);
		pauseFlag = true;
	}

	
	

	@Override
	protected void onStartX() {
		// TODO Auto-generated method stub
		super.onStartX();
		Log.d(tag, "onStartX pauseFlag " + pauseFlag);
		if( boardDetailInfo == null && pauseFlag) setInit();
	}


	@Override
	public void setInit() {
		
		Log.d(tag, "onStartX pauseFlag1 " + pauseFlag);
		
		intent = getIntent();
		CustomLog.L(tag, "setInit pauseFlag " + pauseFlag);
		boardDetailInfo = new BoardDetailInfo();
		
		Log.d(tag, "onStartX pauseFlag2 " + pauseFlag);
		
		try {
			if(intent != null){
				BoardType = intent.getStringExtra("type");
				String i = intent.getStringExtra("indexBtn");
				CustomLog.L(tag, "indexBtn " + i);
				int index = Integer.parseInt(i);
				
				Log.d(tag, "onStartX pauseFlag3 " + pauseFlag);
				
				((TextView)findViewById(R.id.board_detail_title)).setText(
						getResources().getStringArray(R.array.board_tab)[index]);
				
				((TextView)findViewById(R.id.toolbar2_01)).setOnClickListener(this);
				((TextView)findViewById(R.id.toolbar2_02)).setOnClickListener(this);
				
				Log.d(tag, "onStartX pauseFlag4 " + pauseFlag);
				
				new SKTActivity.Action(Constants.BOARD_DETAIL_PRIMITIVE).execute(
						BoardType,
						intent.getStringExtra("id"), 
						intent.getStringExtra("yearmon"));
				
				Log.d(tag, "onStartX pauseFlag5" + pauseFlag);
				
			}
			super.setInit();
		} catch (Exception e) {
			// TODO: handle exception
			finishActivity(false);
		}		
		
		
	}


	@Override
	public void setLayout() {
		// TODO Auto-generated method stub
		super.setLayout();
		
		webView = (WebView) findViewById(R.id.board_de_webview);
	}


	@Override
	protected XMLData onAction(String primitive, String... args) throws SKTException {
		// TODO Auto-generated method stub
		CustomLog.L(tag, "onAction() - primitive : "+primitive);
		CustomLog.L(tag, "onAction() - args : "+args);
		if(pauseFlag){
			Parameters param  = new Parameters(primitive);
			param.put(RequestFeild.DETAIL_TYPE, args[0]);
			param.put(RequestFeild.DETAIL_BOARDKEY, args[1]);
			param.put(RequestFeild.DETAIL_YEARMON, args[2]);
			Controller controller = new Controller(this);
			return controller.request(param);
		}
		return null;
	}
	
	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e){
		// TODO Auto-generated method stub
		String a = result.toString();
		Log.d(tag, "onActionPost() - primitive : "+primitive);
		Log.d(tag, "onActionPost() - result : "+result);
		try {
			if( (result != null && e == null /*&& "1000".equals(e.getErrCode())*/ ) || pauseFlag)	{
				
				XMLData board = result.getChild("mo");
				Log.d(tag, "onActionPost() - board : "+board.toString());
				Log.d(tag, "onActionPost() - board : "+board.get("title"));
				board.setList("board");
				boardDetailInfo.setId(SchUtils.ChkNullStr(board.get("id"), ""));
				boardDetailInfo.setTitle(SchUtils.ChkNullStr(board.get("title"), "제목없음"));
				boardDetailInfo.setWriteDate(SchUtils.ChkNullStr(board.get("writeDate"), ""));
				boardDetailInfo.setAuthor(SchUtils.ChkNullStr(board.get("author"), ""));
				boardDetailInfo.setCategory(SchUtils.ChkNullStr(board.get("category"), ""));
				boardDetailInfo.setViewUrl(SchUtils.ChkNullStr(board.get("viewUrl"), ""));
				boardDetailInfo.setBody(SchUtils.ChkNullStr(board.get("body"), ""));
				boardDetailInfo.setTeam(SchUtils.ChkNullStr(board.get("team"), ""));
				boardDetailInfo.setGubun(SchUtils.ChkNullStr(board.get("gubun"), ""));
				boardDetailInfo.setHasScript(SchUtils.ChkNullStr(board.get("hasScript"), ""));
				CustomLog.L(tag, "board.get(attachment) " + board.get("attachment"));
				String attachments = SchUtils.ChkNullStr(board.get("attachment"), "N");
				boardDetailInfo.setAttachments(attachments);
				
				CustomLog.L(tag, "onActionPost() - attachments " + attachments);
				
				
				
				if("Y".equals(attachments)){
					board.setList("attachFileList");
					int listCnt = Integer.parseInt(board.get("listCnt"));
					
					board.setList("attachFile");
					boardDetailInfo.setAttachFileInfos(new ArrayList<BoardAttachFileInfo>());
					for(int i=0;i<listCnt;i++){
						BoardAttachFileInfo boardAttachFileInfo = new BoardAttachFileInfo();
						boardAttachFileInfo.setFileId(board.get(i, "fileId"));
						boardAttachFileInfo.setFileName(board.get(i, "fileName"));
						boardAttachFileInfo.setFileSize(board.get(i, "fileSize"));
						boardDetailInfo.getAttachFileInfos().add(boardAttachFileInfo);
					}
				}else{
					boardDetailInfo.setAttachFileInfos(new ArrayList<BoardAttachFileInfo>());
				}
				
				CustomLog.L(tag, boardDetailInfo.toString());
				
				setDetailLayout();
			
			
			}else{
				e.alert(this);
			}
						
		} catch (SKTException e2) {
			// TODO: handle exception
			e2.alert(this);
		} catch (Exception e2) {
			// TODO: handle exception
			//finish();
		}
		
		
		//SKTWebUtil.loadWebView(this, webView, body);
	}
	
	
	private void setDetailLayout(){
		
		((TextView)findViewById(R.id.board_de_txt01)).setText(boardDetailInfo.getTitle());
		((TextView)findViewById(R.id.board_de_txt02)).setText("게시자 : "+boardDetailInfo.getAuthor());
		
		String date = boardDetailInfo.getWriteDate().trim();
		
		if(date.length() > 0){
			((TextView)findViewById(R.id.board_de_txt04)).setText(date);
		}
		
		if(boardDetailInfo.getAttachFileInfos().size() <= 0){
			findViewById(R.id.toolbar2_02).setOnClickListener(null);
		}else{
			findViewById(R.id.toolbar2_02).setBackgroundResource(R.drawable.board_btn_attach_file_n);
		}
			
		Log.d(tag, "boardDetailInfo.getBody() " + boardDetailInfo.getBody());
		Log.d(tag, "boardDetailInfo.getHasScript() " + boardDetailInfo.getHasScript());
		CustomLog.L(tag, "boardDetailInfo.getWriteDate() " + boardDetailInfo.getWriteDate());
		try {
			SKTWebUtil.loadWebView(this, webView, boardDetailInfo.getBody());
		} catch (SKTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if("Y".equals(boardDetailInfo.getHasScript()) || "y".equals(boardDetailInfo.getHasScript())) {
			CustomLog.L(tag, "boardDetailInfo.getHasScript() " + boardDetailInfo.getHasScript());
			SKTDialog dl = new SKTDialog(this, SKTDialog.DLG_TYPE_1);
			dl.getDialog("", getResources().getString(R.string.board_detail_error)).show();
		}
		
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(tag, "onDestroy=====");
		pauseFlag = false;
		if(webView != null) {
			webView.clearCache(true);
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.toolbar2_01:
			finishActivity(true);
			break;

		case R.id.toolbar2_02:
			if(boardDetailInfo.getAttachFileInfos().size() > 0){
				//SchValidation.showToast(this, boardDetailInfo.getAttachFileInfos().toString());
				showDialog(0);
				pauseFlag = false;
			}else{
				SchValidation.showToast(this, getResources().getString(R.string.board_write_error2));
			}
			break;
		}
	}
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		finishActivity(false);
	}


	private void finishActivity(boolean flag){
		/*if(flag){
			finishActivity(RequestCode.DetailActivity);
			setResult(RESULT_OK);
			finish();			
		}else{
			finishActivity(RequestCode.DetailActivity);
			setResult(RESULT_CANCELED);
			finish();
		}*/
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog = null;
		TextView title = null;
		
		dialog = new CommonDialog(this);
		dialog.setContentView(R.layout.board_popup);
		title = (TextView)dialog.findViewById(R.id.title);
		title.setText("첨부 파일 목록");
		
		ListView popup_list = (ListView)dialog.findViewById(R.id.popup_list_1);
		popup_list.setAdapter(new PopupListAdapter(this, R.layout.board_popup_list, boardDetailInfo.getAttachFileInfos()));
		popup_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				pauseFlag = false;
				BoardAttachFileInfo boardAttachFileInfo = (BoardAttachFileInfo)parent.getAdapter().getItem(position);
				SKTUtil.viewSecurityImage(BoardDetailActivity.this, boardAttachFileInfo.getFileName(), boardAttachFileInfo.getFileId());
				CustomLog.L(tag, boardAttachFileInfo.toString());
			}
		});
		return dialog;
	}
	
	
	
	class PopupListAdapter extends ArrayAdapter<BoardAttachFileInfo> {
		BoardAttachFileInfo item;
		ViewHolder vh;
		int textViewResourceId;
		
		public PopupListAdapter(Context context, int textViewResourceId,
                                List<BoardAttachFileInfo> objects) {
			super(context, textViewResourceId, objects);
			this.textViewResourceId = textViewResourceId;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View vi = convertView;
			item = getItem(position);
			
			if(vi == null){
	        	LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            vi = inflater.inflate(textViewResourceId, null);            
	            vh = new ViewHolder(vi);                           
	            vi.setTag(vh);
	        }else{
	        	vh=(ViewHolder)vi.getTag();
	        }
			if(item != null){
				
				vh.getTxt01().setText(item.getFileName());
			}
			return vi;
		}
		
		
		class ViewHolder{
			View base;
			
			TextView txt01;
			
			public ViewHolder(View base) {
				this.base = base;
			}			
			
			public TextView getTxt01(){
				if(txt01 == null) txt01 = (TextView)base.findViewById(R.id.board_popup_txt01);
				return txt01;
			}
			
		}
	}
	
	
	
	

}
