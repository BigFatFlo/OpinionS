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

  public static final String questionKey = "com.spersio.opinion.question";
  public static final String ID = "com.spersio.opinion.ID";
  public static final String nID = "com.spersio.opinion.notificationId";
  public static final String tag = "com.spersio.opinion.notificationTag";
  public static final String aID = "com.spersio.opinion.answerId";
  public static final String savedQuestion = "com.spersio.opinion.savedQuestion";
  public static final String timeLeft = "com.spersio.opinion.timeLeft";
  public static final String delete = "com.spersio.opinion.delete";
  public static CountDownTimer countDownTimerAp= null;
  public static CountDownTimer countDownTimerA= null;

@Override
	public void onPushReceive(Context context, Intent intent) {
	try {
			//ParseAnalytics.trackAppOpened(intent);
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			String action = json.getString("action");
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
	  
	  		String[] answers = {json.getString("answer1"),
					json.getString("answer2"),
					json.getString("answer3"),
					json.getString("answer4"),
					json.getString("answer5")};
			
			final Question question = new Question(json.getString("questionID"),
											json.getString("questionText"),
											json.getString("questionID") + "_Answer",
											json.getString("askerUsername"),
											json.getString("groupname"),
											json.getInt("nbrAnswers"),
											1,
											answers,
											json.getBoolean("group"),
											json.getBoolean("subscribersOnly")
											);
			
			final Intent answerIntent = new Intent(ctx, Answer.class);
			answerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			final Bundle extras = new Bundle();
			
			Intent[] quickIntents = new Intent[5];
			PendingIntent[] btPendingIntents = new PendingIntent[5];

			final Bundle extras1 = new Bundle();
			extras1.putString(ID, question.questionID);
			extras1.putInt(nID,question.notificationID);
			extras1.putString(tag,question.notificationTag);
			
			for (int j = 0; j < 5; j++) {
				quickIntents[j] = new Intent(ctx, AnswerNotificationActivity.class);
				extras1.putInt(aID,j+1);
				quickIntents[j].putExtras(extras1);
				btPendingIntents[j] = PendingIntent.getBroadcast(ctx, 21+j, quickIntents[j], PendingIntent.FLAG_UPDATE_CURRENT);
			}
			
			final RemoteViews remoteView = new RemoteViews("com.spersio.opinions", R.layout.custom_notification);
			
			remoteView.setTextViewText(R.id.question_notif, question.text);
			
			final int timeToAnswer = json.getInt("timeToAnswer");
			final int timeToAnswerInMinutes = timeToAnswer/60;
			final int timeInterval = timeToAnswer/6;
			
			int[] drawablesArray = {R.drawable.ic_stat_question_red_blue_1,
									R.drawable.ic_stat_question_red_blue_2,
									R.drawable.ic_stat_question_red_blue_3,
									R.drawable.ic_stat_question_red_blue_4,
									R.drawable.ic_stat_question_all_red};
			
			final NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(ctx)
			        .setSmallIcon(R.drawable.ic_stat_question)
			        .setTicker(timeToAnswerInMinutes + ctx.getResources().getString(R.string.time_to_answer))
			        .setContentTitle(question.askerUsername + ctx.getResources().getString(R.string.needs_opinion))
			        .setContentText(question.text);
			
			Intent deleteIntent = new Intent(ctx, DeleteNotificationActivity.class);
			final PendingIntent deletePendingIntent = PendingIntent.getBroadcast(ctx, 29, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setDeleteIntent(deletePendingIntent);
			
			int[] answerNotifs = {R.id.answer1_notif, R.id.answer2_notif, R.id.answer3_notif, R.id.answer4_notif, R.id.answer5_notif};
			int[] numberNotifs = {R.id.one_notif, R.id.two_notif, R.id.three_notif, R.id.four_notif, R.id.five_notif};
			int[] numberButtonNotifs = {R.id.one_button_notif, R.id.two_button_notif, R.id.three_button_notif, R.id.four_button_notif, R.id.five_button_notif};
			
			for (int j = 0; j < question.nbrAnswers; j++) {
				remoteView.setTextViewText(answerNotifs[j],answers[j]);
				remoteView.setOnClickPendingIntent(numberButtonNotifs[j], btPendingIntents[j]);
			}
			for (int j = question.nbrAnswers; j < 5; j++) {
				remoteView.setViewVisibility(answerNotifs[j], View.GONE);
				remoteView.setViewVisibility(numberNotifs[j], View.GONE);
				remoteView.setViewVisibility(numberButtonNotifs[j], View.GONE);
			}
			
			countDownTimerA = new CountDownTimer(timeToAnswer*1000 + 800, 1000) {

			     public void onTick(long millisUntilFinished) {
			    	 
			    	extras.putParcelable(questionKey, question);
			    	extras.putLong(timeLeft, millisUntilFinished);
					answerIntent.putExtras(extras);
			    	 
			    	 if ((int) (millisUntilFinished/1000) == timeToAnswer) {
							
							mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
							
									TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
									stackBuilder.addParentStack(Answer.class);
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
									mNotificationManager.notify(question.notificationTag, question.notificationID, notification);
					} else {
			    	 
					    	 	int timeIntervalsElapsedInPercent = (int) ((timeToAnswer - (millisUntilFinished/1000)) *100 )/ timeInterval;
								switch (timeIntervalsElapsedInPercent) {
									case 100:
										mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_1);
										mBuilder.setVibrate(new long[] { 0, 150});
					    	 		break;
					    	 		case 200:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_2);
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
					    	 		case 300:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_3);
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
					    	 		case 400:
						    	 		mBuilder.setSmallIcon(R.drawable.ic_stat_question_red_blue_4);
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
					    	 		case 500:
					    	 			mBuilder.setSmallIcon(R.drawable.ic_stat_question_all_red);
						    	 		mBuilder.setVibrate(new long[] { 0, 150});
						    	 	break;
						    	 	default: 
						    	 		mBuilder.setVibrate(new long[] { 0, 0});
								}
									
					    	 	TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
								stackBuilder.addParentStack(Answer.class);
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
								mNotificationManager.notify(question.notificationTag, question.notificationID, notification);
					}
			     }

			     public void onFinish() {
			    	 
			    	 NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			 		manager.cancel(question.notificationTag, question.notificationID);
			    			
			     }
			     
			}.start();
			
		  };

