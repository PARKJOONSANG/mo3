package com.ex.group.board.activity;

import android.content.Intent;
import android.os.Bundle;

import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;

/**
 * 
 *  <pre>
 *	com.ex.group.board.activity
 *	BoardMainActivity.java
 *	</pre>
 *
 *	@Author : 박정호
 * 	@E-MAIL : yee1074@innoace.com
 *	@Date	: 2011. 11. 22. 
 *
 *	TODO
 */
public class BoardMainActivity extends BoardActivity {

	
	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected XMLData onAction(String primitive, String... args)
			throws SKTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e)
			throws SKTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int onActionPre(String primitive) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onCreateX(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, BoardListActivity.class));
	}

}
