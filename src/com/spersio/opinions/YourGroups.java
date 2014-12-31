package com.spersio.opinions;

import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
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

public class YourGroups extends ActionBarActivity{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        overridePendingTransition(0, 0);

        // Notice that setContentView() is not used, because we use the root
           // android.R.id.content as the container for each fragment

           // setup action bar for tabs
           ActionBar actionBar = getSupportActionBar();
           actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
           actionBar.setDisplayShowTitleEnabled(true);

           Tab tab1 = actionBar.newTab()
                   .setText(R.string.action_joined_groups)
                   .setTabListener(new TabListener<JoinedGroupsFragment>(
                           this, "joined groups", JoinedGroupsFragment.class));
           actionBar.addTab(tab1);

           Tab tab2 = actionBar.newTab()
                          .setText(R.string.action_owned_groups)
                          .setTabListener(new TabListener<OwnedGroupsFragment>(
                                  this, "owned groups", OwnedGroupsFragment.class));
           actionBar.addTab(tab2);
        
	};
	
	@Override
    protected void onStart() {
        super.onStart();
        
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
            	startActivity(new Intent(YourGroups.this,Home.class));
                return true;
            case R.id.action_ask:
            	startActivity(new Intent(YourGroups.this,Ask.class));
                return true;
            case R.id.action_profile:
            	Intent intentP = new Intent(YourGroups.this,Profile.class);
				intentP.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intentP);
                return true;
            case R.id.action_subscriptions:
            	startActivity(new Intent(YourGroups.this,YourSubscriptions.class));
                return true;
            case R.id.action_savedQuestions:
            	startActivity(new Intent(YourGroups.this,SavedQuestions.class));
                return true;
            case R.id.action_logOut:
            	ParseUser.logOut();
            	Toast.makeText(YourGroups.this, getResources().getString(R.string.logged_out), Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(YourGroups.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}