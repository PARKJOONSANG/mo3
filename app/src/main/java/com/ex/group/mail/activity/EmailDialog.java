package com.ex.group.mail.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ex.group.mail.util.EmailClientUtil;

import com.ex.group.folder.R;
/**
 * 서명설정 Dialog
 * 
 * @author sjsun5318
 *
 */
public class EmailDialog extends Dialog implements OnClickListener {

	private Context context;
	private EditText body = null;

	public EmailDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.6f;
		// lpWindow.windowAnimations = android.R.anim.accelerate_interpolator |
		// android.R.anim.fade_in | android.R.anim.fade_out;
		getWindow().setAttributes(lpWindow);

		setContentView(R.layout.mail_insert_dialog);
		EditText e = (EditText) findViewById(R.id.PWD);
		e.setText(EmailClientUtil.getSigne(this.context, "sign"));
		e.setSelection(EmailClientUtil.getSigne(this.context, "sign").length());
		Button b = (Button) findViewById(R.id.Ok);
		b.setOnClickListener(this);

		Button c = (Button) findViewById(R.id.can);
		c.setOnClickListener(this);
	}

	public EmailDialog(Context context, EditText body) {
		this(context);
		this.body = body;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.Ok) {
			EditText e = (EditText) findViewById(R.id.PWD);
			EmailClientUtil.setSigne(this.context, "sign", e.getText().toString());

			if (body != null) {
				body.setText("\n\n" + EmailClientUtil.getSigne(this.context, "sign").toString());
			}
			this.dismiss();

		} else if (v.getId() == R.id.can) {

			this.dismiss();
		}
	}

}
