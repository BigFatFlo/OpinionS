package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
//import com.facebook.AppEventsLogger;

public class Menu extends Activity implements LocationListener{
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	String lat;
	String provider;
	protected String latitude,longitude;
	protected boolean gps_enabled,network_enabled;
	
	TextView username= null;
	
	TextView nbrSubscribers= null;
	
	EditText subscribeToUsername= null;
	
	ImageButton subscribeToUser= null;
	
	TextView countryText= null;
	
	ToggleButton useLocationButton = null;
	
	ToggleButton internationalButton = null;
	
	ListView savedQuestionsView = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        setContentView(R.layout.activity_menu);
        
        username = (TextView) findViewById(R.id.username_profile);
        
        nbrSubscribers = (TextView) findViewById(R.id.nbrSubscribers);
        
        subscribeToUsername = (EditText) findViewById(R.id.subscribeToUsername);
        
        subscribeToUser = (ImageButton) findViewById(R.id.subscribeToUser_button);
        
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
        
        //AppEventsLogger.activateApp(this);

        final ProgressDialog dlg = new ProgressDialog(Menu.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Loading Profile. Please wait.");
        dlg.show();
        
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
        
        	findViewById(R.id.nbrSubscribersRefresh_button).requestFocus();	
        	
        if(getIntent().getBooleanExtra("fromLogin", false)) {
        	ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        	currentInstallation.put("user", currentUser);
            
        	List<String> listUser = currentUser.getList("subscribedChannels");
        	
        	if (listUser == null) {
        		ArrayList<String> emptyList = new ArrayList<String>();
        		currentInstallation.put("channels", emptyList);
        	} else {
        		currentInstallation.put("channels",listUser);
        	}
        	
			currentInstallation.saveInBackground();
			
        }
        	
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, Menu.this);
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
        
        nbrSubscribers.setText("You have " + currentUser.getInt("nbrSubscribers") + " subscribers");
        
        findViewById(R.id.nbrSubscribersRefresh_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	final ProgressDialog dlg = new ProgressDialog(Menu.this);
				dlg.setTitle("Please wait.");
				dlg.setMessage("Refreshing data. Please wait.");
				dlg.show();
            	currentUser.fetchInBackground(new GetCallback<ParseObject>() {
            		public void done(ParseObject user, ParseException e) {
      			    if (e == null) {
		            	dlg.dismiss();
      			    	nbrSubscribers.setText("You have " + user.getInt("nbrSubscribers") + " subscribers");
      			    } else {
	            		Toast.makeText(Menu.this, "Unable to refresh your number of subscribers...", Toast.LENGTH_LONG)
						.show();
	            		dlg.dismiss();
      			  }
            	}
            });
            }
        });
        
        subscribeToUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	final ProgressDialog dlg = new ProgressDialog(Menu.this);
				dlg.setTitle("Please wait.");
				dlg.setMessage("Checking username. Please wait.");
				dlg.show();
				
				final HashMap<String, Object> params = new HashMap<String, Object>();
				final String asker_username = subscribeToUsername.getText().toString();
				params.put("username", asker_username);
				
				ParseCloud.callFunctionInBackground("userExists", params, new FunctionCallback<Boolean>() {
					   public void done(Boolean exists, ParseException e) {
						   if (e == null) {
							
							   if (exists) {
								   
								   List<String> subscribedChannels = currentUser.getList("subscribedChannels");
									
									if (subscribedChannels != null) {
									
									if (subscribedChannels.contains(asker_username)) {
										dlg.dismiss();
										Toast.makeText(Menu.this, "Already a subscriber! To unsubscribe, go to Your Subscriptions.", Toast.LENGTH_LONG)
										.show();
									} else {
											
										ParseCloud.callFunctionInBackground("addSubscriber", params, new FunctionCallback<Object>() {
											   public void done(Object object, ParseException e) {

												   dlg.dismiss();
													
												   if (e == null) {
													   
												    currentUser.addUnique("subscribedChannels", asker_username);
												    
													currentUser.saveInBackground();
													
													Toast.makeText(Menu.this, "Successfully subscribed to " + asker_username + "'s questions!", Toast.LENGTH_SHORT)
													.show();
												   } else {
													Toast.makeText(Menu.this, "Unable to subscribe to " + asker_username + "'s questions, please try again.", Toast.LENGTH_LONG)
													.show();
												   }
											   }
										});
									}
									
									} else {
											
										ParseCloud.callFunctionInBackground("addSubscriber", params, new FunctionCallback<Object>() {
											   public void done(Object object, ParseException e) {
												   
												   dlg.dismiss();
												   
												   if (e == null) {
													
												   currentUser.addUnique("subscribedChannels", asker_username);
												    
												   currentUser.saveInBackground();
													
													Toast.makeText(Menu.this, "Successfully subscribed to " + asker_username + "'s questions!", Toast.LENGTH_SHORT)
													.show();
												   } else {
													Toast.makeText(Menu.this, "Unable to subscribe to " + asker_username + "'s questions, please try again.", Toast.LENGTH_LONG)
													.show();
												   }
											   }
										});

									}	
								   
							   } else {
								   dlg.dismiss();
								   Toast.makeText(Menu.this, "User " + asker_username + " doesn't seem to exist", Toast.LENGTH_LONG)
									.show();
							   }
							   
						   } else {
							dlg.dismiss();
							Toast.makeText(Menu.this, "Unable to subscribe to " + asker_username + "'s questions, please try again.", Toast.LENGTH_LONG)
							.show();
						   }
					   }
				});
            }
        });

        if (currentUser.getString("country").length() > 1){
            countryText.setText(currentUser.getString("country"));
            findViewById(R.id.no_country_view).setVisibility(View.GONE);
            }
        
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
        
        
        findViewById(R.id.country_change_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(Menu.this,CountryChange.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intent);
            }
        });
        
        dlg.dismiss();
        
        findViewById(R.id.yourSubscriptions_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(Menu.this,YourSubscriptions.class);
            	startActivity(intent);
            }
        });
        
        findViewById(R.id.savedQuestions_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(Menu.this,SavedQuestions.class);
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
        
        //AppEventsLogger.deactivateApp(this);
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
