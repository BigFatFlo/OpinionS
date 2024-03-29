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
	String groupName = null;
	int notificationID = 0;
	String notificationTag = null;
	Boolean subscribersOnly = null;
	Boolean group = null;
	
	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
		
		questionID = null;
		notificationID = 0;
		
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
	    
	    Question question = getIntent().getParcelableExtra(CustomPushReceiver.questionKey);
	    
		questionID = question.questionID;
		notificationID = question.notificationID;
		notificationTag = question.notificationTag;
		subscribersOnly = question.subscribersOnly;
		group = question.group;
		groupName = question.groupName;
		askerUsername = question.askerUsername;
		
		if (subscribersOnly) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + askerUsername + getResources().getString(R.string.to_subscribers));
		} else if (group) {
			askerInfo.setText(getResources().getString(R.string.question_asked_by) + askerUsername + getResources().getString(R.string.to_group) + groupName);
		}
		dateInfo.setText(question.createdAt);
		questionText.setText(question.text);	
		String total_n = String.valueOf(question.numberOfResponses);
		total.setText(total_n);
		pcTotal.setText("100%");
		total.setVisibility(View.VISIBLE);
		pcTotal.setVisibility(View.VISIBLE);
		
		if (question.savedQuestion) {
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
		
		TextView[] answerTextViews = {answer1, answer2, answer3, answer4, answer5};
		TextView[] nATextViews = {nA1, nA2, nA3, nA4, nA5};
		TextView[] pcTextViews = {pc1, pc2, pc3, pc4, pc5};
		ImageView[] numberImageViews = {one, two, three, four, five};
		
		if (question.nbrAnswers > 1 && question.nbrAnswers < 6) {
		
			for (int i=0; i< question.nbrAnswers; i++) {
				answerTextViews[i].setText(question.answers[i]);
				nATextViews[i].setText(String.valueOf(question.numberForAnswer[i]));
				pcTextViews[i].setText(String.valueOf((double)Math.round(question.percentageForAnswer[i] * 100) / 100) + "%");
	
				answerTextViews[i].setVisibility(View.VISIBLE);
				nATextViews[i].setVisibility(View.VISIBLE);
				pcTextViews[i].setVisibility(View.VISIBLE);
				numberImageViews[i].setVisibility(View.VISIBLE);
			}
			
		} else {
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
				
				List<String> listChannels = currentUser.getList("channels");
				
				if (subscribersOnly) {
					if (listChannels.contains("User_" + askerUsername)) {
						unsubscribe.setText(getResources().getString(R.string.unsubscribe_from) + askerUsername + getResources().getString(R.string.s_questions));
						unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsubscribe_big));
					} else {
						unsubscribe.setText(getResources().getString(R.string.resubscribe_to) + askerUsername + getResources().getString(R.string.s_questions));
						unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe_big));
					}
				} else if (group) {
					if (listChannels.contains("Group_" + groupName)) {
						unsubscribe.setText(getResources().getString(R.string.leave_group) + groupName);
						unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsubscribe_big));
					} else {
						unsubscribe.setText(getResources().getString(R.string.rejoin_group) + groupName);
						unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe_big));	
					}
				} else {
					// Invalid question
				}
				
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
						manager.cancel(notificationTag, notificationID);
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
						manager.cancel(notificationTag, notificationID);
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
						manager.cancel(notificationTag, notificationID);
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
						} else if (unsubscribe.getText().toString().equals(getResources().getString(R.string.leave_group) + groupName)) {
							final ProgressDialog dlg = new ProgressDialog(Results.this);
						    dlg.setTitle(getResources().getString(R.string.please_wait));
						    dlg.setMessage(getResources().getString(R.string.leaving));
						    dlg.show();
						    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							manager.cancel(notificationTag, notificationID);
							HashMap<String, Object> params = new HashMap<String, Object>();
							params.put("groupname", groupName);
							ParseCloud.callFunctionInBackground("subtractMember", params, new FunctionCallback<Object>() {
								   public void done(Object object, ParseException e) {
									   if (e == null) {
										   
										    List<String> list = currentUser.getList("joinedGroups");
										    List<String> listChannels = currentUser.getList("channels");
											
										    listChannels.remove("Group_" + groupName);
											list.remove(groupName);
											currentUser.put("channels", listChannels);
											currentUser.put("joinedGroups", list);
											
											currentUser.saveInBackground();
											
										unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe_big));
										unsubscribe.setText(getResources().getString(R.string.rejoin_group) + groupName);
										dlg.dismiss();
										Toast.makeText(Results.this, getResources().getString(R.string.left_group) + groupName, Toast.LENGTH_SHORT)
										.show();
									   } else {
										dlg.dismiss();
										Toast.makeText(Results.this, getResources().getString(R.string.unable_to_leave) + groupName + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
										.show();
									   }
								   }
								});
							
						} else if (unsubscribe.getText().toString().equals(getResources().getString(R.string.rejoin_group) + groupName)){
							
							final ProgressDialog dlg = new ProgressDialog(Results.this);
						    dlg.setTitle(getResources().getString(R.string.please_wait));
						    dlg.setMessage(getResources().getString(R.string.rejoining));
						    dlg.show();
						    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							manager.cancel(notificationTag, notificationID);
							HashMap<String, Object> params = new HashMap<String, Object>();
							params.put("groupname", groupName);
							ParseCloud.callFunctionInBackground("addMember", params, new FunctionCallback<Object>() {
								   public void done(Object object, ParseException e) {
									   if (e == null) {
										    
									    currentUser.addUnique("joinedGroups", groupName);
									    currentUser.addUnique("channels", "Group_" + groupName);
									    
										currentUser.saveInBackground();
										
										unsubscribeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsubscribe_big));
										unsubscribe.setText(getResources().getString(R.string.leave_group) + groupName);
										dlg.dismiss();
										Toast.makeText(Results.this, getResources().getString(R.string.rejoined_group) + groupName, Toast.LENGTH_SHORT)
										.show();
									   } else {
										dlg.dismiss();
										Toast.makeText(Results.this, getResources().getString(R.string.unable_to_rejoin) + groupName + getResources().getString(R.string.please_try_again), Toast.LENGTH_LONG)
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
						manager.cancel(notificationTag, notificationID);
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
	
	static void saveResults(String questionID, final Context context, int nId, String tag){
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
