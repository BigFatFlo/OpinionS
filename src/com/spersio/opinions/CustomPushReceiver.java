package com.spersio.opinions;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

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
  public static final String subscribersOnly = "com.spersio.opinion.subscribersOnly";
  public static final String international = "com.spersio.opinion.international";
  public static final String around = "com.spersio.opinion.around";
  public static final String radius = "com.spersio.opinion.radius";
  public static final String askerUsername = "com.spersio.opinion.askerUsername";
  public static final String approval = "com.spersio.opinion.approval";
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
  public static final String saveOrSubscribe = "com.spersio.opinion.action";
  public static final String savedQuestion = "com.spersio.opinion.savedQuestion";
  public static final String timeLeft = "com.spersio.opinion.timeLeft";
  public static final String createdAt = "com.spersio.opinion.createdAt";
  public static final String delete = "com.spersio.opinion.delete";
  public static final String country = "com.spersio.opinion.country";
  public static CountDownTimer countDownTimerAp= null;
  public static CountDownTimer countDownTimerA= null;

@Override
	public void onPushReceive(Context context, Intent intent) {
	try {
			//ParseAnalytics.trackAppOpened(intent);
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			String action = json.getString("action");
			/*String questionId = json.getString("questionID");	
			Intent i = new Intent(context, Interested.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	    	i.putExtra(ID, questionId);
	    	context.startActivity(i);*/
			Log.d("notification", "received");
			switch (action) {
				case "com.spersio.opinion.ANSWER":
					notifyForAnswer(context, intent, json);
				break;
				case "com.spersio.opinion.RESULTS":
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
  
  @SuppressLint("NewApi") 
  private void notifyForAnswer(final Context ctx, Intent i, JSONObject json)
		  throws JSONException
		  {
	  
	  		final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	  
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
			final Bundle extras = new Bundle();
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
			
			Intent quickIntent1 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras1 = new Bundle();
			extras1.putString(ID, questionId);
			extras1.putInt(aID,1);
			extras1.putInt(nID,notificationId);
			extras1.putString(tag,notificationTag);
			quickIntent1.putExtras(extras1);
			final PendingIntent btPendingIntent1 = PendingIntent.getBroadcast(ctx, 21, quickIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent2 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras2 = new Bundle();
			extras2.putString(ID, questionId);
			extras2.putInt(aID,2);
			extras2.putInt(nID,notificationId);
			extras2.putString(tag,notificationTag);
			quickIntent2.putExtras(extras2);
			final PendingIntent btPendingIntent2 = PendingIntent.getBroadcast(ctx, 22, quickIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent3 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras3 = new Bundle();
			extras3.putString(ID, questionId);
			extras3.putInt(aID,3);
			extras3.putInt(nID,notificationId);
			extras3.putString(tag,notificationTag);
			quickIntent3.putExtras(extras3);
			final PendingIntent btPendingIntent3 = PendingIntent.getBroadcast(ctx, 23, quickIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent4 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras4 = new Bundle();
			extras4.putString(ID, questionId);
			extras4.putInt(aID,4);
			extras4.putInt(nID,notificationId);
			extras4.putString(tag,notificationTag);
			quickIntent4.putExtras(extras4);
			final PendingIntent btPendingIntent4 = PendingIntent.getBroadcast(ctx, 24, quickIntent4, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent5 = new Intent(ctx, AnswerNotificationActivity.class);
			final Bundle extras5 = new Bundle();
			extras5.putString(ID, questionId);
			extras5.putInt(aID,5);
			extras5.putInt(nID,notificationId);
			extras5.putString(tag,notificationTag);;
			quickIntent5.putExtras(extras5);
			final PendingIntent btPendingIntent5 = PendingIntent.getBroadcast(ctx, 25, quickIntent5, PendingIntent.FLAG_UPDATE_CURRENT);
			
			final RemoteViews remoteView = new RemoteViews("com.spersio.opinion", R.layout.custom_notification);
			
			remoteView.setTextViewText(R.id.question_notif, qText);
			
			remoteView.setViewVisibility(R.id.approve_button_notif, View.GONE);
			remoteView.setViewVisibility(R.id.reject_button_notif, View.GONE);
			
			final NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(ctx)
			        .setSmallIcon(R.drawable.ic_stat_question)
			        .setTicker("60s to answer new question!")
			        .setContentTitle(askerU + " needs your Opinion")
			        .setContentText(qText);
			
			Intent deleteIntent = new Intent(ctx, DeleteNotificationActivity.class);
			final PendingIntent deletePendingIntent = PendingIntent.getBroadcast(ctx, 29, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setDeleteIntent(deletePendingIntent);
			
			switch (nbrA) {
			case 2:
				
				remoteView.setTextViewText(R.id.answer1_notif,answer1);
				remoteView.setTextViewText(R.id.answer2_notif,answer2);
				remoteView.setViewVisibility(R.id.answer3_notif, View.GONE);
				remoteView.setViewVisibility(R.id.answer4_notif, View.GONE);
				remoteView.setViewVisibility(R.id.answer5_notif, View.GONE);
				remoteView.setViewVisibility(R.id.three_notif, View.GONE);
				remoteView.setViewVisibility(R.id.four_notif, View.GONE);
				remoteView.setViewVisibility(R.id.five_notif, View.GONE);
				remoteView.setViewVisibility(R.id.three_button_notif, View.GONE);
				remoteView.setViewVisibility(R.id.four_button_notif, View.GONE);
				remoteView.setViewVisibility(R.id.five_button_notif, View.GONE);
				
				remoteView.setOnClickPendingIntent(R.id.one_button_notif, btPendingIntent1);
				remoteView.setOnClickPendingIntent(R.id.two_button_notif, btPendingIntent2);
				
				
				countDownTimerA = new CountDownTimer(60800, 1000) {

				     public void onTick(long millisUntilFinished) {
				    	 
				    	extras.putLong(timeLeft, millisUntilFinished);
						answerIntent.putExtras(extras);
				    	 
				    	 if ((int) (millisUntilFinished/1000) == 60) {
								
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
										Notification notification = mBuilder.build();
					    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					    				notification.bigContentView = remoteView;
					    				}
										NotificationManager mNotificationManager2 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager2.notify(notificationTag, notificationId, notification);
						} else {
				    	 
						    	 switch((int) (millisUntilFinished / 1000)) {
					    	 		case 50:
					    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
								        mBuilder.setTicker("50s");
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 		break;
					    	 		case 40:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
								        mBuilder.setTicker("40s");
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
					    	 		case 30:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
								        mBuilder.setTicker("30s");
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
					    	 		case 20:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
								        mBuilder.setTicker("20s");
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
					    	 		case 10:
					    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
								        mBuilder.setTicker("10s");
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
						    	 	default: 
						    	 		mBuilder.setVibrate(new long[] { 0, 0});
					    	 		}
				    	 
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
										Notification notification = mBuilder.build();
					    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					    				notification.bigContentView = remoteView;
					    				}
										NotificationManager mNotificationManager2 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager2.notify(notificationTag, notificationId, notification);
						}
				     }

				     public void onFinish() {
				    	 
				    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 		manager.cancel(notificationTag, notificationId);
				    			
				     }
				  }.start();
						
				break;
				
			case 3:	
				
				remoteView.setTextViewText(R.id.answer1_notif,answer1);
				remoteView.setTextViewText(R.id.answer2_notif,answer2);
				remoteView.setTextViewText(R.id.answer3_notif,answer3);
				remoteView.setViewVisibility(R.id.answer4_notif, View.GONE);
				remoteView.setViewVisibility(R.id.answer5_notif, View.GONE);
				remoteView.setViewVisibility(R.id.four_notif, View.GONE);
				remoteView.setViewVisibility(R.id.five_notif, View.GONE);
				remoteView.setViewVisibility(R.id.four_button_notif, View.GONE);
				remoteView.setViewVisibility(R.id.five_button_notif, View.GONE);
				
				remoteView.setOnClickPendingIntent(R.id.one_button_notif, btPendingIntent1);
				remoteView.setOnClickPendingIntent(R.id.two_button_notif, btPendingIntent2);
				remoteView.setOnClickPendingIntent(R.id.three_button_notif, btPendingIntent3);
				
				countDownTimerA = new CountDownTimer(60800, 1000) {

				     public void onTick(long millisUntilFinished) {
				    	 
				    	 	extras.putLong(timeLeft, millisUntilFinished);
							answerIntent.putExtras(extras);
					    	 
					    	 if ((int) (millisUntilFinished/1000) == 60) {
				 				
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
				 						Notification notification = mBuilder.build();
					    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					    				notification.bigContentView = remoteView;
					    				}
				 						NotificationManager mNotificationManager3 =
				 							(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 						// notificationId allows you to update the notification later on.
				 						mNotificationManager3.notify(notificationTag, notificationId, notification);
				    		 
				    	 } else { 
				    	 
				    		 switch((int) (millisUntilFinished / 1000)) {
				    	 		case 50:
				    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
							        mBuilder.setTicker("50s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
				    	 		break;
				    	 		case 40:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
							        mBuilder.setTicker("40s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 30:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
							        mBuilder.setTicker("30s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 20:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
							        mBuilder.setTicker("20s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 10:
				    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
							        mBuilder.setTicker("10s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
					    	 	default: 
					    	 		mBuilder.setVibrate(new long[] { 0, 0});
				    	 		}
				    		 
				    		 
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
										Notification notification = mBuilder.build();
					    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					    				notification.bigContentView = remoteView;
					    				}
										NotificationManager mNotificationManager3 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager3.notify(notificationTag, notificationId, notification);
				    	 }
				     }

				     public void onFinish() {
				    	 
				    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 		manager.cancel(notificationTag, notificationId);
				    			
				     }
				  }.start();
						
				break;
			case 4:
						
				remoteView.setTextViewText(R.id.answer1_notif,answer1);
				remoteView.setTextViewText(R.id.answer2_notif,answer2);
				remoteView.setTextViewText(R.id.answer3_notif,answer3);
				remoteView.setTextViewText(R.id.answer4_notif,answer4);
				remoteView.setViewVisibility(R.id.answer5_notif, View.GONE);
				remoteView.setViewVisibility(R.id.five_notif, View.GONE);
				remoteView.setViewVisibility(R.id.five_button_notif, View.GONE);
				
				remoteView.setOnClickPendingIntent(R.id.one_button_notif, btPendingIntent1);
				remoteView.setOnClickPendingIntent(R.id.two_button_notif, btPendingIntent2);
				remoteView.setOnClickPendingIntent(R.id.three_button_notif, btPendingIntent3);
				remoteView.setOnClickPendingIntent(R.id.four_button_notif, btPendingIntent4);
				
				countDownTimerA = new CountDownTimer(60800, 1000) {

				     public void onTick(long millisUntilFinished) {
				    	 
				    	 	extras.putLong(timeLeft, millisUntilFinished);
							answerIntent.putExtras(extras);
					    	 
					    	 if ((int) (millisUntilFinished/1000) == 60) {
								
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
										Notification notification = mBuilder.build();
					    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					    				notification.bigContentView = remoteView;
					    				}
										NotificationManager mNotificationManager4 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager4.notify(notificationTag, notificationId, notification);
										
				    	 } else {
				    	 
				    		 switch((int) (millisUntilFinished / 1000)) {
				    	 		case 50:
				    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
							        mBuilder.setTicker("50s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
				    	 		break;
				    	 		case 40:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
							        mBuilder.setTicker("40s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 30:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
							        mBuilder.setTicker("30s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 20:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
							        mBuilder.setTicker("20s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 10:
				    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
							        mBuilder.setTicker("10s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
					    	 	default: 
					    	 		mBuilder.setVibrate(new long[] { 0, 0});
				    	 		}
				    		 
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
										Notification notification = mBuilder.build();
					    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					    				notification.bigContentView = remoteView;
					    				}
										NotificationManager mNotificationManager4 =
											(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
										// notificationId allows you to update the notification later on.
										mNotificationManager4.notify(notificationTag, notificationId, notification);
				    	 }
				     }

				     public void onFinish() {
				    	 
				    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 		manager.cancel(notificationTag, notificationId);
				    			
				     }
				  }.start();
				
				break;
			case 5:
				
				remoteView.setTextViewText(R.id.answer1_notif,answer1);
				remoteView.setTextViewText(R.id.answer2_notif,answer2);
				remoteView.setTextViewText(R.id.answer3_notif,answer3);
				remoteView.setTextViewText(R.id.answer4_notif,answer4);
				remoteView.setTextViewText(R.id.answer5_notif,answer5);
				
				remoteView.setOnClickPendingIntent(R.id.one_button_notif, btPendingIntent1);
				remoteView.setOnClickPendingIntent(R.id.two_button_notif, btPendingIntent2);
				remoteView.setOnClickPendingIntent(R.id.three_button_notif, btPendingIntent3);
				remoteView.setOnClickPendingIntent(R.id.four_button_notif, btPendingIntent4);
				remoteView.setOnClickPendingIntent(R.id.five_button_notif, btPendingIntent5);
				
				countDownTimerA = new CountDownTimer(60800, 1000) {

				     public void onTick(long millisUntilFinished) {
				    	 
				    	 	extras.putLong(timeLeft, millisUntilFinished);
							answerIntent.putExtras(extras);
					    	 
					    	 if ((int) (millisUntilFinished/1000) == 60) {
				 			
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
				 				Notification notification = mBuilder.build();
			    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			    				notification.bigContentView = remoteView;
			    				}
				 				NotificationManager mNotificationManager5 =
				 					(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 				// notificationId allows you to update the notification later on.
				 				mNotificationManager5.notify(notificationTag, notificationId, notification);
				    	 
				    	 } else {
				    		 
				    		 switch((int) (millisUntilFinished / 1000)) {
				    	 		case 50:
				    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
							        mBuilder.setTicker("50s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
				    	 		break;
				    	 		case 40:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
							        mBuilder.setTicker("40s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 30:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
							        mBuilder.setTicker("30s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 20:
					    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
							        mBuilder.setTicker("20s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
				    	 		case 10:
				    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
							        mBuilder.setTicker("10s");
					    	 		mBuilder.setVibrate(new long[] { 0, 150});
					    	 	break;
					    	 	default: 
					    	 		mBuilder.setVibrate(new long[] { 0, 0});
				    	 		}
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
				 				Notification notification = mBuilder.build();
			    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			    				notification.bigContentView = remoteView;
			    				}
				 				NotificationManager mNotificationManager5 =
				 					(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				 				// notificationId allows you to update the notification later on.
				 				mNotificationManager5.notify(notificationTag, notificationId, notification);
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

@SuppressLint("NewApi") 
  private void notifyForResults(Context ctx, Intent i, JSONObject json)
		  throws JSONException
		  {
			
			final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	
			Log.d("notification", "in notifyForResults");
			
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
			Boolean subscribersO = json.getBoolean("subscribersOnly");
			Boolean inter = json.getBoolean("international");
			Integer r = json.getInt("radius");
			Boolean arnd = json.getBoolean("around");
			String creationDate = json.get("createdAt").toString();
			String countr = json.getString("country");
			
			// Creates an explicit intent for an Activity in your app
			Intent resultsIntent = new Intent(ctx, Results.class);
			resultsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle extras = new Bundle();
			extras.putString(ID, questionId);
			extras.putString(text, qText);
			extras.putString(country, countr);
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
			extras.putBoolean(subscribersOnly, subscribersO);
			extras.putBoolean(international, inter);
			extras.putBoolean(around, arnd);
			extras.putString(askerUsername, askerU);
			extras.putBoolean(savedQuestion, false);
			extras.putString(createdAt, creationDate);
			resultsIntent.putExtras(extras);
			
			Intent quickIntent1 = new Intent(ctx, ResultsNotificationActivity.class);
			Bundle extras1 = new Bundle();
			extras1.putString(ID, questionId);
			extras1.putInt(nID,notificationId);
			extras1.putString(tag,notificationTag);
			extras1.putBoolean(subscribersOnly, subscribersO);
			extras1.putString(askerUsername, askerU);
			extras1.putInt(saveOrSubscribe, 1);
			quickIntent1.putExtras(extras1);
			PendingIntent btPendingIntent1 = PendingIntent.getBroadcast(ctx, 31, quickIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Intent quickIntent2 = new Intent(ctx, ResultsNotificationActivity.class);
			Bundle extras2 = new Bundle();
			extras2.putString(ID, questionId);
			extras2.putInt(nID,notificationId);
			extras2.putString(tag,notificationTag);
			extras2.putBoolean(subscribersOnly, subscribersO);
			extras2.putString(askerUsername, askerU);
			extras2.putInt(saveOrSubscribe, 2);
			quickIntent2.putExtras(extras2);
			PendingIntent btPendingIntent2 = PendingIntent.getBroadcast(ctx, 32, quickIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
			
			final RemoteViews remoteView = new RemoteViews("com.spersio.opinion", R.layout.custom_notification_results);
			
			remoteView.setTextViewText(R.id.question_view_r_notif, qText);
			
			if (subscribersO) {
				remoteView.setImageViewResource(R.id.subscribe_button_notif, R.drawable.button_unsubscribe);
			} else {
				remoteView.setImageViewResource(R.id.subscribe_button_notif, R.drawable.button_subscribe);
			}
			
			switch (nbrA) {
				case 2:
					remoteView.setTextViewText(R.id.answer1_r_notif,answer1);
					remoteView.setTextViewText(R.id.answer2_r_notif,answer2);
					remoteView.setTextViewText(R.id.n_answer1_notif,nAnswer1.toString());
					remoteView.setTextViewText(R.id.n_answer2_notif,nAnswer2.toString());
					remoteView.setTextViewText(R.id.pc1_notif,String.valueOf((double)Math.round(pcAnswer1 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc2_notif,String.valueOf((double)Math.round(pcAnswer2 * 10) / 10) + "%");
					remoteView.setViewVisibility(R.id.n_answer3_notif, View.GONE);
					remoteView.setViewVisibility(R.id.n_answer4_notif, View.GONE);
					remoteView.setViewVisibility(R.id.n_answer5_notif, View.GONE);
					remoteView.setViewVisibility(R.id.pc3_notif, View.GONE);
					remoteView.setViewVisibility(R.id.pc4_notif, View.GONE);
					remoteView.setViewVisibility(R.id.pc5_notif, View.GONE);
					remoteView.setViewVisibility(R.id.answer3_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.answer4_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.answer5_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.three_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.four_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.five_r_notif, View.GONE);
				break;
				case 3:
					remoteView.setTextViewText(R.id.answer1_r_notif,answer1);
					remoteView.setTextViewText(R.id.answer2_r_notif,answer2);
					remoteView.setTextViewText(R.id.answer3_r_notif,answer3);
					remoteView.setTextViewText(R.id.n_answer1_notif,nAnswer1.toString());
					remoteView.setTextViewText(R.id.n_answer2_notif,nAnswer2.toString());
					remoteView.setTextViewText(R.id.n_answer3_notif,nAnswer3.toString());
					remoteView.setTextViewText(R.id.pc1_notif,String.valueOf((double)Math.round(pcAnswer1 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc2_notif,String.valueOf((double)Math.round(pcAnswer2 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc3_notif,String.valueOf((double)Math.round(pcAnswer3 * 10) / 10) + "%");
					remoteView.setViewVisibility(R.id.n_answer4_notif, View.GONE);
					remoteView.setViewVisibility(R.id.n_answer5_notif, View.GONE);
					remoteView.setViewVisibility(R.id.pc4_notif, View.GONE);
					remoteView.setViewVisibility(R.id.pc5_notif, View.GONE);
					remoteView.setViewVisibility(R.id.answer4_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.answer5_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.four_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.five_r_notif, View.GONE);
				break;
				case 4:
					remoteView.setTextViewText(R.id.answer1_r_notif,answer1);
					remoteView.setTextViewText(R.id.answer2_r_notif,answer2);
					remoteView.setTextViewText(R.id.answer3_r_notif,answer3);
					remoteView.setTextViewText(R.id.answer4_r_notif,answer4);
					remoteView.setTextViewText(R.id.n_answer1_notif,nAnswer1.toString());
					remoteView.setTextViewText(R.id.n_answer2_notif,nAnswer2.toString());
					remoteView.setTextViewText(R.id.n_answer3_notif,nAnswer3.toString());
					remoteView.setTextViewText(R.id.n_answer4_notif,nAnswer4.toString());
					remoteView.setTextViewText(R.id.pc1_notif,String.valueOf((double)Math.round(pcAnswer1 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc2_notif,String.valueOf((double)Math.round(pcAnswer2 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc3_notif,String.valueOf((double)Math.round(pcAnswer3 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc4_notif,String.valueOf((double)Math.round(pcAnswer4 * 10) / 10) + "%");
					remoteView.setViewVisibility(R.id.n_answer5_notif, View.GONE);
					remoteView.setViewVisibility(R.id.pc5_notif, View.GONE);
					remoteView.setViewVisibility(R.id.answer5_r_notif, View.GONE);
					remoteView.setViewVisibility(R.id.five_r_notif, View.GONE);
				break;
				case 5:
					remoteView.setTextViewText(R.id.answer1_r_notif,answer1);
					remoteView.setTextViewText(R.id.answer2_r_notif,answer2);
					remoteView.setTextViewText(R.id.answer3_r_notif,answer3);
					remoteView.setTextViewText(R.id.answer4_r_notif,answer4);
					remoteView.setTextViewText(R.id.answer5_r_notif,answer5);
					remoteView.setTextViewText(R.id.n_answer1_notif,nAnswer1.toString());
					remoteView.setTextViewText(R.id.n_answer2_notif,nAnswer2.toString());
					remoteView.setTextViewText(R.id.n_answer3_notif,nAnswer3.toString());
					remoteView.setTextViewText(R.id.n_answer4_notif,nAnswer4.toString());
					remoteView.setTextViewText(R.id.n_answer5_notif,nAnswer5.toString());
					remoteView.setTextViewText(R.id.pc1_notif,String.valueOf((double)Math.round(pcAnswer1 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc2_notif,String.valueOf((double)Math.round(pcAnswer2 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc3_notif,String.valueOf((double)Math.round(pcAnswer3 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc4_notif,String.valueOf((double)Math.round(pcAnswer4 * 10) / 10) + "%");
					remoteView.setTextViewText(R.id.pc5_notif,String.valueOf((double)Math.round(pcAnswer5 * 10) / 10) + "%");
				break;
			}
			
			remoteView.setOnClickPendingIntent(R.id.saveResults_button_notif, btPendingIntent1);
			remoteView.setOnClickPendingIntent(R.id.subscribe_button_notif, btPendingIntent2);
			
			NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(ctx)
		        .setSmallIcon(R.drawable.ic_stat_question)
		        .setTicker("The results are in!")
		        .setContentTitle("The results are in!")
		        .setContentText(qText);
			
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
			Log.d("notification", "builder");
			Notification notification = mBuilder.build();
			if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			notification.bigContentView = remoteView;
			}
			NotificationManager mNotificationManager =
				(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			// notificationId allows you to update the notification later on.
			mNotificationManager.notify(notificationTag, notificationId, notification);
		  };	  

}
  
  