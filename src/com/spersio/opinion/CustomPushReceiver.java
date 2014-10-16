package com.spersio.opinion;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {

  public static final String ID = "com.spersio.opinion.ID";
  public static final String text = "com.spersio.opinion.text";
  public static final String nbrAnswers = "com.spersio.opinion.nbrAnswers";
  public static final String nA = "com.spersio.opinion.nA";
  public static final String A1 = "com.spersio.opinion.A1";
  public static final String A2 = "com.spersio.opinion.A2";
  public static final String A3 = "com.spersio.opinion.A3";
  public static final String A4 = "com.spersio.opinion.A4";
  public static final String A5 = "com.spersio.opinion.A5";
  public static final String international = "com.spersio.opinion.international";
  public static final String around = "com.spersio.opinion.around";
  public static final String radius = "com.spersio.opinion.radius";
  public static final String askerUsername = "com.spersio.opinion.askerUsername";
  public static final String interest = "com.spersio.opinion.interest";
  public static final String nID = "com.spersio.opinion.notificationId";
  public static final String tag = "com.spersio.opinion.notificationTag";
  public static final String nA1 = "com.spersio.opinion.nA1";
  public static final String nA2 = "com.spersio.opinion.nA2";
  public static final String nA3 = "com.spersio.opinion.nA3";
  public static final String nA4 = "com.spersio.opinion.nA4";
  public static final String nA5 = "com.spersio.opinion.nA5";
  public static final String pcA1 = "com.spersio.opinion.pcA1";
  public static final String pcA2 = "com.spersio.opinion.pcA2";
  public static final String pcA3 = "com.spersio.opinion.pcA3";
  public static final String pcA4 = "com.spersio.opinion.pcA4";
  public static final String pcA5 = "com.spersio.opinion.pcA5";
  public static final String aID = "com.spersio.opinion.answerId";
  public static CountDownTimer countDownTimerI= null;
  public static CountDownTimer countDownTimerA= null;

@Override
	public void onPushReceive(Context context, Intent intent) {
	Log.d("Notification","reçue");	
	try {
			//ParseAnalytics.trackAppOpened(intent);
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			String action = json.getString("action");
			/*String questionId = json.getString("questionID");	
			Intent i = new Intent(context, Interested.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	    	i.putExtra(ID, questionId);
	    	context.startActivity(i);*/
			switch (action) {
				case "com.spersio.opinion.ANSWER_INTEREST":
					notifyForInterest(context, intent, json);
				break;
				case "com.spersio.opinion.ANSWER_QUESTION":
					notifyForAnswer(context, intent, json);
				break;
				case "com.spersio.opinion.QUESTION_RESULTS":
					notifyForResults(context, intent, json);
				break;
				default:
					super.onPushReceive(context, intent);
			}
		} catch (JSONException e) {
		 //TODO Auto-generated catch block
			super.onPushReceive(context, intent);
	}
  };
  
@Override
	public void onPushOpen(Context context, Intent intent) {
		Intent launchIntent = new Intent(context, Menu.class);
		launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(launchIntent);
	};
  
  private void notifyForInterest(final Context ctx, Intent i, JSONObject json)
  throws JSONException
  {
	String questionId = json.getString("questionID");
	final String notificationTag = questionId + "_Interest";
	final Integer notificationId = 1;
	final String qText = json.getString("questionText");
	Integer nbrA = json.getInt("nbrAnswers");
	final String answer1 = json.getString("answer1");
	final String answer2 = json.getString("answer2");
	final String answer3 = json.getString("answer3");
	final String answer4 = json.getString("answer4");
	final String answer5 = json.getString("answer5");
	String askerU = json.getString("askerUsername");
	Boolean inter = json.getBoolean("international");
	Integer r = json.getInt("radius");
	Boolean arnd = json.getBoolean("around");
	
	// Creates an explicit intent for an Activity in your app
	final Intent interestIntent = new Intent(ctx, Interested.class);
	interestIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	Bundle extras = new Bundle();
	extras.putString(ID, questionId);
	extras.putString(text, qText);
	extras.putInt(nbrAnswers, nbrA);
	extras.putString(A1, answer1);
	extras.putString(A2, answer2);
	extras.putString(A3, answer3);
	extras.putString(A4, answer4);
	extras.putString(A5, answer5);
	extras.putInt(nID,notificationId);
	extras.putString(tag,notificationTag);
	extras.putInt(radius, r);
	extras.putBoolean(international, inter);
	extras.putBoolean(around, arnd);
	extras.putString(askerUsername, askerU);
	interestIntent.putExtras(extras);
	
	Intent quickIntent1 = new Intent(ctx, InterestNotificationActivity.class);
	Bundle extras1 = new Bundle();
	extras1.putString(ID, questionId);
	Log.d("FirstID",questionId);
	Log.d("SecondID",extras1.getString(CustomPushReceiver.ID));
	extras1.putString(text, qText);
	extras1.putInt(nbrAnswers, nbrA);
	extras1.putString(A1, answer1);
	extras1.putString(A2, answer2);
	extras1.putString(A3, answer3);
	extras1.putString(A4, answer4);
	extras1.putString(A5, answer5);
	extras1.putInt(interest,1);
	extras1.putInt(nID,notificationId);
	extras1.putString(tag,notificationTag);
	extras1.putInt(radius, r);
	extras1.putBoolean(international, inter);
	extras1.putBoolean(around, arnd);
	extras1.putString(askerUsername, askerU);
	quickIntent1.putExtras(extras1);
	Log.d("ThirdID",quickIntent1.getExtras().getString(CustomPushReceiver.ID));
	final PendingIntent btPendingIntent1 = PendingIntent.getBroadcast(ctx, 11, quickIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
	
	Intent quickIntent0 = new Intent(ctx, InterestNotificationActivity.class);
	Bundle extras0 = new Bundle();
	extras0.putString(ID, questionId);
	extras0.putString(text, qText);
	extras0.putInt(nbrAnswers, nbrA);
	extras0.putString(A1, answer1);
	extras0.putString(A2, answer2);
	extras0.putString(A3, answer3);
	extras0.putString(A4, answer4);
	extras0.putString(A5, answer5);
	extras0.putInt(interest,0);
	extras0.putInt(nID,notificationId);
	extras0.putString(tag,notificationTag);
	extras0.putInt(radius, r);
	extras0.putBoolean(international, inter);
	extras0.putBoolean(around, arnd);
	extras0.putString(askerUsername, askerU);
	quickIntent0.putExtras(extras0);
	final PendingIntent btPendingIntent0 = PendingIntent.getBroadcast(ctx, 10, quickIntent0, PendingIntent.FLAG_UPDATE_CURRENT);
	
	final NotificationCompat.Builder mBuilder =
	        new NotificationCompat.Builder(ctx)
	        .setSmallIcon(R.drawable.ic_stat_question)
	        .setTicker("New question to approve!")
	        .setContentTitle("Your Approval is needed")
	        .setContentText(qText)
	        .setStyle(new NotificationCompat.BigTextStyle().bigText(qText + extras1.getString(ID) + answer1 + "  " + answer2 + "  " + answer3 + "  " + answer4 + "  " + answer5))
			.addAction(R.drawable.ic_stat_question, "Interesting", btPendingIntent1)
			.addAction(R.drawable.ic_stat_question, "Not Interesting", btPendingIntent0);
	
	countDownTimerI = new CountDownTimer(31000, 5000) {

	     public void onTick(long millisUntilFinished) {
	    	 
	    	 if (millisUntilFinished>28000) {
	    				
	    				mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
	    				// The stack builder object will contain an artificial back stack for the
	    				// started Activity.
	    				// This ensures that navigating backward from the Activity leads out of
	    				// your application to the Home screen.
	    				TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
	    				// Adds the back stack for the Intent (but not the Intent itself)
	    				stackBuilder.addParentStack(Interested.class);
	    				// Adds the Intent that starts the Activity to the top of the stack
	    				stackBuilder.addNextIntent(interestIntent);
	    				PendingIntent interestPendingIntent =
	    						stackBuilder.getPendingIntent(
	    							0,
	    							PendingIntent.FLAG_UPDATE_CURRENT
	    						);
	    				mBuilder.setContentIntent(interestPendingIntent);
	    				NotificationManager mNotificationManager =
	    					(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
	    				// notificationId allows you to update the notification later on.
	    				mNotificationManager.notify(notificationTag, notificationId, mBuilder.build());
	    	 } else {
	    	 
	    		mBuilder.setTicker(String.valueOf((int) (millisUntilFinished / 1000)));
	    			
	    	 		switch((int) (millisUntilFinished / 1000)) {
	    	 		case 25:
	    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
	    	 		break;
	    	 		case 20:
	    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
		    	 	break;
	    	 		case 15:
	    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
		    	 	break;
	    	 		case 10:
	    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
		    	 	break;
		    	 	default: 
		    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
	    	 		}
	    	 
	    	 		mBuilder.setVibrate(new long[] { 0, 150});
	    			// The stack builder object will contain an artificial back stack for the
	    			// started Activity.
	    			// This ensures that navigating backward from the Activity leads out of
	    			// your application to the Home screen.
	    			TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
	    			// Adds the back stack for the Intent (but not the Intent itself)
	    			stackBuilder.addParentStack(Interested.class);
	    			// Adds the Intent that starts the Activity to the top of the stack
	    			stackBuilder.addNextIntent(interestIntent);
	    			PendingIntent interestPendingIntent =
	    					stackBuilder.getPendingIntent(
	    						0,
	    						PendingIntent.FLAG_UPDATE_CURRENT
	    					);
	    			mBuilder.setContentIntent(interestPendingIntent);
	    			NotificationManager mNotificationManager =
	    				(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
	    			// notificationId allows you to update the notification later on.
	    			mNotificationManager.notify(notificationTag, notificationId, mBuilder.build());
	    	 }
	     }

	     public void onFinish() {
	    	 
	    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
	 		manager.cancel(notificationTag, notificationId);
	    			
	     }
	  }.start();
	
	
  };
  
  private void notifyForAnswer(final Context ctx, Intent i, JSONObject json)
		  throws JSONException
		  {
			String questionId = json.getString("questionID");
			final String notificationTag = questionId + "_Answer";
			final Integer notificationId = 2;
			final String qText = json.getString("questionText");
			Integer nbrA = json.getInt("nbrAnswers");
			final String answer1 = json.getString("answer1");
			final String answer2 = json.getString("answer2");
			final String answer3 = json.getString("answer3");
			final String answer4 = json.getString("answer4");
			final String answer5 = json.getString("answer5");
			String askerU = json.getString("askerUsername");
			Boolean inter = json.getBoolean("international");
			Integer r = json.getInt("radius");
			Boolean arnd = json.getBoolean("around");
			
			// Creates an explicit intent for an Activity in your app
			final Intent answerIntent = new Intent(ctx, Answer.class);
			answerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle extras = new Bundle();
			extras.putString(ID, questionId);
			extras.putString(text, qText);
			extras.putInt(nbrAnswers, nbrA);
			extras.putString(A1, answer1);
			extras.putString(A2, answer2);
			extras.putString(A3, answer3);
			extras.putString(A4, answer4);
			extras.putString(A5, answer5);
			extras.putInt(nID,notificationId);
			extras.putString(tag,notificationTag);
			extras.putInt(radius, r);
			extras.putBoolean(international, inter);
			extras.putBoolean(around, arnd);
			extras.putString(askerUsername, askerU);
			answerIntent.putExtras(extras);
			
			Intent quickIntent1 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras1 = new Bundle();
			extras1.putString(ID, questionId);
			Log.d("FirstID",questionId);
			Log.d("SecondID",extras1.getString(CustomPushReceiver.ID));
			extras1.putString(text, qText);
			extras1.putInt(nbrAnswers, nbrA);
			extras1.putString(A1, answer1);
			extras1.putString(A2, answer2);
			extras1.putString(A3, answer3);
			extras1.putString(A4, answer4);
			extras1.putString(A5, answer5);
			extras1.putInt(aID,1);
			extras1.putInt(nID,notificationId);
			extras1.putString(tag,notificationTag);
			extras1.putInt(radius, r);
			extras1.putBoolean(international, inter);
			extras1.putBoolean(around, arnd);
			extras1.putString(askerUsername, askerU);
			quickIntent1.putExtras(extras1);
			Log.d("ThirdID",quickIntent1.getExtras().getString(CustomPushReceiver.ID));
			final PendingIntent btPendingIntent1 = PendingIntent.getBroadcast(ctx, 21, quickIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent2 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras2 = new Bundle();
			extras2.putString(ID, questionId);
			extras2.putString(text, qText);
			extras2.putInt(nbrAnswers, nbrA);
			extras2.putString(A1, answer1);
			extras2.putString(A2, answer2);
			extras2.putString(A3, answer3);
			extras2.putString(A4, answer4);
			extras2.putString(A5, answer5);
			extras2.putInt(aID,2);
			extras2.putInt(nID,notificationId);
			extras2.putString(tag,notificationTag);
			extras2.putInt(radius, r);
			extras2.putBoolean(international, inter);
			extras2.putBoolean(around, arnd);
			extras2.putString(askerUsername, askerU);
			quickIntent2.putExtras(extras2);
			final PendingIntent btPendingIntent2 = PendingIntent.getBroadcast(ctx, 22, quickIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent3 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras3 = new Bundle();
			extras3.putString(ID, questionId);
			extras3.putString(text, qText);
			extras3.putInt(nbrAnswers, nbrA);
			extras3.putString(A1, answer1);
			extras3.putString(A2, answer2);
			extras3.putString(A3, answer3);
			extras3.putString(A4, answer4);
			extras3.putString(A5, answer5);
			extras3.putInt(aID,3);
			extras3.putInt(nID,notificationId);
			extras3.putString(tag,notificationTag);
			extras3.putInt(radius, r);
			extras3.putBoolean(international, inter);
			extras3.putBoolean(around, arnd);
			extras3.putString(askerUsername, askerU);
			quickIntent3.putExtras(extras3);
			final PendingIntent btPendingIntent3 = PendingIntent.getBroadcast(ctx, 23, quickIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent4 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras4 = new Bundle();
			extras4.putString(ID, questionId);
			extras4.putString(text, qText);
			extras4.putInt(nbrAnswers, nbrA);
			extras4.putString(A1, answer1);
			extras4.putString(A2, answer2);
			extras4.putString(A3, answer3);
			extras4.putString(A4, answer4);
			extras4.putString(A5, answer5);
			extras4.putInt(aID,4);
			extras4.putInt(nID,notificationId);
			extras4.putString(tag,notificationTag);
			extras4.putInt(radius, r);
			extras4.putBoolean(international, inter);
			extras4.putBoolean(around, arnd);
			extras4.putString(askerUsername, askerU);
			quickIntent4.putExtras(extras4);
			final PendingIntent btPendingIntent4 = PendingIntent.getBroadcast(ctx, 24, quickIntent4, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent5 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras5 = new Bundle();
			extras5.putString(ID, questionId);
			extras5.putString(text, qText);
			extras5.putInt(nbrAnswers, nbrA);
			extras5.putString(A1, answer1);
			extras5.putString(A2, answer2);
			extras5.putString(A3, answer3);
			extras5.putString(A4, answer4);
			extras5.putString(A5, answer5);
			extras5.putInt(aID,5);
			extras5.putInt(nID,notificationId);
			extras5.putString(tag,notificationTag);
			extras5.putBoolean(international, inter);
			extras5.putBoolean(around, arnd);
			extras5.putString(askerUsername, askerU);
			quickIntent5.putExtras(extras5);
			final PendingIntent btPendingIntent5 = PendingIntent.getBroadcast(ctx, 25, quickIntent5, PendingIntent.FLAG_UPDATE_CURRENT);
			
			final NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(ctx)
			        .setSmallIcon(R.drawable.ic_stat_question)
			        .setTicker("New question!")
			        .setContentTitle("Your Opinion is needed")
			        .setContentText(qText)
			        .setStyle(new NotificationCompat.BigTextStyle().bigText(qText))
					.addAction(R.drawable.ic_stat_question, answer1, btPendingIntent1)
					.addAction(R.drawable.ic_stat_question, answer2, btPendingIntent2);
			
			switch (nbrA) {
			case 2:
				
				countDownTimerA = new CountDownTimer(61000, 10000) {

				     public void onTick(long millisUntilFinished) {
				    	 
				    	 if (millisUntilFinished>58000) {
								
								mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
								// The stack builder object will contain an artificial back stack for the
										// started Activity.
										// This ensures that navigating backward from the Activity leads out of
										// your application to the Home screen.
										TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(ctx);
										// Adds the back stack for the Intent (but not the Intent itself)
										stackBuilder2.addParentStack(Answer.class);
										// Adds the Intent that starts the Activity to the top of the stack
										stackBuilder2.addNextIntent(answerIntent);
										PendingIntent answerPendingIntent2 =
												stackBuilder2.getPendingIntent(
													0,
													PendingIntent.FLAG_UPDATE_CURRENT
												);
										mBuilder.setContentIntent(answerPendingIntent2);
										NotificationManager mNotificationManager2 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager2.notify(notificationTag, notificationId, mBuilder.build());
						} else {
				    	 
						        mBuilder.setTicker(String.valueOf((int) (millisUntilFinished / 1000)));
								
						    	 switch((int) (millisUntilFinished / 1000)) {
					    	 		case 50:
					    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
					    	 		break;
					    	 		case 40:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
						    	 	break;
					    	 		case 30:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
						    	 	break;
					    	 		case 20:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
						    	 	break;
						    	 	default: 
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
					    	 		}
				    	 
				    	 		mBuilder.setVibrate(new long[] { 0, 150});
								// The stack builder object will contain an artificial back stack for the
										// started Activity.
										// This ensures that navigating backward from the Activity leads out of
										// your application to the Home screen.
										TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(ctx);
										// Adds the back stack for the Intent (but not the Intent itself)
										stackBuilder2.addParentStack(Answer.class);
										// Adds the Intent that starts the Activity to the top of the stack
										stackBuilder2.addNextIntent(answerIntent);
										PendingIntent answerPendingIntent2 =
												stackBuilder2.getPendingIntent(
													0,
													PendingIntent.FLAG_UPDATE_CURRENT
												);
										mBuilder.setContentIntent(answerPendingIntent2);
										NotificationManager mNotificationManager2 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager2.notify(notificationTag, notificationId, mBuilder.build());
						}
				     }

				     public void onFinish() {
				    	 
				    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 		manager.cancel(notificationTag, notificationId);
				    			
				     }
				  }.start();
						
				break;
				
			case 3:	
				
				countDownTimerA = new CountDownTimer(41000, 10000) {

				     public void onTick(long millisUntilFinished) {
				    	 
				    	 if (millisUntilFinished>38000) {
				    	 
			 					mBuilder.addAction(R.drawable.ic_stat_question, answer3, btPendingIntent3);
				 				
				 				mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
				 				// The stack builder object will contain an artificial back stack for the
				 						// started Activity.
				 						// This ensures that navigating backward from the Activity leads out of
				 						// your application to the Home screen.
				 						TaskStackBuilder stackBuilder3 = TaskStackBuilder.create(ctx);
				 						// Adds the back stack for the Intent (but not the Intent itself)
				 						stackBuilder3.addParentStack(Answer.class);
				 						// Adds the Intent that starts the Activity to the top of the stack
				 						stackBuilder3.addNextIntent(answerIntent);
				 						PendingIntent answerPendingIntent3 =
				 								stackBuilder3.getPendingIntent(
				 									0,
				 									PendingIntent.FLAG_UPDATE_CURRENT
				 								);
				 						mBuilder.setContentIntent(answerPendingIntent3);
				 						NotificationManager mNotificationManager3 =
				 							(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 						// notificationId allows you to update the notification later on.
				 						mNotificationManager3.notify(notificationTag, notificationId, mBuilder.build());
				    		 
				    	 } else { 
				    	 
				    		 	mBuilder.setTicker(String.valueOf((int) (millisUntilFinished / 1000)));
								
						    	 switch((int) (millisUntilFinished / 1000)) {
					    	 		case 50:
					    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
					    	 		break;
					    	 		case 40:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
						    	 	break;
					    	 		case 30:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
						    	 	break;
					    	 		case 20:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
						    	 	break;
						    	 	default: 
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
					    	 		}
				    	 
				    	 		mBuilder.setVibrate(new long[] { 0, 100});
								// The stack builder object will contain an artificial back stack for the
										// started Activity.
										// This ensures that navigating backward from the Activity leads out of
										// your application to the Home screen.
										TaskStackBuilder stackBuilder3 = TaskStackBuilder.create(ctx);
										// Adds the back stack for the Intent (but not the Intent itself)
										stackBuilder3.addParentStack(Answer.class);
										// Adds the Intent that starts the Activity to the top of the stack
										stackBuilder3.addNextIntent(answerIntent);
										PendingIntent answerPendingIntent3 =
												stackBuilder3.getPendingIntent(
													0,
													PendingIntent.FLAG_UPDATE_CURRENT
												);
										mBuilder.setContentIntent(answerPendingIntent3);
										NotificationManager mNotificationManager3 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager3.notify(notificationTag, notificationId, mBuilder.build());
				    	 }
				     }

				     public void onFinish() {
				    	 
				    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 		manager.cancel(notificationTag, notificationId);
				    			
				     }
				  }.start();
						
				break;
			case 4:
						
						countDownTimerA = new CountDownTimer(41000, 10000) {

						     public void onTick(long millisUntilFinished) {
						    	 
						    	 if (millisUntilFinished>38000) {
						    	 
						    		 	mBuilder.addAction(R.drawable.ic_stat_question, answer3, btPendingIntent3);
										mBuilder.addAction(R.drawable.ic_stat_question, answer4, btPendingIntent4);
										
										mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
										// The stack builder object will contain an artificial back stack for the
												// started Activity.
												// This ensures that navigating backward from the Activity leads out of
												// your application to the Home screen.
												TaskStackBuilder stackBuilder4 = TaskStackBuilder.create(ctx);
												// Adds the back stack for the Intent (but not the Intent itself)
												stackBuilder4.addParentStack(Answer.class);
												// Adds the Intent that starts the Activity to the top of the stack
												stackBuilder4.addNextIntent(answerIntent);
												PendingIntent answerPendingIntent4 =
														stackBuilder4.getPendingIntent(
															0,
															PendingIntent.FLAG_UPDATE_CURRENT
														);
												mBuilder.setContentIntent(answerPendingIntent4);
												NotificationManager mNotificationManager4 =
													(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
												// notificationId allows you to update the notification later on.
												mNotificationManager4.notify(notificationTag, notificationId, mBuilder.build());
												
						    	 } else {
						    	 
						    	
									     mBuilder.setTicker(String.valueOf((int) (millisUntilFinished / 1000)));
										
								    	 switch((int) (millisUntilFinished / 1000)) {
							    	 		case 50:
							    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
							    	 		break;
							    	 		case 40:
								    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
								    	 	break;
							    	 		case 30:
								    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
								    	 	break;
							    	 		case 20:
								    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
								    	 	break;
								    	 	default: 
								    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
							    	 		}
						    	 
						    	 		mBuilder.setVibrate(new long[] { 0, 100});
										// The stack builder object will contain an artificial back stack for the
												// started Activity.
												// This ensures that navigating backward from the Activity leads out of
												// your application to the Home screen.
												TaskStackBuilder stackBuilder4 = TaskStackBuilder.create(ctx);
												// Adds the back stack for the Intent (but not the Intent itself)
												stackBuilder4.addParentStack(Answer.class);
												// Adds the Intent that starts the Activity to the top of the stack
												stackBuilder4.addNextIntent(answerIntent);
												PendingIntent answerPendingIntent4 =
														stackBuilder4.getPendingIntent(
															0,
															PendingIntent.FLAG_UPDATE_CURRENT
														);
												mBuilder.setContentIntent(answerPendingIntent4);
												NotificationManager mNotificationManager4 =
													(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
												// notificationId allows you to update the notification later on.
												mNotificationManager4.notify(notificationTag, notificationId, mBuilder.build());
						    	 }
						     }

						     public void onFinish() {
						    	 
						    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
						 		manager.cancel(notificationTag, notificationId);
						    			
						     }
						  }.start();
						
				break;
			case 5:
				
				countDownTimerA = new CountDownTimer(41000, 10000) {

				     public void onTick(long millisUntilFinished) {
				    	 
				    	 if (millisUntilFinished>38000) {
				    	 
				    		 	mBuilder.addAction(R.drawable.ic_stat_question, answer3, btPendingIntent3);
				    		 	mBuilder.addAction(R.drawable.ic_stat_question, answer4, btPendingIntent4);
				 				mBuilder.addAction(R.drawable.ic_stat_question, answer5, btPendingIntent5);
				 			
				 			mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
				 			// The stack builder object will contain an artificial back stack for the
				 				// started Activity.
				 				// This ensures that navigating backward from the Activity leads out of
				 				// your application to the Home screen.
				 				TaskStackBuilder stackBuilder5 = TaskStackBuilder.create(ctx);
				 				// Adds the back stack for the Intent (but not the Intent itself)
				 				stackBuilder5.addParentStack(Answer.class);
				 				// Adds the Intent that starts the Activity to the top of the stack
				 				stackBuilder5.addNextIntent(answerIntent);
				 				PendingIntent answerPendingIntent5 =
				 						stackBuilder5.getPendingIntent(
				 							0,
				 							PendingIntent.FLAG_UPDATE_CURRENT
				 						);
				 				mBuilder.setContentIntent(answerPendingIntent5);
				 				NotificationManager mNotificationManager5 =
				 					(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 				// notificationId allows you to update the notification later on.
				 				mNotificationManager5.notify(notificationTag, notificationId, mBuilder.build());
				    	 
				    	 } else {
				    	 
				    	
				    		 	mBuilder.setTicker(String.valueOf((int) (millisUntilFinished / 1000)));
				 			
						    	 switch((int) (millisUntilFinished / 1000)) {
					    	 		case 50:
					    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
					    	 		break;
					    	 		case 40:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
						    	 	break;
					    	 		case 30:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
						    	 	break;
					    	 		case 20:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
						    	 	break;
						    	 	default: 
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
					    	 		}
				    	 
				    	 		mBuilder.setVibrate(new long[] { 0, 100});
				 			// The stack builder object will contain an artificial back stack for the
				 				// started Activity.
				 				// This ensures that navigating backward from the Activity leads out of
				 				// your application to the Home screen.
				 				TaskStackBuilder stackBuilder5 = TaskStackBuilder.create(ctx);
				 				// Adds the back stack for the Intent (but not the Intent itself)
				 				stackBuilder5.addParentStack(Answer.class);
				 				// Adds the Intent that starts the Activity to the top of the stack
				 				stackBuilder5.addNextIntent(answerIntent);
				 				PendingIntent answerPendingIntent5 =
				 						stackBuilder5.getPendingIntent(
				 							0,
				 							PendingIntent.FLAG_UPDATE_CURRENT
				 						);
				 				mBuilder.setContentIntent(answerPendingIntent5);
				 				NotificationManager mNotificationManager5 =
				 					(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 				// notificationId allows you to update the notification later on.
				 				mNotificationManager5.notify(notificationTag, notificationId, mBuilder.build());
				    	 }
				     }

				     public void onFinish() {
				    	 
				    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 		manager.cancel(notificationTag, notificationId);
				    			
				     }
				  }.start();
				
			break;
			}
			
		  };
		  
  private void notifyForResults(Context ctx, Intent i, JSONObject json)
		  throws JSONException
		  {
			String questionId = json.getString("questionID");
			String notificationTag = questionId + "_Results";
			Integer notificationId = 3;
			String qText = json.getString("questionText");
			Integer nbrA = json.getInt("nbrAnswers");
			Integer nAn = json.getInt("nA");
			String answer1 = json.getString("answer1");
			String answer2 = json.getString("answer2");
			String answer3 = json.getString("answer3");
			String answer4 = json.getString("answer4");
			String answer5 = json.getString("answer5");
			Integer nAnswer1 = json.getInt("nA1");
			Integer nAnswer2 = json.getInt("nA2");
			Integer nAnswer3 = json.getInt("nA3");
			Integer nAnswer4 = json.getInt("nA4");
			Integer nAnswer5 = json.getInt("nA5");
			Double pcAnswer1 = json.getDouble("pcA1");
			Double pcAnswer2 = json.getDouble("pcA2");
			Double pcAnswer3 = json.getDouble("pcA3");
			Double pcAnswer4 = json.getDouble("pcA4");
			Double pcAnswer5 = json.getDouble("pcA5");
			String askerU = json.getString("askerUsername");
			Boolean inter = json.getBoolean("international");
			Integer r = json.getInt("radius");
			Boolean arnd = json.getBoolean("around");
			
			// Creates an explicit intent for an Activity in your app
			Intent resultsIntent = new Intent(ctx, Results.class);
			resultsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle extras = new Bundle();
			extras.putString(ID, questionId);
			extras.putString(text, qText);
			extras.putInt(nbrAnswers, nbrA);
			extras.putString(A1, answer1);
			extras.putString(A2, answer2);
			extras.putString(A3, answer3);
			extras.putString(A4, answer4);
			extras.putString(A5, answer5);
			extras.putInt(nA, nAn);
			extras.putInt(nA1, nAnswer1);
			extras.putInt(nA2, nAnswer2);
			extras.putInt(nA3, nAnswer3);
			extras.putInt(nA4, nAnswer4);
			extras.putInt(nA5, nAnswer5);
			extras.putDouble(pcA1, pcAnswer1);
			extras.putDouble(pcA2, pcAnswer2);
			extras.putDouble(pcA3, pcAnswer3);
			extras.putDouble(pcA4, pcAnswer4);
			extras.putDouble(pcA5, pcAnswer5);
			extras.putInt(nID,notificationId);
			extras.putString(tag,notificationTag);
			extras.putInt(radius, r);
			extras.putBoolean(international, inter);
			extras.putBoolean(around, arnd);
			extras.putString(askerUsername, askerU);
			resultsIntent.putExtras(extras);
			
			Intent quickIntent = new Intent(ctx, ResultsNotificationActivity.class);
			Bundle extras1 = new Bundle();
			extras1.putString(ID, questionId);
			extras1.putString(text, qText);
			extras1.putInt(nbrAnswers, nbrA);
			extras1.putString(A1, answer1);
			extras1.putString(A2, answer2);
			extras1.putString(A3, answer3);
			extras1.putString(A4, answer4);
			extras1.putString(A5, answer5);
			extras1.putInt(nA, nAn);
			extras1.putInt(nA1, nAnswer1);
			extras1.putInt(nA2, nAnswer2);
			extras1.putInt(nA3, nAnswer3);
			extras1.putInt(nA4, nAnswer4);
			extras1.putInt(nA5, nAnswer5);
			extras1.putDouble(pcA1, pcAnswer1);
			extras1.putDouble(pcA2, pcAnswer2);
			extras1.putDouble(pcA3, pcAnswer3);
			extras1.putDouble(pcA4, pcAnswer4);
			extras1.putDouble(pcA5, pcAnswer5);
			extras1.putInt(nID,notificationId);
			extras1.putString(tag,notificationTag);
			extras1.putInt(radius, r);
			extras1.putBoolean(international, inter);
			extras1.putBoolean(around, arnd);
			extras1.putString(askerUsername, askerU);
			quickIntent.putExtras(extras1);
			PendingIntent btPendingIntent = PendingIntent.getBroadcast(ctx, 30, quickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(ctx)
		        .setSmallIcon(R.drawable.ic_stat_question)
		        .setTicker("The results are in!")
		        .setContentTitle("The results are in!")
		        .setContentText(qText)
		        .setStyle(new NotificationCompat.BigTextStyle().bigText(qText))
				.addAction(R.drawable.ic_stat_question, "Save Results", btPendingIntent);
			
			mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(Results.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultsIntent);
			PendingIntent resultsPendingIntent =
					stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
					);
			mBuilder.setContentIntent(resultsPendingIntent);
			NotificationManager mNotificationManager =
				(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			// notificationId allows you to update the notification later on.
			mNotificationManager.notify(notificationTag, notificationId, mBuilder.build());
		  };	  

}
  
  