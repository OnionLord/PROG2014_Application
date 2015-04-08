package prog.project.progclient2014.db;

public class BasicThings {
	
	
	//Server Addresses may be changed.
	
	//DB Tables
	//Just 2Table. They're simple.
	//You must make the schema 'prog2014'
	
	//1st table 
	//account
	// id(primary) : Varchar(20), Not Null
	// pw          : varchar(20), not null
	// name        : varchar(100)
	
	//2nd table
	//alarminfo
	// no(primary) : int, not null
	// id          : varchar(20), not null
	// alarmtime   : timestamp
	
	
	//MySQL DB Server
	//public static final String url = "jdbc:mysql://192.168.0.9:3306/prog2014";
	public static final String url = "jdbc:mysql://192.168.0.9:3306/prog2014";
    public static final String user = "prog2014";
    public static final String pass = "prog2014";
    
    
    //Processing Server
    public static final String kinect_url = "192.168.0.9";
    public static final int kinect_port = 7777;
}
