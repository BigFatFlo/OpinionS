package com.spersio.opinions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AnswerNotificationActivity extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String questionID = extras.getString(CustomPushReceiver.ID);
			int nA = extras.getInt(CustomPushReceiver.aID);
			int nId = extras.getInt(CustomPushReceiver.nID);
			String tag = extras.getString(CustomPushReceiver.tag);
			
			Answer.addAnswer(questionID,nA,context,nId,tag);
			
	  }
	}