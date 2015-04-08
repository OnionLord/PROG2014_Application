package prog.project.progclient2014;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import prog.project.progclient2014.db.BasicThings;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmView extends Activity {

	private AlarmManager alarmManager;
	private Context mContext;
	public static final int DEFAULT_ALARM_REQUEST = 800;
	private int max_no = 0;
	TimePicker timePickerAlarmTime;
	Button btnAddAlarm;
	Button btnExit;
	ListView listViewAlarm;
	ArrayList<AlarmData> arrayListAlarmTimeItem = new ArrayList<AlarmData>(); 
	TextView title;
//	GregorianCalendar currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+09:00"));
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

	
	AdapterAlarm arrayAdapterAlarmList;

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	BasicThings conn_inform = new BasicThings();
	
	String id;
	String name;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prog_alarm_view);
        Intent intent = getIntent();
		id = intent.getExtras().getString("id");
		name = intent.getExtras().getString("name");
		title = (TextView)findViewById(R.id.txt_list_user);
		title.setText(name+"("+id+")님의 알람 목록 입니다.");
		
		mContext = getApplicationContext();
        
        timePickerAlarmTime = (TimePicker)findViewById(R.id.picker_list);
	   	 btnAddAlarm = (Button)findViewById(R.id.btn_list_add);
	   	 listViewAlarm	= (ListView)findViewById(R.id.list_list);
	   	 btnExit = (Button)findViewById(R.id.btn_list_exit);
	   	 
	   	 timePickerAlarmTime.setIs24HourView(false);
	   	 
	   	 arrayAdapterAlarmList = new AdapterAlarm(mContext, arrayListAlarmTimeItem);
	   	 listViewAlarm.setAdapter(arrayAdapterAlarmList);
	       
	   	 alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	   	 alarmManager.setTimeZone("GMT+09:00");
	     getMax();  
			btnExit.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					clearAlarm();
					finish();

				}
				
			});
	       
			btnAddAlarm.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					
					int hh = timePickerAlarmTime.getCurrentHour();
					int mm = timePickerAlarmTime.getCurrentMinute();
					int reqCode = DEFAULT_ALARM_REQUEST+arrayListAlarmTimeItem.size();
					int i =arrayListAlarmTimeItem.size();
					
					
					
					GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
					
					int currentYY = currentCalendar.get(Calendar.YEAR);
					int currentMM = currentCalendar.get(Calendar.MONTH);
					int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
					
					gregorianCalendar.set(currentYY, currentMM, currentDD, hh, mm,00);
	
					Timestamp temp;

					//현재 시간보다 앞선 시간을 알람 설정시 다음날 울리도록 설정함.
					if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
						gregorianCalendar.set(currentYY, currentMM, currentDD+1, hh, mm,00);
						Log.i("MM",Integer.toString(mm));
						Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
						temp = new Timestamp(currentYY-1900, currentMM, currentDD + 1, hh, mm, 00, 00);
					}
					else
					{
						temp = new Timestamp(currentYY-1900, currentMM, currentDD, hh, mm, 00, 00);
					}
					Date nowDate = new Date(currentYY, currentMM, currentDD);
					getMax();
					Insert_DB(temp);
					arrayListAlarmTimeItem.add(new AlarmData(hh, mm, max_no));
					arrayAdapterAlarmList.notifyDataSetChanged();
					Intent intent = new Intent(AlarmView.this, AlarmPopup.class);
					intent.putExtra("id", id);
					intent.putExtra("time", hh+":"+mm);
					intent.putExtra("data", "날짜 : " + currentCalendar.getTime().toLocaleString());
					intent.putExtra("reqCode", max_no);
					
					Toast.makeText(mContext, "추가 완료!", 0).show();
					
					PendingIntent pi = PendingIntent.getActivity(AlarmView.this, max_no, intent,PendingIntent.FLAG_UPDATE_CURRENT );
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, gregorianCalendar.getTimeInMillis() ,AlarmManager.INTERVAL_DAY, pi);
					
					
			    	
					
				}
			});
			
			
	}
	
	private void getMax()
	{
		Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	BasicThings conn_inform = new BasicThings();
    	
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(BasicThings.url, BasicThings.user, BasicThings.pass);
            
            stmt = conn.prepareStatement("select max(no) from alarminfo");
            rs = stmt.executeQuery();
            
            while(rs.next()) {
            	max_no = rs.getInt(1);
            
            }

            
        }
        catch(Exception e) {
            e.printStackTrace();
            
        } 
        finally
        {
        	if(stmt != null) try{ stmt.close(); }catch(SQLException ex){}
        	 if(conn != null) try{ conn.close(); }catch(SQLException ex){}
        	 if(rs != null) try{ rs.close();} catch(SQLException ex){}
        }
	}
	
	private void Insert_DB(Timestamp nowDate)
	{
		Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	BasicThings conn_inform = new BasicThings();
    	
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(BasicThings.url, BasicThings.user, BasicThings.pass);
            
            max_no +=1;
            Log.i("MAXNO", Integer.toString(max_no));
            stmt = conn.prepareStatement("insert into alarminfo values(?,?,?)");
            stmt.setInt(1, max_no);
            stmt.setString(2, id);
            stmt.setTimestamp(3, nowDate);
            stmt.executeUpdate();
            
           
            
        }
        catch(Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
        	.setTitle("Error")
        	.setMessage("접속 실패 . 다시 시도하세요. ")
        	.setPositiveButton("확인.", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
            //tv.setText(e.toString());
        } 
        finally
        {
        	if(stmt != null) try{ stmt.close(); }catch(SQLException ex){}
        	 if(conn != null) try{ conn.close(); }catch(SQLException ex){}
        	 if(rs != null) try{ rs.close();} catch(SQLException ex){}
        }
	}
	
	//모든 알람 초기
	private void clearAlarm()
	{
		int i;
		AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//	    Intent i = new Intent(mContext ,AlarmTestForHaruActivity.class);
	 	Intent intent = new Intent(mContext, AlarmPopup.class);
	 	PendingIntent pi = null;
	 	for ( i = 0 ; i <= max_no ; i ++)
	 	{
	 		pi = PendingIntent.getActivity(mContext, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	 		alarmManager.cancel(pi);
	 	}
	}
	
	 @SuppressWarnings("deprecation")
	@Override
	 //�ٽ� ������ �ؾ��� �͵�.
	    protected void onResume() {
	    	super.onResume();
			GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
			/*
			AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//		    Intent i = new Intent(mContext ,AlarmTestForHaruActivity.class);
		 	Intent intent = new Intent(mContext, AlarmPopup.class);
		 	
		 	PendingIntent pi = PendingIntent.getActivity(mContext, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		    alarmManager.cancel(pi);*/
			clearAlarm();
	    	try {
	    		arrayListAlarmTimeItem.clear();
	            Class.forName("com.mysql.jdbc.Driver");
	            conn = DriverManager.getConnection(conn_inform.url, conn_inform.user, conn_inform.pass);
	            
	            //����ð� ������ �͵��� �����Ѵ�.
	            stmt = conn.prepareStatement("select no, id, alarmtime from alarminfo A where A.alarmtime >= now() and id = ?");
	            stmt.setString(1, id);
	            rs = stmt.executeQuery();
	            Date result_date;
	            Timestamp result_time;
	            int hh;
	            int mm;
	            int reqCode;
	  
	            while(rs.next()) {
	            	result_time = rs.getTimestamp(3);
	            	
	            	hh = result_time.getHours();
	            	mm = result_time.getMinutes();
	            	reqCode = rs.getInt(1);
	            	
	            	arrayListAlarmTimeItem.add(new AlarmData(hh, mm, reqCode));
	            	Intent intent = new Intent(AlarmView.this, AlarmPopup.class);
					intent.putExtra("time", hh+":"+mm);
					intent.putExtra("data", "�˶�: " + result_time.toLocaleString());
					intent.putExtra("reqCode", reqCode);
					//title.setText(result_time.getYear()+""+result_time.getMonth()+""+result_time.getDay()+"���� �˶� ����Ʈ");

					gregorianCalendar.set(result_time.getYear()+1900, result_time.getMonth(), result_time.getDay(), hh, mm,00);
					Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
									
					PendingIntent pi = PendingIntent.getActivity(AlarmView.this, reqCode, intent,PendingIntent.FLAG_UPDATE_CURRENT );
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, gregorianCalendar.getTimeInMillis() ,AlarmManager.INTERVAL_DAY, pi);
	            	
	            }
	            arrayAdapterAlarmList.notifyDataSetChanged();
	            
	        }
	        catch(Exception e) {
	            e.printStackTrace();
	            //tv.setText(e.toString());
	            new AlertDialog.Builder(this)
	        	.setTitle("Error")
	        	.setMessage("접속실패. 다시시도하세.")
	        	.setPositiveButton("확인 ", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();
	        } 
	        finally
	        {
	        	if(stmt != null) try{ stmt.close(); }catch(SQLException ex){}
	        	 if(conn != null) try{ conn.close(); }catch(SQLException ex){}
	        	 if(rs != null) try{ rs.close();} catch(SQLException ex){}
	        }

	    }
	 
	 @Override
	 public void onBackPressed() {
	    Log.d("CDA", "onBackPressed Called");
	 }

}
