package com.spersio.opinions;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;

public class OpinionS extends Application {

	  @Override
	  public void onCreate() {
	    super.onCreate();
	    
	    ParseCrashReporting.enable(this);

	    Parse.initialize(this, "yZMElToeMuWoxSzeNUfK0glPK638E6d47IUxRYdF", "mWuT6CbcMloUKWAhYbzZlaojXlhstvTkzOwBYRDN");
	    
	    ParseInstallation.getCurrentInstallation().saveInBackground();
	    
	  }

}
