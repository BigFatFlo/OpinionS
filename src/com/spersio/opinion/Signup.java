package com.spersio.opinion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Signup extends Activity {
	
	private EditText usernameView;
	//private EditText emailView;
	private EditText passwordView;
	private EditText passwordAgainView;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_signup);
        
        usernameView = (EditText) findViewById(R.id.username);
        //emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        passwordAgainView = (EditText) findViewById(R.id.passwordAgain);
    };    
     
    @Override
    protected void onStart() {
        super.onStart();
    };

    @Override
    protected void onResume() {
        super.onResume();
    
    // Set up the submit button click handler
          findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

              // Validate the sign up data
              boolean validationError = false;
              StringBuilder validationErrorMessage =
                  new StringBuilder(getResources().getString(R.string.error_please));
              if (isEmpty(usernameView)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_no_username));
              }
              /*if (isEmpty(emailView)) {
                  if (validationError) {
                    validationErrorMessage.append(getResources().getString(R.string.error_and));
                  }
                  validationError = true;
                  validationErrorMessage.append(getResources().getString(R.string.error_no_email));
                }*/
              if (isEmpty(passwordView)) {
                if (validationError) {
                  validationErrorMessage.append(getResources().getString(R.string.error_and));
                }
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_no_password));
              }
              if (!isMatching(passwordView, passwordAgainView)) {
                if (validationError) {
                  validationErrorMessage.append(getResources().getString(R.string.error_and));
                }
                validationError = true;
                validationErrorMessage.append(getResources().getString(
                    R.string.error_passwords_diff));
              }
              validationErrorMessage.append(getResources().getString(R.string.error_end));

              // If there is a validation error, display the error
              if (validationError) {
                Toast.makeText(Signup.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
                return;
              }

              // Set up a progress dialog
              final ProgressDialog dlg = new ProgressDialog(Signup.this);
              dlg.setTitle("Please wait.");
              dlg.setMessage("Signing up. Please wait.");
              dlg.show();

              // Set up a new Parse user
              final ParseUser user = new ParseUser();
              user.setUsername(usernameView.getText().toString());
              //user.setEmail(emailView.getText().toString());
              user.setPassword(passwordView.getText().toString());
              user.put("country", "");
              user.put("nbrQuestionsAsked", 0);
              user.put("international", false);
              user.put("internationalChannel", "");
              user.put("useLocation", false);
              // Call the Parse signup method
              user.signUpInBackground(new SignUpCallback() {

                @Override
                public void done(ParseException e) {
                  dlg.dismiss();
                  if (e != null) {
                    // Show the error message
                    Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                  } else {
                    // Start an intent for the Profile activity
                	  Intent intent = new Intent(Signup.this, Menu.class);
                    startActivity(intent);
                  }
                }
              });
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

        private boolean isMatching(EditText etText1, EditText etText2) {
          if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
          } else {
            return false;
          }
        };

}
