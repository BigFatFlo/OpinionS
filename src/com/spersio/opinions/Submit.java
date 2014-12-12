package com.spersio.opinions;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Submit extends Activity {

	TextView questionText= null;
	
	TextView answer1 = null;
	TextView answer2 = null;
	TextView answer3 = null;
	TextView answer4 = null;
	TextView answer5 = null;
	
	TextView usersNumber = null;
	TextView usersAvailable = null;
	
	ImageButton delete1 = null;
	ImageButton delete2 = null;
	ImageButton delete3 = null;
	ImageButton delete4 = null;
	ImageButton delete5 = null;
	
	Button submitButton = null;
	
	ImageView one = null;
	ImageView two = null;
	ImageView three = null;
	ImageView four = null;
	ImageView five = null;
	
	RadioButton normalSubmit = null;
	RadioButton internationalSubmit = null;
	RadioButton areaSubmit = null;
	RadioButton subscribersSubmit = null;
	
	SeekBar usersBar = null;
	
	EditText questionSubmit = null;

	EditText answerSubmit = null;
	
	Integer nbrAnswers = 0;
	
	Integer submit_type = -1;
	
	Integer nbrChannels = 0;
	
	Integer nbrUsersPerChannel = 0;
	
	Integer nbrSubscribers = 0;
	
	Integer nbrUsersTargeted = 0;
	
	Integer nbrUsersAvailable = 0;
	
	Integer radius = 0;
	
	Boolean international = false;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_submit);

		questionSubmit = (EditText) findViewById(R.id.question_submit);
		
		answerSubmit= (EditText) findViewById(R.id.answer_submit);

		questionText= (TextView) findViewById(R.id.question_preview);
		
		usersNumber= (TextView) findViewById(R.id.users_number);
		usersAvailable= (TextView) findViewById(R.id.users_available);
		
		answer1= (TextView) findViewById(R.id.answer1_preview);
		answer2= (TextView) findViewById(R.id.answer2_preview);
		answer3= (TextView) findViewById(R.id.answer3_preview);
		answer4= (TextView) findViewById(R.id.answer4_preview);
		answer5= (TextView) findViewById(R.id.answer5_preview);
		
		one= (ImageView) findViewById(R.id.one_submit);
		two= (ImageView) findViewById(R.id.two_submit);
		three= (ImageView) findViewById(R.id.three_submit);
		four= (ImageView) findViewById(R.id.four_submit);
		five= (ImageView) findViewById(R.id.five_submit);
		
		delete1= (ImageButton) findViewById(R.id.delete_answer1);
		delete2= (ImageButton) findViewById(R.id.delete_answer2);
		delete3= (ImageButton) findViewById(R.id.delete_answer3);
		delete4= (ImageButton) findViewById(R.id.delete_answer4);
		delete5= (ImageButton) findViewById(R.id.delete_answer5);
		
		submitButton= (Button) findViewById(R.id.submit_question_button);
		
		normalSubmit= (RadioButton) findViewById(R.id.normal_submit);
		internationalSubmit= (RadioButton) findViewById(R.id.international_submit);
		areaSubmit= (RadioButton) findViewById(R.id.area_submit);
		subscribersSubmit= (RadioButton) findViewById(R.id.subscribers_submit);
		
		answer1.setVisibility(View.GONE);
		answer2.setVisibility(View.GONE);
		answer3.setVisibility(View.GONE);
		answer4.setVisibility(View.GONE);
		answer5.setVisibility(View.GONE);
		
		one.setVisibility(View.GONE);
		two.setVisibility(View.GONE);
		three.setVisibility(View.GONE);
		four.setVisibility(View.GONE);
		five.setVisibility(View.GONE);
		
		delete1.setVisibility(View.GONE);
		delete2.setVisibility(View.GONE);
		delete3.setVisibility(View.GONE);
		delete4.setVisibility(View.GONE);
		delete5.setVisibility(View.GONE);
		
		submitButton.setVisibility(View.GONE);
		
		usersNumber.setVisibility(View.GONE);
		usersAvailable.setVisibility(View.GONE);
		
		usersBar = (SeekBar)findViewById(R.id.users_seekbar);
		usersBar.setVisibility(View.GONE);
		
		
	};
	
	@Override
	protected void onStart() {
		super.onStart();
	};

	@Override
	protected void onResume() {
		super.onResume();
	
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			
			findViewById(R.id.question_done).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
				  	if (questionSubmit.getText().length()>8){
					Toast.makeText(Submit.this, "Question added", Toast.LENGTH_SHORT)
					.show();	
					questionText.setText(questionSubmit.getText().toString());
					answerSubmit.requestFocus();
				  	} else {
				  	Toast.makeText(Submit.this, "Questions must be more than 8 characters and less than 135", Toast.LENGTH_LONG)
					.show();
				  	}
				}
			});

			findViewById(R.id.answer_add).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answerSubmit.getText().toString().equals("") && answerSubmit.getText().toString() != null){
						switch (nbrAnswers)
						{
						  case 0:
							  	Toast.makeText(Submit.this, "Answer added", Toast.LENGTH_SHORT)
								.show();	
							  	answer1.setText(answerSubmit.getText().toString());
							  	answer1.setVisibility(View.VISIBLE);
							  	delete1.setVisibility(View.VISIBLE);
							  	one.setVisibility(View.VISIBLE);
							  	nbrAnswers=nbrAnswers+1;
							  	break;
						  case 1:
							  	Toast.makeText(Submit.this, "Answer added", Toast.LENGTH_SHORT)
								.show();	
							    answer2.setText(answerSubmit.getText().toString());
							    answer2.setVisibility(View.VISIBLE);
							  	answer2.requestFocus();
							    delete2.setVisibility(View.VISIBLE);
							  	two.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 2:
							  	Toast.makeText(Submit.this, "Answer added", Toast.LENGTH_SHORT)
								.show();	
							    answer3.setText(answerSubmit.getText().toString());
							    answer3.setVisibility(View.VISIBLE);
							  	answer3.requestFocus();
							    delete3.setVisibility(View.VISIBLE);
							  	three.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 3:
							  	Toast.makeText(Submit.this, "Answer added", Toast.LENGTH_SHORT)
								.show();	
							    answer4.setText(answerSubmit.getText().toString());
							    answer4.setVisibility(View.VISIBLE);
							  	answer4.requestFocus();
							    delete4.setVisibility(View.VISIBLE);
							  	four.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 4:
							  	Toast.makeText(Submit.this, "Answer added", Toast.LENGTH_SHORT)
								.show();	
							    answer5.setText(answerSubmit.getText().toString());
							    answer5.setVisibility(View.VISIBLE);
							  	answer5.requestFocus();
							    delete5.setVisibility(View.VISIBLE);
							  	five.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 5:
							  Toast.makeText(Submit.this, "You can't submit more than 5 answers!", Toast.LENGTH_LONG)
								.show();
							    break;
						  default:
							  Toast.makeText(Submit.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
								.show();;             
						}
					} else {
						Toast.makeText(Submit.this, "You need to type something!", Toast.LENGTH_LONG)
						.show();
					}
				}
			});
			
			findViewById(R.id.delete_answer1).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer1.getText().toString().equals("") && answer1.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 1:
						    answer1.setText("");
						    answer1.setVisibility(View.GONE);
						    delete1.setVisibility(View.GONE);
						  	one.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 2:
						    answer1.setText(answer2.getText().toString());
						  	answer2.setText("");
						  	answer2.setVisibility(View.GONE);
						  	delete2.setVisibility(View.GONE);
						  	two.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 3:
						  	answer1.setText(answer2.getText().toString());
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText("");
						  	answer3.setVisibility(View.GONE);
						  	delete3.setVisibility(View.GONE);
						  	three.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 4:
						  	answer1.setText(answer2.getText().toString());
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						  	four.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer1.setText(answer2.getText().toString());
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						  	five.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer2).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer2.getText().toString().equals("") && answer2.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 2:
						  	answer2.setText("");
						  	answer2.setVisibility(View.GONE);
						  	delete2.setVisibility(View.GONE);
						  	two.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 3:
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText("");
						  	answer3.setVisibility(View.GONE);
						  	delete3.setVisibility(View.GONE);
						  	three.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 4:
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						  	four.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						  	five.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer3).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer3.getText().toString().equals("") && answer3.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 3:
						  	answer3.setText("");
						  	answer3.setVisibility(View.GONE);
						  	delete3.setVisibility(View.GONE);
						  	three.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 4:
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						  	four.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						  	five.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer4).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer4.getText().toString().equals("") && answer4.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 4:
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						  	four.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						  	five.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer5).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer5.getText().toString().equals("") && answer5.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 5:
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						  	five.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
      
			normalSubmit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if (isChecked) {
						if (questionText.getText().toString().length() > 8  && nbrAnswers > 1 && nbrAnswers < 6 && !(questionText.getText().toString().equals("Here you can see a preview of your question"))) {
						submit_type = 0;
						internationalSubmit.setChecked(false);
						areaSubmit.setChecked(false);
						subscribersSubmit.setChecked(false);
						final ProgressDialog dlg = new ProgressDialog(Submit.this);
						dlg.setTitle("Please wait.");
						dlg.setMessage("Loading available users. Please wait.");
						dlg.show();
						
						ParseQuery<ParseObject> query0 = ParseQuery.getQuery("Country");
						query0.whereEqualTo("name", currentUser.getString("country"));
						query0.getFirstInBackground(new GetCallback<ParseObject>() {
						  public void done(ParseObject country, ParseException e) {
							if (e == null) {
							  int nbrChannelsAvailable = country.getInt("nbrChannels"); // number of available channels
							  
							  switch (nbrChannelsAvailable) {
								case -1:
								dlg.dismiss();
								Toast.makeText(Submit.this, "Sorry, no users available in your country", Toast.LENGTH_LONG)
							    .show();
								usersNumber.setVisibility(View.GONE);
								usersAvailable.setVisibility(View.GONE);
								usersBar.setVisibility(View.GONE);
								submitButton.setVisibility(View.GONE);
								break;
								case 0:
								nbrUsersAvailable = country.getInt("nbrUsers");
								nbrUsersTargeted = nbrUsersAvailable;
								usersNumber.setText("Ask the " + nbrUsersAvailable + " available users");
								usersNumber.setVisibility(View.VISIBLE);
								usersAvailable.setVisibility(View.GONE);
								usersBar.setVisibility(View.GONE);
								submitButton.setVisibility(View.VISIBLE);
    							submitButton.requestFocus();
								nbrChannels = 1;
								dlg.dismiss();
								break;
								case 1:
								nbrUsersPerChannel = country.getInt("avUsersPerChannel");
								nbrUsersTargeted = nbrUsersPerChannel;
								usersNumber.setText("Ask the " + nbrUsersPerChannel + " available users");
								usersNumber.setVisibility(View.VISIBLE);
								usersAvailable.setVisibility(View.GONE);
								usersBar.setVisibility(View.GONE);
								submitButton.setVisibility(View.VISIBLE);
								submitButton.requestFocus();
								nbrChannels = 1;
								dlg.dismiss();
								break;
								default:
								nbrUsersPerChannel = country.getInt("avUsersPerChannel");
								nbrUsersAvailable = country.getInt("nbrUsers");
								nbrUsersTargeted = nbrUsersPerChannel;
								usersNumber.setText("Ask " + nbrUsersPerChannel + " users (slide for more)");
								usersNumber.setVisibility(View.VISIBLE);
								usersBar.setMax(nbrChannelsAvailable-1);
								usersBar.setProgress(0);
								usersBar.setOnSeekBarChangeListener(customSeekBarListener);
								usersBar.setVisibility(View.VISIBLE);
								usersAvailable.setText(nbrUsersAvailable + " users available in your country");
								usersAvailable.setVisibility(View.VISIBLE);
								submitButton.setVisibility(View.VISIBLE);
								usersAvailable.requestFocus();
								nbrChannels = 1;
								dlg.dismiss();
								}
							  
							} else {
								normalSubmit.setChecked(false);
								internationalSubmit.setChecked(false);
								areaSubmit.setChecked(false);
								subscribersSubmit.setChecked(false);
								submit_type = -1;
								usersNumber.setVisibility(View.GONE);
								usersAvailable.setVisibility(View.GONE);
								usersBar.setVisibility(View.GONE);
								submitButton.setVisibility(View.GONE);
							  Toast.makeText(Submit.this, "Unable to load available users...", Toast.LENGTH_LONG)
							  .show();
							  dlg.dismiss();
							}
						  }
						});
						} else {
							normalSubmit.setChecked(false);
							internationalSubmit.setChecked(false);
							areaSubmit.setChecked(false);
							subscribersSubmit.setChecked(false);
							submit_type = -1;
							Toast.makeText(Submit.this, "You need to add a question and 2 to 5 answers first!", Toast.LENGTH_LONG)
							.show();
							usersNumber.setVisibility(View.GONE);
							usersAvailable.setVisibility(View.GONE);
							usersBar.setVisibility(View.GONE);
							submitButton.setVisibility(View.GONE);
							
						}
					}
				}
			});
			
			internationalSubmit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if (questionText.getText().toString().length() > 8  && nbrAnswers > 1 && nbrAnswers < 6 && !(questionText.getText().toString().equals("Here you can see a preview of your question"))) {
						submit_type = 1;
						normalSubmit.setChecked(false);
						areaSubmit.setChecked(false);
						subscribersSubmit.setChecked(false);
						if (currentUser.getBoolean("international")) {
		            		final ProgressDialog dlg1 = new ProgressDialog(Submit.this);
							dlg1.setTitle("Please wait.");
							dlg1.setMessage("Loading available users. Please wait.");
							dlg1.show();
							ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Country");
							query1.getInBackground("7wQb7cZnQH", new GetCallback<ParseObject>() {
							  public void done(ParseObject country, ParseException e) {
								if (e == null) {
								  int nbrChannelsAvailable = country.getInt("nbrChannels"); // number of available channels
								  
								  switch (nbrChannelsAvailable) {
									case 0:
									nbrUsersAvailable = country.getInt("nbrUsers");
									nbrUsersTargeted = nbrUsersAvailable;
									usersNumber.setText("Ask the " + nbrUsersAvailable + " users available in the world");
									usersNumber.setVisibility(View.VISIBLE);
									usersAvailable.setVisibility(View.GONE);
									usersBar.setVisibility(View.GONE);
									submitButton.setVisibility(View.VISIBLE);
	    							submitButton.requestFocus();
									nbrChannels = 1;
									dlg1.dismiss();
									break;
									case 1:
									nbrUsersPerChannel = country.getInt("avUsersPerChannel");
									nbrUsersTargeted = nbrUsersPerChannel;
									usersNumber.setText("Ask the " + nbrUsersPerChannel + " users available in the world");
									usersNumber.setVisibility(View.VISIBLE);
									usersAvailable.setVisibility(View.GONE);
									usersBar.setVisibility(View.GONE);
									submitButton.setVisibility(View.VISIBLE);
									submitButton.requestFocus();
									nbrChannels = 1;
									dlg1.dismiss();
									break;
									default:
									nbrUsersPerChannel = country.getInt("avUsersPerChannel");
									nbrUsersAvailable = country.getInt("nbrUsers");
									nbrUsersTargeted = nbrUsersPerChannel;
									usersNumber.setText("Ask " + nbrUsersPerChannel + " users (slide for more)");
									usersNumber.setVisibility(View.VISIBLE);
									usersBar.setMax(nbrChannelsAvailable-1);
									usersBar.setProgress(0);
									usersBar.setOnSeekBarChangeListener(customSeekBarListener);
									usersBar.setVisibility(View.VISIBLE);
									usersAvailable.setText(nbrUsersAvailable + " users available in the world");
									usersAvailable.setVisibility(View.VISIBLE);
									submitButton.setVisibility(View.VISIBLE);
	    							usersAvailable.requestFocus();
									nbrChannels = 1;
									dlg1.dismiss();
									}
								  
								} else {
									normalSubmit.setChecked(false);
									internationalSubmit.setChecked(false);
									areaSubmit.setChecked(false);
									subscribersSubmit.setChecked(false);
									submit_type = -1;
									usersNumber.setVisibility(View.GONE);
									usersAvailable.setVisibility(View.GONE);
									usersBar.setVisibility(View.GONE);
									submitButton.setVisibility(View.GONE);
								  Toast.makeText(Submit.this, "Unable to load available users...", Toast.LENGTH_LONG)
								  .show();
								  dlg1.dismiss();
								}
							  }
							});
		            		} else {
		            			normalSubmit.setChecked(false);
								internationalSubmit.setChecked(false);
								areaSubmit.setChecked(false);
								subscribersSubmit.setChecked(false);
								submit_type = -1;
								usersNumber.setVisibility(View.GONE);
								usersAvailable.setVisibility(View.GONE);
								usersBar.setVisibility(View.GONE);
								submitButton.setVisibility(View.GONE);
								Toast.makeText(Submit.this, "You need to accept international questions!", Toast.LENGTH_LONG)
								.show();
		            		}
						} else {
							normalSubmit.setChecked(false);
							internationalSubmit.setChecked(false);
							areaSubmit.setChecked(false);
							subscribersSubmit.setChecked(false);
							submit_type = -1;
							Toast.makeText(Submit.this, "You need to add a question and 2 to 5 answers first!", Toast.LENGTH_LONG)
							.show();
							usersNumber.setVisibility(View.GONE);
							usersAvailable.setVisibility(View.GONE);
							usersBar.setVisibility(View.GONE);
							submitButton.setVisibility(View.GONE);
							
						}
					}
				}
			});
			
			
			areaSubmit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if (questionText.getText().toString().length() > 8  && nbrAnswers > 1 && nbrAnswers < 6 && !(questionText.getText().toString().equals("Here you can see a preview of your question"))) {
						submit_type = 2;
						internationalSubmit.setChecked(false);
						normalSubmit.setChecked(false);
						subscribersSubmit.setChecked(false);
						if (currentUser.getBoolean("useLocation") && currentUser.getBoolean("locationKnown")) {
							final ProgressDialog dlg2 = new ProgressDialog(Submit.this);
							dlg2.setTitle("Please wait.");
							dlg2.setMessage("Loading available users. Please wait.");
							dlg2.show();
							radius = 1;
							HashMap<String, Object> params = new HashMap<String, Object>();
							params.put("radius", radius);
							ParseCloud.callFunctionInBackground("usersAround", params, new FunctionCallback<Integer>() {
							   public void done(Integer number, ParseException e) {
								   if (e == null) {
									nbrUsersAvailable = number;
									nbrUsersTargeted = nbrUsersAvailable;
									if (nbrUsersAvailable == 0) {
										normalSubmit.setChecked(false);
										internationalSubmit.setChecked(false);
										areaSubmit.setChecked(false);
										subscribersSubmit.setChecked(false);
										submit_type = -1;
									Toast.makeText(Submit.this, "No users available in a radius of " + radius + "km", Toast.LENGTH_LONG)
									.show();
									submitButton.setVisibility(View.GONE);
									} else {
									usersNumber.setText("In a radius of " + radius + " km (slide for more)");
									usersNumber.setVisibility(View.VISIBLE);
									usersBar.setMax(9);
									usersBar.setProgress(0);
									usersBar.setOnSeekBarChangeListener(customSeekBarListener);
									usersBar.setVisibility(View.VISIBLE);
									usersAvailable.setText(nbrUsersAvailable + " users available");
									usersAvailable.setVisibility(View.VISIBLE);
									submitButton.setVisibility(View.VISIBLE);
									usersAvailable.requestFocus();
									nbrChannels = 1;
									dlg2.dismiss();
									}
								   } else {
									    normalSubmit.setChecked(false);
										internationalSubmit.setChecked(false);
										areaSubmit.setChecked(false);
										subscribersSubmit.setChecked(false);
										submit_type = -1;
										usersNumber.setVisibility(View.GONE);
										usersAvailable.setVisibility(View.GONE);
										usersBar.setVisibility(View.GONE);
										submitButton.setVisibility(View.GONE);
									Toast.makeText(Submit.this, "Unable to load available users", Toast.LENGTH_LONG)
									.show();
									dlg2.dismiss();
								   }
							   }
							});
							} else {
								normalSubmit.setChecked(false);
								internationalSubmit.setChecked(false);
								areaSubmit.setChecked(false);
								subscribersSubmit.setChecked(false);
								submit_type = -1;
								usersNumber.setVisibility(View.GONE);
								usersAvailable.setVisibility(View.GONE);
								usersBar.setVisibility(View.GONE);
								submitButton.setVisibility(View.GONE);
							Toast.makeText(Submit.this, "Location turned off or unavailable...", Toast.LENGTH_LONG)
							.show();
							}
						} else {
							normalSubmit.setChecked(false);
							internationalSubmit.setChecked(false);
							areaSubmit.setChecked(false);
							subscribersSubmit.setChecked(false);
							submit_type = -1;
							Toast.makeText(Submit.this, "You need to add a question and 2 to 5 answers first!", Toast.LENGTH_LONG)
							.show();
							usersNumber.setVisibility(View.GONE);
							usersAvailable.setVisibility(View.GONE);
							usersBar.setVisibility(View.GONE);
							submitButton.setVisibility(View.GONE);
							
						}
					}
				}
			});
			
			
			subscribersSubmit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if (questionText.getText().toString().length() > 8  && nbrAnswers > 1 && nbrAnswers < 6 && !(questionText.getText().toString().equals("Here you can see a preview of your question"))) {
						submit_type = 3;
						internationalSubmit.setChecked(false);
						areaSubmit.setChecked(false);
						normalSubmit.setChecked(false);
						currentUser.fetchInBackground(new GetCallback<ParseObject>() {
	            			  public void done(ParseObject user, ParseException e) {
	            			    if (e == null) {
	    		            		nbrSubscribers = user.getInt("nbrSubscribers");
	    		            		if (nbrSubscribers <= 0) {
	    		            			Toast.makeText(Submit.this, "You don't seem to have any subscribers...", Toast.LENGTH_LONG)
	    								  .show();
	    		            			normalSubmit.setChecked(false);
	    								internationalSubmit.setChecked(false);
	    								areaSubmit.setChecked(false);
	    								subscribersSubmit.setChecked(false);
	    								submit_type = -1;
	    								usersNumber.setVisibility(View.GONE);
	    								usersAvailable.setVisibility(View.GONE);
	    								usersBar.setVisibility(View.GONE);
										submitButton.setVisibility(View.GONE);
	    		            		} else {
	    		            		usersNumber.setVisibility(View.GONE);
	    		            		usersBar.setVisibility(View.GONE);
	    		            		usersAvailable.setText("Send to " + nbrSubscribers + " subscribers");
	    							usersAvailable.setVisibility(View.VISIBLE);
									submitButton.setVisibility(View.VISIBLE);
	    							submitButton.requestFocus();
	    							nbrChannels = 1;
	    		            		}
	            			    } else {
	    		            		nbrSubscribers = currentUser.getInt("nbrSubscribers");
	    		            		if (nbrSubscribers <= 0) {
	    		            			Toast.makeText(Submit.this, "You don't seem to have any subscribers...", Toast.LENGTH_LONG)
	    								  .show();
	    		            			normalSubmit.setChecked(false);
	    								internationalSubmit.setChecked(false);
	    								areaSubmit.setChecked(false);
	    								subscribersSubmit.setChecked(false);
	    								submit_type = -1;
	    								usersNumber.setVisibility(View.GONE);
	    								usersAvailable.setVisibility(View.GONE);
	    								usersBar.setVisibility(View.GONE);
	    								submitButton.setVisibility(View.GONE);
	    		            		} else {
	    		            		usersNumber.setVisibility(View.GONE);
	    		            		usersBar.setVisibility(View.GONE);
	    		            		usersAvailable.setText("Send to " + nbrSubscribers + " subscribers");
	    							usersAvailable.setVisibility(View.VISIBLE);
									submitButton.setVisibility(View.VISIBLE);
	    							submitButton.requestFocus();
	    							nbrChannels = 1;
	    		            		}
	            			    }
	            			  }
	            			});
						} else {
							normalSubmit.setChecked(false);
							internationalSubmit.setChecked(false);
							areaSubmit.setChecked(false);
							subscribersSubmit.setChecked(false);
							submit_type = -1;
							Toast.makeText(Submit.this, "You need to add a question and 2 to 5 answers first!", Toast.LENGTH_LONG)
							.show();
							usersNumber.setVisibility(View.GONE);
							usersAvailable.setVisibility(View.GONE);
							usersBar.setVisibility(View.GONE);
							submitButton.setVisibility(View.GONE);
							
						}
					}
				}
			});
			
			findViewById(R.id.submit_question_button).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (submit_type == -1) {
					Toast.makeText(Submit.this, "You have to select target users!", Toast.LENGTH_LONG)
					.show();
					} else {
					if  (nbrChannels == 0) {
					Toast.makeText(Submit.this, "No users available for your criteria", Toast.LENGTH_LONG)
					.show();
					} else {
					if (questionText.getText().toString().length() > 8 && !(questionText.getText().toString().equals("Here you can see a preview of your question"))){
					// change to 10, 15, ..????
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("text", questionText.getText().toString());
					params.put("nbrAnswers", nbrAnswers);
					params.put("nbrChannels", nbrChannels);
					params.put("radius",radius);
					switch (submit_type) {
						case 0:
						params.put("international",false);
						params.put("around",false);
						params.put("subscribersOnly", false);
						params.put("nbrUsersTargeted", nbrUsersTargeted);
						break;
						case 1:
						params.put("international",true);
						params.put("around",false);
						params.put("subscribersOnly", false);
						params.put("nbrUsersTargeted", nbrUsersTargeted);
						break;
						case 2:
						params.put("international",false);
						params.put("around",true);
						params.put("subscribersOnly", false);
						params.put("nbrUsersTargeted", nbrUsersTargeted);
						break;
						case 3:
						params.put("international",false);
						params.put("around",false);
						params.put("subscribersOnly", true);
						params.put("nbrUsersTargeted", nbrSubscribers);
						break;
					}
					switch (nbrAnswers)
					{
					case 2:
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						break;
					case 3:
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						params.put("answer3", answer3.getText().toString());
						break;
					case 4:
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						params.put("answer3", answer3.getText().toString());
						params.put("answer4", answer4.getText().toString());
						break;
					case 5:
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						params.put("answer3", answer3.getText().toString());
						params.put("answer4", answer4.getText().toString());
						params.put("answer5", answer5.getText().toString());
						break;
					default:
						Toast.makeText(Submit.this, "You have to submit at at least 2 possible answers", Toast.LENGTH_LONG)
						.show();
					}
					if (nbrAnswers > 1 && nbrAnswers < 6) {
					final ProgressDialog dlg = new ProgressDialog(Submit.this);
					dlg.setTitle("Please wait.");
					dlg.setMessage("Submitting question. Please wait.");
					dlg.show();
					ParseCloud.callFunctionInBackground("newQuestion", params, new FunctionCallback<Object>() {
						   public void done(Object object, ParseException e) {
							   if (e == null) {
								dlg.dismiss();
								Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_SHORT)
								.show();
								Intent intent = new Intent(Submit.this,Menu.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							   } else {
								dlg.dismiss();
								Toast.makeText(Submit.this, "Unable to submit the question, please try again", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});
					} else {
						Toast.makeText(Submit.this, "You have to submit at at least 2 possible answers", Toast.LENGTH_LONG)
						.show();
					}
				}
				else 
				{
				Toast.makeText(Submit.this, "Questions must be more than 8 characters and less than 135", Toast.LENGTH_LONG)
				.show();
				}
			}
			}
			}
		});

		} else {
			Toast.makeText(Submit.this, "You are not logged in", Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Submit.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

	};
	
	@Override
	protected void onPause() {
		super.onPause();
		//unregisterReceiver(mReceiver);
	};
	
	@Override
	protected void onStop() {
		super.onStop();
	};
	
	
	private SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
		
	 @Override
	 public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	     	switch (submit_type) {
				case 0:
			usersNumber.setText("Ask " + Math.min(nbrUsersAvailable,(progress+1)*nbrUsersPerChannel) + " users");
			nbrUsersTargeted = Math.min(nbrUsersAvailable,(progress+1)*nbrUsersPerChannel);
			nbrChannels = progress+1;
				break;
				case 1:
			usersNumber.setText("Ask " + Math.min(nbrUsersAvailable,(progress+1)*nbrUsersPerChannel) + " users");
			nbrUsersTargeted = Math.min(nbrUsersAvailable,(progress+1)*nbrUsersPerChannel);
			nbrChannels = progress+1;
				break;
				case 2:
			radius = progress+1;
				break;
			}
	 };
	 
	 @Override
	 public void onStartTrackingTouch(SeekBar seekBar) {
	 };
	 
	 @Override
	 public void onStopTrackingTouch(SeekBar seekBar) {  
			if (submit_type == 2) {
		 	final ProgressDialog dlgR = new ProgressDialog(Submit.this);
			dlgR.setTitle("Please wait.");
			dlgR.setMessage("Loading available users. Please wait.");
			dlgR.show();
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("radius", radius);
			ParseCloud.callFunctionInBackground("usersAround", params, new FunctionCallback<Integer>() {
			   public void done(Integer number, ParseException e) {
				   if (e == null) {
					nbrUsersAvailable = number;
					usersNumber.setText("In a radius of " + radius + "km");
					usersAvailable.setText(number + " users available");
					nbrUsersTargeted = number;
					dlgR.dismiss();
				   } else {
					Toast.makeText(Submit.this, "Unable to load available users", Toast.LENGTH_LONG)
					.show();
					dlgR.dismiss();
				   }
			   }
			});
			}
			findViewById(R.id.submit_question_button).requestFocus();
	 };
	 
	};
	
}
