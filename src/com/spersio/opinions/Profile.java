package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ConfigCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Profile extends ActionBarActivity {

	TextView profileTitle = null;
	TextView actualProfile = null;
	TextView country = null;
	
	ToggleButton internationalButton = null;
	
	Spinner countriesSpinner = null;
	Spinner sexesSpinner = null;
	Spinner datesSpinner = null;
	Spinner salariesSpinner = null;
	
	List<String> listCountries = null;
	List<String> listSexes = new ArrayList<String>();
	List<String> listDates = null;
	List<String> listSalaries = null;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile);

		profileTitle = (TextView) findViewById(R.id.profile_title);
		
		actualProfile = (TextView) findViewById(R.id.actual_profile);
		
		country = (TextView) findViewById(R.id.country_change);
		
		internationalButton = (ToggleButton) findViewById(R.id.international_button);
		
		countriesSpinner = (Spinner) findViewById(R.id.list_countries_spinner);
		sexesSpinner = (Spinner) findViewById(R.id.sexes_spinner);
		datesSpinner = (Spinner) findViewById(R.id.dates_spinner);
		salariesSpinner = (Spinner) findViewById(R.id.salaries_spinner);

		final ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {
			
			final ProgressDialog dlg = new ProgressDialog(Profile.this);
	        dlg.setTitle(getResources().getString(R.string.please_wait));
	        dlg.setMessage(getResources().getString(R.string.loading_criteria));
	        dlg.show();
	        
	        ParseConfig.getInBackground(new ConfigCallback() {
	        	  @Override
	        	  public void done(ParseConfig config, ParseException e) {
	        	    if (e == null) {
	        	    } else {
	        	      config = ParseConfig.getCurrentConfig();
	        	    }
	        	    
	        	    listCountries = config.getList("listOfCountries");
	        	    listSexes.add(getResources().getString(R.string.man));
	        	    listSexes.add(getResources().getString(R.string.woman));
	        	    listDates = config.getList("listOfDates");
	        	    listSalaries = config.getList("listOfSalaries");
	        	    
	        	    if (!listCountries.isEmpty() && !listSexes.isEmpty() && !listDates.isEmpty() && !listSalaries.isEmpty() ) 
					{
						ArrayAdapter<String> adapterCountries = new ArrayAdapter<String>(Profile.this,
								android.R.layout.simple_spinner_item , listCountries);
						adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						countriesSpinner.setAdapter(adapterCountries);
						
						ArrayAdapter<String> adapterSexes = new ArrayAdapter<String>(Profile.this,
								android.R.layout.simple_spinner_item , listSexes);
						adapterSexes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						sexesSpinner.setAdapter(adapterSexes);
						
						ArrayAdapter<String> adapterDates = new ArrayAdapter<String>(Profile.this,
								android.R.layout.simple_spinner_item , listDates);
						adapterDates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						datesSpinner.setAdapter(adapterDates);
						
						ArrayAdapter<String> adapterSalaries = new ArrayAdapter<String>(Profile.this,
								android.R.layout.simple_spinner_item , listSalaries);
						adapterSalaries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						salariesSpinner.setAdapter(adapterSalaries);
						dlg.dismiss();
					}
					else {
						dlg.dismiss();
						Toast.makeText(Profile.this, getResources().getString(R.string.unable_to_load_criteria), Toast.LENGTH_LONG)
						.show();
					}
	        	  }
	        	});
	        
	        
		}
		
		else {
			Toast.makeText(Profile.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
			.show();
			Intent intent = new Intent(Profile.this, Login.class);
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

		overridePendingTransition(0, 0);
	
		final ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {	
			
			int charNumber = currentUser.getInt("characteristicsNumber");
			
			Log.d("CharNumber", String.valueOf(charNumber));
			
			if (charNumber != -1) {
				
				String sex = null;
				if (charNumber < 1000) {
					sex = getResources().getString(R.string.man);
				} else {
					sex = getResources().getString(R.string.woman);
				}
				int salaryRange = charNumber%10; 
				int dateOfBirth = 0;
				int absoluteDateOfBirth = ((charNumber%1000) - (charNumber%10))/10;
				if (absoluteDateOfBirth<15) {
					dateOfBirth = absoluteDateOfBirth + 2000;
				} else {
					dateOfBirth = absoluteDateOfBirth + 1900;
				}
				String country = currentUser.getString("country");
				Boolean international = currentUser.getBoolean("international");
				String internationalChoice = null;
				if (international) {
					internationalChoice = getResources().getString(R.string.want);
				} else {
					internationalChoice = getResources().getString(R.string.dont_want);
				}
				
				actualProfile.setVisibility(View.VISIBLE);

				findViewById(R.id.change_profile_button).setVisibility(View.VISIBLE);
				findViewById(R.id.change_profile_text).setVisibility(View.VISIBLE);
				
				findViewById(R.id.save_profile_button).setVisibility(View.GONE);
				findViewById(R.id.country_change).setVisibility(View.GONE);
				findViewById(R.id.international).setVisibility(View.GONE);
				findViewById(R.id.sex_change).setVisibility(View.GONE);
				findViewById(R.id.salary_change).setVisibility(View.GONE);
				findViewById(R.id.date_of_birth_change).setVisibility(View.GONE);
				
				internationalButton.setVisibility(View.GONE);
				countriesSpinner.setVisibility(View.GONE);
				sexesSpinner.setVisibility(View.GONE);
				datesSpinner.setVisibility(View.GONE);
				salariesSpinner.setVisibility(View.GONE);
				
				profileTitle.setText(getResources().getString(R.string.profile_complete));
				actualProfile.setText(getResources().getString(R.string.you_are) + sex +
									  getResources().getString(R.string.born_in) + dateOfBirth + 
									  getResources().getString(R.string.living_in) + country + 
									  getResources().getString(R.string.have_a_salary) + salaryRange*10000 + 
									  getResources().getString(R.string.euro_and) + (salaryRange+1)*10000 + 
									  getResources().getString(R.string.euro) + 
									  getResources().getString(R.string.and) + internationalChoice + 
									  getResources().getString(R.string.to_receive_international));
				
				findViewById(R.id.change_profile_button).setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						
						profileTitle.setText(getResources().getString(R.string.complete_profile));
						
						actualProfile.setVisibility(View.GONE);
						
						findViewById(R.id.change_profile_button).setVisibility(View.GONE);
						findViewById(R.id.change_profile_text).setVisibility(View.GONE);
						
						findViewById(R.id.save_profile_button).setVisibility(View.VISIBLE);
						findViewById(R.id.country_change).setVisibility(View.VISIBLE);
						findViewById(R.id.international).setVisibility(View.VISIBLE);
						findViewById(R.id.sex_change).setVisibility(View.VISIBLE);
						findViewById(R.id.salary_change).setVisibility(View.VISIBLE);
						findViewById(R.id.date_of_birth_change).setVisibility(View.VISIBLE);
						
						internationalButton.setVisibility(View.VISIBLE);
						countriesSpinner.setVisibility(View.VISIBLE);
						sexesSpinner.setVisibility(View.VISIBLE);
						datesSpinner.setVisibility(View.VISIBLE);
						salariesSpinner.setVisibility(View.VISIBLE);
						
					}
				});
				
				
			} else {
				
				profileTitle.setText(getResources().getString(R.string.complete_profile));
				
				actualProfile.setVisibility(View.GONE);
				
				findViewById(R.id.change_profile_button).setVisibility(View.GONE);
				findViewById(R.id.change_profile_text).setVisibility(View.GONE);
				
				findViewById(R.id.save_profile_button).setVisibility(View.VISIBLE);
				findViewById(R.id.country_change).setVisibility(View.VISIBLE);
				findViewById(R.id.international).setVisibility(View.VISIBLE);
				findViewById(R.id.sex_change).setVisibility(View.VISIBLE);
				findViewById(R.id.salary_change).setVisibility(View.VISIBLE);
				findViewById(R.id.date_of_birth_change).setVisibility(View.VISIBLE);
				
				internationalButton.setVisibility(View.VISIBLE);
				countriesSpinner.setVisibility(View.VISIBLE);
				sexesSpinner.setVisibility(View.VISIBLE);
				datesSpinner.setVisibility(View.VISIBLE);
				salariesSpinner.setVisibility(View.VISIBLE);
				
			}
				
		final String actualCountry = currentUser.getString("country");
		final int actualCharNumber = charNumber;
		final Boolean international = currentUser.getBoolean("international");
			
		findViewById(R.id.save_profile_button).setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						if (countriesSpinner.getSelectedItemPosition()==-1 | sexesSpinner.getSelectedItemPosition()==-1 | datesSpinner.getSelectedItemPosition()==-1 | salariesSpinner.getSelectedItemPosition()==-1) {						
							
							Toast.makeText(Profile.this, getResources().getString(R.string.your_profile_is_not_complete), Toast.LENGTH_LONG)
							.show();
							
						} else {
							
							final String selectedCountry = countriesSpinner.getSelectedItem().toString();
							final int selectedSexNumber = sexesSpinner.getSelectedItemPosition();
							final String selectedSex = sexesSpinner.getSelectedItem().toString();
							final int selectedDate = (int) datesSpinner.getSelectedItem();
							final int selectedSalaryRange = salariesSpinner.getSelectedItemPosition();
							
							String internationalChoice = null;
							if (internationalButton.isChecked()) {
								internationalChoice = getResources().getString(R.string.want);
							} else {
								internationalChoice = getResources().getString(R.string.dont_want);
							}
							
							final int newCharNumber =  1000*selectedSexNumber + 10*((selectedDate-1900)%100) + selectedSalaryRange ;
							
							final Boolean changeOfCountry = !(selectedCountry.equals(actualCountry));
							final Boolean changeOfCharNumber = ((newCharNumber != actualCharNumber) && (actualCharNumber != -1));
							final Boolean newProfile = ((newCharNumber != actualCharNumber) && (actualCharNumber == -1));
							final Boolean changeOfInternational = (international != internationalButton.isChecked());
							
							if (changeOfCountry | changeOfCharNumber | newProfile | changeOfInternational) {
								
								DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
								    @Override
								    public void onClick(DialogInterface dialog, int which) {
								        switch (which){
								        case DialogInterface.BUTTON_POSITIVE:
								        	
								        	dialog.dismiss();
								        	
								        	final ProgressDialog dlg = new ProgressDialog(Profile.this);
											dlg.setTitle(getResources().getString(R.string.please_wait));
											dlg.setMessage(getResources().getString(R.string.saving_profile));
											dlg.show();
											
											final HashMap<String, Object> params = new HashMap<String, Object>();
											params.put("oldCharNumber", actualCharNumber);
											params.put("newCharNumber", newCharNumber);
											params.put("oldCountry", actualCountry);
											params.put("newCountry", selectedCountry);
											params.put("newProfile", newProfile);
											params.put("changeOfCountry", changeOfCountry);
											params.put("changeOfCharNumber", changeOfCharNumber);
											params.put("changeOfInternational", changeOfInternational);
											params.put("international", internationalButton.isChecked());
								        	
											ParseCloud.callFunctionInBackground("changeOfProfile", params, new FunctionCallback<Object>() {
												   public void done(Object object, ParseException e) {

													   if (e == null) {
														   dlg.dismiss();
														   
														   currentUser.put("country",selectedCountry);
														   currentUser.put("characteristicsNumber", newCharNumber);
														   currentUser.put("international", internationalButton.isChecked());
														   currentUser.saveInBackground();
														   
														   Toast.makeText(Profile.this, getResources().getString(R.string.profile_saved), Toast.LENGTH_SHORT)
															.show();
														   
														   Intent intent = new Intent(Profile.this,Home.class);
														   intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
														   startActivity(intent);
														   
													   } else {

														   dlg.dismiss();
														   Toast.makeText(Profile.this, getResources().getString(R.string.could_not_save_profile), Toast.LENGTH_SHORT)
															.show();
														   
													   }
											}});
								            break;
		
								        case DialogInterface.BUTTON_NEGATIVE:
								        	
								        	dialog.dismiss();
								        	
								            break;
								        }
								    }
								};
								
								AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
								builder.setMessage(getResources().getString(R.string.do_you_confirm) + selectedSex +
												   getResources().getString(R.string.born_in) + selectedDate + 
												   getResources().getString(R.string.living_in) + selectedCountry + 
												   getResources().getString(R.string.have_a_salary) + selectedSalaryRange*10000 + 
												   getResources().getString(R.string.euro_and) + (selectedSalaryRange+1)*10000 +
												   getResources().getString(R.string.euro) +
												   getResources().getString(R.string.and) + internationalChoice + 
												   getResources().getString(R.string.to_receive_international) + "?")
									.setPositiveButton("Yes", dialogClickListener)
								    .setNegativeButton("No", dialogClickListener).show();
							
						} else {
								
							Toast.makeText(Profile.this, getResources().getString(R.string.no_changes), Toast.LENGTH_SHORT)
							.show();
								
						}
					}
						
					}});	

			} else {
				Toast.makeText(Profile.this, getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
				.show();
				Intent intent = new Intent(Profile.this, Login.class);
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_profile_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_home:
            	startActivity(new Intent(Profile.this,Home.class));
                return true;
            case R.id.action_ask:
            	startActivity(new Intent(Profile.this,Ask.class));
                return true;
            case R.id.action_subscriptions:
            	startActivity(new Intent(Profile.this,YourSubscriptions.class));
                return true;
            case R.id.action_groups:
            	startActivity(new Intent(Profile.this,YourGroups.class));
                return true;
            case R.id.action_savedQuestions:
            	startActivity(new Intent(Profile.this,SavedQuestions.class));
                return true;
            case R.id.action_logOut:
            	ParseUser.logOut();
            	Toast.makeText(Profile.this, getResources().getString(R.string.logged_out), Toast.LENGTH_LONG)
                .show();
            	Intent intent = new Intent(Profile.this,Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            	startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
