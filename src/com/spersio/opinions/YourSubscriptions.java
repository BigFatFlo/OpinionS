package com.spersio.opinions;

import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

public class YourSubscriptions extends ActionBarActivity{
	
	ListView yourSubscriptionsView = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        overridePendingTransition(0, 0);

        setContentView(R.layout.activity_your_subscriptions);
        
        yourSubscriptionsView = (ListView) findViewById(R.id.yourSubscriptions);
        
	};
	
    @Override
    protected void onStart() {
        super.onStart();
    
       
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {

			final ProgressDialog dlg = new ProgressDialog(YourSubscriptions.this);
		    dlg.setTitle(getResources().getString(R.string.please_wait));
		    dlg.setMessage(getResources().getString(R.string.loading_subscriptions));
	        
		    List<String> list = currentUser.getList("subscribedUsers");
		    
		    if (list!=null) {
		    
			    if (list.isEmpty()) {
			    	
				    yourSubscriptionsView.setVisibility(View.GONE);
				    findViewById(R.id.unsubscribeFromSelection_text).setVisibility(View.GONE);
			    	findViewById(R.id.unsubscribeFromSelection_button).setVisibility(View.GONE);
			    	Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.no_subscriptions), Toast.LENGTH_LONG)
		            .show();
				    dlg.dismiss();
				    	
			    } else {
			
					SubscriptionsCustomArrayAdapter adapter = new SubscriptionsCustomArrayAdapter(YourSubscriptions.this, R.layout.subscriptions_adapter_item, R.id.subscription_username_adapter , list);
				    yourSubscriptionsView.setAdapter(adapter);
				    dlg.dismiss();
			    
			    }
		    
		    } else {
		    	
		    	yourSubscriptionsView.setVisibility(View.GONE);
			    findViewById(R.id.unsubscribeFromSelection_text).setVisibility(View.GONE);
		    	findViewById(R.id.unsubscribeFromSelection_button).setVisibility(View.GONE);
		    	Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.no_subscriptions), Toast.LENGTH_LONG)
	            .show();
			    dlg.dismiss();
		    	
		    }
        
        } else {
        	Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
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
			    dlg.setTitle(getResources().getString(R.string.please_wait));
			    dlg.setMessage(getResources().getString(R.string.unsubscribing));
			    dlg.show();
		        
			    final List<String> toUnsubscribeFrom = SubscriptionsCustomArrayAdapter.toUnsubscribe;
			    
		        if (toUnsubscribeFrom!=null && !toUnsubscribeFrom.isEmpty()) {
		        	
		        	HashMap<String, List<String>> params = new HashMap<String, List<String>>();
					params.put("usernames", toUnsubscribeFrom);
					
						ParseCloud.callFunctionInBackground("subtractSubscriber", params, new FunctionCallback<Object>() {
							   public void done(Object object, ParseException e) {
								   if (e == null) {
									
								   final List<String> listUsers = currentUser.getList("subscribedUsers");
								   final List<String> listChannels = currentUser.getList("channels");
						        	
						        	for (String username : toUnsubscribeFrom) {
						        		listUsers.remove(username);
						        		listChannels.remove("User_" + username);
						        	}
						        	
						        	currentUser.put("subscribedUsers", listUsers);
						        	currentUser.put("channels", listChannels);
						        	
						        	currentUser.saveInBackground();
									
								    SubscriptionsCustomArrayAdapter.toUnsubscribe.clear();
								    
								    Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.unsubscribed_from_selection), Toast.LENGTH_LONG)
						            .show();
								    
								    if (listChannels.isEmpty()) {
								    	
								    yourSubscriptionsView.setVisibility(View.GONE);
								    findViewById(R.id.unsubscribeFromSelection_text).setVisibility(View.GONE);
								    findViewById(R.id.unsubscribeFromSelection_button).setVisibility(View.GONE);
								    Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.no_subscriptions), Toast.LENGTH_LONG)
						            .show();
								    dlg.dismiss();
								    	
								    } else {
								    
						        	SubscriptionsCustomArrayAdapter adapter = new SubscriptionsCustomArrayAdapter(YourSubscriptions.this, R.layout.subscriptions_adapter_item, R.id.subscription_username_adapter , listUsers);
								    yourSubscriptionsView.setAdapter(adapter);
								    dlg.dismiss();
								    }
									
								   } else {
									Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.unable_to_unsubscribe_from_selection), Toast.LENGTH_LONG)
									.show();
									dlg.dismiss();
								   }
							   }
						});
				    
		        }  else {
		        	Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.unable_to_unsubscribe_from_selection), Toast.LENGTH_LONG)
		            .show();
					dlg.dismiss();
		        }
		        
			}
			
		});
    	
    	findViewById(R.id.backToHome_YS_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(YourSubscriptions.this,Home.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			});	
    	
        } else {
        	Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
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
        
        List<String> toUnsubscribeFrom = SubscriptionsCustomArrayAdapter.toUnsubscribe;
	    
        if (toUnsubscribeFrom!=null && !toUnsubscribeFrom.isEmpty()) {
        
        SubscriptionsCustomArrayAdapter.toUnsubscribe.clear();
        
        }
        
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_your_subscriptions_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_home:
            	startActivity(new Intent(YourSubscriptions.this,Home.class));
                return true;
            case R.id.action_ask:
            	startActivity(new Intent(YourSubscriptions.this,Ask.class));
                return true;
            case R.id.action_profile:
            	Intent intentP = new Intent(YourSubscriptions.this,Profile.class);
				intentP.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intentP);
                return true;
            case R.id.action_groups:
            	startActivity(new Intent(YourSubscriptions.this,YourGroups.class));
                return true;
            case R.id.action_savedQuestions:
            	startActivity(new Intent(YourSubscriptions.this,SavedQuestions.class));
                return true;
            case R.id.action_logOut:
            	ParseUser.logOut();
            	Toast.makeText(YourSubscriptions.this, getResources().getString(R.string.logged_out), Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(YourSubscriptions.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}