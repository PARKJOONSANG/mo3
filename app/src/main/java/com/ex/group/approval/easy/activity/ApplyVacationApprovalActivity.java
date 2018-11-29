package com.ex.group.approval.easy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.dialog.WheelDatePickerDialogHelper;
import com.ex.group.approval.easy.dialog.ifaces.PEDialogInterface;
import com.ex.group.approval.easy.domain.VocCode;
import com.ex.group.approval.easy.primitive.DraftPrimitive;
import com.ex.group.approval.easy.primitive.VacCodePrimitive;
import com.ex.group.approval.easy.util.ActivityLauncher;
import com.ex.group.folder.R;
import com.skt.pe.common.activity.ifaces.CommonUI;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.primitive.util.PrimitiveList;
import com.skt.pe.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplyVacationApprovalActivity extends ApprovalCommonActivity implements OnClickListener, CommonUI {
	private final String TAG = "ApplyVacationApprovalActivity";
	
	private Button applicationDate = null;
	private Button fromDate = null;
	private Button untilDate = null;
	private Button selectGuntaeCode = null;
	private Button selectExceptDate = null;
//	private DraftFormPrimitive dformPrim = null;
	private DraftPrimitive draftPrim = new DraftPrimitive();
	private VacCodePrimitive vcPrim = null;
	
	// 2015-03-05 Join 추가 시작
	private TextView tvExceptDate1 = null;		// 총 휴가일수 (제외일수까지 포함한 값 = 단순히 휴가시작일 - 휴가종료일 만 계산된 값)
	private TextView tvExceptDate2 = null;		// 제외일수
	// 2015-03-05 Join 추가 끝
	
	private boolean[] exclusiveDateBackup = null;
	private int term = 0;
	
	private final static String activityTitle = "휴가신청서";
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected int assignLayout() {
		return R.layout.easy_apply_vacation;
	}
	
	@Override
    public void onCreateX(Bundle savedInstanceState) {
		setSubTitle(activityTitle);
		Intent intent = getIntent();
		vcPrim = (VacCodePrimitive) intent.getSerializableExtra(ApprovalConstant.IntentArg.PRIMITIVE);
		draftPrim.setDraftKind(DraftPrimitive.DraftKind.VACATIION);
		initUI();
    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	protected void onReceive(Primitive primitive, SKTException e) {
		super.onReceive(primitive, e);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.apply_vacation_button_targetdate:
				super.selectDate(new PEDialogInterface.OnClickListener() {
	
					@Override
					public void onClick(Object date, String formattedDate) {
						applicationDate.setText(formattedDate);
						draftPrim.setTargetDate((Date)date);
					}
				}, draftPrim.getTargetDate());
				break;
			case R.id.apply_vacation_button_fromdate:
				super.selectDate( new PEDialogInterface.OnClickListener() {
	
					@Override
					public void onClick(Object date, String formattedDate) {
						if (draftPrim.getTargetDate().after((Date)date) == true) {
							alert("휴가 시작일을 신청일 이후로 선택하시기 바랍니다.");
						}
						// 2015-03-05 Join 수정 - 휴가 시작일 선택에 불편함이 있어 주석처리
						/*else if (DateUtil.diffDate((Date)date, draftPrim.getUntilDate()) >= term) {
							alert(termOverMessage());
						}*/
						else {
								exclusiveDateBackup = null;
								setFromDate(date, formattedDate);
								setUntilDate(date, formattedDate);	// 2015-03-05 Join 추가 - 휴가 시작일을 선택하면 휴가 종료일도 같이 세팅되도록 요청하여 추가
								setVacationCount();
								setExceptCount();
						}
					}
				}, draftPrim.getFromDate());
				break;
			case R.id.apply_vacation_button_untildate:
				super.selectDate( new PEDialogInterface.OnClickListener() {
	
					@Override
					public void onClick(Object date, String formattedDate) {
						if (draftPrim.getFromDate().after((Date)date) == true) {
							alert("휴가 종료일을 휴가 시작일 이후로 선택하시기 바랍니다.");
						} 
						// 2015-03-05 Join 수정 - 휴가 종료일 선택에 불편함이 있어 주석처리
						/*else if (DateUtil.diffDate(draftPrim.getFromDate(), (Date)date) >= term) {
							alert(termOverMessage());
						}*/ 
						else {
							exclusiveDateBackup = null;
							setUntilDate(date, formattedDate);
							setVacationCount();
							setExceptCount();
						}
					}
				}, draftPrim.getUntilDate());
				break;
			case R.id.apply_vacation_button_guntae:
				super.selectGuntaeCode( new PEDialogInterface.OnClickListener() {
	
					@Override
					public void onClick(Object obj, String arg) {
						Log.d(TAG, "Guntae Selected: " + arg);
						
						VocCode vc = vcPrim.getVocCodeTree().getItem(arg);
						setFormCode(vc);
					}
				}, vcPrim.getVocCodeTree());
				break;
			case R.id.apply_vacation_button_exceptdate:
				if (getVacationCount() < 2) {
					alert("근태기간이 2일이상일 경우에만 선택이 가능합니다.");
				} else {
					super.selectExceptDate(draftPrim.getFromDate(), draftPrim.getUntilDate(), exclusiveDateBackup, new PEDialogInterface.OnClickListener() {
						@Override
						public void onClick(Object obj, String arg) {
							PrimitiveList exclusiveDate = new PrimitiveList(); 
							boolean[] userClicks = (boolean []) obj;
							exclusiveDateBackup = userClicks;
							Date fromDate = draftPrim.getFromDate();
							for (int i=0; i<userClicks.length; i++) {
								if (userClicks[i] == false) {
									exclusiveDate.add(DraftPrimitive.formatDate(DateUtil.addDate(fromDate, i)));
								}
							}
							SKTUtil.log("TEST", exclusiveDate.toString());
							draftPrim.setExclusiveDate(exclusiveDate.toString());
							setExceptCount();
						}
					});
				}
				break;
			case R.id.apply_vacation_button_ok:
				String message = validationUI();
				if (message == null) {
					// 2015-03-05 Join 추가 시작 - 상세한 휴가정보를 제공하기 위한 구문 추가
					draftPrim.setTotalVacation(tvExceptDate1.getText().toString());
					draftPrim.setExceptCount(tvExceptDate2.getText().toString());
					// 2015-03-05 Join 추가 끝
					ActivityLauncher.launchApprovalLine(ApplyVacationApprovalActivity.this, draftPrim, ApprovalConstant.EasyApprovalType.VACATION, activityTitle);
				}
				else
					alert(message);
				break;
			case R.id.apply_vacation_button_cancel:
				finish();
				break;
		} // switch
	}

	@Override
	public void initUI() {
		selectGuntaeCode = (Button) findViewById(R.id.apply_vacation_button_guntae);
		selectGuntaeCode.setOnClickListener(this);
		selectExceptDate = (Button) findViewById(R.id.apply_vacation_button_exceptdate);
		selectExceptDate.setOnClickListener(this);
		
		Button btnOk = (Button) findViewById(R.id.apply_vacation_button_ok);
		btnOk.setOnClickListener(this);
		Button btnCancel = (Button) findViewById(R.id.apply_vacation_button_cancel);
		btnCancel.setOnClickListener(this);
		
		// 근태코드 정보 기초 세팅
		VocCode initVc = vcPrim.getVocCodeTree().getItem("1_1_1"); 
		if (initVc == null) {
			alert("등록된 근태정보가 없습니다.", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}
		setFormCode(initVc);
		
		// 각종 날짜 초기 세팅
		initDateUI();
	}
	
	private void initDateUI() {
		Date currentDate = new Date(System.currentTimeMillis());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String strDate = format.format(currentDate).toString();
		
		Date initDate = null;
		try {
			initDate = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// 외출일 선택
		applicationDate = (Button) findViewById(R.id.apply_vacation_button_targetdate);
		applicationDate.setOnClickListener(this);
		
		applicationDate.setText(WheelDatePickerDialogHelper.generateDateFormat(initDate));
		draftPrim.setTargetDate(initDate);
		
		fromDate = (Button) findViewById(R.id.apply_vacation_button_fromdate);
		fromDate.setOnClickListener(this);
		
		fromDate.setText(WheelDatePickerDialogHelper.generateDateFormat(initDate));
		draftPrim.setFromDate(initDate);
		
		untilDate = (Button) findViewById(R.id.apply_vacation_button_untildate);
		untilDate.setOnClickListener(this);
		
		untilDate.setText(WheelDatePickerDialogHelper.generateDateFormat(initDate));
		draftPrim.setUntilDate(initDate);
		
		// 제외일 초기 세팅
		setVacationCount();
		setExceptCount();
	}
	
	private void setFromDate(Object date, String formattedDate) {
		fromDate.setText(formattedDate);
		draftPrim.setFromDate((Date)date);
	}
	
	private void setUntilDate(Object date, String formattedDate) {
		untilDate.setText(formattedDate);
		draftPrim.setUntilDate((Date)date);
	}
	
	private void setFormCode(VocCode vc) {
		
		Log.d(TAG, "getCodeNm : "+vc.getCodeNm());
		Log.d(TAG, "getCode : "+vc.getCode());
		Log.d(TAG, "getTerm : "+vc.getTerm());
		
		selectGuntaeCode.setText(vc.getCodeNm());
		draftPrim.setFormCode(vc.getCode());
		draftPrim.setTag(DraftPrimitive.Tags.FORM_CODE_NAME, vc.getCodeNm());
		TextView term = (TextView) findViewById(R.id.apply_vacation_textview_term);
		term.setText(String.valueOf(vc.getTerm()));
		this.term = vc.getTerm();
	}
	
	private void setExceptCount() {
		int exceptCount = 0;
		
		if (exclusiveDateBackup != null) {
			for (int i=0; i<exclusiveDateBackup.length; i++) {
				// 2015-03-05 Join 수정 시작 - 제외일 산정 방법 변경에 따른 수정
				// if (exclusiveDateBackup[i] == false)
				if (exclusiveDateBackup[i] == true)				
					exceptCount++;
				// 2015-03-05 Join 수정 끝
			}
		}
		tvExceptDate2 = (TextView) findViewById(R.id.apply_vacation_textview_exceptdate2);
		tvExceptDate2.setText(String.valueOf(exceptCount));
	}
	
	private void setVacationCount() {
		int vacationCount = 0;
		
		vacationCount = DateUtil.diffDate(draftPrim.getFromDate(), draftPrim.getUntilDate()) + 1;
		
		tvExceptDate1 = (TextView) findViewById(R.id.apply_vacation_textview_exceptdate1);
		tvExceptDate1.setText(String.valueOf(vacationCount));
	}
	
	private int getVacationCount() {
		return DateUtil.diffDate(draftPrim.getFromDate(), draftPrim.getUntilDate()) + 1;
	}
	
	private String termOverMessage() {
		String message = (String) draftPrim.getTag(DraftPrimitive.Tags.FORM_CODE_NAME) + "는 " + this.term + "일까지 사용할 수 있습니다. 잔여일 이내로 신청하여 주시기 바랍니다.";
		return message;
	}

	@Override
	public void resetUI() {
	}

	@Override
	public String validationUI() {
		return setDraftPrimitive();
	}
	
	private String setDraftPrimitive() {
		if (draftPrim.getUntilDate().before(draftPrim.getFromDate()) == true) {
			return "휴가 시작일을 휴가 종료일 이전으로 선택하시기 바랍니다.";
		} 
		// 2015-03-05 Join 수정 시작 - 휴가일수 산정 시 제외일을 염두하지 않아 기존 구문 주석처리 후 제외일까지 산정하여 휴가일수를 추출하는 구문 추가 
		/*if (DateUtil.diffDate(draftPrim.getFromDate(), draftPrim.getUntilDate()) >= term) {
			return termOverMessage();
		}*/
		int realVactionTerm = 0;		// 실제 휴가일수 = 총 휴가일수 - 제외일수
		realVactionTerm = Integer.parseInt(tvExceptDate1.getText().toString()) - Integer.parseInt(tvExceptDate2.getText().toString());
//		Log.d(TAG, "realVactionTerm ======= " + realVactionTerm);
		if( realVactionTerm > this.term) {
			return termOverMessage();
		}
		// 2015-03-05 Join 수정 끝
		// 연락처
		EditText telNum = (EditText) findViewById(R.id.apply_vacation_edittext_telnum);
		if (telNum.getText().toString().length() == 0)
			return "연락처 항목이 입력되지 않았습니다.";
		draftPrim.setTelNum(telNum.getText().toString());
		
		// 적요
		EditText descript = (EditText) findViewById(R.id.apply_vacation_edittext_descript);
		if (descript.getText().toString().length() == 0)
			return "적요 항목이 입력되지 않았습니다.";
		draftPrim.setDescript(descript.getText().toString());
		
		// 나머지는 컨트럴에서 변경이 일어날 시 즉시 데이터가 들어간다.
		SKTUtil.log("TEST", "DraftPrimitive: " + draftPrim.toParameterString());
		
		return null;
	}
}