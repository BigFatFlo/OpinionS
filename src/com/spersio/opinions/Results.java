package com.spersio.opinions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.parse.FunctionCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Results extends ActionBarActivity {

	TextView askerInfo= null;
	TextView dateInfo= null;

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
	
	TextView unsubscribe = null;
	TextView saveResults_text = null;
	
	ImageView one = null;
	ImageView two = null;
	ImageView three = null;
	ImageView four = null;
	ImageView five = null;
	
	ImageButton backToHome = null;
	ImageButton saveResults = null;
	ImageButton unsubscribeButton = null;
	
	String questionID = null;
	String askerUsername = null;
	String groupname = null;
	int nId = 0;
	String tag = null;
	
	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
		
		questionID = null;
		nId = 0;
		
		overridePendingTransition(0, 0);
		
		setContentView(R.layout.activity_results);

		askerInfo= (TextView) findViewById(R.id.asker_info_r);
		dateInfo= (TextView) findViewById(R.id.date_info_r);
		
		questionText= (TextView) findViewById(R.id.question_view_r);
		
		backToHome= (ImageButton) findViewById(R.id.backToHome_button);
		saveResults= (ImageButton) findViewById(R.id.saveResults_button);
		unsubscribeButton= (ImageButton) findViewById(R.id.unsubscribe_button);
		
		answer1= (TextView) findViewById(R.id.answer1_r);
		answer2= (TextView) findViewById(R.id.answer2_r);
		answer3= (TextView) findViewById(R.id.answer3_r);
		answer4= (TextView) findViewById(R.id.answer4_r);
		answer5= (TextView) findViewById(R.id.answer5_r);
		
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
		
		unsubscribe= (TextView) findViewById(R.id.unsubscribe_text);
		saveResults_text= (TextView) findViewById(R.id.saveResults_text);
		
		one= (ImageView) findViewById(R.id.one_r);
		two= (ImageView) findViewById(R.id.two_r);
		three= (ImageView) findViewById(R.id.three_r);
		four= (ImageView) findViewById(R.id.four_r);
		five= (ImageView) findViewById(R.id.five_r);
		
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
		
		one.setVisibility(View.GONE);
		two.setVisibility(View.GONE);
		three.setVisibility(View.GONE);
		four.setVisibility(View.GONE);
		five.setVisibility(View.GONE);
		
		saveResults.setVisibility(View.GONE);
		saveResults_text.setVisibility(View.GONE);
		unsubscribe.setVisibility(View.GONE);
		unsubscribeButton.setVisibility(View.GONE);

		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
	    
	    Bundle extras = getIntent().getExtras();
		questionID = extras.getString(CustomPushReceiver.ID);
		nId = extras.getInt(CustomPushReceiver.nID);
		tag = extras.getString(CustomPushReceiver.tag);
		Boolean subscribersOnly = extras.getBoolean(CustomPushReceiver.subscribersOnly);
		Boolean group = extras.getBoolean(CustomPushReceiver.group);
		groupname = extras.getString(CustomPushReceiver.groupname);
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
		final Boolean savedQuestion = extras.getBoolean(CustomPushReceiver.savedQuestion);
		final String creationTime = extras.getString(CustomPushReceiver.createdAt);
		
		if (subscribersOnly) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + askerUsername + getResources().getString(R.string.to_subscribers));
		} else if (group) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + askerUsername + getResources().getString(R.string.to_group) + groupname);
		}
		dateInfo.setText(creationTime);
		questionText.setText(qText);	
		String total_n = String.valueOf(nA);
		total.setText(total_n);
		pcTotal.setText("100%");
		total.setVisibility(View.VISIBLE);
		pcTotal.setVisibility(View.VISIBLE);
		
		if (savedQuestion) {
			backToHome.setVisibility(View.GONE);
			ActionBar actionBar = getSupportActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
		} else {
			ActionBar actionBar = getSupportActionBar();
			actionBar.hide();
			saveResults.setVisibility(View.VISIBLE);
			saveResults_text.setVisibility(View.VISIBLE);
		}
		
		unsubscribe.setVisibility(View.VISIBLE);
		unsubscribeButton.setVisibility(View.VISIBLE);
		
		if (subscribersOnly) {
			unsubscribe.setText(getResources().getString(R.string.unsubscribe_from) + askerUsername + getResources().getString(R.string.s_questions));	
		} else if (group) {
			unsubscribe.setText(getResources().getString(R.string.leave_group) + groupname);	
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
			one.setVisibility(View.VISIBLE);
			two.setVisibility(View.VISIBLE);
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
			one.setVisibility(View.VISIBLE);
			two.setVisibility(View.VISIBLE);
			three.setVisibility(View.VISIBLE);
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
			one.setVisibility(View.VISIBLE);
			two.setVisibility(View.VISIBLE);
			three.setVisibility(View.VISIBLE);
			four.setVisibility(View.VISIBLE);
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
			one.setVisibility(View.VISIBLE);
			two.setVisibility(View.VISIBLE);
			three.setVisibility(View.VISIBLE);
			four.setVisibility(View.VISIBLE);
			five.setVisibility(View.VISIBLE);
		    break;
	  default:
		  Toast.makeText(Results.this, getResources().getString(R.string.oops), Toast.LENGTH_LONG)
			.show();             
	}
	
	} else {
		Toast.makeText(Results.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
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
		
		uiHelper.onResume();
			
			final ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				
	findViewById(R.id.share_button_facebook).setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {

		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {	
			
		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(Results.this)
        .setLink("https://www.opinions.spersio.com/results/" + questionID)
        .build();
		uiHelper.trackPendingDialogCall(shareDialog.present());
		} else {
			
		Toast.makeText(Results.this, getResources().getString(R.string.need_facebook_app),Toast.LENGTH_LONG).show();
			
		}
	}});
	
	findViewById(R.id.share_button_twitter).setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {

			// Create intent using ACTION_VIEW and a normal Twitter url:
			String tweetUrl = 
			    String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
			        urlEncode(getResources().getString(R.string.check_out_results)), urlEncode("https://www.opinions.spersio.com/results/" + questionID));
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

			// Narrow down to official Twitter app, if available:
			List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
			for (ResolveInfo info : matches) {
			    if (info.activityInfo.packageName.toLowerCase(Locale.ENGLISH).startsWith("com.twitter")) {
			        intent.setPackage(info.activityInfo.packageName);
			    }
			}

			startActivity(intent);
			
	}});
	
	findViewById(R.id.share_button_email).setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
		            "mailto","", null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.poll_results));
			emailIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.check_out_results) + "https://www.opinions.spersio.com/results/" + questionID);
			try {
				startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_email)));
	        } catch (android.content.ActivityNotFoundException ex) {
	            Toast.makeText(Results.this, getResources().getString(R.string.no_email_clients),Toast.LENGTH_LONG).show();
	        }
			
		}
	});
				
	findViewById(R.id.saveResults_button).setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {
			final ProgressDialog dlg = new ProgressDialog(Results.this);
		    dlg.setTitle(getResources().getString(R.string.please_wait));
		    dlg.setMessage(getResources().getString(R.string.saving_results));
		    dlg.show();
		    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(tag, nId);
		    ParseRelation<ParseObject> relation = currentUser.getRelation("savedQuestions");
		    relation.add(ParseObject.createWithoutData("Question", questionID));
		    currentUser.saveInBackground();
			Toast.makeText(Results.this, getResources().getString(R.string.results_saved), Toast.LENGTH_SHORT)
			.show();
			saveResults.setVisibility(View.GONE);
			saveResults_text.setVisibility(View.GONE);
			findViewById(R.id.saveResults_text).setVisibility(View.GONE);
			dlg.dismiss();
		}
		});
	
	findViewById(R.id.unsubscribe_button).setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {
			
			if (unsubscribe.getText().toString().equals(getResources().getString(R.string.unsubscribe_from) + askerUsername + getResources().getString(R.string.s_questions))) {
				
			final ProgressDialog dlg = new ProgressDialog(Results.this);
		    dlg.setTitle(getResources().getString(R.string.please_wait));
		    dlg.setMessage(getResources().getString(R.string.unsubscribing));
		    dlg.show();
		    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(tag, nId);
			HashMap<String, List<String>> params = new HashMap<String, List<String>>();
			ArrayList<String> usernames = new ArrayList<String>();
			usernames.add(askerUsername);
			params.put("usernames", usernames);
			ParseCloud.callFunctionInBackground("subtractSubscriber", params, new FunctionCallback<Object>() {
				   public void done(Object object, ParseException e) {
					   if (e == null) {
						   
						    List<String> list = currentUser.getList("subscribedUsers");
						    List<String> listChannels = currentUser.getList("channels");
						    
							list.remove(askerUsername);
							listChannels.remove("User_" + askerUsername);
							currentUser.put("subscribedUsers", list);
							currentUser.put("channels", listChannels);
							
							currentUser.saveInBackground();
							
						unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe_big));
						unsubscribe.setText(getResources().getString(R.string.resubscribe_to) + askerUsername + getResources().getString(R.string.s_questions));
						dlg.dismiss();
						Toast.makeText(Results.this, getResources().getString(R.string.unsubscribed_from) + askerUsername + getResources().getString(R.string.s_questions), Toast.LENGTH_SHORT)
						.show();
					   } else {
						dlg.dismiss();
						Toast.makeText(Results.this, getResources().getString(R.string.unable_to_unsubscribe_from) + askerUsername + getResources().getString(R.string.s_questions_please), Toast.LENGTH_LONG)
						.show();
					   }
				   }
				});
				
			} else if (unsubscribe.getText().toString().equals(getResources().getString(R.string.resubscribe_to) + askerUsername + getResources().getString(R.string.s_questions))){
			
			final ProgressDialog dlg = new ProgressDialog(Results.this);
		    dlg.setTitle(getResources().getString(R.string.please_wait));
		    dlg.setMessage(getResources().getString(R.string.resubscribing));
		    dlg.show();
		    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(tag, nId);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("username", askerUsername);
			ParseCloud.callFunctionInBackground("addSubscriber", params, new FunctionCallback<Object>() {
				   public void done(Object object, ParseException e) {
					   if (e == null) {
						    
					    currentUser.addUnique("subscribedUsers", askerUsername);
					    currentUser.addUnique("channels", "User_" + askerUsername);
					    
						currentUser.saveInBackground();
						
						unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsubscribe_big));
						unsubscribe.setText(getResources().getString(R.string.unsubscribe_from) + askerUsername + getResources().getString(R.string.s_questions));
						dlg.dismiss();
						Toast.makeText(Results.this, getResources().getString(R.string.resubscribed_to) + askerUsername + getResources().getString(R.string.s_questions), Toast.LENGTH_SHORT)
						.show();
					   } else {
						dlg.dismiss();
						Toast.makeText(Results.this, getResources().getString(R.string.unable_to_resubscribe) + askerUsername + getResources().getString(R.string.s_questions_please), Toast.LENGTH_LONG)
						.show();
					   }
				   }
				});
			} else if (unsubscribe.getText().toString().equals(getResources().getString(R.string.leave_group) + groupname)) {
				final ProgressDialog dlg = new ProgressDialog(Results.this);
			    dlg.setTitle(getResources().getString(R.string.please_wait));
			    dlg.setMessage(getResources().getString(R.string.leaving));
			    dlg.show();
			    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				manager.cancel(tag, nId);
				HashMap<String, List<String>> params = new HashMap<String, List<String>>();
				ArrayList<String> groupnames = new ArrayList<String>();
				groupnames.add(groupname);
				params.put("groupnames", groupnames);
				ParseCloud.callFunctionInBackground("subtractMember", params, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
							   
							    List<String> list = currentUser.getList("joinedGroups");
							    List<String> listChannels = currentUser.getList("channels");
								
							    listChannels.remove("Group_" + groupname);
								list.remove(groupname);
								currentUser.put("channels", listChannels);
								currentUser.put("joinedGroups", list);
								
								currentUser.saveInBackground();
								
							unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe_big));
							unsubscribe.setText(getResources().getString(R.string.rejoin_group) + groupname);
							dlg.dismiss();
							Toast.makeText(Results.this, getResources().getString(R.string.left_group) + groupname, Toast.LENGTH_SHORT)
							.show();
						   } else {
							dlg.dismiss();
							Toast.makeText(Results.this, getResources().getString(R.string.unable_to_leave) + groupname + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
							.show();
						   }
					   }
					});
				
			} else if (unsubscribe.getText().toString().equals(getResources().getString(R.string.rejoin_group) + groupname)){
				
				final ProgressDialog dlg = new ProgressDialog(Results.this);
			    dlg.setTitle(getResources().getString(R.string.please_wait));
			    dlg.setMessage(getResources().getString(R.string.rejoining));
			    dlg.show();
			    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				manager.cancel(tag, nId);
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("groupname", groupname);
				ParseCloud.callFunctionInBackground("addMember", params, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
							    
						    currentUser.addUnique("joinedGroups", groupname);
						    currentUser.addUnique("channels", "Group_" + groupname);
						    
							currentUser.saveInBackground();
							
							unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsubscribe_big));
							unsubscribe.setText(getResources().getString(R.string.leave_group) + groupname);
							dlg.dismiss();
							Toast.makeText(Results.this, getResources().getString(R.string.rejoined_group) + groupname, Toast.LENGTH_SHORT)
							.show();
						   } else {
							dlg.dismiss();
							Toast.makeText(Results.this, getResources().getString(R.string.unable_to_rejoin) + groupname + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
							.show();
						   }
					   }
					});
			}
		}
		
		});
			
			findViewById(R.id.backToHome_button).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					manager.cancel(tag, nId);
					Intent intent = new Intent(Results.this,Home.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				});		

		} else {
			Toast.makeText(Results.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
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
		Toast.makeText(context, context.getResources().getString(R.string.results_saved), Toast.LENGTH_SHORT)
		.show();
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(tag, nId);
		} else {
		Toast.makeText(context, context.getResources().getString(R.string.log_in_and_try_again), Toast.LENGTH_LONG)
		.show();
		}
		
	};
	
	public static void unsubscribe(final String asker_username, final Context context){
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser!=null){
			
			HashMap<String, ArrayList<String>> params = new HashMap<String, ArrayList<String>>();
			ArrayList<String> usernames = new ArrayList<String>();
			usernames.add(asker_username);
			params.put("usernames", usernames);
			
				ParseCloud.callFunctionInBackground("subtractSubscriber", params, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
							
								List<String> list = currentUser.getList("subscribedUsers");
								List<String> listChannels = currentUser.getList("channels");
								
								list.remove(asker_username);
								listChannels.remove("User_" + asker_username);
								currentUser.put("subscribedUsers", list);
								currentUser.put("channels", listChannels);
								
								currentUser.saveInBackground();
							
							Toast.makeText(context, context.getResources().getString(R.string.unsubscribed_from) + asker_username + context.getResources().getString(R.string.s_questions), Toast.LENGTH_SHORT)
							.show();
						   } else {
							Toast.makeText(context, context.getResources().getString(R.string.unable_to_unsubscribe_from) + asker_username + context.getResources().getString(R.string.s_questions_please), Toast.LENGTH_LONG)
							.show();
						   }
					   }
				});
		
		} else {
		Toast.makeText(context, context.getResources().getString(R.string.log_in_and_try_again), Toast.LENGTH_LONG)
		.show();
		}
		
	};
	
	public static void leave(final String group_name, final Context context){
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser!=null){
			
			HashMap<String, ArrayList<String>> params = new HashMap<String, ArrayList<String>>();
			ArrayList<String> groupnames = new ArrayList<String>();
			groupnames.add(group_name);
			params.put("groupnames", groupnames);
			
				ParseCloud.callFunctionInBackground("subtractMember", params, new FunctionCallback<Object>() {
					   public void done(Object object, ParseException e) {
						   if (e == null) {
							
							    List<String> list = currentUser.getList("joinedGroups");
							    List<String> listChannels = currentUser.getList("channels");
								
							    listChannels.remove("Group_" + group_name);
								list.remove(group_name);
								currentUser.put("channels", listChannels);
								currentUser.put("joinedGroups", list);
								
								currentUser.saveInBackground();
							
							Toast.makeText(context, context.getResources().getString(R.string.left_group) + group_name , Toast.LENGTH_SHORT)
							.show();
						   } else {
							Toast.makeText(context, context.getResources().getString(R.string.unable_to_leave) + group_name + context.getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
							.show();
						   }
					   }
				});
		
		} else {
		Toast.makeText(context, context.getResources().getString(R.string.log_in_and_try_again), Toast.LENGTH_LONG)
		.show();
		}
		
	};
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	};
	
	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	            Toast.makeText(Results.this, getResources().getString(R.string.results_shared), Toast.LENGTH_SHORT)
	    		.show();
	        }
	    });
	    
	};
	
	public static String urlEncode(String s) {
	    try {
	        return URLEncoder.encode(s, "UTF-8");
	    }
	    catch (UnsupportedEncodingException e) {
	        Log.wtf("TwitterShare", "UTF-8 should always be supported", e);
	        throw new RuntimeException("URLEncoder.encode() failed for " + s);
	    }
	}
	
}
