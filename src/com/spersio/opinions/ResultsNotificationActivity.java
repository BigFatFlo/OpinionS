package com.spersio.opinions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ResultsNotificationActivity extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String questionID = extras.getString(CustomPushReceiver.ID);
			int nId = extras.getInt(CustomPushReceiver.nID);
			String tag = extras.getString(CustomPushReceiver.tag);
			int action = extras.getInt(CustomPushReceiver.saveOrSubscribe);
			String askerUsername = extras.getString(CustomPushReceiver.askerUsername);
			Boolean subscribersOnly = extras.getBoolean(CustomPushReceiver.subscribersOnly);
			
			switch (action) { 
				case 1:
					Results.saveResults(questionID,context,nId,tag);
				break;
				case 2:
					if (subscribersOnly) {
						Results.unsubscribe(askerUsername,context);
					} else {
						Results.subscribe(askerUsername,context);
					}
				break;
			}
			
	  }
	}