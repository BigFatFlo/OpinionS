package com.spersio.opinion;

import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Interested extends Activity {

	TextView questionText= null;
	
	TextView askerInfo= null;
	
	TextView answer1 = null;
	TextView answer2 = null;
	TextView answer3 = null;
	TextView answer4 = null;
	TextView answer5 = null;
	
	Integer nA = 0;
	Integer nId = 0;
	String tag = null;
	String questionID = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ParseAnalytics.trackAppOpened(getIntent());
		
		questionID = null;
		nA = 0;
		nId = 0;
		
		setContentView(R.layout.activity_interested);

		questionText= (TextView) findViewById(R.id.in_question_view);
		
		askerInfo= (TextView) findViewById(R.id.asker_info_i);
		
		answer1= (TextView) findViewById(R.id.answer1_preview);
		answer2= (TextView) findViewById(R.id.answer2_preview);
		answer3= (TextView) findViewById(R.id.answer3_preview);
		answer4= (TextView) findViewById(R.id.answer4_preview);
		answer5= (TextView) findViewById(R.id.answer5_preview);
		
		answer1.setVisibility(View.GONE);
		answer2.setVisibility(View.GONE);
		answer3.setVisibility(View.GONE);
		answer4.setVisibility(View.GONE);
		answer5.setVisibility(View.GONE);
			
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			
	        Bundle extras = getIntent().getExtras();
			questionID = extras.getString(CustomPushReceiver.ID);
			nId = extras.getInt(CustomPushReceiver.nID);
			tag = extras.getString(CustomPushReceiver.tag);
			final String qText = extras.getString(CustomPushReceiver.text);
			nA = extras.getInt(CustomPushReceiver.nbrAnswers);
			final Boolean international = extras.getBoolean(CustomPushReceiver.international);
			final Boolean around = extras.getBoolean(CustomPushReceiver.around);
			final int radius = extras.getInt(CustomPushReceiver.radius);
			final String askerUsername = extras.getString(CustomPushReceiver.askerUsername);
			 
			if (international) {
				askerInfo.setText("International question asked by " + askerUsername);
			} else if (around) {
				askerInfo.setText("Question asked to users within " + radius + "km of " + askerUsername);
			} else {
				askerInfo.setText("Question asked by " + askerUsername);
			}

			questionText.setText(qText);
			
			switch (nA)
			{
			  case 2:
				  	answer1.setText(extras.getString(CustomPushReceiver.A1));
					answer2.setText(extras.getString(CustomPushReceiver.A2));
					answer1.setVisibility(View.VISIBLE);
					answer2.setVisibility(View.VISIBLE);
				    break;
			  case 3:
				  	answer1.setText(extras.getString(CustomPushReceiver.A1));
					answer2.setText(extras.getString(CustomPushReceiver.A2));
					answer3.setText(extras.getString(CustomPushReceiver.A3));
					answer1.setVisibility(View.VISIBLE);
					answer2.setVisibility(View.VISIBLE);
					answer3.setVisibility(View.VISIBLE);
				    break;
			  case 4:
				  	answer1.setText(extras.getString(CustomPushReceiver.A1));
					answer2.setText(extras.getString(CustomPushReceiver.A2));
					answer3.setText(extras.getString(CustomPushReceiver.A3));
					answer4.setText(extras.getString(CustomPushReceiver.A4));
					answer1.setVisibility(View.VISIBLE);
					answer2.setVisibility(View.VISIBLE);
					answer3.setVisibility(View.VISIBLE);
					answer4.setVisibility(View.VISIBLE);
					break;
			  case 5:
				  	answer1.setText(extras.getString(CustomPushReceiver.A1));
					answer2.setText(extras.getString(CustomPushReceiver.A2));
					answer3.setText(extras.getString(CustomPushReceiver.A3));
					answer4.setText(extras.getString(CustomPushReceiver.A4));
					answer5.setText(extras.getString(CustomPushReceiver.A5));
					answer1.setVisibility(View.VISIBLE);
					answer2.setVisibility(View.VISIBLE);
					answer3.setVisibility(View.VISIBLE);
					answer4.setVisibility(View.VISIBLE);
					answer5.setVisibility(View.VISIBLE);
				    break;
			  default:
				  Toast.makeText(Interested.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
					.show();             
			}
			
			
			
			/*ParseQuery<ParseObject> questionQuery = ParseQuery.getQuery("Question");
			questionQuery.getInBackground(questionID, new GetCallback<ParseObject>() {
				public void done(final ParseObject question, ParseException e) {
					if (e == null) {
						parseQuestion = question; 
						questionText.setText(question.getString("text"));
						dlg.dismiss();
						int nA = question.getInt("nbrAnswers");
						switch (nA){
						case 2:
							answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							break;
						case 3:
							answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer3.setText(question.getString("answer3"));
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							answer3.setVisibility(View.VISIBLE);
							break;
						case 4:
							answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer3.setText(question.getString("answer3"));
							answer4.setText(question.getString("answer4"));
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							answer3.setVisibility(View.VISIBLE);
							answer4.setVisibility(View.VISIBLE);
							break;
						case 5:
							answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer3.setText(question.getString("answer3"));
							answer4.setText(question.getString("answer4"));
							answer5.setText(question.getString("answer5"));
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							answer3.setVisibility(View.VISIBLE);
							answer4.setVisibility(View.VISIBLE);
							answer5.setVisibility(View.VISIBLE);
							break;
						}
					} else {
					dlg.dismiss();
					Toast.makeText(Interested.this, "Unable to load the question", Toast.LENGTH_LONG)
					.show();
					Intent intent = new Intent(Interested.this, Menu.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					}
				}
			});*/
		} else {
			Toast.makeText(Interested.this, "You are not logged in", Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Interested.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
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
				
				findViewById(R.id.interested_button).setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						final ProgressDialog dlg = new ProgressDialog(Interested.this);
						dlg.setTitle("Please wait.");
					    dlg.setMessage("Registering interest. Please wait.");
					    dlg.show();
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("id", questionID);
						params.put("interest", 1);
						ParseCloud.callFunctionInBackground("addInterest", params, new FunctionCallback<Object>() {
						   public void done(Object object, ParseException e) {
							   if (e == null) {
								dlg.dismiss();
								Toast.makeText(Interested.this, "Your interest has been noted!", Toast.LENGTH_LONG)
								.show();
								CustomPushReceiver.countDownTimerI.cancel();
								NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
								manager.cancel(tag, nId);
								Intent intent = new Intent(Interested.this, Menu.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							   } else {
								dlg.dismiss();
								Toast.makeText(Interested.this, "Unable to register interest", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});
						
						/*int nI_0 = parseQuestion.getInt("interested");
						parseQuestion.put("interested", nI_0+1);
						parseQuestion.saveInBackground();	
						Toast.makeText(Interested.this, "Your interest has been noted!", Toast.LENGTH_LONG)
						.show();
						Intent intent = new Intent(Interested.this, Menu.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);*/
					}
				});
				
				findViewById(R.id.not_interested_button).setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("id", questionID);
						params.put("interest", 0);
						ParseCloud.callFunctionInBackground("addInterest", params, new FunctionCallback<Object>() {
						   public void done(Object object, ParseException e) {
							   if (e == null) {
								Toast.makeText(Interested.this, "I find your lack of interest disturbing...", Toast.LENGTH_LONG)
								.show();
								CustomPushReceiver.countDownTimerI.cancel();
								NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
								manager.cancel(tag, nId);
								Intent intent = new Intent(Interested.this, Menu.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							   } else {
								Toast.makeText(Interested.this, "Unable to register uninterest, please try again", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});
						
						/*int nI_0 = parseQuestion.getInt("notInterested");
						parseQuestion.put("notInterested", nI_0+1);
						parseQuestion.saveInBackground();
						Toast.makeText(Interested.this, "I find your lack of interest disturbing...", Toast.LENGTH_LONG)
						.show();
						Intent intent = new Intent(Interested.this, Menu.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);*/
					}
				});
				
				} else {
					Toast.makeText(Interested.this, "You are not logged in", Toast.LENGTH_LONG)
					.show();
					Intent intent = new Intent(Interested.this, Login.class);
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
		};
		
		public static void addInterest(String qID, int interest, final Context context, final int nId, final String tag){
			if (ParseUser.getCurrentUser()!=null){
			HashMap<String, Object> params2 = new HashMap<String, Object>();
				params2.put("id", qID);
				params2.put("interest", interest);
				Log.d("Id",params2.get("id").toString());
				Log.d("qID",qID);
				ParseCloud.callFunctionInBackground("addInterest", params2, new FunctionCallback<Object>() {
				   public void done(Object object, ParseException e) {
					   if (e == null) {
						Toast.makeText(context, "Your interest has been noted!", Toast.LENGTH_LONG)
						.show();
						CustomPushReceiver.countDownTimerI.cancel();
						NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						manager.cancel(tag, nId);
					   } else {
						Toast.makeText(context, "Unable to register interest, please try again", Toast.LENGTH_LONG)
						.show();
					   }
				   }
				});
			} else {
				Toast.makeText(context, "Log in and try again!", Toast.LENGTH_LONG)
				.show();
			}
		};
	}