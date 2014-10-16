package com.spersio.opinion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class InterestNotificationActivity extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String qID = extras.getString(CustomPushReceiver.ID);
			int notificationId = extras.getInt(CustomPushReceiver.nID);
			String notificationTag = extras.getString(CustomPushReceiver.tag);
			int interest = extras.getInt(CustomPushReceiver.interest);
			
			Interested.addInterest(qID,interest,context,notificationId,notificationTag);
			
	  }
	}