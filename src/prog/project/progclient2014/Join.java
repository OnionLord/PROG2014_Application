package prog.project.progclient2014;

import java.sql.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import prog.project.progclient2014.db.*;

public class Join extends Activity {
	
	private String id;
	private String pw;
	private String pwc;
	private String name;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prog_join);
	    // TODO Auto-generated method stub

        Button btn_join = (Button)findViewById(R.id.btn_join_join);
        btn_join.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(View v) {
				
				

				Join_Process();
			}
        	
        });
        
        Button btn_back = (Button)findViewById(R.id.btn_join_back);
        btn_back.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(View v) {
				//�������� ���ư����� finish.
				finish();
			}
        	
        });
        
	}
	
	private void Join_Process()
	{
		EditText txt_id = (EditText)findViewById(R.id.txt_join_id);
		EditText txt_pw = (EditText)findViewById(R.id.txt_join_pw);
		EditText txt_pwc = (EditText)findViewById(R.id.txt_join_pwc);
		EditText txt_name = (EditText)findViewById(R.id.txt_join_name);
		
		id = txt_id.getText().toString();
		pw = txt_pw.getText().toString();
		pwc = txt_pwc.getText().toString();
		name = txt_name.getText().toString();
		
		if(pw.compareTo(pwc) != 0)
		{
			new AlertDialog.Builder(this)
        	.setTitle("Fail Join")
        	.setMessage("Password is not equal  to Confirm Password.")
        	.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
		else if(id.length() <= 0)
		{
			new AlertDialog.Builder(this)
        	.setTitle("Fail Join")
        	.setMessage("ID is too short(more than 5 characters)")
        	.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
		else if(pw.length() <= 0)
		{
			new AlertDialog.Builder(this)
        	.setTitle("Fail Join")
        	.setMessage("Password is too short(more than 5 characters)")
        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
		else if(name.length() <= 0)
		{
			new AlertDialog.Builder(this)
        	.setTitle("Fail Join")
        	.setMessage("Name is too short(more than 5 characters)")
        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
		else
		{
		
			String message = "ID : " + id + "\n" + "Name : " + name + "\n" + "Is there all right?";
	
			new AlertDialog.Builder(this)
			.setTitle("Join")
			.setMessage(message)
			.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
			{
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
				
			})
			.setNegativeButton("OK",new DialogInterface.OnClickListener() {
				
	
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					Insert_DB();
				}
			}).show();
		}
	}
	
	private void Insert_DB()
	{
		Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	BasicThings conn_inform = new BasicThings();
    	
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(conn_inform.url, conn_inform.user, conn_inform.pass);
            
            
            stmt = conn.prepareStatement("insert into account values(?,?,?)");
            stmt.setString(1, id);
            stmt.setString(2, pw);
            stmt.setString(3, name);
            stmt.executeUpdate();
            
            new AlertDialog.Builder(this)
        	.setTitle("Join Complete")
        	.setMessage("Account is created. Please Login")
        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					
				}
			}).show();
            
        }
        catch(Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
        	.setTitle("Error")
        	.setMessage("Connect is wrong. Please try again.")
        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
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


}
