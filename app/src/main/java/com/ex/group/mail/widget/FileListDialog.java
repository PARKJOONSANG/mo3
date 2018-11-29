package com.ex.group.mail.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.pe.common.data.SKTUtil;

import java.util.ArrayList;
import java.util.Map;

import com.ex.group.folder.R;


/**
 * 관계사 App 둘러보기 화면
 * @author june
 *
 */
public class FileListDialog extends Dialog {

	private ArrayList<Map<String, Object>> data ;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);

		setContentView(R.layout.mail_file_dialog);
	
		setFileUI(data);
		
	}

	public FileListDialog(Context context, ArrayList<Map<String, Object>> data) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.data = data;
	}
	
	private void setFileUI(ArrayList<Map<String, Object>> data) {
		int xmlcount = 0;
		xmlcount = data.size();
		if(xmlcount > 0) {
			LinearLayout layout = (LinearLayout)findViewById(R.id.FILEATTLIST);
			layout.removeAllViews();
			
			for(int a = 0 ; a < xmlcount ; a++){
				final String m_FileName =  data.get(a).get("fileName").toString();
				final String m_FileId 	=  data.get(a).get("fileId").toString();
			
				LinearLayout tempLayout = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.mail_filelist, null);
				Button txtFileName	= (Button) tempLayout.findViewById(R.id.filebtn);
				
				txtFileName.setText(m_FileName);
				txtFileName.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						SKTUtil.viewSecurityImage(context, m_FileName, m_FileId);
						Log.i("FileListDialog", "file name : "+m_FileName+"\nfileID : "+m_FileId);
					}
				});
				layout.addView(tempLayout);
			}
		}
	}

}
