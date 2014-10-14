package com.spersio.opinion;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class Opinion extends Application {
	

	  @Override
	  public void onCreate() {
	    super.onCreate();

	    Parse.initialize(this, "yZMElToeMuWoxSzeNUfK0glPK638E6d47IUxRYdF", "mWuT6CbcMloUKWAhYbzZlaojXlhstvTkzOwBYRDN");
	    
	    ParseInstallation.getCurrentInstallation().saveInBackground();
	    
	  }

}
