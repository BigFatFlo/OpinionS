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
	
	Integer notificationID = 0;
	String notificationTag = null;
	
	Integer nbrAnswers = 0;
	
	CountDownTimer countDownTimer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		
		questionID = null;
		nbrAnswers = 0;
		notificationID = 0;
		
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
         
        Question question = extras.getParcelable(CustomPushReceiver.questionKey);
		
		questionID = question.questionID;
		notificationID = question.notificationID;
		notificationTag = question.notificationTag;
		nbrAnswers = question.nbrAnswers;
			
		if (question.subscribersOnly) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + question.askerUsername + getResources().getString(R.string.to_his_her_subscribers));
		} else if (question.group) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + question.askerUsername + getResources().getString(R.string.to_the_group) + question.groupName);
		} else {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + question.askerUsername);
		}

		questionText.setText(question.text);
		
		TextView[] answerTextViews = {answer1, answer2, answer3, answer4, answer5};
		ImageButton[] chooseImageButtons = {choose1, choose2, choose3, choose4, choose5};
		ImageView[] numberImageViews = {one, two, three, four, five};
		
		if (nbrAnswers > 1 && nbrAnswers < 6) {
		
			for (int i=0; i< nbrAnswers; i++) {
				answerTextViews[i].setText(question.answers[i]);
	
				answerTextViews[i].setVisibility(View.VISIBLE);
				chooseImageButtons[i].setVisibility(View.VISIBLE);
				numberImageViews[i].setVisibility(View.VISIBLE);
			}
		
		} else {
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
			
			final TextView[] answerTextViews = {answer1, answer2, answer3, answer4, answer5};
			final ImageButton[] chooseImageButtons = {choose1, choose2, choose3, choose4, choose5};
			int[] chooseButtonsResources = {R.id.choose_answer1, R.id.choose_answer2, R.id.choose_answer3, R.id.choose_answer4, R.id.choose_answer5};
			
			for (int i=0; i< nbrAnswers; i++) {
				
				final int j = i;
				findViewById(chooseButtonsResources[i]).setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						answerChosen = chooseAnswer(j, nbrAnswers, choiceMade, answerTextViews, chooseImageButtons)[0];
						choiceMade = chooseAnswer(j, nbrAnswers, choiceMade, answerTextViews, chooseImageButtons)[1] == 1;
					}
				});
				
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
										manager.cancel(notificationTag, notificationID);
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
	
	public int[] chooseAnswer(int answer, int nbrAnswers, boolean choiceMade, TextView[] answerTextViews, ImageButton[] chooseImageButtons) {
		if (!choiceMade){
			for (int i = 0; i < nbrAnswers; i++) {
				if (i != answer) {
					chooseImageButtons[i].setVisibility(View.INVISIBLE);
				}
			}
			chooseImageButtons[answer].setImageResource(R.drawable.ic_reject);
			answerTextViews[answer].setTypeface(null, Typeface.BOLD_ITALIC);
			return new int[] {answer + 1, 1};
		} else {
			for (int i = 0; i < nbrAnswers; i++) {
				if (i != answer) {
					chooseImageButtons[i].setVisibility(View.VISIBLE);
				}
			}
			chooseImageButtons[answer].setImageResource(R.drawable.ic_select);
			answerTextViews[answer].setTypeface(null, Typeface.NORMAL);
			return new int[] {0, 0};
		}
	};
	
	public static void addAnswer(final String qID, int answer, final Context context, final int notificationId, final String nTag){
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
							manager.cancel(nTag, notificationId);
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
