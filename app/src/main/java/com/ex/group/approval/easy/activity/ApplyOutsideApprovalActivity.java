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
import android.widget.Toast;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.dialog.WheelDatePickerDialogHelper;
import com.ex.group.approval.easy.dialog.WheelTimePickerDialogHelper;
import com.ex.group.approval.easy.dialog.ifaces.PEDialogInterface;
import com.ex.group.approval.easy.primitive.DraftFormPrimitive;
import com.ex.group.approval.easy.primitive.DraftPrimitive;
import com.ex.group.approval.easy.util.ActivityLauncher;
import com.ex.group.approval.easy.widget.CodeSpinner;
import com.skt.pe.common.activity.ifaces.CommonUI;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.primitive.util.PrimitiveList;
import com.skt.pe.common.widget.MemberEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ApplyOutsideApprovalActivity extends ApprovalCommonActivity implements OnClickListener, CommonUI {
	private final String TAG = "ApplyOutsideApprovalActivity";
	private Button selectDate = null;
	private Button selectStartTime = null;
	private Button selectEndTime = null;
	private DraftFormPrimitive dformPrim = null;
	private DraftPrimitive draftPrim = null;
	
	private final static String activityTitle = "외출신청서";
	
	private CodeSpinner csFormCode;
	
	// 2015-06-26 Join 추가 - 필요 시 아래 구문 추가
//	private String[] childName;
//	private String[] childJmdrbeonho;
	// 2015-06-26 Join 추가 끝
	
	@Override
	protected int assignLayout() {
		return R.layout.easy_apply_outside;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
    public void onCreateX(Bundle savedInstanceState) {
		setSubTitle(activityTitle);
		
		Intent intent = getIntent();
		dformPrim = (DraftFormPrimitive) intent.getSerializableExtra(ApprovalConstant.IntentArg.PRIMITIVE);
		Log.d(TAG, "onCreateX() - dformPrim : "+dformPrim);
		Map<String, Object> o = (Map<String, Object>) getLastNonConfigurationInstance();
		if (o != null) {
			draftPrim = (DraftPrimitive) o.get("primitive");
		} else {
			draftPrim = new DraftPrimitive();
		}
		
		draftPrim.setDraftKind(DraftPrimitive.DraftKind.OUTSIDE);
		initUI();
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 * 화면 로테이션 시 List 데이터가 사라지는 현상을 방지...
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("primitive", draftPrim);

		return map;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	protected void onReceive(Primitive primitive, SKTException e) {
		super.onReceive(primitive, e);
	}
	
	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent intent) {
		super.onActivityResultX(requestCode, resultCode, intent);
		/*
		 * 동행인 구성원 검색 선택 시 결과 처리...
		 */
		if (requestCode == ApprovalConstant.RequestCode.SELECT_COOPERATOR) {
			if (intent != null) {
				String[] names = null;
				String[] empIds = null;
	
				names = (String[]) intent.getStringArrayExtra("names");
				empIds = (String[]) intent.getStringArrayExtra("empids");
				
				for (int i=0; i<names.length; i++) {
					SKTUtil.log("TEST", "(" + i + ")" + names[i] + ": " + empIds[i]);
					Log.d(TAG, "(" + i + ") " + names[i] + " : " + empIds[i]);
				}
				
				MemberEditText tvCooperator = (MemberEditText) findViewById(R.id.apply_outside_textview_cooperator);
				for (int i=0; i<names.length; i++) {
					if (tvCooperator.isExistValue(empIds[i]) == false)
						tvCooperator.addText(names[i], empIds[i]);
				}
				
				tvCooperator.requestFocus();
				tvCooperator.setText(tvCooperator.toString());
//				tvCooperator.setText(tvCooperator.toShortString());
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 주소록 버튼 = 동행인 추가 
		case R.id.apply_outside_button_membersearch:
			// 0-공무, 1-사무, 2-교육, 3-육아, 4-임신
			if("3".equals(csFormCode.getValue()) || "육아".equals(csFormCode.getValue())) {
				Toast.makeText(ApplyOutsideApprovalActivity.this, "육아외출은 본인 외출만 가능하며 동행인 등록은 하실 수 없습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			super.selectMemberSearch(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityLauncher.launchMemberSearch(ApplyOutsideApprovalActivity.this, getSelectMemberSearchType(), ActivityLauncher.MemberSelectType.MULTI, ApprovalConstant.RequestCode.SELECT_COOPERATOR);
				}
			}, "cooperator");
			break;
		case R.id.apply_outside_button_target_date:
			super.selectDate(new PEDialogInterface.OnClickListener() {

				@Override
				public void onClick(Object date, String formattedDate) {
					selectDate.setText(formattedDate);
					draftPrim.setTargetDate((Date)date);
				}
			}, draftPrim.getTargetDate());
			break;
		case R.id.apply_outside_button_starttime:
			super.selectTime( new PEDialogInterface.OnClickListener() {
				@Override
				public void onClick(Object date, String formattedDate) {
					/*if (((Date) date).compareTo(draftPrim.getUntilTime()) >= 0) {
						alert("외출 시작시간을 종료시간 이전으로 선택하시기 바랍니다.");
					} else {
						selectStartTime.setText(formattedDate);
						selectEndTime.setText(formattedDate);
						// 2015-03-09 Join 추가 시작 - 외출 시작시간을 선택하면 외출 종료시간도 같이 세팅되도록 요청하여 추가
						draftPrim.setFromTime((Date)date);
						draftPrim.setUntilTime((Date)date);
						// 2015-03-09 Join 추가 끝
					}*/
					selectStartTime.setText(formattedDate);
					selectEndTime.setText(formattedDate);
					// 2015-03-09 Join 추가 시작 - 외출 시작시간을 선택하면 외출 종료시간도 같이 세팅되도록 요청하여 추가
					draftPrim.setFromTime((Date)date);
					draftPrim.setUntilTime((Date)date);
					// 2015-03-09 Join 추가 끝
				}
			}, draftPrim.getFromTime());
			break;
		case R.id.apply_outside_button_endtime:
			super.selectTime( new PEDialogInterface.OnClickListener() {
				@Override
				public void onClick(Object date, String formattedDate) {
					if (draftPrim.getFromTime().compareTo((Date)date) >= 0) {
						alert("외출 종료시간을 시작시간 이후로 선택하시기 바랍니다.");
					} else {
						selectEndTime.setText(formattedDate);
						draftPrim.setUntilTime((Date)date);
					}
				}
			}, draftPrim.getUntilTime());
			break;
		case R.id.apply_outside_button_ok:
			
			// 2015-06-26 Join 추가 - 필요 시 아래 구문 추가
			// 0-공무, 1-사무, 2-교육, 3-육아, 4-임신
//			if("3".equals(csFormCode.getValue()) || "육아".equals(csFormCode.getValue())) {
//				Toast.makeText(ApplyOutsideApprovalActivity.this, "자녀 주민번호 선택 Dialog!!", Toast.LENGTH_SHORT).show();
//			}
			// 2015-06-26 Join 추가 끝
			
			String message = validationUI();
			if (message == null)
				ActivityLauncher.launchApprovalLine(ApplyOutsideApprovalActivity.this, draftPrim, ApprovalConstant.EasyApprovalType.OUTSIDE, activityTitle);
			else
				alert(message);
			break;
		case R.id.apply_outside_button_cancel:
			finish();
			break;
		case R.id.apply_outside_button_use_owncar_yes:
		case R.id.apply_outside_textview_use_owncar_yes:
			selectUserOwnCarButton(true);
			break;
		case R.id.apply_outside_button_use_owncar_no:
		case R.id.apply_outside_textview_use_owncar_no:
			selectUserOwnCarButton(false);
			break;
		} // switch
	}
	
	@Override
	public void initUI() {
		// 구성원 검색 
		Button btn1 = (Button) findViewById(R.id.apply_outside_button_membersearch);
		btn1.setOnClickListener(this);
		
		// 외출일 선택
		initDateUI();
		
		Button btnOk = (Button) findViewById(R.id.apply_outside_button_ok);
		btnOk.setOnClickListener(this);
		Button btnCancel = (Button) findViewById(R.id.apply_outside_button_cancel);
		btnCancel.setOnClickListener(this);

		Button btnYes = (Button) findViewById(R.id.apply_outside_button_use_owncar_yes);
		TextView tvYes = (TextView) findViewById(R.id.apply_outside_textview_use_owncar_yes);
		btnYes.setOnClickListener(this);
		tvYes.setOnClickListener(this);
		Button btnNo = (Button) findViewById(R.id.apply_outside_button_use_owncar_no);
		TextView tvNo = (TextView) findViewById(R.id.apply_outside_textview_use_owncar_no);
		btnNo.setOnClickListener(this);
		tvNo.setOnClickListener(this);
	
		TextView myName = (TextView) findViewById(R.id.apply_outside_textview_myname);
		myName.setText(dformPrim.getFormInfo().getName());
		
		TextView outDate = (TextView) findViewById(R.id.apply_outside_textview_outdate);
		outDate.setText(dformPrim.getFormInfo().getOutDate());
		
		TextView carNumber = (TextView) findViewById(R.id.apply_outside_textview_car_number);
		carNumber.setText(dformPrim.getFormInfo().getCarNumber());

		/*
		 * formCode항목이 육아아 없는 사용자면 '육아' 항목이 없는 항목을 Spinner에 삽입
		 */
		csFormCode = (CodeSpinner) findViewById(R.id.apply_outside_spinner_formcode);
		String[] formCodeValues;
		String[] formCodeCodes;
		if (dformPrim.getFormInfo().getHaveChild() == true) {
			formCodeValues = getResources().getStringArray(R.array.easyaproval_apply_outside_spinner_formcode_name);
			formCodeCodes = getResources().getStringArray(R.array.easyaproval_apply_outside_spinner_formcode_code);
			// 2015-06-26 Join 추가 - 필요 시 아래 구문 추가
//			childName = dformPrim.getFormInfo().getChildName().split("@");
//			childJmdrbeonho = dformPrim.getFormInfo().getChildJmdrbeonho().split("@");
			// 2015-06-26 Join 추가 끝
		} else {
			formCodeValues = getResources().getStringArray(R.array.easyaproval_apply_outside_spinner_formcode_name_without_child);
			formCodeCodes = getResources().getStringArray(R.array.easyaproval_apply_outside_spinner_formcode_code_without_child);
		}
		csFormCode.setValues(formCodeValues, formCodeCodes);
		
		MemberEditText cooperator = (MemberEditText) findViewById(R.id.apply_outside_textview_cooperator);
		selectUserOwnCarButton(true);
	}

	private void initDateUI() {
		Date currentDate = new Date(System.currentTimeMillis());
		selectDate = (Button) findViewById(R.id.apply_outside_button_target_date);
		selectDate.setOnClickListener(this);
		
		selectDate.setText(WheelDatePickerDialogHelper.generateDateFormat(currentDate));
		draftPrim.setTargetDate(currentDate);

		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(currentDate);
		Date fromTime = (new GregorianCalendar(0, 0, 0, currentCalendar.get(Calendar.HOUR_OF_DAY) + 1, 0, 0)).getTime();
		selectStartTime = (Button) findViewById(R.id.apply_outside_button_starttime);
		selectStartTime.setOnClickListener(this);
		
		selectStartTime.setText(WheelTimePickerDialogHelper.generateDateFormat(fromTime));
		draftPrim.setFromTime(fromTime);
		
		int untilHour = currentCalendar.get(Calendar.HOUR_OF_DAY) + 4;
		int untilMinute = 0;
		if (untilHour > 23) {
			untilHour = 23;
			untilMinute = 30;
		}
		
		Date untilTime = (new GregorianCalendar(0, 0, 0, untilHour > 23 ? 23 : untilHour, untilMinute, 0)).getTime();
		selectEndTime = (Button) findViewById(R.id.apply_outside_button_endtime);
		selectEndTime.setOnClickListener(this);
		
		selectEndTime.setText(WheelTimePickerDialogHelper.generateDateFormat(untilTime));
		draftPrim.setUntilTime(untilTime);
	}
	
	private void selectUserOwnCarButton(boolean isYes) {
		Button yesButton = (Button) findViewById(R.id.apply_outside_button_use_owncar_yes);
		Button noButton = (Button) findViewById(R.id.apply_outside_button_use_owncar_no);
		
		/*
		 * 차넘버가 없으면 무조껀 차량이용여부를 false로 남긴다.
		 */
		if (isYes == true && dformPrim.getFormInfo().getCarNumber().length() > 0) {
			yesButton.setBackgroundResource(R.drawable.easy_radio_on);
			noButton.setBackgroundResource(R.drawable.easy_radio_off);
			draftPrim.setIsOwnCar(true);
		} else {
			yesButton.setBackgroundResource(R.drawable.easy_radio_off);
			noButton.setBackgroundResource(R.drawable.easy_radio_on);
			draftPrim.setIsOwnCar(false);
		}
	}
	
	@Override
	public void resetUI() {
	}

	@Override
	public String validationUI() {
		return setDraftPrimitive();
	}
	
	private String setDraftPrimitive() {
		// 외출유형
		CodeSpinner formCode = (CodeSpinner) findViewById(R.id.apply_outside_spinner_formcode);
		draftPrim.setFormCode(formCode.getValue());
		draftPrim.setTag(DraftPrimitive.Tags.FORM_CODE_NAME, formCode.getText().toString());
		
		// 용무
		EditText descript = (EditText) findViewById(R.id.apply_outside_edittext_descript);
		if (descript.getText().toString().length() == 0)
			return "용무가 입력되지 않았습니다.";
		draftPrim.setDescript(descript.getText().toString());
		
		// 행선지
		EditText location = (EditText) findViewById(R.id.apply_outside_edittext_location);
		if (location.getText().toString().length() == 0)
			return "행선지가 입력되지 않았습니다.";
		draftPrim.setLocation(location.getText().toString());
		
		// 동행인
		MemberEditText tvCooperator = (MemberEditText) findViewById(R.id.apply_outside_textview_cooperator);
		
		// 0-공무, 1-사무, 2-교육, 3-육아, 4-임신
		if(("3".equals(csFormCode.getValue()) || "육아".equals(csFormCode.getValue())) && !"".equals(tvCooperator.getText().toString())) {
			tvCooperator.setText("");
			tvCooperator.clearData();
			draftPrim.setCooperator("");
			return "육아외출은 본인 외출만 가능하며 동행인 등록은 하실 수 없습니다.";
		}
		
		PrimitiveList cooperatorList = new PrimitiveList();
		for (String cooperator : tvCooperator.getValues()) {
			cooperatorList.add(cooperator);
		}
		
		Log.d(TAG, "cooperatorList.toString() ============ " + cooperatorList.toString());
		
		draftPrim.setCooperator(cooperatorList.toString());
		
		if (isValidTargetDate() == false) 
			return "외출일과 외출시간을 현재시간 이후로 선택하시길 바랍니다.";
		
		// 나머지는 컨트럴에서 변경이 일어날 시 즉시 데이터가 들어간다.
		SKTUtil.log("TEST", "DraftPrimitive: " + draftPrim.toParameterString());
		
		return null;
	}

	private boolean isValidTargetDate() {
		Calendar targetDate = Calendar.getInstance();
		Calendar fromTime = Calendar.getInstance();
		
		targetDate.setTime(draftPrim.getTargetDate());
		fromTime.setTime(draftPrim.getFromTime());
		
//		SKTUtil.log("TEST", "targetDate: " + DateUtil.format(targetDate.getTime()));
//		SKTUtil.log("TEST", "fromTime: " + DateUtil.format(fromTime.getTime()));
		
		targetDate.set(Calendar.HOUR_OF_DAY, fromTime.get(Calendar.HOUR_OF_DAY));
		targetDate.set(Calendar.MINUTE, fromTime.get(Calendar.MINUTE));
		
		Date currentDate = new Date(System.currentTimeMillis());
//		SKTUtil.log("TEST", "Summed targetDate: " + DateUtil.format(targetDate.getTime()));
//		SKTUtil.log("TEST", "Current Date: " + DateUtil.format(currentDate));
		
		return targetDate.getTime().after(currentDate);
	}
}