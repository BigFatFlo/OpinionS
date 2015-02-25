package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
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
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Home extends ActionBarActivity {
	
	TextView username= null;
	
	TextView nbrSubscribers= null;
	
	TextView profileStatus = null;
	
	EditText subscribeToUsername= null;
	
	EditText joinGroupname= null;
	
	ImageButton profileButton = null;
	
	ImageButton subscribeToUser= null;

	ImageButton joinGroup= null;
	
	ImageButton createGroup= null;
	
	ToggleButton internationalButton = null;
	
	ListView savedQuestionsView = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        overridePendingTransition(0, 0);
        
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        setContentView(R.layout.activity_home);
        
        username = (TextView) findViewById(R.id.username_profile);
        
        nbrSubscribers = (TextView) findViewById(R.id.nbrSubscribers);
        
        profileStatus = (TextView) findViewById(R.id.profile_status);
        
        subscribeToUsername = (EditText) findViewById(R.id.subscribeToUsername);
        
        joinGroupname = (EditText) findViewById(R.id.joinGroupname);
        
        profileButton = (ImageButton) findViewById(R.id.profile_button);
        
        subscribeToUser = (ImageButton) findViewById(R.id.subscribeToUser_button);
        
        joinGroup = (ImageButton) findViewById(R.id.joinGroup_button);
        
        createGroup = (ImageButton) findViewById(R.id.createGroup_button);
		
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
        
        final ActionBar actionBar = getSupportActionBar();
		
		final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        
        //AppEventsLogger.activateApp(this);
        
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
        	
        if (currentUser.getInt("characteristicsNumber") != -1) {
        	profileStatus.setText(getResources().getString(R.string.profile_is_complete));
        	profileStatus.setTextColor(getResources().getColor(R.color.darkBlue));
        	profileButton.setVisibility(View.GONE);
        }
        
    	findViewById(R.id.nbrSubscribersRefresh_button).requestFocus();	
    	
    	subscribeToUsername.setOnFocusChangeListener(new OnFocusChangeListener() {          

	        public void onFocusChange(View v, boolean hasFocus) {
	            if (hasFocus) {
	            	actionBar.hide();
	            } else {
	                imm.hideSoftInputFromWindow(subscribeToUsername.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	            	actionBar.show();
	            }
	        }
	    });
    	
    	joinGroupname.setOnFocusChangeListener(new OnFocusChangeListener() {          

	        public void onFocusChange(View v, boolean hasFocus) {
	            if (hasFocus) {
	            	actionBar.hide();
	            } else {
	                imm.hideSoftInputFromWindow(subscribeToUsername.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	            	actionBar.show();
	            }
	        }
	    });
        	
        if(getIntent().getBooleanExtra("fromLogin", false)) {
        	ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        	currentInstallation.put("user", currentUser);
            
        	List<String> listChannels = currentUser.getList("channels");
        	
        	if (listChannels == null) {
        		
        		ArrayList<String> emptyList = new ArrayList<String>();
        		currentInstallation.put("channels", emptyList);
        		
        	} else {
        		currentInstallation.put("channels",listChannels);
        	}
       
			currentInstallation.saveInBackground();
			
        }
        
        username.setText(currentUser.getUsername());
        
        nbrSubscribers.setText(getResources().getString(R.string.you_have) + currentUser.getInt("nbrSubscribers") + getResources().getString(R.string.subscribers));
        
        findViewById(R.id.nbrSubscribersRefresh_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	final ProgressDialog dlg = new ProgressDialog(Home.this);
				dlg.setTitle(getResources().getString(R.string.please_wait));
				dlg.setMessage(getResources().getString(R.string.refreshing_data));
				dlg.show();
            	currentUser.fetchInBackground(new GetCallback<ParseObject>() {
            		public void done(ParseObject user, ParseException e) {
      			    if (e == null) {
		            	dlg.dismiss();
      			    	nbrSubscribers.setText(getResources().getString(R.string.you_have) + user.getInt("nbrSubscribers") + getResources().getString(R.string.subscribers));
      			    } else {
	            		Toast.makeText(Home.this, getResources().getString(R.string.unable_to_refresh), Toast.LENGTH_LONG)
						.show();
	            		dlg.dismiss();
      			  }
            	}
            });
            }
        });
        
        subscribeToUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	findViewById(R.id.subscribeToUser).requestFocus();	
            	
            	if (subscribeToUsername.getText().toString().length() > 0) {
            	
	            	final ProgressDialog dlg = new ProgressDialog(Home.this);
					dlg.setTitle(getResources().getString(R.string.please_wait));
					dlg.setMessage(getResources().getString(R.string.checking_username));
					dlg.show();
					
					final HashMap<String, Object> params = new HashMap<String, Object>();
					final String askerUsername = subscribeToUsername.getText().toString();
					params.put("username", askerUsername);
					
					ParseCloud.callFunctionInBackground("userExists", params, new FunctionCallback<Boolean>() {
						   public void done(Boolean exists, ParseException e) {
							   if (e == null) {
								
								   if (exists) {
									   
									   List<String> subscribedUsers = currentUser.getList("subscribedUsers");
										
										if (subscribedUsers != null) {
										
										if (subscribedUsers.contains(askerUsername)) {
											dlg.dismiss();
											Toast.makeText(Home.this, getResources().getString(R.string.already_subscriber), Toast.LENGTH_LONG)
											.show();
										} else {
												
											addSubscriber(currentUser, askerUsername, params);
											dlg.dismiss();
											
										}
										
										} else {
												
											addSubscriber(currentUser, askerUsername, params);
											dlg.dismiss();
	
										}	
									   
								   } else {
									   dlg.dismiss();
									   Toast.makeText(Home.this, getResources().getString(R.string.user) + askerUsername + getResources().getString(R.string.not_exist), Toast.LENGTH_LONG)
										.show();
								   }
								   
							   } else {
								dlg.dismiss();
								Toast.makeText(Home.this, getResources().getString(R.string.unable_to_subscribe) + askerUsername + getResources().getString(R.string.s_questions_please), Toast.LENGTH_LONG)
								.show();
							   }
						   }
					});
					
	            } else {
					
					Toast.makeText(Home.this, getResources().getString(R.string.have_to_type), Toast.LENGTH_LONG)
		            .show();
					
				}
            	
            }
        });
        
        joinGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
	            if (joinGroupname.getText().toString().length()>0) {	
            	
            		findViewById(R.id.joinGroup).requestFocus();	
	            	
	            	final ProgressDialog dlg = new ProgressDialog(Home.this);
					dlg.setTitle(getResources().getString(R.string.please_wait));
					dlg.setMessage(getResources().getString(R.string.checking_groupname));
					dlg.show();
					
					final HashMap<String, Object> params = new HashMap<String, Object>();
					final String groupName = joinGroupname.getText().toString();
					params.put("groupname", groupName);
					params.put("username", currentUser.getUsername());
					
					ParseCloud.callFunctionInBackground("groupExists", params, new FunctionCallback<Boolean>() {
						   public void done(Boolean exists, ParseException e) {
							   if (e == null) {
								
								   if (exists) {
									   
									   List<String> joinedGroups = currentUser.getList("joinedGroups");
										
										if (joinedGroups != null) {
										
										if (joinedGroups.contains(groupName)) {
											dlg.dismiss();
											Toast.makeText(Home.this, getResources().getString(R.string.already_member), Toast.LENGTH_LONG)
											.show();
										} else {
												
											addMember(currentUser, groupName, params);
											dlg.dismiss();
											
										}
										
										} else {
												
											addMember(currentUser, groupName, params);
											dlg.dismiss();
	
										}	
									   
								   } else {
									   dlg.dismiss();
									   Toast.makeText(Home.this, getResources().getString(R.string.the_group) + groupName + getResources().getString(R.string.not_exist), Toast.LENGTH_LONG)
										.show();
								   }
								   
							   } else {
								dlg.dismiss();
								Toast.makeText(Home.this, getResources().getString(R.string.unable_to_join) + groupName + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
								.show();
							   }
						   }
					});
				
	            } else {
					
					Toast.makeText(Home.this, getResources().getString(R.string.have_to_type), Toast.LENGTH_LONG)
		            .show();
					
				}
				
            }
        });
        
        createGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            	if (joinGroupname.getText().toString().length()>0) {
            	
	            	findViewById(R.id.joinGroup).requestFocus();
	            	
	            	final ProgressDialog dlg = new ProgressDialog(Home.this);
					dlg.setTitle(getResources().getString(R.string.please_wait));
					dlg.setMessage(getResources().getString(R.string.checking_groupname));
					dlg.show();
					
					final HashMap<String, Object> params = new HashMap<String, Object>();
					final String groupName = joinGroupname.getText().toString();
					params.put("groupname", groupName);
					params.put("username", currentUser.getUsername());
					
					ParseCloud.callFunctionInBackground("createGroup", params, new FunctionCallback<Boolean>() {
						   public void done(Boolean exists, ParseException e) {
							   if (e == null) {
								   
								   if (exists) {
									   
									   dlg.dismiss();
									   Toast.makeText(Home.this, getResources().getString(R.string.a_group_called) + groupName + getResources().getString(R.string.already_exists), Toast.LENGTH_LONG)
									   .show();
									   
								   } else {
									   
									   currentUser.addUnique("ownedGroups", groupName);
									    
									   currentUser.saveInBackground();
									   
									   dlg.dismiss();
									   Toast.makeText(Home.this, getResources().getString(R.string.group) + groupName + getResources().getString(R.string.successfully_created), Toast.LENGTH_LONG)
									   .show();
									   
								   }
								   
							   } else {
								   
								   dlg.dismiss();
								   Toast.makeText(Home.this, getResources().getString(R.string.unable_to_create), Toast.LENGTH_LONG)
								   .show(); 
								   
							   }
					}});
				
	            } else {
					
					Toast.makeText(Home.this, getResources().getString(R.string.have_to_type), Toast.LENGTH_LONG)
		            .show();
					
				}
				
        }});
        
        findViewById(R.id.profile_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(Home.this,Profile.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intent);
            }
        });
        
        } else {
        	Toast.makeText(Home.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(Home.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }
        
};
    

    @Override
    protected void onPause() {
        super.onPause();
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_ask:
            	startActivity(new Intent(Home.this,Ask.class));
                return true;
            case R.id.action_profile:
            	Intent intentP = new Intent(Home.this,Profile.class);
				intentP.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intentP);
                return true;
            case R.id.action_subscriptions:
            	startActivity(new Intent(Home.this,YourSubscriptions.class));
                return true;
            case R.id.action_groups:
            	startActivity(new Intent(Home.this,YourGroups.class));
                return true;
            case R.id.action_savedQuestions:
            	startActivity(new Intent(Home.this,SavedQuestions.class));
                return true;
            case R.id.action_logOut:
            	ParseUser.logOut();
            	Toast.makeText(Home.this, getResources().getString(R.string.logged_out), Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(Home.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void addMember(final ParseUser user, final String groupName, HashMap<String, Object> params) {
    
    ParseCloud.callFunctionInBackground("addMember", params, new FunctionCallback<Object>() {
		   public void done(Object object, ParseException e) {
				
			   if (e == null) {
				   
			    user.addUnique("joinedGroups", groupName);
				user.addUnique("channels", "Group_" + groupName);
			    
				user.saveInBackground();
				
				Toast.makeText(Home.this, getResources().getString(R.string.successfully_joined) + groupName, Toast.LENGTH_SHORT)
				.show();
			   } else {
				Toast.makeText(Home.this, getResources().getString(R.string.unable_to_join) + groupName + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
				.show();
			   }
		   }
	});
    }
    
    private void addSubscriber(final ParseUser user, final String askerUsername, HashMap<String, Object> params) {
    	
    	ParseCloud.callFunctionInBackground("addSubscriber", params, new FunctionCallback<Object>() {
			   public void done(Object object, ParseException e) {
					
				   if (e == null) {
					   
				    user.addUnique("subscribedUsers", askerUsername);
					user.addUnique("channels", "User_" + askerUsername);
				    
					user.saveInBackground();
					
					Toast.makeText(Home.this, getResources().getString(R.string.successfully_subscribed) + askerUsername + getResources().getString(R.string.s_questions), Toast.LENGTH_SHORT)
					.show();
				   } else {
					Toast.makeText(Home.this, getResources().getString(R.string.unable_to_subscribe) + askerUsername + getResources().getString(R.string.s_questions_please), Toast.LENGTH_LONG)
					.show();
				   }
			   }
		});
    }
    
}
