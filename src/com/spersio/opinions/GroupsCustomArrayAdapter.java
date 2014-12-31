package com.spersio.opinions;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class GroupsCustomArrayAdapter extends ArrayAdapter<String> {
	
	private static List<String> list;	
	
	private static Context context; 
	private static int row_layout_resource;
	
	protected static int textView;

	private static class ViewHolder
	{
		TextView groupname;
		ImageButton detailsButton;
		
		int position;
	}
	
	

	public GroupsCustomArrayAdapter(Context context, int row_layout_resource,int row_textview,
			List<String> data) {
		super(context, row_layout_resource, row_textview, data);
		
		GroupsCustomArrayAdapter.list = data;	
		GroupsCustomArrayAdapter.context = context;
		GroupsCustomArrayAdapter.row_layout_resource = row_layout_resource;
		
		textView = row_textview;
		
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		
		final ViewHolder holder;
		
		if (convertView == null) 
		{
			LayoutInflater mInflater = (LayoutInflater) GroupsCustomArrayAdapter.context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
										
			convertView = mInflater.inflate(GroupsCustomArrayAdapter.row_layout_resource, parent, false);
						
			holder = new ViewHolder();	
			
			holder.groupname = (TextView) convertView.findViewById(textView);
			
			holder.detailsButton = (ImageButton) convertView.findViewById(R.id.details_adapter);
			
			holder.position = position;
			
			holder.detailsButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(context, Group.class);
					intent.putExtra("groupname", list.get(holder.position));
					
					context.startActivity(intent);
					
				}
			});
			
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
			holder.groupname.setText(list.get(position));
		
		holder.position = position;
		
		return convertView;
	}	
	
	
}