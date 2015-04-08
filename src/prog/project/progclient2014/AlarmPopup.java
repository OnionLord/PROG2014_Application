package prog.project.progclient2014;

//이부분에서 소켓 받아 알람 끝내도록 처리
//소리도 날 수 있으면 내보고.

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

import android.media.MediaPlayer;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import prog.project.progclient2014.AlarmView.*;

//import processing.net.*;
//import processing.core.*;
import java.net.*;
import java.io.*;

//import prog.project.progclient2014.client.ClientToProcessing;
import prog.project.progclient2014.db.BasicThings;

public class AlarmPopup extends Activity implements SensorEventListener{

	TextView textViewAlarmedTime;
	
	private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    
    private int shaking = 0;
 
    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;
 
    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    private TextView test;
    //private ClientToProcessing client;
    
    private int shake_num;
    private TextView txt;
    private String send_message;
    
    private int completed;
    private String messageFromServer;
    public Runnable t;
    private static MediaPlayer mp;
    class netThread extends Thread
    {
    	public void run()
    	{
    		BasicThings conn_inform = new BasicThings();
    		
    		try {
    			
    			//System.out.println("Connect to Server Server IP :" + serverIp);
    			// º“ƒœ¿ª ª˝º∫«œø© ø¨∞·¿ª ø‰√ª«—¥Ÿ.
    			final Socket socket = new Socket(conn_inform.kinect_url, conn_inform.kinect_port); 

    			// º“ƒœ¿« ¿‘∑¬Ω∫∆Æ∏≤¿ª æÚ¥¬¥Ÿ.
    			InputStream in = socket.getInputStream();
    			final DataInputStream dis = new DataInputStream(in);
    			OutputStream out = socket.getOutputStream();
    	        final DataOutputStream dos = new DataOutputStream(out);

    			// º“ƒœ¿∏∑Œ ∫Œ≈Õ πﬁ¿∫ µ•¿Ã≈Õ∏¶ √‚∑¬«—¥Ÿ.
    	        dos.writeUTF(send_message);
    	        final String serverNo = dis.readUTF();
    	        final int action_No = Integer.parseInt(serverNo);
    	      
    	        new Thread(new Runnable()
	        	{
	        		
	    			@Override
	    			public void run() {
	    				runOnUiThread(new Runnable(){
	    					@Override
	    					public void run()
	    					{
	    						if(action_No > 10)
	    						{
	    							switch(action_No%5)
		    						{
		    						case 0:
		    							txt.setTextColor(Color.parseColor("#FFCC00"));
		    							txt.setText("왼손으로 노란 동그라미에 위치하여\n빨간 동그라미로 만드세요.\n각 동그라미에 5초씩 있으면 됩니다.");
		    							break;
		    						case 1:
		    							
		    							txt.setTextColor(Color.parseColor("#0000FF"));
		    							
		    							txt.setText("오른손으로 파란 동그라미에 위치하여\n빨간 동그라미로 만드세요.\n각 동그라미에 5초씩 있으면 됩니다.");
		    							break;
		    						case 2:
		    							txt.setText("머리로 하얀 동그라미에 위치하여\n빨간 동그라미로 만드세요.\n각 동그라미에 5초씩 있으면 됩니다.");
		    							break;
		    						case 3:
		    							txt.setTextColor(Color.parseColor("#FF00FF"));
		    							
		    							txt.setText("왼발로 자줏빛 동그라미에 위치하여\n빨간 동그라미로 만드세요.\n각 동그라미에 5초씩 있으면 됩니다.");
		    							break;
		    						case 4:
		    							txt.setTextColor(Color.parseColor("#00FFFF"));
		    							txt.setText("오른발로 초록 동그라미에 위치하여\n빨간 동그라미로 만드세요.\n각 동그라미에 5초씩 있으면 됩니다.");
		    							break;
		    						default:
		    								break;
		    						}
	    						}
	    						else
	    						{
	    							switch(action_No%5)
		    						{
		    						case 0:
		    							txt.setText("서버 화면의 정 가운데에 빛을 비추어 주세요");
		    							break;
		    						case 1:
		    							txt.setText("왼쪽 윗구석에 빛을 비추어 주세요");
		    							break;
		    						case 2:
		    							txt.setText("왼쪽 아랫구석에 빛을 비추어 주세요");
		    							break;
		    						case 3:
		    							txt.setText("오른쪽 윗구석에 빛을 비추어 주세요");
		    							break;
		    						case 4:
		    							txt.setText("오른쪽 아랫구석 빛을 비추어 주세요");
		    							break;
		    						default:
		    								break;
		    						}
	    						}
	    						
	    						
	    					}
	    				}
	    				);
	    				
	    			}
	        		
	        	}).start();
	        	
    	        
    	        messageFromServer = dis.readUTF();
    	        Log.i("TEST",messageFromServer);
    	        completed = 1;
    	        
    	        if(messageFromServer.equals("E"))
    	        {
    	        	new Thread(new Runnable()
    	        	{
    	        		
    	    			@Override
    	    			public void run() {
    	    				runOnUiThread(new Runnable(){
    	    					@Override
    	    					public void run()
    	    					{
    	    						new AlertDialog.Builder(AlarmPopup.this)
    	    				    	.setTitle("Alarm is done")
    	    				    	.setMessage("Alarm is done")
    	    				    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	    							
    	    							@Override
    	    							public void onClick(DialogInterface dialog, int which) {
    	    			    	        	try {
												dis.close();
												dos.close();
	    	    			        			socket.close();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
    	    								finish();
    	    								
    	    							}
    	    						}).show();
    	    						
    	    					}
    	    				}
    	    				);
    	    				
    	    			}
    	        		
    	        	}).start();
    	        	
    	        	/*synchronized(t)
    				{
    					try {
    	                    t.notify();
    	                }
    	                catch (Exception e) {
    	                }
    				}*/
    	        }
    	        
    	        //setTextLabel(messageFromServer);
    	        //txt.setText(messageFromServer);
    	        
    			// Ω∫∆Æ∏≤∞˙ º“ƒœ¿ª ¥›¥¬¥Ÿ.
    			dis.close();
    			dos.close();
    			socket.close();
    			System.out.println("Connection Closed.");
    		} catch(ConnectException ce) {
    			ce.printStackTrace();
    		} catch(IOException ie) {
    			ie.printStackTrace();
    		} catch(Exception e) {
    			e.printStackTrace();  
    		}  
    	}
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		messageFromServer = "";
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.prog_alarm_popup);
		textViewAlarmedTime = (TextView)findViewById(R.id.txt_popup_content);
		Intent intent = getIntent();
		String time = intent.getStringExtra("time");
		String id = intent.getStringExtra("id");
		int reqCode = intent.getIntExtra("reqCode", 0);
		textViewAlarmedTime.setText(time);
		 sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
         accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		shake_num = ((int)(Math.random()*50)) + 20;
    	test = (TextView)findViewById(R.id.test1);
    	txt = (TextView)findViewById(R.id.txt_from_server);
    	int activity_num = ((int)(Math.random()*10));
