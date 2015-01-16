package com.spersio.opinions;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable{
	
	protected String questionID;
	protected String text;
	protected String notificationTag;
	protected String askerUsername;
	protected String groupName;
	protected String createdAt;
	protected int nbrAnswers;
	protected int numberOfResponses;
	protected int notificationID;
	protected String[] answers;
	protected int[] numberForAnswer;
	protected double[] percentageForAnswer;
	protected boolean group;
	protected boolean subscribersOnly;
	protected boolean savedQuestion;
	
	public Question (String questionID,
					String text,  
					String notificationTag,
					String askerUsername,
					String groupName,
					String createdAt,
					int nbrAnswers,
					int numberOfResponses,
					int notificationID,
					String[] answers,  
					int[] numberForAnswer, 
					double[] percentageForAnswer, 
					boolean group, 
					boolean subscribersOnly,
					boolean savedQuestion) {
		
		this.questionID = questionID;
		this.text = text;
		this.notificationTag = notificationTag;
		this.askerUsername = askerUsername;
		this.groupName = groupName;
		this.createdAt = createdAt;
		this.nbrAnswers = nbrAnswers;
		this.numberOfResponses = numberOfResponses;
		this.notificationID = notificationID;
		this.answers = answers;
		this.numberForAnswer = numberForAnswer;
		this.percentageForAnswer = percentageForAnswer;
		this.group = group;
		this.subscribersOnly = subscribersOnly;
		this.savedQuestion = savedQuestion;
	}
	
	public Question (String questionID,
					String text,  
					String notificationTag,
					String askerUsername,
					String groupName,
					int nbrAnswers,
					int notificationID,
					String[] answers,
					boolean group, 
					boolean subscribersOnly) {

		this.questionID = questionID;
		this.text = text;
		this.notificationTag = notificationTag;
		this.askerUsername = askerUsername;
		this.groupName = groupName;
		this.createdAt = null;
		this.nbrAnswers = nbrAnswers;
		this.numberOfResponses = -1;
		this.notificationID = notificationID;
		this.answers = answers;
		this.numberForAnswer = null;
		this.percentageForAnswer = null;
		this.group = group;
		this.subscribersOnly = subscribersOnly;
		this.savedQuestion = false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(questionID);
		dest.writeString(text);
		dest.writeString(notificationTag);
		dest.writeString(askerUsername);
		dest.writeString(groupName);
		dest.writeString(createdAt);
		dest.writeInt(nbrAnswers);
		dest.writeInt(numberOfResponses);
		dest.writeInt(notificationID);
		dest.writeStringArray(answers);
		dest.writeIntArray(numberForAnswer);
		dest.writeDoubleArray(percentageForAnswer);
		dest.writeByte((byte) (group ? 1 : 0)); 
		dest.writeByte((byte) (subscribersOnly ? 1 : 0)); 
		dest.writeByte((byte) (savedQuestion ? 1 : 0));
		
		
	}
	
	public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>()
			{
			    @Override
			    public Question createFromParcel(Parcel source)
			    {
			        return new Question(source);
			    }

			    @Override
			    public Question[] newArray(int size)
			    {
				return new Question[size];
			    }
	};

	public Question(Parcel in) {
		
		this.questionID = in.readString();
		this.text = in.readString();
		this.notificationTag = in.readString();
		this.askerUsername = in.readString();
		this.groupName = in.readString();
		this.createdAt = in.readString();
		this.nbrAnswers = in.readInt();
		this.numberOfResponses = in.readInt();
		this.notificationID = in.readInt();
		this.answers = in.createStringArray();
		this.numberForAnswer = in.createIntArray();
		this.percentageForAnswer = in.createDoubleArray();
		this.group = in.readByte() !=0;
		this.subscribersOnly = in.readByte() !=0;
		this.savedQuestion = in.readByte() !=0;
		
	}
	
}
