package com.spersio.opinion;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Results extends Activity {

	TextView askerInfo= null;

	TextView questionText= null;
	
	TextView answer1 = null;
	TextView answer2 = null;
	TextView answer3 = null;
	TextView answer4 = null;
	TextView answer5 = null;
	TextView nA1 = null;
	TextView nA2 = null;
	TextView nA3 = null;
	TextView nA4 = null;
	TextView nA5 = null;
	TextView total = null;
	TextView pc1 = null;
	TextView pc2 = null;
	TextView pc3 = null;
	TextView pc4 = null;
	TextView pc5 = null;
	TextView pcTotal = null;
	
	Button backToMenu = null;
	Button saveResults = null;
	Button subscribe = null;
	
	String questionID = null;
	String askerUsername = null;
	int nId = 0;
	String tag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ParseAnalytics.trackAppOpened(getIntent());
		
		questionID = null;
		nId = 0;
		
		setContentView(R.layout.activity_results);

		askerInfo= (TextView) findViewById(R.id.asker_info_r);
		
		questionText= (TextView) findViewById(R.id.question_view);
		
		backToMenu= (Button) findViewById(R.id.backToMenu_button);
		saveResults= (Button) findViewById(R.id.saveResults_button);
		subscribe= (Button) findViewById(R.id.subscribe_button);
		
		answer1= (TextView) findViewById(R.id.answer1_preview);
		answer2= (TextView) findViewById(R.id.answer2_preview);
		answer3= (TextView) findViewById(R.id.answer3_preview);
		answer4= (TextView) findViewById(R.id.answer4_preview);
		answer5= (TextView) findViewById(R.id.answer5_preview);
		
		nA1= (TextView) findViewById(R.id.n_answer1);
		nA2= (TextView) findViewById(R.id.n_answer2);
		nA3= (TextView) findViewById(R.id.n_answer3);
		nA4= (TextView) findViewById(R.id.n_answer4);
		nA5= (TextView) findViewById(R.id.n_answer5);
		total= (TextView) findViewById(R.id.results_total_n);
		
		pc1= (TextView) findViewById(R.id.pc1);
		pc2= (TextView) findViewById(R.id.pc2);
		pc3= (TextView) findViewById(R.id.pc3);
		pc4= (TextView) findViewById(R.id.pc4);
		pc5= (TextView) findViewById(R.id.pc5);
		pcTotal= (TextView) findViewById(R.id.pcTotal);
		
		answer1.setVisibility(View.GONE);
		answer2.setVisibility(View.GONE);
		answer3.setVisibility(View.GONE);
		answer4.setVisibility(View.GONE);
		answer5.setVisibility(View.GONE);
		nA1.setVisibility(View.GONE);
		nA2.setVisibility(View.GONE);
		nA3.setVisibility(View.GONE);
		nA4.setVisibility(View.GONE);
		nA5.setVisibility(View.GONE);
		total.setVisibility(View.GONE);
		
		pc1.setVisibility(View.GONE);
		pc2.setVisibility(View.GONE);
		pc3.setVisibility(View.GONE);
		pc4.setVisibility(View.GONE);
		pc5.setVisibility(View.GONE);
		pcTotal.setVisibility(View.GONE);
		
		saveResults.setVisibility(View.GONE);
		subscribe.setVisibility(View.GONE);

		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
	    
	    Bundle extras = getIntent().getExtras();
		questionID = extras.getString(CustomPushReceiver.ID);
		nId = extras.getInt(CustomPushReceiver.nID);
		tag = extras.getString(CustomPushReceiver.tag);
		final Boolean international = extras.getBoolean(CustomPushReceiver.international);
		final Boolean around = extras.getBoolean(CustomPushReceiver.around);
		final int radius = extras.getInt(CustomPushReceiver.radius);
		final int nbrAnswers = extras.getInt(CustomPushReceiver.nbrAnswers);
		askerUsername = extras.getString(CustomPushReceiver.askerUsername);
		final String A1 = extras.getString(CustomPushReceiver.A1);
		final String A2 = extras.getString(CustomPushReceiver.A2);
		final String A3 = extras.getString(CustomPushReceiver.A3);
		final String A4 = extras.getString(CustomPushReceiver.A4);
		final String A5 = extras.getString(CustomPushReceiver.A5);
		final int NA1 = extras.getInt(CustomPushReceiver.nA1);
		final int NA2 = extras.getInt(CustomPushReceiver.nA2);
		final int NA3 = extras.getInt(CustomPushReceiver.nA3);
		final int NA4 = extras.getInt(CustomPushReceiver.nA4);
		final int NA5 = extras.getInt(CustomPushReceiver.nA5);
		final int nA = extras.getInt(CustomPushReceiver.nA);
		final double pcA1 = extras.getDouble(CustomPushReceiver.pcA1);
		final double pcA2 = extras.getDouble(CustomPushReceiver.pcA2);
		final double pcA3 = extras.getDouble(CustomPushReceiver.pcA3);
		final double pcA4 = extras.getDouble(CustomPushReceiver.pcA4);
		final double pcA5 = extras.getDouble(CustomPushReceiver.pcA5);
		final String qText = extras.getString(CustomPushReceiver.text);		
			
		if (international) {
			askerInfo.setText("International question asked by " + askerUsername);
		} else if (around) {
			askerInfo.setText("Question asked to users within " + radius + "km of " + askerUsername);
		} else {
			askerInfo.setText("Question asked by " + askerUsername);
		}
		questionText.setText(qText);	
		String total_n = String.valueOf(nA);
		total.setText(total_n);
		pcTotal.setText("100%");
		total.setVisibility(View.VISIBLE);
		pcTotal.setVisibility(View.VISIBLE);
		saveResults.setVisibility(View.VISIBLE);
		subscribe.setVisibility(View.VISIBLE);
		List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
		
		if (subscribedChannels != null) {
		
		if (subscribedChannels.contains(askerUsername)) {
			subscribe.setText("Unsubscribe from " + askerUsername + "'s questions");	
		} else {
			subscribe.setText("Subscribe to " + askerUsername + "'s questions");	
		}
		
		} else {
			subscribe.setText("Subscribe to " + askerUsername + "'s questions");	
		}
			
		
	switch (nbrAnswers)
	{
	  case 2:
		  	answer1.setText(A1);
		  	answer2.setText(A2);
			nA1.setText(String.valueOf(NA1));
			nA2.setText(String.valueOf(NA2));
			pc1.setText(String.valueOf((double)Math.round(pcA1 * 100) / 100) + "%");
			pc2.setText(String.valueOf((double)Math.round(pcA2 * 100) / 100) + "%");
			answer1.setVisibility(View.VISIBLE);
			answer2.setVisibility(View.VISIBLE);
			nA1.setVisibility(View.VISIBLE);
			nA2.setVisibility(View.VISIBLE);
			pc1.setVisibility(View.VISIBLE);
			pc2.setVisibility(View.VISIBLE);
		    break;
	  case 3:
		  	answer1.setText(A1);
		  	answer2.setText(A2);
		  	answer3.setText(A3);
			nA1.setText(String.valueOf(NA1));
			nA2.setText(String.valueOf(NA2));
			nA3.setText(String.valueOf(NA3));
			pc1.setText(String.valueOf((double)Math.round(pcA1 * 100) / 100) + "%");
			pc2.setText(String.valueOf((double)Math.round(pcA2 * 100) / 100) + "%");
			pc3.setText(String.valueOf((double)Math.round(pcA3 * 100) / 100) + "%");
			answer1.setVisibility(View.VISIBLE);
			answer2.setVisibility(View.VISIBLE);
			answer3.setVisibility(View.VISIBLE);
			nA1.setVisibility(View.VISIBLE);
			nA2.setVisibility(View.VISIBLE);
			nA3.setVisibility(View.VISIBLE);
			pc1.setVisibility(View.VISIBLE);
			pc2.setVisibility(View.VISIBLE);
			pc3.setVisibility(View.VISIBLE);
		    break;
	  case 4:
		  	answer1.setText(A1);
		  	answer2.setText(A2);
		  	answer3.setText(A3);
		  	answer4.setText(A4);
			nA1.setText(String.valueOf(NA1));
			nA2.setText(String.valueOf(NA2));
			nA3.setText(String.valueOf(NA3));
			nA4.setText(String.valueOf(NA4));
			pc1.setText(String.valueOf((double)Math.round(pcA1 * 100) / 100) + "%");
			pc2.setText(String.valueOf((double)Math.round(pcA2 * 100) / 100) + "%");
			pc3.setText(String.valueOf((double)Math.round(pcA3 * 100) / 100) + "%");
			pc4.setText(String.valueOf((double)Math.round(pcA4 * 100) / 100) + "%");
			answer1.setVisibility(View.VISIBLE);
			answer2.setVisibility(View.VISIBLE);
			answer3.setVisibility(View.VISIBLE);
			answer4.setVisibility(View.VISIBLE);
			nA1.setVisibility(View.VISIBLE);
			nA2.setVisibility(View.VISIBLE);
			nA3.setVisibility(View.VISIBLE);
			nA4.setVisibility(View.VISIBLE);
			pc1.setVisibility(View.VISIBLE);
			pc2.setVisibility(View.VISIBLE);
			pc3.setVisibility(View.VISIBLE);
			pc4.setVisibility(View.VISIBLE);
			break;
	  case 5:
		  	answer1.setText(A1);
		  	answer2.setText(A2);
		  	answer3.setText(A3);
		  	answer4.setText(A4);
		  	answer5.setText(A5);
			nA1.setText(String.valueOf(NA1));
			nA2.setText(String.valueOf(NA2));
			nA3.setText(String.valueOf(NA3));
			nA4.setText(String.valueOf(NA4));
			nA5.setText(String.valueOf(NA5));
			pc1.setText(String.valueOf((double)Math.round(pcA1 * 100) / 100) + "%");
			pc2.setText(String.valueOf((double)Math.round(pcA2 * 100) / 100) + "%");
			pc3.setText(String.valueOf((double)Math.round(pcA3 * 100) / 100) + "%");
			pc4.setText(String.valueOf((double)Math.round(pcA4 * 100) / 100) + "%");
			pc5.setText(String.valueOf((double)Math.round(pcA5 * 100) / 100) + "%");
			answer1.setVisibility(View.VISIBLE);
			answer2.setVisibility(View.VISIBLE);
			answer3.setVisibility(View.VISIBLE);
			answer4.setVisibility(View.VISIBLE);
			answer5.setVisibility(View.VISIBLE);
			nA1.setVisibility(View.VISIBLE);
			nA2.setVisibility(View.VISIBLE);
			nA3.setVisibility(View.VISIBLE);
			nA4.setVisibility(View.VISIBLE);
			nA5.setVisibility(View.VISIBLE);
			pc1.setVisibility(View.VISIBLE);
			pc2.setVisibility(View.VISIBLE);
			pc3.setVisibility(View.VISIBLE);
			pc4.setVisibility(View.VISIBLE);
			pc5.setVisibility(View.VISIBLE);
		    break;
	  default:
		  Toast.makeText(Results.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
			.show();             
	}
	
	} else {
		Toast.makeText(Results.this, "You are not logged in", Toast.LENGTH_LONG)
		.show();
		Intent intent = new Intent(Results.this, Login.class);
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
	
	findViewById(R.id.saveResults_button).setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {
			final ProgressDialog dlg = new ProgressDialog(Results.this);
		    dlg.setTitle("Please wait.");
		    dlg.setMessage("Saving results. Please wait.");
		    dlg.show();
		    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(tag, nId);
		    ParseRelation<ParseObject> relation = currentUser.getRelation("savedQuestions");
		    relation.add(ParseObject.createWithoutData("Question", questionID));
		    currentUser.saveInBackground();
			Toast.makeText(Results.this, "Results saved!", Toast.LENGTH_LONG)
			.show();
			saveResults.setVisibility(View.GONE);
			dlg.dismiss();
		}
		});
	
	findViewById(R.id.subscribe_button).setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {
			
			if (subscribe.getText().toString().equals("Unsubscribe from " + askerUsername + "'s questions")) {
				
			final ProgressDialog dlg = new ProgressDialog(Results.this);
		    dlg.setTitle("Please wait.");
		    dlg.setMessage("Unsubscribing. Please wait.");
		    dlg.show();
		    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(tag, nId);
			ParsePush.unsubscribeInBackground(askerUsername);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("username", askerUsername);
			ParseCloud.callFunctionInBackground("subtractSubscriber", params, new FunctionCallback<Object>() {
				   public void done(Object object, ParseException e) {
					   if (e == null) {
						subscribe.setVisibility(View.GONE);
						dlg.dismiss();
						Toast.makeText(Results.this, "Unsubscribed from " + askerUsername + "'s questions!", Toast.LENGTH_LONG)
						.show();
					   } else {
						dlg.dismiss();
						Toast.makeText(Results.this, "Unable to unsubscribe from " + askerUsername + "'s questions, please try again.", Toast.LENGTH_LONG)
						.show();
					   }
				   }
				});
				
			} else {
			
			final ProgressDialog dlg = new ProgressDialog(Results.this);
		    dlg.setTitle("Please wait.");
		    dlg.setMessage("Subscribing. Please wait.");
		    dlg.show();
		    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(tag, nId);
			ParsePush.subscribeInBackground(askerUsername);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("username", askerUsername);
			ParseCloud.callFunctionInBackground("addSubscriber", params, new FunctionCallback<Object>() {
				   public void done(Object object, ParseException e) {
					   if (e == null) {
						subscribe.setVisibility(View.GONE);
						dlg.dismiss();
						Toast.makeText(Results.this, "Subscribed to " + askerUsername + "'s questions!", Toast.LENGTH_LONG)
						.show();
					   } else {
						dlg.dismiss();
						Toast.makeText(Results.this, "Unable to subscribe to " + askerUsername + "'s questions, please try again.", Toast.LENGTH_LONG)
						.show();
					   }
				   }
				});
			}
		}
		});
	
		/*ParseQuery<ParseObject> questionQuery = ParseQuery.getQuery("Question");
			questionQuery.getInBackground(questionID, new GetCallback<ParseObject>() {
				public void done(final ParseObject question, ParseException e) {
					if (e==null){
						
						if (question.getBoolean("international")) {
							askerInfo.setText("International question asked by " + question.getString("askerUsername"));
						} else if (question.getBoolean("around")) {
							askerInfo.setText("Question asked to users within " + question.getInt("radius") + "km of " + question.getString("askerUsername"));
						} else {
							askerInfo.setText("Question asked by " + question.getString("askerUsername"));
						}
						questionText.setText(question.getString("text"));
						int nA = question.getInt("nbrAnswers");	
						String total_n = String.valueOf(question.getInt("nA"));
						total.setText(total_n);
						pcTotal.setText("100%");
						total.setVisibility(View.VISIBLE);
						pcTotal.setVisibility(View.VISIBLE);
						saveResults.setVisibility(View.VISIBLE);
						dlg.dismiss();
					switch (nA)
					{
					  case 2:
						  	answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							nA1.setText(String.valueOf(question.getInt("nA1")));
							nA2.setText(String.valueOf(question.getInt("nA2")));
							pc1.setText(String.valueOf((double)Math.round(question.getDouble("pcA1") * 100) / 100) + "%");
							pc2.setText(String.valueOf((double)Math.round(question.getDouble("pcA2") * 100) / 100) + "%");
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							nA1.setVisibility(View.VISIBLE);
							nA2.setVisibility(View.VISIBLE);
							pc1.setVisibility(View.VISIBLE);
							pc2.setVisibility(View.VISIBLE);
						    break;
					  case 3:
						  	answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer3.setText(question.getString("answer3"));
							nA1.setText(String.valueOf(question.getInt("nA1")));
							nA2.setText(String.valueOf(question.getInt("nA2")));
							nA3.setText(String.valueOf(question.getInt("nA3")));
							pc1.setText(String.valueOf((double)Math.round(question.getDouble("pcA1") * 100) / 100) + "%");
							pc2.setText(String.valueOf((double)Math.round(question.getDouble("pcA2") * 100) / 100) + "%");
							pc3.setText(String.valueOf((double)Math.round(question.getDouble("pcA3") * 100) / 100) + "%");
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							answer3.setVisibility(View.VISIBLE);
							nA1.setVisibility(View.VISIBLE);
							nA2.setVisibility(View.VISIBLE);
							nA3.setVisibility(View.VISIBLE);
							pc1.setVisibility(View.VISIBLE);
							pc2.setVisibility(View.VISIBLE);
							pc3.setVisibility(View.VISIBLE);
						    break;
					  case 4:
						  	answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer3.setText(question.getString("answer3"));
							answer4.setText(question.getString("answer4"));
							nA1.setText(String.valueOf(question.getInt("nA1")));
							nA2.setText(String.valueOf(question.getInt("nA2")));
							nA3.setText(String.valueOf(question.getInt("nA3")));
							nA4.setText(String.valueOf(question.getInt("nA4")));
							pc1.setText(String.valueOf((double)Math.round(question.getDouble("pcA1") * 100) / 100) + "%");
							pc2.setText(String.valueOf((double)Math.round(question.getDouble("pcA2") * 100) / 100) + "%");
							pc3.setText(String.valueOf((double)Math.round(question.getDouble("pcA3") * 100) / 100) + "%");
							pc4.setText(String.valueOf((double)Math.round(question.getDouble("pcA4") * 100) / 100) + "%");
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							answer3.setVisibility(View.VISIBLE);
							answer4.setVisibility(View.VISIBLE);
							nA1.setVisibility(View.VISIBLE);
							nA2.setVisibility(View.VISIBLE);
							nA3.setVisibility(View.VISIBLE);
							nA4.setVisibility(View.VISIBLE);
							pc1.setVisibility(View.VISIBLE);
							pc2.setVisibility(View.VISIBLE);
							pc3.setVisibility(View.VISIBLE);
							pc4.setVisibility(View.VISIBLE);
							break;
					  case 5:
						  	answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer3.setText(question.getString("answer3"));
							answer4.setText(question.getString("answer4"));
							answer5.setText(question.getString("answer5"));
							nA1.setText(String.valueOf(question.getInt("nA1")));
							nA2.setText(String.valueOf(question.getInt("nA2")));
							nA3.setText(String.valueOf(question.getInt("nA3")));
							nA4.setText(String.valueOf(question.getInt("nA4")));
							nA5.setText(String.valueOf(question.getInt("nA5")));
							pc1.setText(String.valueOf((double)Math.round(question.getDouble("pcA1") * 100) / 100) + "%");
							pc2.setText(String.valueOf((double)Math.round(question.getDouble("pcA2") * 100) / 100) + "%");
							pc3.setText(String.valueOf((double)Math.round(question.getDouble("pcA3") * 100) / 100) + "%");
							pc4.setText(String.valueOf((double)Math.round(question.getDouble("pcA4") * 100) / 100) + "%");
							pc5.setText(String.valueOf((double)Math.round(question.getDouble("pcA5") * 100) / 100) + "%");
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							answer3.setVisibility(View.VISIBLE);
							answer4.setVisibility(View.VISIBLE);
							answer5.setVisibility(View.VISIBLE);
							nA1.setVisibility(View.VISIBLE);
							nA2.setVisibility(View.VISIBLE);
							nA3.setVisibility(View.VISIBLE);
							nA4.setVisibility(View.VISIBLE);
							nA5.setVisibility(View.VISIBLE);
							pc1.setVisibility(View.VISIBLE);
							pc2.setVisibility(View.VISIBLE);
							pc3.setVisibility(View.VISIBLE);
							pc4.setVisibility(View.VISIBLE);
							pc5.setVisibility(View.VISIBLE);
						    break;
					  default:
						  Toast.makeText(Results.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
							.show();             
					}
					
					findViewById(R.id.saveResults_button).setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							final ProgressDialog dlg2 = new ProgressDialog(Results.this);
						    dlg2.setTitle("Please wait.");
						    dlg2.setMessage("Saving results. Please wait.");
						    dlg2.show();
						    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							manager.cancel(nId);
						    ParseRelation<ParseObject> relation = currentUser.getRelation("savedQuestions");
						    relation.add(ParseObject.createWithoutData("Question", questionID));
						    currentUser.saveInBackground();
							Toast.makeText(Results.this, "Results saved!", Toast.LENGTH_LONG)
							.show();
							saveResults.setVisibility(View.GONE);
							dlg2.dismiss();
						}
						});
					
				} else {
					dlg.dismiss();
					Toast.makeText(Results.this, "Unable to load the results", Toast.LENGTH_LONG)
					.show();; 	
					}
				
				}});*/
			
			findViewById(R.id.backToMenu_button).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					manager.cancel(tag, nId);
					Intent intent = new Intent(Results.this,Menu.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				});
			
			

		} else {
			Toast.makeText(Results.this, "You are not logged in", Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Results.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	
	};
	
	public static void saveResults(String questionID, final Context context, int nId, String tag){
		if (ParseUser.getCurrentUser()!=null){
		ParseRelation<ParseObject> relation = ParseUser.getCurrentUser().getRelation("savedQuestions");
		relation.add(ParseObject.createWithoutData("Question", questionID));
		ParseUser.getCurrentUser().saveInBackground();
		Toast.makeText(context, "Results saved!", Toast.LENGTH_LONG)
		.show();
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(tag, nId);
		} else {
		Toast.makeText(context, "Log in and try again!", Toast.LENGTH_LONG)
		.show();
		}
		
	};
}
