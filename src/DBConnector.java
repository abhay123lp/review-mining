import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnector {
	
	DBConnector(){}
	
	private Connection conn = null;
	private ResultSet rs;
	
	private void connect(){
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "product_review";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root"; 
		String password = "123456";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName, userName, password);
			System.out.println("Connected MySQL Database");
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void disconnect(){
		try {
			System.out.println("Disconnected from Database");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sql(String query){
		connect();
		try {
			Statement st = conn.createStatement();
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
	}

	public void getRowsFromReview(String[] columnNames, String where){
		sql("SELECT * FROM reviews_review WHERE "+where);
		for(String columnname: columnNames){
		}
		
	}
	
	public void getRowsFromReviewer(){
		sql("SELECT * FROM reviews_reviewer");
	}
	
	public void getRowsFromStore(){
		sql("SELECT * FROM reviews_store");
	}
	
}
