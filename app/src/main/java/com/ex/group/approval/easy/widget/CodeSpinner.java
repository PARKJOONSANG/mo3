package com.ex.group.approval.easy.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import com.ex.group.folder.R;

public class CodeSpinner extends android.support.v7.widget.AppCompatButton
{
	int normalBitmapId;
	int clickedBitmapId;
	
	private String title;
	private String values[];
	private String codes[];
	private int default_index = 0;
	private int select_index = 0;
	
	private DialogInterface.OnClickListener onClickListener = null;
	
	public CodeSpinner(Context context)
	{
		super(context);
		init(context, null);
	}
		
	public CodeSpinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context, attrs);
	}
	
	public void init(Context context, AttributeSet attrs)
	{
		if (attrs != null) {
			TypedArray ar = context.obtainStyledAttributes(attrs, R.styleable.CodeSpinner);
			CharSequence[] seqs;
			seqs = ar.getTextArray(R.styleable.CodeSpinner_values);
			if (seqs != null) {
				this.values = new String[seqs.length];
				for (int i=0; i<seqs.length; i++) {
					this.values[i] = seqs[i].toString();
				}
			}
			seqs = ar.getTextArray(R.styleable.CodeSpinner_codes);
			if (seqs != null) {
				this.codes = new String[seqs.length];
				for (int i=0; i<seqs.length; i++) {
					this.codes[i] = seqs[i].toString();
				}
			}
			default_index = ar.getInteger(R.styleable.CodeSpinner_defaultIndex, 0);
			select_index = default_index;
			ar.recycle();
			this.setText(values[select_index]);
			this.setGravity(Gravity.CENTER);
		}
	}
	
	public void setValues(String[] values, String[] codes) {
		this.values = values;
		this.codes = codes;
	}
	
	public void setBitmapId(int normalId, int clickedId) {
		normalBitmapId = normalId;
		clickedBitmapId = clickedId;
		
		setBackgroundResource(normalBitmapId);
	}
	
	public void setDialogShape(String title, String values[])
	{
		this.title = title;
		this.values = values;
		
		if(values != null)
		{
			this.setText(values[0]);
		}
	}
	
	public void setDialogShape(String title, int itemId)
	{		
		this.title = title;
		this.values = getResources().getStringArray(itemId);
		
		if(values != null)
		{
			this.setText(values[0]);
		}
	}
	
	public void setOnClickListener(DialogInterface.OnClickListener listener) {
		this.onClickListener = listener;
	}
		
	public boolean onTouchEvent(MotionEvent event)
	{
		super.onTouchEvent(event);
		
		int action = event.getAction();		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

		switch (action) {
			case MotionEvent.ACTION_UP:
				//setBackgroundResource(normalBitmapId);
				//new AlertDialog.Builder(getContext())				
				
				break;
	
			case MotionEvent.ACTION_DOWN:
				//setBackgroundResource(clickedBitmapId);
				
				if(values != null && values.length > 0) {
					dialog.setTitle(title);
					dialog.setIcon(null);
					dialog.setItems(values, dialogOnClickListener);
					dialog.show();
				}
				
				break;

		}
		return true;		
	}
	
	public String getValue() {
		if (select_index >= this.values.length) {
			select_index = this.default_index;
		}
		
		if (this.codes == null)
			return this.values[select_index];
		
		return this.codes[select_index];
	}
	
	public void setDefault() {
		setIndex(default_index);
	}
	
	public void setCode(String code) {
		for (int i=0; i<this.codes.length; i++) {
			if (codes[i].equals(code) == true) {
				setIndex(i);
				break;
			}
		}
	}
	
	public String getCode(int index) {
		return this.codes[index];
	}
	
	public void setIndex(int index) {
		select_index = index;
		this.setText(values[select_index]);
	}
	
	DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener()
	{		
		public void onClick(DialogInterface dialog, int which)
		{
			CodeSpinner.this.setText(values[which]);
			select_index = which;
			if (onClickListener != null) {
				onClickListener.onClick(dialog, select_index);
			}
		}
	};
}
