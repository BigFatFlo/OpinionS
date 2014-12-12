package com.spersio.opinions;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseUser;

public class CountryChange extends Activity {

	TextView country= null;
	
	Spinner listeC= null;
	
	List<String> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_country_change);

		country = (TextView) findViewById(R.id.country_change);
		
		listeC = (Spinner) findViewById(R.id.list_countries_spinner);

		final ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {
			
			final ProgressDialog dlg = new ProgressDialog(CountryChange.this);
	        dlg.setTitle("Please wait.");
	        dlg.setMessage("Loading countries. Please wait.");
	        dlg.show();
	        
	        ParseConfig.getInBackground(new ConfigCallback() {
	        	  @Override
	        	  public void done(ParseConfig config, ParseException e) {
	        	    if (e == null) {
	        	    } else {
	        	      config = ParseConfig.getCurrentConfig();
	        	    }
	        	    
	        	    list = config.getList("listOfCountries");
	        	    
	        	    if (!list.isEmpty()) 
					{
						ArrayAdapter<String> adapterC = new ArrayAdapter<String>(CountryChange.this,
								android.R.layout.simple_spinner_item , list);
						adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						listeC.setAdapter(adapterC);
						dlg.dismiss();
					}
					else {
						dlg.dismiss();
						Toast.makeText(CountryChange.this, "Unable to load available countries", Toast.LENGTH_LONG)
						.show();
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
			
		findViewById(R.id.country_submit_button).setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if (listeC.getSelectedItemPosition()==-1) {						
							Toast.makeText(CountryChange.this, "You have to select a country!", Toast.LENGTH_LONG)
							.show();
						} else {
						final String selectedItem = list.get(listeC.getSelectedItemPosition());
						if (selectedItem.equals(actualCountry)){
							Intent intent = new Intent(CountryChange.this,Menu.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivity(intent);
						} else {
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
