package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;

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

public class SavedQuestions extends ActionBarActivity{
	
	//TextView username= null;
	
	ListView savedQuestionsView = null;
	
	Integer page = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        overridePendingTransition(0, 0);

        setContentView(R.layout.activity_saved_questions);
        
        //username = (TextView) findViewById(R.id.username_savedQuestions);
        
        savedQuestionsView = (ListView) findViewById(R.id.savedQuestions);
        
        findViewById(R.id.newerSavedQuestions_text).setVisibility(View.GONE);
        findViewById(R.id.newerSavedQuestions_button).setVisibility(View.GONE);
        savedQuestionsView.setVisibility(View.GONE);
		findViewById(R.id.olderSavedQuestions_text).setVisibility(View.GONE);
		findViewById(R.id.olderSavedQuestions_button).setVisibility(View.GONE);
        
	};
	
    @Override
    protected void onStart() {
        super.onStart();
    
       
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
        	
        page = 1;	

		final ProgressDialog dlg = new ProgressDialog(SavedQuestions.this);
	    dlg.setTitle(getResources().getString(R.string.please_wait));
	    dlg.setMessage(getResources().getString(R.string.loading_questions));
	    dlg.show();
        	
        //username.setText(currentUser.getUsername());
        
        HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("firstPage", page);
        
        ParseCloud.callFunctionInBackground("savedQuestions", params, new FunctionCallback<ArrayList<HashMap<String, Object>>>() {
			   public void done(ArrayList<HashMap<String, Object>> object, ParseException e) {
				   if (e == null) {
					  
					   savedQuestionsView.setVisibility(View.VISIBLE);
					   findViewById(R.id.olderSavedQuestions_text).setVisibility(View.VISIBLE);
					   findViewById(R.id.olderSavedQuestions_button).setVisibility(View.VISIBLE);
					   String[] from = {"askerUsername", "questionText"};
					   int[] to = {R.id.asker_adapter, R.id.question_adapter};
					   SavedQuestionsCustomSimpleAdapter adapter = new SavedQuestionsCustomSimpleAdapter(SavedQuestions.this, object, R.layout.question_adapter_item , from, to);
					   savedQuestionsView.setAdapter(adapter);
					   dlg.dismiss();
					   
					   if (object.isEmpty()) {
						   savedQuestionsView.setVisibility(View.GONE);
						   findViewById(R.id.olderSavedQuestions_text).setVisibility(View.GONE);
						   findViewById(R.id.olderSavedQuestions_button).setVisibility(View.GONE);
						   Toast.makeText(SavedQuestions.this, getResources().getString(R.string.no_saved_questions), Toast.LENGTH_LONG)
				            .show();
					   }
					   
				   } else {
					   Toast.makeText(SavedQuestions.this, getResources().getString(R.string.unable_to_load_questions), Toast.LENGTH_LONG)
			            .show();
					   dlg.dismiss();
				   }
			   }
		});
        
        } else {
        	Toast.makeText(SavedQuestions.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(SavedQuestions.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }

        
    };
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
    	
    	
        	
        findViewById(R.id.olderSavedQuestions_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final ProgressDialog dlg = new ProgressDialog(SavedQuestions.this);
			    dlg.setTitle(getResources().getString(R.string.please_wait));
			    dlg.setMessage(getResources().getString(R.string.loading_questions));
			    dlg.show();

		        page=page+1;
		        
		        if (SavedQuestionsCustomSimpleAdapter.toDelete!=null && !SavedQuestionsCustomSimpleAdapter.toDelete.isEmpty()) {
		        
		        HashMap<String, Object> params0 = new HashMap<String, Object>();
				params0.put("questions", SavedQuestionsCustomSimpleAdapter.toDelete);
		        
				ParseCloud.callFunctionInBackground("deleteSavedQuestions", params0, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
						   } else {
							Toast.makeText(SavedQuestions.this, getResources().getString(R.string.unable_to_delete_questions), Toast.LENGTH_LONG)
							.show();
						   }
					   }
				});
		        
		        SavedQuestionsCustomSimpleAdapter.toDelete.clear();
		        }
		        
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("firstPage", page);
		        
		        ParseCloud.callFunctionInBackground("savedQuestions", params, new FunctionCallback<ArrayList<HashMap<String, Object>>>() {
					   public void done(ArrayList<HashMap<String, Object>> object, ParseException e) {
						   if (e == null) {
							  
							   if (!object.isEmpty()) {
							   
							   String[] from = {"askerUsername", "questionText"};
							   int[] to = {R.id.asker_adapter, R.id.question_adapter};
							   SavedQuestionsCustomSimpleAdapter adapter = new SavedQuestionsCustomSimpleAdapter(SavedQuestions.this, object, R.layout.question_adapter_item , from, to);
							   savedQuestionsView.setAdapter(adapter);
						       findViewById(R.id.newerSavedQuestions_text).setVisibility(View.VISIBLE);
						       findViewById(R.id.newerSavedQuestions_button).setVisibility(View.VISIBLE);
							   dlg.dismiss();
							   } else {
								   Toast.makeText(SavedQuestions.this, getResources().getString(R.string.no_more_questions_to_load), Toast.LENGTH_LONG)
						           .show();
								   page = page-1;
								   dlg.dismiss();
							   }
							   
						   } else {
							   Toast.makeText(SavedQuestions.this, e.getMessage(), Toast.LENGTH_LONG)
					           .show();
							   page = page-1;
							   dlg.dismiss();
						   }
					   }
				});
				
			}
			
		});
    	
    	findViewById(R.id.newerSavedQuestions_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final ProgressDialog dlg = new ProgressDialog(SavedQuestions.this);
			    dlg.setTitle(getResources().getString(R.string.please_wait));
			    dlg.setMessage(getResources().getString(R.string.loading_questions));
			    dlg.show();

		        page=page-1;
		        
				if (SavedQuestionsCustomSimpleAdapter.toDelete!=null && !SavedQuestionsCustomSimpleAdapter.toDelete.isEmpty()) {
				
				HashMap<String, Object> params0 = new HashMap<String, Object>();
				params0.put("questions", SavedQuestionsCustomSimpleAdapter.toDelete);
		        
				ParseCloud.callFunctionInBackground("deleteSavedQuestions", params0, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
						   } else {
							Toast.makeText(SavedQuestions.this, getResources().getString(R.string.unable_to_delete_questions), Toast.LENGTH_LONG)
							.show();
						   }
					   }
				});
		        
		        SavedQuestionsCustomSimpleAdapter.toDelete.clear();
				}
				

				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("firstPage", page);
				
		        ParseCloud.callFunctionInBackground("savedQuestions", params, new FunctionCallback<ArrayList<HashMap<String, Object>>>() {
					   public void done(ArrayList<HashMap<String, Object>> object, ParseException e) {
						   if (e == null) {
							  
							   String[] from = {"askerUsername", "questionText"};
							   int[] to = {R.id.asker_adapter, R.id.question_adapter};
							   SavedQuestionsCustomSimpleAdapter adapter = new SavedQuestionsCustomSimpleAdapter(SavedQuestions.this, object, R.layout.question_adapter_item , from, to);
							   savedQuestionsView.setAdapter(adapter);
							   if (page>1){
							       findViewById(R.id.newerSavedQuestions_text).setVisibility(View.VISIBLE);
								   findViewById(R.id.newerSavedQuestions_button).setVisibility(View.VISIBLE);
						       } else {
							       findViewById(R.id.newerSavedQuestions_text).setVisibility(View.GONE);
						    	   findViewById(R.id.newerSavedQuestions_button).setVisibility(View.GONE);
						       }
							   dlg.dismiss();
							   
						   } else {
							   Toast.makeText(SavedQuestions.this, e.getMessage(), Toast.LENGTH_LONG)
					           .show();
							   page = page+1;
							   dlg.dismiss();
						   }
					   }
				}); 
				
			}
			
		});
    	
    	findViewById(R.id.backToHome_SQ_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(SavedQuestions.this,Home.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			});	
    	
        } else {
        	Toast.makeText(SavedQuestions.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(SavedQuestions.this, Login.class);
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
        
        page = 0;
        
        if (SavedQuestionsCustomSimpleAdapter.toDelete!=null && !SavedQuestionsCustomSimpleAdapter.toDelete.isEmpty()) {
        
        HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("questions", SavedQuestionsCustomSimpleAdapter.toDelete);
        
		ParseCloud.callFunctionInBackground("deleteSavedQuestions", params, new FunctionCallback<Object>() {
			   public void done(Object object, ParseException e) {
				   if (e == null) {
				   } else {
					Toast.makeText(SavedQuestions.this, getResources().getString(R.string.unable_to_delete_questions), Toast.LENGTH_LONG)
					.show();
				   }
			   }
		});
        
        SavedQuestionsCustomSimpleAdapter.toDelete.clear();
        }
        
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_saved_questions_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_home:
            	startActivity(new Intent(SavedQuestions.this,Home.class));
                return true;
            case R.id.action_ask:
            	startActivity(new Intent(SavedQuestions.this,Ask.class));
                return true;
            case R.id.action_profile:
            	Intent intentP = new Intent(SavedQuestions.this,Profile.class);
				intentP.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intentP);
                return true;
            case R.id.action_subscriptions:
            	startActivity(new Intent(SavedQuestions.this,YourSubscriptions.class));
                return true;
            case R.id.action_groups:
            	startActivity(new Intent(SavedQuestions.this,YourGroups.class));
                return true;
            case R.id.action_logOut:
            	ParseUser.logOut();
            	Toast.makeText(SavedQuestions.this, getResources().getString(R.string.logged_out), Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(SavedQuestions.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}