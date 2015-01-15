package com.spersio.opinions;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class OwnedGroupsFragment extends Fragment{
	
	ListView yourGroupsView = null;
	TextView yourGroupsTitle = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        return inflater.inflate(R.layout.activity_your_groups, container, false);
        
	};
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
        View v = getView();
        
        yourGroupsView = (ListView) v.findViewById(R.id.yourGroups);

        yourGroupsTitle = (TextView) v.findViewById(R.id.yourGroups_title);
        yourGroupsTitle.setText(getActivity().getResources().getString(R.string.owned_groups));
       
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {

			final ProgressDialog dlg = new ProgressDialog(getActivity());
		    dlg.setTitle(getResources().getString(R.string.please_wait));
		    dlg.setMessage(getResources().getString(R.string.loading_groups));
	        
		    List<String> list = currentUser.getList("ownedGroups");
		    
		    if (list!=null) {
		    
		    if (list.isEmpty()) {
		    	
			    yourGroupsView.setVisibility(View.GONE);
		    	Toast.makeText(getActivity(), getResources().getString(R.string.dont_own_groups), Toast.LENGTH_LONG)
	            .show();
			    dlg.dismiss();
			    	
			    } else {
			
			GroupsCustomArrayAdapter adapter = new GroupsCustomArrayAdapter(getActivity(), R.layout.group_adapter_item, R.id.owned_group_name_adapter , list);
		    yourGroupsView.setAdapter(adapter);
		    dlg.dismiss();
			    
			    }
		    
		    } else {
		    	
		    	yourGroupsView.setVisibility(View.GONE);
		    	Toast.makeText(getActivity(), getResources().getString(R.string.dont_own_groups), Toast.LENGTH_LONG)
	            .show();
			    dlg.dismiss();
		    	
		    }
        
        } else {
        	Toast.makeText(getActivity(), getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(getActivity(), Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }

        
    };
    
    @Override
	public void onResume() {
    	super.onResume();
    	
    	final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser!=null) {
    	
    	getActivity().findViewById(R.id.backToHome_YG_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(),Home.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			});	
    	
        } else {
        	Toast.makeText(getActivity(), getResources().getString(R.string.not_logged_in), Toast.LENGTH_LONG)
            .show();
        	Intent intent = new Intent(getActivity(), Login.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       	  	startActivity(intent);
        }
    	
    };

    @Override
	public void onPause() {
        super.onPause();
    };
  
}