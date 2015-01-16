package com.spersio.opinions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SavedQuestionsCustomSimpleAdapter extends SimpleAdapter {
	
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
	
	

	public SavedQuestionsCustomSimpleAdapter(Context context,
			List<HashMap<String, Object>> data, int row_layout_resource, String[] from, int[] to) {
		super(context, data, row_layout_resource, from, to);
		
		SavedQuestionsCustomSimpleAdapter.listMap = data;	
		SavedQuestionsCustomSimpleAdapter.context = context;
		SavedQuestionsCustomSimpleAdapter.row_layout_resource = row_layout_resource;
		
		viewList = to;
		fromList = from;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		final ViewHolder holder;
		
		if (convertView == null) 
		{
			LayoutInflater mInflater = (LayoutInflater) SavedQuestionsCustomSimpleAdapter.context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
										
			convertView = mInflater.inflate(SavedQuestionsCustomSimpleAdapter.row_layout_resource, parent, false);
						
			holder = new ViewHolder();	
			
			holder.askerInfo = (TextView) convertView.findViewById(viewList[0]);
			holder.questionText = (TextView) convertView.findViewById(viewList[1]);
			
			holder.deleteButton = (ImageButton) convertView.findViewById(R.id.delete_adapter);
			holder.showResultsButton = (ImageButton) convertView.findViewById(R.id.results_adapter);
			
			holder.position = position;
			
			holder.deleteButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					
					HashMap<String, Object> question = SavedQuestionsCustomSimpleAdapter.listMap.get(holder.position);
					
					toDelete.add(question.get("questionID").toString());
					
					SavedQuestionsCustomSimpleAdapter.listMap.remove(holder.position);
					notifyDataSetChanged();
					
				}
				
			});
			
			holder.showResultsButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					HashMap<String, Object> question = SavedQuestionsCustomSimpleAdapter.listMap.get(holder.position);
					
					String[] answers = {question.get("answer1").toString(),
										question.get("answer2").toString(),
										question.get("answer3").toString(),
										question.get("answer4").toString(),
										question.get("answer5").toString()};
		
					int[] numberForAnswer = {(int) question.get("nA1"),
											(int) question.get("nA2"),
											(int) question.get("nA3"),
											(int) question.get("nA4"),
											(int) question.get("nA5")};
					
					double[] percentageForAnswer = new double[5];
					String[] pcStrings = {"pcA1", "pcA2", "pcA3", "pcA4", "pcA5"};
					
					for (int i=0; i< 5; i++) {
						if (question.get(pcStrings[i]) instanceof Integer) {
							percentageForAnswer[i] = ((Integer) question.get(pcStrings[i])).doubleValue();
							} else {
							percentageForAnswer[i] = (double) (question.get(pcStrings[i]));
						}
					}
					
					Question questionForResults = new Question(question.get("questionID").toString(),
							question.get("questionText").toString(),
							"noTag",
							question.get("askerUsername").toString(),
							question.get("groupname").toString(),
							question.get("createdAt").toString(),
							(int) question.get("nbrAnswers"),
							(int) question.get("nA"),
							0,
							answers,
							numberForAnswer,
							percentageForAnswer,
							(boolean) question.get("group"),
							(boolean) question.get("subscribersOnly"),
							true);
					
					Intent intent = new Intent(context, Results.class);
					intent.putExtra(CustomPushReceiver.questionKey, questionForResults);
					
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