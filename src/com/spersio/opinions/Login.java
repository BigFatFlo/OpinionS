package com.spersio.opinions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends Activity {
	
	private EditText usernameView;
	private EditText passwordView;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        usernameView = (EditText) findViewById(R.id.username_login);
        passwordView = (EditText) findViewById(R.id.password_login);
    };

    @Override
    protected void onStart() {
        super.onStart();
    };
    
    @Override
    protected void onResume() {
        super.onResume();
        
          // Set up the submit button click handler
          findViewById(R.id.log_in_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

              // Validate the sign up data
              boolean validationError = false;
              StringBuilder validationErrorMessage =
                  new StringBuilder(getResources().getString(R.string.error_please));
              if (isEmpty(usernameView)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_no_username));
              }
              if (isEmpty(passwordView)) {
                if (validationError) {
                  validationErrorMessage.append(getResources().getString(R.string.error_and));
                }
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_no_password));
              }
              validationErrorMessage.append(getResources().getString(R.string.error_end));

              // If there is a validation error, display the error
              if (validationError) {
                Toast.makeText(Login.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
                return;
              }

              // Set up a progress dialog
              final ProgressDialog dlg = new ProgressDialog(Login.this);
              dlg.setTitle(getResources().getString(R.string.please_wait));
              dlg.setMessage(getResources().getString(R.string.logging_in));
              dlg.show();

              // Set up a new Parse user
              ParseUser user = new ParseUser();
              user.setUsername(usernameView.getText().toString());
              user.setPassword(passwordView.getText().toString());
              // Call the Parse signup method
              
              ParseUser.logInInBackground(usernameView.getText().toString(), 
            		 passwordView.getText().toString(), new LogInCallback() {
            	  public void done(ParseUser user, ParseException e) {
            		  dlg.dismiss();
            		  if (user != null) {
            	    	Intent intent = new Intent(Login.this, com.spersio.opinions.Home.class);
            	    	intent.putExtra("fromLogin", true);
                        startActivity(intent);
            	    } else {
            	    	Toast.makeText(Login.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG)
                        .show();
            	    }
            	  }
            	});
            }
          });
          
          findViewById(R.id.password_forgotten).setOnClickListener(new View.OnClickListener() {
              public void onClick(View view) {

            	  Intent intent = new Intent(Login.this, ResetPassword.class);
            	  startActivity(intent);
              }
              });
          
          findViewById(R.id.no_account_button).setOnClickListener(new View.OnClickListener() {
              public void onClick(View view) {
            	  
            	  Intent intent = new Intent(Login.this, Signup.class);
            	  startActivity(intent);
              }
              });
            	  
    	};

        private boolean isEmpty(EditText etText) {
          if (etText.getText().toString().trim().length() > 0) {
            return false;
          } else {
            return true;
          }
		};
}