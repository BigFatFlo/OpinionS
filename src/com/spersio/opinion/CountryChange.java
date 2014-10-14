package com.spersio.opinion;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CountryChange extends Activity {

	TextView username= null;

	TextView country= null;
	
	Spinner listeP= null;
	
	ArrayList<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_country_change);

		username = (TextView) findViewById(R.id.username_profile_pref_lang);

		country = (TextView) findViewById(R.id.country_change);
		
		listeP = (Spinner) findViewById(R.id.listP_spinner);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Country");

		final ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {
			
			final ProgressDialog dlg = new ProgressDialog(CountryChange.this);
	        dlg.setTitle("Please wait.");
	        dlg.setMessage("Loading countries. Please wait.");
	        dlg.show();
			
			username.setText(currentUser.getUsername().toString());

			query.getInBackground("jRVoUfwRRe",new GetCallback<ParseObject>() {
				public void done(ParseObject allCountries, ParseException e) {
					if (e == null) {
						JSONArray jArray = allCountries.getJSONArray("linkedCountries");
						for (int i=0;i<jArray.length();i++){
							try {
								list.add(jArray.getString(i));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (!list.isEmpty()) 
						{
							ArrayAdapter<String> adapterP = new ArrayAdapter<String>(CountryChange.this,
									android.R.layout.simple_spinner_item , list);
							adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							listeP.setAdapter(adapterP);
							dlg.dismiss();
						}
						else {
							dlg.dismiss();
							Toast.makeText(CountryChange.this, "Unable to load available countries", Toast.LENGTH_LONG)
							.show();
						}
					}
				}
			});

		}
		
		else {
			Toast.makeText(CountryChange.this, "You are not logged in", Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(CountryChange.this, Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	};	
		
	protected void onRestart() {
		super.onRestart();
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
		
		final String actualCountry = currentUser.getString("country");
			
		findViewById(R.id.pref_submit_button).setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if (listeP.getSelectedItemPosition()==-1) {						
							Toast.makeText(CountryChange.this, "You have to select a country!", Toast.LENGTH_LONG)
							.show();
						} else {
						final String selectedItem = list.get(listeP.getSelectedItemPosition());
						if (selectedItem.equals(actualCountry)){
							Intent intent = new Intent(CountryChange.this,Menu.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivity(intent);
						} else {
						/*ParseQuery<ParseObject> countryQuery = ParseQuery.getQuery("Country");
						countryQuery.whereEqualTo("name", selectedItem);
						countryQuery.getFirstInBackground(new GetCallback<ParseObject>() {
							  public void done(final ParseObject country, ParseException e) {
								  final int nbrChannels = country.getInt("nbrChannels");
								  if (nbrChannels == 0){
										ParseObject newChannel = new ParseObject("Channel");
										newChannel.put("name",selectedItem + "_1");
										newChannel.put("full",false);
										newChannel.put("country",selectedItem);
										newChannel.put("members",1);
										newChannel.put("lastTester",0);
										newChannel.put("maxMembers",1000);
										newChannel.saveInBackground();
										country.put("nbrChannels",1);
										country.saveInBackground();
										ParseInstallation installation = ParseInstallation.getCurrentInstallation();
										installation.put("channel",selectedItem + "_1");
										installation.saveInBackground();
										currentUser.put("channel",selectedItem + "_1");
										currentUser.saveInBackground();
									} else {
										ParseQuery<ParseObject> channelQuery = ParseQuery.getQuery("Channel");	
										channelQuery.whereEqualTo("country",selectedItem);
										channelQuery.whereNotEqualTo("full", true);
										channelQuery.getFirstInBackground(new GetCallback<ParseObject>() {
											  public void done(ParseObject channel, ParseException e) {							    
												if (e == null){
												int m = channel.getInt("members");
												int max = channel.getInt("maxMembers");
												final String channelName = channel.getString("name");
												channel.put("members", m + 1);
												if (m + 1 == max) {
													channel.put("full", true);
													country.put("nbreChannels",country.getInt("nbrChannels")+1);
													country.saveInBackground();
												}
												channel.saveInBackground();
												ParseInstallation installation = ParseInstallation.getCurrentInstallation();
												installation.put("channel",channelName);
												installation.saveInBackground();
												currentUser.put("channel",channelName);
												currentUser.saveInBackground();
												} else {
													ParseObject newChannel = new ParseObject("Channel");
													newChannel.put("name",selectedItem + "_" + (nbrChannels+1));
													newChannel.put("full",false);
													newChannel.put("country",selectedItem);
													newChannel.put("members",1);
													newChannel.put("lastTester",0);
													newChannel.put("maxMembers",1000);
													newChannel.saveInBackground();
													ParseInstallation installation = ParseInstallation.getCurrentInstallation();
													installation.put("channel",selectedItem + "_" + (nbrChannels+1));
													installation.saveInBackground();
													currentUser.put("channel",selectedItem + "_" + (nbrChannels+1));
													currentUser.saveInBackground();
												}
									}});
									}
							  }
							});*/
						
						currentUser.put("country",selectedItem);
						currentUser.put("changeOfChannel", true);
						currentUser.saveInBackground();
						Intent intent = new Intent(CountryChange.this,Menu.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						}
					}
					}});		

			} else {
				Toast.makeText(CountryChange.this, "You are not logged in", Toast.LENGTH_LONG)
				.show();
				Intent intent = new Intent(CountryChange.this, Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
	};
	
	@Override
	protected void onPause() {
		super.onPause();
	};
	
	@Override
	protected void onStop() {
		super.onStop();
	};
}
