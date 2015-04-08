package prog.project.progclient2014;

/*
 * http://capdroid.wordpress.com/2012/07/10/configuring-and-accessing-mysql-jdbc-driver-on-android-application/
 * http://www.devpia.com/MAEUL/Contents/Detail.aspx?BoardID=19&MAEULNO=8&no=7539
 * http://blog.naver.com/skywood1/100204445016
 * */

import java.sql.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import prog.project.progclient2014.db.*;

public class MainActivity extends Activity {

	private EditText txt_id=null;
    private EditText txt_pw=null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prog_main);

        //������ �������� ������ ���� ���� ����.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(View v) {
				loginCheck();
			}
        
        });
        
        
        Button btn_join = (Button)findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(MainActivity.this, Join.class);
				startActivity(intent);
			}
        	
        });
        
        //loginCheck();

    }
    
    //������ ��������
    public void loginCheck() {
    	//TextView tv = (TextView)this.findViewById(R.id.test);
    	txt_id = (EditText)this.findViewById(R.id.txt_join_id);
    	txt_pw = (EditText)this.findViewById(R.id.txt_pw);
    	
    	String id = txt_id.getText().toString();
    	String pw = txt_pw.getText().toString();
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
    	BasicThings conn_inform = new BasicThings();
    	
    	//DB�������� ����.
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(conn_inform.url, conn_inform.user, conn_inform.pass);
            
            String name="";

            stmt = conn.prepareStatement("select count(*),name from account where id=? and pw=?");
            stmt.setString(1, id);
            stmt.setString(2, pw);
            rs = stmt.executeQuery();

            int login_result = 0;
            
            //ResultSetMetaData rsmd = rs.getMetaData();
			
            while(rs.next()) {
            	login_result = rs.getInt(1);
            	name = rs.getString(2);
            }
            
            if(login_result <= 0)
            {
            	//Login Fail
            	new AlertDialog.Builder(this)
            	.setTitle("Login Fail")
            	.setMessage("로그인 실패 ")
            	.setPositiveButton("확인 ", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();
            }
            else
            {
            	//Login Success
            	/*
            	new AlertDialog.Builder(this)
            	.setTitle("Login Success")
            	.setMessage("������ ����")
            	.setPositiveButton("����", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();*/
            
            	
            	Intent intent = new Intent(MainActivity.this, AlarmView.class);
				intent.putExtra("id",id);
				intent.putExtra("name",name);
            	startActivity(intent);
			
            	
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
            //tv.setText(e.toString());
            new AlertDialog.Builder(this)
        	.setTitle("Error")
        	.setMessage("접속 실패. 다시 시도하세요 ")
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
