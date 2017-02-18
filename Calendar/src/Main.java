import java.awt.EventQueue;
import java.sql.SQLException;
public class Main {
	
	public static void main(String[] args) {
		// Establish database connection
		Database.establishConnection();
		//check if both tables already exist in the database, if not, create them
		try{
			if(!Database.doesEventsDataExist() && !Database.doesTypeDataExist()){
				Database.createDatabase();
				//fill preset names for types in the type table				
				
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		Database.fillTypeTable();
		Database.closeConnection();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				App window = new App();
			}
		});
		
	}
}