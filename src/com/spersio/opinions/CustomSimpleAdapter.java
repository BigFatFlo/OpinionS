package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class CustomSimpleAdapter extends SimpleAdapter {
	
	private static List<HashMap<String, Object>> listMap;	
	
	private static Context context; 
	private static int row_layout_resource;
	
	protected static int[] viewList;
	protected static String[] fromList;
	
	public static List<String> toDelete = new ArrayList<String>();

	private static class ViewHolder
	{
		TextView askerInfo;
		TextView questionText;		
		ImageButton deleteButton;
		ImageButton showResultsButton;
		
		int position;
	}
	
	

	public CustomSimpleAdapter(Context context,
			List<HashMap<String, Object>> data, int row_layout_resource, String[] from, int[] to) {
		super(context, data, row_layout_resource, from, to);
		
		CustomSimpleAdapter.listMap = data;	
		CustomSimpleAdapter.context = context;
		CustomSimpleAdapter.row_layout_resource = row_layout_resource;
		
		viewList = to;
		fromList = from;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		final ViewHolder holder;
		
		if (convertView == null) 
		{
			LayoutInflater mInflater = (LayoutInflater) CustomSimpleAdapter.context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
										
			convertView = mInflater.inflate(CustomSimpleAdapter.row_layout_resource, parent, false);
						
			holder = new ViewHolder();	
			
			holder.askerInfo = (TextView) convertView.findViewById(viewList[0]);
			holder.questionText = (TextView) convertView.findViewById(viewList[1]);
			
			holder.deleteButton = (ImageButton) convertView.findViewById(R.id.delete_adapter);
			holder.showResultsButton = (ImageButton) convertView.findViewById(R.id.results_adapter);
			
			holder.position = position;
			
			holder.deleteButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					
					HashMap<String, Object> question = CustomSimpleAdapter.listMap.get(holder.position);
					
					toDelete.add(question.get("questionID").toString());
					
					CustomSimpleAdapter.listMap.remove(holder.position);
					notifyDataSetChanged();
					
				}
				
			});
			
			holder.showResultsButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					HashMap<String, Object> question = CustomSimpleAdapter.listMap.get(holder.position);
					
					
					Intent intent = new Intent(context, Results.class);
					
					Bundle extras = new Bundle();
					extras.putString(CustomPushReceiver.ID, question.get("questionID").toString());
					extras.putString(CustomPushReceiver.text, question.get("questionText").toString());
					extras.putInt(CustomPushReceiver.nbrAnswers, (int) question.get("nbrAnswers"));
					extras.putString(CustomPushReceiver.A1, question.get("answer1").toString());
					extras.putString(CustomPushReceiver.A2, question.get("answer2").toString());
					extras.putString(CustomPushReceiver.A3, question.get("answer3").toString());
					extras.putString(CustomPushReceiver.A4, question.get("answer4").toString());
					extras.putString(CustomPushReceiver.A5, question.get("answer5").toString());
					extras.putInt(CustomPushReceiver.nA, (int) question.get("nA"));
					extras.putInt(CustomPushReceiver.nA1, (int) question.get("nA1"));
					extras.putInt(CustomPushReceiver.nA2, (int) question.get("nA2"));
					extras.putInt(CustomPushReceiver.nA3, (int) question.get("nA3"));
					extras.putInt(CustomPushReceiver.nA4, (int) question.get("nA4"));
					extras.putInt(CustomPushReceiver.nA5, (int) question.get("nA5"));
					if (question.get("pcA1") instanceof Integer) {
					extras.putDouble(CustomPushReceiver.pcA1, ((Integer) question.get("pcA1")).doubleValue());
					} else {
					extras.putDouble(CustomPushReceiver.pcA1, (double) (question.get("pcA1")));
					}
					if (question.get("pcA2") instanceof Integer) {
					extras.putDouble(CustomPushReceiver.pcA2, ((Integer) question.get("pcA2")).doubleValue());
					} else {
					extras.putDouble(CustomPushReceiver.pcA2, (double) (question.get("pcA2")));
					}
					if (question.get("pcA3") instanceof Integer) {
					extras.putDouble(CustomPushReceiver.pcA3, ((Integer) question.get("pcA3")).doubleValue());
					} else {
					extras.putDouble(CustomPushReceiver.pcA3, (double) (question.get("pcA3")));
					}
					if (question.get("pcA4") instanceof Integer) {
					extras.putDouble(CustomPushReceiver.pcA4, ((Integer) question.get("pcA4")).doubleValue());
					} else {
					extras.putDouble(CustomPushReceiver.pcA4, (double) (question.get("pcA4")));
					}
					if (question.get("pcA5") instanceof Integer) {
					extras.putDouble(CustomPushReceiver.pcA5, ((Integer) question.get("pcA5")).doubleValue());
					} else {
					extras.putDouble(CustomPushReceiver.pcA5, (double) (question.get("pcA5")));
					}
					extras.putInt(CustomPushReceiver.radius, (int) question.get("radius"));
					extras.putBoolean(CustomPushReceiver.subscribersOnly, (boolean) question.get("subscribersOnly"));
					extras.putBoolean(CustomPushReceiver.international, (boolean) question.get("international"));
					extras.putBoolean(CustomPushReceiver.around, (boolean) question.get("around"));
					extras.putString(CustomPushReceiver.askerUsername, question.get("askerUsername").toString());
					extras.putBoolean(CustomPushReceiver.savedQuestion, true);
					extras.putString(CustomPushReceiver.country, question.get("country").toString());
					extras.putString(CustomPushReceiver.createdAt, question.get("createdAt").toString());
					
					intent.putExtras(extras);
					
					context.startActivity(intent);
						
				}
				
			});
			
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
			holder.askerInfo.setText(listMap.get(position).get(fromList[0]).toString());
			holder.questionText.setText(listMap.get(position).get(fromList[1]).toString());
			
		
		holder.position = position;
		
		return convertView;
	}	
	
	
}