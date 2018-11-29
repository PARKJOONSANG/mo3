package com.ex.group.mail.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ex.group.mail.data.EmailDatabaseHelper;
import com.ex.group.mail.data.EmailDetailData;
import com.ex.group.mail.data.EmailFileListData;
import com.ex.group.mail.data.EmailReceiveListData;
import com.ex.group.mail.data.FileContents;
import com.ex.group.mail.data.NameMail;
import com.ex.group.mail.util.EmailClientUtil;
import com.ex.group.mail.util.EmailFileDialog;
import com.ex.group.mail.widget.DownloadHelper;
import com.ex.group.mail.widget.FileListDialog;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.conf.Environ;
import com.skt.pe.common.conf.EnvironManager;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.data.SKTWebUtil;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ex.group.folder.R;
/**
 * 메일 상세 화면
 * 
 * @author sjsun5318
 *
 */
public class EmailDetailActivity extends SKTActivity implements OnClickListener {

	private final String tmpPath = "/sdcard/cjmo/.tmp";
	private final String downPath_drm = "/sdcard/cjmo/drm";
	private final String downPath_mail = "/sdcard/cjmo/mail";
	private final int FILE_ECM = 1;
	private final int FILE_MAIL = 2;
	private DownloadHelper downloadHelper = null;
	private EmailDetailData detailData = null;
	private ArrayList<String> fileIdList = null;
	private ArrayList<String> fileNameList = null;
	private ArrayList<FileContents> fileContentList = null;
	private ArrayList<String> contentSizeList = null;
	private ArrayList<String> fileEcmList = null;
	private ArrayList<String> authorization = null;
	private ArrayList<String> checkUrl = null;
	private ArrayList<String> downloadUrl = null;
	private Intent intent = null;
	private HashMap<String, String> attachMap = new HashMap<String, String>();
	private ArrayList<Map<String, Object>> fileList = null;
	private TextView[] a_title = null;
	private LinearLayout detailLinearLayout = null;
	private String[] m_mailIds = null;
	private String[] m_changeKeys = null;
	private EmailReceiveListData receive = null;
	private NameMail fromInfo = null;
	private NameMail[] toList = null;
	private NameMail[] ccList = null;
	private NameMail[] bccList = null;
	private WebView webView = null;
	private FileListDialog dia = null;
	private String id;
	private String mailId;
	private String mailChangeKey;
	private String modelName;
	private String m_fileDocId = "";
	private int m_curIndex = 0;
	private int m_nVisibility = 0;
	private int m_nVisibility_cc = 0;
	private int fileIndex = 0;
	private String folderType = "";
	private String toTotalCnt = "";
	// private String ccTotalCnt = "";
	private String boxName = "";

