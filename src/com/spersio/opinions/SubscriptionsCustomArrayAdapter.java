package com.spersio.opinions;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SubscriptionsCustomArrayAdapter extends ArrayAdapter<String> {
	
	private static List<String> list;	
	
	private static Context context; 
	private static int row_layout_resource;
	
	protected static int textView;
	
	public static List<String> toUnsubscribe = new ArrayList<String>();

	private static class ViewHolder
	{
		TextView subscriptionUsername;
		CheckBox checkBox;
		
		int position;
	}
	
	

	public SubscriptionsCustomArrayAdapter(Context context, int row_layout_resource,int row_textview,
			List<String> data) {
		super(context, row_layout_resource, row_textview, data);
		
		SubscriptionsCustomArrayAdapter.list = data;	
		SubscriptionsCustomArrayAdapter.context = context;
		SubscriptionsCustomArrayAdapter.row_layout_resource = row_layout_resource;
		
		textView = row_textview;
		
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		
		final ViewHolder holder;
		
		if (convertView == null) 
		{
			LayoutInflater mInflater = (LayoutInflater) SubscriptionsCustomArrayAdapter.context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
										
			convertView = mInflater.inflate(SubscriptionsCustomArrayAdapter.row_layout_resource, parent, false);
						
			holder = new ViewHolder();	
			
			holder.subscriptionUsername = (TextView) convertView.findViewById(textView);
			
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.subscription_checkbox_adapter);
			
			holder.position = position;
			
			holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if (isChecked) {
						toUnsubscribe.add(list.get(holder.position).toString());
					} else {
						toUnsubscribe.remove(list.get(holder.position).toString());
					}
					
				}
			});
			
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
			holder.subscriptionUsername.setText(list.get(position).toString());
		
		holder.position = position;
		
		return convertView;
	}	
	
	
}