package com.spersio.opinions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeleteNotificationActivity extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent) {
					
		CustomPushReceiver.countDownTimerA.cancel();
			
	  }
	}