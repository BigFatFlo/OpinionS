package com.spersio.opinions;

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
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Group extends ActionBarActivity{
	
	TextView groupName = null;
	TextView memberOrOwner = null;
	TextView listOfOwners = null;
	TextView addOwner = null;
	TextView listOfMembers = null;
	TextView nbrMembers = null;
	TextView leaveGroup = null;
	
	EditText addOwnerUsername = null;
	
	ImageButton addOwnerButton = null;
	ImageButton leaveGroupButton = null;
	
	// Button deleteGroup = null;
	
	Spinner owners = null;
	Spinner members = null;
	
	String groupname = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        overridePendingTransition(0, 0);
        
        setContentView(R.layout.activity_group);
        
        groupName = (TextView) findViewById(R.id.group_title);
        
        memberOrOwner = (TextView) findViewById(R.id.member_or_owner);
        listOfOwners = (TextView) findViewById(R.id.list_owners_text);
        addOwner = (TextView) findViewById(R.id.addOwner);
        listOfMembers = (TextView) findViewById(R.id.list_members_text);
        nbrMembers = (TextView) findViewById(R.id.nbrMembers);
        leaveGroup = (TextView) findViewById(R.id.leaveGroup);
        
        addOwnerUsername = (EditText) findViewById(R.id.addOwnerUsername);
        
        addOwnerButton = (ImageButton) findViewById(R.id.addOwner_button);
        leaveGroupButton = (ImageButton) findViewById(R.id.leaveGroup_button);
        
        // deleteGroup = (Button) findViewById(R.id.delete_group_button);
        
        owners = (Spinner) findViewById(R.id.list_owners_spinner);
        members = (Spinner) findViewById(R.id.list_members_spinner);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
	};
	
	@Override
    protected void onStart() {
        super.onStart();
        
        listOfOwners.setVisibility(View.GONE);
        listOfMembers.setVisibility(View.GONE);
        memberOrOwner.setVisibility(View.GONE);
        addOwnerButton.setVisibility(View.GONE);
        addOwnerUsername.setVisibility(View.GONE);
        addOwner.setVisibility(View.GONE);
        owners.setVisibility(View.GONE);
        members.setVisibility(View.GONE);
        leaveGroup.setVisibility(View.GONE);
        leaveGroupButton.setVisibility(View.GONE);
        // deleteGroup.setVisibility(View.GONE);
        
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {

		final ProgressDialog dlg = new ProgressDialog(Group.this);
	    dlg.setTitle(getResources().getString(R.string.please_wait));
	    dlg.setMessage(getResources().getString(R.string.loading_group_data));
	    dlg.show();
        
	    Intent intent = getIntent();
	    groupname = intent.getStringExtra("groupname");
		
        HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("groupname", groupname);
		params.put("username", currentUser.getUsername());
        
        ParseCloud.callFunctionInBackground("groupData", params, new FunctionCallback<HashMap<String, Object>>() {
			@SuppressWarnings("unchecked")
			public void done(HashMap<String, Object> data, ParseException e) {
				   if (e == null) {
					  
					   dlg.dismiss();
					   
					   groupName.setText(groupname);
					   
					   Boolean isOwner = (Boolean) data.get("isOwner");
					   Boolean isMember = (Boolean) data.get("isMember");
					   
					   if (isOwner) {
						   
						    listOfOwners.setVisibility(View.VISIBLE);
					        listOfMembers.setVisibility(View.VISIBLE);
					        memberOrOwner.setVisibility(View.VISIBLE);
					        addOwnerButton.setVisibility(View.VISIBLE);
					        addOwnerUsername.setVisibility(View.VISIBLE);
					        addOwner.setVisibility(View.VISIBLE);
					        owners.setVisibility(View.VISIBLE);
					        members.setVisibility(View.VISIBLE);
					        // deleteGroup.setVisibility(View.VISIBLE);
					        
					        if (isMember) {
							    memberOrOwner.setText(getResources().getString(R.string.you_are_an_owner_and_a_member));
						        leaveGroup.setVisibility(View.VISIBLE);
						        leaveGroupButton.setVisibility(View.VISIBLE);
					        } else {
						        memberOrOwner.setText(getResources().getString(R.string.you_are_an_owner));
						    }
					        
					        nbrMembers.setText(getResources().getString(R.string.nbrMembers) + ": " + String.valueOf((int) data.get("nbrMembers")));
					        
					        ArrayAdapter<String> adapterOwners = new ArrayAdapter<String>(Group.this,
									android.R.layout.simple_spinner_item , (List<String>) data.get("owners"));
							adapterOwners.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							owners.setAdapter(adapterOwners);

					        ArrayAdapter<String> adapterMembers = new ArrayAdapter<String>(Group.this,
									android.R.layout.simple_spinner_item , (List<String>) data.get("members"));
							adapterMembers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							members.setAdapter(adapterMembers);
						   
					   } else if (isMember) {
						   
						    listOfOwners.setVisibility(View.VISIBLE);
					        memberOrOwner.setVisibility(View.VISIBLE);
					        owners.setVisibility(View.VISIBLE);
					        leaveGroup.setVisibility(View.VISIBLE);
					        leaveGroupButton.setVisibility(View.VISIBLE);
					        
					        memberOrOwner.setText(getResources().getString(R.string.you_are_a_member));
					        
					        nbrMembers.setText(getResources().getString(R.string.nbrMembers) + ": " + String.valueOf((int) data.get("nbrMembers")));
					        
					        ArrayAdapter<String> adapterOwners = new ArrayAdapter<String>(Group.this,
									android.R.layout.simple_spinner_item , (List<String>) data.get("owners"));
							adapterOwners.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							owners.setAdapter(adapterOwners);
						   
					   } else {
						   
						   Toast.makeText(Group.this, getResources().getString(R.string.not_member_nor_owner), Toast.LENGTH_LONG)
				            .show();
						   
					   }
					   
					   
				   } else {
					   Toast.makeText(Group.this, getResources().getString(R.string.unable_to_load_group_data), Toast.LENGTH_LONG)
			            .show();
					   dlg.dismiss();
				   }
			   }
		});
        
        } else {
        	Toast.makeText(Group.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(Group.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }
        
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
        	
        	final ActionBar actionBar = getSupportActionBar();
     		
     		final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        	
        	addOwnerUsername.setOnFocusChangeListener(new OnFocusChangeListener() {          

    	        public void onFocusChange(View v, boolean hasFocus) {
    	            if (hasFocus) {
    	            	actionBar.hide();
    	            } else {
    	                imm.hideSoftInputFromWindow(addOwnerUsername.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    	            	actionBar.show();
    	            }
    	        }
    	    });
        	
        	addOwnerButton.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					
					findViewById(R.id.addOwner).requestFocus();
					
					if (addOwnerUsername.getText().toString().length() > 0) {
					
						final ProgressDialog dlg = new ProgressDialog(Group.this);
						dlg.setTitle(getResources().getString(R.string.please_wait));
						dlg.setMessage(getResources().getString(R.string.checking_username));
						dlg.show();
						
						final HashMap<String, Object> params = new HashMap<String, Object>();
						final String new_owner_username = addOwnerUsername.getText().toString();
						params.put("newOwnerUsername", new_owner_username);
						params.put("username", currentUser.getUsername());
						params.put("groupname", groupname);
						
						ParseCloud.callFunctionInBackground("addOwner", params, new FunctionCallback<Integer>() {
							   public void done(Integer result, ParseException e) {
								   
								   dlg.dismiss();
								   
								   if (e == null) {
									   
									   switch (result) {
									   case -4:
										   
										   Toast.makeText(Group.this, getResources().getString(R.string.not_owner), Toast.LENGTH_LONG)
								            .show();
										   
									   break;
									   case -3:
										   
										   Toast.makeText(Group.this, getResources().getString(R.string.user) + new_owner_username + getResources().getString(R.string.not_exist), Toast.LENGTH_LONG)
								            .show();
										   
									   break;
									   case -2:
										   
										   Toast.makeText(Group.this, getResources().getString(R.string.user) + new_owner_username + getResources().getString(R.string.already_an_owner) + groupname, Toast.LENGTH_LONG)
								            .show();
										   
									   break;
									   case -1:
										   
										   Toast.makeText(Group.this, getResources().getString(R.string.the_group) + groupname + getResources().getString(R.string.not_exist), Toast.LENGTH_LONG)
								            .show();
										   
									   break;
									   case 0:
										   
										   Toast.makeText(Group.this, getResources().getString(R.string.successfully_added) + new_owner_username + getResources().getString(R.string.as_an_owner_of_the_group) + groupname, Toast.LENGTH_LONG)
								            .show();
										   
									   break;
									   }
									   
								   } else {
									   
									   Toast.makeText(Group.this, getResources().getString(R.string.unable_to_add_owner) + groupname + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
							            .show();
									   
								   }
							   }
						});
					
					} else {
						
						Toast.makeText(Group.this, getResources().getString(R.string.have_to_type), Toast.LENGTH_LONG)
			            .show();
						
					}
						
				}
        	});
        	
        	leaveGroupButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
        	
					if (leaveGroup.getText().toString().equals(getResources().getString(R.string.leave_group))) {
						
						final ProgressDialog dlg = new ProgressDialog(Group.this);
					    dlg.setTitle(getResources().getString(R.string.please_wait));
					    dlg.setMessage(getResources().getString(R.string.leaving));
					    dlg.show();
					    
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("groupname", groupname);
						
						ParseCloud.callFunctionInBackground("subtractMember", params, new FunctionCallback<Object>() {
							   public void done(Object object, ParseException e) {
								   if (e == null) {
									   
									    List<String> list = currentUser.getList("joinedGroups");
									    List<String> listChannels = currentUser.getList("channels");
										
									    listChannels.remove("Group_" + groupname);
										list.remove(groupname);
										currentUser.put("channels", listChannels);
										currentUser.put("joinedGroups", list);
										
										currentUser.saveInBackground();
										
										leaveGroupButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe_big));
										leaveGroup.setText(getResources().getString(R.string.rejoin_group));
										dlg.dismiss();
										Toast.makeText(Group.this, getResources().getString(R.string.left_group) + groupname, Toast.LENGTH_SHORT)
										.show();
								   } else {
										dlg.dismiss();
										Toast.makeText(Group.this, getResources().getString(R.string.unable_to_leave) + groupname + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
										.show();
								   }
							   }
						});
					
					} else if (leaveGroup.getText().toString().equals(getResources().getString(R.string.rejoin_group))){
					
						final ProgressDialog dlg = new ProgressDialog(Group.this);
					    dlg.setTitle(getResources().getString(R.string.please_wait));
					    dlg.setMessage(getResources().getString(R.string.rejoining));
					    dlg.show();
						
					    HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("groupname", groupname);
						
						ParseCloud.callFunctionInBackground("addMember", params, new FunctionCallback<Object>() {
							   public void done(Object object, ParseException e) {
								   if (e == null) {
									    
								    currentUser.addUnique("joinedGroups", groupname);
								    currentUser.addUnique("channels", "Group_" + groupname);
								    
									currentUser.saveInBackground();
									
									leaveGroupButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsubscribe_big));
									leaveGroup.setText(getResources().getString(R.string.leave_group));
									dlg.dismiss();
									Toast.makeText(Group.this, getResources().getString(R.string.rejoined_group) + groupname, Toast.LENGTH_SHORT)
									.show();
								   } else {
									dlg.dismiss();
									Toast.makeText(Group.this, getResources().getString(R.string.unable_to_rejoin) + groupname + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
									.show();
								   }
							   }
						});
					}
				}
		
        	});
		
		} else {
        	Toast.makeText(Group.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(Group.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }
				
	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_your_groups_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_home:
            	startActivity(new Intent(Group.this,Home.class));
                return true;
            case R.id.action_ask:
            	startActivity(new Intent(Group.this,Ask.class));
                return true;
            case R.id.action_profile:
            	Intent intentP = new Intent(Group.this,Profile.class);
				intentP.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intentP);
                return true;
            case R.id.action_subscriptions:
            	startActivity(new Intent(Group.this,YourSubscriptions.class));
                return true;
            case R.id.action_savedQuestions:
            	startActivity(new Intent(Group.this,SavedQuestions.class));
                return true;
            case R.id.action_logOut:
            	ParseUser.logOut();
            	Toast.makeText(Group.this, getResources().getString(R.string.logged_out), Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(Group.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}