package com.spersio.opinion;

import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Answer extends Activity {

	TextView questionText= null;
	
	TextView askerInfo= null;
	
	String questionID = null;
	
	TextView answer1 = null;
	TextView answer2 = null;
	TextView answer3 = null;
	TextView answer4 = null;
	TextView answer5 = null;
	
	Button choose1 = null;
	Button choose2 = null;
	Button choose3 = null;
	Button choose4 = null;
	Button choose5 = null;
	
	Button submit = null;
	
	Boolean choiceMade = false;
	Integer alreadyAnswered = 0;
	Integer answerChosen = 0;
	
	Integer nId = 0;
	String tag = null;
	
	Integer nA = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ParseAnalytics.trackAppOpened(getIntent());
		
		questionID = null;
		nA = 0;
		nId = 0;
		
		setContentView(R.layout.activity_answer);

		questionText= (TextView) findViewById(R.id.question_view);
		
		askerInfo= (TextView) findViewById(R.id.asker_info_a);
		
		submit= (Button) findViewById(R.id.submit_answer_button);
		
		answer1= (TextView) findViewById(R.id.answer1_preview);
		answer2= (TextView) findViewById(R.id.answer2_preview);
		answer3= (TextView) findViewById(R.id.answer3_preview);
		answer4= (TextView) findViewById(R.id.answer4_preview);
		answer5= (TextView) findViewById(R.id.answer5_preview);
		
		choose1= (Button) findViewById(R.id.choose_answer1);
		choose2= (Button) findViewById(R.id.choose_answer2);
		choose3= (Button) findViewById(R.id.choose_answer3);
		choose4= (Button) findViewById(R.id.choose_answer4);
		choose5= (Button) findViewById(R.id.choose_answer5);
		
		answer1.setVisibility(View.GONE);
		answer2.setVisibility(View.GONE);
		answer3.setVisibility(View.GONE);
		answer4.setVisibility(View.GONE);
		answer5.setVisibility(View.GONE);
		choose1.setVisibility(View.GONE);
		choose2.setVisibility(View.GONE);
		choose3.setVisibility(View.GONE);
		choose4.setVisibility(View.GONE);
		choose5.setVisibility(View.GONE);
		
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
				choose1.setVisibility(View.VISIBLE);
				choose2.setVisibility(View.VISIBLE);
			    break;
		  case 3:
			  	answer1.setText(extras.getString(CustomPushReceiver.A1));
				answer2.setText(extras.getString(CustomPushReceiver.A2));
				answer3.setText(extras.getString(CustomPushReceiver.A3));
				answer1.setVisibility(View.VISIBLE);
				answer2.setVisibility(View.VISIBLE);
				answer3.setVisibility(View.VISIBLE);
				choose1.setVisibility(View.VISIBLE);
				choose2.setVisibility(View.VISIBLE);
				choose3.setVisibility(View.VISIBLE);
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
				choose1.setVisibility(View.VISIBLE);
				choose2.setVisibility(View.VISIBLE);
				choose3.setVisibility(View.VISIBLE);
				choose4.setVisibility(View.VISIBLE);
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
				choose1.setVisibility(View.VISIBLE);
				choose2.setVisibility(View.VISIBLE);
				choose3.setVisibility(View.VISIBLE);
				choose4.setVisibility(View.VISIBLE);
				choose5.setVisibility(View.VISIBLE);
			    break;
		  default:
			  Toast.makeText(Answer.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
				.show();             
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
			
						switch (nA){
						case 2:
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 1;
									choose2.setVisibility(View.GONE);
									choose1.setText("Unchoose!");
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose1.setText("Choose!");
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.GONE);
									choose2.setText("Unchoose!");
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setText("Choose!");
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							
							break;
						case 3:
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 1;
									choose2.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose1.setText("Unchoose!");
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose1.setText("Choose!");
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose2.setText("Unchoose!");
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose2.setText("Choose!");
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 3;
									choose1.setVisibility(View.GONE);
									choose2.setVisibility(View.GONE);
									choose3.setText("Unchoose!");
									answer3.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose3.setText("Choose!");
									answer3.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							
							break;
						case 4:
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 1;
									choose2.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose4.setVisibility(View.GONE);
									choose1.setText("Unchoose!");
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose1.setText("Choose!");
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose4.setVisibility(View.GONE);
									choose2.setText("Unchoose!");
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose2.setText("Choose!");
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 3;
									choose1.setVisibility(View.GONE);
									choose2.setVisibility(View.GONE);
									choose4.setVisibility(View.GONE);
									choose3.setText("Unchoose!");
									answer3.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose3.setText("Choose!");
									answer3.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer4).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 4;
									choose1.setVisibility(View.GONE);
									choose2.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose4.setText("Unchoose!");
									answer4.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setText("Choose!");
									answer4.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							break;
						case 5:
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 1;
										choose2.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose1.setText("Unchoose!");
										answer1.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose1.setText("Choose!");
										answer1.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 2;
										choose1.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose2.setText("Unchoose!");
										answer2.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose2.setText("Choose!");
										answer2.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 3;
										choose1.setVisibility(View.GONE);
										choose2.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose3.setText("Unchoose!");
										answer3.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose3.setText("Choose!");
										answer3.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer4).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 4;
										choose1.setVisibility(View.GONE);
										choose2.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose4.setText("Unchoose!");
										answer4.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose4.setText("Choose!");
										answer4.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer5).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 5;
										choose1.setVisibility(View.GONE);
										choose2.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setText("Unchoose!");
										answer5.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setText("Choose!");
										answer5.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							break;
						}
						
						findViewById(R.id.submit_answer_button).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								if (choiceMade && answerChosen != 0 && answerChosen < 6){
								//int nA = question.getInt("nA");
									final ProgressDialog dlg = new ProgressDialog(Answer.this);
									dlg.setTitle("Please wait.");
								    dlg.setMessage("Submitting Answer. Please wait.");
								    dlg.show();
									HashMap<String, Object> params = new HashMap<String, Object>();
									params.put("n", answerChosen);
									params.put("id", questionID);
									
								ParseCloud.callFunctionInBackground("addAnswer", params, new FunctionCallback<Object>() {
									   public void done(Object object, ParseException e) {
										   if (e == null) {
											dlg.dismiss();
											Toast.makeText(Answer.this, "Answer submitted", Toast.LENGTH_LONG)
											.show();
											currentUser.addUnique("answeredQuestions", questionID);
											currentUser.saveInBackground();
											CustomPushReceiver.countDownTimerA.cancel();
											NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
											manager.cancel(tag, nId);
											Intent intent = new Intent(Answer.this,Menu.class);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);
										   } else {
											dlg.dismiss();
											Toast.makeText(Answer.this, "Unable to submit you answer, please try again", Toast.LENGTH_LONG)
											.show();
										   }
									   };

									});
										
								/*switch(answerChosen){
								case 1:
									int nA1_0 = question.getInt("nA1");
									question.put("nA1",nA1_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 2:
									int nA2_0 = question.getInt("nA2");
									question.put("nA2",nA2_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 3:
									int nA3_0 = question.getInt("nA3");
									question.put("nA3",nA3_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 4:
									int nA4_0 = question.getInt("nA4");
									question.put("nA4",nA4_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 5:
									int nA5_0 = question.getInt("nA5");
									question.put("nA5",nA5_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								}
								Toast.makeText(Answer.this, "Answer submitted", Toast.LENGTH_LONG)
								.show();
								Intent intent2 = new Intent(Answer.this,Menu.class);
								intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent2);*/
								} else {
									Toast.makeText(Answer.this, "You have to choose an answer!", Toast.LENGTH_LONG)
									.show();
								}
							}
							});
			
			
			
			/*ParseQuery<ParseObject> questionQuery = ParseQuery.getQuery("Question");
			questionQuery.getInBackground(questionID, new GetCallback<ParseObject>() {
				public void done(final ParseObject question, ParseException e) {
					if (e == null) {
						questionText.setText(question.getString("text"));
						dlg.dismiss();
						int nA = question.getInt("nbrAnswers");
						switch (nA){
						case 2:
							answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							choose1.setVisibility(View.VISIBLE);
							choose2.setVisibility(View.VISIBLE);
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 1;
									choose2.setVisibility(View.GONE);
									choose1.setText("Unchoose!");
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose1.setText("Choose!");
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.GONE);
									choose2.setText("Unchoose!");
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setText("Choose!");
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							
							break;
						case 3:
							answer1.setText(question.getString("answer1"));
							answer2.setText(question.getString("answer2"));
							answer3.setText(question.getString("answer3"));
							answer1.setVisibility(View.VISIBLE);
							answer2.setVisibility(View.VISIBLE);
							answer3.setVisibility(View.VISIBLE);
							choose1.setVisibility(View.VISIBLE);
							choose2.setVisibility(View.VISIBLE);
							choose3.setVisibility(View.VISIBLE);
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 1;
									choose2.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose1.setText("Unchoose!");
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose1.setText("Choose!");
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose2.setText("Unchoose!");
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose2.setText("Choose!");
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 3;
									choose1.setVisibility(View.GONE);
									choose2.setVisibility(View.GONE);
									choose3.setText("Unchoose!");
									answer3.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose3.setText("Choose!");
									answer3.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							
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
							choose1.setVisibility(View.VISIBLE);
							choose2.setVisibility(View.VISIBLE);
							choose3.setVisibility(View.VISIBLE);
							choose4.setVisibility(View.VISIBLE);
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 1;
									choose2.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose4.setVisibility(View.GONE);
									choose1.setText("Unchoose!");
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose1.setText("Choose!");
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose4.setVisibility(View.GONE);
									choose2.setText("Unchoose!");
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose2.setText("Choose!");
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 3;
									choose1.setVisibility(View.GONE);
									choose2.setVisibility(View.GONE);
									choose4.setVisibility(View.GONE);
									choose3.setText("Unchoose!");
									answer3.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose3.setText("Choose!");
									answer3.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer4).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 4;
									choose1.setVisibility(View.GONE);
									choose2.setVisibility(View.GONE);
									choose3.setVisibility(View.GONE);
									choose4.setText("Unchoose!");
									answer4.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setText("Choose!");
									answer4.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
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
							choose1.setVisibility(View.VISIBLE);
							choose2.setVisibility(View.VISIBLE);
							choose3.setVisibility(View.VISIBLE);
							choose4.setVisibility(View.VISIBLE);
							choose5.setVisibility(View.VISIBLE);
							findViewById(R.id.choose_answer1).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 1;
										choose2.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose1.setText("Unchoose!");
										answer1.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose1.setText("Choose!");
										answer1.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 2;
										choose1.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose2.setText("Unchoose!");
										answer2.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose2.setText("Choose!");
										answer2.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 3;
										choose1.setVisibility(View.GONE);
										choose2.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose3.setText("Unchoose!");
										answer3.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose3.setText("Choose!");
										answer3.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer4).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 4;
										choose1.setVisibility(View.GONE);
										choose2.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose5.setVisibility(View.GONE);
										choose4.setText("Unchoose!");
										answer4.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose4.setText("Choose!");
										answer4.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer5).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 5;
										choose1.setVisibility(View.GONE);
										choose2.setVisibility(View.GONE);
										choose3.setVisibility(View.GONE);
										choose4.setVisibility(View.GONE);
										choose5.setText("Unchoose!");
										answer5.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setText("Choose!");
										answer5.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							break;
						}
						
						findViewById(R.id.submit_answer_button).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								if (choiceMade && answerChosen != 0 && answerChosen < 6){
								//int nA = question.getInt("nA");
								HashMap<String, Object> params = new HashMap<String, Object>();
									params.put("n", answerChosen);
									params.put("id", questionID);
									
								ParseCloud.callFunctionInBackground("addAnswer", params, new FunctionCallback<Object>() {
									   public void done(Object object, ParseException e) {
										   if (e == null) {
											Toast.makeText(Answer.this, "Answer submitted", Toast.LENGTH_LONG)
											.show();
											currentUser.addUnique("answeredQuestions", questionID);
											currentUser.saveInBackground();
											NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
											manager.cancel(nId);
											Intent intent = new Intent(Answer.this,Menu.class);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);
										   } else {
											Toast.makeText(Answer.this, "Unable to submit you answer, please try again", Toast.LENGTH_LONG)
											.show();
										   }
									   };

									});
										
								/*switch(answerChosen){
								case 1:
									int nA1_0 = question.getInt("nA1");
									question.put("nA1",nA1_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 2:
									int nA2_0 = question.getInt("nA2");
									question.put("nA2",nA2_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 3:
									int nA3_0 = question.getInt("nA3");
									question.put("nA3",nA3_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 4:
									int nA4_0 = question.getInt("nA4");
									question.put("nA4",nA4_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								case 5:
									int nA5_0 = question.getInt("nA5");
									question.put("nA5",nA5_0+1);
									question.put("nA", nA+1);
									question.saveInBackground();
									currentUser.addUnique("answeredQuestions", questionID);
									currentUser.saveInBackground();
									break;
								}
								Toast.makeText(Answer.this, "Answer submitted", Toast.LENGTH_LONG)
								.show();
								Intent intent2 = new Intent(Answer.this,Menu.class);
								intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent2);
								} else {
									Toast.makeText(Answer.this, "You have to choose an answer!", Toast.LENGTH_LONG)
									.show();
								}
							}
							});
						
					} else {
						dlg.dismiss();
						Toast.makeText(Answer.this, "Unable to load the question!", Toast.LENGTH_LONG)
						.show();
					}
				}});*/
			
			
			/*Toast.makeText(Answer.this, "You already answered this question!", Toast.LENGTH_LONG)
			.show();
			ParseQuery<ParseObject> questionQuery = ParseQuery.getQuery("Question");
			questionQuery.getInBackground(questionID, new GetCallback<ParseObject>() {
				public void done(final ParseObject question, ParseException e) {
					if (e==null){
						dlg.dismiss();
						questionText.setText(question.getString("text"));
						
						int nA = question.getInt("nbrAnswers");	
					switch (nA)
					{
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
					  default:
						  Toast.makeText(Answer.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
							.show();             
					}
				} else {
					Toast.makeText(Answer.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
					.show();; 	
				}
				}
			});*/

		} else {
			Toast.makeText(Answer.this, "You are not logged in", Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Answer.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

	};
	
	public static void addAnswer(final String qID, int answer, final Context context, final int notificationId, final String notificationTag){
		if (ParseUser.getCurrentUser()!=null){
		HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("n", answer);
				params.put("id", qID);
				
			ParseCloud.callFunctionInBackground("addAnswer", params, new FunctionCallback<Object>() {
				   public void done(Object object, ParseException e) {
					   if (e == null) {
						ParseUser.getCurrentUser().addUnique("answeredQuestions", qID);
						ParseUser.getCurrentUser().saveInBackground();
						Toast.makeText(context, "Answer submitted", Toast.LENGTH_LONG)
						.show();
						CustomPushReceiver.countDownTimerA.cancel();
						NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						manager.cancel(notificationTag, notificationId);
					   } else {
						Toast.makeText(context, "Unable to submit you answer, please try again", Toast.LENGTH_LONG)
						.show();
					   }
				   };
				});
		} else {
			Toast.makeText(context, "You are not logged in!", Toast.LENGTH_LONG)
			.show();
		}
	};
}