	/**
	 * onCreate 메소드
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreateX(Bundle savedInstanceState) {
		Log.d("EmailDetailActivity", "JSJ process chedck1");
		EnvironManager.setNeedEncPwd(true);
		modelName = Build.MODEL;
		intent = getIntent();
		receive = (EmailReceiveListData) intent.getParcelableExtra("receive");
		m_mailIds = intent.getStringArrayExtra("mailIds");
		m_changeKeys = intent.getStringArrayExtra("changeKeys");
		m_curIndex = intent.getIntExtra("curIndex", 0);
		id = intent.getStringExtra("id");
		boxName = intent.getStringExtra("boxName");

		// Log.i("", "m_changeKeys.length : "+m_changeKeys.length);
		// for(int i=0; i<m_changeKeys.length; i++){
		// Log.i("", i+" : "+ m_changeKeys[i]);
		// }

		Log.d("", "value check = " + receive);
		Log.d("", "value check = " + m_mailIds[0]);
		Log.d("", "value check = " + m_curIndex);
		Log.d("", "value check = " + id);
		Log.d("", "value check = " + boxName);

		mailId = receive.getMailId();

		mailChangeKey = receive.getMailChangeKey();
		// Log.i("", "mailChangeKey : "+mailChangeKey);

		folderType = receive.getBoxType();
		Log.d("EmailDetailActivity", "JSJ process chedck2");
		TextView settitle = (TextView) findViewById(R.id.settitle);
		checkEmailAddr();
		settitle.setText("상세보기");
		Log.d("EmailDetailActivity", "JSJ process chedck3");
		a_title = new TextView[7];
		a_title[0] = (TextView) findViewById(R.id.Detail_From);
		a_title[1] = (TextView) findViewById(R.id.Detail_To);
		a_title[2] = (TextView) findViewById(R.id.Detail_Cc);
		a_title[3] = (TextView) findViewById(R.id.Detail_Bcc);
		a_title[4] = (TextView) findViewById(R.id.Detail_Subject);
		a_title[5] = (TextView) findViewById(R.id.Detail_SendDate);
		a_title[6] = (TextView) findViewById(R.id.Detail_Content);
		detailLinearLayout = (LinearLayout) findViewById(R.id.detailLinearLayout);
		if ("SHW-M110S".equals(modelName)) {

		} else if ("SHW-M180S".equals(modelName)) {
			detailLinearLayout.setMinimumHeight(943);
		}
		// layoutPicture = (LinearLayout)findViewById(R.id.Layout5);
		webView = (WebView) findViewById(R.id.Detail_Content_HTML);
		
		
		findViewById(R.id.top_close).setOnClickListener(this);			//닫기버튼
		findViewById(R.id.Layout).setOnClickListener(this); // 열림, 닫힘 버튼(받는사람이 다수 일 경우)
		findViewById(R.id.Layout_cc).setOnClickListener(this); // 참조버튼
		findViewById(R.id.BtnReply).setOnClickListener(this); // 답장버튼
		findViewById(R.id.BtnFile).setOnClickListener(this); // 파일버튼
		// findViewById(R.id.BtnForward).setOnClickListener(this); //전달 버튼
		//
		// if(receive.getParentBoxType().equals("S") ) {
		// findViewById(R.id.BtnReplyAll).setVisibility(View.GONE);
		// } else if(receive.getParentBoxType().equals("D")) {
		// findViewById(R.id.BtnReply).setVisibility(View.GONE);
		// findViewById(R.id.BtnReplyAll).setVisibility(View.GONE);
		// } else {
		// findViewById(R.id.BtnReply).setVisibility(View.VISIBLE);
		// findViewById(R.id.BtnReplyAll).setVisibility(View.VISIBLE);
		// }

		findViewById(R.id.BtnDelMail).setOnClickListener(this); // 삭제버튼
		findViewById(R.id.picture).setOnClickListener(this);

		// if(!StringUtil.isNull(EmailClientUtil.companyCd) &&
		// EmailClientUtil.companyCd.equals("SKT")) {
		// layoutPicture.setVisibility(View.GONE);
		// } else {
		// layoutPicture.setVisibility(View.VISIBLE);
		// }

		findViewById(R.id.picture_layout).setVisibility(View.GONE);
		ScrollView v = (ScrollView) findViewById(R.id.SCROLLVIEW);
		v.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// onClick(findViewById(R.id.Detail_Temp));
				// onClick(findViewById(R.id.Detail_Temp));
				findViewById(R.id.Detail_Temp).setVisibility(View.VISIBLE);
				findViewById(R.id.Detail_Temp).setVisibility(View.GONE);
				return false;
			}
		});

		downloadHelper = new DownloadHelper(this);
		downloadHelper.bind();

		new Action(EmailClientUtil.COMMON_MAILADV_CONTENT).execute();

	}

	private void checkEmailAddr() {
		if (StringUtil.isNull(EmailClientUtil.empNm)) {
			EmailClientUtil.empNm = EmailClientUtil.getMail(EmailDetailActivity.this, EmailClientUtil.id);
		}
	}

	/**
	 * 액션 처리후 UI 설정<br>
	 * 
	 * @param primitive
	 *            액션명
	 */
	@Override
	protected void onActionPost(String primitive, final XMLData result,
                                SKTException e) {
		if (e != null) {
			if (e.getErrCode().equals("5402")) {
				e.alert("오류", this, new DialogButton(0) {

					public void onClick(DialogInterface arg0, int arg1) {

						EmailDatabaseHelper helper = new EmailDatabaseHelper(
								EmailDetailActivity.this);
						helper.deleteReceiveTable(id,
								new String[] { receive.getMailId() },
								new String[] { receive.getMailChangeKey() });
						helper.updateTable(id, new String[] { "TOTAL_COUNT" },
								new String[] { Integer.toString(Integer
										.parseInt(receive.getTotalCnt()) - 1) });
						helper.close();

						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				}, new DialogButton(0) {
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});
			} else if (e.getErrCode().equals("9999")
					|| e.getErrCode().equals("8888")) {
				e.alert(this);
			} else {
				e.alert("오류", this, new DialogButton(0) {
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});
			}
		} else {
			if (EmailClientUtil.COMMON_MAIL_CONTENT.equals(primitive)) {
				setUI(result);
			} else if (EmailClientUtil.COMMON_MAILADV_CONTENT.equals(primitive)) {
				setAdvUI(result);
			} else if (EmailClientUtil.COMMON_MAIL_DEL.equals(primitive)) {

				EmailDatabaseHelper helper = new EmailDatabaseHelper(
						EmailDetailActivity.this);
				helper.deleteReceiveTable(id,
						new String[] { detailData.getMailId() },
						new String[] { detailData.getChangeKey() });
				helper.updateTable(id, new String[] { "TOTAL_COUNT" },
						new String[] { Integer.toString(Integer
								.parseInt(receive.getTotalCnt()) - 1) });
				helper.close();
				setResult(RESULT_OK);
				finish();
			} else if (EmailClientUtil.COMMON_MAILADV_DEL.equals(primitive)) {

				EmailDatabaseHelper helper = new EmailDatabaseHelper(
						EmailDetailActivity.this);
				helper.deleteReceiveTable(id,
						new String[] { detailData.getMailId() },
						new String[] { detailData.getChangeKey() });
				helper.updateTable(id, new String[] { "TOTAL_COUNT" },
						new String[] { Integer.toString(Integer
								.parseInt(receive.getTotalCnt()) - 1) });
				helper.close();
				setResult(RESULT_OK);
				finish();
			} else if ("COMMON_MAIL_ATTACHCHECK".equals(primitive)) {
				try {
					if (result.get("isSecurity").equals("true")) {// 보안문서

						// SKTUtil.viewSecurityImage(EmailDetailActivity.this,
						// m_fileDocName, m_fileDocId);

					} else { // 비보안문서

						String isYN = EmailClientUtil.getFileView(
								EmailDetailActivity.this,
								EmailClientUtil.companyCd, EmailClientUtil.id);
						// SharedPreferences pref = null;
						// pref =
						// this.getSharedPreferences("PREF_TOFFICE_EMAIL", 0);
						// String s_alertYn = pref.getString("EMAIL_ALERT_YN",
						// "N");

						// s_alertYn = "N"; //for test

						if ("N".equals(isYN)) {

							final EmailFileDialog dlg = new EmailFileDialog(
									EmailDetailActivity.this);
							dlg.show();
							Button btn1 = (Button) dlg
									.findViewById(R.id.btnDlgDownOk);
							btn1.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {

									try {
										CheckBox cb = (CheckBox) dlg
												.findViewById(R.id.CheckNext);

										if (cb.isChecked()) {
											EmailClientUtil.setFileView(
													EmailDetailActivity.this,
													EmailClientUtil.companyCd,
													EmailClientUtil.id, "Y");
										}

										Controller ct = new Controller(
												EmailDetailActivity.this);
										Parameters paramAttach = new Parameters(
												"ATTACH_VIEW");
										paramAttach.put("docId", m_fileDocId);
										paramAttach.put("docPage", "1");
										paramAttach.put("companyCd",
												EmailClientUtil.companyCd);
										paramAttach.put("lang", "ko");

										String attUri = ct.getUri(paramAttach,
												Environ.FILE_ATTACHMENT);
										attUri = SKTUtil.getDownUrl(
												EmailDetailActivity.this,
												attUri);
										SKTUtil.openUrl(
												EmailDetailActivity.this,
												attUri);
										dlg.dismiss();
									} catch (SKTException e) {

										e.alert("오류", EmailDetailActivity.this,
												new DialogButton(0) {
													public void onClick(
															DialogInterface arg0,
															int arg1) {
														finish();
													}
												});
									}
								}
							});

							Button btn2 = (Button) dlg
									.findViewById(R.id.btnDlgDownCancel);
							btn2.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									dlg.dismiss();
								}
							});

						} else {
							try {
								Controller ct = new Controller(
										EmailDetailActivity.this);
								Parameters paramAttach = new Parameters(
										"ATTACH_VIEW");
								paramAttach.put("docId", m_fileDocId);
								paramAttach.put("docPage", "1");
								paramAttach.put("companyCd",
										EmailClientUtil.companyCd);
								paramAttach.put("lang", "ko");
								String attUri = ct.getUri(paramAttach,
										Environ.FILE_ATTACHMENT);
								attUri = SKTUtil.getDownUrl(
										EmailDetailActivity.this, attUri);
								SKTUtil.openUrl(EmailDetailActivity.this,
										attUri);
							} catch (SKTException ex) {

								ex.alert("오류", this, new DialogButton(0) {
									public void onClick(DialogInterface arg0,
											int arg1) {
										finish();
									}
								});
							}
						}

					}
				} catch (NotFoundException e1) {
					e1.printStackTrace();
				} catch (SKTException e1) {
					e1.alert("오류", this, new DialogButton(0) {
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
						}
					});
				}

			} else if (EmailClientUtil.COMMON_MAIL_ISREAD.equals(primitive)) {
				// ((ImageView)
				// findViewById(R.id.imgIsRead)).setImageResource(R.drawable.icon_mail_unread);
				// ((ImageView)
				// findViewById(R.id.imgIsRead)).setVisibility(View.VISIBLE);
			} else if ("checkUrl_1".equals(primitive)) {
				fileDownLoad(FILE_ECM, fileList.get(fileIndex).get("checkUrl")
						.toString(), fileList.get(fileIndex).get("downloadUrl")
						.toString(), fileList.get(fileIndex).get("fileName")
						.toString());
			}
		}
	}

	/**
	 * 상세화면 ALL UI 설정<br>
	 * 
	 * @param xmlData
	 */
	private void setAdvUI(XMLData xmlData) {
		try {
			m_nVisibility = 0;
			m_nVisibility_cc = 0;
			Button button = (Button) findViewById(R.id.Layout); // 받는사람 열림, 닫힘
																// 버튼
			button.setBackgroundResource(R.drawable.mail_icon_dropdown);

			LinearLayout layout1 = (LinearLayout) findViewById(R.id.Layout1);
			// layout1.setVisibility(View.GONE);

			ImageView file_icon = (ImageView) findViewById(R.id.detail_file_icon);

			// 발신자 정보

			fromInfo = detailData.getFromInfo();
			a_title[0]
					.setText(EmailClientUtil.getNameString(fromInfo.getName()));
			findViewById(R.id.FORM_LAYOUT).setVisibility(View.VISIBLE);

			if (receive.getParentBoxType().equals("S")) {
				findViewById(R.id.FORM_LAYOUT).setVisibility(View.GONE);
			}
			// 동보 수신자 숨김 처리 용
			String isHide = xmlData.get("isHide");
			toTotalCnt = xmlData.get("rcptCnt");
			toList = detailData.getToList();
			if (toList != null) {
				Log.d("", "수신자 리스트 toList " + toList.length);
				if (toList.length > 0) {
					// if(!"1".equals(isHide)){
					if (toList.length > 1) {
						findViewById(R.id.Layout).setVisibility(View.VISIBLE);
						findViewById(R.id.drop_layout).setVisibility(View.VISIBLE);
						if (toList[toList.length - 1].getName().indexOf("외(") > -1) {
							int toCnt = Integer.parseInt(toTotalCnt);
							a_title[1].setText(EmailClientUtil
									.getNameString(toList[0].getName())
									+ "외 "
									+ (toCnt - 1) + "명");
						} else {
							a_title[1].setText(EmailClientUtil
									.getNameString(toList[0].getName())
									+ "외 "
									+ (toList.length - 1) + "명");
						}
						// a_title[1].setText(EmailClientUtil.getNameString(toList[0].getName())+"외"+(toList.length-1)+"명");
						// a_title[1].setText(StringUtil.isNull(toList[0].getName())
						// ? toList[0].getMail() :
						// toList[0].getName()+"외"+(toList.length-1)+"명");
						// int toCnt = Integer.parseInt(toTotalCnt);
						// a_title[1].setText(EmailClientUtil.getNameString(toList[0].getName())+"외"+(toCnt-1)+"명");

					} else {
						findViewById(R.id.Layout).setVisibility(View.GONE);
						findViewById(R.id.drop_layout).setVisibility(View.GONE);
						// a_title[1].setText(StringUtil.isNull(toList[0].getName())
						// ? toList[0].getMail() : toList[0].getName());
						a_title[1].setText(EmailClientUtil
								.getNameString(toList[0].getName()));
						Log.d("", "수신자 리스트 toList toList[0].getName()"
								+ toList[0].getName());
					}
					// }
				}
			}

			// 참조자 리스트
			ccList = detailData.getCcList();

			if (ccList != null) {
				Log.d("", "수신자 리스트 ccList " + ccList.length);
				if (ccList.length > 0) {
					// if(!"1".equals(isHide)){
					if (ccList.length > 1) {
						findViewById(R.id.Layout_cc).setVisibility(View.VISIBLE);
						findViewById(R.id.drop_layout2).setVisibility(View.VISIBLE);
						a_title[2].setText(EmailClientUtil
								.getNameString(ccList[0].getName()) + "외 " 	+ (ccList.length - 1) + "명");
					} else {
						findViewById(R.id.Layout_cc).setVisibility(View.GONE);
						findViewById(R.id.drop_layout2).setVisibility(View.GONE);
						a_title[2].setText(EmailClientUtil
								.getNameString(ccList[0].getName()));
						Log.d("", "수신자 리스트 ccList ccList[0].getName()"	+ ccList[0].getName());
					}
					// }
				}
			}

			// 동보 수신자 숨김 처리
			if ("1".equals(isHide)) {
				findViewById(R.id.Detail_To).setVisibility(View.INVISIBLE);
				findViewById(R.id.Detail_Cc).setVisibility(View.INVISIBLE);
				findViewById(R.id.Layout).setVisibility(View.INVISIBLE);
				findViewById(R.id.Layout_cc).setVisibility(View.INVISIBLE);
//				findViewById(R.id.drop_layout).setVisibility(View.INVISIBLE);
				Log.i("", "---------------------");
//				findViewById(R.id.drop_layout2).setVisibility(View.INVISIBLE);
			}

			// 제목
			if (detailData.getMailTitle() == null
					|| detailData.getMailTitle().trim().length() == 0) {
				a_title[4].setText("제목없음");
			} else {
				a_title[4].setText(detailData.getMailTitle());
			}

			// 날짜
			String date = "";
			String time1[] = null;
			String time = "";
			int time2 = 0;

			// if(receive.getParentBoxType().equals("I1")||receive.getParentBoxType().equals("I2")){
			/*
			 * if(!StringUtil.isNull(detailData.getReceivedDate())) { date =
			 * detailData.getReceivedDate().substring(2,10).replace("-", ".");
			 * time1 =
			 * detailData.getReceivedDate().substring(11,xmlData.get("receivedDate"
			 * ).length()).split(":"); time2 = Integer.parseInt(time1[0]);
			 * if(time2 > 12) { time2 -=12; time =
			 * getResources().getString(R.string.pm)+time2+":"+time1[1]; } else
			 * { time =
			 * getResources().getString(R.string.am)+time1[0]+":"+time1[1]; } }
			 */
			// } else {
			if (!StringUtil.isNull(detailData.getSendDate())) {
				date = detailData.getSendDate().substring(2, 10)
						.replace("/", ".");
				time1 = detailData.getSendDate()
						.substring(11, detailData.getSendDate().length())
						.split(":");
				time2 = Integer.parseInt(time1[0]);
				if (time2 > 12) {
					time2 -= 12;
					time = getResources().getString(R.string.mail_pm) + time2 + ":"
							+ time1[1];
				} else {
					time = getResources().getString(R.string.mail_am) + time1[0]
							+ ":" + time1[1];
				}
			}
			// }

			a_title[5].setText(date + "   " + time);
			Log.i("detail", "file name.......... : "+ EmailClientUtil.setValue(xmlData.get("name")));
			if (null != detailData.getHasAttachments()
					&& !"".equals(detailData.getHasAttachments())
					&& !"0".equals(detailData.getHasAttachments())) {

				xmlData.setList("file");

				int f = 0;
				fileList = new ArrayList<Map<String, Object>>();
				fileIdList = new ArrayList<String>();
				fileNameList = new ArrayList<String>();
				contentSizeList = new ArrayList<String>();
				downloadUrl = new ArrayList<String>();
				fileContentList = new ArrayList<FileContents>();

				for (int j = 0; j < xmlData.size(); j++) {
					XMLData childXmlFileData = xmlData.getChild(j);

					fileIdList.add(EmailClientUtil.setValue(childXmlFileData
							.get("url")));
					fileNameList.add(EmailClientUtil.setValue(childXmlFileData
							.get("name")));
					Log.i("Detail","fileName : "+ EmailClientUtil.setValue(childXmlFileData.get("name")));
					contentSizeList.add(EmailClientUtil
							.setValue(childXmlFileData.get("len")));
					// downloadUrl.add(EmailClientUtil.setValue(childXmlFileData.get(i,"url")));

					FileContents filecontent = new FileContents();
					filecontent.setOper(childXmlFileData.get("oper"));
					filecontent.setKind("<kind>0</kind>");
					filecontent.setId(childXmlFileData.get("id"));
					filecontent.setSeq(childXmlFileData.get("seq"));
					filecontent.setName(childXmlFileData.get("name"));
					filecontent.setDesc(childXmlFileData.get("desc"));
					filecontent.setLen(childXmlFileData.get("len"));
					filecontent.setPath(childXmlFileData.get("path"));
					filecontent.setUrl(childXmlFileData.get("url"));
					fileContentList.add(filecontent);

					f++;
				}
				Button b = (Button) findViewById(R.id.BtnFile);

				// 첨부파일 무조건 있다.
				// if(f > 0){
				file_icon.setVisibility(View.VISIBLE);
//				b.setText("첨부파일(" + f + ")");
				// }else{
				/*
				 * b.setText("첨부파일");
				 * b.setBackgroundDrawable(getResources().getDrawable
				 * (R.drawable.btn_toolbar_d)); b.setEnabled(false);
				 * file_icon.setVisibility(View.GONE);
				 */
				// }

				for (int a = 0; a < fileIdList.size(); a++) {
					HashMap<String, Object> file = new HashMap<String, Object>();
					file.put("fileId", fileIdList.get(a));
					file.put("fileName", fileNameList.get(a));
					file.put("fileSize", contentSizeList.get(a));
					// file.put("downloadUrl", downloadUrl.get(a));

					fileList.add(a, file);
				}

				/*
				 * int xmlcount = 0; xmlcount = fileList.size(); if(xmlcount >
				 * 0) { LinearLayout layout =
				 * (LinearLayout)findViewById(R.id.FILEATTLIST);
				 * findViewById(R.id.FILELAYOUT).setVisibility(View.VISIBLE);
				 * layout.removeAllViews();
				 * 
				 * for(int a = 0 ; a < xmlcount ; a++){ final String m_FileName
				 * = fileList.get(a).get("fileName").toString(); final String
				 * m_FileId = fileList.get(a).get("fileId").toString(); final
				 * String m_FileSize =
				 * fileList.get(a).get("fileSize").toString(); //final String
				 * m_downloadUrl =
				 * fileList.get(a).get("downloadUrl").toString(); final String
				 * m_FileEcm = "false"; final int index = a; LinearLayout
				 * tempLayout = (LinearLayout)
				 * LayoutInflater.from(this).inflate(R.layout.filelist, null);
				 * TextView txtFileName = (TextView)
				 * tempLayout.findViewById(R.id.filebtn);
				 * txtFileName.setText(Html.fromHtml("<u>" + m_FileName + " ("
				 * +getFileSize(m_FileSize, "true".equals(m_FileEcm) ? true :
				 * false) + ")  <u>")); txtFileName.setOnClickListener(new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { fileIndex = index;
				 * //통신 불가 이면 break if(!connetedCheck()) { return; }
				 * 
				 * NetworkInfo wifi =
				 * SKTUtil.getWifiNetwork(EmailDetailActivity.this);
				 * if(wifi.isConnected()) { fileDownLoad(FILE_MAIL,m_FileId,
				 * m_FileName); return; }
				 * 
				 * String alsert_message = ""; if(getFileSize(m_FileSize,
				 * "true".equals(m_FileEcm) ? true : false).equals("ECM") ||
				 * getFileSize(m_FileSize, "true".equals(m_FileEcm) ? true :
				 * false).equals("NONE")) { alsert_message =
				 * getResources().getString(R.string.DOWNLOAD_NO_FILESIZE); }
				 * else { alsert_message =
				 * StringUtil.format(getResources().getString
				 * (R.string.DOWNLOAD_ALERT_1), getFileSize(m_FileSize,
				 * "true".equals(m_FileEcm) ? true : false),
				 * getDownLoadTime(m_FileSize)); } new
				 * SKTDialog(EmailDetailActivity
				 * .this,2).getDialog(getResources()
				 * .getString(R.string.dialog_ok), alsert_message, new
				 * DialogButton(0) {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { fileDownLoad(FILE_MAIL,m_FileId, m_FileName); }
				 * }).show();
				 * 
				 * } }); layout.addView(tempLayout); } }
				 */
			} else {
				// 첨부파일 없을ㄸㅐ
				Button b = (Button) findViewById(R.id.BtnFile);
//				b.setText("첨부파일");
				b.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.mail_attach_file_f));
				b.setEnabled(false);
				file_icon.setVisibility(View.GONE);
			}

			String body = detailData.getBody();

			xmlData.setList("NFMailInfoD");

			if ("0".equals(xmlData.get("isHtml"))) {
				webView.setVisibility(View.GONE);
				findViewById(R.id.picture_layout).setVisibility(View.GONE);
				a_title[6].setText(body);
				a_title[6].setVisibility(View.VISIBLE);

			} else {
				a_title[6].setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
				findViewById(R.id.picture_layout).setVisibility(View.VISIBLE);
				SKTWebUtil.loadWebView(this, webView, true, body, true);
			}

			LinearLayout file = (LinearLayout) findViewById(R.id.FILELAYOUT);

			file.setVisibility(View.GONE);
			LinearLayout fileLayout = (LinearLayout) findViewById(R.id.FILEATTLIST);
			fileLayout.removeAllViews();

			findViewById(R.id.FILELAYOUT).setVisibility(View.GONE);
			if (receive.getParentBoxType().equals("S")) {
				Button reply = (Button) findViewById(R.id.BtnReply);
//				reply.setText("재발송");
				reply.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_forward));
			}
			findViewById(R.id.picture_layout).setVisibility(View.GONE);
		} catch (SKTException e) {
			e.alert("오류", this, new DialogButton(0) {
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
		}
	}

	/**
	 * 상세화면 ALL UI 설정<br>
	 * 
	 * @param xmlData
	 */
	private void setUI(XMLData xmlData) {
		try {
			m_nVisibility = 0;
			m_nVisibility_cc = 0;
			Button button = (Button) findViewById(R.id.Layout);
			button.setBackgroundResource(R.drawable.mail_icon_dropdown);

			Button button_cc = (Button) findViewById(R.id.Layout_cc);
			button_cc.setBackgroundResource(R.drawable.mail_icon_dropdown);

			LinearLayout layout1 = (LinearLayout) findViewById(R.id.Layout1);
			layout1.setVisibility(View.GONE);

			ImageView file_icon = (ImageView) findViewById(R.id.detail_file_icon);

			// 발신자 정보

			fromInfo = detailData.getFromInfo();
			a_title[0].setText(StringUtil.isNull(fromInfo.getName()) ? fromInfo
					.getMail() : fromInfo.getName());
			findViewById(R.id.FORM_LAYOUT).setVisibility(View.VISIBLE);

			if (receive.getParentBoxType().equals("S")) {
				findViewById(R.id.FORM_LAYOUT).setVisibility(View.GONE);
			}

			// 수신자 리스트

			toList = detailData.getToList();
			Log.d("", "수신자 리스트 toList " + toList.length);
			if (toList.length > 0) {
				if (toList.length > 1) {
					findViewById(R.id.Layout).setVisibility(View.VISIBLE);
					a_title[1]
							.setText(StringUtil.isNull(toList[0].getName()) ? toList[0]
									.getMail() : toList[0].getName() + "외 "
									+ (toList.length - 1) + "명");
				} else {
					findViewById(R.id.Layout).setVisibility(View.GONE);
					a_title[1]
							.setText(StringUtil.isNull(toList[0].getName()) ? toList[0]
									.getMail() : toList[0].getName());
					Log.d("",
							"수신자 리스트 toList toList[0].getName()"
									+ toList[0].getName());
				}
			}

			// 제목
			if (detailData.getMailTitle() == null
					|| detailData.getMailTitle().trim().length() == 0) {
				a_title[4].setText("제목없음");
			} else {
				a_title[4].setText(detailData.getMailTitle());
			}

			// 날짜

			String date = "";
			String time1[] = null;
			String time = "";
			int time2 = 0;
			if (receive.getParentBoxType().equals("I")) {
				if (!StringUtil.isNull(detailData.getReceivedDate())) {
					date = detailData.getReceivedDate().substring(2, 10)
							.replace("-", ".");
					time1 = detailData
							.getReceivedDate()
							.substring(11,
									xmlData.get("receivedDate").length() - 1)
							.split(":");
					time2 = Integer.parseInt(time1[0]);
					if (time2 > 12) {
						time2 -= 12;
						time = getResources().getString(R.string.mail_pm) + time2
								+ ":" + time1[1];
					} else {
						time = getResources().getString(R.string.mail_am) + time1[0]
								+ ":" + time1[1];
					}
				}
			} else {
				if (!StringUtil.isNull(detailData.getSendDate())) {
					date = detailData.getSendDate().substring(2, 10)
							.replace("-", ".");
					time1 = detailData
							.getSendDate()
							.substring(11, xmlData.get("sendDate").length() - 1)
							.split(":");
					time2 = Integer.parseInt(time1[0]);
					if (time2 > 12) {
						time2 -= 12;
						time = getResources().getString(R.string.mail_pm) + time2
								+ ":" + time1[1];
					} else {
						time = getResources().getString(R.string.mail_am) + time1[0]
								+ ":" + time1[1];
					}
				}
			}

			a_title[5].setText(date + "   " + time);

			// 첨부파일-인라인이미지
			XMLData childXmlFileData = xmlData.getChild("attachmentsList");

			childXmlFileData.setList("fileAttachment");

			// file 숫자 카운트.
			int f = 0;
			fileList = new ArrayList<Map<String, Object>>();
			fileIdList = new ArrayList<String>();
			fileNameList = new ArrayList<String>();
			contentSizeList = new ArrayList<String>();
			fileEcmList = new ArrayList<String>();
			authorization = new ArrayList<String>();
			checkUrl = new ArrayList<String>();
			downloadUrl = new ArrayList<String>();

			for (int i = 0; i < childXmlFileData.size(); i++) {
				if (!StringUtil.isNull(childXmlFileData.get(i, "id"))) {
					fileIdList.add(EmailClientUtil.setValue(childXmlFileData
							.get(i, "id")));
					fileNameList.add(EmailClientUtil.setValue(childXmlFileData
							.get(i, "name")));
					Log.i("", "file name : "+ EmailClientUtil.setValue(childXmlFileData.get(i, "name")));
					contentSizeList.add(EmailClientUtil
							.setValue(childXmlFileData.get(i, "contentSize")));
					fileEcmList.add(EmailClientUtil.setValue(childXmlFileData
							.get(i, "isEcm")));
					authorization.add(EmailClientUtil.setValue(childXmlFileData
							.get(i, "authorization")));
					checkUrl.add(EmailClientUtil.setValue(childXmlFileData.get(
							i, "checkUrl")));
					downloadUrl.add(EmailClientUtil.setValue(childXmlFileData
							.get(i, "downloadUrl")));

					f++;
				} else {
					attachMap.put(
							StringUtil.isNull(childXmlFileData.get(i,
									"contentId")) ? "" : childXmlFileData.get(
									i, "contentId"),
							StringUtil.isNull(childXmlFileData
									.get(i, "content")) ? "" : childXmlFileData
									.get(i, "content"));
				}
			}

			Button b = (Button) findViewById(R.id.BtnFile);
			Log.i("detail", "f : "+f);
			if (f > 0) {
				file_icon.setVisibility(View.VISIBLE);
//				b.setText("첨부파일(" + f + ")");
			} else {
//				b.setText("첨부파일");
				b.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.mail_attach_file_f));
				b.setEnabled(false);
				file_icon.setVisibility(View.GONE);
			}

			for (int a = 0; a < fileIdList.size(); a++) {
				HashMap<String, Object> file = new HashMap<String, Object>();
				file.put("fileId", fileIdList.get(a));
				file.put("fileName", fileNameList.get(a));
				file.put("fileSize", contentSizeList.get(a));
				file.put("fileEcm", fileEcmList.get(a));
				file.put("authorization", authorization.get(a));
				file.put("checkUrl", checkUrl.get(a));
				file.put("downloadUrl", downloadUrl.get(a));
				fileList.add(a, file);
			}

			String body = detailData.getBody();

			// 메일본문 처리
			if (detailData.getBodyType().equals("Text")) {
				webView.setVisibility(View.GONE);
				findViewById(R.id.picture_layout).setVisibility(View.GONE);
				a_title[6].setText(body);
				a_title[6].setVisibility(View.VISIBLE);

			} else {
				a_title[6].setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
				findViewById(R.id.picture_layout).setVisibility(View.VISIBLE);
				SKTWebUtil.loadWebView(this, webView, true, body,
						!StringUtil.isNull(detailData.getIsImg()));
			}

			LinearLayout file = (LinearLayout) findViewById(R.id.FILELAYOUT);
			if (f > 0) {
				file.setVisibility(View.VISIBLE);
			} else {
				file.setVisibility(View.GONE);
			}
			file.setVisibility(View.GONE);
			LinearLayout fileLayout = (LinearLayout) findViewById(R.id.FILEATTLIST);
			fileLayout.removeAllViews();

			int xmlcount = 0;
			xmlcount = fileList.size();
			if (xmlcount > 0) {
				LinearLayout layout = (LinearLayout) findViewById(R.id.FILEATTLIST);
				findViewById(R.id.FILELAYOUT).setVisibility(View.VISIBLE);
				layout.removeAllViews();

				for (int a = 0; a < xmlcount; a++) {
					final String m_FileName = fileList.get(a).get("fileName")
							.toString();
					final String m_FileId = fileList.get(a).get("fileId")
							.toString();
					final String m_FileSize = fileList.get(a).get("fileSize")
							.toString();
					final String m_FileEcm = fileList.get(a).get("fileEcm")
							.toString();
					final String m_checkUrl = fileList.get(a).get("checkUrl")
							.toString();
					final String m_downloadUrl = fileList.get(a)
							.get("downloadUrl").toString();
					final int index = a;
					LinearLayout tempLayout = (LinearLayout) LayoutInflater
							.from(this).inflate(R.layout.mail_filelist, null);
					TextView txtFileName = (TextView) tempLayout
							.findViewById(R.id.filebtn);
					txtFileName.setText(Html.fromHtml("<u>"
							+ m_FileName
							+ " ("
							+ getFileSize(m_FileSize,
									"true".equals(m_FileEcm) ? true : false)
							+ ")  <u>"));
					txtFileName.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							fileIndex = index;
							// 통신 불가 이면 break
							if (!connetedCheck()) {
								return;
							}

							NetworkInfo wifi = SKTUtil
									.getWifiNetwork(EmailDetailActivity.this);
							if (wifi.isConnected()) {
								if ("true".equals(m_FileEcm)) {
									new Action("checkUrl_1").execute(
											m_checkUrl, m_downloadUrl,
											m_FileName);
								} else {
									fileDownLoad(FILE_MAIL, m_FileId,
											m_FileName);
								}
								return;
							}

							String alsert_message = "";
							if (getFileSize(m_FileSize,
									"true".equals(m_FileEcm) ? true : false)
									.equals("ECM")
									|| getFileSize(
											m_FileSize,
											"true".equals(m_FileEcm) ? true
													: false).equals("NONE")) {
								alsert_message = getResources().getString(
										R.string.mail_DOWNLOAD_NO_FILESIZE);
							} else {
								alsert_message = StringUtil.format(
										getResources().getString(
												R.string.mail_DOWNLOAD_ALERT_1),
										getFileSize(m_FileSize, "true"
												.equals(m_FileEcm) ? true
												: false),
										getDownLoadTime(m_FileSize));
							}
							new SKTDialog(EmailDetailActivity.this, 2)
									.getDialog(
											getResources().getString(
													R.string.mail_dialog_ok),
											alsert_message,
											new DialogButton(0) {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													if ("true"
															.equals(m_FileEcm)) {
														new Action("checkUrl_1")
																.execute(
																		m_checkUrl,
																		m_downloadUrl,
																		m_FileName);
													} else {
														fileDownLoad(FILE_MAIL,
																m_FileId,
																m_FileName);
													}
												}
											}).show();

						}
					});
					layout.addView(tempLayout);
				}
			} else {

			}
			findViewById(R.id.FILELAYOUT).setVisibility(View.GONE);
			if (receive.getParentBoxType().equals("S")) {
				Button reply = (Button) findViewById(R.id.BtnReply);
//				reply.setText("재발송");
				reply.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_forward));
				
			}

			if (detailData.getIsRead().equals("true")) {
				findViewById(R.id.picture_layout).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.picture_layout).setVisibility(View.GONE);
			}

		} catch (SKTException e) {
			e.alert("오류", this, new DialogButton(0) {
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
		}
	}

	// 소요시간(초) = 파일크기(MB) * 8 / 1.42(Mbps) (이통3사 3G 평균값)55.55MB
	private String getFileSize(String fileSize, boolean ecm) {

		if (fileSize == null || fileSize.equals("-1")) {
			if (ecm) {
				return "ECM";
			} else {
				return "NONE";
			}
		}

		double fileSizeFloat = -1;
		try {
			fileSizeFloat = Float.parseFloat(fileSize);

			DecimalFormat format = new DecimalFormat("####.##");

			if (fileSizeFloat / (1024 * 1024) >= 1) {
				fileSizeFloat = fileSizeFloat / (1024 * 1024);
				return format.format(fileSizeFloat) + " MB";
			} else {
				fileSizeFloat = fileSizeFloat / 1024;
				return format.format(fileSizeFloat) + " KB";
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (ecm) {
				return "ECM";
			} else {
				return "NONE";
			}
		}
	}

	private String getDownLoadTime(String fileSize) {
		if (fileSize == null || fileSize.equals("-1")) {
			return "0";
		}

		float fileSizeFloat = Float.parseFloat(fileSize.trim());
		fileSizeFloat = (fileSizeFloat / (1024 * 1024)) * 8 / 1.42f;
		return Math.round(fileSizeFloat) + "";
	}

	private boolean connetedCheck() {
		try {
			NetworkInfo wifi = SKTUtil.getWifiNetwork(this);
			NetworkInfo mobile = SKTUtil.getMobileNetwork(this);

			if (!wifi.isAvailable() && !mobile.isAvailable()) {
				throw new SKTException(
						"Network-Setting is invalid! [available:"
								+ wifi.isAvailable() + "/"
								+ mobile.isAvailable() + "]",
						Constants.Status.E_NETWORK);
			}

			if (!wifi.isConnectedOrConnecting()
					&& !mobile.isConnectedOrConnecting()) {
				throw new SKTException("Network isn't connected! [connected:"
						+ wifi.isConnected() + ","
						+ wifi.isConnectedOrConnecting() + "/"
						+ mobile.isConnected() + ","
						+ mobile.isConnectedOrConnecting() + "]",
						Constants.Status.E_NETWORK);
			}
		} catch (SKTException e) {
			e.alert(this, new DialogButton(0) {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});

			return false;
		}

		return true;
	}

	private void fileDownLoad(int type, String... args) {
		try {
			downloadHelper.showProgress();
			if (type == FILE_ECM) {
				// Controller controller = new
				// Controller(EmailDetailActivity.this);
				// String ret = controller.requestToString(args[0],null);
				// if(ret != null && ret.trim().equals("0")) {
				SKTUtil.unRunningDownloadService(EmailDetailActivity.this,
						tmpPath);
				SKTUtil.downloadPush(EmailDetailActivity.this, args[1],
						args[2], tmpPath, downPath_drm, null);
				// } else {
				// new SKTDialog(this,1).getDialog("실패", "권한이 없습니다.").show();
				// throw new Exception();
				// }
			} else if (type == FILE_MAIL) {
				Controller ct = new Controller(EmailDetailActivity.this);
				Parameters paramAttach = new Parameters(
						"COMMON_MAIL_ATTACHMENT");
				paramAttach.put("ID", args[0]);
				String attUri = ct.getUri(paramAttach, Environ.FILE_SERVICE);
				SKTUtil.unRunningDownloadService(EmailDetailActivity.this,
						tmpPath);
				SKTUtil.downloadPush(EmailDetailActivity.this, attUri, args[1],
						tmpPath, downPath_mail, null);
			}
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
			downloadHelper.dismissProgress();
		}
	}

	/**
	 * 액션 처리 핸들러<br>
	 * - 상세 화면 요청<br>
	 * - 상세 화면 삭제 요청<br>
	 * - 안읽음 표시 요청<br>
	 * - 첨부파일 보안 체크요청<br>
	 * 
	 * @param primitive
	 *            액션명
	 * @param args
	 *            파라미터
	 */
	@Override
	protected XMLData onAction(String primitive, String... args)
			throws SKTException {
		Parameters params = new Parameters(primitive);

		if (EmailClientUtil.COMMON_MAIL_CONTENT.equals(primitive)) {
			// 2014-02-06 JSJ 사원 아이디,이름 추가,작성자 이메일주소
			params.put("empId", EmailClientUtil.id);
			params.put("empName", EmailClientUtil.empNm);
			params.put("emailAddress", EmailClientUtil.emailAddress);

			params.put("ID", mailId);
			params.put("CHANGEKEY", mailChangeKey);
			params.put("inlineType", receive.getBoxId());
			params.put("mailBoxType", receive.getParentBoxType());

			Controller controller = new Controller(this);
			XMLData xmlData = controller.request(params);
			detailData = new EmailDetailData(xmlData);

			setRead();
			return xmlData;

		} else if (EmailClientUtil.COMMON_MAILADV_CONTENT.equals(primitive)) {

			String folderName = EmailClientUtil.getMailFolderKind(folderType);

			if ("private".equals(folderName))
				folderName = boxName;
			params.put("userID", EmailClientUtil.nedmsID);
			params.put("mailID", mailId);
			params.put("folderName", folderName);

			Controller controller = new Controller(this);
			XMLData xmlData = controller.request(params);

			detailData = new EmailDetailData(xmlData);

			setRead();
			return xmlData;
		} else if (EmailClientUtil.COMMON_MAIL_DEL.equals(primitive)) {
			// 2014-02-06 JSJ 사원 아이디,이름 추가,작성자 이메일주소
			params.put("empId", EmailClientUtil.id);
			params.put("empName", EmailClientUtil.empNm);
			params.put("emailAddress", EmailClientUtil.emailAddress);

			params.put("ID", args[0]);
			params.put("CHANGEKEY", args[1]);
			params.put("mailFolderId", receive.getBoxId());
			if (receive.getParentBoxType().equals("D"))
				params.put("isHardDel", "true");
			else
				params.put("isHardDel", "false");

			Controller controller = new Controller(this);
			return controller.request(params);
		} else if (EmailClientUtil.COMMON_MAILADV_DEL.equals(primitive)) {
			params.put("userID", EmailClientUtil.nedmsID);
			params.put("mailID", mailId);

			if (!"trash".equals(EmailClientUtil.getMailFolderKind(folderType))) {
				// 삭제
				if ("sent"
						.equals(EmailClientUtil.getMailFolderKind(folderType))) {
					params.put("mailFolderKind", "send"); // sent => send로 변경
				} else {
					params.put("mailFolderKind",
							EmailClientUtil.getMailFolderKind(folderType));
				}
				params.put("isHardDel", "N");
			} else {
				// 완전 삭제
				params.put("isHardDel", "Y");
			}

			Controller controller = new Controller(this);
			return controller.request(params);
		} else if (EmailClientUtil.COMMON_MAIL_ISREAD.equals(primitive)) {
			// 2014-02-06 JSJ 사원 아이디,이름 추가,작성자 이메일주소
			params.put("empId", EmailClientUtil.id);
			params.put("empName", EmailClientUtil.empNm);
			params.put("emailAddress", EmailClientUtil.emailAddress);

			params.put("ID", args[0]);
			params.put("CHANGEKEY", args[1]);
			params.put("ISREAD", "false");
			Controller controller = new Controller(this);
			XMLData xmlData = controller.request(params);
			setUnread();
			return xmlData;
		} else if ("checkUrl_1".equals(primitive)) {

			Controller controller = new Controller(EmailDetailActivity.this);
			String ret = controller.requestToString(args[0], null);
			if (ret != null && ret.trim().equals("0")) {
				// SKTUtil.unRunningDownloadService(EmailDetailActivity.this,
				// tmpPath);
				// SKTUtil.downloadPush(EmailDetailActivity.this,args[1],
				// args[2], tmpPath, downPath_drm, null);
				// fileDownLoad(FILE_ECM, args[1],args[2]);
			} else {
				throw new SKTException("권한이 없습니다.", "9999");
			}
		}

		return null;
	}

	/**
	 * DB 안읽음표시
	 */
	public void setUnread() {
		EmailDatabaseHelper helper = new EmailDatabaseHelper(this);
		helper.updateReceiveTable(id, detailData.getMailId(),
				detailData.getChangeKey(), new String[] { "IS_READ" },
				new String[] { "false" });
		helper.close();

	}

	/**
	 * DB 읽음표시
	 */
	public void setRead() {
		EmailDatabaseHelper helper = new EmailDatabaseHelper(this);
		helper.updateReceiveTable(id, detailData.getMailId(),
				detailData.getChangeKey(), new String[] { "IS_READ" },
				new String[] { "true" });
		helper.close();
	}

	/**
	 * 액션 처리전 UI 설정
	 * 
	 * @param primitive
	 *            액션명
	 */
	@Override
	protected int onActionPre(String primitive) {

		int ret = 0;

		if (EmailClientUtil.COMMON_MAIL_CONTENT.equals(primitive))
			ret = Action.SERVICE_RETRIEVING;

		return ret;
	}

	/**
	 * onClick 이벤트 핸들러<br>
	 * - 답장 버튼 - 전체 답장 버튼 - 삭제 버튼 - 참조/숨은참조 버튼 - 이전/다음 버튼 - 상세 autoImage 버튼
	 * 
	 * @param v
	 */
	public void onClick(View v) {

		if (v.getId() == R.id.Layout) {
			// LinearLayout layout1 = (LinearLayout)findViewById(R.id.Layout1);
			Button button = (Button) findViewById(R.id.Layout);

			if (m_nVisibility == 0) {
				// layout1.setVisibility(View.VISIBLE);
				button.setBackgroundResource(R.drawable.mail_icon_up);
				m_nVisibility = 1;

				String toNameMailList = "";
				for (int i = 0; i < toList.length; i++) {
					if (i > 0) {
						toNameMailList += "\n";
					}
					toNameMailList += EmailClientUtil.getNameString(toList[i]
							.getName());

				}
				a_title[1].setText(toNameMailList);

			}
			
			
			else if (m_nVisibility == 1) {
				button.setBackgroundResource(R.drawable.mail_icon_dropdown);
				// layout1.setVisibility(View.GONE);
				m_nVisibility = 0;
				if (toList.length > 0) {
					if (toList.length > 1) {
						if (toList[toList.length - 1].getName().indexOf("외(") > -1) {
							int toCnt = Integer.parseInt(toTotalCnt);
							a_title[1].setText(EmailClientUtil
									.getNameString(toList[0].getName())
									+ "외 "
									+ (toCnt - 1) + "명");
						} else {
							a_title[1].setText(EmailClientUtil
									.getNameString(toList[0].getName())
									+ "외 "
									+ (toList.length - 1) + "명");
						}
						// a_title[1].setText(EmailClientUtil.getNameString(toList[0].getName())+"외"+(toList.length-1)+"명");
					} else {
						a_title[1].setText(EmailClientUtil
								.getNameString(toList[0].getName()));
					}
				}
			}
		} else if (v.getId() == R.id.Layout_cc) {
			Button button_cc = (Button) findViewById(R.id.Layout_cc);

			if (m_nVisibility_cc == 0) {
				// layout1.setVisibility(View.VISIBLE);
				button_cc.setBackgroundResource(R.drawable.mail_icon_up);
				m_nVisibility_cc = 1;

				String toNameMailList = "";
				for (int i = 0; i < ccList.length; i++) {
					if (i > 0) {
						toNameMailList += "\n";
					}
					toNameMailList += EmailClientUtil.getNameString(ccList[i]
							.getName());

				}
				a_title[2].setText(toNameMailList);

			} else if (m_nVisibility_cc == 1) {
				button_cc.setBackgroundResource(R.drawable.mail_icon_dropdown);
				// layout1.setVisibility(View.GONE);
				m_nVisibility_cc = 0;
				if (ccList.length > 0) {
					if (ccList.length > 1) {
						a_title[2].setText(EmailClientUtil
								.getNameString(ccList[0].getName())
								+ "외 "
								+ (ccList.length - 1) + "명");
					} else {
						a_title[2].setText(StringUtil.isNull(ccList[0]
								.getName()) ? ccList[0].getMail() : ccList[0]
								.getName());
					}
				}
			}
			// 답장, 재발송
		} else if (v.getId() == R.id.BtnReply) {
			intent = new Intent(this, EmailWriteActivity.class);
			if (receive.getParentBoxType().equals("S")) {
				intent.putExtra("type", "resend");
				intent.putParcelableArrayListExtra("fileContentList",
						fileContentList);
			} else {
				intent.putExtra("type", "reply");
			}
			intent.putExtra("detailData", detailData);
			intent.putExtra("fromInfo", fromInfo);
			intent.putExtra("to", toList);
			intent.putExtra("cc", ccList);
			intent.putExtra("bcc", bccList);
			intent.putExtra("boxid", receive.getBoxId());
			intent.putExtra("boxtype", receive.getBoxType());

			intent.putExtra("mailID", mailId);
			intent.putStringArrayListExtra("fileNameList", fileNameList);
			intent.putStringArrayListExtra("fileIdList", fileIdList);
			startActivity(intent);
			// 전체답장
			// }else if(v.getId()== R.id.BtnReplyAll){
			//
			// intent = new Intent(this,EmailWriteActivity.class);
			//
			// intent.putExtra("type" , "replyall");
			// intent.putExtra("detailData", detailData);
			// intent.putExtra("fromInfo", fromInfo);
			// intent.putExtra("to", toList);
			// intent.putExtra("cc", ccList);
			// intent.putExtra("bcc", bccList);
			// intent.putStringArrayListExtra("fileNameList", fileNameList);
			// startActivity(intent);
			// 삭제
		} else if (v.getId() == R.id.BtnDelMail) {
			SKTDialog dlg = new SKTDialog(EmailDetailActivity.this,
					SKTDialog.DLG_TYPE_2);
			dlg.getDialog(getResources().getString(R.string.mail_alert_title_del),
					getResources().getString(R.string.mail_del_message),
					new DialogButton(0) {
						public void onClick(DialogInterface dialog, int which) {
							// new
							// Action(EmailClientUtil.COMMON_MAIL_DEL).execute(detailData.getMailId(),detailData.getChangeKey());
							new Action(EmailClientUtil.COMMON_MAILADV_DEL)
									.execute(detailData.getMailId(),
											detailData.getChangeKey());
						}
					}, new DialogButton(0) {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
			// }else if(v.getId()== R.id.PrevButton){ //이전버튼.
			// if(m_curIndex < m_mailIds.length-1){
			// mailId = m_mailIds[m_curIndex+1];
			// mailChangeKey = m_changeKeys[m_curIndex+1];
			// m_curIndex++;
			// new Action("COMMON_MAIL_CONTENT").execute();
			// }
			// }else if(v.getId()== R.id.NextButton){ //다음버튼.
			// if(m_curIndex > 0){
			// mailId = m_mailIds[m_curIndex-1];
			// mailChangeKey = m_changeKeys[m_curIndex-1];
			// m_curIndex--;
			// new Action("COMMON_MAIL_CONTENT").execute();
			// }
		} else if (v.getId() == R.id.picture) {

			findViewById(R.id.picture_layout).setVisibility(View.GONE);
			SKTWebUtil.invalidate(webView, true);
			detailData.setIsImg("true");
			/*
			 * webView.getSettings().setLoadsImagesAutomatically(true);
			 * webView.invalidate(); try { SKTWebUtil.loadWebView(this, webView,
			 * true, detailData.getBody(), null , true); } catch (SKTException
			 * e) {
			 * 
			 * e.printStackTrace(); }
			 */
			// } else if(v.getId() == R.id.BtnForward) {
			// //CJ 전달로 변경
			// intent = new Intent(this,EmailWriteActivity.class);
			//
			// intent.putExtra("type" , "forward");
			// intent.putExtra("boxType", receive.getParentBoxType());
			// intent.putExtra("id", id);
			// intent.putExtra("detailData", detailData);
			// intent.putExtra("fromInfo", fromInfo);
			// intent.putExtra("to", toList);
			// intent.putExtra("cc", ccList);
			// intent.putExtra("bcc", bccList);
			// intent.putStringArrayListExtra("fileNameList", fileNameList);
			// startActivity(intent);
			// dia = new FileListDialog(this,fileList);
			// dia.setOwnerActivity(this);
			// dia.show();
			//
			// LinearLayout linear =
			// (LinearLayout)dia.findViewById(R.id.ROOT_VIEW);
			//
			// if(getResources().getConfiguration().orientation ==
			// Configuration.ORIENTATION_LANDSCAPE) {
			// linear.getLayoutParams().height = 390;
			// } else {
			// linear.getLayoutParams().height = 475;
			// }

		} else if (v.getId() == R.id.BtnFile) {
			if (fileList != null && fileList.size() > 0) {

				/*
				 * Toast.makeText(this, "첨부파일", Toast.LENGTH_SHORT).show();
				 * 
				 * Controller ct = new Controller(EmailDetailActivity.this);
				 * Parameters paramAttach = new Parameters("ATTACH_VIEW");
				 * paramAttach.put("docId",
				 * fileList.get(0).get("downloadUrl").toString());
				 * paramAttach.put("docPage", "1"); paramAttach.put("companyCd",
				 * EmailClientUtil.companyCd); paramAttach.put("lang", "ko");
				 * String attUri;
				 */
				// try {

				// SKTUtil.viewImage(EmailDetailActivity.this,
				// fileList.get(0).get("fileName")+"", paramAttach);
				// attUri = ct.getUri(paramAttach, Environ.FILE_ATTACHMENT);
				// attUri = SKTUtil.getDownUrl(EmailDetailActivity.this,
				// attUri);
				// SKTUtil.openUrl(EmailDetailActivity.this, attUri);
				// } catch (Exception e) {
				// } catch (SKTException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				dia = new FileListDialog(this, fileList);
				dia.setOwnerActivity(this);
				dia.show();

				LinearLayout linear = (LinearLayout) dia
						.findViewById(R.id.ROOT_VIEW);

				// if(getResources().getConfiguration().orientation ==
				// Configuration.ORIENTATION_LANDSCAPE) {
				// linear.getLayoutParams().height = 390;
				// } else {
				// linear.getLayoutParams().height = 375; //575
				// }
			} else {
				SKTDialog d = new SKTDialog(this);
				d.getDialog("첨부파일이 없습니다.").show();
			}
		}
		
		else if(v.getId() == R.id.top_close){
			finish();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (dia == null) {
			return;
		}
		LinearLayout linear = (LinearLayout) dia.findViewById(R.id.ROOT_VIEW);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			linear.getLayoutParams().height = 390;
		} else {
			linear.getLayoutParams().height = 350; // 595
		}
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 옵션 메뉴 생성
	 * 
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mail_detailmenu, menu);

		// if(receive.getParentBoxType().equals("S") ) {
		// menu.findItem(R.id.MENUREPLY).setVisible(false);
		// menu.findItem(R.id.MENUREPLYALL).setVisible(false);
		// menu.findItem(R.id.MENUCHECKUNREAD).setVisible(false);
		// }

		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		downloadHelper.unbind();
		if (webView != null) {
			webView.clearCache(true);
		}
	}

	@Override
	protected void onStartX() {
		// TODO Auto-generated method stub
		super.onStartX();
		downloadHelper.bind();
	}

	/**
	 * 옵션 메뉴 선택 이벤트 헨들러<br>
	 * - 답장 - 전체 답장 - 전달 - 삭제 - 메일작성 - 안읽음 표시
	 * 
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.MENUFORWARD) { // 전달

			intent = new Intent(this, EmailWriteActivity.class);
			intent.putParcelableArrayListExtra("fileContentList",
					fileContentList);

			intent.putExtra("type", "forward");
			intent.putExtra("boxType", receive.getParentBoxType());
			intent.putExtra("id", id);
			intent.putExtra("detailData", detailData);
			intent.putExtra("fromInfo", fromInfo);
			intent.putExtra("to", toList);
			intent.putExtra("cc", ccList);
			intent.putExtra("bcc", bccList);
			intent.putExtra("boxid", receive.getBoxId());
			intent.putExtra("boxtype", receive.getBoxType());
			// mailId
			intent.putExtra("mailID", mailId);

			intent.putStringArrayListExtra("fileNameList", fileNameList);
			intent.putStringArrayListExtra("fileIdList", fileIdList);

			startActivity(intent);
			//
			// if(item.getItemId()==R.id.MENUREPLY){ //답장
			//
			// intent = new Intent(this,EmailWriteActivity.class);
			//
			// intent.putExtra("type" , "reply");
			// intent.putExtra("detailData", detailData);
			// intent.putExtra("fromInfo", fromInfo);
			// intent.putExtra("to", toList);
			// intent.putExtra("cc", ccList);
			// intent.putExtra("bcc", bccList);
			// intent.putStringArrayListExtra("fileNameList", fileNameList);
			// startActivity(intent);
			//
			// }else if (item.getItemId()==R.id.MENUREPLYALL){ //전체 답장
			//
			// intent = new Intent(this,EmailWriteActivity.class);
			//
			// intent.putExtra("type" , "replyall");
			// intent.putExtra("detailData", detailData);
			// intent.putExtra("fromInfo", fromInfo);
			// intent.putExtra("to", toList);
			// intent.putExtra("cc", ccList);
			// intent.putExtra("bcc", bccList);
			// intent.putStringArrayListExtra("fileNameList", fileNameList);
			// startActivity(intent);
			//
			// }else
			//
			// }else if(item.getItemId() == R.id.MENUDEL){ //삭제
			//
			// SKTDialog dlg = new SKTDialog(EmailDetailActivity.this,
			// SKTDialog.DLG_TYPE_2);
			// dlg.getDialog("삭제","정말로 삭제하겠습니까?",
			// new DialogButton(0) {
			// public void onClick(DialogInterface dialog, int which) {
			// new
			// Action("COMMON_MAIL_DEL").execute(detailData.getMailId(),detailData.getChangeKey());
			// }
			// },
			// new DialogButton(0) {
			// public void onClick(DialogInterface dialog, int which) {
			// }
			// }
			// ).show();
		} else if (item.getItemId() == R.id.MENUWRITE) { // 메일작성

			intent = new Intent(this, EmailWriteActivity.class);
			startActivity(intent);

			// }else if(item.getItemId()==R.id.MENUCHECKUNREAD){ //안읽음표시
			//
			// new
			// Action("COMMON_MAIL_ISREAD").execute(detailData.getMailId(),detailData.getChangeKey());
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * onActivityResult
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResultX(int requestCode, int resultCode,
			Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				finish();
			}
		}
	}

	/**
	 * onBackPressed 메소드
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("DetailBack", "Back");
		setResult(RESULT_OK, intent);
		finish();
		super.onBackPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.skt.pe.common.activity.SKTActivity#OnImageError()
	 */
	@Override
	public void OnImageError() {
		new Action(EmailClientUtil.COMMON_MAILADV_CONTENT).execute();
	}

	/**
	 * 파일 클릭 이벤트 핸들러
	 * 
	 * @author sjsun5318
	 *
	 */
	public class EventHandler implements OnClickListener {
		private EmailFileListData m_data;

		// private EmailDetailActivity m_activity;

		public EventHandler(EmailFileListData a_data,
				EmailDetailActivity a_activity) {
			m_data = a_data;
			// m_activity = a_activity;
		}

		public void onClick(View view) {
			Parameters params = new Parameters(
					EmailClientUtil.COMMON_ISS_RETRIEVE);
			// 2014-02-06 JSJ 사원 아이디,이름 추가
			params.put("empId", EmailClientUtil.id);
			params.put("empName", EmailClientUtil.empNm);
			params.put("docId", m_data.getM_szId());

			// SKTUtil.viewSecurityImage(EmailDetailActivity.this,
			// m_data.getM_szName(), params);
		}
	}

	/**
	 * 설정할 레이아웃을 리턴한다.
	 */
	@Override
	protected int assignLayout() {
		return R.layout.mail_detail;
	}

}
