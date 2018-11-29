package com.ex.group.mail.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.mail.data.EmailReceiveListData;
import com.ex.group.mail.util.EmailClientUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ex.group.folder.R;
import com.ex.group.folder.R.drawable;

/**
 * 메일 리스트 adapter
 * 
 * @author sjsun5318
 *
 */
public class EmailReceiveListAdapter extends ArrayAdapter<EmailReceiveListData> {

	private int del = 0;
	protected static final int COLOR_READ = Color.rgb(207, 213, 223);
	protected static final int COLOR_NAME_TEXT = Color.rgb(70, 70, 70);
	protected static final int COLOR_UNREAD_TEXT1 = Color.rgb(124, 175, 34);
	protected static final int COLOR_UNREAD_TEXT2 = Color.rgb(0, 0, 0);
	protected static final int COLOR_UNREAD_BG = Color.rgb(255, 255, 255);
	protected static final int COLOR_READ_BG = Color.rgb(226, 226, 226);
	protected static final int COLOR_MAIN_BG = Color.rgb(247, 247, 247);
	protected static final int COLOR_DATE = Color.rgb(15, 102, 163);
	protected static final int COLOR_CONTENT = Color.rgb(102, 102, 102);

	private final String date_type_1 = "yy.MM.dd";
	// private final String date_type_2 = "MM.dd ( EEE )";
	// private final String date_type_3 = "( EEE ) aaa hh:mm";
	private final String date_type_4 = "aaa hh:mm";
	private Context mContext;
	private Resources res;