//    	send_message = id+","+Integer.toString(activity_num)+","+Integer.toString(reqCode);
    	send_message = Integer.toString(activity_num);
    	mp = MediaPlayer.create(this, R.raw.yee);
		mp.setLooping(true);
		mp.start();
		
    	test.setText("흔든 횟수 : 0 / "+shake_num);
    	
    	start_thread();
    	
		Delete_DB(reqCode);
		 

	}
	/*
	private void alert_success()
	{
		new AlertDialog.Builder(this)
    	.setTitle("Alarm is done")
    	.setMessage("Alarm is done")
    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
	}*/
	
	private void start_thread()
	{
		netThread nT = new netThread();
    	nT.start();
	}
	
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
	
	private void setTextLabel(String str)
	{
		this.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				//txt.setText(str);
			}
		});
	}//Handler을 사용하자.
	
	//서버를 찾을 수 없을때 흔들어서 종료하도록 한다.
	@Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
            SensorManager.SENSOR_DELAY_GAME);
    }
 
    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
 
                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;
 
                if (speed > SHAKE_THRESHOLD) {
                	shaking++;
                	test.setText("흔든 횟수 : "+shaking+" / "+shake_num);
                	if(shaking >= shake_num)
                	{
                		new AlertDialog.Builder(AlarmPopup.this)
				    	.setTitle("Alarm is done")
				    	.setMessage("Alarm is done")
				    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
			    	        	
								finish();
								
							}
						}).show();
                	}
                }
 
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
 
        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override//Back눌러서 나가는거 방지.
	 public void onBackPressed() {
	    Log.d("CDA", "onBackPressed Called");
	 }
	
	
}

