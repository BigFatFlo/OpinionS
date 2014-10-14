package com.spersio.opinion;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Submit extends Activity implements OnSeekBarChangeListener{

	TextView questionText= null;
	
	TextView answer1 = null;
	TextView answer2 = null;
	TextView answer3 = null;
	TextView answer4 = null;
	TextView answer5 = null;
	
	TextView usersNumber = null;
	TextView usersAvailable = null;
	
	Button delete1 = null;
	Button delete2 = null;
	Button delete3 = null;
	Button delete4 = null;
	Button delete5 = null;
	
	RadioGroup radioGroup = null;
	
	SeekBar usersBar = null;
	
	EditText questionSubmit = null;

	EditText answerSubmit = null;
	
	Integer nbrAnswers = 0;
	
	Integer submit_type = -1;
	
	Integer nbrChannels = 0;
	
	Integer nbrUsersPerChannel = 0;
	
	Integer radius = 0;
	
	Boolean international = false;
	
	/*WifiP2pManager mManager;
	
	Channel mChannel;
	
	BroadcastReceiver mReceiver;
	
	IntentFilter mIntentFilter;
	
	private List peers = new ArrayList();*/

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_submit);

		questionSubmit = (EditText) findViewById(R.id.question_submit);
		
		answerSubmit= (EditText) findViewById(R.id.answer_submit);

		questionText= (TextView) findViewById(R.id.question_preview);
		
		usersNumber= (TextView) findViewById(R.id.users_number);
		usersAvailable= (TextView) findViewById(R.id.users_available);
		
		answer1= (TextView) findViewById(R.id.answer1_preview);
		answer2= (TextView) findViewById(R.id.answer2_preview);
		answer3= (TextView) findViewById(R.id.answer3_preview);
		answer4= (TextView) findViewById(R.id.answer4_preview);
		answer5= (TextView) findViewById(R.id.answer5_preview);
		
		delete1= (Button) findViewById(R.id.delete_answer1);
		delete2= (Button) findViewById(R.id.delete_answer2);
		delete3= (Button) findViewById(R.id.delete_answer3);
		delete4= (Button) findViewById(R.id.delete_answer4);
		delete5= (Button) findViewById(R.id.delete_answer5);
		
		/*int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
			mIntentFilter = new IntentFilter();
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
			mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		    mChannel = mManager.initialize(this, getMainLooper(), null);
		    mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
		} else{*/
			radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		//}
		
		answer1.setVisibility(View.GONE);
		answer2.setVisibility(View.GONE);
		answer3.setVisibility(View.GONE);
		answer4.setVisibility(View.GONE);
		answer5.setVisibility(View.GONE);
		
		delete1.setVisibility(View.GONE);
		delete2.setVisibility(View.GONE);
		delete3.setVisibility(View.GONE);
		delete4.setVisibility(View.GONE);
		delete5.setVisibility(View.GONE);
		
		usersNumber.setVisibility(View.GONE);
		usersAvailable.setVisibility(View.GONE);
		
		usersBar = (SeekBar)findViewById(R.id.users_seekbar); // make seekbar object
        usersBar.setOnSeekBarChangeListener(this);
		usersBar.setVisibility(View.GONE);
		
		
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
			
			findViewById(R.id.question_done).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					questionText.setText(questionSubmit.getText().toString());
				}
			});

			findViewById(R.id.answer_add).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answerSubmit.getText().toString().equals("") && answerSubmit.getText().toString() != null){
						switch (nbrAnswers)
						{
						  case 0:
							  	answer1.setText(answerSubmit.getText().toString());
							  	answer1.setVisibility(View.VISIBLE);
							  	delete1.setVisibility(View.VISIBLE);
							  	nbrAnswers=nbrAnswers+1;
							  	break;
						  case 1:
							    answer2.setText(answerSubmit.getText().toString());
							    answer2.setVisibility(View.VISIBLE);
							    delete2.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 2:
							    answer3.setText(answerSubmit.getText().toString());
							    answer3.setVisibility(View.VISIBLE);
							    delete3.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 3:
							    answer4.setText(answerSubmit.getText().toString());
							    answer4.setVisibility(View.VISIBLE);
							    delete4.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 4:
							    answer5.setText(answerSubmit.getText().toString());
							    answer5.setVisibility(View.VISIBLE);
							    delete5.setVisibility(View.VISIBLE);
							    nbrAnswers=nbrAnswers+1;
							    break;
						  case 5:
							  Toast.makeText(Submit.this, "You can't submit more than 5 answers!", Toast.LENGTH_LONG)
								.show();
							    break;
						  default:
							  Toast.makeText(Submit.this, "Oops, something went wrong!", Toast.LENGTH_LONG)
								.show();;             
						}
					} else {
						Toast.makeText(Submit.this, "You need to type something!", Toast.LENGTH_LONG)
						.show();
					}
				}
			});
			
			findViewById(R.id.delete_answer1).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer1.getText().toString().equals("") && answer1.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 1:
						    answer1.setText("");
						    answer1.setVisibility(View.GONE);
						    delete1.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 2:
						    answer1.setText(answer2.getText().toString());
						  	answer2.setText("");
						  	answer2.setVisibility(View.GONE);
						  	delete2.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 3:
						  	answer1.setText(answer2.getText().toString());
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText("");
						  	answer3.setVisibility(View.GONE);
						  	delete3.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 4:
						  	answer1.setText(answer2.getText().toString());
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer1.setText(answer2.getText().toString());
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer2).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer2.getText().toString().equals("") && answer2.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 2:
						  	answer2.setText("");
						  	answer2.setVisibility(View.GONE);
						  	delete2.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 3:
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText("");
						  	answer3.setVisibility(View.GONE);
						  	delete3.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 4:
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer2.setText(answer3.getText().toString());
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer3).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer3.getText().toString().equals("") && answer3.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 3:
						  	answer3.setText("");
						  	answer3.setVisibility(View.GONE);
						  	delete3.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 4:
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer3.setText(answer4.getText().toString());
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer4).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer4.getText().toString().equals("") && answer4.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 4:
						  	answer4.setText("");
						  	answer4.setVisibility(View.GONE);
						  	delete4.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  case 5:
						  	answer4.setText(answer5.getText().toString());
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
			
			findViewById(R.id.delete_answer5).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (!answer5.getText().toString().equals("") && answer5.getText().toString()!=null ){
					switch (nbrAnswers)
					{
					  case 5:
						  	answer5.setText("");
						  	answer5.setVisibility(View.GONE);
						  	delete5.setVisibility(View.GONE);
						    nbrAnswers=nbrAnswers-1;
						    break;
					  default:
						  Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
							.show();
					    ;
					}
				} else {
					Toast.makeText(Submit.this, "There is no answer to delete!", Toast.LENGTH_LONG)
					.show();	
				}
				}
			});
      
			radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		    {
		        public void onCheckedChanged(RadioGroup group, int checkedId) {
		        	switch (checkedId) {
		        		case R.id.normal_submit:
		        			submit_type = 0;
		        		break;
		        		case R.id.international_submit:
		        			submit_type = 1;
		        		break;
		        		case R.id.area_submit:
		        			submit_type = 2;
		        		break;
		        		default:
		        			submit_type = -1;
		        	}
					if (questionText.getText().toString().length() > 1 && nbrAnswers > 1 && nbrAnswers < 6) {
					switch (submit_type) { 
		            	case 0: // Normal (ie) country submit
							final ProgressDialog dlg = new ProgressDialog(Submit.this);
							dlg.setTitle("Please wait.");
							dlg.setMessage("Loading available users. Please wait.");
							dlg.show();
							ParseQuery<ParseObject> query0 = ParseQuery.getQuery("Country");
							query0.whereEqualTo("name", currentUser.getString("country"));
							query0.getFirstInBackground(new GetCallback<ParseObject>() {
							  public void done(ParseObject country, ParseException e) {
								if (e == null) {
								  int nbrChannelsAvailable = country.getInt("nbrChannels"); // number of available channels
								  
								  switch (nbrChannelsAvailable) {
									case -1:
									dlg.dismiss();
									Toast.makeText(Submit.this, "Sorry, no users available in your country", Toast.LENGTH_LONG)
								    .show();
									break;
									case 0:
									int nbrUsersAvailable0 = country.getInt("nbrUsers");
									usersNumber.setText("Ask about " + nbrUsersAvailable0 + "users");
									usersNumber.setVisibility(View.VISIBLE);
									usersAvailable.setText(nbrUsersAvailable0 + "users available");
									usersAvailable.setVisibility(View.VISIBLE);
									nbrChannels = 1;
									dlg.dismiss();
									break;
									default:
									nbrUsersPerChannel = country.getInt("avUsersPerChannel");
									int nbrUsersAvailable1 = country.getInt("nbrUsers");
									usersNumber.setText("Ask " + nbrUsersPerChannel + "users");
									usersNumber.setVisibility(View.VISIBLE);
									usersBar.setMax(nbrChannelsAvailable-1);
									usersBar.setVisibility(View.VISIBLE);
									usersAvailable.setText(nbrUsersAvailable1 + "users available in your country");
									usersAvailable.setVisibility(View.VISIBLE);
									nbrChannels = 1;
									dlg.dismiss();
									}
								  
								} else {
								  Toast.makeText(Submit.this, "Unable to load available users...", Toast.LENGTH_LONG)
								  .show();
								  dlg.dismiss();
								}
							  }
							});
		            	break;
		            	case 1: // International submit (country = "international")
							final ProgressDialog dlg1 = new ProgressDialog(Submit.this);
							dlg1.setTitle("Please wait.");
							dlg1.setMessage("Loading available users. Please wait.");
							dlg1.show();
							ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Country");
							query1.getInBackground("7wQb7cZnQH", new GetCallback<ParseObject>() {
							  public void done(ParseObject country, ParseException e) {
								if (e == null) {
								  int nbrChannelsAvailable = country.getInt("nbrChannels"); // number of available channels
								  
								  switch (nbrChannelsAvailable) {
									case 0:
									int nbrUsersAvailable0 = country.getInt("nbrUsers");
									usersNumber.setText("Ask " + nbrUsersAvailable0 + "users");
									usersNumber.setVisibility(View.VISIBLE);
									usersAvailable.setText(nbrUsersAvailable0 + "users available");
									usersAvailable.setVisibility(View.VISIBLE);
									nbrChannels = 1;
									dlg1.dismiss();
									break;
									default:
									nbrUsersPerChannel = country.getInt("avUsersPerChannel");
									int nbrUsersAvailable1 = country.getInt("nbrUsers");
									usersNumber.setText("Ask about " + nbrUsersPerChannel + "users");
									usersNumber.setVisibility(View.VISIBLE);
									usersBar.setMax(nbrChannelsAvailable);
									usersBar.setVisibility(View.VISIBLE);
									usersAvailable.setText(nbrUsersAvailable1 + "users available in the world");
									usersAvailable.setVisibility(View.VISIBLE);
									nbrChannels = 1;
									dlg1.dismiss();
									}
								  
								} else {
								  Toast.makeText(Submit.this, "Unable to load available users...", Toast.LENGTH_LONG)
								  .show();
								  dlg1.dismiss();
								}
							  }
							});
		            	break;
		            	case 2: // In the area submit
							if (currentUser.getBoolean("useLocation") && currentUser.getBoolean("locationKnown")) {
							final ProgressDialog dlg2 = new ProgressDialog(Submit.this);
							dlg2.setTitle("Please wait.");
							dlg2.setMessage("Loading available users. Please wait.");
							dlg2.show();
							radius = 1;
							HashMap<String, Object> params = new HashMap<String, Object>();
							params.put("radius", radius);
							ParseCloud.callFunctionInBackground("usersAround", params, new FunctionCallback<Integer>() {
							   public void done(Integer number, ParseException e) {
								   if (e == null) {
									int nbrUsersAvailable = number;
									usersNumber.setText("In a radius of " + radius + "km");
									usersNumber.setVisibility(View.VISIBLE);
									usersBar.setMax(10);
									usersBar.setVisibility(View.VISIBLE);
									usersAvailable.setText(nbrUsersAvailable + "users available");
									usersAvailable.setVisibility(View.VISIBLE);
									nbrChannels = 1;
									dlg2.dismiss();
								   } else {
									Toast.makeText(Submit.this, "Unable to load available users", Toast.LENGTH_LONG)
									.show();
									dlg2.dismiss();
								   }
							   }
							});
							} else {
							Toast.makeText(Submit.this, "Location unavailable...", Toast.LENGTH_LONG)
							.show();
							}
		            	break;
		            	
		            	// WiFi Direct Submit
		            		/*registerReceiver(mReceiver, mIntentFilter);
		            		private PeerListListener peerListListener = new PeerListListener() {
		            	        @Override
		            	        public void onPeersAvailable(WifiP2pDeviceList peerList) {

		            	            // Out with the old, in with the new.
		            	            peers.clear();
		            	            peers.addAll(peerList.getDeviceList());

		            	            // If an AdapterView is backed by this data, notify it
		            	            // of the change.  For instance, if you have a ListView of available
		            	            // peers, trigger an update.
		            	            //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
		            	            if (peers.size() == 0) {
		            	                Log.d("Submit", "No devices found");
		            	                return;
		            	            }
		            	        }
		            		};*/
		            }
					} else {
					if (submit_type == 0 || submit_type == 1 || submit_type == 2 || submit_type == 3) {
						radioGroup.clearCheck();
					}
					Toast.makeText(Submit.this, "You need to add a question and 2 to 5 answers first!", Toast.LENGTH_LONG)
					.show();
					}
		        }
		    });
			
			
			findViewById(R.id.submit_question_button).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					// change to 10, 15, ..????
					if (submit_type == -1) {
					Toast.makeText(Submit.this, "You have to select target users!", Toast.LENGTH_LONG)
					.show();
					} else {
					if  (nbrChannels == 0) {
					Toast.makeText(Submit.this, "No users available for your criteria", Toast.LENGTH_LONG)
					.show();
					} else {
					if (questionText.getText().toString().length() > 1){
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("text", questionText.getText().toString());
					params.put("nbrAnswers", nbrAnswers);
					params.put("nbrChannels", nbrChannels);
					params.put("radius",radius);
					switch (submit_type) {
						case 0:
						params.put("international",false);
						params.put("around",false);
						break;
						case 1:
						params.put("international",true);
						params.put("around",false);
						break;
						case 2:
						params.put("international",false);
						params.put("around",true);
						break;
					}
					switch (nbrAnswers)
					{
					case 2:
						/*HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("text", questionText.getText().toString());
						params.put("nbrAnswers", nbrAnswers);
						params.put("international", international);*/
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						/*ParseCloud.callFunctionInBackground("newQuestion", params, new FunctionCallback<Float>() {
						   void done(ParseException e) {
							   if (e == null) {
								Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
								.show();
								Intent intent2 = new Intent(Submit.this,Menu.class);
								intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent2);
							   } else {
								Toast.makeText(Interested.this, "Unable to submit the question, please try again", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});*/
						
						/*ParseObject question2 = new ParseObject("Question");
						question2.put("country", currentUser.get("country"));
						question2.put("text", questionText.getText().toString());
						question2.put("nA", 0);
						question2.put("nbrAnswers", nbrAnswers);
						question2.put("answer1", answer1.getText().toString());
						question2.put("answer2", answer2.getText().toString());
						question2.put("nA1", 0);
						question2.put("nA2", 0);
						question2.put("toSendForTest", true);
						question2.put("toSendForAnswer",false);
						question2.put("toSendForResults",false);
						question2.put("interested", 0);
						question2.put("notInterested", 0);
						question2.put("approved", false);
						question2.put("published", false);
						question2.put("asker",currentUser);
						question2.saveInBackground();

						Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
						.show();
						Intent intent2 = new Intent(Submit.this,Menu.class);
						intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent2);*/
						break;
					case 3:
						/*HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("text", questionText.getText().toString());
						params.put("nbrAnswers", nbrAnswers);
						params.put("international", international);*/
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						params.put("answer3", answer3.getText().toString());
						/*ParseCloud.callFunctionInBackground("newQuestion", params, new FunctionCallback<Float>() {
						   void done(ParseException e) {
							   if (e == null) {
								Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
								.show();
								Intent intent3 = new Intent(Submit.this,Menu.class);
								intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent3);
							   } else {
								Toast.makeText(Interested.this, "Unable to submit the question, please try again", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});*/
					
						/*ParseObject question3 = new ParseObject("Question");
						question3.put("country", currentUser.get("country"));
						question3.put("text", questionText.getText().toString());
						question3.put("nA", 0);
						question3.put("nbrAnswers", nbrAnswers);
						question3.put("answer1", answer1.getText().toString());
						question3.put("answer2", answer2.getText().toString());
						question3.put("answer3", answer3.getText().toString());
						question3.put("nA1", 0);
						question3.put("nA2", 0);
						question3.put("nA3", 0);
						question3.put("toSendForTest", true);
						question3.put("toSendForAnswer",false);
						question3.put("toSendForResults",false);
						question3.put("interested", 0);
						question3.put("notInterested", 0);
						question3.put("approved", false);
						question3.put("published", false);
						question3.put("asker",currentUser);
						question3.saveInBackground();

						Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
						.show();
						Intent intent3 = new Intent(Submit.this,Menu.class);
						intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent3);*/
						break;
					case 4:
						/*HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("text", questionText.getText().toString());
						params.put("nbrAnswers", nbrAnswers);
						params.put("international", international);*/
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						params.put("answer3", answer3.getText().toString());
						params.put("answer4", answer4.getText().toString());
						/*ParseCloud.callFunctionInBackground("newQuestion", params, new FunctionCallback<Float>() {
						   void done(ParseException e) {
							   if (e == null) {
								Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
								.show();
								Intent intent4 = new Intent(Submit.this,Menu.class);
								intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent4);
							   } else {
								Toast.makeText(Interested.this, "Unable to submit the question, please try again", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});*/
					
						/*ParseObject question4 = new ParseObject("Question");
						question4.put("country", currentUser.get("country"));
						question4.put("text", questionText.getText().toString());
						question4.put("nA", 0);
						question4.put("nbrAnswers", nbrAnswers);	
						question4.put("answer1", answer1.getText().toString());
						question4.put("answer2", answer2.getText().toString());
						question4.put("answer3", answer3.getText().toString());
						question4.put("answer4", answer4.getText().toString());
						question4.put("nA1", 0);
						question4.put("nA2", 0);
						question4.put("nA3", 0);
						question4.put("nA4", 0);
						question4.put("toSendForTest", true);
						question4.put("toSendForAnswer",false);
						question4.put("toSendForResults",false);
						question4.put("interested", 0);
						question4.put("notInterested", 0);
						question4.put("approved", false);
						question4.put("published", false);
						question4.put("asker",currentUser);
						question4.saveInBackground();

						Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
						.show();
						Intent intent4 = new Intent(Submit.this,Menu.class);
						intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent4);*/
						break;
					case 5:
						/*HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("text", questionText.getText().toString());
						params.put("nbrAnswers", nbrAnswers);
						params.put("international", international);*/
						params.put("answer1", answer1.getText().toString());
						params.put("answer2", answer2.getText().toString());
						params.put("answer3", answer3.getText().toString());
						params.put("answer4", answer4.getText().toString());
						params.put("answer5", answer5.getText().toString());
						/*ParseCloud.callFunctionInBackground("newQuestion", params, new FunctionCallback<Float>() {
						   void done(ParseException e) {
							   if (e == null) {
								Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
								.show();
								Intent intent5 = new Intent(Submit.this,Menu.class);
								intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent5);
							   } else {
								Toast.makeText(Interested.this, "Unable to submit the question, please try again", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});*/
					
						/*ParseObject question5 = new ParseObject("Question");
						question5.put("country", currentUser.get("country"));
						question5.put("text", questionText.getText().toString());
						question5.put("nA", 0);
						question5.put("nbrAnswers", nbrAnswers);	
						question5.put("answer1", answer1.getText().toString());
						question5.put("answer2", answer2.getText().toString());
						question5.put("answer3", answer3.getText().toString());
						question5.put("answer4", answer4.getText().toString());
						question5.put("answer5", answer5.getText().toString());
						question5.put("nA1", 0);
						question5.put("nA2", 0);
						question5.put("nA3", 0);
						question5.put("nA4", 0);
						question5.put("nA5", 0);
						question5.put("toSendForTest", true);
						question5.put("toSendForAnswer",false);
						question5.put("toSendForResults",false);
						question5.put("interested", 0);
						question5.put("notInterested", 0);
						question5.put("approved", false);
						question5.put("published", false);
						question5.put("asker",currentUser);
						question5.saveInBackground();

						Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
						.show();
						Intent intent5 = new Intent(Submit.this,Menu.class);
						intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent5);*/
						break;
					default:
						Toast.makeText(Submit.this, "You have to submit at at least 2 possible answers", Toast.LENGTH_LONG)
						.show();
					}
					if (nbrAnswers > 1 && nbrAnswers < 6) {
					final ProgressDialog dlg4 = new ProgressDialog(Submit.this);
					dlg4.setTitle("Please wait.");
					dlg4.setMessage("Submitting question. Please wait.");
					dlg4.show();
					ParseCloud.callFunctionInBackground("newQuestion", params, new FunctionCallback<Object>() {
						   public void done(Object object, ParseException e) {
							   if (e == null) {
								dlg4.dismiss();
								Toast.makeText(Submit.this, "Question submitted", Toast.LENGTH_LONG)
								.show();
								Intent intent = new Intent(Submit.this,Menu.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							   } else {
								dlg4.dismiss();
								Toast.makeText(Submit.this, "Unable to submit the question, please try again", Toast.LENGTH_LONG)
								.show();
							   }
						   }
						});
					} else {
						Toast.makeText(Submit.this, "You have to submit at at least 2 possible answers", Toast.LENGTH_LONG)
						.show();
					}
				}
				else 
				{
				Toast.makeText(Submit.this, "You have to submit a question of more than 15 characters", Toast.LENGTH_LONG)
				.show();
				}
			}
			}
			}
		});

		} else {
			Toast.makeText(Submit.this, "You are not logged in", Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Submit.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

	};
	
	@Override
	protected void onPause() {
		super.onPause();
		//unregisterReceiver(mReceiver);
	};
	
	@Override
	protected void onStop() {
		super.onStop();
	};
	
	
	 @Override
	 public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	     	switch (submit_type) {
				case 0:
			usersNumber.setText("Ask about " + (progress+1)*nbrUsersPerChannel + "users");
			nbrChannels = progress+1;
				break;
				case 1:
			usersNumber.setText("Ask about " + (progress+1)*nbrUsersPerChannel + "users");
			nbrChannels = progress+1;
				break;
				case 2:
			radius = progress+1;
			final ProgressDialog dlgR = new ProgressDialog(Submit.this);
			dlgR.setTitle("Please wait.");
			dlgR.setMessage("Loading available users. Please wait.");
			dlgR.show();
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("radius", radius);
			ParseCloud.callFunctionInBackground("usersAround", params, new FunctionCallback<Integer>() {
			   public void done(Integer number, ParseException e) {
				   if (e == null) {
					int nbrUsersAvailable = number;
					usersNumber.setText("In a radius of " + radius + "km");
					usersAvailable.setText(nbrUsersAvailable + "users available");
					dlgR.dismiss();
				   } else {
					Toast.makeText(Submit.this, "Unable to load available users", Toast.LENGTH_LONG)
					.show();
					dlgR.dismiss();
				   }
			   }
			});	
				break;
			}
	 };
	 
	 @Override
	 public void onStartTrackingTouch(SeekBar seekBar) {
	 };
	 
	 @Override
	 public void onStopTrackingTouch(SeekBar seekBar) {  	
	 };
	
}
