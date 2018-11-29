package com.ex.group.board.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.board.data.BoardListInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  <pre>
 *	com.ex.group.board.widget
 *	BoardListAdapter.java
 *	</pre>
 *
 *	@Author : 박정호
 * 	@E-MAIL : yee1074@innoace.com
 *	@Date	: 2011. 11. 23. 
 *
 *	TODO
 *	List Adapter 
 */
public class BoardListAdapter extends ArrayAdapter<BoardListInfo> {
	private ArrayList<BoardListInfo> items;
	private BoardListInfo item;
	private int textViewResourceId;
	private ViewHolder vh;

	public BoardListAdapter(Context context, int textViewResourceId, List<BoardListInfo> objects) {
		super(context, textViewResourceId, objects);		
		items = (ArrayList<BoardListInfo>) objects;
		this.textViewResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		item = (BoardListInfo)items.get(position);
		
		Resources resource = getContext().getResources( );
		float iScale = resource.getDisplayMetrics( ).density;
		
		// 첨부파일 아이콘
		int width = 10 * (int)iScale;
		int height = 20 * (int)iScale;
		Drawable drawable = resource.getDrawable(R.drawable.board_icon_file);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		Drawable iconFile = new BitmapDrawable(resource, Bitmap.createScaledBitmap(bitmap, width, height, true));
		
		if(vi == null){
        	LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(textViewResourceId, null);            
            vh = new ViewHolder(vi);                           
            vi.setTag(vh);
        }else{
        	vh=(ViewHolder)vi.getTag();
        }
		if(item != null){
			if("0".equals(item.getLevel())){
				vh.getListLay().setVisibility(View.VISIBLE);
				vh.getReLay().setVisibility(View.GONE);
				
				vh.getListTxt01().setText(item.getAuthor());
				vh.getListTxt02().setText(item.getWriteDate());
				vh.getListTxt03().setText(/*"["+ item.getTeam() +"] "+*/item.getTitle());
				
				if("Y".equals(item.getAttachment())) {
					vh.getListTxt03().setCompoundDrawablesWithIntrinsicBounds(null, null, iconFile, null);
					vh.getListTxt03().setCompoundDrawablePadding(5);
				}else{
					vh.getListTxt03().setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
				
				int readCount = 0;
				if(item.getRead() != null) {
					try {
						readCount = Integer.parseInt(item.getRead());
					} catch(NumberFormatException e) {
						readCount = 0;
					}
				}
				vh.getListTxt04().setText("조회 : " + String.format("%,d", readCount));
				
			}else{
				vh.getListLay().setVisibility(View.GONE);
				vh.getReLay().setVisibility(View.VISIBLE);
				
				vh.getReImg().setVisibility(View.VISIBLE);
				vh.getReTxt01().setText("Re"+item.getLevel()+":"+item.getTitle());
				if("Y".equals(item.getAttachment())) {
					vh.getReTxt01().setCompoundDrawablesWithIntrinsicBounds(null, null, iconFile, null);
					vh.getReTxt01().setCompoundDrawablePadding(5);
				}else{
					vh.getReTxt01().setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
				vh.getReTxt02().setText(item.getAuthor());
				vh.getReTxt03().setText(item.getWriteDate());
				int readCount = 0;
				if(item.getRead() != null) {
					try {
						readCount = Integer.parseInt(item.getRead());
					} catch(NumberFormatException e) {
						readCount = 0;
					}
				}
				vh.getReTxt04().setText("조회 : " + String.format("%,d", readCount));
			}
		}
		return vi;
	}
	
	
	
	
	public class ViewHolder{
		View base;
		
		LinearLayout re_lay;
		ImageView re_img;
		TextView re_txt01;
		TextView re_txt02;
		TextView re_txt03;
		TextView re_txt04;
		
		
		LinearLayout list_lay;
		TextView list_txt01;
		TextView list_txt02;
		TextView list_txt03;
		TextView list_txt04;
		
		public ViewHolder(View base) {
			this.base = base;
		}
		
		
		LinearLayout getReLay(){
			if(re_lay == null) re_lay = (LinearLayout)base.findViewById(R.id.list_re_lay);
			return re_lay;
		}		
		ImageView getReImg(){
			if(re_img == null) re_img = (ImageView)base.findViewById(R.id.list_re_img);
			return re_img;
		}		
		TextView getReTxt01(){
			if(re_txt01 == null) re_txt01 = (TextView)base.findViewById(R.id.list_re_txt01);
			return re_txt01;
		}		
		TextView getReTxt02(){
			if(re_txt02 == null) re_txt02 = (TextView)base.findViewById(R.id.list_re_txt02);
			return re_txt02;
		}		
		TextView getReTxt03(){
			if(re_txt03 == null) re_txt03 = (TextView)base.findViewById(R.id.list_re_txt03);
			return re_txt03;
		}
		TextView getReTxt04(){
			if(re_txt04 == null) re_txt04 = (TextView)base.findViewById(R.id.list_re_txt04);
			return re_txt04;
		}
		
		
		
		
		LinearLayout getListLay(){
			if(list_lay == null) list_lay = (LinearLayout)base.findViewById(R.id.list_lay);
			return list_lay;
		}
		
		TextView getListTxt01(){
			if(list_txt01 == null) list_txt01 = (TextView)base.findViewById(R.id.list_txt01);
			return list_txt01;
		}		
		TextView getListTxt02(){
			if(list_txt02 == null) list_txt02 = (TextView)base.findViewById(R.id.list_txt02);
			return list_txt02;
		}		
		TextView getListTxt03(){
			if(list_txt03 == null) list_txt03 = (TextView)base.findViewById(R.id.list_txt03);
			return list_txt03;
		}
		TextView getListTxt04(){
			if(list_txt04 == null) list_txt04 = (TextView)base.findViewById(R.id.list_txt04);
			return list_txt04;
		}
		
	}
	

}
