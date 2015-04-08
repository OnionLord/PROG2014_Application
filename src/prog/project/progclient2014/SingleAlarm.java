package prog.project.progclient2014;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class SingleAlarm extends LinearLayout {
	Context mContext;
	TextView textViewTime;
	Button btnSingleAlarmItemCancel;
	AlarmData alarmData;
	private int position;
	
	public SingleAlarm(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.prog_single_alarm_view, this);
		textViewTime = (TextView)layout.findViewById(R.id.txt_single);
		btnSingleAlarmItemCancel = (Button)findViewById(R.id.btn_single_del);
		
		btnSingleAlarmItemCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onRemoveButtonClickListner != null)
				onRemoveButtonClickListner.onClicked(alarmData.hh, alarmData.mm, alarmData.reqCode, position);
			}
		});
	}
	
	public interface OnRemoveButtonClickListner{
		void onClicked(int hh, int mm ,int reqCode, int position);
	}
	
	OnRemoveButtonClickListner onRemoveButtonClickListner;
	
	void setOnRemoveButtonClickListner(OnRemoveButtonClickListner onRemoveButtonClickListner){
		this.onRemoveButtonClickListner = onRemoveButtonClickListner;
	}
	
	public boolean setData(AlarmData alarmData, int position){
		
		this.alarmData = alarmData;
		this.position = position;
		
		String hh;
		String mm;
		
		if(alarmData.hh < 10)
		{
			hh = "0"+alarmData.hh;
		}
		else
		{
			hh = Integer.toString(alarmData.hh);
		}
		if(alarmData.mm < 10)
		{
			mm = "0"+alarmData.mm;
		}
		else
		{
			mm = Integer.toString(alarmData.mm);
		}
		textViewTime.setText(hh+":"+mm);
		
		return true;
	}
	

}
