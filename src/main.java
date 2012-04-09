import java.util.ArrayList;
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
		
		//DBConnector db = new DBConnector();
		//db.sql("select * from reviews_review limit 2");
		
		//DAO dao = new DAO();
		//ArrayList<Review> reviews = dao.getReviews(404, 0, 100, 1);
		testTagging();
	}
	public static void testTagging(){
		// test Tagging 
		Tagging t = new Tagging() ;
		
		/*try {
			t.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		t.getTagging("I had a cup of tea today. Can I have another cup of tea?", "");
	}
}
