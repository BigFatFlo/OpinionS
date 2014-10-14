package com.spersio.opinion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseAnalytics;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class Menu extends Activity implements LocationListener{
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	String lat;
	String provider;
	protected String latitude,longitude;
	protected boolean gps_enabled,network_enabled;
	
	TextView username= null;
	
	TextView countryText= null;
	
	ToggleButton useLocationButton = null;
	
	ToggleButton internationalButton = null;
	
	ListView savedQuestionsView = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ParseAnalytics.trackAppOpened(getIntent());

        setContentView(R.layout.activity_menu);
        
        username = (TextView) findViewById(R.id.username_profile);
        
        countryText = (TextView) findViewById(R.id.country_view);
        
        useLocationButton = (ToggleButton) findViewById(R.id.location_button);
		
		internationalButton = (ToggleButton) findViewById(R.id.international_button);
        
        savedQuestionsView = (ListView) findViewById(R.id.savedQuestions);
        
	};
	
    @Override
    protected void onStart() {
        super.onStart();
    };
    
    @Override
    protected void onResume() {
        super.onResume();
        

        final ProgressDialog dlg = new ProgressDialog(Menu.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Loading Profile. Please wait.");
        dlg.show();
        
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
        
        final Boolean useLocation = currentUser.getBoolean("useLocation");
        
        if (useLocation) {
        	useLocationButton.setChecked(true);
        	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);
        } else {
        	useLocationButton.setChecked(false);
        }	
        
        useLocationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                	currentUser.put("useLocation",true);
					//currentUser.saveInBackground();
                	/*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            		installation.put("useLocation",true);*/
                	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, Menu.this);
                } else {
                	currentUser.put("useLocation",false);
                	//currentUser.saveInBackground();
                	/*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            		installation.put("useLocation",false);*/
                	locationManager.removeUpdates(Menu.this);
                }
            }
        });
		
		internationalButton.setChecked(currentUser.getBoolean("international"));
		
		internationalButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentUser.put("international",isChecked);
            }
        });
        
        username.setText(currentUser.getUsername());

        ParseInstallation.getCurrentInstallation().put("user", currentUser);
        
        if (currentUser.getString("country").length() > 1){
            countryText.setText(currentUser.getString("country"));
            }
            
           /* if (currentUser.getString("channel")!=null){
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        		installation.put("channel",currentUser.getString("channel"));
        		//installation.saveInBackground();
            }*/
        
        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (currentUser.getString("country").length() > 1) {
            	Intent intent = new Intent(Menu.this,Submit.class);
            	startActivity(intent);
            	} else {
            		Toast.makeText(Menu.this, "You need to choose your country before submitting a question!", Toast.LENGTH_LONG)
                    .show();
            	}
            }
        });
        
        
        findViewById(R.id.pref_change_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(Menu.this,CountryChange.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intent);
            }
        });
        
        dlg.dismiss();
        
        findViewById(R.id.savedQuestions_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(Menu.this,SavedQuestions.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intent);
            }
        });
        
        findViewById(R.id.log_out_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	ParseUser.logOut();
            	Toast.makeText(Menu.this, "You are now logged out", Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(Menu.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
            }
        });
        
        } else {
        	Toast.makeText(Menu.this, "You are not logged in", Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(Menu.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }
        
};
    

    @Override
    protected void onPause() {
        super.onPause();
    };
    
    @Override
    protected void onStop() {
        super.onStop();
        if (ParseUser.getCurrentUser() != null) {
        	ParseUser.getCurrentUser().saveInBackground();
        }
        ParseInstallation.getCurrentInstallation().saveInBackground();
        if (locationManager != null){
        locationManager.removeUpdates(Menu.this);
        }
    };
	
	
@Override
public void onLocationChanged(Location location) {
Log.d("GPS","Change in GPS");
Log.d("GPS",location.toString());
ParseUser currentUser= ParseUser.getCurrentUser();
if (currentUser!=null){
ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
ParseInstallation installation = ParseInstallation.getCurrentInstallation();
installation.put("location",point);
installation.put("locationKnown", true);
//Log.d("GPS","Saving Installation");
//installation.saveInBackground();
currentUser.put("location", point);
currentUser.put("locationKnown", true);
//Log.d("GPS","Saving User");
//currentUser.saveInBackground();
} else {
	Toast.makeText(Menu.this, "Unable to update your location", Toast.LENGTH_LONG)
    .show();
}
};
 
@Override
public void onProviderDisabled(String provider) {
Log.d("GPS","disable");
};
 
@Override
public void onProviderEnabled(String provider) {
Log.d("GPS","enable");
};
 
@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
Log.d("GPS","Status: " + status);
Log.d("GPS","Provider: " + provider);
};
    
    
}
