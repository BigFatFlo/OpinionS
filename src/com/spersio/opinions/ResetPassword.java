package com.spersio.opinions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;


public class ResetPassword extends Activity {
	
	private TextView titleView;
	private EditText emailView;	
	private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_reset_password);
        
        titleView = (TextView) findViewById(R.id.reset_title);
        emailView = (EditText) findViewById(R.id.email_reset);
        resetButton = (Button) findViewById(R.id.reset_button);
        
    };

    @Override
    protected void onStart() {
        super.onStart();
    };
    
    @Override
    protected void onResume() {
        super.onResume();
        
    	resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
        
            	if(emailView.getText().toString().length()>3) {
        	
            		final ProgressDialog dlg = new ProgressDialog(ResetPassword.this);
                    dlg.setTitle("Please wait.");
                    dlg.setMessage("Checking address. Please wait.");
                    dlg.show();
            		
		        	ParseUser.requestPasswordResetInBackground(emailView.getText().toString(),
		                    new RequestPasswordResetCallback() {
						public void done(ParseException e) {
						if (e == null) {
							
							emailView.setVisibility(View.GONE);
							resetButton.setVisibility(View.GONE);
							titleView.setText("An email has been sent to your email address!");
							dlg.dismiss();
							
						} else {
							Toast.makeText(ResetPassword.this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_LONG)
				            .show();
							dlg.dismiss();
						}
						
						}
					});
        	
        } else {
        	Toast.makeText(ResetPassword.this, "You have to type in your email address!", Toast.LENGTH_LONG)
            .show();
        }
        
        }});
    	
    	findViewById(R.id.reset_back_to_menu_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(ResetPassword.this,Menu.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
            }
        });
            	  
    };
}