	public EmailReceiveListAdapter(Context a_context, int a_nTextViewResourceId) {
		super(a_context, a_nTextViewResourceId);
		mContext = a_context;
		res = mContext.getResources();
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	private String dateFormat(String originalDate) {

		int year = Integer.parseInt(originalDate.substring(0, 4));
		int month = Integer.parseInt(originalDate.substring(5, 7));
		int day = Integer.parseInt(originalDate.substring(8, 10));
		int hourOfDay = Integer.parseInt(originalDate.substring(11, 13));
		int minute = Integer.parseInt(originalDate.substring(14, 16));
		int second = Integer.parseInt(originalDate.substring(17, 19));

		Calendar nowDate = Calendar.getInstance();
		nowDate.set(new Date().getYear(), new Date().getMonth(),
				new Date().getDate());

		Calendar oldDate = Calendar.getInstance();
		oldDate.set(year, month - 1, day, hourOfDay, minute, second);

		DateFormat dateformate = new SimpleDateFormat(date_type_1);

		if (dateformate.format(nowDate.getTime()).equals(
				dateformate.format(oldDate.getTime()))) {
			dateformate = new SimpleDateFormat(date_type_4);
			return dateformate.format(oldDate.getTime());
		} else {
			return dateformate.format(oldDate.getTime());
		}
	}

	/**
	 * View UI 표현
	 * 
	 * @param a_nPosition
	 * @param a_convertView
	 * @param a_parent
	 */
	public View getView(final int a_nPosition, View a_convertView,
                        ViewGroup a_parent) {
		View v = a_convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.mail_list_item, null);

		}
		final EmailReceiveListData item = getItem(a_nPosition);
		if (item != null) {
			TextView[] m_TextView = new TextView[3];
			m_TextView[0] = (TextView) v.findViewById(R.id.Email_Name);
			m_TextView[1] = (TextView) v.findViewById(R.id.DATE);
			m_TextView[2] = (TextView) v.findViewById(R.id.RECEIVETITLE);

			String info = "";
			if (item.getBoxType().equals("S")) {
				info = item.getToList();
				String[] infoList = info.split(";");
				if (infoList.length > 1) {
					info = EmailClientUtil.getNameString(infoList[0]) + "외"
							+ (infoList.length - 1) + "명";
				} else if (infoList.length == 1) {
					info = EmailClientUtil.getNameString(infoList[0]);
				}

			} else {
				info = EmailClientUtil.getNameString(item.getFromInfo());
			}
			m_TextView[0].setText(info);

			// if(item.getBoxType().equals("I")){
			// if(item.getReceivedDate()!= null &&
			// !item.getReceivedDate().equals("")){
			// String date = dateFormat(item.getReceivedDate());
			// m_TextView[1].setText(date);
			// }
			// } else {
			// if(item.getSendDate() != null && !item.getSendDate().equals("")){
			// String date = dateFormat(item.getSendDate());
			// m_TextView[1].setText(date);
			//
			// }
			// }
			String date = item.getReceivedDate();
			if (null != item.getReceivedDate() && !"".equals(date)
					&& date.length() > 8) {
				m_TextView[1].setText(date.substring(0, 4) + "/"
						+ date.substring(4, 6) + "/" + date.substring(6, 8));
			} else {
				m_TextView[1].setText(item.getReceivedDate());
			}

			m_TextView[2].setText(item.getMailSubject());
			// 답장요구, 보통, 긴급, 보안, 예약
			String mailType = item.getMailType();
			ImageView img = (ImageView) v.findViewById(R.id.MAIN_ICON2);
			ImageView imgReplay = (ImageView) v.findViewById(R.id.img_replay);
			ImageView imgSecret = (ImageView) v.findViewById(R.id.img_secret);

			/*
			 * //답장인지 아닌지 알수가 없다. if("답장".equals(mailType)) {
			 * img.setVisibility(View.INVISIBLE);
			 * 
			 * } else
			 */
			imgReplay.setVisibility(View.INVISIBLE);
			imgSecret.setVisibility(View.INVISIBLE);
			/*
			 * if(mailType!=null && mailType.indexOf("isReply")>-1) {
			 * //img.setImageDrawable(res.getDrawable(R.drawable.mail_03));
			 * imgReplay.setVisibility(View.VISIBLE); } else if(mailType!=null
			 * && mailType.indexOf("isFast")>-1) {
			 * img.setImageDrawable(res.getDrawable(R.drawable.icon_mail_red));
			 * } else if(mailType!=null && mailType.indexOf("isSecret")>-1) {
			 * imgSecret.setVisibility(View.VISIBLE); } else
			 * if("RESV".equals(mailType)) {
			 * img.setImageDrawable(res.getDrawable(R.drawable.icon_mail_blue));
			 * 
			 * } else {
			 * img.setImageDrawable(res.getDrawable(R.drawable.icon_mail_green
			 * )); }
			 */

			if (mailType != null && mailType.indexOf("isReply") > -1) {
				// imgReplay.setVisibility(View.VISIBLE);
				img.setImageDrawable(res
						.getDrawable(R.drawable.mail_icon_mail_reply));
			}
			if (mailType != null && mailType.indexOf("isFast") > -1)
				img.setImageDrawable(res.getDrawable(R.drawable.mail_icon_mail_red));
			if (mailType != null && mailType.indexOf("isSecret") > -1) {
				// imgSecret.setVisibility(View.VISIBLE);
				img.setImageDrawable(res
						.getDrawable(R.drawable.mail_icon_mail_secret));
			}

			if (mailType == null || ("").equals(mailType)) {
				img.setImageDrawable(res
						.getDrawable(R.drawable.mail_icon_mail_green));
			}

			ImageView m_mainIcon = (ImageView) v.findViewById(R.id.MAIN_ICON2);
			ImageView icon_clip = (ImageView) v.findViewById(R.id.MAIN_ICON3);
			// 열람 메일, 미열람 메일 구분X
			/*
			 * if("1".equals(item.getIsRead())) {
			 * 
			 * m_mainIcon.setImageDrawable(res.getDrawable(R.drawable.icon_mail_02
			 * )); //m_TextView[1].setTextColor(mContext.getResources().
			 * getColorStateList(R.color.gray_white_color_selector));
			 * m_TextView[
			 * 2].setTextColor(mContext.getResources().getColorStateList
			 * (R.color.gray_white_color_selector)); } else {
			 * m_mainIcon.setImageDrawable
			 * (res.getDrawable(R.drawable.icon_mail));
			 * //m_TextView[1].setTextColor
			 * (mContext.getResources().getColorStateList
			 * (R.color.list_date_text_white_color_selector));
			 * m_TextView[2].setTextColor
			 * (mContext.getResources().getColorStateList
			 * (R.color.list_text_selector)); }
			 */

			m_TextView[0].setTextColor(mContext.getResources()
					.getColorStateList(
							R.color.mail_list_date_text_white_color_selector));
			m_TextView[1].setTextColor(mContext.getResources()
					.getColorStateList(
							R.color.mail_list_date_text_white_color_selector));
			m_TextView[2].setTextColor(mContext.getResources()
					.getColorStateList(
							R.color.mail_list_date_text_white_color_selector));
			if ("0".equals(item.getHasAttachments())) {
				m_TextView[0].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
			} else {
				// m_TextView[0].setCompoundDrawablesWithIntrinsicBounds(0, 0,
				// R.drawable.icon_clip, 0);
				// m_TextView[0].setCompoundDrawablePadding(10);
				icon_clip.setVisibility(View.VISIBLE);
			}

			LinearLayout m_mainLayout = (LinearLayout) v
					.findViewById(R.id.MAIN_LAYOUT);
			if (a_nPosition % 2 == 0) {
				m_mainLayout.setBackgroundDrawable(getContext().getResources()
						.getDrawable(R.drawable.mail_list_view_selector));
			} else {
				m_mainLayout.setBackgroundDrawable(getContext().getResources()
						.getDrawable(R.drawable.mail_list_view_selector));
			}

			ImageView button = (ImageView) v.findViewById(R.id.CHECK_BUTTON);

			if (del == 1) {
				button.setVisibility(View.VISIBLE);
				// LinearLayout delpadding =
				// (LinearLayout)v.findViewById(R.id.delpadding);
				// delpadding.setPadding(20, delpadding.getPaddingTop(),
				// delpadding.getPaddingRight(), delpadding.getPaddingBottom());
				// m_mainIcon.setVisibility(View.GONE);
			} else {
				item.setDel(false);
				button.setVisibility(View.GONE);

				if ("1".equals(item.getIsRead())) { // 읽은 메일
					m_mainIcon.setVisibility(View.VISIBLE);
					// m_mainIcon.setImageResource(R.drawable.icon_nonemail);
				} else {
					m_mainIcon.setVisibility(View.VISIBLE);
					m_mainIcon.setImageResource(R.drawable.mail_icon_mail);
				}
			}
			if (item.isDel()) {
				button.setBackgroundResource(drawable.mail_check_on);
			} else {
				button.setBackgroundResource(drawable.mail_check_off);
			}

		}
		return v;
	}

}
