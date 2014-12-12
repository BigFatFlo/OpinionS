package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class SavedQuestions extends Activity{
	
	//TextView username= null;
	
	ListView savedQuestionsView = null;
	
	Integer page = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_questions);
        
        //username = (TextView) findViewById(R.id.username_savedQuestions);
        
        savedQuestionsView = (ListView) findViewById(R.id.savedQuestions);
        
        findViewById(R.id.newerSavedQuestions_button).setVisibility(View.GONE);
        savedQuestionsView.setVisibility(View.GONE);
		findViewById(R.id.olderSavedQuestions_button).setVisibility(View.GONE);
        
	};
	
    @Override
    protected void onStart() {
        super.onStart();
    
       
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
        	
        page = 1;	

		final ProgressDialog dlg = new ProgressDialog(SavedQuestions.this);
	    dlg.setTitle("Please wait.");
	    dlg.setMessage("Loading Questions. Please wait.");
	    dlg.show();
        	
        //username.setText(currentUser.getUsername());
        
        HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("firstPage", page);
        
        ParseCloud.callFunctionInBackground("savedQuestions", params, new FunctionCallback<ArrayList<HashMap<String, Object>>>() {
			   public void done(ArrayList<HashMap<String, Object>> object, ParseException e) {
				   if (e == null) {
					  
					   savedQuestionsView.setVisibility(View.VISIBLE);
					   findViewById(R.id.olderSavedQuestions_button).setVisibility(View.VISIBLE);
					   String[] from = {"askerUsername", "questionText"};
					   int[] to = {R.id.asker_adapter, R.id.question_adapter};
					   CustomSimpleAdapter adapter = new CustomSimpleAdapter(SavedQuestions.this, object, R.layout.adapter_item , from, to);
					   savedQuestionsView.setAdapter(adapter);
					   dlg.dismiss();
					   
					   if (object.isEmpty()) {
						   savedQuestionsView.setVisibility(View.GONE);
						   findViewById(R.id.olderSavedQuestions_button).setVisibility(View.GONE);
						   Toast.makeText(SavedQuestions.this, "No saved questions!", Toast.LENGTH_LONG)
				            .show();
					   }
					   
				   } else {
					   Toast.makeText(SavedQuestions.this, "Unable to load saved questions", Toast.LENGTH_LONG)
			            .show();
					   dlg.dismiss();
				   }
			   }
		});
        
        } else {
        	Toast.makeText(SavedQuestions.this, "You are not logged in", Toast.LENGTH_LONG)
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
			    dlg.setTitle("Please wait.");
			    dlg.setMessage("Loading Questions. Please wait.");
			    dlg.show();

		        page=page+1;
		        
		        if (CustomSimpleAdapter.toDelete!=null && !CustomSimpleAdapter.toDelete.isEmpty()) {
		        
		        HashMap<String, Object> params0 = new HashMap<String, Object>();
				params0.put("questions", CustomSimpleAdapter.toDelete);
		        
				ParseCloud.callFunctionInBackground("deleteSavedQuestions", params0, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
						   } else {
							Toast.makeText(SavedQuestions.this, "Unable to delete the required questions", Toast.LENGTH_LONG)
							.show();
						   }
					   }
				});
		        
		        CustomSimpleAdapter.toDelete.clear();
		        }
		        
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("firstPage", page);
		        
		        ParseCloud.callFunctionInBackground("savedQuestions", params, new FunctionCallback<ArrayList<HashMap<String, Object>>>() {
					   public void done(ArrayList<HashMap<String, Object>> object, ParseException e) {
						   if (e == null) {
							  
							   if (!object.isEmpty()) {
							   
							   String[] from = {"askerUsername", "questionText"};
							   int[] to = {R.id.asker_adapter, R.id.question_adapter};
							   CustomSimpleAdapter adapter = new CustomSimpleAdapter(SavedQuestions.this, object, R.layout.adapter_item , from, to);
							   savedQuestionsView.setAdapter(adapter);
						       findViewById(R.id.newerSavedQuestions_button).setVisibility(View.VISIBLE);
							   dlg.dismiss();
							   } else {
								   Toast.makeText(SavedQuestions.this, "No more questions to load!", Toast.LENGTH_LONG)
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
			    dlg.setTitle("Please wait.");
			    dlg.setMessage("Loading Questions. Please wait.");
			    dlg.show();

		        page=page-1;
		        
				if (CustomSimpleAdapter.toDelete!=null && !CustomSimpleAdapter.toDelete.isEmpty()) {
				
				HashMap<String, Object> params0 = new HashMap<String, Object>();
				params0.put("questions", CustomSimpleAdapter.toDelete);
		        
				ParseCloud.callFunctionInBackground("deleteSavedQuestions", params0, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
						   } else {
							Toast.makeText(SavedQuestions.this, "Unable to delete the required questions", Toast.LENGTH_LONG)
							.show();
						   }
					   }
				});
		        
		        CustomSimpleAdapter.toDelete.clear();
				}
				

				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("firstPage", page);
				
		        ParseCloud.callFunctionInBackground("savedQuestions", params, new FunctionCallback<ArrayList<HashMap<String, Object>>>() {
					   public void done(ArrayList<HashMap<String, Object>> object, ParseException e) {
						   if (e == null) {
							  
							   String[] from = {"askerUsername", "questionText"};
							   int[] to = {R.id.asker_adapter, R.id.question_adapter};
							   CustomSimpleAdapter adapter = new CustomSimpleAdapter(SavedQuestions.this, object, R.layout.adapter_item , from, to);
							   savedQuestionsView.setAdapter(adapter);
							   if (page>1){
								   findViewById(R.id.newerSavedQuestions_button).setVisibility(View.VISIBLE);
						       } else {
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
    	
    	findViewById(R.id.backToMenu_SQ_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(SavedQuestions.this,Menu.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			});	
    	
        } else {
        	Toast.makeText(SavedQuestions.this, "You are not logged in", Toast.LENGTH_LONG)
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
        
        if (CustomSimpleAdapter.toDelete!=null && !CustomSimpleAdapter.toDelete.isEmpty()) {
        
        HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("questions", CustomSimpleAdapter.toDelete);
        
		ParseCloud.callFunctionInBackground("deleteSavedQuestions", params, new FunctionCallback<Object>() {
			   public void done(Object object, ParseException e) {
				   if (e == null) {
				   } else {
					Toast.makeText(SavedQuestions.this, "Unable to delete the required questions", Toast.LENGTH_LONG)
					.show();
				   }
			   }
		});
        
        CustomSimpleAdapter.toDelete.clear();
        }
        
    };
    
}