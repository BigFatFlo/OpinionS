package com.spersio.opinions;

import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
	
	TextView countDown = null;
	
	String questionID = null;
	
	TextView answer1 = null;
	TextView answer2 = null;
	TextView answer3 = null;
	TextView answer4 = null;
	TextView answer5 = null;
	
	ImageButton choose1 = null;
	ImageButton choose2 = null;
	ImageButton choose3 = null;
	ImageButton choose4 = null;
	ImageButton choose5 = null;
	
	ImageView one = null;
	ImageView two = null;
	ImageView three = null;
	ImageView four = null;
	ImageView five = null;
	
	Button submit = null;
	
	Boolean choiceMade = false;
	Integer alreadyAnswered = 0;
	Integer answerChosen = 0;
	
	Integer nId = 0;
	String tag = null;
	
	Integer nA = 0;
	
	CountDownTimer countDownTimer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		
		questionID = null;
		nA = 0;
		nId = 0;
		
		setContentView(R.layout.activity_answer);

		questionText= (TextView) findViewById(R.id.question_view_a);
		
		askerInfo= (TextView) findViewById(R.id.asker_info_a);
		
		countDown = (TextView) findViewById(R.id.countdown_a);
		
		submit= (Button) findViewById(R.id.send_answer_button);
		
		answer1= (TextView) findViewById(R.id.answer1_a);
		answer2= (TextView) findViewById(R.id.answer2_a);
		answer3= (TextView) findViewById(R.id.answer3_a);
		answer4= (TextView) findViewById(R.id.answer4_a);
		answer5= (TextView) findViewById(R.id.answer5_a);
		
		choose1= (ImageButton) findViewById(R.id.choose_answer1);
		choose2= (ImageButton) findViewById(R.id.choose_answer2);
		choose3= (ImageButton) findViewById(R.id.choose_answer3);
		choose4= (ImageButton) findViewById(R.id.choose_answer4);
		choose5= (ImageButton) findViewById(R.id.choose_answer5);
		
		one= (ImageView) findViewById(R.id.one_a);
		two= (ImageView) findViewById(R.id.two_a);
		three= (ImageView) findViewById(R.id.three_a);
		four= (ImageView) findViewById(R.id.four_a);
		five= (ImageView) findViewById(R.id.five_a);
		
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
		
		one.setVisibility(View.GONE);
		two.setVisibility(View.GONE);
		three.setVisibility(View.GONE);
		four.setVisibility(View.GONE);
		five.setVisibility(View.GONE);
		
		Bundle extras = getIntent().getExtras();
		
		long timeLeft = extras.getLong(CustomPushReceiver.timeLeft);
		
		countDownTimer = new CountDownTimer(timeLeft-1300, 100) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished<10100) {
                	countDown.setTextColor(getResources().getColor(R.color.red));
                }
            	
            	countDown.setText(String.valueOf((double)Math.round(millisUntilFinished/100) / 10));
            }

            public void onFinish() {
            	Toast.makeText(Answer.this, getResources().getString(R.string.too_late), Toast.LENGTH_SHORT)
				.show();
				Intent intent = new Intent(Answer.this, Home.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				Answer.this.finish();
            }
         }.start();
		
		questionID = extras.getString(CustomPushReceiver.ID);
		nId = extras.getInt(CustomPushReceiver.nID);
		tag = extras.getString(CustomPushReceiver.tag);
		final String qText = extras.getString(CustomPushReceiver.text);
		nA = extras.getInt(CustomPushReceiver.nbrAnswers);
		final Boolean subscribersOnly = extras.getBoolean(CustomPushReceiver.subscribersOnly);
		final Boolean group = extras.getBoolean(CustomPushReceiver.group);
		final String askerUsername = extras.getString(CustomPushReceiver.askerUsername);
		final String groupname = extras.getString(CustomPushReceiver.groupname);
			
		if (subscribersOnly) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + askerUsername + getResources().getString(R.string.to_his_her_subscribers));
		} else if (group) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + askerUsername + getResources().getString(R.string.to_the_group) + groupname);
		} else {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + askerUsername);
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
				one.setVisibility(View.VISIBLE);
				two.setVisibility(View.VISIBLE);
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
				one.setVisibility(View.VISIBLE);
				two.setVisibility(View.VISIBLE);
				three.setVisibility(View.VISIBLE);
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
				one.setVisibility(View.VISIBLE);
				two.setVisibility(View.VISIBLE);
				three.setVisibility(View.VISIBLE);
				four.setVisibility(View.VISIBLE);
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
				one.setVisibility(View.VISIBLE);
				two.setVisibility(View.VISIBLE);
				three.setVisibility(View.VISIBLE);
				four.setVisibility(View.VISIBLE);
				five.setVisibility(View.VISIBLE);
			    break;
		  default:
			  Toast.makeText(Answer.this, getResources().getString(R.string.oops), Toast.LENGTH_LONG)
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
									choose2.setVisibility(View.INVISIBLE);
									choose1.setImageResource(R.drawable.ic_reject);
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose1.setImageResource(R.drawable.ic_select);
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.INVISIBLE);
									choose2.setImageResource(R.drawable.ic_reject);
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setImageResource(R.drawable.ic_select);
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
									choose2.setVisibility(View.INVISIBLE);
									choose3.setVisibility(View.INVISIBLE);
									choose1.setImageResource(R.drawable.ic_reject);
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose1.setImageResource(R.drawable.ic_select);
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.INVISIBLE);
									choose3.setVisibility(View.INVISIBLE);
									choose2.setImageResource(R.drawable.ic_reject);
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose2.setImageResource(R.drawable.ic_select);
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 3;
									choose1.setVisibility(View.INVISIBLE);
									choose2.setVisibility(View.INVISIBLE);
									choose3.setImageResource(R.drawable.ic_reject);
									answer3.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose3.setImageResource(R.drawable.ic_select);
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
									choose2.setVisibility(View.INVISIBLE);
									choose3.setVisibility(View.INVISIBLE);
									choose4.setVisibility(View.INVISIBLE);
									choose1.setImageResource(R.drawable.ic_reject);
									answer1.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose1.setImageResource(R.drawable.ic_select);
									answer1.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 2;
									choose1.setVisibility(View.INVISIBLE);
									choose3.setVisibility(View.INVISIBLE);
									choose4.setVisibility(View.INVISIBLE);
									choose2.setImageResource(R.drawable.ic_reject);
									answer2.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose2.setImageResource(R.drawable.ic_select);
									answer2.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 3;
									choose1.setVisibility(View.INVISIBLE);
									choose2.setVisibility(View.INVISIBLE);
									choose4.setVisibility(View.INVISIBLE);
									choose3.setImageResource(R.drawable.ic_reject);
									answer3.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose4.setVisibility(View.VISIBLE);
									choose3.setImageResource(R.drawable.ic_select);
									answer3.setTypeface(null, Typeface.NORMAL);
									choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer4).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
									answerChosen = 4;
									choose1.setVisibility(View.INVISIBLE);
									choose2.setVisibility(View.INVISIBLE);
									choose3.setVisibility(View.INVISIBLE);
									choose4.setImageResource(R.drawable.ic_reject);
									answer4.setTypeface(null, Typeface.BOLD_ITALIC);
									choiceMade = true;
									} else {
									answerChosen = 0;
									choose1.setVisibility(View.VISIBLE);
									choose2.setVisibility(View.VISIBLE);
									choose3.setVisibility(View.VISIBLE);
									choose4.setImageResource(R.drawable.ic_select);
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
										choose2.setVisibility(View.INVISIBLE);
										choose3.setVisibility(View.INVISIBLE);
										choose4.setVisibility(View.INVISIBLE);
										choose5.setVisibility(View.INVISIBLE);
										choose1.setImageResource(R.drawable.ic_reject);
										answer1.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose1.setImageResource(R.drawable.ic_select);
										answer1.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer2).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 2;
										choose1.setVisibility(View.INVISIBLE);
										choose3.setVisibility(View.INVISIBLE);
										choose4.setVisibility(View.INVISIBLE);
										choose5.setVisibility(View.INVISIBLE);
										choose2.setImageResource(R.drawable.ic_reject);
										answer2.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose2.setImageResource(R.drawable.ic_select);
										answer2.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer3).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 3;
										choose1.setVisibility(View.INVISIBLE);
										choose2.setVisibility(View.INVISIBLE);
										choose4.setVisibility(View.INVISIBLE);
										choose5.setVisibility(View.INVISIBLE);
										choose3.setImageResource(R.drawable.ic_reject);
										answer3.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose3.setImageResource(R.drawable.ic_select);
										answer3.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer4).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 4;
										choose1.setVisibility(View.INVISIBLE);
										choose2.setVisibility(View.INVISIBLE);
										choose3.setVisibility(View.INVISIBLE);
										choose5.setVisibility(View.INVISIBLE);
										choose4.setImageResource(R.drawable.ic_reject);
										answer4.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose5.setVisibility(View.VISIBLE);
										choose4.setImageResource(R.drawable.ic_select);
										answer4.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							findViewById(R.id.choose_answer5).setOnClickListener(new View.OnClickListener() {
								public void onClick(View view) {
									if (!choiceMade){
										answerChosen = 5;
										choose1.setVisibility(View.INVISIBLE);
										choose2.setVisibility(View.INVISIBLE);
										choose3.setVisibility(View.INVISIBLE);
										choose4.setVisibility(View.INVISIBLE);
										choose5.setImageResource(R.drawable.ic_reject);
										answer5.setTypeface(null, Typeface.BOLD_ITALIC);
										choiceMade = true;
									} else {
										answerChosen = 0;
										choose1.setVisibility(View.VISIBLE);
										choose2.setVisibility(View.VISIBLE);
										choose3.setVisibility(View.VISIBLE);
										choose4.setVisibility(View.VISIBLE);
										choose5.setImageResource(R.drawable.ic_select);
										answer5.setTypeface(null, Typeface.NORMAL);
										choiceMade = false;
									}
								}});
							break;
						}
						
						findViewById(R.id.send_answer_button).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								if (choiceMade && answerChosen != 0 && answerChosen < 6){
									final ProgressDialog dlg = new ProgressDialog(Answer.this);
									dlg.setTitle(getResources().getString(R.string.please_wait));
								    dlg.setMessage(getResources().getString(R.string.sending_answer));
								    dlg.show();
				            			    	
				            			    HashMap<String, Object> params = new HashMap<String, Object>();
											params.put("n", answerChosen);
											params.put("id", questionID);
												
											ParseCloud.callFunctionInBackground("addAnswer", params, new FunctionCallback<Object>() {
												   public void done(Object object, ParseException e) {
													   if (e == null) {
														dlg.dismiss();
														Toast.makeText(Answer.this, getResources().getString(R.string.answer_sent), Toast.LENGTH_SHORT)
														.show();
														ParseUser.getCurrentUser().addUnique("answeredQuestions", questionID);
														ParseUser.getCurrentUser().saveInBackground();
														CustomPushReceiver.countDownTimerA.cancel();
														NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
														manager.cancel(tag, nId);
														Intent intent = new Intent(Answer.this,Home.class);
														intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
														startActivity(intent);
														countDownTimer.cancel();
														Answer.this.finish();
													   } else {
														dlg.dismiss();
														Toast.makeText(Answer.this, getResources().getString(R.string.error) + e.getMessage() , Toast.LENGTH_LONG)
														.show();
													   }
												   };

												});
								
								} else {
									Toast.makeText(Answer.this, getResources().getString(R.string.have_to_choose_answer), Toast.LENGTH_LONG)
									.show();
								}
							}
							});
			
		} else {
			Toast.makeText(Answer.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Answer.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			countDownTimer.cancel();
			Answer.this.finish();
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
						Toast.makeText(context, context.getResources().getString(R.string.answer_sent), Toast.LENGTH_SHORT)
						.show();
						CustomPushReceiver.countDownTimerA.cancel();
						NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						manager.cancel(notificationTag, notificationId);
					   } else {
						Toast.makeText(context, context.getResources().getString(R.string.error) + e.getMessage() , Toast.LENGTH_LONG)
						.show();
					   }
				   };
				});
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
			.show();
		}
	};
}
