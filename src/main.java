import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 */

/**
 * @author shaoxinjiang
 *
 */
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello,world!");
		DBConnector db = new DBConnector();
		db.sql("select * from reviews_review limit 2");
	}

}
