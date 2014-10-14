package com.spersio.opinion;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class SavedQuestions extends Activity{
	
	TextView username= null;
	
	ListView savedQuestionsView = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_questions);
        
        username = (TextView) findViewById(R.id.username_savedQuestions);
        
        savedQuestionsView = (ListView) findViewById(R.id.savedQuestions);
        
	};
	
    @Override
    protected void onStart() {
        super.onStart();
    
       
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
        	

		final ProgressDialog dlg = new ProgressDialog(SavedQuestions.this);
	    dlg.setTitle("Please wait.");
	    dlg.setMessage("Loading Questions. Please wait.");
        	
        username.setText(currentUser.getUsername());
        
        ParseQueryAdapter<ParseObject> adapter =
        		  new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
        		    public ParseQuery<ParseObject> create() {
        		    	ParseRelation<ParseObject> relation = currentUser.getRelation("savedQuestions");
        		    	ParseQuery<ParseObject> query = relation.getQuery();
        		      return query;
        		    }
        		  });
        
        adapter.setTextKey("text");
        adapter.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {
        	   public void onLoading() {
        	        dlg.show();   
        	   }
        	 
        	   public void onLoaded(List<ParseObject> objects, Exception e) {
        		   	dlg.dismiss();
        	   }
        	 });
        
        savedQuestionsView.setAdapter(adapter);
        
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
    };
    

    

    @Override
    protected void onPause() {
        super.onPause();
    };
    
    @Override
    protected void onStop() {
        super.onStop();
    };
}