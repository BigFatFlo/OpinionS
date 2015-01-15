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
  public static final String group = "com.spersio.opinion.group";
  public static final String groupname = "com.spersio.opinion.groupname";
  public static final String askerUsername = "com.spersio.opinion.askerUsername";
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
  public static final String saveOrUnsubscribeOrLeave = "com.spersio.opinion.action";
  public static final String savedQuestion = "com.spersio.opinion.savedQuestion";
  public static final String timeLeft = "com.spersio.opinion.timeLeft";
  public static final String createdAt = "com.spersio.opinion.createdAt";
  public static final String delete = "com.spersio.opinion.delete";
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
		Intent launchIntent = new Intent(context, Home.class);
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
			String grpname = json.getString("groupname");
			Boolean grp = json.getBoolean("group");
			Boolean subOnly = json.getBoolean("subscribersOnly");
			
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
			extras.putBoolean(group, grp);
			extras.putBoolean(subscribersOnly, subOnly);
			extras.putString(askerUsername, askerU);
			extras.putString(groupname, grpname);
			
			Intent[] quickIntents = new Intent[5];
			PendingIntent[] btPendingIntents = new PendingIntent[5];

			final Bundle extras1 = new Bundle();
			extras1.putString(ID, questionId);
			extras1.putInt(nID,notificationId);
			extras1.putString(tag,notificationTag);
			
			for (int j = 0; j < 5; j++) {
				quickIntents[j] = new Intent(ctx, AnswerNotificationActivity.class);
				extras1.putInt(aID,j+1);
				quickIntents[j].putExtras(extras1);
				btPendingIntents[j] = PendingIntent.getBroadcast(ctx, 21+j, quickIntents[j], PendingIntent.FLAG_UPDATE_CURRENT);
			}
			
			final RemoteViews remoteView = new RemoteViews("com.spersio.opinions", R.layout.custom_notification);
			
			remoteView.setTextViewText(R.id.question_notif, qText);
			
			final NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(ctx)
			        .setSmallIcon(R.drawable.ic_stat_question)
			        .setTicker(ctx.getResources().getString(R.string.time_to_answer))
			        .setContentTitle(askerU + ctx.getResources().getString(R.string.needs_opinion))
			        .setContentText(qText);
			
			Intent deleteIntent = new Intent(ctx, DeleteNotificationActivity.class);
			final PendingIntent deletePendingIntent = PendingIntent.getBroadcast(ctx, 29, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setDeleteIntent(deletePendingIntent);
			
			int[] answerNotifs = {R.id.answer1_notif, R.id.answer2_notif, R.id.answer3_notif, R.id.answer4_notif, R.id.answer5_notif};
			int[] numberNotifs = {R.id.one_notif, R.id.two_notif, R.id.three_notif, R.id.four_notif, R.id.five_notif};
			int[] numberButtonNotifs = {R.id.one_button_notif, R.id.two_button_notif, R.id.three_button_notif, R.id.four_button_notif, R.id.five_button_notif};
			String[] answers = {answer1, answer2, answer3, answer4, answer5};
			
			for (int j = 0; j < nbrA; j++) {
				remoteView.setTextViewText(answerNotifs[j],answers[j]);
				remoteView.setOnClickPendingIntent(numberButtonNotifs[j], btPendingIntents[j]);
			}
			for (int j = nbrA; j < 5; j++) {
				remoteView.setViewVisibility(answerNotifs[j], View.GONE);
				remoteView.setViewVisibility(numberNotifs[j], View.GONE);
				remoteView.setViewVisibility(numberButtonNotifs[j], View.GONE);
			}
				
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
									TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
									// Adds the back stack for the Intent (but not the Intent itself)
									stackBuilder.addParentStack(Answer.class);
									// Adds the Intent that starts the Activity to the top of the stack
									stackBuilder.addNextIntent(answerIntent);
									PendingIntent answerPendingIntent =
											stackBuilder.getPendingIntent(
												0,
												PendingIntent.FLAG_UPDATE_CURRENT
											);
									mBuilder.setContentIntent(answerPendingIntent);
									Notification notification = mBuilder.build();
				    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
				    				notification.bigContentView = remoteView;
				    				}
									NotificationManager mNotificationManager =
										(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
									// notificationId allows you to update the notification later on.
									mNotificationManager.notify(notificationTag, notificationId, notification);
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
									TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
									// Adds the back stack for the Intent (but not the Intent itself)
									stackBuilder.addParentStack(Answer.class);
									// Adds the Intent that starts the Activity to the top of the stack
									stackBuilder.addNextIntent(answerIntent);
									PendingIntent answerPendingIntent =
											stackBuilder.getPendingIntent(
												0,
												PendingIntent.FLAG_UPDATE_CURRENT
											);
									mBuilder.setContentIntent(answerPendingIntent);
									Notification notification = mBuilder.build();
				    				if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
				    				notification.bigContentView = remoteView;
				    				}
									NotificationManager mNotificationManager =
										(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
									// notificationId allows you to update the notification later on.
									mNotificationManager.notify(notificationTag, notificationId, notification);
					}
			     }

			     public void onFinish() {
			    	 
			    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			 		manager.cancel(notificationTag, notificationId);
			    			
			     }
			     
			}.start();
			
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
			String grpname = json.getString("groupname");
			Boolean grp = json.getBoolean("group");
			String askerU = json.getString("askerUsername");
			Boolean subOnly = json.getBoolean("subscribersOnly");
			String creationDate = json.get("createdAt").toString();
			
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
			extras.putBoolean(group, grp);
			extras.putBoolean(subscribersOnly, subOnly);
			extras.putString(askerUsername, askerU);
			extras.putString(groupname, grpname);
			extras.putBoolean(savedQuestion, false);
			extras.putString(createdAt, creationDate);
			resultsIntent.putExtras(extras);
			
			Intent[] quickIntents = new Intent[2];
			PendingIntent[] btPendingIntents = new PendingIntent[2];

			final Bundle extras1 = new Bundle();
			extras1.putString(ID, questionId);
			extras1.putInt(nID,notificationId);
			extras1.putString(tag,notificationTag);
			extras1.putBoolean(subscribersOnly, subOnly);
			extras1.putBoolean(group, grp);
			extras1.putString(askerUsername, askerU);
			extras1.putString(groupname, grpname);
			
			for (int j = 0; j < 2; j++) {
				quickIntents[j] = new Intent(ctx, ResultsNotificationActivity.class);
				extras1.putInt(saveOrUnsubscribeOrLeave, j+1);
				quickIntents[j].putExtras(extras1);
				btPendingIntents[j] = PendingIntent.getBroadcast(ctx, 31+j, quickIntents[j], PendingIntent.FLAG_UPDATE_CURRENT);
			}
			
			final RemoteViews remoteView = new RemoteViews("com.spersio.opinions", R.layout.custom_notification_results);

			remoteView.setTextViewText(R.id.question_view_r_notif, qText);
			
			if (subOnly | grp) {
				remoteView.setImageViewResource(R.id.unsubscribe_button_notif, R.drawable.ic_unsubscribe);
			}
			
			int[] answerNotifs = {R.id.answer1_r_notif, R.id.answer2_r_notif, R.id.answer3_r_notif, R.id.answer4_r_notif, R.id.answer5_r_notif};
			int[] numberNotifs = {R.id.one_r_notif, R.id.two_r_notif, R.id.three_r_notif, R.id.four_r_notif, R.id.five_r_notif};
			int[] nAnswerNotifs = {R.id.n_answer1_notif, R.id.n_answer2_notif, R.id.n_answer3_notif, R.id.n_answer4_notif, R.id.n_answer5_notif};
			int[] pcNotifs = {R.id.pc1_notif, R.id.pc2_notif, R.id.pc3_notif, R.id.pc4_notif, R.id.pc5_notif};
			String[] answers = {answer1, answer2, answer3, answer4, answer5};
			Integer[] nAnswers = {nAnswer1, nAnswer2, nAnswer3, nAnswer4, nAnswer5};
			double[] pcAnswers = {pcAnswer1, pcAnswer2, pcAnswer3, pcAnswer4, pcAnswer5};
			
			for (int j = 0; j < nbrA; j++) {
				remoteView.setTextViewText(answerNotifs[j],answers[j]);
				remoteView.setTextViewText(nAnswerNotifs[j],nAnswers[j].toString());
				remoteView.setTextViewText(pcNotifs[j],String.valueOf((double)Math.round(pcAnswers[j] * 10) / 10) + "%");
			}
			for (int j = nbrA; j < 5; j++) {
				remoteView.setViewVisibility(answerNotifs[j], View.GONE);
				remoteView.setViewVisibility(numberNotifs[j], View.GONE);
				remoteView.setViewVisibility(pcNotifs[j], View.GONE);
				remoteView.setViewVisibility(nAnswerNotifs[j], View.GONE);
			}
			
			remoteView.setOnClickPendingIntent(R.id.saveResults_button_notif, btPendingIntents[0]);
			remoteView.setOnClickPendingIntent(R.id.unsubscribe_button_notif, btPendingIntents[1]);
			
			NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(ctx)
		        .setSmallIcon(R.drawable.ic_stat_question)
		        .setTicker(ctx.getResources().getString(R.string.results_are_in))
		        .setContentTitle(ctx.getResources().getString(R.string.results_are_in))
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
  
  