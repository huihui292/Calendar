//Import declerations
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	private static Statement s;
	private static String sql;
	private static String connectionString ="jdbc:hsqldb:testdb,sa,";
	private static Connection connection = null; 
	private static int id = 1; //used to give each Event object a unique ID
	
	//HELPER FUNCTIONS
	static void establishConnection(){
		//HELPER METHOD TO CONNECT TO DATABASE
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	static boolean doesEventsDataExist() throws SQLException{
		//HELPER METHOD TO CHECK IF THE EVENTS TABLE EXISTS IN THE DATABASE
		DatabaseMetaData md = connection.getMetaData();
		String EVENTS ="EVENTS";
		ResultSet rs = md.getTables(null, null, EVENTS, null);
		return rs.next();
	}
	
	static boolean doesTypeDataExist() throws SQLException{
		//HELPER METHOD TO CHECK IF THE TYPE TABLE EXISTS IN THE DATABASE
		DatabaseMetaData md = connection.getMetaData();
		String TYPE ="TYPE";
		ResultSet rs = md.getTables(null, null, TYPE, null);
		return rs.next();
	}
	
	static void createDatabase(){
		//CREATE TABLE FOR EVENTS AND TYPES
		sql = "CREATE TABLE EVENTS(EVENTNAME VARCHAR(24), EVENTDATE DATE, START INT, END INT, TYPE INT, LOCATION VARCHAR(24), DESCRIPTION VARCHAR(100), ID INT);"
				+"CREATE TABLE TYPE(ID INT, TYPES VARCHAR(24));";
		try {
			s = connection.createStatement();
			s.executeQuery(sql);	
		} catch (SQLException e) {
			System.out.println("Connection Was Not Made");
			e.printStackTrace();	
		}
	}
	
	static void closeConnection(){
		//HELPER FUNCTION TO CLOSE THE CONNECTION TO THE DATABASE
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void fillTypeTable(){
		//FILL TYPE TABLE
		sql = "DELETE FROM TYPE";
		try{
			s = connection.createStatement();
			s.executeQuery(sql);
		}
		catch(SQLException e){
			e.printStackTrace();		
		}
		sql = "INSERT INTO TYPE(ID, TYPES) VALUES('0', 'Holiday');"
			 +"INSERT INTO TYPE(ID, TYPES) VALUES('1', 'Concert');"
			 +"INSERT INTO TYPE(ID, TYPES) VALUES('2', 'Meeting/Appointment');"
			 +"INSERT INTO TYPE(ID, TYPES) VALUES('3', 'Birthday');"
			 +"INSERT INTO TYPE(ID, TYPES) VALUES('4', 'Exam');"
			 +"INSERT INTO TYPE(ID, TYPES) VALUES('5', 'Other');";
		try{
			s = connection.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}	

	@SuppressWarnings("unused")
	private static boolean isTypeEmpty(){
		//HELPER METHOD TO CHECK IF THE TYPE TABLE HAS BEEN FILLED
		//USED SO THAT WE DON'T HAVE TO FILL THE TYPE TABLE EVERYTIME WE COMPILE
		boolean isEmpty = false;
		sql = "SELECT * FROM TYPE;";
		try{
			s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			//IF THE RESULTSET IS EMPTY, TRUE
			if(!rs.next())
				isEmpty = true;	
		}catch(SQLException e){
			e.printStackTrace();
		}
		return isEmpty;
	}
	
	//METHODS TO BE USED OUTSIDE OF THE CLASS
	public static void insertEvent(Event event){
		//METHOD TO INSERT AN EVENT INTO THE EVENTS TABLE
		establishConnection();
		//STEP1: RETRIEVE INFORMATION ABOUT THE EVENT AND STORE THE DATA INTO A STRING VARIABLES 
		//IN ORDER TO USE THESE IN THE SQL STATEMENT
		//STRINGS THAT ARE FINAL MUST NOT BE CHANGED
			final String TYPE = Integer.toString(event.getType());
			final String NAME = event.getName();
			//THE DAY AND MONTH VARIABLES, 
			String DAY = Integer.toString(event.getDate());
			if(event.getDate() < 10)
				DAY = "0"+DAY;
			String MONTH = Integer.toString(event.getMonth());
			if(event.getMonth() < 10)
				MONTH = "0"+MONTH;
			final String YEAR = Integer.toString(event.getYear());
			final String LOCATION = event.getLocation();
			final String START = Integer.toString(event.getStart());
			final String END = Integer.toString(event.getEnd());
			final String DESCRIPTION = event.getDescription();
		
			//STEP2: PARSE YEAR-MONTH-DAY IN ORDER TO INSERT SQL DATE FORMAT (YYYY-MM-DD)
			final String DATE = YEAR+"-"+MONTH+"-"+DAY;
			//ASSIGN THE EVENT OBJECT ITS UNIQUE ID
			event.eventId = id;
			//CAST THE EVENT OBJECT'S ID INTO STRING TO INSERT INTO SQL STATEMENT
			final String ID =  Integer.toString(event.eventId); //give the event object a unique id
		
	    sql = "INSERT INTO EVENTS(EVENTNAME, EVENTDATE, START, END, TYPE, LOCATION, DESCRIPTION, ID) VALUES('"+NAME+"', '"+DATE+"', '"+START+"', '"+END+"', '"+TYPE+"', '"+LOCATION+"', '"+DESCRIPTION+"', '"+ID+"');";
	        try{
	    	  s = connection.createStatement();
	    	  s.executeQuery(sql);
		   }catch (SQLException e) {
			  e.printStackTrace();
		   }
	    //ADD 1 TO THE STATIC ID IN ORDER FOR THE NEXT EVENT OBJECT TO HAVE A UNIQUE ID
		id++;
		closeConnection();	
	}
	
	public static ArrayList<Event> getAllByType(int type){
		//METHOD TO GET ALL EVENTS UNDER THE SAME TYPE	
		establishConnection();
		//CREATE ARRAY TO STORE SIMILAR EVENTS
		ArrayList<Event> results = new ArrayList<Event>();
		//CAST TYPE INTO A STRING FOR SQL STATEMENT
		String TYPE = Integer.toString(type);
		
		sql = "SELECT * FROM EVENTS WHERE TYPE="+TYPE+";";
		try{
			s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			//LOOP THROUGH EVERY RESULT SET ROW 
			while(rs.next()){
				//FORMAT THE SQL DATE BACK INTO ITS THREE SEPARATE COMPONENENTS: YEAR/MONTH/DAY
				//VARIABLES THAT ARE FINAL MUST NOT BE CHANGED
				final String DATE = rs.getString(2); //GETS THE DATE COLUMN OF THE TABLE
				//PLACE CORRESONDING INDEXES to YYYY-MM-DD format
				//                   indexes:   0123456789
				final String YEAR = Character.toString(DATE.charAt(0))+Character.toString(DATE.charAt(1))
									+Character.toString(DATE.charAt(2))+Character.toString(DATE.charAt(3));
				final String MONTH = Character.toString(DATE.charAt(5))+Character.toString(DATE.charAt(6));
				final String DAY = Character.toString(DATE.charAt(8))+Character.toString(DATE.charAt(9));
				//CREATE EVENT OBJECT AND ASSIGN IT THE INFORMATION THAT IS PRESENT IN THE TABLE
				//EX: getString(1) gets the title of the event, geString(3) gets the start time and
				//so on
				final Event current = new Event(rs.getString(1), Integer.parseInt(YEAR), 
						Integer.parseInt(MONTH), Integer.parseInt(DAY), 
						Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)), 
						Integer.parseInt(rs.getString(5)), rs.getString(6), rs.getString(7), 
						Integer.parseInt(rs.getString(8)));
				//ADD THIS EVENT OBJECT TO THE RESULTS ARRAY
				results.add(current);
			}	
		} catch (SQLException e) {
				e.printStackTrace();
		}
		closeConnection();
		//RETURN THE RESULTS ARRAY
		return results;
	}
		
	public static String getNameOfType(int type){
		//METHOD TO GET THE NAME OF THE TYPE ITSELF
		establishConnection();
		//CAST THE TYPE TO STRING FOR SQL STATEMENT
		final String TYPE = Integer.toString(type);
		String result = ""; //WHERE THE NAME WILL BE STORED
		sql = "SELECT TYPES FROM TYPE WHERE ID="+TYPE+";";
		try{
			s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			//PLACE THE CURSOR ON THE FIRST ROW OF THE RESULT SET
			while(rs.next()){
				result = rs.getString(1); //ASSIGN THE NAME OF THE TYPE TO RESULT
			}
		}
		catch(SQLException e){
			e.printStackTrace();		
		}
		closeConnection();
		//RETURN THE RESULT STRING
		return result;
	}

	public static ArrayList<Event> getByLocation(String location){
		//METHOD TO RETRIEVE ALL THE EVENTS IN THE SIMILAR LOCATION
		establishConnection();
		ArrayList<Event> results = new ArrayList<Event>();
		sql = "SELECT * FROM EVENTS WHERE LOCATION='"+location+"';";
		try{
			s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			//LOOP THROUGH EVERY RESULT SET ROW 
			while(rs.next()){
				//FORMAT THE SQL DATE BACK INTO ITS THREE SEPARATE COMPONENENTS: YEAR/MONTH/DAY
				//VARIABLES THAT ARE FINAL MUST NOT BE CHANGED
				final String DATE = rs.getString(2); //GETS THE DATE COLUMN OF THE TABLE
				//PLACE CORRESONDING INDEXES to YYYY-MM-DD format
				//                   indexes:   0123456789
				final String YEAR = Character.toString(DATE.charAt(0))+Character.toString(DATE.charAt(1))
									+Character.toString(DATE.charAt(2))+Character.toString(DATE.charAt(3));
				final String MONTH = Character.toString(DATE.charAt(5))+Character.toString(DATE.charAt(6));
				final String DAY = Character.toString(DATE.charAt(8))+Character.toString(DATE.charAt(9));
				//CREATE EVENT OBJECT AND ASSIGN IT THE INFORMATION THAT IS PRESENT IN THE TABLE
				//EX: getString(1) gets the title of the event, geString(3) gets the start time and
				//so on
				final Event current = new Event(rs.getString(1), Integer.parseInt(YEAR), 
						Integer.parseInt(MONTH), Integer.parseInt(DAY), 
						Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)), 
						Integer.parseInt(rs.getString(5)), rs.getString(6), rs.getString(7), 
						Integer.parseInt(rs.getString(8)));
				//ADD THIS EVENT OBJECT TO THE RESULTS ARRAY
				results.add(current);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		closeConnection();
		return results;
	}
	
	public static ArrayList<Event> getAllEvents(){
		//ESTABLISH CONNECTION
		establishConnection();
		//RETRIEVE EVENTS BY TYPE
		ArrayList<Event> results = new ArrayList<Event>();
		sql = "SELECT * FROM EVENTS;";
		try{
			s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()){
				//PARSE YEAR/MONTH/DAY AND PLACE IN SEPARATE STRING VARIABLES
				final String DATE = rs.getString(2); //GETS THE DATE COLUMN
				//PLACE CORRESPNDING VALUES to YYYY-MM-DD format
				//                  indexes    0123456789
				final String YEAR = Character.toString(DATE.charAt(0))+Character.toString(DATE.charAt(1))
									+Character.toString(DATE.charAt(2))+Character.toString(DATE.charAt(3));
				final String MONTH = Character.toString(DATE.charAt(5))+Character.toString(DATE.charAt(6));
				final String DAY = Character.toString(DATE.charAt(8))+Character.toString(DATE.charAt(9));
				//CREATE EVENT OBJECT WITH ALL THE RELEVANT INFORMATION
				Event current = new Event(rs.getString(1), Integer.parseInt(YEAR), 
						Integer.parseInt(MONTH), Integer.parseInt(DAY), 
						Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)), 
						Integer.parseInt(rs.getString(5)), rs.getString(6), rs.getString(7), 
						Integer.parseInt(rs.getString(8)));
				//PLACE THIS EVENT OBJECT INTO RESULTS ARRAY
				results.add(current);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}		
		closeConnection();
		//RETURN RESULTS ARRAY
		return results;
	}

	public static ArrayList<Event> getByDate(String year, String month, String day){
		//METHOD TO RETRIEVE EVENTS UNDER THE SAME DAY
		establishConnection();
		//CREATE ARRAY TO PLACE ALL EVENTS UNDER THE SAME DAY; TO BE RETURNED
		ArrayList<Event> results = new ArrayList<Event>();
		final String date = year+"-"+month+"-"+day;
		sql = "SELECT * FROM EVENTS WHERE EVENTDATE='"+date+"';";
		try{
			s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			//LOOP THROUGH EVERY RESULT SET ROW 
			while(rs.next()){
				//FORMAT THE SQL DATE BACK INTO ITS THREE SEPARATE COMPONENENTS: YEAR/MONTH/DAY
				//VARIABLES THAT ARE FINAL MUST NOT BE CHANGED
				final String DATE = rs.getString(2); //GETS THE DATE COLUMN OF THE TABLE
				//PLACE CORRESONDING INDEXES to YYYY-MM-DD format
				//                   indexes:   0123456789
				final String YEAR = Character.toString(DATE.charAt(0))+Character.toString(DATE.charAt(1))
									+Character.toString(DATE.charAt(2))+Character.toString(DATE.charAt(3));
				final String MONTH = Character.toString(DATE.charAt(5))+Character.toString(DATE.charAt(6));
				final String DAY = Character.toString(DATE.charAt(8))+Character.toString(DATE.charAt(9));
				//CREATE EVENT OBJECT AND ASSIGN IT THE INFORMATION THAT IS PRESENT IN THE TABLE
				//EX: getString(1) gets the title of the event, geString(3) gets the start time and
				//so on
				final Event current = new Event(rs.getString(1), Integer.parseInt(YEAR), 
						Integer.parseInt(MONTH), Integer.parseInt(DAY), 
						Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)), 
						Integer.parseInt(rs.getString(5)), rs.getString(6), rs.getString(7), 
						Integer.parseInt(rs.getString(8)));
				//ADD THIS EVENT OBJECT TO THE RESULTS ARRAY
				results.add(current);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		//RETURN THE ARRAY
		closeConnection();
 		return results;			
	}

	public static ArrayList<Event> getByName(String name){
		//METHOD TO RETRIEVE EVENTS WITH THE SAME NAME
		establishConnection();
		ArrayList<Event> results = new ArrayList<Event>();
		sql = "SELECT * FROM EVENTS WHERE EVENTNAME='"+name+"';";
		try{
			s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()){
				//System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6)+" "+rs.getString(7)+" "+rs.getString(8));
				
				//PARSE YEAR/MONTH/DAY AND PLACE IN SEPARATE STRING VARIABLES
				final String DATE = rs.getString(2); //GETS THE DATE COLUMN
				//PLACE CORRESPNDING VALUES to YYYY-MM-DD format
				//                  indexes    0123456789
				final String YEAR = Character.toString(DATE.charAt(0))+Character.toString(DATE.charAt(1))
									+Character.toString(DATE.charAt(2))+Character.toString(DATE.charAt(3));
				final String MONTH = Character.toString(DATE.charAt(5))+Character.toString(DATE.charAt(6));
				final String DAY = Character.toString(DATE.charAt(8))+Character.toString(DATE.charAt(9));
				//CREATE EVENT OBJECT WITH ALL THE RELEVANT INFORMATION
				Event current = new Event(rs.getString(1), Integer.parseInt(YEAR), 
						Integer.parseInt(MONTH), Integer.parseInt(DAY), 
						Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)), 
						Integer.parseInt(rs.getString(5)), rs.getString(6), rs.getString(7), 
						Integer.parseInt(rs.getString(8)));
				//PLACE THIS EVENT OBJECT INTO RESULTS ARRAY
				results.add(current);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		closeConnection();
		return results;
	}
	
	public static void deleteAllEvents(){
		//METHOD TO DELETE ALL EVENTS FROM TABLE
		establishConnection();
		sql = "DELETE FROM EVENTS;";
		try {
			s = connection.createStatement();
			s.executeQuery(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		closeConnection();
	}
	
	public static void deleteEvent(String id){
		//METHOD TO DELETE SINGLE EVENT FROM THE EVENTS TABLE
		establishConnection();
		//CAST ID INTO STRING
		final String ID = id;
		sql = "DELETE FROM EVENTS WHERE ID='"+ID+"';";
		try {
			s = connection.createStatement();
			s.executeQuery(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		closeConnection();
	}
	
	public static void deleteDatabase(){
		//DELETES THE EVENTS AND TYPE TABLES
		establishConnection();
		sql = "DROP TABLE EVENTS;"+"DROP TABLE TYPE;";
		try{
			s = connection.createStatement();
			s.executeQuery(sql);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		closeConnection();
	}
	
}
	
