package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Ask extends ActionBarActivity {

	TextView questionText= null;
	
	TextView answer1 = null;
	TextView answer2 = null;
	TextView answer3 = null;
	TextView answer4 = null;
	TextView answer5 = null;
	
	TextView usersAvailable = null;
	
	ImageButton delete1 = null;
	ImageButton delete2 = null;
	ImageButton delete3 = null;
	ImageButton delete4 = null;
	ImageButton delete5 = null;
	
	Button askButton = null;
	
	ImageView one = null;
	ImageView two = null;
	ImageView three = null;
	ImageView four = null;
	ImageView five = null;
	
	RadioButton askSubscribers = null;
	RadioButton askGroup = null;
	
	Spinner groupsOwned = null;
	
	EditText questionSubmit = null;

	EditText answerSubmit = null;
	
	Integer nbrAnswers = 0;
	
	Integer ask_type = -1;
	
	Integer nbrChannels = 0;
	
	Integer nbrUsersPerChannel = 0;
	
	Integer nbrSubscribers = 0;
	
	Integer nbrUsersTargeted = 0;
	
	Integer nbrUsersAvailable = 0;
	
	String groupName = "";

	List<String> listGroups = null;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(0, 0);
		
		setContentView(R.layout.activity_ask);

		questionSubmit = (EditText) findViewById(R.id.question_ask);
		
		answerSubmit= (EditText) findViewById(R.id.answer_ask);

		questionText= (TextView) findViewById(R.id.question_preview);
		
		usersAvailable= (TextView) findViewById(R.id.users_available);
		
		answer1= (TextView) findViewById(R.id.answer1_preview);
		answer2= (TextView) findViewById(R.id.answer2_preview);
		answer3= (TextView) findViewById(R.id.answer3_preview);
		answer4= (TextView) findViewById(R.id.answer4_preview);
		answer5= (TextView) findViewById(R.id.answer5_preview);
		
		one= (ImageView) findViewById(R.id.one_ask);
		two= (ImageView) findViewById(R.id.two_ask);
		three= (ImageView) findViewById(R.id.three_ask);
		four= (ImageView) findViewById(R.id.four_ask);
		five= (ImageView) findViewById(R.id.five_ask);
		
		delete1= (ImageButton) findViewById(R.id.delete_answer1);
		delete2= (ImageButton) findViewById(R.id.delete_answer2);
		delete3= (ImageButton) findViewById(R.id.delete_answer3);
		delete4= (ImageButton) findViewById(R.id.delete_answer4);
		delete5= (ImageButton) findViewById(R.id.delete_answer5);
		
		askButton= (Button) findViewById(R.id.ask_question_button);
		
		askSubscribers= (RadioButton) findViewById(R.id.subscribers_ask_radiobutton);
		askGroup= (RadioButton) findViewById(R.id.group_ask_radiobutton);
		
		groupsOwned = (Spinner) findViewById(R.id.groupsOwned_spinner);
		groupsOwned.setFocusable(true);
		groupsOwned.setFocusableInTouchMode(true);
		
		answer1.setVisibility(View.GONE);
		answer2.setVisibility(View.GONE);
		answer3.setVisibility(View.GONE);
		answer4.setVisibility(View.GONE);
		answer5.setVisibility(View.GONE);
		
		one.setVisibility(View.GONE);
		two.setVisibility(View.GONE);
		three.setVisibility(View.GONE);
		four.setVisibility(View.GONE);
		five.setVisibility(View.GONE);
		
		delete1.setVisibility(View.GONE);
		delete2.setVisibility(View.GONE);
		delete3.setVisibility(View.GONE);
		delete4.setVisibility(View.GONE);
		delete5.setVisibility(View.GONE);
		
		askButton.setVisibility(View.GONE);
		
		usersAvailable.setVisibility(View.GONE);
		
		groupsOwned.setVisibility(View.GONE);
		
		
	};
	
	@Override
	protected void onStart() {
		super.onStart();
	};

	@Override
	protected void onResume() {
		super.onResume();
		
		final ActionBar actionBar = getSupportActionBar();
		
		final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		
		findViewById(R.id.ask_title).requestFocus();
	
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			
			questionSubmit.setOnFocusChangeListener(new OnFocusChangeListener() {          

		        public void onFocusChange(View v, boolean hasFocus) {
		            if (hasFocus) {
		            	actionBar.hide();
		            } else {
		                imm.hideSoftInputFromWindow(questionSubmit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		            	actionBar.show();
		            }
		        }
		    });
			
			findViewById(R.id.question_done).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
				  	if (questionSubmit.getText().length()>8){
					Toast.makeText(Ask.this, getResources().getString(R.string.question_added), Toast.LENGTH_SHORT)
					.show();	
					questionText.setText(questionSubmit.getText().toString());
					questionText.requestFocus();
				  	} else {
				  	Toast.makeText(Ask.this, getResources().getString(R.string.questions_must_be), Toast.LENGTH_LONG)
					.show();
				  	}
				}
			});
			
			answerSubmit.setOnFocusChangeListener(new OnFocusChangeListener() {          

		        public void onFocusChange(View v, boolean hasFocus) {
		            if (hasFocus) {
		            	actionBar.hide();
		            } else {
		            	imm.hideSoftInputFromWindow(answerSubmit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		            	actionBar.show();
		            }
		        }
		    });

			final ArrayList<TextView> answerTextViews = new ArrayList<TextView>();
			answerTextViews.add(answer1);
			answerTextViews.add(answer2);
			answerTextViews.add(answer3);
			answerTextViews.add(answer4);
			answerTextViews.add(answer5);

			final ArrayList<ImageButton> deleteImageButtons = new ArrayList<ImageButton>();
			deleteImageButtons.add(delete1);
			deleteImageButtons.add(delete2);
			deleteImageButtons.add(delete3);
			deleteImageButtons.add(delete4);
			deleteImageButtons.add(delete5);

			final ArrayList<ImageView> numberImageViews = new ArrayList<ImageView>();
			numberImageViews.add(one);
			numberImageViews.add(two);
			numberImageViews.add(three);
			numberImageViews.add(four);
			numberImageViews.add(five);
			
			findViewById(R.id.answer_add).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					
					if (!answerSubmit.getText().toString().equals("") && answerSubmit.getText().toString() != null){
						
						if (nbrAnswers<5) {
							Toast.makeText(Ask.this, getResources().getString(R.string.answer_added), Toast.LENGTH_SHORT)
							.show();	
						  	answerTextViews.get(nbrAnswers).setText(answerSubmit.getText().toString());
						  	answerTextViews.get(nbrAnswers).setVisibility(View.VISIBLE);
						  	if (nbrAnswers > 0) {
						  		answerTextViews.get(nbrAnswers).requestFocus();
						  	}
						  	deleteImageButtons.get(nbrAnswers).setVisibility(View.VISIBLE);
						  	numberImageViews.get(nbrAnswers).setVisibility(View.VISIBLE);
						  	nbrAnswers=nbrAnswers+1;
						} else {
							Toast.makeText(Ask.this, getResources().getString(R.string.answers_max), Toast.LENGTH_LONG)
							.show();
						}
					
					} else {
						Toast.makeText(Ask.this, getResources().getString(R.string.need_to_type), Toast.LENGTH_LONG)
						.show();
					}
				}
			});
			
			findViewById(R.id.delete_answer1).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					nbrAnswers = delete_answer(1,nbrAnswers,answerTextViews, deleteImageButtons, numberImageViews);
				}
			});
			
			findViewById(R.id.delete_answer2).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					nbrAnswers = delete_answer(2,nbrAnswers,answerTextViews, deleteImageButtons, numberImageViews);
				}
			});
			
			findViewById(R.id.delete_answer3).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					nbrAnswers = delete_answer(3,nbrAnswers,answerTextViews, deleteImageButtons, numberImageViews);
				}
			});
			
			findViewById(R.id.delete_answer4).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					nbrAnswers = delete_answer(4,nbrAnswers,answerTextViews, deleteImageButtons, numberImageViews);
				}
			});
			
			findViewById(R.id.delete_answer5).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					nbrAnswers = delete_answer(5,nbrAnswers,answerTextViews, deleteImageButtons, numberImageViews);
				}
			});
			
			askSubscribers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if (questionText.getText().toString().length() > 8  && nbrAnswers > 1 && nbrAnswers < 6 && !(questionText.getText().toString().equals(getResources().getString(R.string.here_you_can_see)))) {
							ask_type = 0;
							askGroup.setChecked(false);
							groupsOwned.setVisibility(View.GONE);
							currentUser.fetchInBackground(new GetCallback<ParseObject>() {
		            			  public void done(ParseObject user, ParseException e) {
		            			    if (e == null) {
		    		            		sendToSubscribers(user);
		            			    } else {
		            			    	sendToSubscribers(currentUser);
		            			    }
		            			  }
		            			});
						} else {
							askSubscribers.setChecked(false);
							askGroup.setChecked(false);
							ask_type = -1;
							nbrUsersTargeted = 0;
							groupName = "";
							Toast.makeText(Ask.this, getResources().getString(R.string.need_to_add), Toast.LENGTH_LONG)
							.show();
							usersAvailable.setVisibility(View.GONE);
							askButton.setVisibility(View.GONE);
							
						}
					}
				}
			});
			
			askGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if (questionText.getText().toString().length() > 8  && nbrAnswers > 1 && nbrAnswers < 6 && !(questionText.getText().toString().equals(getResources().getString(R.string.here_you_can_see)))) {
							ask_type = 1;
							askSubscribers.setChecked(false);
							currentUser.fetchInBackground(new GetCallback<ParseObject>() {
		            			  public void done(ParseObject user, ParseException e) {
		            			    if (e == null) {
		    		            		sendToGroup(user);
		            			    } else {
		            			    	sendToGroup(currentUser);
		            			    }
		            			  }
		            			});
						} else {
							askSubscribers.setChecked(false);
							askGroup.setChecked(false);
							ask_type = -1;
							nbrUsersTargeted = 0;
							groupName = "";
							Toast.makeText(Ask.this, getResources().getString(R.string.need_to_add), Toast.LENGTH_LONG)
							.show();
							usersAvailable.setVisibility(View.GONE);
							askButton.setVisibility(View.GONE);
							groupsOwned.setVisibility(View.GONE);
							
						}
					}
				}
			});	
			
			groupsOwned.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						
						groupName = parent.getItemAtPosition(pos).toString();
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("groupname", groupName);
						params.put("username", currentUser.getUsername());
						
						ParseCloud.callFunctionInBackground("nbrMembersInGroup", params, new FunctionCallback<Integer>() {
							   public void done(Integer number, ParseException e) {
								   if (e == null) {
									   
									   switch (number) {
									   
										   case -2:
											   Toast.makeText(Ask.this, getResources().getString(R.string.not_owner), Toast.LENGTH_LONG)
												.show();
											   nbrUsersTargeted = 0;
											   groupName = "";
				    						   usersAvailable.setVisibility(View.GONE);
											   askButton.setVisibility(View.GONE);
										   break;
										   case -1:
											   Toast.makeText(Ask.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_LONG)
												.show();
											   nbrUsersTargeted = 0;
											   groupName = "";
				    						   usersAvailable.setVisibility(View.GONE);
											   askButton.setVisibility(View.GONE);
										   break;
										   case 0:
											   Toast.makeText(Ask.this, getResources().getString(R.string.group_empty), Toast.LENGTH_LONG)
												.show();
											   nbrUsersTargeted = 0;
											   groupName = "";
				    						   usersAvailable.setVisibility(View.GONE);
											   askButton.setVisibility(View.GONE);
										   break;
										   default:
											   nbrUsersTargeted = number;
											   usersAvailable.setText(getResources().getString(R.string.send_to_the) + number + getResources().getString(R.string.members_of) + groupName);
				    						   usersAvailable.setVisibility(View.VISIBLE);
											   askButton.setVisibility(View.VISIBLE);
				    						   askButton.requestFocus();
									   }
									   
								   } else {
									   
									   nbrUsersTargeted = 0;
									   groupName = "";
									   Toast.makeText(Ask.this, getResources().getString(R.string.no_info_group), Toast.LENGTH_LONG)
										.show();
		    						   usersAvailable.setVisibility(View.GONE);
									   askButton.setVisibility(View.GONE);
									   
								   }
						
			    }});
			};

			    public void onNothingSelected(AdapterView<?> parent) {
			    	nbrUsersTargeted = 0;
				   groupName = "";
				   usersAvailable.setVisibility(View.GONE);
				   askButton.setVisibility(View.GONE);
			    }			
				
			});
			
			findViewById(R.id.ask_question_button).setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (ask_type == -1) {
						
						Toast.makeText(Ask.this, getResources().getString(R.string.no_targeted_users), Toast.LENGTH_LONG)
						.show();
						
					} else if (nbrUsersTargeted == 0) {
						
						Toast.makeText(Ask.this, getResources().getString(R.string.no_users_criteria), Toast.LENGTH_LONG)
						.show();
						
					} else if (questionText.getText().toString().length() > 8 && !(questionText.getText().toString().equals(getResources().getString(R.string.here_you_can_see)))){
						
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("text", questionText.getText().toString());
						params.put("nbrAnswers", nbrAnswers);
						params.put("nbrUsersTargeted", nbrUsersTargeted);
						
						switch (ask_type) {
							
							case 0:
								params.put("group", false);
								params.put("groupname", groupName);
								params.put("subscribersOnly", true);
								params.put("nbrUsersTargeted", nbrUsersTargeted);
							break;
							case 1:
								if (groupName.equals("")) {						
									Toast.makeText(Ask.this, getResources().getString(R.string.no_group), Toast.LENGTH_LONG)
									.show();
								} else {
									params.put("groupname", groupName);
									params.put("group",true);
									params.put("subscribersOnly", false);
									params.put("nbrUsersTargeted", nbrUsersTargeted);
								}
							break;
						}
						
						if (nbrAnswers > 1 && nbrAnswers < 6) {
							
							for (int i = 1; i < nbrAnswers +1; i ++) {
								params.put("answer" + i, answerTextViews.get(i-1).getText().toString());
							}
							
							final ProgressDialog dlg = new ProgressDialog(Ask.this);
							dlg.setTitle(getResources().getString(R.string.please_wait));
							dlg.setMessage(getResources().getString(R.string.please_wait));
							dlg.show();
							
							ParseCloud.callFunctionInBackground("newQuestion", params, new FunctionCallback<Object>() {
								   public void done(Object object, ParseException e) {
									   if (e == null) {
											dlg.dismiss();
											Toast.makeText(Ask.this, getResources().getString(R.string.question_sent), Toast.LENGTH_SHORT)
											.show();
											Intent intent = new Intent(Ask.this,Home.class);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);
									   } else {
											dlg.dismiss();
											Toast.makeText(Ask.this, getResources().getString(R.string.unable_to_send), Toast.LENGTH_LONG)
											.show();
									   }
								   }
								});
							
						} else {
							Toast.makeText(Ask.this, getResources().getString(R.string.need_to_add_answers), Toast.LENGTH_LONG)
							.show();
						}
						
					} else {
						Toast.makeText(Ask.this, getResources().getString(R.string.questions_must_be), Toast.LENGTH_LONG)
						.show();
					}
				}
		});

		} else {
			Toast.makeText(Ask.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Ask.this, Login.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_ask_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_home:
            	startActivity(new Intent(Ask.this,Home.class));
                return true;
            case R.id.action_profile:
            	Intent intentP = new Intent(Ask.this,Profile.class);
				intentP.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            	startActivity(intentP);
                return true;
            case R.id.action_subscriptions:
            	startActivity(new Intent(Ask.this,YourSubscriptions.class));
                return true;
            case R.id.action_groups:
            	startActivity(new Intent(Ask.this,YourGroups.class));
                return true;
            case R.id.action_savedQuestions:
            	startActivity(new Intent(Ask.this,SavedQuestions.class));
                return true;
            case R.id.action_logOut:
            	ParseUser.logOut();
            	Toast.makeText(Ask.this, getResources().getString(R.string.logged_out), Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(Ask.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        
    }
    

	public int delete_answer(int answer, int nbrAnswers, ArrayList<TextView> answerTextViews, List<ImageButton> deleteImageButtons, List<ImageView> numberImageViews) {
		if (!answerTextViews.get(answer - 1).getText().toString().equals("") && answerTextViews.get(answer - 1).getText().toString()!=null ){
			if (nbrAnswers > 0 && nbrAnswers < 6) {
				for (int i = answer-1; i < nbrAnswers - 1; i++) {
					answerTextViews.get(i).setText(answerTextViews.get(i+1).getText().toString());
				}
				answerTextViews.get(nbrAnswers-1).setText("");
				answerTextViews.get(nbrAnswers-1).setVisibility(View.GONE);
				deleteImageButtons.get(nbrAnswers-1).setVisibility(View.GONE);
				numberImageViews.get(nbrAnswers-1).setVisibility(View.GONE);
				return nbrAnswers - 1;
			} else {
				Toast.makeText(Ask.this, getResources().getString(R.string.no_answer_to_delete), Toast.LENGTH_LONG)
				.show();
				return nbrAnswers;
			}
		} else {
			Toast.makeText(Ask.this, getResources().getString(R.string.no_answer_to_delete), Toast.LENGTH_LONG)
			.show();
			return nbrAnswers;
		}
	};
	
	public void sendToSubscribers(ParseObject user) {
		nbrSubscribers = user.getInt("nbrSubscribers");
		if (nbrSubscribers <= 0) {
			Toast.makeText(Ask.this, getResources().getString(R.string.no_subscribers), Toast.LENGTH_LONG)
			  .show();
			askSubscribers.setChecked(false);
			askGroup.setChecked(false);
			ask_type = -1;
			nbrUsersTargeted = 0;
			groupName = "";
			usersAvailable.setVisibility(View.GONE);
			askButton.setVisibility(View.GONE);
		} else {
    	nbrUsersTargeted = nbrSubscribers;
    	groupName = "";
		usersAvailable.setText(getResources().getString(R.string.send_to) + nbrSubscribers + getResources().getString(R.string.subscribers));
		usersAvailable.setVisibility(View.VISIBLE);
		askButton.setVisibility(View.VISIBLE);
		askButton.requestFocus();
		}
	};
	
	public void sendToGroup(ParseObject user) {
		listGroups = user.getList("ownedGroups");
		if (listGroups != null) {
    		if (listGroups.isEmpty()) {
    			Toast.makeText(Ask.this, getResources().getString(R.string.no_groups), Toast.LENGTH_LONG)
				  .show();
				askSubscribers.setChecked(false);
				askGroup.setChecked(false);
				ask_type = -1;
				nbrUsersTargeted = 0;
				groupName = "";
				usersAvailable.setVisibility(View.GONE);
				askButton.setVisibility(View.GONE);
				groupsOwned.setVisibility(View.GONE);
    		} else {
			ArrayAdapter<String> adapterG = new ArrayAdapter<String>(Ask.this,
					android.R.layout.simple_spinner_item , listGroups);
			adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			groupsOwned.setAdapter(adapterG);
			groupsOwned.setVisibility(View.VISIBLE);
			groupsOwned.requestFocus();
    		}
		} else {
			Toast.makeText(Ask.this, getResources().getString(R.string.no_groups), Toast.LENGTH_LONG)
			  .show();
			askSubscribers.setChecked(false);
			askGroup.setChecked(false);
			ask_type = -1;
			nbrUsersTargeted = 0;
			groupName = "";
			usersAvailable.setVisibility(View.GONE);
			askButton.setVisibility(View.GONE);
			groupsOwned.setVisibility(View.GONE);
		}
	}
	
}
