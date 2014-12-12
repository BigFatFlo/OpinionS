package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class YourSubscriptions extends Activity{
	
	ListView yourSubscriptionsView = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_your_subscriptions);
        
        yourSubscriptionsView = (ListView) findViewById(R.id.yourSubscriptions);
        
	};
	
    @Override
    protected void onStart() {
        super.onStart();
    
       
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {

			final ProgressDialog dlg = new ProgressDialog(YourSubscriptions.this);
		    dlg.setTitle("Please wait.");
		    dlg.setMessage("Loading Subscriptions. Please wait.");
	        
		    List<String> list = currentUser.getList("subscribedChannels");
		    
		    if (list!=null) {
		    
		    if (list.isEmpty()) {
		    	
			    yourSubscriptionsView.setVisibility(View.GONE);
		    	findViewById(R.id.unsubscribeFromSelection_button).setVisibility(View.GONE);
		    	Toast.makeText(YourSubscriptions.this, "You don't have any subscriptions!", Toast.LENGTH_LONG)
	            .show();
			    dlg.dismiss();
			    	
			    } else {
			
			CustomArrayAdapter adapter = new CustomArrayAdapter(YourSubscriptions.this, R.layout.array_adapter_item, R.id.subscription_username_adapter , list);
		    yourSubscriptionsView.setAdapter(adapter);
		    dlg.dismiss();
			    
			    }
		    
		    } else {
		    	
		    	yourSubscriptionsView.setVisibility(View.GONE);
		    	findViewById(R.id.unsubscribeFromSelection_button).setVisibility(View.GONE);
		    	Toast.makeText(YourSubscriptions.this, "You don't have any subscriptions!", Toast.LENGTH_LONG)
	            .show();
			    dlg.dismiss();
		    	
		    }
        
        } else {
        	Toast.makeText(YourSubscriptions.this, "You are not logged in", Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(YourSubscriptions.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }

        
    };
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
    	
        findViewById(R.id.unsubscribeFromSelection_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final ProgressDialog dlg = new ProgressDialog(YourSubscriptions.this);
			    dlg.setTitle("Please wait.");
			    dlg.setMessage("Unsubscribing. Please wait.");
			    dlg.show();
		        
			    final List<String> toUnsubscribeFrom = CustomArrayAdapter.toUnsubscribe;
			    
		        if (toUnsubscribeFrom!=null && !toUnsubscribeFrom.isEmpty()) {
		        	
		        	HashMap<String, List<String>> params = new HashMap<String, List<String>>();
					params.put("usernames", toUnsubscribeFrom);
					
						ParseCloud.callFunctionInBackground("subtractSubscriber", params, new FunctionCallback<Object>() {
							   public void done(Object object, ParseException e) {
								   if (e == null) {
									
								   final List<String> list = currentUser.getList("subscribedChannels");
						        	
						        	for (String username : toUnsubscribeFrom) {
						        		list.remove(username);
						        	}
						        	
						        	currentUser.put("subscribedChannels", list);
						        	
						        	currentUser.saveInBackground();
									
								    CustomArrayAdapter.toUnsubscribe.clear();
								    
								    Toast.makeText(YourSubscriptions.this, "Successfully unsubscribed from selection!", Toast.LENGTH_LONG)
						            .show();
								    
								    if (list.isEmpty()) {
								    	
								    yourSubscriptionsView.setVisibility(View.GONE);
								    findViewById(R.id.unsubscribeFromSelection_button).setVisibility(View.GONE);
								    Toast.makeText(YourSubscriptions.this, "You don't have any subscriptions!", Toast.LENGTH_LONG)
						            .show();
								    dlg.dismiss();
								    	
								    } else {
								    
						        	CustomArrayAdapter adapter = new CustomArrayAdapter(YourSubscriptions.this, R.layout.array_adapter_item, R.id.subscription_username_adapter , list);
								    yourSubscriptionsView.setAdapter(adapter);
								    dlg.dismiss();
								    }
									
								   } else {
									Toast.makeText(YourSubscriptions.this, "Unable to unsubscribe from selection", Toast.LENGTH_LONG)
									.show();
									dlg.dismiss();
								   }
							   }
						});
				    
		        }  else {
		        	Toast.makeText(YourSubscriptions.this, "You didn't check any subscriptions to remove", Toast.LENGTH_LONG)
		            .show();
					dlg.dismiss();
		        }
		        
			}
			
		});
    	
    	findViewById(R.id.backToMenu_SQ_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(YourSubscriptions.this,Menu.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			});	
    	
        } else {
        	Toast.makeText(YourSubscriptions.this, "You are not logged in", Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(YourSubscriptions.this, Login.class);
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
        
        List<String> toUnsubscribeFrom = CustomArrayAdapter.toUnsubscribe;
	    
        if (toUnsubscribeFrom!=null && !toUnsubscribeFrom.isEmpty()) {
        
        CustomArrayAdapter.toUnsubscribe.clear();
        
        }
        
    };
    
}