@SuppressLint("NewApi") 
  private void notifyForResults(Context ctx, Intent i, JSONObject json)
		  throws JSONException
		  {
			
			final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			
			String[] answers = {json.getString("answer1"),
								json.getString("answer2"),
								json.getString("answer3"),
								json.getString("answer4"),
								json.getString("answer5")};
			
			int[] numberForAnswer = {json.getInt("nA1"),
									json.getInt("nA2"),
									json.getInt("nA3"),
									json.getInt("nA4"),
									json.getInt("nA5")};
			
			double[] percentageForAnswer = {json.getDouble("pcA1"),
											json.getDouble("pcA2"),
											json.getDouble("pcA3"),
											json.getDouble("pcA4"),
											json.getDouble("pcA5")};
			
			Question question = new Question(json.getString("questionID"),
											json.getString("questionText"),
											json.getString("questionID") + "_Results",
											json.getString("askerUsername"),
											json.getString("groupname"),
											json.get("createdAt").toString(),
											json.getInt("nbrAnswers"),
											json.getInt("nA"),
											2,
											answers,
											numberForAnswer,
											percentageForAnswer,
											json.getBoolean("group"),
											json.getBoolean("subscribersOnly"),
											false
											);
			
			Intent resultsIntent = new Intent(ctx, Results.class);
			resultsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			
			resultsIntent.putExtra(questionKey,question);
			
			Intent quickIntent = new Intent(ctx, ResultsNotificationActivity.class);

			final Bundle extras1 = new Bundle();
			extras1.putString(ID, question.questionID);
			extras1.putInt(nID,question.notificationID);
			extras1.putString(tag,question.notificationTag);
			quickIntent.putExtras(extras1);
			
			PendingIntent btPendingIntent = PendingIntent.getBroadcast(ctx, 31, quickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			final RemoteViews remoteView = new RemoteViews("com.spersio.opinions", R.layout.custom_notification_results);

			remoteView.setTextViewText(R.id.question_view_r_notif, question.text);
			
			int[] answerNotifs = {R.id.answer1_r_notif, R.id.answer2_r_notif, R.id.answer3_r_notif, R.id.answer4_r_notif, R.id.answer5_r_notif};
			int[] numberNotifs = {R.id.one_r_notif, R.id.two_r_notif, R.id.three_r_notif, R.id.four_r_notif, R.id.five_r_notif};
			int[] nAnswerNotifs = {R.id.n_answer1_notif, R.id.n_answer2_notif, R.id.n_answer3_notif, R.id.n_answer4_notif, R.id.n_answer5_notif};
			int[] pcNotifs = {R.id.pc1_notif, R.id.pc2_notif, R.id.pc3_notif, R.id.pc4_notif, R.id.pc5_notif};
			
			for (int j = 0; j < question.nbrAnswers; j++) {
				remoteView.setTextViewText(answerNotifs[j],answers[j]);
				remoteView.setTextViewText(nAnswerNotifs[j],String.valueOf(numberForAnswer[j]));
				remoteView.setTextViewText(pcNotifs[j],String.valueOf((double)Math.round(percentageForAnswer[j] * 10) / 10) + "%");
			}
			for (int j = question.nbrAnswers; j < 5; j++) {
				remoteView.setViewVisibility(answerNotifs[j], View.GONE);
				remoteView.setViewVisibility(numberNotifs[j], View.GONE);
				remoteView.setViewVisibility(pcNotifs[j], View.GONE);
				remoteView.setViewVisibility(nAnswerNotifs[j], View.GONE);
			}
			
			remoteView.setOnClickPendingIntent(R.id.saveResults_button_notif, btPendingIntent);
			
			NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(ctx)
		        .setSmallIcon(R.drawable.ic_stat_question)
		        .setTicker(ctx.getResources().getString(R.string.results_are_in))
		        .setContentTitle(ctx.getResources().getString(R.string.results_are_in))
		        .setContentText(question.text);
			
			mBuilder.setVibrate(new long[] { 0, 400, 150, 400 });
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
			stackBuilder.addParentStack(Results.class);
			stackBuilder.addNextIntent(resultsIntent);
			PendingIntent resultsPendingIntent =
					stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
					);
			mBuilder.setContentIntent(resultsPendingIntent);
			Notification notification = mBuilder.build();
			if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			notification.bigContentView = remoteView;
			}
			NotificationManager mNotificationManager =
				(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(question.notificationTag, question.notificationID, notification);
		  };	  

}
  
  