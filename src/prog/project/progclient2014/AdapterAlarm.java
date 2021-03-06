package prog.project.progclient2014;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import prog.project.progclient2014.SingleAlarm.*;
import prog.project.progclient2014.db.BasicThings;

public class AdapterAlarm extends BaseAdapter{

	Context mContext;
	ArrayList<String> mData;
	LayoutInflater mInflate;
	ArrayList<AlarmData> arrayListAlarmDatas;
	
	public AdapterAlarm(Context context, ArrayList<AlarmData> arrayListAlarmDatas) {
		mContext = context;
		this.arrayListAlarmDatas = arrayListAlarmDatas;
		mInflate = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayListAlarmDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return arrayListAlarmDatas.get(position).reqCode;
	}
	
	public boolean removeData(int position){
		arrayListAlarmDatas.remove(position);
		notifyDataSetChanged();
		return false;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SingleAlarm layoutSingleAlarmItem = (SingleAlarm) convertView;
		
		if (layoutSingleAlarmItem == null) {
			layoutSingleAlarmItem = new SingleAlarm(mContext);
			layoutSingleAlarmItem.setOnRemoveButtonClickListner(onRemoveButtonClickListner);
		}
		layoutSingleAlarmItem.setData(arrayListAlarmDatas.get(position), position);
		return layoutSingleAlarmItem;
	}
	
	OnRemoveButtonClickListner onRemoveButtonClickListner = new OnRemoveButtonClickListner() {
		
		@Override
		public void onClicked(int hh, int mm, int reqCode, int position) {
			Toast.makeText(mContext, "position : "+position + " reqCode :"+reqCode, 0).show();
			 AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//			    Intent i = new Intent(mContext ,AlarmTestForHaruActivity.class);
			 	Intent intent = new Intent(mContext, AlarmPopup.class);
			    //Toast.makeText(mContext, "reqCode : "+reqCode, 0).show();
			    PendingIntent pi = PendingIntent.getActivity(mContext, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			    alarmManager.cancel(pi);
			    Delete_DB(reqCode);
	            
			removeData(position);
		}
	};
	
	private void Delete_DB(int no)
	{
		Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	BasicThings conn_inform = new BasicThings();
    	
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(conn_inform.url, conn_inform.user, conn_inform.pass);
            
            
            stmt = conn.prepareStatement("delete from alarminfo where no=?");
            stmt.setInt(1, no);
            stmt.executeUpdate();
            

        }
        catch(Exception e) {
            e.printStackTrace();
            Log.i("MAXNO","EE");
            //tv.setText(e.toString());
        } 
        finally
        {
        	if(stmt != null) try{ stmt.close(); }catch(SQLException ex){}
        	 if(conn != null) try{ conn.close(); }catch(SQLException ex){}
        	 if(rs != null) try{ rs.close();} catch(SQLException ex){}
        }
	}

}
